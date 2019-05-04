package org.nyusziful.pictureorganizer.Model;

import java.util.List;

public class MediafileDAOImplLocal implements MediafileDAO {
    @Override
    public List<MediafileDTO> getMediafiles() {
        return null;
    }

    @Override
    public MediafileDTO getMediafileByName(String name) {
        return null;
    }

    @Override
    public MediafileDTO getMediafileById(int id) {
        return null;
    }

    @Override
    public int saveMediafile(MediafileDTO Mediafile) {
        return 0;
    }

    @Override
    public boolean deleteMediafile(MediafileDTO Mediafile) {
        return false;
    }
}
