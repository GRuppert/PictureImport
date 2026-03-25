package org.nyusziful.pictureorganizer.Service.Hash;

import org.junit.Test;
import org.nyusziful.pictureorganizer.DTO.ImageDTO;

import java.io.File;

import static org.junit.Assert.*;

/**
 * Unit tests for hashing utilities.
 *
 * Covers the gaps in HashTest (integration) and JPGHashTest (scan only):
 *  - processHash()      — byte-array to hex string
 *  - Hasher.formatHash() — hex to XMP/UUID format
 *  - Type.getType()     — extension routing
 *  - JPEGSegment        — getMarkerText, equals, getBytesLeft
 *  - JPGHash.scan()     — synthetic byte arrays
 *  - checkIntegrity()   — file-level structural validation
 *  - checkBackupExif()  — backup EXIF detection
 *  - exifHash field     — populated for JPEGs that carry EXIF
 */
public class HashUnitTest {

    // -----------------------------------------------------------------------
    // processHash — converts raw MD5 bytes to 32-char lowercase hex string
    // -----------------------------------------------------------------------

    @Test
    public void processHash_allZeroBytes_returns32Zeros() {
        byte[] zeros = new byte[16];
        assertEquals("00000000000000000000000000000000", MediaFileHash.processHash(zeros));
    }

    @Test
    public void processHash_allFFBytes_returns32Fs() {
        byte[] ffs = new byte[16];
        for (int i = 0; i < ffs.length; i++) ffs[i] = (byte) 0xFF;
        assertEquals("ffffffffffffffffffffffffffffffff", MediaFileHash.processHash(ffs));
    }

    @Test
    public void processHash_knownMD5EmptyString() {
        // MD5("") = d41d8cd98f00b204e9800998ecf8427e
        byte[] digest = {
            (byte)0xd4, (byte)0x1d, (byte)0x8c, (byte)0xd9,
            (byte)0x8f, (byte)0x00, (byte)0xb2, (byte)0x04,
            (byte)0xe9, (byte)0x80, (byte)0x09, (byte)0x98,
            (byte)0xec, (byte)0xf8, (byte)0x42, (byte)0x7e
        };
        assertEquals("d41d8cd98f00b204e9800998ecf8427e", MediaFileHash.processHash(digest));
    }

    @Test
    public void processHash_lowNibbleOnly_leadingZeroPreserved() {
        // Byte 0x0A must produce "0a", not "a" — ensure leading zero
        byte[] input = new byte[16];
        input[0] = 0x0A;
        String result = MediaFileHash.processHash(input);
        assertEquals("0a", result.substring(0, 2));
    }

    // -----------------------------------------------------------------------
    // Hasher.formatHash — only reformats valid 32-char hex; passes rest through
    // -----------------------------------------------------------------------

    @Test
    public void formatHash_validLowercaseHex_returnsUUIDFormat() {
        String hash = "d41d8cd98f00b204e9800998ecf8427e";
        assertEquals("d41d8cd9-8f00-b204-e980-0998ecf8427e", Hasher.formatHash(hash));
    }

    @Test
    public void formatHash_validUppercaseHex_returnsUUIDFormat() {
        String hash = "D41D8CD98F00B204E9800998ECF8427E";
        assertEquals("D41D8CD9-8F00-B204-E980-0998ECF8427E", Hasher.formatHash(hash));
    }

    @Test
    public void formatHash_EMPTYHASH_returnedUnchanged() {
        // "XXX..." is not valid hex — must be returned as-is
        assertEquals(MediaFileHash.EMPTYHASH, Hasher.formatHash(MediaFileHash.EMPTYHASH));
    }

    @Test
    public void formatHash_UNKNOWN_returnedUnchanged() {
        // "ZZZ..." is not valid hex — must be returned as-is
        assertEquals(MediaFileHash.UNKNOWN, Hasher.formatHash(MediaFileHash.UNKNOWN));
    }

    @Test
    public void formatHash_tooShort_returnedUnchanged() {
        String short31 = "d41d8cd98f00b204e9800998ecf842";
        assertEquals(short31, Hasher.formatHash(short31));
    }

