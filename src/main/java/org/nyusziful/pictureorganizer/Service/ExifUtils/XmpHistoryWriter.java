package org.nyusziful.pictureorganizer.Service.ExifUtils;

import com.adobe.internal.xmp.XMPException;
import com.adobe.internal.xmp.XMPMeta;
import com.adobe.internal.xmp.XMPMetaFactory;
import com.adobe.internal.xmp.XMPSchemaRegistry;
import com.adobe.internal.xmp.options.PropertyOptions;
import com.adobe.internal.xmp.options.SerializeOptions;
import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.xmp.XmpDirectory;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Appends structured history entries to XMP metadata in image files and XMP sidecars.
 *
 * History entries are written to {@code xmpMM:History} using the {@code stEvt:} event
 * namespace for standard fields and the {@code porg:} namespace for project-specific fields.
 *
 * <p>Write strategy:
 * <ul>
 *   <li>XMP sidecar ({@code .xmp}): modified in-memory with Adobe XMP Core, written directly.</li>
 *   <li>JPG and other image files: XMP extracted via metadata-extractor, modified in-memory,
 *       serialised to a temp file, then embedded back via exiftool (which handles APP1
 *       segment byte-offset management).</li>
 * </ul>
 *
 * <p>{@code contentHash} is NOT stored as a dedicated per-entry field to save space in the
 * 64 KB APP1 limit. Pass it as a synthetic {@link MetadataChange} instead:
 * <pre>
 *   // IMPORT: establish baseline
 *   new MetadataChange("_contentHash", null, contentHash)
 *   // VIDEO_TRIM: record intentional pixel change
 *   new MetadataChange("_contentHash", previousContentHash, newContentHash)
 * </pre>
 *
 * <p>{@code previousCanonicalHash}: included as a dedicated field because at IMPORT it is
 * the only record of the hash of the completely untouched original file before any tooling ran.
 */
public class XmpHistoryWriter {

    private static final Logger LOG = LoggerFactory.getLogger(XmpHistoryWriter.class);

    /** PictureOrganizer proprietary XMP namespace. "porg" = PictureOrganizer. */
    public static final String PORG_NS = "https://pictureorganizer.nyusziful.org/xmp/1.0/";
    public static final String PORG_PREFIX = "porg";
    public static final String PORG_FIELD_ACTION = "action";
    public static final String PORG_FIELD_TIME = "when";
    public static final String PORG_FIELD_VERSION = "ver";

    static final String XMPMM_NS = "http://ns.adobe.com/xap/1.0/mm/";
    static final String STEVT_NS = "http://ns.adobe.com/xap/1.0/sType/ResourceEvent#";
    static final String STEVT_PREFIX = "stEvt";

    static final String SOFTWARE_AGENT = "2.0";

