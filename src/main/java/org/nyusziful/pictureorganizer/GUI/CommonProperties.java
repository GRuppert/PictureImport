package org.nyusziful.pictureorganizer.GUI;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.util.prefs.Preferences;

import org.nyusziful.pictureorganizer.Rename.TableViewMediaFile.WriteMethod;

public class CommonProperties {
    private static CommonProperties instance;

    private WriteMethod copyOrMove = WriteMethod.COPY;
    private ZoneId zone = ZoneId.systemDefault();
    private String pictureSet = "K";
    private Path toDir;
    private File fromDir;
    private Preferences prefs = Preferences.userNodeForPackage(org.nyusziful.pictureorganizer.GUI.PictureOrganizer.class);
    private static final String INDIR = "InputDirectory";
    private static final String OUTDIR = "OutputDirectory";


    private CommonProperties() {
        fromDir = new File(prefs.get(INDIR, "C:\\"));
        toDir = Paths.get(prefs.get(OUTDIR, "C:\\"));

    }

    public void save() {
        prefs.put(INDIR, fromDir.toString());
        prefs.put(OUTDIR, toDir.toString());
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
