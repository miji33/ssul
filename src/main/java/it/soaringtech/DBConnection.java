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

    // ★ static 변수(conn) 삭제! 복잡한 상태 관리 제거!

    public static Connection getConnection() {
        Connection conn = null;
        try {
            // ★ 새로 추가: DB 드라이버 강제 호출 안전장치
            Class.forName("org.sqlite.JDBC");

            // 1. 경로 설정
            String dbUrl = "jdbc:sqlite:";
            String dbFileName = "ssul.db";

            if (Platform.isDesktop()) {
                dbUrl += dbFileName;
            } else {
                File storage = Services.get(StorageService.class)
                        .flatMap(s -> s.getPrivateStorage())
                        .orElseThrow(() -> new RuntimeException("저장소를 찾을 수 없습니다."));
                dbUrl += new File(storage, dbFileName).getAbsolutePath();
            }

            // 2. ★ 무조건 새로운 연결 생성 (가장 안전함)
            conn = DriverManager.getConnection(dbUrl);

            // 3. 테이블 확인 (연결할 때마다 체크해도 SQLite는 매우 빠름)
            createTableIfNotExists(conn);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("DB 연결 실패: " + e.getMessage());
        }

        return conn; // 싱싱한 연결 반환
    }

    // 메서드 파라미터로 conn을 받아오도록 수정
    private static void createTableIfNotExists(Connection conn) {
        String sql = "CREATE TABLE IF NOT EXISTS ssul (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT NOT NULL, " +
                "content TEXT, " +
                "category TEXT, " +
                "reg_date TEXT)";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            // System.out.println("테이블 확인 완료"); // 로그가 너무 많으면 주석 처리해도 됨
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
