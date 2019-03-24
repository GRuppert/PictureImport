/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nyusziful.pictureorganizer.Hash;

import java.io.File;
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
public class HashTest {
    static class TestData {
        String filename;
        String fullHash;
        String Hash;

        public TestData(String filename, String fullHash, String Hash) {
            this.filename = filename;
            this.fullHash = fullHash;
            this.Hash = Hash;
        }
    }
    private static final TestData[] TESTS = new TestData[] {
        new TestData("20160627_183440_GT-I9195I-20160627_173440.jpg",
                     "ab75c83fa44195b2f27a7a47cbabe852",
                     "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"),
        new TestData("20160627_173442_GT-I9195I-20160627_173443.jpg",
                     "c759536767849aba90ab4743a975c936",
                     "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"),
        new TestData("K2005-01-3_1@10-0_1-12(+0100)(Mon)-d41d8cd98f00b204e9800998ecf8427e-d41d8cd98f00b204e9800998ecf8427e-IMAG0001.JPG",
                     "e740252f6a3f7525498acc2aa97f8441",
                     "175a0db094949f12cba7b4434abbde5e"),
        new TestData("K2007-05-0_5@09-1_2-12(+0200)(Sat)-d41d8cd98f00b204e9800998ecf8427e-d41d8cd98f00b204e9800998ecf8427e-IMAG0017.JPG",
                     "d331dc822b8c96c68b3e92c08c3a8fb5",
                     "943819f7c307879c6e832b409f9c5dea"),
        new TestData("K2008-08-1_7@09-1_4-28(+0200)(Sun)-d41d8cd98f00b204e9800998ecf8427e-d41d8cd98f00b204e9800998ecf8427e-MOV03819.3GP",
                     "4e5ba5e19219ffe63a9d139749bf6fec",
                     "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"),
        new TestData("V5_K2015-07-2_5@12-3_2-29(+0200)(Sat)-4f0f7fe2fabb83c399af967ccf860d88-47e0be579ef91106cdd6c818b2976ce2-DSC09459.ARW",
                     "4f0f7fe2fabb83c399af967ccf860d88",
                     "47e0be579ef91106cdd6c818b2976ce2"),
        new TestData("V5_K2015-07-2_5@12-3_2-29(+0200)(Sat)-fcc9ab21a2fd849ef07a1b8b1045e0cf-47e0be579ef91106cdd6c818b2976ce2-DSC09460.ARW",
                     "fcc9ab21a2fd849ef07a1b8b1045e0cf",
                     "47e0be579ef91106cdd6c818b2976ce2"),
        new TestData("1.mp4",
                "fcc9ab21a2fd849ef07a1b8b1045e0cf",
                "47e0be579ef91106cdd6c818b2976ce2"),
        new TestData("V5_K2015-07-2_5@12-3_2-29(+0200)(Sat)-cd293a213a3291ad280629d38e7d3a3c-47e0be579ef91106cdd6c818b2976ce2-DSC09461.ARW",
                     "cd293a213a3291ad280629d38e7d3a3c",
                     "47e0be579ef91106cdd6c818b2976ce2")
    };
    String filename;
    String fullHash;
    String Hash;


    /**
     * Test of getHash method, of class Hash.
     */
    @Test
    public void testFullHash() {
        File file = new File(this.getClass().getClassLoader().getResource(filename).getFile());
        String result = MediaFileHash.getFullHash(file);
        assertEquals("MD5 of file(" + filename + ") counted: " + result + " awaited: " + fullHash, fullHash, result);
    }
    
    /**
     * Test of getHash method, of class Hash.
     */
    @Test
    public void testHash() {
        File file = new File(this.getClass().getClassLoader().getResource(filename).getFile());
        String result = MediaFileHash.getHash(file);
        assertEquals("Hash of file(" + filename + ") counted: " + result + " awaited: " + Hash, Hash, result);
    }

    public HashTest(TestData data) {
        super();
        this.filename = data.filename;
        this.fullHash = data.fullHash;
        this.Hash = data.Hash;
    }


    @Parameters    
    public static List<TestData> data() {
        return Arrays.asList(TESTS);
    }    
    
}
