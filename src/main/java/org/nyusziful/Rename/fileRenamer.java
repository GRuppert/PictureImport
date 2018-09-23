/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nyusziful.Rename;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import static org.nyusziful.Main.StaticTools.errorOut;

/**
 *
 * @author gabor
 */
public class fileRenamer {
    public static DateTimeFormatter dfV1 = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");//20161124_200546
    public static DateTimeFormatter dfV2 = DateTimeFormatter.ofPattern("yyyy-MM-dd@HH-mm-ssZ");//2016-11-24@20-05-46+0200
    public static DateTimeFormatter dfV3 = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");//20161124200546
    public static DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd@HH-mm-ss");//2016-11-24@20-05-46
    public static String[] CAMERAS = {
        "NA",
        "ILCE-5100",
        "ILCE-6000",
        "GT-I9192",
        "GT-I9195I",
        "Lumia 1020",
        "FinePix S5800 S800",
        "TG-3            ",
        "ST25i",
        "GT-I8190N",
        "E52-1"
    };

    /**
     * 
     * @param filename
     * @return meta with information from the fileName, null if no compatible format found, unknown values are null 
     */
    public static meta getV(String filename) {
        meta metaFile = null;
        if (filename.startsWith("V") && filename.substring(2, 3).equals("_")) {
            switch (filename.substring(1, 2)) {
                case "5":
                    metaFile = getV5(filename);
                    break;
                case "6":
                    metaFile = getV6(filename);
                    break;
                default:
                    metaFile = null;
            }
        } else {
            if ((metaFile = getV1(filename)) == null)
                if ((metaFile = getV2(filename)) == null)
                    if ((metaFile = getV4(filename)) == null)
                        metaFile = getV3(filename);
            
        }
        return metaFile;
    }
    
