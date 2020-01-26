package org.nyusziful.pictureorganizer.Service;

public class FolderService {
    public static String winToDataPath(String path) {
        return path.replaceAll("\\\\", "/");
    }

    public static String dataToWinPath(String path) {
        return path.replaceAll("/", "\\\\");
    }
}
