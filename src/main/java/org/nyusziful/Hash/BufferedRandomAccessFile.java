package org.nyusziful.Hash;

/** No Copyright
 *
 * The person who associated a work with this deed has dedicated the work to the
 * public domain by waiving all of his or her rights to the work worldwide under
 * copyright law, including all related and neighboring rights, to the extent
 * allowed by law.
 *
 * https://creativecommons.org/publicdomain/zero/1.0/
 */

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;

    /**
     * Ähnlich wie {@link RandomAccessFile}, stellt Methoden zum gepufferten Lesen bereit.
     * Es wird dabei sichergestellt, dass ein Lesevorgang nach #{link BufferedRandomAccessFile#seek(long)} korrekt funktioniert.
     * Zudem wird sichergestellt, dass #{link BufferedRandomAccessFile#getFilePointer()} die korrekte Position ausgibt, die nach den
     * entsprechenden Schreibe- und Lesevorgängen auch von {@link RandomAccessFile#getFilePointer()} ausgegeben werden würde.
     *
     * @author awa
     */
public class BufferedRandomAccessFile implements Closeable {
    private boolean needsFill;
    private RandomAccessFile raf;
    private int bufferLength;
    private int bufferPos;
    private byte buffer[];
    private static final int DEFAULT_BUFFER_SIZE = 8192;
    private static int DEFAULT_EXPECTED_LINE_LENGTH = 80;

