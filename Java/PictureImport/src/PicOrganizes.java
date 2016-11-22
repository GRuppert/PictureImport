import java.util.ArrayList;
import java.util.Iterator;
import java.util.TimeZone;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileSystemView;

public class PicOrganizes extends Application {        
	Path toDir = Paths.get("G:\\Pictures\\Fényképek\\Közös\\");
        //Path toDir = Paths.get("E:\\exiftoolTest");
        
        File fromDir = new File("E:\\exiftoolTest");
        //File fromDir = new File("G:\\Pictures\\!Válogatós\\Közös");
        TimeZone timeZoneLocal = TimeZone.getTimeZone("Europe/Budapest");

        ObservableList<String> cameras = FXCollections.observableArrayList(
            "NA",
            "ILCE-5100",
            "ILCE-6000",
            "GT-I9192",
            "GT-I9195I",
            "Lumia 1020",
            "FinePix S5800 S800"
        );
        ComboBox camera = new ComboBox(cameras);
        String naModel;

	static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        static int MOVE = 1;
        static int COPY = 0;
        int copyOrMove;
        int maxWidth, maxHeight;
        final ProgressIndicator progressIndicator = new ProgressIndicator(0);
        private TableView<fileLocs> table = new TableView<>();
        private final ObservableList<fileLocs> data = FXCollections.observableArrayList();
        
        public static class fileLocs {
            private final SimpleBooleanProperty processing;
            private final SimpleStringProperty oldName;
            private final SimpleStringProperty newName;

            private fileLocs(String oName, String nName) {
                this.oldName = new SimpleStringProperty(oName);
                this.newName = new SimpleStringProperty(nName);
                this.processing = new SimpleBooleanProperty(true);
            }

            public final String getOldName() {
                return oldName.get();
            }
            public final void setOldName(String fName) {
                oldName.set(fName);
            }
            public SimpleStringProperty oldNameProperty() {
                return oldName;
            }

            public final String getNewName() {
                return newName.get();
            }
            public final void setNewName(String fName) {
                newName.set(fName);
            }
            public SimpleStringProperty newNameProperty() {
                return newName;
            }

            public final Boolean getProcessing() {
                return processing.get();
            }
            public final void setProcessing(Boolean fName) {
                processing.set(fName);
            }
            public SimpleBooleanProperty processingProperty() {
                return processing;
            }
            
            public Path getOldPath() {
                return Paths.get(oldName.get());
            }
            
            public Path getNewPath() {
                return Paths.get(newName.get());
            }
        }        
        
        private ArrayList<String> chooseDirectories() {
		ArrayList<File> drives = new ArrayList<File>();
		ArrayList<String> list = new ArrayList<String>();
                File[] paths;
                FileSystemView fsv = FileSystemView.getFileSystemView();
                paths = File.listRoots();
		ArrayList<String> Sony = new ArrayList<String>();
		Sony.add("DCIM\\");
		Sony.add("PRIVATE\\AVCHD\\BDMV\\STREAM\\");
		Sony.add("PRIVATE\\M4ROOT\\CLIP\\");
		ArrayList<String> Samsung = new ArrayList<String>();
		Samsung.add("DCIM\\Camera");
		Samsung.add("WhatsApp\\Media\\WhatsApp Images");

                for(File path:paths)
                {
                    String desc = fsv.getSystemTypeDescription(path);
                    if (desc.startsWith("USB") || desc.startsWith("SD")) drives.add(path);
                }
                
                for(File drive:drives) {                    
                    boolean valid = true;
                    for(String criteria:Sony) {
                        File probe = new File(drive+criteria);
                        if(probe.exists() && probe.isDirectory()) {
                            continue;
                        }
                        valid = false;
                        break;
                    }
                    if (valid) {
                        for (File subdir:new File(drive+Sony.get(0)).listFiles((new FilenameFilter() {public boolean accept(File dir, String name) {return dir.isDirectory();}}))) {
                            list.add(subdir.toString());
                        }
                        list.add(drive+Sony.get(1));
                        list.add(drive+Sony.get(2));
                    }
                }
                return list;		
	}

       
        private void list_Out(ArrayList<Path[]> objects) {
            data.removeAll(data);
            for (Path[] obj:objects) {
              data.add(new fileLocs(obj[0].toString(), obj[1].toString()));
            }
        }
        
