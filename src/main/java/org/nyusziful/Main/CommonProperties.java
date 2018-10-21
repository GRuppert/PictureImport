package org.nyusziful.Main;

import org.nyusziful.Rename.tableViewMediaFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneId;

public class CommonProperties {
    private static CommonProperties instance;

    private int copyOrMove = tableViewMediaFile.COPY;
    private ZoneId zone = ZoneId.systemDefault();
    private String pictureSet = "K";
    private Path toDir = Paths.get("G:\\Pictures\\Photos\\Új\\SzandranakUj");
    private File fromDir = new File("G:\\Pictures\\Photos\\Új\\Szandranak");
//    private Path toDir = Paths.get("E:\\temp\\compare\\1");
//    private File fromDir = new File("E:\\temp\\compare\\2");
//    private Path toDir = Paths.get("G:\\Pictures\\Photos\\V5\\Közös");
//    private File fromDir = new File("G:\\Pictures\\Photos\\Új");


    private CommonProperties() {
    }

    public static CommonProperties getInstance() {
        if (CommonProperties.instance == null) {
            CommonProperties.instance = new CommonProperties();
        }
        return CommonProperties.instance;
    }

    public int getCopyOrMove() {
        return copyOrMove;
    }

    public void setCopyOrMove(int copyOrMove) {
        this.copyOrMove = copyOrMove;
    }

    public ZoneId getZone() {
        return zone;
    }

    public void setZone(ZoneId zone) {
        this.zone = zone;
    }

    public String getPictureSet() {
        return pictureSet;
    }

    public void setPictureSet(String pictureSet) {
        this.pictureSet = pictureSet;
    }

    public Path getToDir() {
        return toDir;
    }

    public void setToDir(Path toDir) {
        this.toDir = toDir;
    }

    public File getFromDir() {
        return fromDir;
    }

    public void setFromDir(File fromDir) {
        this.fromDir = fromDir;
    }
}
