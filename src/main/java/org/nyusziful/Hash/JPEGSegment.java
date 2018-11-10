package org.nyusziful.Hash;

public class JPEGSegment implements MediaFileSegment {
    private long startAddress;
    private long length;
    private int marker;
    private MediaFileStruct data;
    private long read = 0;
    private String id = "";

    public JPEGSegment(long startAddress, long length, int marker, String id) {
        this.startAddress = startAddress;
        this.length = length;
        this.marker = marker;
        this.id = id;
    }

    public JPEGSegment(long startAddress, long length, int marker) {
        this(startAddress, length, marker, "");
    }

    @Override
    public long getStartAddress() {
        return startAddress;
    }

    @Override
    public long getLength() {
        return length;
    }

    public long getBytesLeft() {
        return length - read;
    }

    public String getMarker() {
        return getMarker(marker);
    }

    public static String getMarker(int marker) {
        String markerText = String.format("0xFF%02X ", marker);
        switch (marker) {
            case 216:
                markerText += "Start Of Image";
                break;
            case 192:
                markerText += "Start Of Frame baseline";
                break;
            case 194:
                markerText += "Start Of Frame progressive ";
                break;
            case 196:
                markerText += "Define Huffman Table";
                break;
            case 219:
                markerText += "Define Quantization Table";
                break;
            case 221:
                markerText += "Define Restart Interval";
                break;
            case 218:
                markerText += "Start Of Scan";
                break;
            case 254:
                markerText += "Comment";
                break;
            case 217:
                markerText += "End Of Image";
                break;
            case 208:
            case 209:
            case 210:
            case 211:
            case 212:
            case 213:
            case 214:
            case 215:
                markerText += "Restart";
                break;
            case 224:
            case 225:
            case 226:
            case 227:
            case 228:
            case 229:
            case 230:
            case 231:
            case 232:
            case 233:
            case 234:
            case 235:
            case 236:
            case 237:
            case 238:
            case 239:
                markerText += "Application-specific";
                break;
            default:
                markerText += "Unknown marker " + marker;
        }
        return markerText;
    }

    public void resetRead() {
        this.read = 0;
    }

    public void addRead(long read) {
        this.read += read;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && this.getClass().equals(obj.getClass())) {
            JPEGSegment otherSegment = (JPEGSegment) obj;
            if (this.getMarker().equals(otherSegment.getMarker()))
                if (this.getLength() == otherSegment.getLength())
                    if (this.getStartAddress() == otherSegment.getStartAddress())
                        if (this.getId().equals(otherSegment.getId()))
                            return true;
        }
        return false;

    }

    @Override
    public String toString() {
        String header = String.format("%,8d / 0x%8X  " + this.getMarker() + "%n", this.getStartAddress(), this.getStartAddress());
        if (length > 0) {
            return header;
        } else {
            String payload = String.format(" payload: " + this.getId() + "%,8d length %n", this.getLength());
            return header + payload;
        }
    }

    public MediaFileStruct getData() {
        return data;
    }

    public void setData(MediaFileStruct data) {
        this.data = data;
    }
}
