package org.nyusziful.pictureorganizer.Service.ExifUtils;

/**
 * Represents a single field-level delta in a metadata edit operation.
 *
 * Serializes to the JSON format used inside porg:changes history entries:
 *   {"f":"DateTimeOriginal","from":"2025:01:15 14:23:00","to":"2025:01:15 15:23:00"}
 *
 * Special synthetic fields (prefixed with underscore) are used for hash bookkeeping:
 *   {"f":"_contentHash","from":null,"to":"abc123"}  — IMPORT baseline
 *   {"f":"_contentHash","from":"old","to":"new"}     — VIDEO_TRIM
 */
public record MetadataChange(String field, String from, String to) {

    public String toJson() {
        return "{\"f\":" + jsonString(field)
                + ",\"from\":" + (from == null ? "null" : jsonString(from))
                + ",\"to\":" + (to == null ? "null" : jsonString(to))
                + "}";
    }

    private static String jsonString(String value) {
        return "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }
}
