package org.nyusziful.pictureorganizer.DAL.Entity;

import javax.persistence.*;
import java.nio.file.Path;
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
    private int id;
    private String path;
    private String name;
    @ManyToOne(cascade=CascadeType.PERSIST)
    private Folder parent;
    @OneToMany(mappedBy = "parent")
    private Collection<Folder> children;
    @ManyToOne
    @JoinColumn(name="drive_id", referencedColumnName="id")
    private Drive drive;

    public Folder() {
    }

    public Folder(Drive drive, Path path) {
        this.path = path.toString().substring(2).replaceAll("\\\\", "/");
        this.name = path.getFileName().toString();
        this.drive = drive;
    }

    public int getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public Folder getParent() {
        return parent;
    }

    public Collection<Folder> getChildren() {
        return children;
    }

    public Drive getDrive() {
        return drive;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setParent(Folder parent) {
        this.parent = parent;
    }

    public void setChildren(Collection<Folder> children) {
        this.children = children;
    }

    public void setDrive(Drive drive) {
        this.drive = drive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