        private void errorOut(String msg) {
            JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
        }
                       
	private void renameFiles(ArrayList<String> directories, Path target) {
            Iterator<String> iter = directories.iterator();
            while(iter.hasNext()) {
                    File dir1 = new File(iter.next());
                    if(dir1.isDirectory()) {
                        ArrayList<Path[]> files = new ArrayList<>();
                        File[] content = dir1.listFiles(new FilenameFilter() {
                            public boolean accept(File dir, String name) {
                                    name = name.toLowerCase();
                                return name.endsWith(".mts") || name.endsWith(".arw") || name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".mp4") || name.endsWith(".avi") || name.endsWith(".mov") || name.endsWith(".mpg") || name.endsWith(".3gp") || name.endsWith(".nef") || name.endsWith(".png") || name.endsWith(".dng") || name.endsWith(".tif") || name.endsWith(".gpx") || name.endsWith(".nar") || name.endsWith(".pdf") || name.endsWith(".gif");
                            }});
                        if (content.length > 0) {
                            for(int i = 0; i < content.length; i++) {
                                if (content[i].getName().toLowerCase().endsWith(".gpx") || content[i].getName().toLowerCase().endsWith(".nar") || content[i].getName().toLowerCase().endsWith(".pdf")) {
                                    files.add(new Path[] {content[i].toPath(), Paths.get(target + "\\" + content[i].getName())});
                                    progressIndicator.setProgress(i/content.length);
                                    continue;
                                }
                                if (content[i].getName().toLowerCase().endsWith(".mp4")) {
                                    String thmb = content[i].toString().replaceFirst("CLIP", "THMBNL").replaceFirst(".MP4", "T01.JPG");
                                    File thmbF = new File(thmb);
                                    if (thmbF.exists()) {
                                        String thmbMeta = readMetaDataMove(thmbF).toString();
                                        Path vid = Paths.get(target + thmbMeta.replaceFirst("T01.JPG", ".MP4"));
                                        files.add(new Path[] {content[i].toPath(), vid});
                                        progressIndicator.setProgress(i/content.length);
                                        continue;
                                    }
                                }
                                files.add(new Path[] {content[i].toPath(), Paths.get(target + readMetaDataMove(content[i]).toString())});
                                progressIndicator.setProgress(i/content.length);
                            }
                        }
                        if (files.size() > 0) {       
                            list_Out(files);
                        }
                    }				
            }
	}
        
	private void moveFiles(ArrayList<String> directories) {
            Iterator<String> iter = directories.iterator();
            while(iter.hasNext()) {
                File dir1 = new File(iter.next());
                if(dir1.isDirectory()) {
                    File[] content = dir1.listFiles(new FilenameFilter() {
                        public boolean accept(File dir, String name) {
                                name = name.toLowerCase();
                            return name.endsWith(".mts") || name.endsWith(".arw") || name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".mp4") || name.endsWith(".dng");
                        }});
                    if (content.length > 0) {
                        Path target = null;
                        String oldName = "";
                        ArrayList<Path[]> files = new ArrayList<>();
                        for(int i = 0; i < content.length; i++) {
                            Path source = content[i].toPath();
                            String fileName = content[i].getName().substring(0, 8);
                            GregorianCalendar cal = new GregorianCalendar();
                            if (fileName != oldName) {
                                Date fileDate = new GregorianCalendar(Integer.parseInt(fileName.substring(0, 4)), Integer.parseInt(fileName.substring(4, 6))-1, Integer.parseInt(fileName.substring(6, 8))).getTime();
                                File[] dirs = fromDir.listFiles(new FilenameFilter() {public boolean accept(File dir, String name) {return dir.isDirectory();}});
                                for(int j = 0; j < dirs.length; j++) {
                                    String actDir = dirs[j].getName();
                                    int sY = Integer.parseInt(actDir.substring(0, 4)), sM = Integer.parseInt(actDir.substring(5, 7))-1, sD = Integer.parseInt(actDir.substring(8, 10));
                                    int eY = Integer.parseInt(actDir.substring(13, 17)), eM = Integer.parseInt(actDir.substring(18, 20))-1, eD = Integer.parseInt(actDir.substring(21, 23));
                                    cal.clear();
                                    cal.set(sY, sM, sD);
                                    Date startDate = cal.getTime();
                                    cal.set(eY, eM, eD);
                                    Date endDate = cal.getTime();
//                                        Date startDate = new GregorianCalendar(sY, sM, sD).getTime();
//                                        Date endDate = new GregorianCalendar(eY, eM, eD).getTime();
                                    if (!(fileDate.before(startDate) || fileDate.after(endDate))) {
                                        target = dirs[j].toPath();
                                        files.add(new Path[] {source, Paths.get(target + "\\" + content[i].getName())});
                                        j = dirs.length;
                                   }
                                }
                            }    
                            oldName = fileName;
                        }
                        list_Out(files);
                    }
                }				
            }
            
        }

	private void removeFiles() {
            File[] directories = toDir.toFile().listFiles((File dir, String name) -> dir.isDirectory());
            for (File dir1 : directories) {
                if(dir1.isDirectory()) {
                    File[] content = dir1.listFiles(new FilenameFilter() {
                        public boolean accept(File dir, String name) {
                                name = name.toLowerCase();
                            return name.endsWith("__highres.jpg");
                        }});
                    if (content.length > 0) {
                        for(int i = 0; i < content.length; i++) {
                            String absolutePath = content[i].getAbsolutePath();
                            try {
                                Files.deleteIfExists(Paths.get(absolutePath.substring(0, absolutePath.length()-13) + ".jpg"));
                            } catch (IOException e) {
                                errorOut(e.getMessage());
                            }
                        }
                    }
                }				
            }
            
        }

        private static ArrayList<Object[]> readDirectoryContent(Path path) {
            final ArrayList<Object[]> files = new ArrayList();
            try
            {
                Files.walkFileTree (path, new SimpleFileVisitor<Path>() 
                {
                      @Override public FileVisitResult 
                    visitFile(Path file, BasicFileAttributes attrs) {
                            if (!attrs.isDirectory() && attrs.isRegularFile()) {
                                files.add(new Object[]{file.toString(), sdf.format(attrs.lastModifiedTime().toMillis()), attrs.size()});                               
                            }
                            return FileVisitResult.CONTINUE;                            
                        }

                      @Override public FileVisitResult 
                    visitFileFailed(Path file, IOException exc) {
                            System.out.println("skipped: " + file + " (" + exc + ")");
                            // Skip folders that can't be traversed
                            return FileVisitResult.CONTINUE;
                        }

                      @Override public FileVisitResult
                    postVisitDirectory (Path dir, IOException exc) {
                            if (exc != null)
                                System.out.println("had trouble traversing: " + dir + " (" + exc + ")");
                            // Ignore errors traversing a folder
                            return FileVisitResult.CONTINUE;
                        }
                });
            }
            catch (IOException e)
            {
                throw new AssertionError ("walkFileTree will not throw IOException if the FileVisitor does not");
            }
            return files;
        } 
        
        private void compare() {
            long startTime = System.currentTimeMillis();
            ArrayList<Object[]> readDirectoryContent = readDirectoryContent(fromDir.toPath());
            long stopTime = System.currentTimeMillis();
            long elapsedTime = stopTime - startTime;
            CsvParserSettings csvParserSettings = new CsvParserSettings();
            CsvParser parser = new CsvParser(new CsvParserSettings());
            try {
                List<String[]> filterData = parser.parseAll(new FileReader("e:\\filter.csv"));
                List<String[]> backupData = parser.parseAll(new FileReader("e:\\fekete.csv"));
                ArrayList<String[]> sizeMismatch = new ArrayList();
                ArrayList<String[]> wrong = new ArrayList();
                data:
                for (String[] data : backupData) {
                    for (String[] filter : filterData) {
                        if (filter[0].endsWith(data[0])) {
                            if (filter[1].equals(data[1])) {
                                break data;
                            } else {
                                sizeMismatch.add(data);
                                break data;
                            }
                        }
                    }
                    wrong.add(data);
                }
                System.out.println("Ennyi: " + sizeMismatch.size() + "/" + wrong.size() + "/" + backupData.size());
                
            } catch (FileNotFoundException ex) {
                Logger.getLogger(PicOrganizes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
               
	public Path readMetaDataMove(File file) {
        naModel = camera.getSelectionModel().getSelectedItem().toString();
        String modelF = camera.getSelectionModel().getSelectedItem().toString();
        String dateF = sdf.format(file.lastModified());
        GregorianCalendar calF = new GregorianCalendar(); 
        calF.setTimeInMillis(file.lastModified());
        if (file.getName().toLowerCase().endsWith(".3gp")) {
            calF.add(Calendar.HOUR, -2);
            int shift = 2;
            int oldHour = Integer.parseInt(dateF.substring(9, 11));
            int newHourI = oldHour + shift;
            if (newHourI > 23) {newHourI -= 24;}
            String newHour = Integer.toString(newHourI);
            if (newHourI < 10) newHour = "0" + newHour;
            if (oldHour > 23 - shift) {
                int newDay = Integer.parseInt(dateF.substring(6, 8)) + 1;
                if (newDay > 29) {errorOut("Hónapváltás? " + dateF + " : " + file.getName());}
                dateF = dateF.substring(0, 6) + Integer.toString(newDay) + "_" + newHour + dateF.substring(11);
            } else {
                dateF = dateF.substring(0, 9) + newHour + dateF.substring(11);
            }

        }
 //       readMetaData(file);
        Metadata metadata;
        try {
            metadata = ImageMetadataReader.readMetadata(file);
            Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            if (directory != null)
                for (Tag tag : directory.getTags()) {
                    if (tag.getTagName().equals("Model")) modelF = tag.getDescription();
                    if (tag.getTagName().equals("Date/Time")) dateF = tag.getDescription();
                }
            Iterable<ExifSubIFDDirectory> directories2 = metadata.getDirectoriesOfType(ExifSubIFDDirectory.class);
            for (ExifSubIFDDirectory directory2 : directories2) {
//            ExifSubIFDDirectory directory2 = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
                if (directory2 != null) {
                    Date dateOrig = directory2.getDateOriginal(timeZoneLocal);
                    if (dateOrig != null)
                        dateF = sdf.format(dateOrig);

                }
            }

     } catch (ImageProcessingException e) {
//              errorOut(e.toString());         
        } catch (IOException e) {
              errorOut(file.getName() +e.toString());
        }
        dateF = dateF.replace(":", "");
        dateF = dateF.replace(" ", "_");
        modelF = modelF.replaceAll("[<>:\"\\/|?*]", "!");
        printOut(calF);
        if (file.getName().startsWith(dateF + "_" + modelF + "-")) {
            return Paths.get("\\" + file.getName());
        }
        return Paths.get("\\" + dateF + "_" + modelF + "-" + file.getName());
		
	}
               
	private void nameFiles(ArrayList<String> directories, Path target) {
            Iterator<String> iter = directories.iterator();
            while(iter.hasNext()) {
                    File dir1 = new File(iter.next());
                    if(dir1.isDirectory()) {
                        ArrayList<Path[]> files = new ArrayList<>();
                        File[] content = dir1.listFiles(new FilenameFilter() {
                            public boolean accept(File dir, String name) {
                                    name = name.toLowerCase();
                                return name.endsWith(".mts") || name.endsWith(".arw") || name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".mp4") || name.endsWith(".avi") || name.endsWith(".mov") || name.endsWith(".mpg") || name.endsWith(".3gp") || name.endsWith(".nef") || name.endsWith(".png") || name.endsWith(".dng") || name.endsWith(".tif") || name.endsWith(".gpx") || name.endsWith(".nar") || name.endsWith(".pdf") || name.endsWith(".gif");
                            }});
                        if (content.length > 0) {
                            for(int i = 0; i < content.length; i++) {
                                if (content[i].getName().toLowerCase().endsWith(".gpx") || content[i].getName().toLowerCase().endsWith(".nar") || content[i].getName().toLowerCase().endsWith(".pdf")) {
                                    files.add(new Path[] {content[i].toPath(), Paths.get(target + "\\" + content[i].getName())});
                                    progressIndicator.setProgress(i/content.length);
                                    continue;
                                }
                                if (content[i].getName().toLowerCase().endsWith(".mp4")) {
                                    String thmb = content[i].toString().replaceFirst("CLIP", "THMBNL").replaceFirst(".MP4", "T01.JPG");
                                    File thmbF = new File(thmb);
                                    if (thmbF.exists()) {
                                        String thmbMeta = readMetaDataRe(thmbF).toString();
                                        Path vid = Paths.get(target + thmbMeta.replaceFirst("T01.JPG", ".MP4"));
                                        files.add(new Path[] {content[i].toPath(), vid});
                                        progressIndicator.setProgress(i/content.length);
                                        continue;
                                    }
                                }
                                files.add(new Path[] {content[i].toPath(), Paths.get(target + readMetaDataRe(content[i]).toString())});
                                progressIndicator.setProgress(i/content.length);
                            }
                        }
                        if (files.size() > 0) {       
                            list_Out(files);
                        }
                    }				
            }
	}
        
	public Path readMetaDataRe(File file) {
        naModel = camera.getSelectionModel().getSelectedItem().toString();
        String modelF = camera.getSelectionModel().getSelectedItem().toString();
        String dateF = sdf.format(file.lastModified());
        GregorianCalendar calF = new GregorianCalendar(); 
        calF.setTimeInMillis(file.lastModified());
        if (file.getName().toLowerCase().endsWith(".3gp")) {
            calF.add(Calendar.HOUR, -2);
            int shift = 2;
            int oldHour = Integer.parseInt(dateF.substring(9, 11));
            int newHourI = oldHour + shift;
            if (newHourI > 23) {newHourI -= 24;}
            String newHour = Integer.toString(newHourI);
            if (newHourI < 10) newHour = "0" + newHour;
            if (oldHour > 23 - shift) {
                int newDay = Integer.parseInt(dateF.substring(6, 8)) + 1;
                if (newDay > 29) {errorOut("Hónapváltás? " + dateF + " : " + file.getName());}
                dateF = dateF.substring(0, 6) + Integer.toString(newDay) + "_" + newHour + dateF.substring(11);
            } else {
                dateF = dateF.substring(0, 9) + newHour + dateF.substring(11);
            }

        }
 //       readMetaData(file);
        Metadata metadata;
        try {
            metadata = ImageMetadataReader.readMetadata(file);
            Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            if (directory != null)
                for (Tag tag : directory.getTags()) {
                    if (tag.getTagName().equals("Model")) modelF = tag.getDescription();
                    if (tag.getTagName().equals("Date/Time")) dateF = tag.getDescription();
                }
            Iterable<ExifSubIFDDirectory> directories2 = metadata.getDirectoriesOfType(ExifSubIFDDirectory.class);
            for (ExifSubIFDDirectory directory2 : directories2) {
//            ExifSubIFDDirectory directory2 = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
                if (directory2 != null) {
                    Date dateOrig = directory2.getDateOriginal(timeZoneLocal);
                    if (dateOrig != null) {
                        dateF = sdf.format(dateOrig);
                        calF.setTimeInMillis(dateOrig.getTime());
                    }
                }
            }

     } catch (ImageProcessingException e) {
//              errorOut(e.toString());         
        } catch (IOException e) {
              errorOut(file.getName() +e.toString());
        }
        dateF = dateF.replace(":", "");
        dateF = dateF.replace(" ", "_");
        modelF = modelF.replaceAll("[<>:\"\\/|?*]", "!");
        String newForm = printOut(calF);
        String addition = dateF + "_" + modelF + "-";
        String oldFileName = file.getName();
        if (oldFileName.startsWith(addition)) {
            oldFileName = oldFileName.substring(addition.length());
        }
        return Paths.get("\\" + newForm + oldFileName);
		
	}
        
        public String printOut(GregorianCalendar calendar) {
            int offsetInMillis = (calendar.get(Calendar.ZONE_OFFSET)+calendar.get(Calendar.DST_OFFSET));
            calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
            String offset = String.format("%02d%02d", Math.abs(offsetInMillis / 3600000), Math.abs((offsetInMillis / 60000) % 60));
            return "K" + calendar.get(Calendar.YEAR) + "-" + String.format("%02d", calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.WEEK_OF_MONTH) + "-" + calendar.get(Calendar.DAY_OF_WEEK) + "_" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + "-" + String.format("%02d", calendar.get(Calendar.MINUTE)) + "-" + String.format("%02d", calendar.get(Calendar.SECOND)) + "_" + ((offsetInMillis<0) ? "m" : "p") + offset + "-";
        }

        public void createXMP(File file) {
         //exiftool -ext jpg -tagsfromfile -@ exif2xmp.args -@ iptc2xmp.args %d%f.xmp
         // -ext EXT
        }
        
	public void readMetaDataTest(File file) {
            String modelF = camera.getSelectionModel().getSelectedItem().toString();
            String dateF = sdf.format(file.lastModified());
            Metadata metadata;
            try {
                metadata = ImageMetadataReader.readMetadata(file);

                Iterable<Directory> directories = metadata.getDirectories();
                for (Directory directory : directories) {
                    System.out.println("\n" + directory.getName() + "-------------\n");
                    for (Tag tag : directory.getTags()) {
                        System.out.println(tag.getTagName() + " : " + tag.getDescription());
                     }
                }
            } catch (ImageProcessingException e) {
    //              errorOut(e.toString());         
            } catch (IOException e) {
                  errorOut(file.getName() +e.toString());
            }
	}
        
        @Override
        public void start(Stage primaryStage) {
            DirectoryChooser chooser = new DirectoryChooser();
            final ToggleGroup group = new ToggleGroup();
            group.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
                public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
                    if (group.getSelectedToggle() != null) {
                        copyOrMove = (int) group.getSelectedToggle().getUserData();
                    }                
                }
            });
            RadioButton rbCopy = new RadioButton("Copy");
            rbCopy.setToggleGroup(group);
            rbCopy.setUserData(COPY);
            rbCopy.setSelected(true);
            RadioButton rbMove = new RadioButton("Move");
            rbMove.setUserData(MOVE);
            rbMove.setToggleGroup(group);

                Button btnImport = new Button("Import");
                btnImport.setOnAction(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event) {
                        ArrayList<String> directories = chooseDirectories(); 
                        renameFiles(directories, toDir); 
                    }
                });

                Button btnRename = new Button("Átnevezés");
                btnRename.setOnAction(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event) {
                        chooser.setInitialDirectory(fromDir);
                        File file = chooser.showDialog(primaryStage);
                        if(file != null) {
                            ArrayList<String> directories = new ArrayList<String>();
                            directories.add(file.toString());
                            Path tempDir = Paths.get(toDir.toString() + "\\" + file.getName());
                            renameFiles(directories, tempDir); 
                        }                    
                    }
                });

                Button btnName = new Button("Új nevezék");
                btnName.setOnAction(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event) {
                        chooser.setInitialDirectory(fromDir);
                        File file = chooser.showDialog(primaryStage);
                        if(file != null) {
                            ArrayList<String> directories = new ArrayList<String>();
                            directories.add(file.toString());
                            Path tempDir = Paths.get(toDir.toString() + "\\" + file.getName());
                            nameFiles(directories, tempDir); 
                        }                    
                    }
                });

                Button btnMove = new Button("Szétszórás");
                btnMove.setOnAction(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event) {
                        chooser.setInitialDirectory(toDir.toFile());
                        File file = chooser.showDialog(primaryStage);
                        if(file != null) {
                            ArrayList<String> directories = new ArrayList<String>();
                            directories.add(file.toString());
                            moveFiles(directories); 
                        }                    
                    }
                });

                Button btnShow = new Button("Könyvtárak");
                btnShow.setOnAction(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event) {
                        File[] dirs = toDir.toFile().listFiles(new FilenameFilter() {public boolean accept(File dir, String name) {return dir.isDirectory();}});
                        for(int j = 0; j < dirs.length; j++) {
                                File[] content = dirs[j].listFiles();
    /*                              File[] content = dirs[j].listFiles(new FilenameFilter() {
                                    public boolean accept(File dir, String name) {
                                        name = name.toLowerCase();
                                    return name.endsWith(".mts") || name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".mp4");
                                }});                                
    */
                                System.out.println(dirs[j].getName() + " : " + content.length);
                        }
                        GregorianCalendar cal = new GregorianCalendar();
                        File[] dirc = fromDir.listFiles(new FilenameFilter() {public boolean accept(File dir, String name) {return dir.isDirectory();}});
                        Date startDate = cal.getTime();
                        Date endDate = cal.getTime();
                        String oldDir = "";
                        for(int j = 0; j < dirc.length; j++) {
                            String actDir = dirc[j].getName();
                            int sY = Integer.parseInt(actDir.substring(0, 4)), sM = Integer.parseInt(actDir.substring(5, 7))-1, sD = Integer.parseInt(actDir.substring(8, 10));
                            int eY = Integer.parseInt(actDir.substring(13, 17)), eM = Integer.parseInt(actDir.substring(18, 20))-1, eD = Integer.parseInt(actDir.substring(21, 23));
                            cal.clear();
                            cal.set(sY, sM, sD);
                            startDate = cal.getTime();
                            cal.set(eY, eM, eD);
    //                                        Date startDate = new GregorianCalendar(sY, sM, sD).getTime();
    //                                        Date endDate = new GregorianCalendar(eY, eM, eD).getTime();
                            if (!(endDate.before(startDate)) && j > 0) {
                                System.out.println(oldDir + " -><- " + actDir);
                            }
                            oldDir = actDir;
                            endDate = cal.getTime();
                        }
                    }
                });
            
            BorderPane root = new BorderPane();            
                VBox settings = new VBox();
                    settings.getChildren().add(rbCopy);
                    settings.getChildren().add(rbMove);
            root.setLeft(settings);
                HBox header = new HBox();
                    header.setAlignment(Pos.CENTER);
                    header.getChildren().addAll(btnImport, btnRename, btnMove, btnShow, btnName);
                HBox fromTo = new HBox();
                    Label from = new Label(fromDir.toString());
                    Button btnFrom = new Button("Módosít");
                    btnFrom.setOnAction(new EventHandler<ActionEvent>(){
                        @Override
                        public void handle(ActionEvent event) {
                            if (Files.exists(toDir))
                                chooser.setInitialDirectory(fromDir);
                            else
                                chooser.setInitialDirectory(new File("C:\\"));
                            File file = chooser.showDialog(primaryStage);
                            if (file != null) {
                                fromDir = file;
                                from.setText(fromDir.toString());
                            }
                        }
                    });
                    Label to = new Label(toDir.toString());
                    Button btnTo = new Button("Módosít");
                    btnTo.setOnAction(new EventHandler<ActionEvent>(){
                        @Override
                        public void handle(ActionEvent event) {
                            if (Files.exists(toDir))
                                chooser.setInitialDirectory(toDir.toFile());
                            else
                                chooser.setInitialDirectory(new File("C:\\"));
                            File file = chooser.showDialog(primaryStage);
                            if (file != null) {
                                toDir = file.toPath();
                                from.setText(toDir.toString());
                            }
                        }
                    });
                    fromTo.getChildren().addAll(from, btnFrom, to, btnTo);                   
                    camera.getSelectionModel().selectFirst();
                VBox head = new VBox();
                head.getChildren().addAll(header, fromTo, camera);
            root.setTop(head);
                table.setEditable(true);
                table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                TableColumn oldNameCol = new TableColumn("Régi név");
                oldNameCol.setCellValueFactory(new PropertyValueFactory<fileLocs, String>("oldName"));
                TableColumn newNameCol = new TableColumn("Új név");
                newNameCol.setCellValueFactory(new PropertyValueFactory<fileLocs, String>("newName"));
                TableColumn processingCol = new TableColumn("Mehet");
                processingCol.setCellValueFactory(new PropertyValueFactory<fileLocs, Boolean>("processing"));
                table.setItems(data);
                table.getColumns().addAll(oldNameCol, newNameCol, processingCol);  
            root.setCenter(table);
                Button btnGo = new Button("Mehet");
                btnGo.setOnAction(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event) {
                        int i = 0;
                        for (fileLocs record : data) {
                            try {                                    
                                if (Files.notExists(record.getNewPath().getParent())) {
                                        Files.createDirectory(record.getNewPath().getParent());
                                }
                                if (copyOrMove == COPY) {
                                    Files.copy(record.getOldPath(), record.getNewPath());                               
                                } else if (copyOrMove == MOVE) {
                                    Files.move(record.getOldPath(), record.getNewPath());                               
                                }
                                } catch (IOException e) {
                                    errorOut(e.getMessage());
                                }                                 
                                progressIndicator.setProgress(i/data.size());
                                i++;
                            }
                        data.removeAll(data);                        
                    }
                });

                Button btnClr = new Button("Mégsem");
                btnClr.setOnAction(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event) {
                        data.removeAll(data);
                    }
                });

                Button btnRefresh = new Button("Frissít");
                btnRefresh.setOnAction(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event) {
                        for(fileLocs paths:data) {
                            Path newPath = paths.getNewPath();
                            String fileName = newPath.getFileName().toString();
                            if (fileName.contains(naModel)) {
                                String replacement = newPath.getParent() + "\\" + fileName.replaceFirst(naModel, camera.getSelectionModel().getSelectedItem().toString());
                                paths.setNewName(replacement);
                            }
                        }
                        naModel = camera.getSelectionModel().getSelectedItem().toString();
                    }
                });
                HBox footer = new HBox();
                    footer.setSpacing(10);
                    footer.setAlignment(Pos.CENTER);
                    footer.getChildren().addAll(btnGo, btnClr, btnRefresh, progressIndicator);
           
            root.setBottom(footer);
            Scene mainScene = new Scene(root);
            primaryStage.setScene(mainScene);
            Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

            //set Stage boundaries to visible bounds of the main screen
            maxWidth = (int) (primaryScreenBounds.getWidth() - 150);
            maxHeight = (int) (primaryScreenBounds.getHeight() - 150);
            primaryStage.setX(primaryScreenBounds.getMinX());
            primaryStage.setY(primaryScreenBounds.getMinY());
            primaryStage.setWidth(maxWidth);
            primaryStage.setHeight(maxHeight);
            primaryStage.show();
 	}       

	public static void main(String[] args) {
            launch(args);
//            readMetaDataTest(new File("e:\\DSC07914.ARW"));
//            removeFiles();
//            compare();
	}
}
