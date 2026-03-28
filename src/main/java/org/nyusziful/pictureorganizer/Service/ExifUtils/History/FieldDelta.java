package org.nyusziful.pictureorganizer.Service.ExifUtils.History;

import java.util.Map;

/**
 * A single field-level change: the field's name, its value before the change (null if the
 * field did not exist), and its value after the change (null if the field was deleted).
 * At least one of {@code from} or {@code to} must be non-null.
 *
 * <p>Serializes to compact JSON stored inside the {@code porg:History} XMP bag:
 * {@code {"f":"DateTimeOriginal","from":"2025:01:15 14:23:00","to":"2025:01:15 15:23:00"}}
 * Absent fields ({@code from} when adding, {@code to} when deleting) are omitted entirely
 * to minimise byte usage inside the 64 KB JPEG APP1 limit.
 */
public record FieldDelta(String fieldName, String from, String to) {

    public FieldDelta {
        if (fieldName == null || fieldName.isBlank())
            throw new IllegalArgumentException("fieldName must not be blank");
        if (from == null && to == null)
            throw new IllegalArgumentException("at least one of from/to must be non-null");
    }

    /** Serializes this delta to its compact JSON representation. */
    public String toJson() {
        StringBuilder sb = new StringBuilder("{\"f\":");
        sb.append(JsonUtil.escape(fieldName));
        if (from != null) sb.append(",\"from\":").append(JsonUtil.escape(from));
        if (to   != null) sb.append(",\"to\":").append(JsonUtil.escape(to));
        return sb.append('}').toString();
    }

    /** Parses a {@code FieldDelta} from its compact JSON representation. */
    public static FieldDelta fromJson(String json) {
        Map<String, String> m = JsonUtil.parseObject(json);
        String f    = JsonUtil.unquote(m.get("f"));
        String from = m.containsKey("from") ? JsonUtil.unquote(m.get("from")) : null;
        String to   = m.containsKey("to")   ? JsonUtil.unquote(m.get("to"))   : null;
        return new FieldDelta(f, from, to);
    }
}
