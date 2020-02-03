/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nyusziful.pictureorganizer.Service.Rename;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collection;

import com.sun.javaws.exceptions.InvalidArgumentException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 *
 * @author gabor
 */
@RunWith(Parameterized.class)
public class fileRenamerTest {
    String ver;
    String pictureSet;
    String originalName;
    ZonedDateTime date;
    String iID;
    String dID;
    int original;
    String expResult;
    
    @Parameters    
    public static Collection data() {
        return Arrays.asList(new Object[][] {
            { "6", "K", "DSC0001.JPG", ZonedDateTime.of(2016, 11, 04, 16, 10, 38, 00, ZoneId.of("+0100")), null, "a3a650e9653d8c59a40571e6cf18b24d", 0, "V6_K2016-11-0_4@15-1_0-38(+0100)(Fri)-a3a650e9653d8c59a40571e6cf18b24d-0-DSC0001.JPG" },
            { "6", "K", "DSC0001.JPG", ZonedDateTime.of(2016, 11, 04, 0, 10, 38, 00, ZoneId.of("+0100")), null, "a3a650e9653d8c59a40571e6cf18b24d", 9, "V6_K2016-11-0_3@23-1_0-38(+0100)(Fri)-a3a650e9653d8c59a40571e6cf18b24d-9-DSC0001.JPG" }
        });
    }    
    
    
    public fileRenamerTest(
            String ver,
            String pictureSet,
            String originalName,
            ZonedDateTime date,
            String iID,
            String dID,
            int original,
            String expResult
    ) {
        super();
        this.ver = ver;
        this.pictureSet = pictureSet;
        this.originalName = originalName;
        this.date = date;
        this.iID = iID;
        this.dID = dID;
        this.original = original;
        this.ver = ver;
        this.expResult = expResult;
    }

    
    /**
     * Test of getFileName method, of class FileRenamer.
     */
    @Test
    public void testGetFileName() {
        System.out.println("getFileName");
        String result = null;
        try {
            result = FileNameFactory.getFileName(ver, pictureSet, originalName, date, iID, dID, original);
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        }
        assertEquals("Rename of file(" + originalName + ") result: " + result + " awaited: " + expResult, expResult, result);
    }
    
}
