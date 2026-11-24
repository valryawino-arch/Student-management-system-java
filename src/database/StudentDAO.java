
package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.PostgraduateStudent;
import model.Student;
import model.UndergraduateStudent;

public class StudentDAO {

    public boolean registerStudent(Student s) {
        String sql = "INSERT INTO Students (admission_no, name, email, phone, year_of_study, category) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, s.getAdmissionNo());
            ps.setString(2, s.getName());
            ps.setString(3, s.getEmail());
            ps.setString(4, s.getPhone());
            ps.setInt(5, s.getYearOfStudy());
            ps.setString(6, s.getCategory().toUpperCase());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                System.out.println("Admission number already exists.");
            } else {
                System.out.println("Error: " + e.getMessage());
            }
            return false;
        }
    }

    public ArrayList<Student> viewStudents() {
        ArrayList<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM Students";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                students.add(buildStudent(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return students;
    }

    public ArrayList<Student> searchStudent(String keyword) {
        ArrayList<Student> results = new ArrayList<>();
        String sql = "SELECT * FROM Students WHERE admission_no LIKE ? OR name LIKE ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                results.add(buildStudent(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return results;
    }

    public boolean updateStudent(Student s) {
        String sql = "UPDATE Students SET name = ?, email = ?, phone = ?, year_of_study = ? WHERE admission_no = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, s.getName());
            ps.setString(2, s.getEmail());
            ps.setString(3, s.getPhone());
            ps.setInt(4, s.getYearOfStudy());
            ps.setString(5, s.getAdmissionNo());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteStudent(String admissionNo) {
        String sql = "DELETE FROM Students WHERE admission_no = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, admissionNo);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    // Creates the right child object depending on the saved category
    private Student buildStudent(ResultSet rs) throws SQLException {
        String adm = rs.getString("admission_no");
        String name = rs.getString("name");
        String email = rs.getString("email");
        String phone = rs.getString("phone");
        int year = rs.getInt("year_of_study");

        Student s;
        if (rs.getString("category").equals("POSTGRADUATE")) {
            s = new PostgraduateStudent(adm, name, email, phone, year);
        } else {
            s = new UndergraduateStudent(adm, name, email, phone, year);
        }
        s.setStudentId(rs.getInt("student_id"));
        return s;
    }
}