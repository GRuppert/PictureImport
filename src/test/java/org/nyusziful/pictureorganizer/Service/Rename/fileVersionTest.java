/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nyusziful.pictureorganizer.Service.Rename;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.nyusziful.pictureorganizer.DTO.Meta;

/**
 *
 * @author gabor
 */
@RunWith(Parameterized.class)
public class fileVersionTest {
    static class TestData {
        String filename;
        Meta expResult;

        public TestData(String filename, Meta expResult) {
            this.filename = filename;
            this.expResult = expResult;
        }
    }
    private static final TestData[] TESTS = new TestData[] {
        new TestData(//V1
            "20140630_092353_Lumia 1020-WP_20140630_09_23_56_Raw__highres.dng",
            new Meta(1, "WP_20140630_09_23_56_Raw__highres.dng", ZonedDateTime.of(2014, 06, 30, 9, 23, 53, 00, ZoneId.systemDefault()), null, "Lumia 1020", null, null, null, null, null, null, null, null, null)
        ),
        new TestData(//V2
            "K2016-11-0_3@07-5_0-24_Thu(p0100)-117_1757.JPG",
            new Meta(2, "117_1757.JPG", ZonedDateTime.of(2016, 11, 03, 7, 50, 24, 00, ZoneId.of("+0100")), null, null, null, null, null, null, null, null, null, null, null)
        ),
        new TestData(//V3
            "K2016_11!0_4@15_1_0_38(+0100)(Fri)-117_1757.JPG",
            new Meta(3, "117_1757.JPG", ZonedDateTime.of(2016, 11, 04, 16, 10, 38, 00, ZoneId.of("+0100")), null, null, null, null, null, null, null, null, null, null, null)
        ),
        new TestData(//V4
            "K2004-05-1_5@10-1_3-46(+0200)(Sat)-a3a650e9653d8c59a40571e6cf18b24d-a3a650e9653d8c59a40571e6cf18b24d-117_1757.JPG",
            new Meta(4, "117_1757.JPG", ZonedDateTime.of(2004, 05, 15, 12, 13, 46, 00, ZoneId.of("+0200")), null, null, null, "a3a650e9653d8c59a40571e6cf18b24d", "a3a650e9653d8c59a40571e6cf18b24d", null, null, null, null, null, null)
        ),
        new TestData(//V5
            "V5_E0000-00-0_0@00-0_0-00(+0000)(Xxx)-065216b05febb18b704df5f2b78c3786-XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX-WP_20151011_10_47_57_Pro.jpg",
                    new Meta(5, "WP_20151011_10_47_57_Pro.jpg", null, null, null, "065216b05febb18b704df5f2b78c3786", "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", null, null, null, null, null, null, null)
            ),
        new TestData(//V5
            "V5_K2017-08-2_1@05-3_3-43(+0200)(Mon)-f71ead298d7cb82c2c4807e660e4bb1e-e39ea2ce9f8a8ad1d86c4dde1dd38f6d-20170821_073343.jpg",
            new Meta(5, "20170821_073343.jpg", ZonedDateTime.of(2017, 8, 21, 07, 33, 43, 00, ZoneId.of("+0200")), null, null, "f71ead298d7cb82c2c4807e660e4bb1e", "e39ea2ce9f8a8ad1d86c4dde1dd38f6d", null, null, null, null, null, null, null)
        ),
        new TestData(//V6
            "V6_E0000-00-0_0@00-0_0-00(+0000)(Xxx)-000f8699c647aab4b4cf8c0d2bebfeb6-a-DSC01531.JPG",
            new Meta(6, "DSC01531.JPG", null, null, null, null, "000f8699c647aab4b4cf8c0d2bebfeb6", null, null, "a", null, null, null, null)
        ),
        new TestData(//V6
            "V6_K2018-06-1_4@18-5_2-34(+0200)(Thu)-0e6036c18363812dae6c727f671ae422-0-D5C04531.ARW",
            new Meta(6, "D5C04531.ARW", ZonedDateTime.of(2018, 06, 14, 20, 52, 34, 00, ZoneId.of("+0200")), null, null, null, "0e6036c18363812dae6c727f671ae422", null, null, "0", null, null, null, null)
        )
    };
    String filename;
    Meta expResult;
    
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
     * Test of getV method, of class FileRenamer.
     */
    @Test
    public void testGetV() {
        Meta result = FileNameFactory.getV(filename);
        assertEquals("Meta of file(" + filename + ") result: " + result + " awaited: " + expResult, expResult.toString(), result.toString());
    }
}
