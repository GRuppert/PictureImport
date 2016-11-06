import java.util.ArrayList;
import java.util.Iterator;
import java.util.TimeZone;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.imaging.jpeg.JpegSegmentMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifReader;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.iptc.IptcReader;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.filechooser.FileSystemView;

public final class PicOrganizes {        
	//Path tempDirectory = Paths.get("N:\\Fényképek\\Közös\\!Átválogatott");
	Path tempDirectory = Paths.get("G:\\Pictures\\Fényképek\\Közös\\!Átválogatott");
        //Path tempDirectory = Paths.get("E:\\tempPicOrg");
        
        //File picDir = new File("N:\\Fényképek\\Közös");
        File picDir = new File("G:\\Pictures\\Fényképek\\Közös");

	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        static int MOVE = 1;
        static int COPY = 0;
        int maxWidth, maxHeight;
        
	private ArrayList<String> chooseDirectoriesVis(ArrayList<String> list) {
            JDialog chooseDirDialog = new JDialog(null, Dialog.ModalityType.APPLICATION_MODAL);
            JPanel newContentPane = new JPanel();
            newContentPane.setOpaque(true); //content panes must be opaque
            chooseDirDialog.setContentPane(newContentPane);
            
            ArrayList<JCheckBox> checkboxes = new ArrayList<JCheckBox>();
            for (String element:list) {
                checkboxes.add(new JCheckBox(element));
            }
            for (JCheckBox checkbox:checkboxes) {
                newContentPane.add(checkbox);
                checkbox.setSelected(true);
            }
            JButton button = new JButton("Import");
            button.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {chooseDirDialog.dispose();}});
            newContentPane.add(button);
            chooseDirDialog.pack();
            chooseDirDialog.setVisible(true);

            list = new ArrayList<String>();
            for (JCheckBox checkbox:checkboxes) {
                if (checkbox.isSelected()) list.add(checkbox.getText());
            }
            return list;
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
                return chooseDirectoriesVis(list);		
	}

        private Object[] listOut(ArrayList<Path[]> objects, String invText, String dateText) {
            String outList = "";
            for (Path[] obj:objects) {
              outList = outList + obj[0].toString() + " -> " + obj[1].toString() + "\n";
            }
            JTextArea textArea = new JTextArea(outList);
            JScrollPane scrollPane = new JScrollPane(textArea);
            JTextArea invalid = new JTextArea(invText);
            JTextArea date = new JTextArea(dateText);
            textArea.setEditable(false);
            scrollPane.setPreferredSize( new Dimension( maxWidth, maxHeight ) );
            Object[] out = {invalid, date, scrollPane};
            Object[] buttonText = {"Mehet", "Mégse", "Frissít"};
            int value = JOptionPane.showOptionDialog(null, out, "File names", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, buttonText, buttonText[2]);
            return new Object[] {value, invalid.getText(), date.getText()};
        }
        
        private void errorOut(String msg) {
            JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        private JDialog progressDiag(JProgressBar bar) {
            JDialog progressDialog = new JDialog(null, Dialog.ModalityType.MODELESS);
            JPanel newContentPane = new JPanel();
            newContentPane.setOpaque(true); //content panes must be opaque
            progressDialog.setContentPane(newContentPane);
            bar.setValue(0);
            bar.setStringPainted(true);
            newContentPane.add(bar);                            
            progressDialog.pack();
            progressDialog.setVisible(true);           
            return progressDialog;
        }
               
	private void renameFiles(ArrayList<String> directories, Path target, int mode) {
            Iterator<String> iter = directories.iterator();
            while(iter.hasNext()) {
                    File dir1 = new File(iter.next());
                    if(dir1.isDirectory()) {
                        ArrayList<Path[]> files = new ArrayList<>();
                        File[] content = dir1.listFiles(new FilenameFilter() {
                            public boolean accept(File dir, String name) {
                                    name = name.toLowerCase();
                                return name.endsWith(".mts") || name.endsWith(".arw") || name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".mp4") || name.endsWith(".avi") || name.endsWith(".mov") || name.endsWith(".mpg") || name.endsWith(".3gp") || name.endsWith(".nef") || name.endsWith(".png") || name.endsWith(".dng") || name.endsWith(".tif") || name.endsWith(".gpx") || name.endsWith(".nar") || name.endsWith(".pdf");
                            }});
                        if (content.length > 0) {
                            JProgressBar progressBar = new JProgressBar(0, content.length);
                            JDialog progressDialog = progressDiag(progressBar);                           

                            for(int i = 0; i < content.length; i++) {
                                if (content[i].getName().toLowerCase().endsWith(".gpx") || content[i].getName().toLowerCase().endsWith(".nar") || content[i].getName().toLowerCase().endsWith(".pdf")) {
                                    files.add(new Path[] {content[i].toPath(), Paths.get(target + "\\" + content[i].getName())});
                                    progressBar.setValue(i);
                                    continue;
                                }
                                if (content[i].getName().toLowerCase().endsWith(".mp4")) {
                                    String thmb = content[i].toString().replaceFirst("CLIP", "THMBNL").replaceFirst(".MP4", "T01.JPG");
                                    File thmbF = new File(thmb);
                                    if (thmbF.exists()) {
                                        String thmbMeta = readMetaDataMove(thmbF).toString();
                                        Path vid = Paths.get(target + thmbMeta.replaceFirst("T01.JPG", ".MP4"));
                                        files.add(new Path[] {content[i].toPath(), vid});
                                        progressBar.setValue(i);
                                        continue;
                                    }
                                }
                                files.add(new Path[] {content[i].toPath(), Paths.get(target + readMetaDataMove(content[i]).toString())});
                                progressBar.setValue(i);                                
                            }
                            progressDialog.dispose();
                        }
                        if (files.size() > 0) {       
                            String startDate = files.get(0)[1].getFileName().toString().substring(0, 8);
                            String naName = "NA";
                            Object[] ok = listOut(files, naName, startDate);
                            while (ok[0].equals(2)) {
                                if (!startDate.equals(ok[2].toString())) {
 /*                                   try {
                                        int newDateInt = Integer.parseInt(ok[2].toString());
                                        int firstFile = Integer.parseInt(files.get(0)[1].getFileName().toString().substring(0, 8));
                                        int diff = 
                                        for(Path[] paths:files) {
                                            int fileDate = Integer.parseInt(paths[1].getFileName().toString().substring(0, 8));
                                            int newfileDate = fileDate + diff;
                                            paths[1] = Paths.get(paths[1].getParent() + "\\" + Integer.toString(newfileDate)+ paths[1].getFileName().toString().substring(9));
                                        }
                                        startDate = Integer.toString(newDate);
                                    } catch (NumberFormatException e) {
                                        errorOut("Rendes számot írál be: ÉÉÉÉHHNN");
                                    }*/
                                }
                                if (!naName.equals(ok[1].toString())) {
                                    String newNaName = ok[1].toString();
                                    for(Path[] paths:files) {
                                        paths[1] = Paths.get(paths[1].getParent() + "\\" + paths[1].getFileName().toString().replaceAll("_"+naName+"-", "_"+newNaName+"-"));
                                    }
                                    naName = newNaName;
                                }
                                ok = listOut(files, ok[1].toString(), ok[2].toString());
                            }
                            if (ok[0].equals(JOptionPane.OK_OPTION)) {
                                JProgressBar progressBar = new JProgressBar(0, files.size());
                                JDialog progressDialog = progressDiag(progressBar);
                                for (int i = 0; i < files.size(); i++) {
                                    try {                                    
                                        if (Files.notExists(target)) {
                                            Files.createDirectory(target);
                                        }
                                        if (mode == COPY) {
                                            Files.copy(files.get(i)[0], files.get(i)[1]);                               
                                        } else if (mode == MOVE) {
                                            Files.move(files.get(i)[0], files.get(i)[1]);                               
                                        }
                                    } catch (IOException e) {
                                        errorOut(e.getMessage());
                                    }                                 
                                    progressBar.setValue(i); 
                                }
                                progressDialog.dispose();
                            }
                        }
                    }				
            }
	}
        
	private void moveFiles(ArrayList<String> directories, int mode) {
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
                                File[] dirs = picDir.listFiles(new FilenameFilter() {public boolean accept(File dir, String name) {return dir.isDirectory();}});
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
                        Object[] ok = listOut(files, "", "");
                        if (ok[0].equals(JOptionPane.OK_OPTION)) {
                            JProgressBar progressBar = new JProgressBar(0, files.size());
                            JDialog progressDialog = progressDiag(progressBar);
                            for (int i = 0; i < files.size(); i++) {
                                try {  
                                    if (Files.exists(files.get(i)[1])) {
                                        if (mode == MOVE) {
                                            Files.delete(files.get(i)[0]);                               
                                        }                                        
                                    } else {
                                        if (mode == COPY) {
                                            Files.copy(files.get(i)[0], files.get(i)[1]);                               
                                        } else if (mode == MOVE) {
                                            Files.move(files.get(i)[0], files.get(i)[1]);                               
                                        }
                                    }
                               } catch (IOException e) {
                                    errorOut(e.getMessage());
                                }                                 
                                progressBar.setValue(i); 
                            }
                            progressDialog.dispose();
                        }
                    }
                }				
            }
            
        }

	public Path readMetaDataMove(File file) {
        String modelF = "NA";
        String dateF = sdf.format(file.lastModified());
        if (file.getName().toLowerCase().endsWith(".3gp")) {
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
            ExifSubIFDDirectory directory2 = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
            //getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL)
            if (directory2 != null) {
                Date dateOrig = directory2.getDateOriginal(TimeZone.getTimeZone("Europe/Budapest"));
                if (dateOrig != null)
                    dateF = sdf.format(dateOrig);
            }

     } catch (ImageProcessingException e) {
//              errorOut(e.toString());         
        } catch (IOException e) {
              errorOut(file.getName() +e.toString());
        }
        dateF = dateF.replace(":", "");
        dateF = dateF.replace(" ", "_");
        modelF = modelF.replaceAll("[<>:\"\\/|?*]", "!");
        return Paths.get("\\" + dateF + "_" + modelF + "-" + file.getName());
		
	}

	public PicOrganizes() {
            Boolean run = true;
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            maxWidth = (int) (screenSize.getWidth() - 150);
            maxHeight = (int) (screenSize.getHeight() - 150);
            do {
                Object[] options = {"Import", "Átnevezés", "Átpakolás", "db szám", "Dátumok", "Kilép"};
                int n = JOptionPane.showOptionDialog(null, "Mi legyen?", "Main", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
                switch (n) {
                    case 0: 
                        ArrayList<String> directories = chooseDirectories(); 
                        renameFiles(directories, tempDirectory, MOVE); 
                        break;
                    case 1: 
                        JFileChooser chooser = new JFileChooser();
                        chooser.setCurrentDirectory(picDir);
                        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                        chooser.setAcceptAllFileFilterUsed(false);
                        int returnVal = chooser.showOpenDialog(null);
                        if(returnVal == JFileChooser.APPROVE_OPTION) {
                            ArrayList<String> dirList = new ArrayList();
                            dirList.add(chooser.getSelectedFile().getPath());
                            Path tempDir = Paths.get(tempDirectory.toString() + "\\" + chooser.getSelectedFile().getName());
                            renameFiles(dirList, tempDir, MOVE); 
                        }                    
                        break;
                    case 2:
                        chooser = new JFileChooser();
                        chooser.setCurrentDirectory(tempDirectory.toFile());
                        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                        chooser.setAcceptAllFileFilterUsed(false);
                        returnVal = chooser.showOpenDialog(null);
                        if(returnVal == JFileChooser.APPROVE_OPTION) {
                            ArrayList<String> dirList = new ArrayList();
                            dirList.add(chooser.getSelectedFile().getPath());
                            moveFiles(dirList, MOVE); 
                        }                    
                        break;
                    case 3:
                        File[] dirs = tempDirectory.toFile().listFiles(new FilenameFilter() {public boolean accept(File dir, String name) {return dir.isDirectory();}});
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
                       break;
                    case 4:
                        GregorianCalendar cal = new GregorianCalendar();
                        File[] dirc = picDir.listFiles(new FilenameFilter() {public boolean accept(File dir, String name) {return dir.isDirectory();}});
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
                        break;
                    case 5:
                        run = false;
                        break;
                }
            } while (run);    
	}       
		
	public static void main(String[] args) {
		PicOrganizes O = new PicOrganizes();
	}

}
