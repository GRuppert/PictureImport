/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Rename;

import TimeShift.Stripes;
import TimeShift.TimeLine;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author gabor
 */
public class StaticToolsTest {
    
    public StaticToolsTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of exifToMeta method, of class StaticTools.
     */
    @Test
    public void testExifToMeta_File() {
        System.out.println("exifToMeta");
        File fileMeta = null;
        meta expResult = null;
        meta result = StaticTools.exifToMeta(fileMeta);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of exifToMeta method, of class StaticTools.
     */
    @Test
    public void testExifToMeta_ArrayList_File() {
        System.out.println("exifToMeta");
        ArrayList<String> filenames = null;
        File dir = null;
        List<meta> expResult = null;
        List<meta> result = StaticTools.exifToMeta(filenames, dir);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createXmp method, of class StaticTools.
     */
    @Test
    public void testCreateXmp() {
        System.out.println("createXmp");
        File file = null;
        File expResult = null;
        File result = StaticTools.createXmp(file);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getExif method, of class StaticTools.
     */
    @Test
    public void testGetExif() {
        System.out.println("getExif");
        String[] values = null;
        File file = null;
        ArrayList<String> expResult = null;
        ArrayList<String> result = StaticTools.getExif(values, file);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateExif method, of class StaticTools.
     */
    @Test
    public void testUpdateExif() {
        System.out.println("updateExif");
        List<String> valuePairs = null;
        File directory = null;
        StaticTools.updateExif(valuePairs, directory);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of exifTool method, of class StaticTools.
     */
    @Test
    public void testExifTool() {
        System.out.println("exifTool");
        String[] parameters = null;
        File directory = null;
        ArrayList<String> expResult = null;
        ArrayList<String> result = StaticTools.exifTool(parameters, directory);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of errorOut method, of class StaticTools.
     */
    @Test
    public void testErrorOut() {
        System.out.println("errorOut");
        String source = "";
        Exception e = null;
        StaticTools.errorOut(source, e);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of beep method, of class StaticTools.
     */
    @Test
    public void testBeep() {
        System.out.println("beep");
        StaticTools.beep();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of readFiles method, of class StaticTools.
     */
    @Test
    public void testReadFiles() {
        System.out.println("readFiles");
        File dir = null;
        TimeLine tl = null;
        ZoneId zone = null;
        HashMap<String, Stripes> expResult = null;
        HashMap<String, Stripes> result = StaticTools.readFiles(dir, tl, zone);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTimeFromStr method, of class StaticTools.
     */
    @Test
    public void testGetTimeFromStr() {
        System.out.println("getTimeFromStr");
        String input = "";
        ZoneId zone = null;
        ZonedDateTime expResult = null;
        ZonedDateTime result = StaticTools.getTimeFromStr(input, zone);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getZonedTimeFromStr method, of class StaticTools.
     */
    @Test
    public void testGetZonedTimeFromStr() {
        System.out.println("getZonedTimeFromStr");
        String input = "";
        ZonedDateTime expResult = null;
        ZonedDateTime result = StaticTools.getZonedTimeFromStr(input);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of startOfImageJPG method, of class StaticTools.
     */
    @Test
    public void testStartOfImageJPG() throws Exception {
        System.out.println("startOfImageJPG");
        BufferedInputStream in = null;
        long expResult = 0L;
        long result = StaticTools.startOfImageJPG(in);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of startOfScanJPG method, of class StaticTools.
     */
    @Test
    public void testStartOfScanJPG() throws Exception {
        System.out.println("startOfScanJPG");
        BufferedInputStream in = null;
        long expResult = 0L;
        long result = StaticTools.startOfScanJPG(in);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of startOfScanTiff method, of class StaticTools.
     */
    @Test
    public void testStartOfScanTiff() throws Exception {
        System.out.println("startOfScanTiff");
        File file = null;
        BufferedInputStream in = null;
        byte[] expResult = null;
        byte[] result = StaticTools.startOfScanTiff(file, in);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of readBytes method, of class StaticTools.
     */
    @Test
    public void testReadBytes() throws Exception {
        System.out.println("readBytes");
        InputStream in = null;
        int size = 0;
        byte[] expResult = null;
        byte[] result = StaticTools.readBytes(in, size);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getHash method, of class StaticTools.
     */
    @Test
    public void testGetHash_Path() throws Exception {
        System.out.println("getHash");
        Path folderPath = null;
        ArrayList<String> expResult = null;
        ArrayList<String> result = StaticTools.getHash(folderPath);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFullHashPS method, of class StaticTools.
     */
    @Test
    public void testGetFullHashPS() throws Exception {
        System.out.println("getFullHashPS");
        File file = null;
        String expResult = "";
        String result = StaticTools.getFullHashPS(file);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFullHash method, of class StaticTools.
     */
    @Test
    public void testGetFullHash() throws Exception {
        System.out.println("getFullHash");
        File file = null;
        String expResult = "";
        String result = StaticTools.getFullHash(file);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getHash method, of class StaticTools.
     */
    @Test
    public void testGetHash_File() throws Exception {
        System.out.println("getHash");
        File file = null;
        String expResult = "";
        String result = StaticTools.getHash(file);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of formatHash method, of class StaticTools.
     */
    @Test
    public void testFormatHash() {
        System.out.println("formatHash");
        String hash = "";
        String expResult = "";
        String result = StaticTools.formatHash(hash);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of copyAndBackup method, of class StaticTools.
     */
    @Test
    public void testCopyAndBackup() {
        System.out.println("copyAndBackup");
        File source = null;
        File dest = null;
        File backup = null;
        boolean expResult = false;
        boolean result = StaticTools.copyAndBackup(source, dest, backup);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDir method, of class StaticTools.
     */
    @Test
    public void testGetDir() {
        System.out.println("getDir");
        File dir = null;
        File expResult = null;
        File result = StaticTools.getDir(dir);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFile method, of class StaticTools.
     */
    @Test
    public void testGetFile() {
        System.out.println("getFile");
        File dir = null;
        File expResult = null;
        File result = StaticTools.getFile(dir);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of readMeta method, of class StaticTools.
     */
    @Test
    public void testReadMeta() throws Exception {
        System.out.println("readMeta");
        File file = null;
        ArrayList expResult = null;
        ArrayList result = StaticTools.readMeta(file);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
