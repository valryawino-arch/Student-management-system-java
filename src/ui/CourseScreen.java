package ui;

import database.CourseDAO;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import model.Course;

public class CourseScreen extends JFrame {

    private CourseDAO dao = new CourseDAO();
    private DefaultTableModel tableModel;
    private JTable table;

    private JTextField nameField = new JTextField();
    private JTextField durationField = new JTextField();
    private JTextField admField = new JTextField();

    public CourseScreen() {
        setTitle("Course Management");
        setSize(700, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Table
        tableModel = new DefaultTableModel(new String[]{"ID", "Course Name", "Duration (Years)"}, 0);
        table = new JTable(tableModel);

        // Course form
        JPanel form = new JPanel(new GridLayout(2, 2, 5, 5));
        form.add(new JLabel("Course Name:")); form.add(nameField);
        form.add(new JLabel("Duration (Years):")); form.add(durationField);

        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton clearButton = new JButton("Clear");
        JPanel buttons = new JPanel(new GridLayout(1, 4, 5, 5));
        buttons.add(addButton);
        buttons.add(updateButton);
        buttons.add(deleteButton);
        buttons.add(clearButton);

        // Assign students
        JPanel assign = new JPanel(new GridLayout(1, 4, 5, 5));
        JButton assignButton = new JButton("Assign to Selected Course");
        JButton viewStudentsButton = new JButton("View Course Students");
        assign.add(new JLabel("Student Admission No:"));
        assign.add(admField);
        assign.add(assignButton);
        assign.add(viewStudentsButton);

        JPanel bottom = new JPanel(new GridLayout(3, 1, 5, 5));
        bottom.add(form);
        bottom.add(buttons);
        bottom.add(assign);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        // Actions
        addButton.addActionListener(e -> addCourse());
        updateButton.addActionListener(e -> updateCourse());
        deleteButton.addActionListener(e -> deleteCourse());
        clearButton.addActionListener(e -> clearForm());
        assignButton.addActionListener(e -> assignStudent());
        viewStudentsButton.addActionListener(e -> viewCourseStudents());
        table.getSelectionModel().addListSelectionListener(e -> fillFormFromTable());

        showCourses();
        setJMenuBar(NavBar.back(this));
        setVisible(true);
    }

    private void addCourse() {
        try {
            Course c = new Course(nameField.getText().trim(), Integer.parseInt(durationField.getText().trim()), 0);
            c.setCourseName(c.getCourseName());
            c.setDurationYears(c.getDurationYears());
            if (dao.addCourse(c)) {
                JOptionPane.showMessageDialog(this, "Course added.");
                clearForm();
                showCourses();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Duration must be a number.");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void updateCourse() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a course first.");
            return;
        }
        try {
            Course c = new Course(nameField.getText().trim(), Integer.parseInt(durationField.getText().trim()), 0);
            c.setCourseName(c.getCourseName());
            c.setDurationYears(c.getDurationYears());
            c.setCourseId(getSelectedCourseId());
            if (dao.updateCourse(c)) {
                JOptionPane.showMessageDialog(this, "Course updated.");
                showCourses();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Duration must be a number.");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void deleteCourse() {
        if (table.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this, "Select a course first.");
            return;
        }
        int choice = JOptionPane.showConfirmDialog(this, "Delete this course?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION && dao.deleteCourse(getSelectedCourseId())) {
            clearForm();
            showCourses();
        }
    }

    private void assignStudent() {
        if (table.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this, "Select a course first.");
            return;
        }
        String adm = admField.getText().trim();
        if (dao.registerCourse(adm, getSelectedCourseId())) {
            JOptionPane.showMessageDialog(this, "Student assigned to course.");
            admField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Could not assign. Check the admission number, or the student may already be in this course.");
        }
    }

    private void viewCourseStudents() {
        if (table.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this, "Select a course first.");
            return;
        }
        ArrayList<String> students = dao.viewCourseStudents(getSelectedCourseId());
        if (students.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No students in this course yet.");
        } else {
            JOptionPane.showMessageDialog(this, String.join("\n", students), "Students in course", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private int getSelectedCourseId() {
        return Integer.parseInt(tableModel.getValueAt(table.getSelectedRow(), 0).toString());
    }

    private void showCourses() {
        tableModel.setRowCount(0);
        for (Course c : dao.viewCourses()) {
            tableModel.addRow(new Object[]{c.getCourseId(), c.getCourseName(), c.getDurationYears()});
        }
    }

    private void fillFormFromTable() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        nameField.setText(tableModel.getValueAt(row, 1).toString());
        durationField.setText(tableModel.getValueAt(row, 2).toString());
    }

    private void clearForm() {
        nameField.setText("");
        durationField.setText("");
        table.clearSelection();
    }
}