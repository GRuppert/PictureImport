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
public class fileVersionTest {
    static class TestData {
        String filename;
        meta expResult;

        public TestData(String filename, meta expResult) {
            this.filename = filename;
            this.expResult = expResult;
        }
    }
    private static final TestData[] TESTS = new TestData[] {
        new TestData(
            "K2016_11!0_4@15_1_0_38(+0100)(Fri)-XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX-0-20160627_173440.jpg",
            new meta("20160627_173440.jpg", ZonedDateTime.of(2016, 11, 04, 15, 10, 38, 00, ZoneId.of("+0100")), true, "", "", "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", "", "", "0")
        )
    };
    String filename;
    meta expResult;
    
    @Parameters    
    public static List<TestData> data() {
        return Arrays.asList(TESTS);
    }    
    
    
    public fileVersionTest(TestData data) {
        super();
        this.filename = data.filename;
        this.expResult = data.expResult;
    }
    
    
    /**
     * Test of getV method, of class fileRenamer.
     */
    @Test
    public void testGetV() {
        meta result = fileRenamer.getV(filename);
        assertEquals("Meta of file(" + filename + ") result: " + result + " awaited: " + expResult, expResult, result);
    }
}
