package org.nyusziful.pictureorganizer.Service.ExifUtils.History;

import java.util.Collections;
import java.util.List;

/**
 * A batch of metadata field changes applied atomically, anchored to the file's state
 * immediately before the change via {@code previousCanonicalHash}.
 *
 * <p>{@code previousCanonicalHash} is the value of {@code porg:CanonicalHash} read from
 * the file before any writes in this operation. It is {@code null} on first import (the
 * canonical hash protocol has not yet been applied). When null, the field is omitted from
 * the serialised JSON entirely — it is never written as {@code "null"}.
 *
 * <p>At least one {@link FieldDelta} is required.
 */
public final class MetadataChange {

    private final String previousCanonicalHash;
    private final List<FieldDelta> deltas;

    public MetadataChange(String previousCanonicalHash, List<FieldDelta> deltas) {
        if (deltas == null || deltas.isEmpty())
            throw new IllegalArgumentException("MetadataChange requires at least one FieldDelta");
        this.previousCanonicalHash = previousCanonicalHash;
        this.deltas = Collections.unmodifiableList(deltas);
    }

    /** Convenience factory for the single-delta case. */
    public static MetadataChange of(String previousCanonicalHash, FieldDelta delta) {
        return new MetadataChange(previousCanonicalHash, List.of(delta));
    }

    /**
     * The canonical hash of the file before this change batch, or {@code null} on first import.
     */
    public String getPreviousCanonicalHash() {
        return previousCanonicalHash;
    }

    /** The individual field changes, in the order they were recorded. */
    public List<FieldDelta> getDeltas() {
        return deltas;
    }
}
