package org.nyusziful.pictureorganizer.Service.ExifUtils.History;

import com.adobe.internal.xmp.XMPException;
import com.adobe.internal.xmp.XMPMeta;
import com.adobe.internal.xmp.XMPMetaFactory;
import com.adobe.internal.xmp.XMPProperty;
import com.adobe.internal.xmp.options.PropertyOptions;
import com.adobe.internal.xmp.options.SerializeOptions;
import com.drew.imaging.ImageProcessingException;
import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.tiff.TiffMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.xmp.XmpDirectory;
import org.apache.commons.io.FilenameUtils;
import org.nyusziful.pictureorganizer.Service.ExifUtils.ExifReadWriteET;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Appends and reads {@link XmpHistoryEntry} records in a file's {@code porg:History} XMP bag.
 *
 * <p>History is stored in a dedicated {@code porg:History} bag (not {@code xmpMM:History},
 * which has a fixed {@code stEvt:} schema). Each {@code rdf:li} is a compact JSON object.
 *
 * <p>Write strategy: the updated {@link XMPMeta} is serialised to a temporary {@code .xmp}
 * sidecar, then merged back into the target file via exiftool
 * ({@code -tagsfromfile temp.xmp -xmp:all -overwrite_original}). This reuses the proven
 * exiftool write path and avoids raw JPEG APP1 byte surgery.
 *
 * <p><strong>Caller contract:</strong>
 * <pre>
 *   String prevHash = readCurrentCanonicalHash(file);  // null OK on first import
 *   MetadataChange mc = new MetadataChange(prevHash, deltas);
 *   XmpHistoryEntry entry = XmpHistoryEntry.forMetadataChange(ZonedDateTime.now(), SOFTWARE_AGENT, mc);
 *   XmpHistoryWriter.append(file, entry);
 *   // CanonicalHashService.recompute(file);  ← Phase 1d responsibility, not ours
 * </pre>
 * This class is DB-ignorant. Callers own {@code MediaFileVersion} creation, canonical hash
 * recompute, and archiving pruned entries to the DB before calling {@code append()}.
 */
public class XmpHistoryWriter {

    /** Software agent string written into every history entry. */
    public static final String SOFTWARE_AGENT = "PictureOrganizer/2.0";

    /** XMP namespace URI for {@code porg:} fields. */
    static final String PORG_NAMESPACE_URI = "http://ns.pictureorganizer.nyusziful.org/1.0/";
    static final String PORG_PREFIX        = "porg";

    /**
     * Maximum number of history entries kept inline in the file's XMP.
     * Older entries beyond this limit are pruned from the in-memory XMPMeta before writing.
     * The caller is responsible for archiving pruned entries to the DB first.
     */
    static final int MAX_INLINE_ENTRIES = 50;

    private XmpHistoryWriter() {}

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /**
     * Appends one history entry to the target file's {@code porg:History} XMP bag.
     *
     * <p>For JPEG and RAW files the update is performed via exiftool. For {@code .xmp} sidecar
     * files the sidecar XML is rewritten directly.
     *
     * @param file  the JPEG, RAW, video, or sidecar ({@code .xmp}) file to update
     * @param entry the history entry to append
     * @throws XmpHistoryWriterException on parse failure, write failure, or I/O error
     */
    public static void append(File file, XmpHistoryEntry entry) throws XmpHistoryWriterException {
        try {
            XMPMeta xmpMeta = readXmpMeta(file);
            registerPorgNamespace();

            PropertyOptions bagOptions = new PropertyOptions().setArray(true).setArrayUnordered(true);
            try {
                xmpMeta.appendArrayItem(PORG_NAMESPACE_URI, "History", bagOptions,
                        toXmpRdfLi(entry), null);
            } catch (XMPException e) {
                throw new XmpHistoryWriterException("Failed to append history entry to XMPMeta", e);
            }

            pruneOldEntries(xmpMeta, MAX_INLINE_ENTRIES);
            writeXmpToFile(xmpMeta, file);

        } catch (XmpHistoryWriterException e) {
            throw e;
        } catch (Exception e) {
            throw new XmpHistoryWriterException("Failed to write XMP history to " + file.getName(), e);
        }
    }

    /**
     * Returns all history entries currently stored in the file's {@code porg:History} bag,
     * in insertion order. Returns an empty list if the bag does not yet exist.
     */
    public static List<XmpHistoryEntry> readHistory(File file) throws XmpHistoryWriterException {
        try {
            XMPMeta xmpMeta = readXmpMeta(file);
            registerPorgNamespace();
            int count = xmpMeta.countArrayItems(PORG_NAMESPACE_URI, "History");
            List<XmpHistoryEntry> result = new ArrayList<>(count);
            for (int i = 1; i <= count; i++) {
                XMPProperty item = xmpMeta.getArrayItem(PORG_NAMESPACE_URI, "History", i);
                result.add(fromXmpRdfLi(item.getValue()));
            }
            return result;
        } catch (XmpHistoryWriterException e) {
            throw e;
        } catch (XMPException e) {
            throw new XmpHistoryWriterException("Failed to read XMP history from " + file.getName(), e);
        }
    }

