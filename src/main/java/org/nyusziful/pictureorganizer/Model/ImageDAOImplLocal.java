package org.nyusziful.pictureorganizer.Model;

import java.util.List;

public class ImageDAOImplLocal extends CRUDDAOImpHib<ImageDTO> implements ImageDAO {
    @Override
    public ImageDTO getImageByHash(String hash) {
        return null;
    }
}
