package org.nyusziful.pictureorganizer.DAL.Entity;

import org.nyusziful.pictureorganizer.Service.DriveService;
import org.nyusziful.pictureorganizer.Service.FolderService;

import javax.persistence.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

@Entity
@Table(
    name = "folder",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"drive_id", "path"})}
)
public class Folder extends TrackingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    protected int id = -1;
    private String path;
    private String name;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Folder parent;
    @OneToMany(mappedBy = "parent")
    private Collection<Folder> children;
    @ManyToOne
    @JoinColumn(name = "drive_id", referencedColumnName = "id")
    private Drive drive;
    @Transient
    private Path javaPath;

    @PostLoad
    private void loadPath() {
        javaPath = Paths.get(DriveService.getLocalLetter(drive) + ":" + FolderService.dataToWinPath(path));
    }

    public Folder() {
    }

    public Folder(Drive drive, Path path) {
        javaPath = path;
        this.path = FolderService.winToDataPath(path);
        this.name = path.getFileName() == null ? "" : path.getFileName().toString();
        this.drive = drive;
    }

    public Path getJavaPath() {
        return javaPath;
    }
}
