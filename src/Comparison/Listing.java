package Comparison;


import Rename.StaticTools;
import Main.PicOrganizes;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.chart.XYChart;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author gabor
 */
public class Listing extends Task implements FileVisitor<Path>
{
    private long fileSizeCountTotal = 0;
    private long fileSizeCount = 0;
    private long fileCountTotal = 0;
    private long fileCount = 0;
    private final Path path;
    private final int start;
    private final File output;
    private String delimiter = "\t";
    private PrintWriter pw;
    private long startTime;
    private StringBuilder sb;
    
    public Listing(Path path, int start, File output) {
        this.path = path;
        this.start = start;
        this.output = output;
    }

    @Override
    protected Object call() throws Exception {
        Platform.runLater(() -> {
            PicOrganizes.view.speeds.getData().clear();
            PicOrganizes.view.speeds.getData().add(new XYChart.Data(0, 0));
        });
        fileSizeCountTotal = 0;
        fileSizeCount = 0;
        fileCountTotal = 0;
        fileCount = 0;
        updateMessage("Initializing");
        try
        {
            Files.walkFileTree (path, new SimpleFileVisitor<Path>() {
                  @Override public FileVisitResult 
                visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        if (!attrs.isDirectory() && attrs.isRegularFile() && PicOrganizes.supportedFileType(file.getFileName().toString())) {
                            fileSizeCountTotal += attrs.size();
                            fileCountTotal++;
                        }
                        return FileVisitResult.CONTINUE;                            
                    }

                  @Override public FileVisitResult 
                visitFileFailed(Path file, IOException exc) {
                        StaticTools.errorOut(file.toString(), exc);
                        // Skip folders that can't be traversed
                        return FileVisitResult.CONTINUE;
                    }

                  @Override public FileVisitResult
                postVisitDirectory (Path dir, IOException exc) {
                        if (exc != null)
                        StaticTools.errorOut(dir.toString(), exc);
                        // Ignore errors traversing a folder
                        return FileVisitResult.CONTINUE;
                    }
            });
            updateMessage("Read: " + fileSizeCount/1048576 + "MB from: " + fileSizeCountTotal/1048576 + "MB");
            updateProgress(fileSizeCount, fileSizeCountTotal);

            sb = new StringBuilder();
            startTime = System.nanoTime();
            fileCount = 0;
            Files.walkFileTree (path, this);
            updateMessage("Writing results to: " + output);
            pw = new PrintWriter(output);
            pw.write(sb.toString());
            pw.close();
        }
        catch (IOException e)
        {
            throw new AssertionError ("walkFileTree will not throw IOException if the FileVisitor does not");
        }
        updateMessage("Ready");
        return null;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        if (isCancelled()) {
            return FileVisitResult.TERMINATE;
        }
        if (!attrs.isDirectory() && attrs.isRegularFile() && PicOrganizes.supportedFileType(file.getFileName().toString())) {
            String hash = StaticTools.getHash(file.toFile());
//            String hash = "Test";
            sb.append(start + fileCount).append(delimiter);
            sb.append(hash).append(delimiter);
            sb.append(hash).append(delimiter);
            sb.append(delimiter);
            sb.append(file.getParent().toString().substring(2)).append(delimiter);
            sb.append(file.getFileName()).append(delimiter);
            sb.append(attrs.size());
            sb.append('\n');
            fileSizeCount += attrs.size();
            fileCount++;
            updateMessage("Read: " + fileSizeCount/1048576 + "MB from: " + fileSizeCountTotal/1048576 + "MB");
            updateProgress(fileSizeCount, fileSizeCountTotal);
            long durationNano = System.nanoTime()-startTime;
            int percent = (int) (fileSizeCount*100/fileSizeCountTotal);
            int writeSpeed = (int) (fileSizeCount/1048576*1000000000/durationNano);
            Platform.runLater(() -> {PicOrganizes.view.speeds.getData().add(new XYChart.Data(percent, writeSpeed));});
        }
        return FileVisitResult.CONTINUE;                            
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
  
}
