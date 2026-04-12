package org.nyusziful.pictureorganizer.Service.ExifUtils;

import com.adobe.internal.xmp.XMPException;
import com.adobe.internal.xmp.XMPMeta;
import com.adobe.internal.xmp.XMPMetaFactory;
import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class XmpHistoryWriterTest {

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();

    // ── MetadataChange JSON format ───────────────────────────────────────────────

    @Test
    public void testDeltaJsonFormat_withValues() {
        MetadataChange c = new MetadataChange("DateTimeOriginal",
                "2025:01:15 14:23:00", "2025:01:15 15:23:00");
        assertEquals(
                "{\"f\":\"DateTimeOriginal\",\"from\":\"2025:01:15 14:23:00\",\"to\":\"2025:01:15 15:23:00\"}",
                c.toJson());
    }

    @Test
    public void testDeltaJsonFormat_nullFrom() {
        MetadataChange c = new MetadataChange("_contentHash", null, "abc123");
        assertEquals("{\"f\":\"_contentHash\",\"from\":null,\"to\":\"abc123\"}", c.toJson());
    }

    @Test
    public void testDeltaJsonFormat_specialCharsEscaped() {
        MetadataChange c = new MetadataChange("title", "say \"hi\"", "done\\now");
        String json = c.toJson();
        assertTrue(json.contains("\\\"hi\\\""));
        assertTrue(json.contains("done\\\\now"));
    }

    // ── XMP sidecar — create from scratch ───────────────────────────────────────

    @Test
    public void testAddEventToNewXmpSidecar_createsFile() throws IOException, XMPException {
        File xmpFile = new File(tmp.getRoot(), "test.xmp");
        assertFalse(xmpFile.exists());

        XmpHistoryWriter.addEvent(xmpFile, "PictureOrganizer/test",
                List.of(new MetadataChange("_contentHash", null, "abc123")),
                "prevhash000");

        assertTrue("XMP sidecar should have been created", xmpFile.exists());
        assertTrue("XMP sidecar should not be empty", xmpFile.length() > 0);
    }

    @Test
    public void testAddEventToNewXmpSidecar_hasOneEntryWithVersionAndTimestamp() throws IOException, XMPException {
        File xmpFile = new File(tmp.getRoot(), "test.xmp");

        XmpHistoryWriter.addEvent(xmpFile, "PictureOrganizer/test",
                new ArrayList<>(), null);

        XMPMeta xmpMeta;
        try (FileInputStream in = new FileInputStream(xmpFile)) {
            xmpMeta = XMPMetaFactory.parse(in);
        }
        assertEquals("Should have 1 history entry", 1,
                xmpMeta.countArrayItems(XmpHistoryWriter.XMPMM_NS, "History"));

        String ver = xmpMeta.getStructField(
                XmpHistoryWriter.XMPMM_NS, "History[1]",
                XmpHistoryWriter.STEVT_NS, XmpHistoryWriter.PORG_FIELD_VERSION).getValue();
        assertEquals("PictureOrganizer/test", ver);

        String when = xmpMeta.getStructField(
                XmpHistoryWriter.XMPMM_NS, "History[1]",
                XmpHistoryWriter.STEVT_NS, XmpHistoryWriter.PORG_FIELD_TIME).getValue();
        assertNotNull("Timestamp must be present", when);
        assertFalse("Timestamp must not be empty", when.isEmpty());
    }


    @Test
    public void testAddEventToNewXmpSidecar_previousCanonicalHashReadBack() throws IOException, XMPException {
        File xmpFile = new File(tmp.getRoot(), "prevhash.xmp");

        XmpHistoryWriter.addEvent(xmpFile, "PictureOrganizer/test",
                new ArrayList<>(), "deadbeef01234567");

        XMPMeta xmpMeta;
        try (FileInputStream in = new FileInputStream(xmpFile)) {
            xmpMeta = XMPMetaFactory.parse(in);
        }
        String prevHash = xmpMeta.getStructField(
                XmpHistoryWriter.XMPMM_NS, "History[1]",
                XmpHistoryWriter.PORG_NS, "previousCanonicalHash").getValue();
        assertEquals("deadbeef01234567", prevHash);
    }

    @Test
    public void testAddEventToNewXmpSidecar_changesReadBack() throws IOException, XMPException {
        File xmpFile = new File(tmp.getRoot(), "changes.xmp");

        List<MetadataChange> changes = Arrays.asList(
                new MetadataChange("DateTimeOriginal", "2025:01:15 14:00:00", "2025:01:15 15:00:00"),
                new MetadataChange("_contentHash", null, "aabbcc")
        );

        XmpHistoryWriter.addEvent(xmpFile, "PictureOrganizer/test",
                changes, null);

        XMPMeta xmpMeta;
        try (FileInputStream in = new FileInputStream(xmpFile)) {
            xmpMeta = XMPMetaFactory.parse(in);
        }
        int changesCount = xmpMeta.countArrayItems(
                XmpHistoryWriter.XMPMM_NS, "History[1]/porg:changes");
        assertEquals("Should have 2 change entries", 2, changesCount);

        String firstChange = xmpMeta.getArrayItem(
                XmpHistoryWriter.XMPMM_NS, "History[1]/porg:changes", 1).getValue();
        assertEquals(changes.get(0).toJson(), firstChange);
    }

    // ── XMP sidecar — append to existing ────────────────────────────────────────

    @Test
    public void testAddEventToExistingSidecar_twoEntriesPresent() throws IOException, XMPException {
        File xmpFile = new File(tmp.getRoot(), "append.xmp");

        XmpHistoryWriter.addEvent(xmpFile, "PictureOrganizer/test",
                List.of(new MetadataChange("_contentHash", null, "base")), "prev");
        XmpHistoryWriter.addEvent(xmpFile, "PictureOrganizer/test",
                List.of(new MetadataChange("DateTimeOriginal", "old", "new")), null);

        XMPMeta xmpMeta;
        try (FileInputStream in = new FileInputStream(xmpFile)) {
            xmpMeta = XMPMetaFactory.parse(in);
        }
        assertEquals("Should have 2 history entries", 2,
                xmpMeta.countArrayItems(XmpHistoryWriter.XMPMM_NS, "History"));

        String ver1 = xmpMeta.getStructField(
                XmpHistoryWriter.XMPMM_NS, "History[1]",
                XmpHistoryWriter.STEVT_NS, XmpHistoryWriter.PORG_FIELD_VERSION).getValue();
        String ver2 = xmpMeta.getStructField(
                XmpHistoryWriter.XMPMM_NS, "History[2]",
                XmpHistoryWriter.STEVT_NS, XmpHistoryWriter.PORG_FIELD_VERSION).getValue();
        assertEquals("PictureOrganizer/test", ver1);
        assertEquals("PictureOrganizer/test", ver2);
    }

    // ── JPG — write via exiftool ─────────────────────────────────────────────────

    @Test
    public void testAddEventToJpg_fileReadableAfterWrite() throws IOException, XMPException {
        // Uses test resource JPG; copies to tmp dir to avoid modifying the original
        String jpgResource = "20160627_173442_GT-I9195I-20160627_173443.jpg";
        java.net.URL resource = getClass().getClassLoader().getResource(jpgResource);
        if (resource == null) {
            System.out.println("Skipping testAddEventToJpg — resource not found: " + jpgResource);
            return;
        }
        File original = new File(resource.getFile());
        File copy = new File(tmp.getRoot(), jpgResource);
        FileUtils.copyFile(original, copy);

        long checksumBefore = FileUtils.checksumCRC32(original);

        XmpHistoryWriter.addEvent(copy, "PictureOrganizer/test",
                List.of(new MetadataChange("_contentHash", null, "testcontent")), null);

        // Original must be unmodified
        assertEquals("Original file must not be modified",
                checksumBefore, FileUtils.checksumCRC32(original));

        // Copy must have been written to and remain a valid file
        assertTrue("Output JPG must exist", copy.exists());
        assertTrue("Output JPG must not be empty", copy.length() > 0);
    }
}
