package it.soaringtech;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SsulDao {

    // 1. 데이터 저장 (INSERT)
    public void insert(Ssul ssul) {
        String sql = "INSERT INTO ssul(title, content, category, reg_date) VALUES(?, ?, ?, ?)";

        // try-with-resources: 자동으로 Connection과 Statement를 닫아줍니다.
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, ssul.getTitle());
            pstmt.setString(2, ssul.getContent());
            pstmt.setString(3, ssul.getCategory());
            pstmt.setString(4, ssul.getRegDate());

            pstmt.executeUpdate();
            System.out.println("[DB] 저장 완료: " + ssul.getTitle()); // 최소한의 확인 로그

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("[DB] 저장 실패!");
        }
    }

    // 2. 전체 목록 조회 (SELECT)
    public List<Ssul> selectList() {
        List<Ssul> list = new ArrayList<>();
        String sql = "SELECT * FROM ssul ORDER BY id DESC"; // 최신순 정렬

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Ssul ssul = new Ssul(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("category"),
                        rs.getString("reg_date")
                );
                list.add(ssul);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // 3. 상세 조회 (SELECT ONE) - 나중에 상세 화면에서 사용
    public Ssul selectOne(int id) {
        String sql = "SELECT * FROM ssul WHERE id = ?";
        Ssul ssul = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    ssul = new Ssul(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("content"),
                            rs.getString("category"),
                            rs.getString("reg_date")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ssul;
    }

    // 4. 데이터 삭제 (DELETE) - 나중에 삭제 기능에서 사용
    public void delete(int id) {
        String sql = "DELETE FROM ssul WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("[DB] 삭제 완료 ID: " + id);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Ssul> selectByCategory(String category) {
        List<Ssul> list = new ArrayList<>();
        // SQL: category 컬럼이 입력받은 값과 똑같은 것만 가져오라!
        String sql = "SELECT * FROM ssul WHERE category = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, category); // ? 에 "유머", "공포" 등을 채워넣음

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String tit = rs.getString("title");
                    String con = rs.getString("content");
                    String cat = rs.getString("category");
                    String dat = rs.getString("reg_Date");
                    Ssul s = new Ssul(id, tit, con, cat, dat);
                    list.add(s);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void update(Ssul ssul) {
        String sql = "UPDATE ssul SET title = ?, content = ?, category = ? WHERE id = ?";
        // 수정된 부분: DriverManager 대신 DBConnection.getConnection() 사용
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, ssul.getTitle());
            pstmt.setString(2, ssul.getContent());
            pstmt.setString(3, ssul.getCategory());
            pstmt.setInt(4, ssul.getId());

            int result = pstmt.executeUpdate();
            if (result > 0) {
                System.out.println("썰 수정 완료: " + ssul.getTitle());
            }
        } catch (SQLException e) {
            System.out.println("수정 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }
}