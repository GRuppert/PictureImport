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
import java.text.DateFormat;
import java.text.ParseException;
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
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;
import org.apache.commons.io.FilenameUtils;

public class PicOrganizes extends Application {        
        String pictureSet = "K";
        Path toDir = Paths.get("G:\\Pictures\\Fényképek\\Közös\\");
        //Path toDir = Paths.get("E:\\exiftoolTest");
        
        File fromDir = new File("E:\\Montecarlo");
        //File fromDir = new File("G:\\Pictures\\!Válogatás\\Közös");
        TimeZone timeZoneLocal = TimeZone.getTimeZone("Europe/Budapest");

        static String[] cameras = {
            "NA",
            "ILCE-5100",
            "ILCE-6000",
            "GT-I9192",
            "GT-I9195I",
            "Lumia 1020",
            "FinePix S5800 S800"
        };
        static String[] imageFiles = {
            "jpg",
            "jpeg",
            "png",
            "gif",
            "tif",
            "arw",
            "nef",
            "dng",
            "nar"            
        };
        static String[] videoFiles = {
            "avi",
            "mpg",
            "mp4",
            "mts",
            "3gp",
            "mov"
        };
        static String[] metaFiles = {
            "gpx",
            "sfv",
            "pdf",
            "doc",
            "xls",
            "xlsx"
        };
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
            private final SimpleStringProperty note;

            private fileLocs(String oName, String nName) {
                this.oldName = new SimpleStringProperty(oName);
                this.newName = new SimpleStringProperty(nName);
                this.processing = new SimpleBooleanProperty(true);
                this.note = new SimpleStringProperty("");
            }

            private fileLocs(String oName, String nName, Boolean proc, String nNote) {
                this.oldName = new SimpleStringProperty(oName);
                this.newName = new SimpleStringProperty(nName);
                this.processing = new SimpleBooleanProperty(proc);
                this.note = new SimpleStringProperty(nNote);
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
            
            public final String getNote() {
                return note.get();
            }
            public final void setNote(String fName) {
                note.set(fName);
            }
            public SimpleStringProperty processingNote() {
                return note;
            }
            
            public Path getOldPath() {
                return Paths.get(oldName.get());
            }
            
            public Path getNewPath() {
                return Paths.get(newName.get());
            }
        }        
        
        public static Boolean supportedFileType(String name) {
            if (supportedMetaFileType(name)) return true;
            if (supportedMediaFileType(name)) return true;
            return false;
        }
        public static Boolean supportedMetaFileType(String name) {
            String ext = FilenameUtils.getExtension(name.toLowerCase());
            for (String extSupported : metaFiles) {
                if (ext.equals(extSupported)) return true;
            }
            return false;
            
        }
        public static Boolean supportedMediaFileType(String name) {
            String ext = FilenameUtils.getExtension(name.toLowerCase());
            for (String extSupported : imageFiles) {
                if (ext.equals(extSupported)) return true;
            }
            for (String extSupported : videoFiles) {
                if (ext.equals(extSupported)) return true;
            }
            return false;
        }
        
