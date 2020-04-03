/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nyusziful.pictureorganizer.Service.Rename;

import com.sun.javaws.exceptions.InvalidArgumentException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.nyusziful.pictureorganizer.Model.MediaDirectory;

import java.io.File;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author gabor
 */
@RunWith(Parameterized.class)
public class mediaDirectoryTest {
    static class TestData {
        String filename;
        ZonedDateTime expFrom;
        ZonedDateTime expTo;

        public TestData(String filename, ZonedDateTime expFrom, ZonedDateTime expTo) {
            this.filename = filename;
            this.expFrom = expFrom;
            this.expTo = expTo;
        }
    }
    private static final TestData[] TESTS = new TestData[] {
        new TestData(//V1
            "2018-06-14 - 2018-07-10 Peru",
                ZonedDateTime.of(2018, 5,14,0,0,0,0, ZoneId.systemDefault()),
                ZonedDateTime.of(2018, 6,10,23,59,59,999999999, ZoneId.systemDefault())
        ),
        new TestData(//V6
                "UnsupportedFolder",
                null,
                null
        )
    };
    String filename;
    ZonedDateTime expFrom;
    ZonedDateTime expTo;

    @Parameters
    public static List<TestData> data() {
        return Arrays.asList(TESTS);
    }


    public mediaDirectoryTest(TestData data) {
        super();
        this.filename = data.filename;
        this.expFrom = data.expFrom;
        this.expTo = data.expTo;
    }
    
    
    /**
     * Test of getV method, of class FileRenamer.
     */
    @Test
    public void testMediaDirectory() {
        MediaDirectory result = null;
        try {
            result = new MediaDirectory(new File(filename));
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        }
        assertEquals("Start date of folder(" + filename + ") result: " + result.getFirstDate() + " awaited: " + expFrom, expFrom, result.getFirstDate());
        assertEquals("End date of folder(" + filename + ") result: " + result.getLastDate() + " awaited: " + expTo, expTo, result.getLastDate());
    }
}