    @Test
    public void formatHash_tooLong_returnedUnchanged() {
        String long33 = "d41d8cd98f00b204e9800998ecf8427e0";
        assertEquals(long33, Hasher.formatHash(long33));
    }

    @Test
    public void formatHash_nonHexChars_returnedUnchanged() {
        String nonHex = "d41d8cd98f00b204e9800998ecf8427g"; // 'g' is not hex
        assertEquals(nonHex, Hasher.formatHash(nonHex));
    }

    // -----------------------------------------------------------------------
    // Type.getType — maps file extensions to enum values
    // -----------------------------------------------------------------------

    private File fakeFile(String name) {
        return new File(name);
    }

    @Test
    public void getType_jpg_returnsJPG() {
        assertEquals(MediaFileHash.Type.JPG, MediaFileHash.Type.getType(fakeFile("photo.jpg")));
    }

    @Test
    public void getType_jpeg_returnsJPG() {
        assertEquals(MediaFileHash.Type.JPG, MediaFileHash.Type.getType(fakeFile("photo.jpeg")));
    }

    @Test
    public void getType_JPG_upperCase_returnsJPG() {
        assertEquals(MediaFileHash.Type.JPG, MediaFileHash.Type.getType(fakeFile("photo.JPG")));
    }

    @Test
    public void getType_arw_returnsTIFF() {
        assertEquals(MediaFileHash.Type.TIFF, MediaFileHash.Type.getType(fakeFile("photo.arw")));
    }

    @Test
    public void getType_dng_returnsTIFF() {
        assertEquals(MediaFileHash.Type.TIFF, MediaFileHash.Type.getType(fakeFile("photo.dng")));
    }

    @Test
    public void getType_nef_returnsTIFF() {
        assertEquals(MediaFileHash.Type.TIFF, MediaFileHash.Type.getType(fakeFile("photo.nef")));
    }

    @Test
    public void getType_tif_returnsTIFF() {
        assertEquals(MediaFileHash.Type.TIFF, MediaFileHash.Type.getType(fakeFile("photo.tif")));
    }

    @Test
    public void getType_tiff_returnsTIFF() {
        assertEquals(MediaFileHash.Type.TIFF, MediaFileHash.Type.getType(fakeFile("photo.tiff")));
    }

    @Test
    public void getType_mp4_returnsMP4() {
        assertEquals(MediaFileHash.Type.MP4, MediaFileHash.Type.getType(fakeFile("video.mp4")));
    }

    @Test
    public void getType_3gp_returnsMP4() {
        assertEquals(MediaFileHash.Type.MP4, MediaFileHash.Type.getType(fakeFile("video.3gp")));
    }

    @Test
    public void getType_txt_returnsUNKNOWN() {
        assertEquals(MediaFileHash.Type.UNKNOWN, MediaFileHash.Type.getType(fakeFile("file.txt")));
    }

    @Test
    public void getType_noExtension_returnsUNKNOWN() {
        assertEquals(MediaFileHash.Type.UNKNOWN, MediaFileHash.Type.getType(fakeFile("file")));
    }

    @Test
    public void getDBName_allTypes() {
        assertEquals("jpg", MediaFileHash.Type.JPG.getDBName());
        assertEquals("tif", MediaFileHash.Type.TIFF.getDBName());
        assertEquals("mp4", MediaFileHash.Type.MP4.getDBName());
        assertEquals("n/a", MediaFileHash.Type.UNKNOWN.getDBName());
    }

    // -----------------------------------------------------------------------
    // JPEGSegment — getMarkerText, equals, getBytesLeft
    // -----------------------------------------------------------------------

    @Test
    public void getMarkerText_SOI() {
        assertTrue(JPEGSegment.getMarkerText(216).contains("Start Of Image"));
    }

    @Test
    public void getMarkerText_EOI() {
        assertTrue(JPEGSegment.getMarkerText(217).contains("End Of Image"));
    }

    @Test
    public void getMarkerText_SOS() {
        assertTrue(JPEGSegment.getMarkerText(218).contains("Start Of Scan"));
    }

    @Test
    public void getMarkerText_APP1() {
        assertTrue(JPEGSegment.getMarkerText(225).contains("Application-specific"));
    }

