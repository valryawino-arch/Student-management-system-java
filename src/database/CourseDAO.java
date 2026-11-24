package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import model.Course;

public class CourseDAO {

    public boolean addCourse(Course c) {
        String sql = "INSERT INTO Courses (course_name, duration_years, lecturer_id) VALUES (?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getCourseName());
            ps.setInt(2, c.getDurationYears());
            if (c.getLecturerId() > 0) {
                ps.setInt(3, c.getLecturerId());
            } else {
                ps.setNull(3, Types.INTEGER);
            }
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<Course> viewCourses() {
        ArrayList<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM Courses";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Course c = new Course(rs.getString("course_name"), rs.getInt("duration_years"), rs.getInt("lecturer_id"));
                c.setCourseId(rs.getInt("course_id"));
                courses.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return courses;
    }

    public boolean updateCourse(Course c) {
        String sql = "UPDATE Courses SET course_name = ?, duration_years = ? WHERE course_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getCourseName());
            ps.setInt(2, c.getDurationYears());
            ps.setInt(3, c.getCourseId());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteCourse(int courseId) {
        String sql = "DELETE FROM Courses WHERE course_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, courseId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    // Assign a student (by admission number) to a course
    public boolean registerCourse(String admissionNo, int courseId) {
        String sql = "INSERT INTO Enrollments (student_id, course_id) SELECT student_id, ? FROM Students WHERE admission_no = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, courseId);
            ps.setString(2, admissionNo);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                System.out.println("Student is already in this course.");
            } else {
                System.out.println("Error: " + e.getMessage());
            }
            return false;
        }
    }

    // List the students in a course
    public ArrayList<String> viewCourseStudents(int courseId) {
        ArrayList<String> list = new ArrayList<>();
        String sql = "SELECT s.admission_no, s.name FROM Students s JOIN Enrollments e ON s.student_id = e.student_id WHERE e.course_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, courseId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("admission_no") + " | " + rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return list;
    }
}