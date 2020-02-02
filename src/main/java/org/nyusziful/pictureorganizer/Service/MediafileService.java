package org.nyusziful.pictureorganizer.Service;

import org.nyusziful.pictureorganizer.DAL.DAO.MediafileDAO;
import org.nyusziful.pictureorganizer.DAL.DAO.MediafileDAOImplHib;
import org.nyusziful.pictureorganizer.DAL.Entity.Drive;
import org.nyusziful.pictureorganizer.DAL.Entity.Image;
import org.nyusziful.pictureorganizer.DAL.Entity.Mediafile;
import org.nyusziful.pictureorganizer.DTO.MediafileDTO;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

import static org.nyusziful.pictureorganizer.Service.FolderService.dataToWinPath;

public class MediafileService {
    private MediafileDAO mediafileDAO;
    private DriveService driveService;


    public MediafileService() {
        driveService = new DriveService();
        mediafileDAO = new MediafileDAOImplHib();
    }

    public List<Mediafile> getMediafiles() {
        List<Mediafile> getMediafiles = mediafileDAO.getAll();
        return getMediafiles;
    }

/*
    public MediafileDTO getMediafile(String name) {
        openConnection();
        MediafileDTO getMediafile = mediafileDAO.getByName(name);
        closeConnection();
        return getMediafile;
    }
*/
    public Mediafile getMediafile(Drive drive, Path path) {
        Mediafile getMediafile = mediafileDAO.getByFile(drive, path);
        return getMediafile;
    }

    public MediafileDTO getMediafileDTO(Mediafile mediafile) {
        MediafileDTO mediafileDTO = new MediafileDTO();
        if (mediafile != null) {
            if (mediafile.getImage() != null) mediafileDTO.driveId = mediafile.getImage().getId();
            if (mediafile.getFolder() != null) mediafileDTO.path = FolderService.winToDataPath(mediafile.getFolder().getJavaPath());
            mediafileDTO.filename = mediafile.getFilename();
            mediafileDTO.dateMod = mediafile.getDateMod();
            mediafileDTO.filehash = mediafile.getFilehash();
            mediafileDTO.size = mediafile.getSize();
        }
        return mediafileDTO;
    }

    public File getFile(MediafileDTO mediafile) {
        return new File(mediafile.letter + ":" + dataToWinPath(mediafile.path) + "\\" + mediafile.filename);
    }

    public void persistMediafile(Mediafile mediafile) {
        persistMediafile(Collections.singleton(mediafile));
    }

    public void persistMediafile(Collection<Mediafile> mediafile) {
        for (Mediafile file: mediafile) {
            mediafileDAO.persist(file);
        }
    }

    public void updateMediafile(Mediafile Mediafile) {
        mediafileDAO.merge(Mediafile);
    }

    public static void main(String[] args) {
        final MediafileService mediafileService = new MediafileService();
        Mediafile mediafileDTO = new Mediafile(

        );
        final Drive driveDTO = new Drive();
        driveDTO.setId(100);
/*        MediafileDTO mediafile = mediafileService.getMediafile("001ccb41c7eb77075051f3febdcafe71");
        System.out.println(mediafile);
        mediafileService.updateMediafile(mediafile);*/
    }

    public Mediafile getMediaFile(Path path) {
        final Drive localDrive = driveService.getLocalDrive(path.toString().substring(0, 1));
        return mediafileDAO.getByFile(localDrive, path);
    }

    public List<Mediafile> getMediaFilesFromPath(Path path) {
        final Drive localDrive = driveService.getLocalDrive(path.toString().substring(0, 1));
        return mediafileDAO.getByPath(localDrive, path);
    }

    public int getVersionNumber(Image image) {
        if (image == null) return -1;
        if (image.getParent() == null) return 0;
        return getVersionNumber(image.getParent()) + 1;
    }

    public void flush() {
        mediafileDAO.flush();
    }
}
