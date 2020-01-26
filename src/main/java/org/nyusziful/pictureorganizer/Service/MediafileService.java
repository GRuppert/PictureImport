package org.nyusziful.pictureorganizer.Service;

import org.nyusziful.pictureorganizer.DAL.DAO.MediafileDAO;
import org.nyusziful.pictureorganizer.DAL.DAO.MediafileDAOImplHib;
import org.nyusziful.pictureorganizer.DAL.Entity.Drive;
import org.nyusziful.pictureorganizer.DAL.Entity.Image;
import org.nyusziful.pictureorganizer.DAL.Entity.Mediafile;
import org.nyusziful.pictureorganizer.DTO.ImageDTO;
import org.nyusziful.pictureorganizer.DTO.MediafileDTO;
import org.nyusziful.pictureorganizer.DTO.Meta;
import org.nyusziful.pictureorganizer.Service.ExifUtils.ExifService;

import java.io.File;
import java.util.*;

import static org.nyusziful.pictureorganizer.Service.FolderService.dataToWinPath;
import static org.nyusziful.pictureorganizer.Service.Hash.MediaFileHash.getHash;

public class MediafileService {
    private MediafileDAO mediafileDAO;
    private DriveService driveService;
    private HashMap<String, Mediafile> fileSet = null;


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
    public Mediafile getMediafile(MediafileDTO mediafileDTO) {
        Mediafile getMediafile = mediafileDAO.getByFile(mediafileDTO);
        return getMediafile;
    }

    public MediafileDTO MediafileDTO(Mediafile mediafile) {
        throw new java.lang.UnsupportedOperationException("Not implemented");
    }

    public File getFile(Mediafile mediafile) {
        return new File(driveService.getLocalLetter(mediafile.getDrive()) + ":\\" + mediafile.getFolder() + "\\" + mediafile.getFilename());
    }

    public File getFile(MediafileDTO mediafile) {
        return new File(mediafile.letter + ":\\" + dataToWinPath(mediafile.path) + "\\" + mediafile.filename);
    }

    public Mediafile saveMediafile(Mediafile mediafile) {
        return saveMediafile(Collections.singleton(mediafile)).get(0);
    }

    public List<Mediafile> saveMediafile(Collection<Mediafile> mediafile) {
        List<Mediafile> mediafileDTOList = new ArrayList<>();
        for (Mediafile file: mediafile
             ) {
            mediafileDTOList.add(mediafileDAO.save(file));

        }
        return mediafileDTOList;
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
        mediafileDTO.setDrive(driveDTO);
        final Mediafile b = mediafileService.getMediaFile(mediafileDTO);
        System.out.println(b);
/*        MediafileDTO mediafile = mediafileService.getMediafile("001ccb41c7eb77075051f3febdcafe71");
        System.out.println(mediafile);
        mediafileService.updateMediafile(mediafile);*/
    }

    public Mediafile getMediaFile(Mediafile actFile) {
        if (fileSet == null) {
            fileSet = new HashMap<>();
            final List<Mediafile> byDriveId = mediafileDAO.getByDriveId(actFile.getDrive().getId());
            for (Mediafile file : byDriveId) {
                fileSet.put(file.getFolder().getPath() + file.getFilename(), file);
            }
        }
        Mediafile mediafile = fileSet.get(actFile.getFolder().getPath() + actFile.getFilename());
        return mediafile == null ? actFile : mediafile;
    }

    public int getVersionNumber(Image image) {
        if (image == null) return -1;
        if (image.getParent() == null) return 0;
        return getVersionNumber(image.getParent()) + 1;
    }
}