    @Test
    public void getMarkerText_padding() {
        assertEquals("Padding", JPEGSegment.getMarkerText(0));
    }

    @Test
    public void getMarkerText_unknown() {
        String text = JPEGSegment.getMarkerText(99);
        assertTrue(text.contains("Unknown marker"));
    }

    @Test
    public void equals_identicalSegments_returnsTrue() {
        JPEGSegment a = new JPEGSegment(100, 50, 217, "0xFFD9 End Of Image");
        JPEGSegment b = new JPEGSegment(100, 50, 217, "0xFFD9 End Of Image");
        assertEquals(a, b);
    }

    @Test
    public void equals_differentMarker_returnsFalse() {
        JPEGSegment a = new JPEGSegment(100, 50, 216);
        JPEGSegment b = new JPEGSegment(100, 50, 217);
        assertNotEquals(a, b);
    }

    @Test
    public void equals_differentLength_returnsFalse() {
        JPEGSegment a = new JPEGSegment(100, 50, 217);
        JPEGSegment b = new JPEGSegment(100, 99, 217);
        assertNotEquals(a, b);
    }

    @Test
    public void equals_differentStartAddress_returnsFalse() {
        JPEGSegment a = new JPEGSegment(100, 50, 217);
        JPEGSegment b = new JPEGSegment(200, 50, 217);
        assertNotEquals(a, b);
    }

    @Test
    public void equals_differentId_returnsFalse() {
        JPEGSegment a = new JPEGSegment(100, 50, 225, "Exif\0\0");
        JPEGSegment b = new JPEGSegment(100, 50, 225, "Backup");
        assertNotEquals(a, b);
    }

    @Test
    public void equals_null_returnsFalse() {
        JPEGSegment a = new JPEGSegment(100, 50, 217);
        assertNotEquals(a, null);
    }

    @Test
    public void getBytesLeft_freshSegment_equalsLength() {
        JPEGSegment seg = new JPEGSegment(0, 100, 217);
        assertEquals(100, seg.getBytesLeft());
    }

    @Test
    public void getBytesLeft_afterAddRead_decreases() {
        JPEGSegment seg = new JPEGSegment(0, 100, 217);
        seg.addRead(30);
        assertEquals(70, seg.getBytesLeft());
    }

    // -----------------------------------------------------------------------
    // JPGHash.scan(byte[]) — synthetic JPEG byte sequences
    // -----------------------------------------------------------------------

    @Test
    public void scan_minimalJpeg_SOI_EOI_terminatesCleanly() {
        // Minimal valid JPEG: SOI (FFD8) followed immediately by EOI (FFD9)
        byte[] minimal = {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xD9};
        JPEGMediaFileStruct result = JPGHash.scan(minimal);
        assertEquals("Reached the end of the file", result.getTerminationMessage());
    }

    @Test
    public void scan_onlySOI_terminatesAtEOF() {
        byte[] soiOnly = {(byte) 0xFF, (byte) 0xD8};
        JPEGMediaFileStruct result = JPGHash.scan(soiOnly);
        assertEquals("Reached the end of the file", result.getTerminationMessage());
    }

    @Test
    public void scan_notAJpeg_setsErrorMessage() {
        byte[] notJpeg = {0x00, 0x01, 0x02, 0x03};
        JPEGMediaFileStruct result = JPGHash.scan(notJpeg);
        assertNotNull(result.getTerminationMessage());
        assertFalse(result.getTerminationMessage().isEmpty());
    }

    @Test
    public void scan_minimalJpeg_hasSOISegment() {
        byte[] minimal = {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xD9};
        JPEGMediaFileStruct result = JPGHash.scan(minimal);
        assertTrue("Expected at least SOI segment", result.getSegmentSize() >= 1);
        assertEquals(216, result.getSegment(0).getMarker()); // 0xD8 = 216 = SOI
    }

    @Test
    public void scan_minimalJpeg_noImageData() {
        // Without a SOS segment there is no hashable image data
        byte[] minimal = {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xD9};
        JPEGMediaFileStruct result = JPGHash.scan(minimal);
        assertNull("No SOS → mainImage must be null", result.getMainImage());
    }

