package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ResultDAO {

    public boolean recordMarks(String admissionNo, String unitCode, String unitName, int marks) {
        if (marks < 0 || marks > 100) {
            System.out.println("Marks must be between 0 and 100.");
            return false;
        }
        String sql = "INSERT INTO Examination_Results (student_id, unit_code, unit_name, marks) SELECT student_id, ?, ?, ? FROM Students WHERE admission_no = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, unitCode);
            ps.setString(2, unitName);
            ps.setInt(3, marks);
            ps.setString(4, admissionNo);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<String> viewResults(String admissionNo) {
        ArrayList<String> results = new ArrayList<>();
        String sql = "SELECT r.unit_code, r.unit_name, r.marks FROM Examination_Results r JOIN Students s ON r.student_id = s.student_id WHERE s.admission_no = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, admissionNo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                results.add(rs.getString("unit_code") + " | " + rs.getString("unit_name") + " | " + rs.getInt("marks"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return results;
    }

    // Loads a student's marks from the database into an int array
    public int[] getMarks(String admissionNo) {
        ArrayList<Integer> list = new ArrayList<>();
        String sql = "SELECT r.marks FROM Examination_Results r JOIN Students s ON r.student_id = s.student_id WHERE s.admission_no = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, admissionNo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(rs.getInt("marks"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        int[] marks = new int[list.size()];
        for (int i = 0; i < marks.length; i++) {
            marks[i] = list.get(i);
        }
        return marks;
    }
}