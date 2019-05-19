package org.nyusziful.pictureorganizer.Service;

import org.nyusziful.pictureorganizer.Model.MediafileDAO;
import org.nyusziful.pictureorganizer.Model.MediafileDAOImplHib;
import org.nyusziful.pictureorganizer.Model.MediafileDTO;

import java.util.List;

public class MediafileService {
    private static MediafileDAO mediafileDAO;

    public MediafileService() {
        mediafileDAO = new MediafileDAOImplHib();
    }

    public List<MediafileDTO> getMediafiles() {
        List<MediafileDTO> getMediafiles = mediafileDAO.getAll();
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

    public MediafileDTO saveMediafile(MediafileDTO Mediafile) throws Exception {
        MediafileDTO getMediafile = mediafileDAO.save(Mediafile);
        return getMediafile;
    }

    public void updateMediafile(MediafileDTO Mediafile) throws Exception {
        mediafileDAO.merge(Mediafile);
    }

    public static void main(String[] args) {
/*
        final MediafileService MediafileService = new MediafileService();
        MediafileDTO mediafile = MediafileService.getMediafile("001ccb41c7eb77075051f3febdcafe71");
        System.out.println(mediafile);
        MediafileService.updateMediafile(mediafile);
*/
    }


}
