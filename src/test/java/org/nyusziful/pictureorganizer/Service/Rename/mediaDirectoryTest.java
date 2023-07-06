/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nyusziful.pictureorganizer.Service.Rename;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.nyusziful.pictureorganizer.Model.MediaDirectoryInstance;

import java.io.File;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 *
 * @author gabor
 */
@RunWith(Parameterized.class)
public class mediaDirectoryTest {
    static class TestData {
        String filename;
        boolean valid;
        LocalDate expFrom;
        LocalDate expTo;

        public TestData(String filename, boolean valid, LocalDate expFrom, LocalDate expTo) {
            this.filename = filename;
            this.valid = valid;
            this.expFrom = expFrom;
            this.expTo = expTo;
        }
    }
    private static final TestData[] TESTS = new TestData[] {
        new TestData(
            "2018-06-14 - 2018-07-10 Peru",
                true,
                LocalDate.of(2018, 6,14),
                LocalDate.of(2018, 7,10)
        ),
        new TestData(
                "UnsupportedFolder",
                false,
                null,
                null
        )
    };
    String filename;
    boolean valid;
    LocalDate expFrom;
    LocalDate expTo;

    @Parameters
    public static List<TestData> data() {
        return Arrays.asList(TESTS);
    }


    public mediaDirectoryTest(TestData data) {
        super();
        this.filename = data.filename;
        this.valid = data.valid;
        this.expFrom = data.expFrom;
        this.expTo = data.expTo;
    }
    
    
    /**
     * Test of getV method, of class FileRenamer.
     */
    @Test
    public void testMediaDirectory() {
        MediaDirectoryInstance result = null;
        try {
            result = new MediaDirectoryInstance(new File(filename));
        } catch (IllegalArgumentException e) {
        }
        if (valid) {
            assertEquals("Start date of folder(" + filename + ") result: " + result.getFirstDate() + " awaited: " + expFrom, expFrom, result.getFirstDate());
            assertEquals("End date of folder(" + filename + ") result: " + result.getLastDate() + " awaited: " + expTo, expTo, result.getLastDate());
        } else
            assertNull(result);
    }
}
