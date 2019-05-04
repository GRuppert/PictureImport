package org.nyusziful.pictureorganizer.Model;

import java.util.List;

public interface MediafileDAO {
    public List<MediafileDTO> getMediafiles();
    public MediafileDTO getMediafileByName(String name);
    public MediafileDTO getMediafileById(int id);
    public int saveMediafile(MediafileDTO Mediafile);
    public boolean deleteMediafile(MediafileDTO Mediafile);
}