    private static meta getV1(String filename) {//20160924_144402_ILCE-5100-DSC00615.JPG
        if (filename.length() > 17+4+1) {
            try {
                ZonedDateTime captureDate = LocalDateTime.parse(filename.substring(0, 15), dfV1).atZone(ZoneId.systemDefault());
                String[] parts = filename.substring(15 + 1).split("-");
                if (parts.length == 2)
                    return new meta(parts[1], captureDate, null, parts[0], null, null, null, null, null);
                if (parts.length > 2)
                    for (String camera : CAMERAS)
                        if (filename.substring(15 + 1).startsWith(camera)) {
                            return new meta(filename.substring(15 + 1 + camera.length() + 1), captureDate, null, camera, null, null, null, null, null);
                        }
                errorOut("Not recognized camera", new Exception());
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    private static meta getV2(String filename) {// "K2016-11-0_3@07-5_0-24_Thu(p0100)-" UTC???
        if (filename.length() > 34+1+4) {
            try {
                ZonedDateTime captureDate = ZonedDateTime.parse(filename.substring(1, 10) + filename.substring(11, 17) + filename.substring(18, 22) + (filename.substring(27, 28).equals("p") ? "+" : "-") + filename.substring(28, 32), dfV2);
                return new meta(filename.substring(34), captureDate, null, null, null, null, null, null, null);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    private static meta getV3(String filename) {//K2016_11!0_4@15_1_0_38(+0100)(Fri)-
        if (filename.length() > 34+1+4) {
            try {
                String dateString = 
                        filename.substring(1, 5) + 
                        filename.substring(6, 8) + 
                        filename.substring(9, 10) + 
                        filename.substring(11, 12) + 
                        filename.substring(13, 15) + 
                        filename.substring(16, 17) + 
                        filename.substring(18, 19) +
                        filename.substring(20, 22)
                ;
                ZonedDateTime captureDate = LocalDateTime.parse(dateString, dfV3).atZone(ZoneOffset.UTC);
                captureDate = captureDate.withZoneSameInstant(ZoneId.of(filename.substring(23, 28)));
                return new meta(filename.substring(35), captureDate, null, null, null, null, null, null, null);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    private static meta getV4(String filename) {//K2016_11!0_4@15_1_0_38(+0100)(Fri)-XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX-XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX-
        if (filename.length() > 34+1+4+32+32) {
            try {
                String dateString = 
                        filename.substring(1, 5) + 
                        filename.substring(6, 8) + 
                        filename.substring(9, 10) + 
                        filename.substring(11, 12) + 
                        filename.substring(13, 15) + 
                        filename.substring(16, 17) + 
                        filename.substring(18, 19) +
                        filename.substring(20, 22)
                ;
                ZonedDateTime captureDate = LocalDateTime.parse(dateString, dfV3).atZone(ZoneOffset.UTC);
                captureDate = captureDate.withZoneSameInstant(ZoneId.of(filename.substring(23, 28)));
                
                if (filename.substring(34, 35).equals("-") && filename.substring(67, 68).equals("-")) {
                    return new meta(filename.substring(101), captureDate, null, null, null, filename.substring(35, 67), filename.substring(68, 100), null, null);
                    
                }
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    private static meta getV5(String filename) {//V5_K2016_11!0_4@15_1_0_38(+0100)(Fri)-XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX-XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX-
        int offsetV = 3;
        if (filename.length() > offsetV+34+1+4+32+32) {
            try {
                String dateString = 
                        filename.substring(1 + offsetV, 5 + offsetV) + 
                        filename.substring(6 + offsetV, 8 + offsetV) + 
                        filename.substring(9 + offsetV, 10 + offsetV) + 
                        filename.substring(11 + offsetV, 12 + offsetV) + 
                        filename.substring(13 + offsetV, 15 + offsetV) + 
                        filename.substring(16 + offsetV, 17 + offsetV) + 
                        filename.substring(18 + offsetV, 19 + offsetV) +
                        filename.substring(20 + offsetV, 22 + offsetV)
                ;
                ZonedDateTime captureDate = LocalDateTime.parse(dateString, dfV3).atZone(ZoneOffset.UTC);
                captureDate = captureDate.withZoneSameInstant(ZoneId.of(filename.substring(23 + offsetV, 28 + offsetV)));
                
                if (filename.substring(34 + offsetV, 35 + offsetV).equals("-") && filename.substring(67 + offsetV, 68 + offsetV).equals("-")) {
                    return new meta(filename.substring(101 + offsetV), captureDate, null, null, filename.substring(35 + offsetV, 67 + offsetV), filename.substring(68 + offsetV, 100 + offsetV), null, null, null);
                    
                }
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    private static meta getV6(String filename) {//K2016_11!0_4@15_1_0_38(+0100)(Fri)-XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX-0-
        int offsetV = 3;
        if (filename.length() > offsetV+34+1+4+32+2) {
            try {
                String dateString = 
                        filename.substring(1 + offsetV, 5 + offsetV) + 
                        filename.substring(6 + offsetV, 8 + offsetV) + 
                        filename.substring(9 + offsetV, 10 + offsetV) + 
                        filename.substring(11 + offsetV, 12 + offsetV) + 
                        filename.substring(13 + offsetV, 15 + offsetV) + 
                        filename.substring(16 + offsetV, 17 + offsetV) + 
                        filename.substring(18 + offsetV, 19 + offsetV) +
                        filename.substring(20 + offsetV, 22 + offsetV)
                ;
                ZonedDateTime captureDate = LocalDateTime.parse(dateString, dfV3).atZone(ZoneOffset.UTC);
                captureDate = captureDate.withZoneSameInstant(ZoneId.of(filename.substring(23 + offsetV, 28 + offsetV)));
                
                if (filename.substring(34 + offsetV, 35 + offsetV).equals("-") && filename.substring(67 + offsetV, 68 + offsetV).equals("-")) {
                    return new meta(filename.substring(70 + offsetV), captureDate, null, null, null, filename.substring(35 + offsetV, 67 + offsetV), null, null, filename.substring(68 + offsetV, 69 + offsetV));
                    
                }
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    private static String dateFormat(ZonedDateTime zoned) {
        if (zoned == null) return "E0000-00-0_0@00-0_0-00(+0000)(Xxx)-";
        int offsetSec = zoned.getOffset().getTotalSeconds();
        String offsetSign;
        if (offsetSec < 0) {
            offsetSign = "-";
            offsetSec = offsetSec * -1;
        } else offsetSign = "+";
        String offsetH = Integer.toString(offsetSec / 3600);        
        if (offsetH.length() == 1) offsetH = "0" + offsetH;
        String offsetM = Integer.toString((offsetSec - (int)(offsetSec / 3600) * 3600)/ 60);
        if (offsetM.length() == 1) offsetM = "0" + offsetM;
        
        String dateS = zoned.withZoneSameInstant(ZoneId.of("UTC")).format(outputFormat);
        dateS = dateS.substring(0, 9) + "_" + dateS.substring(9, 15) + "_" + dateS.substring(15) + "(" + offsetSign + offsetH + offsetM + ")" + "(" + zoned.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.US) + ")" + "-";
        return dateS;
    }// "K2016-11-0_3@07-5_0-24(+0100)(Thu)-"

    public static String getFileName(String ver, String pictureSet, String originalName, ZonedDateTime date, String iID, String dID, String original) {
        switch (ver) {
            case "5":
                return "V" + ver + "_" + pictureSet + dateFormat(date) + iID + "-" + dID + "-" + originalName;

            case "6":
                return "V" + ver + "_" + pictureSet + dateFormat(date) + dID + "-" + original + "-" + originalName;
            default:
                return null;
        }
    }
    
}
