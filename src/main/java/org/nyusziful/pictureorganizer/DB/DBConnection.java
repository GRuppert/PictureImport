package org.nyusziful.pictureorganizer.DB;

import org.nyusziful.pictureorganizer.Model.MediafileDTO;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class DBConnection {
    private static Set<MediafileDTO> fileSet = null;

    private static Connection connection;

    private DBConnection() {
    }

    public static Connection getSQLiteConnection() {
        if (connection==null) {
            try {
                Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
                connection=DriverManager.getConnection(
                        "jdbc:ucanaccess://e:/Pictures.accdb");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return connection;
    }

    public static Connection getConnection() {
        if (connection==null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection=DriverManager.getConnection(
                        "jdbc:mysql://192.168.0.54:3306/picture","picture","picture");
                System.out.println("MySQL Connection Created");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    public static Connection getLocalConnection() {
        //TODO need to implement
        return connection;
    }

    public static void main(String[] args) {
        try {
            Connection conn = getConnection();
            Statement s = conn.createStatement();

            // create a table
/*            String tableName = "myTable" + String.valueOf((int) (Math.random() * 1000.0));
            String createTable = "CREATE TABLE " + tableName +
                    " (id Integer, name Text(32))";
            s.execute(createTable);

            // enter value into table
            for (int i = 0; i < 25; i++) {
                String addRow = "INSERT INTO " + tableName + " VALUES ( " +
                        String.valueOf((int) (Math.random() * 32767)) + ", 'Text Value " +
                        String.valueOf(Math.random()) + "')";
                s.execute(addRow);
            }*/

            // Fetch table
            String selTable = "SELECT * FROM drives";
            s.execute(selTable);
            ResultSet rs = s.getResultSet();
            while ((rs != null) && (rs.next())) {
                System.out.println(rs.getString(1) + " : " + rs.getString(2));
            }

            // drop the table
//            String dropTable = "DROP TABLE " + tableName;
//            s.execute(dropTable);

            // flush and cleanup
            s.close();
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void saveFile(Set<MediafileDTO> files) {
        try {
            Connection conn = getConnection();
            String sql = "INSERT INTO file (filename, path, drive_id, image_id, size, date_mod, exifbackup, filehash) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            final int batchSize = 1000;
            int count = 0;

            for (MediafileDTO actFile: files) {

                ps.setString(1, actFile.getFilename());
                ps.setString(2, actFile.getPath());
                ps.setInt(3, actFile.getDriveId());
                ps.setString(4, actFile.getHash());
                ps.setLong(5, actFile.getSize());
                ps.setTimestamp(6, actFile.getDateMod());
                ps.setBoolean(7, false);
                ps.setString(8, actFile.getFullhash());
                ps.addBatch();

                if(++count % batchSize == 0) {
                    ps.executeBatch();
                }
            }
            ps.executeBatch(); // insert remaining records
            ps.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    public static void saveFile(String filename, String path, int driveId, String fullhash, String hash, long size, Timestamp dateMod) {
        try {
            Connection conn = getConnection();
            Statement s = conn.createStatement();
            String selPrevious = "SELECT id FROM file WHERE drive_id = " + driveId + " AND filename = '" + filename + "' AND path = '" + path + "'";
            s.execute(selPrevious);
            ResultSet rs = s.getResultSet();
            if ((rs != null) && (rs.next())) {
                int id = rs.getInt(1);
                String selTable = "UPDATE file SET image_id = '" + hash + "',  filehash = '" + fullhash + "', size = '" + size + "', date_mod = '" + dateMod + "' WHERE id = " + id;
                s.execute(selTable);
            } else {
                String selTable = "INSERT INTO file (filename,  filehash, path, drive_id, image_id, size, date_mod)" +
                        " VALUES ('" + filename + "', '" + fullhash + "', '" + path + "', '" + driveId + "', '" + hash + "', '" + size + "', '" + dateMod + "')";
                s.execute(selTable);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static boolean checkFile(MediafileDTO actFile) {
        if (fileSet == null) {
            fileSet = new HashSet<>();
            try {
                Connection conn = getConnection();
                Statement s = conn.createStatement();
                String selPrevious = "SELECT filename, path, size, date_mod FROM file WHERE drive_id = " + actFile.getDriveId() + ";";
                s.execute(selPrevious);
                ResultSet rs = s.getResultSet();
                while (rs.next()) {
                    fileSet.add(new MediafileDTO(rs.getString(1), rs.getString(2), actFile.getDriveId(), rs.getLong(3), rs.getTimestamp(4)));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }
        return fileSet.contains(actFile);
    }

    public static int getFileID(String filename, String path, int driveId, long size, Timestamp dateMod) {
        try {
            Connection conn = getConnection();
            Statement s = conn.createStatement();
            dateMod.setNanos(0);
            String selPrevious = "SELECT id FROM file WHERE drive_id = " + driveId + " AND filename = '" + filename + "' AND path = '" + path  + "' AND size = '" + size  + "' AND date_mod = '" + dateMod + "'";
            s.execute(selPrevious);
            ResultSet rs = s.getResultSet();
            if ((rs != null) && (rs.next())) {
                return rs.getInt(1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return -2;
        }
        return -1;
    }

    public static void saveImage(String hash, String origFilenam, Date dateTaken) {

    }

    public static void uploadLocalChanges() {
        //TODO need to implement as a background task
    }
}
