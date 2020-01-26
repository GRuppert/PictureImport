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
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

import org.nyusziful.pictureorganizer.DTO.Meta;

/**
 *
 * @author gabor
 */
public class ExifReadWriteTest {
    
    /**
     * Test of readFileMeta method, of class ExifReadWrite.
     */
    @Test
    public void testExifToMeta() {
        System.out.println("readFileMeta");
        String fileName = "V5_K2015-07-2_5@12-3_2-29(+0200)(Sat)-4f0f7fe2fabb83c399af967ccf860d88-47e0be579ef91106cdd6c818b2976ce2-DSC09459.ARW";
        File fileMeta = new File(this.getClass().getClassLoader().getResource(fileName).getFile());
        Meta expResult = new Meta("", ZonedDateTime.of(2015, 07, 25, 14, 32, 29, 00, ZoneId.systemDefault()), true, "ILCE-5100", null, "47e0be579ef91106cdd6c818b2976ce2", "47e0be579ef91106cdd6c818b2976ce2", "", -1);
        Meta result = ExifService.readFileMeta(new File[] {fileMeta}, ZoneId.systemDefault()).iterator().next();
        assert result.originalFilename.endsWith(fileName);
        result.originalFilename = "";
        assertEquals("Meta of file(" + fileName + ") counted: \n" + result + "\n awaited: \n" + expResult, expResult.toString(), result.toString());
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
