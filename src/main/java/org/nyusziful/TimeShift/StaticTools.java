/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nyusziful.TimeShift;

import static org.nyusziful.ExifUtils.ExifReadWrite.readFileMeta;
import static org.nyusziful.ExifUtils.ExifReadWriteET.exifTool;
import static org.nyusziful.Main.StaticTools.errorOut;

import org.nyusziful.Rename.Meta;

import java.io.File;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

/**
 *
 * @author gabor
 */
public class StaticTools {
    /**
     * @recheck the max and min values of the picture dates
     */
    public static HashMap<String, Stripes> readFiles(File dir, TimeLine tl, ZoneId zone) {
        HashMap<String, Stripes> stripes = new HashMap<String, Stripes>();
        if(dir.isDirectory()) {
/*            File[] content = dir.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {                    
                    return (!name.toLowerCase().startsWith("thumb") && !name.toLowerCase().endsWith("_thmb.jpg") && !new File(dir + "\\" + name).isDirectory());
                }});*/
            long date = -1;
            String[] commandAndOptions = {"exiftool", "-b", "-ThumbnailImage", "-w", "thmb/%d%f_thmb.jpg", "."};
            exifTool(commandAndOptions, dir);
//            exifTool(" -b -ThumbnailImage -w thmb/%d%f_thmb.jpg .", dir);
//            ArrayList<String> exifTool = exifTool(" -DateTimeOriginal -Model " + fileNames, dir);
            ArrayList<String> files = new ArrayList<>();
            files.add(".");
            Collection<Meta> exifToMeta = readFileMeta(files.toArray(new File[0]), ZoneId.systemDefault());
            Iterator<Meta> iterator = exifToMeta.iterator();
            String errors = "";
            while (iterator.hasNext()) {
                Meta next = iterator.next();
                String model = next.model;
                ZonedDateTime dateZ = next.date;
                if (dateZ != null) {
                    date = dateZ.toEpochSecond();
                    if (model == null) {model = "NA";}
                    Picture picture = new Picture(new File(/*dir + "\\" + */next.originalFilename), date, model);
                    if (!stripes.containsKey(model)) {
                        stripes.put(model, new Stripes(model, stripes.size(), tl));
                    }
                    stripes.get(model).add(picture);
                } else {
                    errors += "\n" + next.originalFilename;
                }                        
                
            }
            if (!errors.equals("")) {errorOut("xmp", new Exception(errors));}
        }
/*            ArrayList<String> exifToolResponse = exifTool(" -DateTimeOriginal -Model .", dir);
            Iterator<String> iterator = exifToolResponse.iterator();
            if (iterator.hasNext()) {
                String line = iterator.next();
                if (line.startsWith("========")) {
                    filename = line.substring(9);
                    model = null;
                    date = -1;
                }
                String errors = "";
                while (iterator.hasNext()) {
                    line = iterator.next();
                    if (line.startsWith("========") || line.contains("image files read")) {
                        if (date > -1 && model != null) {
                            Picture picture = new Picture(new File(dir + "\\" + filename), date, model);
                            if (!stripes.containsKey(model)) {
                                stripes.put(model, new Stripes(model, stripes.size(), tl));
                            }
                            stripes.get(model).add(picture);
                        } else {
                            errors += "\n" + filename;
                        }                        
                        filename = line.substring(9);
                        model = null;
                        date = -1;
                    } else {
                        switch (line.substring(0, 4)) {
                            case "Date":
                                line = line.substring(34);
                                String dateString = line.length()>25 ? line.substring(0, 25) : line; //2016:11:03 07:50:24+02:00
                                ZonedDateTime dateZ = getZonedTimeFromStr(dateString);
                                if (dateZ == null)
                                    dateZ = getTimeFromStr(dateString, zone);
                                if (dateZ != null)
                                    date = dateZ.toEpochSecond();
                                break;
                            case "Came":
                                model = line.substring(34);
                                break;
                            case "Warn":
                                errorOut("xmp", new Exception(line));
                                break;
                        }
                    }
                }
                if (!errors.equals("")) StaticTools.errorOut("Missing Exif info", new Exception(errors));
            }
        }*/
        return stripes;
    }   
}
