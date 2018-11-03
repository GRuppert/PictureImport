/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nyusziful.ExifUtils;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 *
 * @author gabor
 */
@RunWith(Parameterized.class)
public class ExifWriteTest {
    /**
     * Test of updateExif method, of class ExifReadWrite.
     */
//    @Test
    public void testUpdateExif() {
        System.out.println("updateExif");
        List<String> valuePairs = null;
        File directory = null;
        ExifReadWrite.updateExif(valuePairs, directory);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
