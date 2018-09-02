/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Hash;

import org.nyusziful.Hash.abstractHash;
import org.nyusziful.Hash.hasher;
import java.io.File;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author gabor
 */
public class HashTest {
    
    public HashTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of getFullHash method, of class Hash.
     */
    @Test
    public void testGetFullHash() {
        System.out.println("getFullHash");
        File file = null;
        String expResult = "";
        String result = abstractHash.getFullHash(file);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getHash method, of class Hash.
     */
    @Test
    public void testGetHash() {
        System.out.println("getHash");
        File file = null;
        String expResult = "";
        String result = abstractHash.getHash(file);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
