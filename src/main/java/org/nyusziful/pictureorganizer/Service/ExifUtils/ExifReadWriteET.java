/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nyusziful.pictureorganizer.Service.ExifUtils;

import org.nyusziful.pictureorganizer.DTO.Meta;

import java.io.*;
import java.time.ZoneId;
import java.util.*;

import static org.nyusziful.pictureorganizer.UI.StaticTools.errorOut;
import static org.nyusziful.pictureorganizer.UI.StaticTools.getZonedTimeFromStr;

/**
 *
 * @author gabor
 */
public class ExifReadWriteET {
    private static ArrayList<Meta> readFileMeta(File[] files, ZoneId defaultTZ) {
        String filename = null;

        ArrayList<String> filenames = new ArrayList<>();
        //TODO handle it!
        int chunkSize = 100;//At least 2, exiftool has a different output format for single files
        int done = 0;
        ArrayList<Meta> results = new ArrayList<>();
        while ( done < files.length) {
            File dir = null;
            ArrayList<String> fileList = new ArrayList<>();
            for (int f = 0; (f < chunkSize) && (done + f < files.length); f++) {
                if (dir == null) dir = files[done + f].getParentFile();
                if (!dir.equals(files[done + f].getParentFile())) break;
                fileList.add(files[done + f].getName());
                done++;
            }


            if (filenames.size() == 1 && filenames.get(0).length() > 5) {
                filename = dir + "\\" + filenames.get(0);
            }
            filenames.add(0, "-OriginalDocumentID");
            filenames.add(0, "-DocumentID");
            filenames.add(0, "-InstanceID");
            filenames.add(0, "-DateTimeOriginal");
            filenames.add(0, "-xmp:DateTimeOriginal");
            filenames.add(0, "-Model");
            filenames.add(0, "exiftool");
            ArrayList<String> exifTool = exifTool(filenames.toArray(new String[0]), dir);
            Iterator<String> iterator = exifTool.iterator();
            int i = -1;
            String model = null;
            String note = "";
            String iID = null;
            String dID = null;
            String odID = null;
            String captureDate = null;
            Boolean dateFormat = null;
            while (iterator.hasNext()) {
                String line = iterator.next();
                if (line.startsWith("========")) {
                    if (i > -1) {
                        Meta meta = new Meta(filename, getZonedTimeFromStr(captureDate), dateFormat, model, iID, dID, odID, note, -1);
                        System.out.println(meta);
                        results.add(meta);
                    }
                    i++;
                    String fileTemp = line.substring(9).replaceAll("./", "").replaceAll("/", "\\");
                    filename = dir + "\\" + fileTemp;
                    model = null;
                    captureDate = null;
                    dID = null;
                    odID = null;
                    note = "";
                    dateFormat = false;

                    //End of exiftool output
                } else if (line.contains("image files read")) {
                    if (!line.contains(" 0 image files read")) {
                    }
                } else if (line.contains("files could not be read")) {

                } else {
                    String tagValue = "";
                    if (line.length() > 34) tagValue = line.substring(34);
                    switch (line.substring(0, 4)) {
                        case "Date":
                            if (captureDate == null)
                                captureDate = tagValue;
                            else {
                                if (Math.abs(captureDate.length() - tagValue.length()) == 6) dateFormat = true;
                                captureDate = tagValue;
                            }
                            break;
                        case "Came":
                            model = tagValue;
                            break;
                        case "Orig":
                            odID = tagValue;
                            break;
                        case "Docu":
                            dID = tagValue;
                            break;
                        case "Inst":
                            iID = tagValue;
                            break;
                        case "Warn":
                            note = line;
                            break;
                    }
                }
            }
            if (filename != null) {
                Meta meta = new Meta(filename, getZonedTimeFromStr(captureDate), dateFormat, model, iID, dID, odID, note, -1);
                System.out.println(meta);
                results.add(meta);
//            results.add(new Meta(filename, getZonedTimeFromStr(captureDate), dateFormat, model, note, dID, odID));
            }
        }
        return results;
    }

    public static File createXmp(File file) {
        String[] commandAndOptions = {"exiftool", file.getName(), "-o", file.getName() + ".xmp"};
        List<String> result = exifTool(commandAndOptions, file.getParentFile());
        if (result.get(0).endsWith("files created")) return new File(file.getAbsolutePath() + ".xmp");
        return null;
    }

    public static ArrayList<String> getExif(String[] values, File file) {
        List<String> command = new ArrayList<>(Arrays.asList(values));
        command.add(0, "exiftool");
        command.add(file.getName());
        return exifTool(command, file.getParentFile());
    }

    public static void updateExif(List<String> valuePairs, File directory) {
        valuePairs.add(0, "-overwrite_original");
        valuePairs.add(0, "exiftool");
        exifTool(valuePairs, directory);
    }




    private static ArrayList<String> exifTool(Collection<String> parameters, File directory) {
        return exifTool(parameters.toArray(new String[0]), directory);
    }

    public static ArrayList<String> exifTool(String[] parameters, File directory) {
//        final String command[] = "exiftool " + parameters;
        ArrayList<String> lines = new ArrayList<>();
        try {
            Runtime runtime = Runtime.getRuntime();
            Process p = runtime.exec(parameters, null, directory);
            final BufferedReader stdinReader = new BufferedReader(new InputStreamReader(p.getInputStream(), "ISO-8859-1"));
            final BufferedReader stderrReader = new BufferedReader(new InputStreamReader(p.getErrorStream(), "ISO-8859-1"));
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String s;
                        while (( s=stdinReader.readLine()) != null) {
                            lines.add(s);
                        }
                    }
                    catch(IOException e) {
                    }
                }
            }).start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String s;
                        while (( s=stderrReader.readLine()) != null) {
                            lines.add(s);
                        }
                    }
                    catch(IOException e) {
                    }
                }
            }).start();
            int returnVal = p.waitFor();
        } catch (Exception e) {
            errorOut("xmp", e);
        }
        return lines;
    }

    private static ArrayList<String> exifTool_builder(String[] parameters, File directory) {
//        final String command[] = "exiftool " + parameters;
        ArrayList<String> lines = new ArrayList<>();
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(parameters).directory(directory);
            Process p = processBuilder.start();
            final BufferedReader stdinReader = new BufferedReader(new InputStreamReader(p.getInputStream(), "ISO-8859-1"));
            final BufferedReader stderrReader = new BufferedReader(new InputStreamReader(p.getErrorStream(), "ISO-8859-1"));
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String s;
                        while (( s=stdinReader.readLine()) != null) {
                            lines.add(s);
                        }
                    }
                    catch(IOException e) {
                    }
                }
            }).start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String s;
                        while (( s=stderrReader.readLine()) != null) {
                            lines.add(s);
                        }
                    }
                    catch(IOException e) {
                    }
                }
            }).start();
            int returnVal = p.waitFor();
        } catch (Exception e) {
            errorOut("xmp", e);
        }
        return lines;
    }

}
