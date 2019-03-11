package org.nyusziful.pictureorganizer.DB;

import java.sql.*;

public class DBConnection {
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

    public static void saveFile(String filename, String path, int driveId, String hash, long size, Date dateMod, Boolean exifBackup) {
        try {
            Connection conn = getConnection();
            Statement s = conn.createStatement();
            String selTable = "INSERT INTO file (filename, path, drive_id, image_id, size, date_mod, exifbackup)" +
                    " VALUES ('" + filename + "', '" + path + "', '" + driveId + "', '" + hash + "', '" + size + "', '" + dateMod + "', '" + exifBackup + "')";
            s.execute(selTable);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void saveFile(String filename, String path, int driveId, String hash, long size, Timestamp dateMod) {
        try {
            Connection conn = getConnection();
            Statement s = conn.createStatement();
            String selPrevious = "SELECT id FROM file WHERE drive_id = " + driveId + " AND filename = '" + filename + "' AND path = '" + path + "'";
            s.execute(selPrevious);
            ResultSet rs = s.getResultSet();
            if ((rs != null) && (rs.next())) {
                int id = rs.getInt(1);
                String selTable = "UPDATE file SET image_id = '" + hash + "', size = '" + size + "', date_mod = '" + dateMod + "' WHERE id = " + id;
                s.execute(selTable);
            } else {
                String selTable = "INSERT INTO file (filename, path, drive_id, image_id, size, date_mod)" +
                        " VALUES ('" + filename + "', '" + path + "', '" + driveId + "', '" + hash + "', '" + size + "', '" + dateMod + "')";
                s.execute(selTable);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static int checkFile(String filename, String path, int driveId, long size, Timestamp dateMod) {
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
            return -1;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return -2;
    }

    public static void saveImage(String hash, String origFilenam, Date dateTaken) {

    }

    public static Connection getConnection() {
        if (connection==null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection=DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/picture","picture","picture");
                System.out.println("MySQL Connection Created");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
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

            // close and cleanup
            s.close();
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
