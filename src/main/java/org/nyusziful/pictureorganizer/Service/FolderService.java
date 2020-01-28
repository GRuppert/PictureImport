package org.nyusziful.pictureorganizer.Service;

import org.nyusziful.pictureorganizer.DAL.DAO.FolderDAO;
import org.nyusziful.pictureorganizer.DAL.DAO.FolderDAOImplHib;
import org.nyusziful.pictureorganizer.DAL.Entity.Drive;
import org.nyusziful.pictureorganizer.DAL.Entity.Folder;
import org.nyusziful.pictureorganizer.DTO.FolderDTO;
import org.nyusziful.pictureorganizer.DTO.MediafileDTO;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FolderService {
    private FolderDAO folderDAO;
    private DriveService driveService;

    public FolderService() {
        driveService = new DriveService();
        folderDAO = new FolderDAOImplHib();
    }

    public File getFile(Folder folder) {
        return new File(driveService.getLocalLetter(folder.getDrive()) + ":\\" + dataToWinPath(folder.getPath()));
    }

    public File getFile(FolderDTO folder) {
        return new File(folder.letter + ":" + dataToWinPath(folder.path));
    }



    public static String winToDataPath(String path) {
        return path.replaceAll("\\\\", "/");
    }

    public static String dataToWinPath(String path) {
        return path.replaceAll("/", "\\\\");
    }

    public Folder getFolder(FolderDTO folder) {
        return folderDAO.getFolderByPath(folder);
    }

    public FolderDTO getFolderDTO(Drive drive, Path folder) {
        final FolderDTO folderDTO = new FolderDTO();
        folderDTO.driveId = drive.getId();
        folderDTO.letter = driveService.getLocalLetter(drive);
        folderDTO.path = winToDataPath(folder.toString().substring(3));
        return folderDTO;
    }

    public List<Folder> saveFolder(Collection<Folder> folders) {
        List<Folder> folderList = new ArrayList<>();
        for (Folder folder: folders) {
            folderList.add(folderDAO.save(folder));
        }
        return folderList;
    }

}
