package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardDAO {

    public int countStudents() { return count("SELECT COUNT(*) FROM Students"); }

    public int countCourses() { return count("SELECT COUNT(*) FROM Courses"); }

    public int countResults() { return count("SELECT COUNT(*) FROM Examination_Results"); }

    private int count(String sql) {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return 0;
    }
}