    // Testing
    public static void main(String[] args) {
        final File file = new File("C:/temp/testfile.txt");
        try {
            final StringBuilder s1 = new StringBuilder();
            final StringBuilder s2 = new StringBuilder();
            String line;

            final BufferedReader br = new BufferedReader(new FileReader(file));
            Date d5 = new Date();
            while ((br.readLine()) != null);// s1.append(line);
            Date d6 = new Date();
            System.out.println("BufferedReader took " + String.valueOf((d6.getTime()-d5.getTime())/1000.0) + "s.");
            br.close();

            final BufferedRandomAccessFile braf = new BufferedRandomAccessFile(file, "r");
///         System.out.println("Pos: " + String.valueOf(braf.getFilePointer()) + " Line: " + braf.readLineBuffered());
            Date d1 = new Date();
            while ((braf.readLineBuffered()) != null);// s2.append(line);
            Date d2 = new Date();
            System.out.println("BufferedRandomAccessFile took " + String.valueOf((d2.getTime()-d1.getTime())/1000.0) + "s.");
            braf.close();

            System.out.println(s1.toString().compareTo(s2.toString())==0);

            final RandomAccessFile raf = new RandomAccessFile(file, "r");
            Date d3 = new Date();
            while (raf.readLine() != null);
            Date d4 = new Date();
            System.out.println("RandomAccessFile took " + String.valueOf((d4.getTime()-d3.getTime())/1000.0) + "s.");
            raf.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Checks to make sure that the stream has not been closed */
    private void ensureOpen() throws IOException {
        if (raf == null)
            throw new IOException("Stream closed");
    }

    /**
     * Füllt den Puffer mit Daten ab der aktuellen Dateizeiger. Setzt den Dateizeiger wieder entsprechend.
     * @throws IOException
     */
    private void fill() throws IOException {
        if (needsFill) {
            bufferPos = 0;
            long pos = raf.getFilePointer();
            long size = raf.length();
            long len = buffer.length;
            if (pos + len >= size)
                len = size - pos;
            if (len < 0)
                len = 0;
            bufferLength = (int)len;
            raf.read(buffer, 0, (int) len);
            raf.seek(pos); // file pointer must be at the actual position
            needsFill = false;
        }
    }


    private int peek() throws IOException {
        return peek(bufferPos);
    }
    private int peek(int bPos) throws IOException {
        int c;
        if (bPos < bufferLength-1) c = buffer[bPos+1];
        else {
            long pos = raf.getFilePointer();
            raf.seek(pos+bufferLength);
            c = raf.read();
            raf.seek(pos);
        }
        return c;
    }

    /**
     * Springt an die angegebene Position in der Datei.
     *
     * @param pos Position, an die gesprungen werden soll.
     */
    public void seek(long pos) throws IOException {
        // Seek to the position.
        if (pos > raf.length()) pos = raf.length();
        raf.seek(pos);
        needsFill = true;
    }


    /**
     * Gibt genau wie {@link RandomAccessFile#readLine()} die nächste Zeile zurück. Im Unterschied dazu wird
     * zum Einlesen ein Puffer benutzt, sodass das Einlesen von langen Zeilen wesentlich beschleunigt wird.
     * Falls die Datei viele kurze Zeilen enthält, ist <code>möglicherweise</code> die ungepufferte Variante {@link BufferedRandomAccessFile#readLine()} besser.
     *
     * {@link BufferedRandomAccessFile#getFilePointer()} steht danach am Anfang der nächsten Zeile, auch wenn eine Zeile mit <code>\r\n</code> abgeschlossen ist.
     *
     * @return Die eingelesene Zeile.
     * @throws IOException Wenn ein Fehler beim Einlesen der Datei auftritt.
     */
    public synchronized final String readLineBuffered() throws IOException {
        long bytesRead;
        int startChar;
        StringBuffer s = null;
        String str;

        ensureOpen();

        // Save current position.
        long pos = raf.getFilePointer();
        bytesRead = 0;

        // Fill the buffer, if necessary.
        fill();

        // Read a line.
        bufferLoop:
        for (;;) {
            if (bufferPos >= bufferLength) {
                needsFill = true;
                raf.seek(pos+bytesRead);
                fill();
            }
            if (bufferPos >= bufferLength) { /* EOF */
                if (s != null && s.length() > 0) {
                    str = s.toString();
                }
                else {
                    str = null;
                }
                break bufferLoop;
            }
            boolean eol = false;
            int c = 0;
            int i;

            charLoop:
            for (i = bufferPos; i < bufferLength; ++i) {
                c = buffer[i];
                ++bytesRead;
                if (c == '\n' || (c == '\r' && peek(i)!='\n')) {
                    eol = true;
                    break charLoop;
                }
            }

            startChar = bufferPos;
            bufferPos = i;

            if (eol) {
                if (s == null) {
                    str = i==startChar ? "" : (new String(buffer, startChar, i - startChar - (buffer[i-1]=='\r' ? 1 : 0)));
                } else {
                    if (i!=startChar) s.append(new String(buffer, startChar, i - startChar - (buffer[i-1]=='\r' ? 1 : 0)));
                    str = s.toString();
                }
                ++bufferPos;
                break bufferLoop;
            }

            if (s == null) s = new StringBuffer(DEFAULT_EXPECTED_LINE_LENGTH);
            if (i!=startChar) s.append(new String(buffer, startChar, i - startChar - (buffer[i-1]=='\r' ? 1 : 0) ));
        }

        // Seek to the actual position.
        raf.seek(pos + bytesRead);

        return str;

    }

    public BufferedRandomAccessFile(File file, String mode) throws IOException {
        this(file, mode, DEFAULT_BUFFER_SIZE);
    }

    public BufferedRandomAccessFile(String name, String mode) throws IOException {
        this(name, mode, DEFAULT_BUFFER_SIZE);
    }

    public BufferedRandomAccessFile(String name, String mode, int bufferSize) throws IOException {
        this(new RandomAccessFile(name, mode), bufferSize);
    }

    public BufferedRandomAccessFile(File file, String mode, int bufferSize) throws IOException {
        this(new RandomAccessFile(file, mode), bufferSize);
    }


    private BufferedRandomAccessFile(RandomAccessFile raf, int bufferSize) throws IOException {
        this.raf = raf;
        this.buffer = new byte[bufferSize];
        this.needsFill = true;
    }

    public void close() throws IOException {
        synchronized (this) {
            if (raf == null)
                return;
            try {
                raf.close();
            } finally {
                raf = null;
                buffer = null;
            }
        }
    }

    public long length() throws IOException {
        return raf.length();
    }

    public long getFilePointer() throws IOException {
        return raf.getFilePointer();
    }

    public String readLine() throws IOException {
        needsFill = true;
        return raf.readLine();
    }

    public byte read() throws IOException {
        return readByte();
    }

    public byte readByte() throws IOException {
        if (bufferPos >= bufferLength) needsFill = true;
        fill();
        return buffer[bufferPos++];
    }


}
