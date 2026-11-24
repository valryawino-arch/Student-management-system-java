package ui;

import database.ResultDAO;
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
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import service.StudentManagementSystem;

public class ResultScreen extends JFrame {

    private ResultDAO dao = new ResultDAO();
    private StudentManagementSystem sms = new StudentManagementSystem();
    private DefaultTableModel tableModel;
    private JLabel summaryLabel = new JLabel("Average: -   Grade: -", SwingConstants.CENTER);

    private JTextField admField = new JTextField();
    private JTextField codeField = new JTextField();
    private JTextField unitField = new JTextField();
    private JTextField marksField = new JTextField();

    public ResultScreen() {
        setTitle("Examination Results");
        setSize(700, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Table
        tableModel = new DefaultTableModel(new String[]{"Unit Code", "Unit Name", "Marks"}, 0);
        JTable table = new JTable(tableModel);

        // Form
        JPanel form = new JPanel(new GridLayout(4, 2, 5, 5));
        form.add(new JLabel("Student Admission No:")); form.add(admField);
        form.add(new JLabel("Unit Code:")); form.add(codeField);
        form.add(new JLabel("Unit Name:")); form.add(unitField);
        form.add(new JLabel("Marks (0-100):")); form.add(marksField);

        JButton recordButton = new JButton("Record Marks");
        JButton viewButton = new JButton("View Results");
        JPanel buttons = new JPanel(new GridLayout(1, 2, 5, 5));
        buttons.add(recordButton);
        buttons.add(viewButton);

        JPanel bottom = new JPanel(new GridLayout(3, 1, 5, 5));
        bottom.add(form);
        bottom.add(buttons);
        bottom.add(summaryLabel);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        recordButton.addActionListener(e -> recordMarks());
        viewButton.addActionListener(e -> viewResults());

        setJMenuBar(NavBar.back(this));
        setVisible(true);
    }

    private void recordMarks() {
        try {
            int marks = Integer.parseInt(marksField.getText().trim());
            boolean saved = dao.recordMarks(admField.getText().trim(), codeField.getText().trim(),
                    unitField.getText().trim(), marks);
            if (saved) {
                JOptionPane.showMessageDialog(this, "Marks recorded.");
                codeField.setText("");
                unitField.setText("");
                marksField.setText("");
                viewResults();
            } else {
                JOptionPane.showMessageDialog(this, "Could not record. Check the admission number and marks (0-100).");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Marks must be a number.");
        }
    }

    private void viewResults() {
        String adm = admField.getText().trim();
        tableModel.setRowCount(0);

        ArrayList<String> results = dao.viewResults(adm);
        for (String r : results) {
            tableModel.addRow(r.split(" \\| "));
        }

        int[] marks = dao.getMarks(adm);
        double average = sms.calculateAverage(marks);
        summaryLabel.setText("Average: " + average + "   Grade: " + sms.calculateGrade(average));
    }
}