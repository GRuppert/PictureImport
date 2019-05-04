package org.nyusziful.pictureorganizer.Model;

import java.util.List;

public interface ImageDAO {
    public List<ImageDTO> getImages();
    public ImageDTO getImageByHash(String hash);
    public ImageDTO save(ImageDTO image);
    public void update(ImageDTO image);
}
