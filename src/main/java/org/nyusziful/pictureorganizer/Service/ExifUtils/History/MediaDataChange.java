package org.nyusziful.pictureorganizer.Service.ExifUtils.History;

/**
 * A pixel/media-data-level change — the content hash changed because the media content itself
 * was intentionally modified (e.g. a video trim or a pixel edit).
 *
 * <p>This is distinct from a metadata-only change where the content hash stays the same.
 * Use the constants {@link #REASON_VIDEO_TRIM} and {@link #REASON_PIXEL_EDIT} to identify
 * the reason. All three fields are required and non-null.
 */
public final class MediaDataChange {

    /** Intentional trim of video content (trimStart/trimEnd recorded in the history entry). */
    public static final String REASON_VIDEO_TRIM = "VIDEO_TRIM";

    /** Intentional pixel-level edit (requires explicit user confirmation). */
    public static final String REASON_PIXEL_EDIT = "PIXEL_EDIT";

    private final String contentHashBefore;
    private final String contentHashAfter;
    private final String reason;

    public MediaDataChange(String contentHashBefore, String contentHashAfter, String reason) {
        if (contentHashBefore == null) throw new IllegalArgumentException("contentHashBefore must not be null");
        if (contentHashAfter  == null) throw new IllegalArgumentException("contentHashAfter must not be null");
        if (reason            == null) throw new IllegalArgumentException("reason must not be null");
        this.contentHashBefore = contentHashBefore;
        this.contentHashAfter  = contentHashAfter;
        this.reason            = reason;
    }

    /** Content hash of the media data before the change. */
    public String getContentHashBefore() { return contentHashBefore; }

    /** Content hash of the media data after the change. */
    public String getContentHashAfter()  { return contentHashAfter; }

    /**
     * The reason for the change. Use {@link #REASON_VIDEO_TRIM} or {@link #REASON_PIXEL_EDIT}.
     */
    public String getReason() { return reason; }
}