    static {
        try {
            XMPSchemaRegistry registry = XMPMetaFactory.getSchemaRegistry();
            registry.registerNamespace(PORG_NS, PORG_PREFIX);
            // stEvt: is a standard Adobe namespace but may not be pre-registered in all versions
            try {
                registry.registerNamespace(STEVT_NS, STEVT_PREFIX);
            } catch (XMPException ignored) {
                // Already registered — safe to ignore
            }
        } catch (XMPException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * Appends a history event to the XMP metadata of {@code file}.
     *
     * @param file                 target file — either a {@code .xmp} sidecar or an image (JPG etc.)
     * @param ver        agent string, e.g. {@code "PictureOrganizer/2.0"}
     * @param changes              field-level deltas; include synthetic {@code _contentHash} entries
     *                             at IMPORT or VIDEO_TRIM (see class javadoc)
     * @throws IOException  if the file cannot be read or written
     * @throws XMPException if XMP parsing or serialisation fails
     */
    public static void addEvent(File file,
                                String ver,
                                List<MetadataChange> changes,
                                String previousCanonicalHash) throws IOException, XMPException {
        XMPMeta xmpMeta = readOrCreateXmpMeta(file);
        appendHistoryEntry(xmpMeta, ver, changes, previousCanonicalHash);
        writeXmpToFile(xmpMeta, file);
    }

    // ── XMPMeta read ────────────────────────────────────────────────────────────

    private static XMPMeta readOrCreateXmpMeta(File file) throws IOException, XMPException {
        if (!file.exists()) {
            return XMPMetaFactory.create();
        }
        String ext = FilenameUtils.getExtension(file.getName()).toLowerCase();
        if ("xmp".equals(ext)) {
            try (InputStream in = new FileInputStream(file)) {
                return XMPMetaFactory.parse(in);
            }
        }
        return readXmpMetaFromImage(file);
    }

    private static XMPMeta readXmpMetaFromImage(File file) {
        try {
            Collection<XmpDirectory> dirs = JpegMetadataReader
                    .readMetadata(file)
                    .getDirectoriesOfType(XmpDirectory.class);
            if (!dirs.isEmpty()) {
                XMPMeta meta = dirs.iterator().next().getXMPMeta();
                if (meta != null) return meta;
            }
        } catch (ImageProcessingException | IOException e) {
            LOG.debug("Could not read existing XMP from {}, creating new: {}", file.getName(), e.getMessage());
        }
        return XMPMetaFactory.create();
    }

    // ── History entry construction ───────────────────────────────────────────────

    private static void appendHistoryEntry(XMPMeta xmpMeta,
                                           String ver,
                                           List<MetadataChange> changes,
                                           String previousCanonicalHash) throws XMPException {
        // Append a new ordered-array item (struct) to xmpMM:History
        xmpMeta.appendArrayItem(
                XMPMM_NS,
                "History",
                new PropertyOptions().setArrayOrdered(true),
                null,
                new PropertyOptions().setStruct(true));

        int idx = xmpMeta.countArrayItems(XMPMM_NS, "History");
        String itemPath = "History[" + idx + "]";

        String timestamp = ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        xmpMeta.setStructField(XMPMM_NS, itemPath, STEVT_NS, PORG_FIELD_TIME, timestamp, null);
        xmpMeta.setStructField(XMPMM_NS, itemPath, STEVT_NS, PORG_FIELD_VERSION,
                ver != null ? ver : SOFTWARE_AGENT, null);

        if (!changes.isEmpty()) {
            xmpMeta.setStructField(XMPMM_NS, itemPath, PORG_NS, "changes", null,
                    new PropertyOptions().setArray(true));
            String changesPath = itemPath + "/porg:changes";
            for (MetadataChange change : changes) {
                xmpMeta.appendArrayItem(XMPMM_NS, changesPath, null, change.toJson(), null);
            }
        }

        if (previousCanonicalHash != null) {
            xmpMeta.setStructField(XMPMM_NS, itemPath, PORG_NS, "previousCanonicalHash",
                    previousCanonicalHash, null);
        }
    }

    // ── Write back ───────────────────────────────────────────────────────────────

    private static void writeXmpToFile(XMPMeta xmpMeta, File file) throws IOException, XMPException {
        String ext = FilenameUtils.getExtension(file.getName()).toLowerCase();
        if ("xmp".equals(ext)) {
            writeXmpSidecar(xmpMeta, file);
        } else {
            writeXmpToImageViaExifTool(xmpMeta, file);
        }
    }

    private static void writeXmpSidecar(XMPMeta xmpMeta, File file) throws IOException, XMPException {
        byte[] bytes = XMPMetaFactory.serializeToBuffer(xmpMeta,
                new SerializeOptions().setUseCompactFormat(true));
        File tmp = new File(file.getParentFile(), file.getName() + ".tmp");
        try {
            try (OutputStream out = new FileOutputStream(tmp)) {
                out.write(bytes);
            }
            Files.move(tmp.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            tmp.delete();
            throw e;
        }
    }

    private static void writeXmpToImageViaExifTool(XMPMeta xmpMeta, File file) throws IOException, XMPException {
        byte[] bytes = XMPMetaFactory.serializeToBuffer(xmpMeta,
                new SerializeOptions().setUseCompactFormat(true));

        File tmpXmp = File.createTempFile("porg_xmp_", ".xmp", file.getParentFile());
        try {
            try (OutputStream out = new FileOutputStream(tmpXmp)) {
                out.write(bytes);
            }
            List<String> cmd = new ArrayList<>();
            cmd.add("-xmp<=" + tmpXmp.getAbsolutePath());
            cmd.add(file.getName());
            ExifReadWriteET.updateExif(cmd, file.getParentFile());
        } finally {
            tmpXmp.delete();
        }
    }
}
