package Comparison;


import static ExifUtils.ExifReadWrite.readMeta;
import com.drew.imaging.ImageProcessingException;
import java.io.IOException;
import java.util.ArrayList;
import javafx.beans.property.SimpleBooleanProperty;
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
    private final comparableMediaFile first; 
    private final comparableMediaFile second; 
    private ArrayList<String[]> conflicts;
    public ArrayList<String> footprint;
    private ArrayList<String> unimportant;
    private ArrayList<String> user;

    private SimpleBooleanProperty processing;
    private final SimpleStringProperty firstName;
    private final SimpleStringProperty secondName;
    private final SimpleStringProperty meta;
    
    public duplicate(comparableMediaFile first, comparableMediaFile second, boolean sameImage) {
        unimportant = new ArrayList();
        unimportant.add("Date/Time");
        unimportant.add("Thumbnail Offset");
        unimportant.add("File Size");
        unimportant.add("File Modified Date");
        unimportant.add("File Name");
        unimportant.add("User Comment");
        unimportant.add("xmpMM:InstanceID");
        unimportant.add("Application Record Version");
        unimportant.add("Padding");
        unimportant.add("xmp:ModifyDate");
        unimportant.add("Windows XP Keywords");
        unimportant.add("Caption Digest");
        unimportant.add("Coded Character Set");
        unimportant.add("Enveloped Record Version");
        unimportant.add("Interoperability Index");
        unimportant.add("Interoperability Version");
        unimportant.add("Makernote");
        unimportant.add("Strip Offsets");
        unimportant.add("Unique Image ID");
        unimportant.add("dc:subject[1]");
        unimportant.add("dc:subject[2]");
        unimportant.add("MicrosoftPhoto:Rating");
        unimportant.add("MicrosoftPhoto:LastKeywordIPTC[1]");
        unimportant.add("MicrosoftPhoto:LastKeywordIPTC[2]");
        unimportant.add("MicrosoftPhoto:LastKeywordXMP[1]");
        unimportant.add("MicrosoftPhoto:LastKeywordXMP[2]");
//        unimportant.add("");
        
        user = new ArrayList();
        user.add("GPS Version ID");
        user.add("GPS Altitude Ref");
        user.add("Keywords");
        user.add("Rating");

        this.first = first;
        firstName = new SimpleStringProperty(first.file.getName());
        this.second = second;
        secondName = new SimpleStringProperty(second.file.getName());
        processing = new SimpleBooleanProperty(sameImage);
        meta = new SimpleStringProperty("Equal");
//        if (FilenameUtils.getExtension(first.file.getName().toLowerCase()).equals("jpg"))
/*        try {
            FileType test = FileTypeDetector.detectFileType(new BufferedInputStream(new FileInputStream("E:\\temp\\ARWproof\\DSC01962.ARW")));
            System.out.println(test);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(duplicate.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(duplicate.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        meta.setValue(compareMeta());
    }

    private void addTag(String name, String value1, String value2) {
        if (!unimportant.contains(name)) {
            conflicts.add(new String[]{name, value1, value2});
            if (name.startsWith("Unknown tag (0x")) {if (!footprint.contains("Unknown")) footprint.add("Unknown");} 
            else if (user.contains(name)) {if (!footprint.contains("Mivótunk")) footprint.add("Mivótunk");}
            else footprint.add(name);
        }
    }
    
    private String compareMeta() {
        ArrayList<String[]> tags;
        ArrayList<String[]> tagsBase;
        int realConf = 0;
        conflicts = new ArrayList();
        footprint = new ArrayList();
        try {
            tags = readMeta(first.file);
            tagsBase = readMeta(second.file);
        } catch (ImageProcessingException | IOException ex) {
            return ex.getMessage();
        }
        outerloop:
        for (int i = 0; i < tags.size();) {
            String[] tag = tags.get(i);
            for (int j = 0; j < tagsBase.size();) {
                String[] tagBase = tagsBase.get(j);
                if (tag[0].equals(tagBase[0])) {
                    if (tag[1] != null && !tag[1].equals("")) {
                        if (!tag[1].equals(tagBase[1])) {
                            if (tag[0].equals("Sequence Number")) {
                                footprint = new ArrayList();
                                footprint.add(tag[0]);
                                conflicts = new ArrayList();
                                return "Bracketing";
                            } else {
                                addTag(tag[0], tag[1], tagBase[1]);
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
            addTag(tag[0], tag[1], ""); 
        }
        for (String[] tag : tagsBase) {
            addTag(tag[0], "", tag[1]); 
        }
        return Integer.toString(realConf) + " - " + Integer.toString(conflicts.size() - realConf);
    }

    public final Boolean getProcessing() {return processing.get();}
    public final void setProcessing(Boolean proc) {processing.set(proc);}
    public SimpleBooleanProperty processingProperty() {return processing;}

    public final String getFirstName() {return firstName.get();}
    public final void setFirstName(String proc) {firstName.set(proc);}
    public SimpleStringProperty firstNameProperty() {return firstName;}

    public final String getSecondName() {return secondName.get();}
    public final void setSecondName(String proc) {secondName.set(proc);}
    public SimpleStringProperty secondNameProperty() {return secondName;}

    public final String getMeta() {return meta.get();}
    public final void setMeta(String proc) {meta.set(proc);}
    public SimpleStringProperty metaProperty() {return meta;}

    public ArrayList<String[]> getConflicts() {
        return conflicts;
    }

    public String getDir() {return first.file.getParent();}
}
