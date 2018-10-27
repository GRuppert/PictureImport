package org.nyusziful.Main;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneId;
import org.nyusziful.Rename.TableViewMediaFile.WriteMethod;

public class CommonProperties {
    private static CommonProperties instance;

    private WriteMethod copyOrMove = WriteMethod.COPY;
    private ZoneId zone = ZoneId.systemDefault();
    private String pictureSet = "K";
//    private Path toDir = Paths.get("G:\\Pictures\\Photos\\Új\\SzandranakUj");
//    private File fromDir = new File("G:\\Pictures\\Photos\\Új\\Szandranak");
    private Path toDir = Paths.get("E:\\temp\\delete");
    private File fromDir = new File("E:\\Képek\\200");
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

    public WriteMethod getCopyOrMove() {
        return copyOrMove;
    }

    public void setCopyOrMove(WriteMethod copyOrMove) {
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
