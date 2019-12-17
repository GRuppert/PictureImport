package org.nyusziful.pictureorganizer.Service;

import org.nyusziful.pictureorganizer.DAL.DAO.MediafileDAO;
import org.nyusziful.pictureorganizer.DAL.DAO.MediafileDAOImplHib;
import org.nyusziful.pictureorganizer.DAL.Entity.Drive;
import org.nyusziful.pictureorganizer.DAL.Entity.Mediafile;

import java.util.*;

public class MediafileService {
    private static MediafileDAO mediafileDAO;
    private static Set<Mediafile> fileSet = null;


    public MediafileService() {
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
            fileSet = new HashSet<>();
            fileSet.addAll(mediafileDAO.getByDriveId(actFile.getDrive().getId()));
        }
        for (Mediafile mfile: fileSet) {
            if (mfile.getFilename().equals(actFile.getFilename()) && mfile.getFolder().getPath().equals(actFile.getFolder().getPath())) {
                return mfile;
            }
        }
        return actFile;
    }

}
