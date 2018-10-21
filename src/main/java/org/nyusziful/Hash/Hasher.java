/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nyusziful.Hash;

/**
 *
 * @author gabor
 */
public interface Hasher {

    /**
     * 
     * @param hash
     * @return XMP style representation of the Input if it is a valid HASH
     */
    public static String formatHash(String hash) {
        if (hash.length()!=32 || !hash.matches("^[0-9A-Fa-f]+$")) return hash;
        return hash.substring(0, 8) + "-" + hash.substring(8, 12) + "-" + hash.substring(12, 16) + "-" + hash.substring(16, 20) + "-" + hash.substring(20, 32);
    }
    
}
