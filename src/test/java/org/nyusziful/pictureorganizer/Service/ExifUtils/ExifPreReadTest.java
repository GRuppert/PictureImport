/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nyusziful.pictureorganizer.Service.ExifUtils;

import com.drew.imaging.ImageProcessingException;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

/**
 *
 * @author gabor
 */
public class ExifPreReadTest {
    /**
     * Test of readMeta method, of class ExifReadWrite.
     */
    @Test
    public void testReadMeta() {
        System.out.println("readMeta");
        String fileName = "D5C03339_Exif231.ARW";
        ClassLoader classLoader = ExifReadWriteIMR.class.getClassLoader();
        URL resource = classLoader.getResource(fileName);
        File file = new File(resource.getFile());
        try {
            ArrayList<String[]> strings = ExifReadWriteIMR.readMeta(file);
            ArrayList<String[]> strings2 = ExifReadWriteIMR.readMetaNew(file);
            ArrayList<String[]> common = new ArrayList<>();
            ArrayList<String[]> common2 = new ArrayList<>();
            strings.stream().forEach(stringArray1 -> {
                strings2.stream().forEach(stringArray2 -> {
                    if (stringArray2[0].equals(stringArray1[0]) &&
                       (stringArray2[1] == null && stringArray1[1] == null) ||
                       ((stringArray2[1] != null && stringArray1[1] != null) && stringArray2[1].equals(stringArray1[1]))
                    ) {
                        common.add(stringArray1);
                        common2.add(stringArray2);
                    }
                });
            });
            strings.removeAll(common);
            strings2.removeAll(common2);
            System.out.print("Ennyi");
            assertTrue((strings.size() == 0 && strings2.size() == 0));
            return;
        } catch (ImageProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDigikamMeta() throws Exception {
        System.out.println("readMeta");
        String fileName = "20180717_183213bak.jpg";
        String fileName2 = "20180717_183213bak_digi.jpg";
        ClassLoader classLoader = ExifReadWriteIMR.class.getClassLoader();
        URL resource = classLoader.getResource(fileName);
        URL resource2 = classLoader.getResource(fileName);
        File file = new File(resource.getFile());
        File file2 = new File(resource2.getFile());
        try {
            ArrayList<String[]> strings = ExifReadWriteIMR.readMeta(file);
            ArrayList<String[]> strings2 = ExifReadWriteIMR.readMetaNew(file2);
            ArrayList<String[]> common = new ArrayList<>();
            ArrayList<String[]> common2 = new ArrayList<>();
            strings.stream().forEach(stringArray1 -> {
                strings2.stream().forEach(stringArray2 -> {
                    if (stringArray2[0].equals(stringArray1[0]) &&
                            (stringArray2[1] == null && stringArray1[1] == null) ||
                            ((stringArray2[1] != null && stringArray1[1] != null) && stringArray2[1].equals(stringArray1[1]))
                            ) {
                        common.add(stringArray1);
                        common2.add(stringArray2);
                    }
                });
            });
            strings.removeAll(common);
            strings2.removeAll(common2);
            System.out.print("Ennyi");
            assertTrue((strings.size() == 0 && strings2.size() == 0));
            return;
        } catch (ImageProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
