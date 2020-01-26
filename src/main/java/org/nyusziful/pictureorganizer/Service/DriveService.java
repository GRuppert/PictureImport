package org.nyusziful.pictureorganizer.Service;

import org.nyusziful.pictureorganizer.DAL.DAO.DriveDAO;
import org.nyusziful.pictureorganizer.DAL.DAO.DriveDAOImplHib;
import org.nyusziful.pictureorganizer.DAL.Entity.Drive;

import javax.swing.filechooser.FileSystemView;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class DriveService {
    private static DriveDAO driveDAO;

    public DriveService() {
        driveDAO = new DriveDAOImplHib();
    }

    public List<Drive> getDrives() {
        List<Drive> getDrives = driveDAO.getAll();
        return getDrives;
    }

    private String getVolumeSN(String letter) {
        String volumeSN = "";
        try {
            Process p = Runtime.getRuntime().exec("cmd /c vol "+ letter + ":");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                if (line.startsWith(" Volume Serial Number is ")) volumeSN = line.substring(25);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return volumeSN;
    }

    public Drive getLocalDrive(String letter) {
        final String volumeSN = getVolumeSN(letter);
        final List<Drive> drives = getDrives();
        for (Drive drive : drives) {
            if (volumeSN.equals(drive.getVolumeSN()))
            return drive;
        }
        return null;
    }

    public String getLocalLetter(Drive drive) {
        final File[] files = File.listRoots();
        for (File file : files) {
            if (getVolumeSN(file.getAbsolutePath().substring(0, 1)).equals(drive.getVolumeSN()))
                return file.getAbsolutePath().substring(0, 1);
        }
        return null;
    }

    public static void main(String[] args) {
        final DriveService driveService = new DriveService();
        final List<Drive> drives = driveService.getDrives();
        for (Drive drive : drives) {
            System.out.println(drive);
        }
    }

}
