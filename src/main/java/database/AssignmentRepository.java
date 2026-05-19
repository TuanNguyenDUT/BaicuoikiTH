package database;

import java.sql.*;

/**
 * Lưu/đọc trạng thái line phân công vào PostgreSQL.
 *
 * Bảng assignment_state:
 *   key   VARCHAR(50) PRIMARY KEY  -- tên khoá, mặc định 'current_line'
 *   value INT NOT NULL DEFAULT 0   -- giá trị line hiện tại
 */
public class AssignmentRepository {

    private static final String KEY = "current_line";

    /**
     * Tạo bảng nếu chưa tồn tại và khởi tạo hàng mặc định.
     */
    public static void initTable() {
        String createSQL = "CREATE TABLE IF NOT EXISTS assignment_state ("
                + "key   VARCHAR(50) PRIMARY KEY, "
                + "value INT NOT NULL DEFAULT 0)";
        String insertSQL = "INSERT INTO assignment_state (key, value) "
                + "VALUES (?, 0) ON CONFLICT (key) DO NOTHING";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement()) {
            st.execute(createSQL);
            try (PreparedStatement ps = conn.prepareStatement(insertSQL)) {
                ps.setString(1, KEY);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("initTable lỗi: " + e.getMessage());
        }
    }

    /**
     * Đọc line hiện tại từ DB.
     * @return giá trị line, hoặc 0 nếu lỗi
     */
    public static int getCurrentLine() {
        String sql = "SELECT value FROM assignment_state WHERE key = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, KEY);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("value");
        } catch (SQLException e) {
            System.err.println("getCurrentLine lỗi: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Lưu giá trị line mới vào DB.
     * @param line giá trị cần lưu
     */
    public static void saveLine(int line) {
        String sql = "INSERT INTO assignment_state (key, value) VALUES (?, ?) "
                + "ON CONFLICT (key) DO UPDATE SET value = EXCLUDED.value";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, KEY);
            ps.setInt(2, line);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("saveLine lỗi: " + e.getMessage());
        }
    }

    /**
     * Lấy line hiện tại, rồi tăng lên 1 (mod Lmax) và lưu lại.
     * Đảm bảo mỗi ca thi dùng một line khác nhau, không trùng.
     *
     * @param Lmax số lần tối đa (từ tinhLmax())
     * @return line đã được dùng cho ca này
     */
    public static int getAndIncrementLine(int Lmax) {
        if (Lmax <= 0) return 0;
        int current = getCurrentLine();
        int next = (current + 1) % Lmax;
        saveLine(next);
        return current;
    }
}
