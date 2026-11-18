package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private static final String URL = "jdbc:mysql://localhost:3306/etalase_db";
    private static final String USER = "etalase";
    private static final String PASSWORD = "etalase123";

    public static Connection bukaKoneksi() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void prepareDatabase() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS produk ("
                + "id BIGINT AUTO_INCREMENT PRIMARY KEY, "
                + "nama VARCHAR(100) NOT NULL, "
                + "harga BIGINT NOT NULL, "
                + "foto VARCHAR(100) NOT NULL)";

        try (
            Connection koneksi = bukaKoneksi();
            Statement perintah = koneksi.createStatement()) {
            perintah.executeUpdate(sql);
        }
    }
}