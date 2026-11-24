package ui;

import database.ResultDAO;
import database.StudentDAO;
import java.awt.BorderLayout;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import model.Student;
import service.StudentManagementSystem;

public class ReportScreen extends JFrame {

    private StudentDAO studentDAO = new StudentDAO();
    private ResultDAO resultDAO = new ResultDAO();
    private StudentManagementSystem sms = new StudentManagementSystem();
    private DefaultTableModel tableModel;

    public ReportScreen() {
        setTitle("Academic Performance Report");
        setSize(800, 450);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        tableModel = new DefaultTableModel(
                new String[]{"Admission No", "Name", "Units", "Highest", "Lowest", "Average", "Grade"}, 0);
        JTable table = new JTable(tableModel);

        JButton refreshButton = new JButton("Refresh Report");
        refreshButton.addActionListener(e -> generateReport());

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(refreshButton, BorderLayout.SOUTH);

        generateReport();
        setJMenuBar(NavBar.back(this));
        setVisible(true);
    }

    private void generateReport() {
        tableModel.setRowCount(0);
        for (Student s : studentDAO.viewStudents()) {
            int[] marks = resultDAO.getMarks(s.getAdmissionNo());

            if (marks.length == 0) {
                tableModel.addRow(new Object[]{s.getAdmissionNo(), s.getName(), 0, "-", "-", "-", "-"});
                continue;
            }

            int[] sorted = marks.clone();
            Arrays.sort(sorted);
            double average = sms.calculateAverage(marks);

            tableModel.addRow(new Object[]{s.getAdmissionNo(), s.getName(), marks.length,
                    sorted[sorted.length - 1], sorted[0], average, sms.calculateGrade(average)});
        }
    }
}