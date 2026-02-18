package it.soaringtech;

import com.gluonhq.attach.storage.StorageService;
import com.gluonhq.attach.util.Services;
import com.gluonhq.attach.util.Platform;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            String dbUrl;

            if (Platform.isDesktop()) {
                dbUrl = "jdbc:sqlite:ssul.db";
            } else {
                // 🌟 모바일 환경: 안전한 내부 저장소 경로를 가져와서 SQLite 파일 생성
                File storage = Services.get(StorageService.class)
                        .flatMap(s -> s.getPrivateStorage())
                        .orElseThrow(() -> new RuntimeException("저장소를 찾을 수 없습니다."));

                File dbFile = new File(storage, "ssul.db");
                dbUrl = "jdbc:sqlite:" + dbFile.getAbsolutePath();
            }

            conn = DriverManager.getConnection(dbUrl);
            createTableIfNotExists(conn);

        } catch (Throwable e) {
            e.printStackTrace();
            System.out.println("DB 연결 실패: " + e.getMessage());
        }
        return conn;
    }

    private static void createTableIfNotExists(Connection conn) {
        String sql = "CREATE TABLE IF NOT EXISTS ssul (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT NOT NULL, " +
                "content TEXT, " +
                "category TEXT, " +
                "reg_date TEXT)";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}