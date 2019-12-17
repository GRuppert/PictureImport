package org.nyusziful.pictureorganizer.Service;

import org.nyusziful.pictureorganizer.DAL.DAO.DriveDAO;
import org.nyusziful.pictureorganizer.DAL.DAO.DriveDAOImplHib;
import org.nyusziful.pictureorganizer.DAL.Entity.Drive;

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

    public static void main(String[] args) {
        final DriveService driveService = new DriveService();
        final List<Drive> drives = driveService.getDrives();
        for (Drive drive : drives) {
            System.out.println(drive);
        }
    }

}