    /**
     * Returns the byte length of the serialised XMP in a JPEG file (useful for deciding
     * whether pruning is needed before the next append). Returns {@code -1} for non-JPEG
     * files where the 64 KB APP1 limit does not apply.
     */
    public static int currentXmpByteSize(File file) throws XmpHistoryWriterException {
        String ext = FilenameUtils.getExtension(file.getName()).toLowerCase();
        if (!Arrays.asList("jpg", "jpeg").contains(ext)) return -1;
        try {
            XMPMeta xmpMeta = readXmpMeta(file);
            byte[] serialized = XMPMetaFactory.serializeToBuffer(xmpMeta,
                    new SerializeOptions().setPadding(0));
            return serialized.length;
        } catch (XMPException e) {
            throw new XmpHistoryWriterException("Failed to measure XMP size in " + file.getName(), e);
        }
    }

    // -------------------------------------------------------------------------
    // Serialization (package-private for unit tests)
    // -------------------------------------------------------------------------

    /**
     * Serialises a {@link XmpHistoryEntry} to the compact JSON string stored as one
     * {@code rdf:li} item in the {@code porg:History} bag.
     *
     * <p>Format:
     * <pre>
     * {"when":"2025-07-14T18:23:00+02:00","agent":"PictureOrganizer/2.0",
     *  "mc":{"prevHash":"b7d2...","deltas":[{"f":"DateTimeOriginal","from":"...","to":"..."}]},
     *  "mdc":{"before":"a3f9...","after":"c81b...","reason":"VIDEO_TRIM"}}
     * </pre>
     * {@code mc} / {@code mdc} keys are omitted when the respective change type is absent.
     * {@code prevHash} is omitted when null (first import).
     */
    static String toXmpRdfLi(XmpHistoryEntry entry) {
        StringBuilder sb = new StringBuilder("{");
        sb.append("\"when\":").append(JsonUtil.escape(
                entry.getWhen().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)));
        sb.append(",\"agent\":").append(JsonUtil.escape(entry.getSoftwareAgent()));

        entry.getMetadataChange().ifPresent(mc -> {
            sb.append(",\"mc\":{");
            boolean first = true;
            if (mc.getPreviousCanonicalHash() != null) {
                sb.append("\"prevHash\":").append(JsonUtil.escape(mc.getPreviousCanonicalHash()));
                first = false;
            }
            if (!first) sb.append(",");
            sb.append("\"deltas\":[");
            List<FieldDelta> deltas = mc.getDeltas();
            for (int i = 0; i < deltas.size(); i++) {
                if (i > 0) sb.append(",");
                sb.append(deltas.get(i).toJson());
            }
            sb.append("]}");
        });

        entry.getMediaDataChange().ifPresent(mdc -> {
            sb.append(",\"mdc\":{");
            sb.append("\"before\":").append(JsonUtil.escape(mdc.getContentHashBefore()));
            sb.append(",\"after\":").append(JsonUtil.escape(mdc.getContentHashAfter()));
            sb.append(",\"reason\":").append(JsonUtil.escape(mdc.getReason()));
            sb.append("}");
        });

