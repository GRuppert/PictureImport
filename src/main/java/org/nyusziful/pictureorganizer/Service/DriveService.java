package org.nyusziful.pictureorganizer.Service;

import org.nyusziful.pictureorganizer.DB.HibConnection;
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
        openConnection();
        List<DriveDTO> getDrives = driveDAO.getDrives();
        closeConnection();
        return getDrives;
    }

    private void openConnection() {
        if (((DriveDAOImplHib)driveDAO).getCurrentSession() == null || !((DriveDAOImplHib)driveDAO).getCurrentSession().isOpen())
            ((DriveDAOImplHib)driveDAO).setCurrentSession(HibConnection.getCurrentSession());
//        ((DriveDAOImplHib)driveDAO).getCurrentSession().beginTransaction();

    }

    private void closeConnection() {
//        ((DriveDAOImplHib)driveDAO).getCurrentSession().getTransaction().commit();
        HibConnection.closeSession();
    }

    public static void main(String[] args) {
        final DriveService driveService = new DriveService();
        final List<DriveDTO> drives = driveService.getDrives();
        for (DriveDTO drive : drives) {
            System.out.println(drive);
        }
    }

}
