
import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPIterator;
import com.adobe.xmp.XMPMeta;
import com.adobe.xmp.properties.XMPPropertyInfo;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.xmp.XmpDirectory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author gabor
 */
public class duplicate {
    private final mediaFile first; 
    private final mediaFile second; 
    private final boolean sameImage;
    
    public duplicate(mediaFile first, mediaFile second, boolean sameImage) {
        this.first = first;
        this.second = second;
        this.sameImage = sameImage;
    }

    private String[] compareMeta(File fileMeta, File basefileMeta) {
        String warnings = "";
        String errors = "";
        String[] res;
        if (fileMeta.exists() && basefileMeta.exists()) {
            Metadata metadata;
            Metadata metadataBase;
            ArrayList<String[]> tags = new ArrayList();
            ArrayList<String[]> tagsBase = new ArrayList();
            ArrayList<String> unimportant = new ArrayList();
            unimportant.add("Date/Time");
            unimportant.add("Thumbnail Offset");
            unimportant.add("File Size");
            unimportant.add("File Modified Date");
            unimportant.add("File Name");
            unimportant.add("User Comment");
            try {
                metadata = ImageMetadataReader.readMetadata(fileMeta);
                metadataBase = ImageMetadataReader.readMetadata(basefileMeta);
                for (Directory directoryBase : metadataBase.getDirectories()) {
                    if (!directoryBase.getClass().equals(XmpDirectory.class))
                        for (Tag tagBase : directoryBase.getTags()) {
                            String[] temp = {tagBase.getTagName(), tagBase.getDescription()};
                            tagsBase.add(temp);
                        }
                }
                for (Directory directory : metadata.getDirectories()) {
                    if (!directory.getClass().equals(XmpDirectory.class))
                        for (Tag tag : directory.getTags()) {
                            String[] temp = {tag.getTagName(), tag.getDescription()};
                            tags.add(temp);
                        }
                }
                Collection<XmpDirectory> xmpDirectories = metadata.getDirectoriesOfType(XmpDirectory.class);
                for (XmpDirectory xmpDirectory : xmpDirectories) {
                    XMPMeta xmpMeta = xmpDirectory.getXMPMeta();
                    XMPIterator iterator;
                    try {
                        iterator = xmpMeta.iterator();
                        while (iterator.hasNext()) {
                            XMPPropertyInfo xmpPropertyInfo = (XMPPropertyInfo)iterator.next();
                            if (xmpPropertyInfo.getPath() != null && xmpPropertyInfo.getValue() != null) {
                                String[] temp = {xmpPropertyInfo.getPath(), xmpPropertyInfo.getValue()};
                                tagsBase.add(temp);
                            }
                        }
                    } catch (XMPException ex) {
                        Logger.getLogger(PicOrganizes.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }                    
                Collection<XmpDirectory> xmpDirectoriesBase = metadata.getDirectoriesOfType(XmpDirectory.class);
                for (XmpDirectory xmpDirectory : xmpDirectoriesBase) {
                    XMPMeta xmpMeta = xmpDirectory.getXMPMeta();
                    XMPIterator iterator;
                    try {
                        iterator = xmpMeta.iterator();
                        while (iterator.hasNext()) {
                            XMPPropertyInfo xmpPropertyInfo = (XMPPropertyInfo)iterator.next();
                            if (xmpPropertyInfo.getPath() != null && xmpPropertyInfo.getValue() != null) {
                                String[] temp = {xmpPropertyInfo.getPath(), xmpPropertyInfo.getValue()};
                                tagsBase.add(temp);
                            }
                        }
                    } catch (XMPException ex) {
                        Logger.getLogger(PicOrganizes.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }                    

                outerloop:
                for (int i = 0; i < tags.size();) {
                    String[] tag = tags.get(i);
                    for (int j = 0; j < tagsBase.size();) {
                        String[] tagBase = tagsBase.get(j);
                        if (tag[0].equals(tagBase[0])) {
                            if (tag[1] != null && !tag[1].equals("")) {
                                if (!tag[1].equals(tagBase[1])) {
                                    if (!unimportant.contains(tag[0])) {
                                        errors += tag[0] + ":" + tag[1] + "-><-" +tagBase[1] + " | ";
                                    }
                                }
                                tagsBase.remove(tagBase);                                
                            }
                            tags.remove(tag);
                            continue outerloop;
                        }
                        j++;
                    }
                    i++;
                }
                for (String[] tag : tags) {
                    errors += "+" + tag[0] + ":" + tag[1] + " | ";
                }
                for (String[] tag : tagsBase) {
                    warnings += "-" + tag[0] + ":" + tag[1] + " | ";
                }
            } catch (ImageProcessingException e) {
                StaticTools.errorOut(fileMeta.getName(), e);         
                errors += e.getMessage() + " ";
            } catch (IOException e) {
                StaticTools.errorOut(fileMeta.getName(), e);         
                errors += e.getMessage() + " ";
            }
        } else {
            errors += "MetaData unavailable ";
        }
        res = new String[]{errors, warnings};
        return res;
    }


    
}
