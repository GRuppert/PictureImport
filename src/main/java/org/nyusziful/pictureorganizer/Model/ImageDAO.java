package org.nyusziful.pictureorganizer.Model;

import java.util.List;

public interface ImageDAO extends CRUDDAO<ImageDTO> {
    public ImageDTO getImageByHash(String hash);
}
