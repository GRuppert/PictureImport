package org.nyusziful.Hash;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class JPGHashTest {
    static class TestData {
        String filename;
        String terminationMessage;
        JPEGSegment lastSegment;

        public TestData(String filename, String terminationMessage, JPEGSegment lastSegment) {
            this.filename = filename;
            this.terminationMessage = terminationMessage;
            this.lastSegment = lastSegment;
        }
    }
    private static final TestData[] TESTS = new TestData[] {
            new TestData("20160627_183440_GT-I9195I-20160627_173440.jpg",
                    "Not found marker after segment",
                    new JPEGSegment(2, 104, 225, "Exif\0\0")),
            new TestData("DSC08806.jpg",
                    "Reached the end of the file",
                    new JPEGSegment(3427848, 2, 217, "")),
            new TestData("V6_K2018-06-1_6@19-5_7-24(-0500)(Sat)-ecb60326c6f29a67b8e39c1825cfc083-0-D5C04877.jpg",
                    "Reached the end of the file",
                    new JPEGSegment(325664, 2, 217, "")),
            new TestData("K2005-01-3_1@10-0_1-12(+0100)(Mon)-d41d8cd98f00b204e9800998ecf8427e-d41d8cd98f00b204e9800998ecf8427e-IMAG0001.jpg",
                    "Not found marker after segment",
                    new JPEGSegment(181507, 2, 217, "")),
            new TestData("20181007_120044331_iOS.jpg",
                    "Reached the end of the file",
                    new JPEGSegment(2455749, 2, 217, ""))
    };
    String filename;
    String terminationMessage;
    JPEGSegment lastSegment;

    @Parameterized.Parameters
    public static List<TestData> data() {
        return Arrays.asList(TESTS);
    }

    public JPGHashTest(TestData data) {
        super();
        this.filename = data.filename;
        this.terminationMessage = data.terminationMessage;
        this.lastSegment = data.lastSegment;
    }

    @Test
    public void scanJPG() {
        File file = new File(this.getClass().getClassLoader().getResource(filename).getFile());
        final JPEGMediaFileStruct fileStruct = JPGHash.scan(file);
        assertEquals(filename + " read termination message: " + fileStruct.getTerminationMessage() + " awaited: " + terminationMessage, fileStruct.getTerminationMessage(), terminationMessage);
        assertEquals("Structure of " + filename + " analyzed: " + fileStruct.getLastSegment().toString() + " awaited: " + lastSegment.toString(), fileStruct.getLastSegment(), lastSegment);
//        fileStruct.drawMap();
    }

//    @Test
    public void readDigest() {
    }
}