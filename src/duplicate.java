
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

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
    private final boolean sameSize;
    private ArrayList<String> conflicts;

    private SimpleBooleanProperty processing;
    private final SimpleStringProperty firstName;
    private final SimpleStringProperty secondName;
    private final SimpleObjectProperty metaDiffs;
    
    public duplicate(mediaFile first, mediaFile second, boolean sameImage, boolean sameSize) {
        this.first = first;
        firstName = new SimpleStringProperty(first.getCurrentName());
        this.second = second;
        secondName = new SimpleStringProperty(second.getCurrentName());
        this.sameImage = sameImage;
        this.sameSize = sameSize;
        metaDiffs = new SimpleObjectProperty("Equal");
        processing = new SimpleBooleanProperty(true);
        if (sameImage && !sameSize) metaDiffs.setValue(compareMeta());
        Object test = metaDiffs.getValue();
        System.out.println(test);
    }

    private Object compareMeta() {
        String warnings = "";
        String errors = "";
        String[] res;
        Metadata metadata;
        Metadata metadataBase;
        ArrayList<String[]> tags = new ArrayList();
        ArrayList<String[]> tagsBase = new ArrayList();
        ArrayList<String[]> differences = new ArrayList();
        ArrayList<String> unimportant = new ArrayList();
        unimportant.add("Date/Time");
        unimportant.add("Thumbnail Offset");
        unimportant.add("File Size");
        unimportant.add("File Modified Date");
        unimportant.add("File Name");
        unimportant.add("User Comment");
        try {
            metadata = ImageMetadataReader.readMetadata(first.getFile());
            metadataBase = ImageMetadataReader.readMetadata(second.getFile());
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
                                    differences.add(new String[]{tag[0], tag[1], tagBase[1]});
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
                errors += "+" + tag[0] + ":" + tag[1] + "\n";
                differences.add(new String[]{tag[0], tag[1], ""});
            }
            for (String[] tag : tagsBase) {
                warnings += "-" + tag[0] + ":" + tag[1] + "\n";
                differences.add(new String[]{tag[0], "", tag[1]});
            }
        } catch (ImageProcessingException e) {
            StaticTools.errorOut(firstName.getValue(), e);         
            errors += e.getMessage() + " ";
        } catch (IOException e) {
            StaticTools.errorOut(firstName.getValue(), e);         
            errors += e.getMessage() + " ";
        }
        res = new String[]{errors, warnings};
        return differences.toArray();
    }

    public final Boolean getProcessing() {return processing.get();}
    public final void setProcessing(Boolean proc) {processing.set(proc);}
    public SimpleBooleanProperty processingProperty() {return processing;}

    public final String getFirstName() {return firstName.get();}
    public final void setFirstName(String proc) {firstName.set(proc);}
    public SimpleStringProperty processingFirstName() {return firstName;}

    public final String getSecondName() {return secondName.get();}
    public final void setSecondName(String proc) {secondName.set(proc);}
    public SimpleStringProperty processingSecondName() {return secondName;}

    public final Object getMeta() {
        return metaDiffs.get();
    }
    public final void setMeta(Object proc) {metaDiffs.set(proc);}
    public SimpleObjectProperty processingMeta() {
        return metaDiffs;
    }
}
