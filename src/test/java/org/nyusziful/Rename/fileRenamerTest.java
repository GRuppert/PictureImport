/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nyusziful.Rename;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
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
    static class TestData {
        String filename;
        meta meta;

        public TestData(String filename, meta meta) {
            this.filename = filename;
            this.meta = meta;
        }
    }
    private static final TestData[] TESTS = new TestData[] {
        new TestData(
            "K2016_11!0_4@15_1_0_38(+0100)(Fri)-XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX-0-20160627_173440.jpg",
            new meta("20160627_173440.jpg", ZonedDateTime.of(2016, 11, 04, 15, 10, 38, 00, ZoneId.of("+0100")), true, "", "", "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", "", "", "0")
        )
    };
    String filename;
    meta meta;
    
    @Parameters    
    public static List<TestData> data() {
        return Arrays.asList(TESTS);
    }    
    
    
    public fileRenamerTest(TestData data) {
        super();
        this.filename = data.filename;
        this.meta = data.meta;
    }

    
    /**
     * Test of getFileName method, of class fileRenamer.
     */
    @Test
    public void testGetFileName() {
        System.out.println("getFileName");
        String ver = "";
        String pictureSet = "";
        String originalName = "";
        ZonedDateTime date = null;
        String iID = "";
        String dID = "";
        String original = "";
        String expResult = "";
        String result = fileRenamer.getFileName(ver, pictureSet, originalName, date, iID, dID, original);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
