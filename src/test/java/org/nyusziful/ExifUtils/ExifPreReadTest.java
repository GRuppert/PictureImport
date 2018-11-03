/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nyusziful.ExifUtils;

import com.drew.imaging.ImageProcessingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 *
 * @author gabor
 */
public class ExifPreReadTest {

    /**
     * Test of readMeta method, of class ExifReadWrite.
     */
    @Test
    public void testReadMeta() throws Exception {
        System.out.println("readMeta");
        String fileName = "D5C03339_Exif231.ARW";
        ClassLoader classLoader = ExifReadWriteIMR.class.getClassLoader();
        URL resource = classLoader.getResource(fileName);
        File file = new File(resource.getFile());
        try {
            ArrayList<String[]> strings = ExifReadWriteIMR.readMeta(file);
            ArrayList<String[]> strings2 = ExifReadWriteIMR.readMetaNew(file);
            strings.stream().forEach(strings1 -> {
                if (strings2.contains(strings1)) {strings.remove(strings1); strings2.remove(strings1);}
            }
            );
            System.out.println("a");

        } catch (ImageProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList expResult = null;
        ArrayList result = ExifReadWrite.readMeta(file);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