        return sb.append('}').toString();
    }

    /**
     * Parses one {@code rdf:li} JSON string back to a {@link XmpHistoryEntry}.
     */
    static XmpHistoryEntry fromXmpRdfLi(String json) throws XmpHistoryWriterException {
        try {
            Map<String, String> top = JsonUtil.parseObject(json);

            ZonedDateTime when = ZonedDateTime.parse(
                    JsonUtil.unquote(top.get("when")), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            String agent = JsonUtil.unquote(top.get("agent"));

            MetadataChange mc = null;
            if (top.containsKey("mc")) {
                Map<String, String> mcMap = JsonUtil.parseObject(top.get("mc"));
                String prevHash = mcMap.containsKey("prevHash")
                        ? JsonUtil.unquote(mcMap.get("prevHash")) : null;
                List<String> deltaRaws = mcMap.containsKey("deltas")
                        ? JsonUtil.parseArray(mcMap.get("deltas")) : List.of();
                List<FieldDelta> deltas = new ArrayList<>(deltaRaws.size());
                for (String raw : deltaRaws) deltas.add(FieldDelta.fromJson(raw));
                mc = new MetadataChange(prevHash, deltas);
            }

            MediaDataChange mdc = null;
            if (top.containsKey("mdc")) {
                Map<String, String> mdcMap = JsonUtil.parseObject(top.get("mdc"));
                mdc = new MediaDataChange(
                        JsonUtil.unquote(mdcMap.get("before")),
                        JsonUtil.unquote(mdcMap.get("after")),
                        JsonUtil.unquote(mdcMap.get("reason")));
            }

            return new XmpHistoryEntry(when, agent, mc, mdc);
        } catch (XmpHistoryWriterException e) {
            throw e;
        } catch (Exception e) {
            throw new XmpHistoryWriterException("Failed to parse XMP history entry: " + json, e);
        }
    }

    /**
     * Removes oldest entries from the {@code porg:History} bag if the count exceeds
     * {@code maxEntries}. Entries are removed from index 1 (oldest) first.
     * The caller must archive pruned entries to the DB before calling this.
     */
    static void pruneOldEntries(XMPMeta xmpMeta, int maxEntries) {
        try {
            int count = xmpMeta.countArrayItems(PORG_NAMESPACE_URI, "History");
            while (count > maxEntries) {
                xmpMeta.deleteArrayItem(PORG_NAMESPACE_URI, "History", 1);
                count--;
            }
        } catch (XMPException e) {
            // porg:History bag does not exist yet — nothing to prune
        }
    }

    // -------------------------------------------------------------------------
    // Internal helpers
    // -------------------------------------------------------------------------

    private static void registerPorgNamespace() throws XmpHistoryWriterException {
        try {
            XMPMetaFactory.getSchemaRegistry().registerNamespace(PORG_NAMESPACE_URI, PORG_PREFIX);
        } catch (XMPException e) {
            // Namespace already registered — not an error
            if (!e.getMessage().contains("already")) {
                throw new XmpHistoryWriterException("Failed to register porg XMP namespace", e);
            }
        }
    }

    /**
     * Reads the {@link XMPMeta} from a file using metadata-extractor (for media files)
     * or direct parse (for {@code .xmp} sidecars). Returns an empty {@link XMPMeta} if
     * the file has no XMP data yet.
     */
    private static XMPMeta readXmpMeta(File file) throws XmpHistoryWriterException {
        String ext = FilenameUtils.getExtension(file.getName()).toLowerCase();
        try {
            if ("xmp".equals(ext)) {
                try (FileInputStream fis = new FileInputStream(file)) {
                    return XMPMetaFactory.parse(fis);
                }
            }
            Metadata metadata = readDrew(file, ext);
            if (metadata != null) {
                Collection<XmpDirectory> xmpDirs = metadata.getDirectoriesOfType(XmpDirectory.class);
                for (XmpDirectory dir : xmpDirs) {
                    XMPMeta xmpMeta = dir.getXMPMeta();
                    if (xmpMeta != null) return xmpMeta;
                }
            }
            return XMPMetaFactory.create();
        } catch (XMPException | IOException e) {
            throw new XmpHistoryWriterException("Cannot read XMP from " + file.getName(), e);
        }
    }

    private static Metadata readDrew(File file, String ext) throws XmpHistoryWriterException {
        try (FileInputStream fis = new FileInputStream(file)) {
            return switch (ext) {
                case "jpg", "jpeg"            -> JpegMetadataReader.readMetadata(fis);
                case "arw", "dng", "nef",
                     "tif", "tiff"            -> TiffMetadataReader.readMetadata(fis);
                default                        -> null; // unsupported for XMP read via Drew
            };
        } catch (ImageProcessingException | IOException e) {
            throw new XmpHistoryWriterException("Cannot read metadata from " + file.getName(), e);
        }
    }

    /**
     * Serialises the updated {@link XMPMeta} to a temp {@code .xmp} sidecar and merges it
     * into the target file via exiftool. Deletes the temp sidecar on completion.
     */
    private static void writeXmpToFile(XMPMeta xmpMeta, File file)
            throws XmpHistoryWriterException {
        File tempXmp = null;
        try {
            tempXmp = File.createTempFile("porg_hist_", ".xmp", file.getParentFile());
            try (OutputStream os = new FileOutputStream(tempXmp)) {
                XMPMetaFactory.serialize(xmpMeta, os, new SerializeOptions().setPadding(0));
            }

            String[] cmd = {
                "exiftool", "-overwrite_original",
                "-tagsfromfile", tempXmp.getName(),
                "-xmp:all",
                file.getName()
            };
            ExifReadWriteET.exifTool(cmd, file.getParentFile());

        } catch (XMPException | IOException e) {
            throw new XmpHistoryWriterException("Cannot write XMP to " + file.getName(), e);
        } finally {
            if (tempXmp != null) tempXmp.delete();
        }
    }
}
