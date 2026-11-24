package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.Lecturer;

public class LecturerDAO {

    public boolean addLecturer(Lecturer l) {
        String sql = "INSERT INTO Lecturers (name, email) VALUES (?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, l.getName());
            ps.setString(2, l.getEmail());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<Lecturer> viewLecturers() {
        ArrayList<Lecturer> lecturers = new ArrayList<>();
        String sql = "SELECT * FROM Lecturers";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Lecturer l = new Lecturer(rs.getString("name"), rs.getString("email"));
                l.setLecturerId(rs.getInt("lecturer_id"));
                lecturers.add(l);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return lecturers;
    }
}