        private ArrayList<String> chooseDirectories() {
		ArrayList<File> drives = new ArrayList<>();
		ArrayList<String> list = new ArrayList<>();
                File[] paths;
                FileSystemView fsv = FileSystemView.getFileSystemView();
                paths = File.listRoots();
		ArrayList<String> Sony = new ArrayList<>();
		Sony.add("DCIM\\");
		Sony.add("PRIVATE\\AVCHD\\BDMV\\STREAM\\");
		Sony.add("PRIVATE\\M4ROOT\\CLIP\\");
		ArrayList<String> Samsung = new ArrayList<>();
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
                        for (File subdir:new File(drive+Sony.get(0)).listFiles((File dir, String name) -> dir.isDirectory())) {
                            list.add(subdir.toString());
                        }
                        list.add(drive+Sony.get(1));
                        list.add(drive+Sony.get(2));
                    }
                }
                return list;		
	}
       
        private void listOnScreen(ArrayList<fileLocs> newData) {
            data.removeAll(data);
            newData.stream().forEach((obj) -> {data.add(obj);});
        }
                 
        private void errorOut(String source, Exception e) {
            JOptionPane.showMessageDialog(null, "From :" + source + "\nMessage: " + e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        private String originalFileName(File file) {
            String fileName = file.getName();
            if (fileName.length() < 24) return fileName;
            String[] extensionOld = readMetaDataOld(file);
            if (fileName.startsWith(extensionOld[0])) {
                if (fileName.substring(extensionOld[0].length() + 1).startsWith(extensionOld[1])) {
                    return fileName.substring(extensionOld[0].length() + extensionOld[1].length() + 2);
                } else {
                    for (String camera : cameras)
                        if (fileName.substring(extensionOld[0].length() + 1).startsWith(camera)) 
                            return fileName.substring(extensionOld[0].length() + camera.length() + 2);
                    errorOut(fileName + " not recognized camera", new Exception());
                }
            }
            return "";
        }
        
	private ArrayList<fileLocs> fileRenameList(ArrayList<String> directories, Path target) {
            Iterator<String> iter = directories.iterator();
            ArrayList<fileLocs> files = new ArrayList<>();
            while(iter.hasNext()) {
                File dir1 = new File(iter.next());
                if(dir1.isDirectory()) {
                    File[] content = dir1.listFiles((File dir, String name) -> supportedFileType(name));
                    for(int i = 0; i < content.length; i++) {
                        if (supportedMetaFileType(content[i].getName())) {
                            files.add(new fileLocs(content[i].toString(), target + "\\" + content[i].getName()));
                            progressIndicator.setProgress(i/content.length);
                            continue;
                        }
                        String originalFileName = originalFileName(content[i]);
                        String extension = readMetaData(content[i]);
                        if (extension.equals("")) {
                            String xmp = content[i].toString().replaceFirst(FilenameUtils.getExtension(content[i].getName()), ".xmp");
                            File xmpFile = new File(xmp);
                            if (xmpFile.exists()) extension = readMetaData(xmpFile);                            
                        }
                        if (extension.equals("")) {
                            if (content[i].getName().toLowerCase().endsWith(".mp4")) {
                                String thmb = content[i].toString().replaceFirst("CLIP", "THMBNL").replaceFirst(".MP4", "T01.JPG");
                                File thmbF = new File(thmb);
                                if (thmbF.exists()) {
                                    extension = readMetaData(thmbF);
                                }
                            }
                        }
                        if (extension.equals("")) {
                            GregorianCalendar calF = new GregorianCalendar(); 
                            calF.setTimeInMillis(content[i].lastModified());
                            extension = dateFormat(calF);
                            files.add(new fileLocs(content[i].toString(), target + "\\" + extension + originalFileName, false, "No Date in Meta"));
                            progressIndicator.setProgress(i/content.length);
                            continue;
                        }
                        if (originalFileName.equals("")) {
                            files.add(new fileLocs(content[i].toString(), target + "\\" + extension + content[i].getName(), false, "Date Mismatch"));
                            progressIndicator.setProgress(i/content.length);
                            continue;
                        }
                        files.add(new fileLocs(content[i].toString(), target + "\\" + extension + originalFileName));
                        progressIndicator.setProgress(i/content.length);
                    }
                }				
            }
            return files;
	}
        
	private void sortToDateDirectories(ArrayList<String> directories) {
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
                        ArrayList<fileLocs> files = new ArrayList<>();
                        for (File content1 : content) {
                            Path source = content1.toPath();
                            String fileName = content1.getName().substring(0, 8);
                            GregorianCalendar cal = new GregorianCalendar();
                            if (!fileName.equals(oldName)) {
                                Date fileDate = new GregorianCalendar(Integer.parseInt(fileName.substring(0, 4)), Integer.parseInt(fileName.substring(4, 6))-1, Integer.parseInt(fileName.substring(6, 8))).getTime();
                                File[] dirs = fromDir.listFiles((File dir, String name) -> dir.isDirectory());
                                for (int j = 0; j < dirs.length; j++) {
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
                                        files.add(new fileLocs(source.toString(), target + "\\" + content1.getName()));
                                        j = dirs.length;
                                    }
                                }
                            }    
                            oldName = fileName;
                        }
                        listOnScreen(files);
                    }
                }				
            }
            
        }

	public String[] readMetaDataOld(File file) {
            String modelF = "NA";
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
                //Who cares?    
            } catch (IOException e) {
                errorOut(file.getName(), e);         
            }
            dateF = dateF.replace(":", "");
            dateF = dateF.replace(" ", "_");
            modelF = modelF.replaceAll("[<>:\"\\/|?*]", "!");
            dateFormat(calF);
            String extension = dateF + "_" + modelF + "-";
            return new String[] {dateF, modelF};
        }

	private String readMetaData(File file) {
            GregorianCalendar calF = null; 
            Metadata metadata;
            try {
                metadata = ImageMetadataReader.readMetadata(file);
                Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
                if (directory != null)
                    for (Tag tag : directory.getTags()) {
                        if (tag.getTagName().equals("Date/Time")) {
                            String dateString = tag.getDescription(); //2016:11:03 07:50:24
                            DateFormat df = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss"); 
                            Date captureDate;
                            try {
                                captureDate = df.parse(dateString);
                                calF = new GregorianCalendar();
                                calF.setTimeInMillis(captureDate.getTime());
                            } catch (ParseException e) {
                                errorOut(file.getName(), e);         
                            }
                        }
                    }
                Iterable<ExifSubIFDDirectory> directories2 = metadata.getDirectoriesOfType(ExifSubIFDDirectory.class);
                for (ExifSubIFDDirectory directory2 : directories2) {
                    if (directory2 != null) {
                        Date dateOrig = directory2.getDateOriginal(timeZoneLocal);
                        if (dateOrig != null) {
                            calF = new GregorianCalendar();
                            calF.setTimeInMillis(dateOrig.getTime());
                        }
                    }
                }
            } catch (ImageProcessingException e) {
                //Who cares?    
            } catch (IOException e) {
                errorOut(file.getName(), e);         
            }
            if (calF != null) return dateFormat(calF); else return "";
	}
        
        public String dateFormat(GregorianCalendar calendar) {
            int offsetInMillis = (calendar.get(Calendar.ZONE_OFFSET)+calendar.get(Calendar.DST_OFFSET));
            calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
            String offset = String.format("%02d%02d", Math.abs(offsetInMillis / 3600000), Math.abs((offsetInMillis / 60000) % 60));
            Date date = calendar.getTime();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd@HH-mm-ss");
            DateFormat dayFormat = new SimpleDateFormat("EEE");
            String dateS = dateFormat.format(date);
            dateS = dateS.substring(0, 9) + "_" + dateS.substring(9, 15) + "_" + dateS.substring(15) + "_" + dayFormat.format(date) + "(" + ((offsetInMillis<0) ? "m" : "p") + offset + ")-";
            return pictureSet + dateS;
        }// "K2016-11-0_3@07-5_0-24_Thu(p0100)-"

        public String dateFormatV(GregorianCalendar calendar) {
            int offsetInMillis = (calendar.get(Calendar.ZONE_OFFSET)+calendar.get(Calendar.DST_OFFSET));
            calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
            String offset = String.format("%02d%02d", Math.abs(offsetInMillis / 3600000), Math.abs((offsetInMillis / 60000) % 60));
            return pictureSet + calendar.get(Calendar.YEAR) + "-" + String.format("%02d", calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.WEEK_OF_MONTH) + "-" + calendar.get(Calendar.DAY_OF_WEEK) + "_" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + "-" + String.format("%02d", calendar.get(Calendar.MINUTE)) + "-" + String.format("%02d", calendar.get(Calendar.SECOND)) + "_" + ((offsetInMillis<0) ? "m" : "p") + offset + "-";
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
                        listOnScreen(fileRenameList(directories, toDir));
                    }
                });

                Button btnRename = new Button("Rename");
                btnRename.setOnAction(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event) {
                        if (Files.exists(fromDir.toPath()))
                            chooser.setInitialDirectory(fromDir);
                        else
                            chooser.setInitialDirectory(new File("C:\\"));
                        File file = chooser.showDialog(primaryStage);
                        if(file != null) {
                            ArrayList<String> directories = new ArrayList<String>();
                            directories.add(file.toString());
                            Path tempDir = Paths.get(toDir.toString() + "\\" + file.getName());
                            listOnScreen(fileRenameList(directories, tempDir)); 
                        }                    
                    }
                });

                Button btnMove = new Button("Sort to Directories");
                btnMove.setOnAction(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event) {
                        if (Files.exists(toDir))
                            chooser.setInitialDirectory(toDir.toFile());
                        else
                            chooser.setInitialDirectory(new File("C:\\"));
                        File file = chooser.showDialog(primaryStage);
                        if(file != null) {
                            ArrayList<String> directories = new ArrayList<String>();
                            directories.add(file.toString());
                            sortToDateDirectories(directories); 
                        }                    
                    }
                });

                Button btnShow = new Button("Distribute");
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
                    header.getChildren().addAll(btnImport, btnRename, btnMove, btnShow);
                HBox fromTo = new HBox();
                    Label from = new Label(fromDir.toString());
                    Button btnFrom = new Button("Edit");
                    btnFrom.setOnAction(new EventHandler<ActionEvent>(){
                        @Override
                        public void handle(ActionEvent event) {
                            if (Files.exists(fromDir.toPath()))
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
                    Button btnTo = new Button("Edit");
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
                VBox head = new VBox();
                head.getChildren().addAll(header, fromTo);
            root.setTop(head);


            
                table.setEditable(true);
                table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                TableColumn< fileLocs, Boolean > processingCol = new TableColumn<>( "Process" );
                processingCol.setCellValueFactory( f -> f.getValue().processingProperty());
                processingCol.setCellFactory(CheckBoxTableCell.forTableColumn(processingCol));
                processingCol.setPrefWidth(50);
                processingCol.setResizable(false);
                TableColumn oldNameCol = new TableColumn("Current Filename");
                oldNameCol.setCellValueFactory(new PropertyValueFactory<fileLocs, String>("oldName"));
                TableColumn newNameCol = new TableColumn("After Rename");
                newNameCol.setCellValueFactory(new PropertyValueFactory<fileLocs, String>("newName"));
                TableColumn noteCol = new TableColumn("Remarks");
                noteCol.setCellValueFactory(new PropertyValueFactory<fileLocs, String>("note"));
                table.setItems(data);
                table.getColumns().addAll(processingCol, oldNameCol, newNameCol, noteCol);  
            root.setCenter(table);
                Button btnGo = new Button("Go");
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
                                errorOut(record.getNewName(), e);         
                            }                                 
                            progressIndicator.setProgress(i/data.size());
                            i++;
                        }
                        data.removeAll(data);                        
                    }
                });

                Button btnClr = new Button("Abort");
                btnClr.setOnAction(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event) {
                        data.removeAll(data);
                    }
                });

                Button btnRefresh = new Button("Refresh");
                btnRefresh.setOnAction(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event) {
                        for(fileLocs paths:data) {
                            Path newPath = paths.getNewPath();
                            String fileName = newPath.getFileName().toString();
                            if (fileName.contains(naModel)) {
                                String replacement = toDir + "\\" + fileName;
                                paths.setNewName(replacement);
                            }
                        }
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

        
        public void createXMP(File file) {
         //exiftool -ext jpg -tagsfromfile -@ exif2xmp.args -@ iptc2xmp.args %d%f.xmp
         // -ext EXT
        }
        
	public void readMetaDataTest(File file) {
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
                errorOut(file.getName(), e);         
            } catch (IOException e) {
                errorOut(file.getName(), e);         
            }
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
                                errorOut(content[i].getName(), e);         
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
        
}
