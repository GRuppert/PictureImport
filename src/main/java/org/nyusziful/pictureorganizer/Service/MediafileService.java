package org.nyusziful.pictureorganizer.Service;

import org.nyusziful.pictureorganizer.DAL.DAO.MediafileDAO;
import org.nyusziful.pictureorganizer.DAL.DAO.MediafileDAOImplHib;
import org.nyusziful.pictureorganizer.DAL.Entity.Drive;
import org.nyusziful.pictureorganizer.DAL.Entity.Image;
import org.nyusziful.pictureorganizer.DAL.Entity.MediaFile;
import org.nyusziful.pictureorganizer.DTO.MediafileDTO;
import org.nyusziful.pictureorganizer.DTO.Meta;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

import static org.nyusziful.pictureorganizer.Service.FolderService.dataToWinPath;
import static org.nyusziful.pictureorganizer.Service.Rename.FileNameFactory.getV;

public class MediafileService {
    private MediafileDAO mediafileDAO;
    private DriveService driveService;


    public MediafileService() {
        driveService = new DriveService();
        mediafileDAO = new MediafileDAOImplHib();
    }

    public List<MediaFile> getMediafiles() {
        List<MediaFile> getMediafiles = mediafileDAO.getAll();
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
    public MediaFile getMediafile(Drive drive, Path path) {
        MediaFile getMediafile = mediafileDAO.getByFile(drive, path);
        return getMediafile;
    }

    public MediafileDTO getMediafileDTO(MediaFile mediafile) {
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

    public void persistMediafile(MediaFile mediafile) {
        persistMediafile(Collections.singleton(mediafile));
    }

    public void persistMediafile(Collection<? extends MediaFile> mediafile) {
        for (MediaFile file: mediafile) {
            if (file.getId() > -1)
                mediafileDAO.merge(file);
            else
                mediafileDAO.persist(file);
        }
    }

    public void updateMediafile(MediaFile Mediafile) {
        mediafileDAO.merge(Mediafile);
    }

    public static void main(String[] args) {
        final MediafileService mediafileService = new MediafileService();
        MediaFile mediafileDTO = new MediaFile(

        );
        final Drive driveDTO = new Drive();
        driveDTO.setId(100);
/*        MediafileDTO mediafile = mediafileService.getMediafile("001ccb41c7eb77075051f3febdcafe71");
        System.out.println(mediafile);
        mediafileService.updateMediafile(mediafile);*/
    }

    public MediaFile getMediaFile(Path path) {
        final Drive localDrive = driveService.getLocalDrive(path.toString().substring(0, 1));
        return mediafileDAO.getByFile(localDrive, path);
    }

    public List<MediaFile> getMediaFilesFromPath(Path path) {
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

    /**
     *
     * @param mediaFile
     * @return true if data has been written into the image entity, which has to be persisted
     * @throws Exception if the data in the "original" media file does not match what was already saved into the image
     */
    public boolean updateOriginalImage(MediaFile mediaFile) throws Exception {
        final Image image = mediaFile.getImage();
        final Meta metaOrig = getV(mediaFile.getFilename());
        String origFileName = (metaOrig != null && metaOrig.originalFilename != null) ? metaOrig.originalFilename : mediaFile.getFilename();
        if (image.getOriginalFileHash() == null && image.getDateTaken() == null && image.getOriginalFilename() == null) {
            image.setOriginalFileHash(mediaFile.getFilehash());
            image.setDateTaken(mediaFile.getDateStored());
            image.setOriginalFilename(origFileName);
            mediaFile.setOriginal(true);
            return true;
        } else {
            if (!origFileName.equals(image.getOriginalFilename()) || !mediaFile.getFilehash().equals(image.getOriginalFileHash()) || (!mediaFile.getDateStored().isEqual(image.getDateTaken()) && !mediaFile.getDateStored().isEqual(image.getDateCorrected()))) {
                throw new Exception("Mismatch");
            }
        }
        return false;
    }

}
