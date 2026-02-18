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
            String dbUrl = "";
            String dbFileName = "ssul.db";

            // 🌟 PC 환경과 모바일 환경을 완벽하게 분리!
            if (Platform.isDesktop()) {
                // PC일 때는 기존 드라이버 사용
                Class.forName("org.sqlite.JDBC");
                dbUrl = "jdbc:sqlite:" + dbFileName;
            } else {
                // 안드로이드일 때는 내장 DB용 SQLDroid 드라이버 사용
                Class.forName("org.sqldroid.SQLDroidDriver");
                File storage = Services.get(StorageService.class)
                        .flatMap(s -> s.getPrivateStorage())
                        .orElseThrow(() -> new RuntimeException("저장소를 찾을 수 없습니다."));
                // sqldroid 전용 url 완성
                dbUrl = "jdbc:sqldroid:" + new File(storage, dbFileName).getAbsolutePath();
            }

            conn = DriverManager.getConnection(dbUrl);
            createTableIfNotExists(conn);

        } catch (Throwable e) { // Exception 대신 최상위 방어막인 Throwable 적용
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