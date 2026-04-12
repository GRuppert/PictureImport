package org.nyusziful.Main;

import org.nyusziful.pictureorganizer.DAL.Entity.MediaFileVersion;
import org.nyusziful.pictureorganizer.DAL.JPAConnection;
import org.nyusziful.pictureorganizer.DTO.ImageDTO;
import org.nyusziful.pictureorganizer.Service.Hash.JPGHash;
import org.nyusziful.pictureorganizer.Service.Hash.MediaFileHash;

import jakarta.persistence.EntityManager;
import java.io.File;

/**
 * Orchestrates the manual import of a new picture file:
 * <ol>
 *   <li>Compute the canonical (media-data-only) hash.</li>
 *   <li>Write an import history record to the database.</li>
 *   <li>Create an EXIF backup (.bak) file beside the original.</li>
 *   <li>Embed the canonical hash inside the JPEG COMMENT segment.</li>
 * </ol>
 */
public class ImportService {

    /**
     * Returns the canonical (media-data-only) hash of the file.
     * Returns {@link MediaFileHash#EMPTYHASH} if the hash cannot be computed.
     */
    public String computeCanonicalHash(File file) {
        ImageDTO dto = MediaFileHash.getHash(file);
        return (dto != null && dto.hash != null) ? dto.hash : MediaFileHash.EMPTYHASH;
    }

    /**
     * Persists a {@link MediaFileVersion} record linking the canonical hash to the file.
     * The record is committed immediately via {@link JPAConnection}.
     */
    public void writeHistory(File file, String canonicalHash) {
        MediaFileVersion record = new MediaFileVersion();
        record.setFilehash(canonicalHash);
        EntityManager em = JPAConnection.getInstance().getEntityManager();
        em.persist(record);
        em.getTransaction().commit();
        em.getTransaction().begin();
    }

    /**
     * Creates an EXIF-backup (.bak) file next to the given JPEG.
     *
     * @return true if the backup already existed or was created successfully
     */
    public boolean createExifBackup(File file) {
        return JPGHash.addBackupExif(file, true);
    }

    /**
     * Embeds {@code canonicalHash} into the JPEG COMMENT segment.
     * Only the COMMENT segment is modified; all image data is preserved verbatim.
     *
     * @return true on success
     */
    public boolean writeCanonicalHashToFile(File file, String canonicalHash) {
        return JPGHash.writeCanonicalHash(file, canonicalHash);
    }

    /**
     * Reads back the canonical hash that was previously embedded via
     * {@link #writeCanonicalHashToFile}.
     *
     * @return the 32-char hex hash, or {@code null} if not present
     */
    public String readCanonicalHashFromFile(File file) {
        return JPGHash.readCanonicalHash(file);
    }
}
