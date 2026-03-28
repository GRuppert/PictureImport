package org.nyusziful.pictureorganizer.Service.ExifUtils.History;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Minimal JSON serialization/deserialization for XMP history entries.
 * Handles only the specific structures produced by this package — no general-purpose parser.
 */
class JsonUtil {

    /** Escapes and wraps a Java string as a JSON string literal. */
    static String escape(String s) {
        StringBuilder sb = new StringBuilder("\"");
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"'  -> sb.append("\\\"");
                case '\\' -> sb.append("\\\\");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case '\t' -> sb.append("\\t");
                default   -> sb.append(c);
            }
        }
        return sb.append('"').toString();
    }

    /**
     * Strips surrounding quotes and unescapes a raw JSON string value.
     * Returns null if rawValue is null. Returns rawValue unchanged if it is not quoted
     * (e.g. the literal "null" or a nested object — callers should not pass those here).
     */
    static String unquote(String rawValue) {
        if (rawValue == null) return null;
        if (!rawValue.startsWith("\"")) return rawValue;
        StringBuilder sb = new StringBuilder();
        boolean escaped = false;
        for (int i = 1; i < rawValue.length() - 1; i++) {
            char c = rawValue.charAt(i);
            if (escaped) {
                switch (c) {
                    case '"'  -> sb.append('"');
                    case '\\' -> sb.append('\\');
                    case 'n'  -> sb.append('\n');
                    case 'r'  -> sb.append('\r');
                    case 't'  -> sb.append('\t');
                    default   -> sb.append(c);
                }
                escaped = false;
            } else if (c == '\\') {
                escaped = true;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * Parses a JSON object string into a map of key → raw value.
     * Raw values are: quoted strings (including the quotes), nested objects ({...}),
     * nested arrays ([...]), or other literals (null).
     * Use {@link #unquote(String)} to decode quoted string values.
     */
    static Map<String, String> parseObject(String json) {
        Map<String, String> result = new LinkedHashMap<>();
        json = json.strip();
        // Strip outer braces
        if (json.startsWith("{")) json = json.substring(1);
        if (json.endsWith("}")) json = json.substring(0, json.length() - 1);

        int i = 0;
        while (i < json.length()) {
            // Skip commas and whitespace between pairs
            while (i < json.length() && (json.charAt(i) == ',' || Character.isWhitespace(json.charAt(i)))) i++;
            if (i >= json.length()) break;

            // Expect opening quote of key
            if (json.charAt(i) != '"') { i++; continue; }
            int keyEnd = findStringEnd(json, i + 1);
            String key = unescapeRaw(json.substring(i + 1, keyEnd));
            i = keyEnd + 1;

            // Skip to ':'
            while (i < json.length() && json.charAt(i) != ':') i++;
            i++; // consume ':'
            while (i < json.length() && Character.isWhitespace(json.charAt(i))) i++;
            if (i >= json.length()) break;

            String rawValue = readValue(json, i);
            i += rawValue.length();
            result.put(key, rawValue.strip());
        }
        return result;
    }

    /**
     * Parses a JSON array string into a list of raw values.
     * Use {@link #unquote(String)} on each item to decode quoted strings.
     */
    static List<String> parseArray(String json) {
        List<String> result = new ArrayList<>();
        json = json.strip();
        if (json.startsWith("[")) json = json.substring(1);
        if (json.endsWith("]")) json = json.substring(0, json.length() - 1);

        int i = 0;
        while (i < json.length()) {
            while (i < json.length() && (json.charAt(i) == ',' || Character.isWhitespace(json.charAt(i)))) i++;
            if (i >= json.length()) break;

            String rawValue = readValue(json, i);
            i += rawValue.length();
            String stripped = rawValue.strip();
            if (!stripped.isEmpty()) result.add(stripped);
        }
        return result;
    }

    // Reads one JSON value starting at position i, returns the raw substring (may include trailing whitespace)
    private static String readValue(String s, int i) {
        char c = s.charAt(i);
        if (c == '"') {
            int end = findStringEnd(s, i + 1);
            return s.substring(i, end + 1);
        } else if (c == '{' || c == '[') {
            int end = findMatchingClose(s, i);
            return s.substring(i, end + 1);
        } else {
            // literal (null, number, etc.)
            int end = i;
            while (end < s.length() && s.charAt(end) != ',' && s.charAt(end) != '}' && s.charAt(end) != ']') end++;
            return s.substring(i, end);
        }
    }

    // Returns the index of the closing quote for a string whose content starts at 'start'
    private static int findStringEnd(String s, int start) {
        for (int i = start; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '\\') { i++; continue; } // skip escaped char
            if (c == '"') return i;
        }
        throw new IllegalArgumentException("Unterminated JSON string at position " + start + " in: " + s);
    }

    // Returns the index of the bracket/brace that closes the one at position 'start'
    private static int findMatchingClose(String s, int start) {
        char open  = s.charAt(start);
        char close = (open == '{') ? '}' : ']';
        int depth  = 0;
        for (int i = start; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '"') { i = findStringEnd(s, i + 1); continue; }
            if (c == open)  depth++;
            else if (c == close) { if (--depth == 0) return i; }
        }
        throw new IllegalArgumentException("Unmatched bracket at position " + start + " in: " + s);
    }

    private static String unescapeRaw(String s) {
        // Simple unescape for object keys (which we control and keep ASCII)
        return s.replace("\\\"", "\"").replace("\\\\", "\\")
                .replace("\\n", "\n").replace("\\r", "\r").replace("\\t", "\t");
    }
}
