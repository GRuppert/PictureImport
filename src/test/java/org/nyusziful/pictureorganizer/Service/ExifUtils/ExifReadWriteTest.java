/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nyusziful.pictureorganizer.Service.ExifUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.nyusziful.pictureorganizer.DTO.Meta;
import org.nyusziful.pictureorganizer.Service.Hash.HashTest;

/**
 *
 * @author gabor
 */
@RunWith(Parameterized.class)
public class ExifReadWriteTest {
    static class TestData {
        String filename;
        Meta expMeta;

        public TestData(String filename, Meta expMeta) {
            this.filename = filename;
            this.expMeta = expMeta;
        }
    }

    private static final TestData[] TESTS = new TestData[] {
            new TestData("V5_K2015-07-2_5@12-3_2-29(+0200)(Sat)-4f0f7fe2fabb83c399af967ccf860d88-47e0be579ef91106cdd6c818b2976ce2-DSC09459.ARW",
                    new Meta(5,"", ZonedDateTime.of(2015, 07, 25, 14, 32, 29, 00, ZoneId.systemDefault()), true, "ILCE-5100", null, "47e0be579ef91106cdd6c818b2976ce2", "47e0be579ef91106cdd6c818b2976ce2", "", null, "RAW + JPEG", 1, "SONY", 0, null, 1)),
            new TestData("WP_20140703_16_51_45_Raw__highres.dng",
                    new Meta(0,"", ZonedDateTime.of(2014, 07, 03, 16, 51, 43, 00, ZoneId.systemDefault()), false, "Lumia 1020", null, null, null, null, null, null, 3, "Nokia", null, null, 1))
    };
    String filename;
    Meta expMeta;

    @Parameterized.Parameters
    public static List<TestData> data() {
        return Arrays.asList(TESTS);
    }

    public ExifReadWriteTest(TestData data) {
        super();
        this.filename = data.filename;
        this.expMeta = data.expMeta;
    }


    /**
     * Test of readFileMeta method, of class ExifReadWrite.
     */
    @Test
    public void testExifToMeta() {
        System.out.println("readFileMeta");
        File fileMeta = new File(this.getClass().getClassLoader().getResource(filename).getFile());
        Meta result = ExifService.readFileMeta(new File[] {fileMeta}, ZoneId.systemDefault()).iterator().next();
        assert result.originalFilename.endsWith(filename);
        result.originalFilename = "";
        assertEquals("Meta of file(" + filename + ") counted: \n" + result + "\n awaited: \n" + expMeta, expMeta.toString(), result.toString());
    }

    /**
     * Test of createXmp method, of class ExifReadWrite.
     */
    @Test
    public void testCreateXmp() {
        System.out.println("createXmp");
        String fileName = "V5_K2015-07-2_5@12-3_2-29(+0200)(Sat)-4f0f7fe2fabb83c399af967ccf860d88-47e0be579ef91106cdd6c818b2976ce2-DSC09459.ARW";
        File file = new File(this.getClass().getClassLoader().getResource(fileName).getFile());
        URL resource = this.getClass().getClassLoader().getResource(fileName + ".xmp");
        if (resource != null) {
            File filexmp = new File(this.getClass().getClassLoader().getResource(fileName + ".xmp").getFile());
            if (!filexmp.delete()) {
                fail("Couldn't delete previous xmp");
            }
        }
        long expResult = 2247519194L;
        long check = 1009513469L;
        long result = 0;
        long checkFile = 0;
        try {
            result = FileUtils.checksumCRC32(ExifService.createXmp(file));
            checkFile = FileUtils.checksumCRC32(file);
        } catch (IOException ex) {
            fail("IOException: " + ex.getMessage());
        }
//TODO something more flexible, different writer
             
//        assertEquals("MD5 of xmp file(" + file + ") counted: " + result + " awaited: " + expResult, expResult, result);
        assertEquals("MD5 of file(" + file + ") counted: " + checkFile + " awaited: " + check, check, checkFile);
    }

    /**
     * Test of getExif method, of class ExifReadWrite.
     */
    @Ignore
    @Test
    public void testGetExif() {        
        System.out.println("getExif");
        String[] values = null;
        File file = null;
        ArrayList<String> expResult = null;
        ArrayList<String> result = ExifService.getExif(values, file);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateExif method, of class ExifReadWrite.
     */
    @Ignore
    @Test
    public void testUpdateExif() {
        System.out.println("updateExif");
        List<String> valuePairs = null;
        File directory = null;
        ExifService.updateExif(valuePairs, directory);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of readMeta method, of class ExifReadWrite.
     */
    @Ignore
    @Test
    public void testReadMeta() throws Exception {
        System.out.println("readMeta");
        File file = null;
        ArrayList expResult = null;
        ArrayList result = ExifService.readMeta(file);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
