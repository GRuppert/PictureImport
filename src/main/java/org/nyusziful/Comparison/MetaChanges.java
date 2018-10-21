package org.nyusziful.Comparison;


import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author gabor
 */
public class MetaChanges {
    private ArrayList<String> diffs;
    private int count;
    private ArrayList<String> dirs;
    
    public MetaChanges(ArrayList<String> inList, String dir) {
        diffs = inList;
        count = 1;
        dirs = new ArrayList();
        dirs.add(dir);
    }
    
    public boolean compare (ArrayList<String> inList, String dir) {
        if (inList.containsAll(diffs) && diffs.containsAll(inList)) {
            count++;
            if (!dirs.contains(dir)) dirs.add(dir);
            return true;
        } else return false;
    }
    
    public String getChanges() {
        StringBuilder sb = new StringBuilder();
        for (String s : diffs)
        {
            sb.append(s);
            sb.append("\t");
        }
        return sb.toString();
    }
    
    public int getCount() {return count;}
    
    public String getDirs() {
        StringBuilder sb = new StringBuilder();
        for (String s : dirs)
        {
            sb.append(s);
            sb.append("\n");
        }
        return sb.toString();
    }
}
