package org.nyusziful.pictureorganizer.Service;

import org.nyusziful.pictureorganizer.DAL.DAO.MediafileDAO;
import org.nyusziful.pictureorganizer.DAL.DAO.MediafileDAOImplHib;
import org.nyusziful.pictureorganizer.DAL.Entity.Drive;
import org.nyusziful.pictureorganizer.DAL.Entity.Image;
import org.nyusziful.pictureorganizer.DAL.Entity.Mediafile;
import org.nyusziful.pictureorganizer.DTO.Meta;
import org.nyusziful.pictureorganizer.Service.ExifUtils.ExifService;

import java.io.File;
import java.util.*;

import static org.nyusziful.pictureorganizer.Service.Hash.MediaFileHash.getHash;

public class MediafileService {
    private MediafileDAO mediafileDAO;
    private HashMap<String, Mediafile> fileSet = null;
    private ImageService imageService;


    public MediafileService() {
        mediafileDAO = new MediafileDAOImplHib();
        imageService = new ImageService();
    }

    public List<Mediafile> getMediafiles() {
        List<Mediafile> getMediafiles = mediafileDAO.getAll();
        return getMediafiles;
    }

    public void checkImage(Mediafile mediafile) {
        File file = mediafile.getFile();
        final String hash = getHash(file);
        Image image = imageService.getImage(hash, mediafile);
        if (image == null) {
            Meta meta = ExifService.readMeta(file, zone);
            image = new Image(hash, meta.date, mediafile.getFilename(), mediafile.getType());
        }
        mediafile.setImage(image);
    }

/*
    public MediafileDTO getMediafile(String name) {
        openConnection();
        MediafileDTO getMediafile = mediafileDAO.getByName(name);
        closeConnection();
        return getMediafile;
    }
*/

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
/*
        int version = 1;
        for (Mediafile mediafile : filesForImage) {
            if (mediafile..getFilehash().equals(actFile.getFilehash())
            final Meta v = FileRenamer.getV(mediafile.getFilename());
            if (v != null) {
                version = Math.max(version, v.orig);
            }
        }
*/
    }
}
