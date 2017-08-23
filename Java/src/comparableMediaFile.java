
import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author gabor
 */
public class comparableMediaFile {
    File file;
    String originalFileName;
    String date;
    Long size;
    String sfv;
    int match = 0;
    Boolean ok = false;
    String warnings = "";
    String errors = "";

    comparableMediaFile(File fileIn, String dateIn, Long sizeIn) {
        this.file = fileIn;
        this.date = dateIn;
        this.size = sizeIn;
        originalFileName = file.getName();
        getV1();
        getV2();
        getV3();
    }

    private void getV1() {//20160924_144402_ILCE-5100-DSC00615.JPG
        if (file.getName().length() > 17+4+1) {
            try {
                ZonedDateTime captureDate = LocalDateTime.parse(file.getName().substring(0, 15), PicOrganizes.dfV1).atZone(ZoneId.systemDefault());
                for (String camera : PicOrganizes.CAMERAS)
                    if (file.getName().substring(15 + 1).startsWith(camera)) {
                        originalFileName = file.getName().substring(15 + 1 + camera.length() + 1);
                        return;
                    }
            } catch (DateTimeParseException e) {
            }
        }
    }

    private void getV2() {// "K2016-11-0_3@07-5_0-24_Thu(p0100)-"
        if (file.getName().length() > 34+1+4) {
            try {
                ZonedDateTime captureDate = LocalDateTime.parse(file.getName().substring(1, 10) + file.getName().substring(11, 17) + file.getName().substring(18, 22) + file.getName().substring(27, 32), PicOrganizes.dfV2).atZone(ZoneId.systemDefault());
                originalFileName = file.getName().substring(34);
            } catch (DateTimeParseException e) {
            }
        }
    }

    private void getV3() {
        if (file.getName().length() > 7+1+4) {
            try {
                int parseInt = Integer.parseInt(file.getName().substring(0, 6));
                if (parseInt < 110000)
                originalFileName = file.getName().substring(7);
            } catch (NumberFormatException e) {
            }
        }
    }
}              

