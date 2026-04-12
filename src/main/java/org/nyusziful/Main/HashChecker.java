package org.nyusziful.Main;

/**
 * Single-method interface for checking whether a media file's canonical hash
 * is already recorded in the persistence store.
 *
 * <p>This abstraction lets {@link FolderWatchService} work against both the
 * real Hibernate/MySQL DAL (production) and a lightweight test double.
 */
public interface HashChecker {
    /**
     * @param canonicalHash 32-character MD5 hex hash of the media-only content
     * @return {@code true} if a record for this hash already exists in the store
     */
    boolean isInDatabase(String canonicalHash);
}
