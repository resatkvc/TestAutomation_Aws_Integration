package proje.com.util;

import proje.com.model.User;
import java.sql.*;

public class DatabaseUtil {
    // MySQL bağlantı bilgileri
    private static final String URL = "jdbc:mysql://localhost:3306/testdb";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    // Kullanıcıyı veritabanına ekler
    public static void insertUser(User user) {
        String sql = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.name);
            pstmt.setString(2, user.email);
            pstmt.setString(3, user.password);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Kart bilgisini veritabanına ekler
    public static void insertCard(User user) {
        String sql = "INSERT INTO cards (user_email, card_number, cvc, exp_month, exp_year) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.email);
            pstmt.setString(2, user.cardNumber);
            pstmt.setString(3, user.cvc);
            pstmt.setString(4, user.expMonth);
            pstmt.setString(5, user.expYear);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Tabloları otomatik oluşturur
    public static void createTablesIfNotExist() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String usersTable = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(100)," +
                    "email VARCHAR(100) UNIQUE," +
                    "password VARCHAR(100))";
            String cardsTable = "CREATE TABLE IF NOT EXISTS cards (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "user_email VARCHAR(100)," +
                    "card_number VARCHAR(20)," +
                    "cvc VARCHAR(10)," +
                    "exp_month VARCHAR(10)," +
                    "exp_year VARCHAR(10)," +
                    "FOREIGN KEY (user_email) REFERENCES users(email))";
            conn.createStatement().execute(usersTable);
            conn.createStatement().execute(cardsTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
} 