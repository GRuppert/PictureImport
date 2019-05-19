package org.nyusziful.pictureorganizer.Service;

import org.nyusziful.pictureorganizer.Model.DriveDAO;
import org.nyusziful.pictureorganizer.Model.DriveDAOImplHib;
import org.nyusziful.pictureorganizer.Model.DriveDTO;

import java.util.List;

public class DriveService {
    private static DriveDAO driveDAO;

    public DriveService() {
        driveDAO = new DriveDAOImplHib();
    }

    public List<DriveDTO> getDrives() {
        List<DriveDTO> getDrives = driveDAO.getAll();
        return getDrives;
    }

    public static void main(String[] args) {
        final DriveService driveService = new DriveService();
        final List<DriveDTO> drives = driveService.getDrives();
        for (DriveDTO drive : drives) {
            System.out.println(drive);
        }
    }

}
