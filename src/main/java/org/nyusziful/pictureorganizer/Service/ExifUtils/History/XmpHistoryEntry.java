package org.nyusziful.pictureorganizer.Service.ExifUtils.History;

import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * One history event appended to a file's {@code porg:History} XMP bag.
 *
 * <p>There is no action-type enum field: the kind of change is self-evident from the
 * presence of {@link MetadataChange} and/or {@link MediaDataChange}. At least one of
 * the two must be present.
 *
 * <ul>
 *   <li>Metadata-only change (GPS add, time fix, caption…): only {@code metadataChange} present.</li>
 *   <li>Media data change (VIDEO_TRIM, PIXEL_EDIT): only {@code mediaDataChange} present.</li>
 *   <li>Combined (rare): both present.</li>
 * </ul>
 *
 * <p>Use the static factories {@link #forMetadataChange} and {@link #forMediaDataChange}
 * for the common single-type cases.
 */
public final class XmpHistoryEntry {

    private final ZonedDateTime when;
    private final String softwareAgent;
    private final MetadataChange metadataChange;
    private final MediaDataChange mediaDataChange;

    /**
     * Primary constructor. At least one of {@code metadataChange} or {@code mediaDataChange}
     * must be non-null.
     */
    public XmpHistoryEntry(ZonedDateTime when, String softwareAgent,
                           MetadataChange metadataChange, MediaDataChange mediaDataChange) {
        if (when          == null) throw new IllegalArgumentException("when must not be null");
        if (softwareAgent == null) throw new IllegalArgumentException("softwareAgent must not be null");
        if (metadataChange == null && mediaDataChange == null)
            throw new IllegalArgumentException(
                    "XmpHistoryEntry must contain at least one of metadataChange or mediaDataChange");
        this.when            = when;
        this.softwareAgent   = softwareAgent;
        this.metadataChange  = metadataChange;
        this.mediaDataChange = mediaDataChange;
    }

    /** Factory for a pure metadata change (the common case). */
    public static XmpHistoryEntry forMetadataChange(ZonedDateTime when, String softwareAgent,
                                                     MetadataChange metadataChange) {
        return new XmpHistoryEntry(when, softwareAgent, metadataChange, null);
    }

    /** Factory for a pure media data change (VIDEO_TRIM / PIXEL_EDIT). */
    public static XmpHistoryEntry forMediaDataChange(ZonedDateTime when, String softwareAgent,
                                                      MediaDataChange mediaDataChange) {
        return new XmpHistoryEntry(when, softwareAgent, null, mediaDataChange);
    }

    /** The timestamp of this history event. */
    public ZonedDateTime getWhen() { return when; }

    /** The software agent that made the change, e.g. {@code "PictureOrganizer/2.0"}. */
    public String getSoftwareAgent() { return softwareAgent; }

    /** The metadata field changes, if any. */
    public Optional<MetadataChange> getMetadataChange() { return Optional.ofNullable(metadataChange); }

    /** The media data change (content hash change), if any. */
    public Optional<MediaDataChange> getMediaDataChange() { return Optional.ofNullable(mediaDataChange); }
}
