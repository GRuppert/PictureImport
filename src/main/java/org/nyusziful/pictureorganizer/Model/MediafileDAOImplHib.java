package org.nyusziful.pictureorganizer.Model;

import org.nyusziful.pictureorganizer.DB.AbstractDAO;

import java.util.List;

public class MediafileDAOImplHib extends AbstractDAO implements MediafileDAO {
    @Override
    public List<MediafileDTO> getMediafiles() {
        return (List<MediafileDTO>) getCurrentSession().createQuery("from MediafileDTO").list();
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