    // -----------------------------------------------------------------------
    // checkIntegrity — verifies segment chain covers the full file
    // -----------------------------------------------------------------------

    /** DSC08806.jpg scans completely ("Reached the end of the file") → integrity OK */
    @Test
    public void checkIntegrity_cleanJpeg_returnsTrue() {
        File file = new File(getClass().getClassLoader().getResource("DSC08806.jpg").getFile());
        assertTrue("DSC08806.jpg is a structurally intact JPEG", JPGHash.checkIntegrity(file));
    }

    /**
     * 20160627_183440...jpg terminates with "Not found marker after segment",
     * meaning the segment chain does not cover the full file.
     */
    @Test
    public void checkIntegrity_truncatedJpeg_returnsFalse() {
        File file = new File(getClass().getClassLoader()
                .getResource("20160627_183440_GT-I9195I-20160627_173440.jpg").getFile());
        assertFalse("Truncated/broken JPEG should fail integrity check", JPGHash.checkIntegrity(file));
    }

    // -----------------------------------------------------------------------
    // checkBackupExif — detects presence of duplicate "Backup" EXIF segment
    // -----------------------------------------------------------------------

    /** DSC08806_bak.jpg was produced by addBackupExif → contains the backup segment */
    @Test
    public void checkBackupExif_fileWithBackup_returnsTrue() {
        File file = new File(getClass().getClassLoader().getResource("DSC08806_bak.jpg").getFile());
        assertTrue("DSC08806_bak.jpg should contain a backup EXIF segment", JPGHash.checkBackupExif(file));
    }

    /** DSC08806.jpg is the original without a backup EXIF segment */
    @Test
    public void checkBackupExif_fileWithoutBackup_returnsFalse() {
        File file = new File(getClass().getClassLoader().getResource("DSC08806.jpg").getFile());
        assertFalse("DSC08806.jpg should not contain a backup EXIF segment", JPGHash.checkBackupExif(file));
    }

    // -----------------------------------------------------------------------
    // exifHash — populated for JPEGs that carry an APP1/Exif segment
    // -----------------------------------------------------------------------

    /**
     * A JPEG with a proper Exif APP1 segment must produce a non-empty exifHash.
     * DSC08806.jpg has Exif data and is structurally clean.
     */
    @Test
    public void exifHash_jpegWithExif_isNotEMPTYHASH() {
        File file = new File(getClass().getClassLoader().getResource("DSC08806.jpg").getFile());
        ImageDTO dto = MediaFileHash.getHash(file);
        assertNotEquals(
            "DSC08806.jpg has Exif — exifHash must be populated",
            MediaFileHash.EMPTYHASH,
            dto.exifHash
        );
    }

    /**
     * A JPEG without a recognisable Exif segment must leave exifHash as EMPTYHASH.
     * 20160627_183440...jpg terminates before a valid Exif segment is detected.
     */
    @Test
    public void exifHash_jpegWithNoExif_remainsEMPTYHASH() {
        File file = new File(getClass().getClassLoader()
                .getResource("20160627_183440_GT-I9195I-20160627_173440.jpg").getFile());
        ImageDTO dto = MediaFileHash.getHash(file);
        assertEquals(
            "File without parseable Exif must keep EMPTYHASH as exifHash",
            MediaFileHash.EMPTYHASH,
            dto.exifHash
        );
    }

    /**
     * The content hash (hash) and EXIF hash (exifHash) must always be distinct values
     * for a file that contains both image data and EXIF metadata.
     */
    @Test
    public void hashAndExifHash_areDifferent_forJpegWithBoth() {
        File file = new File(getClass().getClassLoader()
                .getResource("K2007-05-0_5@09-1_2-12(+0200)(Sat)-d41d8cd98f00b204e9800998ecf8427e-d41d8cd98f00b204e9800998ecf8427e-IMAG0017.JPG")
                .getFile());
        ImageDTO dto = MediaFileHash.getHash(file);
        assertNotEquals("Image hash and EXIF hash must not be equal", dto.hash, dto.exifHash);
    }
}
