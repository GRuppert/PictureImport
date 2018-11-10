package org.nyusziful.Hash;


import java.util.HashMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author gabor
 */
public class IfdTag {
    long address;
    int tagId;
    int type;
    long count;
    long offset;


    public IfdTag(long address) {
        this.address = address;
    }
    
    public String getTagId() {
        return IfdNames.getTag(tagId);
    }

    public String getType() {
        return IfdNames.getType(type);
    }
    
    public int getTypeLength() {
        switch (type) {
            case 1:
                return 1;
            case 2:
                return 1;
            case 3:
                return 2;
            case 4:
                return 4;
            case 5:
                return 8;
            default:
                return 0;
        }
    }
    
    public long getCount() {
        return count;
    }
    
    public long getValue() {
        if (count < 4) return offset;
        else return -1;
    }
    
    public long getPointer() {
        if (count > 3) return offset;
        else return -1;
        
    }
    
}
