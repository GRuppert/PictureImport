package org.nyusziful.pictureorganizer.Service;

import org.nyusziful.pictureorganizer.DAL.DAO.MediaFileDAO;
import org.nyusziful.pictureorganizer.DAL.DAO.MediaFileDAOImplHib;
import org.nyusziful.pictureorganizer.DAL.Entity.*;
import org.nyusziful.pictureorganizer.DTO.Meta;

import java.util.*;

import static org.nyusziful.pictureorganizer.UI.StaticTools.*;

public class MediaFileService {
    private MediaFileDAO mediafileDAO;
    private static MediaFileService instance;

    private MediaFileService() {
        mediafileDAO = new MediaFileDAOImplHib();
    }

    public static MediaFileService getInstance() {
        if (instance == null) {
            instance = new MediaFileService();
        }
        return instance;
    }

    public MediaFile createMediaFile(String name, Meta meta) {
        if (supportedRAWFileType(name)) {
            return new RAWMediaFile(meta.originalFilename, meta.shotnumber);
        } else if (supportedJPGFileType(name)) {
            return new JPGMediaFile(meta.originalFilename, meta.shotnumber, JPGMediaFile.setWithQuality(meta.quality), null);
        } else if (supportedVideoFileType(name)) {
            return new VideoMediaFile(meta.originalFilename, meta.shotnumber);
        } else {
            return new MediaFile(meta.originalFilename, meta.shotnumber);
        }
    }
    public List<MediaFile> getMediafiles() {
        return mediafileDAO.getAll();
    }
    public void saveMediaFile(MediaFile mediafile) {
        saveMediaFile(mediafile, false);
    }

    public void saveMediaFile(MediaFile mediafile, boolean batch) {
        saveMediaFiles(Collections.singleton(mediafile), batch);
    }

    public void saveMediaFiles(Collection<? extends MediaFile> mediaFiles) {
        saveMediaFiles(mediaFiles, false);
    }

    public void saveMediaFiles(Collection<? extends MediaFile> mediaFiles, boolean batch) {
        for (MediaFile file : mediaFiles) {
            if (file.getId() > -1)
                mediafileDAO.merge(file, batch);
            else
                mediafileDAO.persist(file, batch);
        }
    }

    public void updateMediafile(MediaFile Mediafile) {
        mediafileDAO.merge(Mediafile);
    }

    public MediaFile getMediaFileByID(int id) {
        return mediafileDAO.getById(id);
    }

    public void flush() {
        mediafileDAO.flush();
    }

    public void close() {
        mediafileDAO.close();
    }


    /**
     * @param mediaFile
     * @return true if data has been written into the image entity, which has to be persisted
     * @throws Exception if the data in the "original" media file does not match what was already saved into the image
     */
/*    public boolean updateOriginalImage(MediaFile mediaFile) throws Exception {
        final Image image = mediaFile.getImage();
        final Meta metaOrig = getV(mediaFile.getFilename());
        String origFileName = (metaOrig != null && metaOrig.originalFilename != null) ? metaOrig.originalFilename : mediaFile.getFilename();
        if (image.getOriginalFileHash() == null && image.getDateTaken() == null && image.getOriginalFilename() == null) {
            image.setOriginalFileHash(mediaFile.getFilehash());
            image.setDateTaken(mediaFile.getDateStored());
            image.setOriginalFilename(origFileName);
            mediaFile.setOriginal(true);
            if (mediaFile instanceof JPGMediaFile)
                image.setExif(((JPGMediaFile)mediaFile).getExif());
            if (mediaFile instanceof VideoMediaFile)
                image.setDuration(((VideoMediaFile)mediaFile).getDuration());
            return true;
        } else {
            if (!origFileName.equals(image.getOriginalFilename()) || !mediaFile.getFilehash().equals(image.getOriginalFileHash()) || ((image.getDateTaken() == null || mediaFile.getDateStored().compareTo(image.getDateTaken()) != 0) && (image.getActualDate() == null || mediaFile.getDateStored().compareTo(image.getActualDate()) != 0 ))) {
                throw new Exception("Mismatch");
            }
        }
        return false;
    }*/

    public boolean updateBestimateImage(MediaFile mediaFile) throws Exception {
/*        final Image image = mediaFile.getImage();
        final Meta metaOrig = getV(mediaFile.getFilename());
        String origFileName = (metaOrig != null && metaOrig.originalFilename != null) ? metaOrig.originalFilename : mediaFile.getFilename();
        if (image.getOriginalFileHash() == null && image.getDateTaken() == null && image.getOriginalFilename() == null
        && image.getBestimateFileHash() == null && image.getDateCorrected() == null && image.getBestimateFilename() == null) {
            image.setBestimateFileHash(mediaFile.getFilehash());
            image.setDateCorrected(mediaFile.getDateStored());
            image.setBestimateFilename(origFileName);
            return true;
        } else {
            if (!(origFileName.equals(image.getBestimateFilename()) || origFileName.equals(image.getOriginalFilename()))
                    || !(mediaFile.getFilehash().equals(image.getOriginalFileHash()) || mediaFile.getFilehash().equals(image.getBestimateFileHash()))
                    || ((image.getDateTaken() == null || mediaFile.getDateStored().compareTo(image.getDateTaken()) != 0) && (image.getActualDate() == null || mediaFile.getDateStored().compareTo(image.getActualDate()) != 0 ))) {
                throw new Exception("Mismatch");
            }
        }*/
        return false;
    }

    public List<MediaFile> getMediaFileByMediaDirectory(MediaDirectory mediaDirectory) {
        return mediafileDAO.getMediaFileByMediaDirectory(mediaDirectory);
    }

    public MediaFile getMediafileByImage(String hash, String type, Integer shotnumber) {
        return mediafileDAO.getMediafileByImage(hash, type, shotnumber);
    }



}