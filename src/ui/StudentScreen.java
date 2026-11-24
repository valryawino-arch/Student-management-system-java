package ui;

import database.StudentDAO;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import model.PostgraduateStudent;
import model.Student;
import model.UndergraduateStudent;

public class StudentScreen extends JFrame {

    private StudentDAO dao = new StudentDAO();
    private DefaultTableModel tableModel;
    private JTable table;

    private JTextField admField = new JTextField();
    private JTextField nameField = new JTextField();
    private JTextField emailField = new JTextField();
    private JTextField phoneField = new JTextField();
    private JTextField yearField = new JTextField();
    private JComboBox<String> categoryBox = new JComboBox<>(new String[]{"Undergraduate", "Postgraduate"});
    private JTextField searchField = new JTextField();

    public StudentScreen() {
        setTitle("Student Management");
        setSize(800, 550);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Top: search
        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        JButton searchButton = new JButton("Search");
        JButton viewAllButton = new JButton("View All");
        JPanel searchButtons = new JPanel(new GridLayout(1, 2, 5, 5));
        searchButtons.add(searchButton);
        searchButtons.add(viewAllButton);
        searchPanel.add(new JLabel(" Admission No or Name: "), BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButtons, BorderLayout.EAST);

        // Middle: table
        tableModel = new DefaultTableModel(new String[]{"Admission No", "Name", "Email", "Phone", "Year", "Category"}, 0);
        table = new JTable(tableModel);

        // Bottom: form and buttons
        JPanel form = new JPanel(new GridLayout(6, 2, 5, 5));
        form.add(new JLabel("Admission No (e.g. I163/7462/2024):")); form.add(admField);
        form.add(new JLabel("Name:")); form.add(nameField);
        form.add(new JLabel("Email:")); form.add(emailField);
        form.add(new JLabel("Phone:")); form.add(phoneField);
        form.add(new JLabel("Year of Study:")); form.add(yearField);
        form.add(new JLabel("Category:")); form.add(categoryBox);

        JButton registerButton = new JButton("Register");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton clearButton = new JButton("Clear");
        JPanel buttons = new JPanel(new GridLayout(1, 4, 5, 5));
        buttons.add(registerButton);
        buttons.add(updateButton);
        buttons.add(deleteButton);
        buttons.add(clearButton);

        JPanel bottom = new JPanel(new BorderLayout(5, 5));
        bottom.add(form, BorderLayout.CENTER);
        bottom.add(buttons, BorderLayout.SOUTH);

        add(searchPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        // Actions
        registerButton.addActionListener(e -> registerStudent());
        updateButton.addActionListener(e -> updateStudent());
        deleteButton.addActionListener(e -> deleteStudent());
        clearButton.addActionListener(e -> clearForm());
        searchButton.addActionListener(e -> showStudents(dao.searchStudent(searchField.getText().trim())));
        viewAllButton.addActionListener(e -> showStudents(dao.viewStudents()));
        table.getSelectionModel().addListSelectionListener(e -> fillFormFromTable());

        showStudents(dao.viewStudents());
        setJMenuBar(NavBar.back(this));
        setVisible(true);
    }

    // Builds a student from the form; setters validate the input
    private Student readForm() {
        String adm = admField.getText().trim();
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        int year = Integer.parseInt(yearField.getText().trim());

        Student s;
        if (categoryBox.getSelectedItem().equals("Postgraduate")) {
            s = new PostgraduateStudent(adm, name, email, phone, year);
        } else {
            s = new UndergraduateStudent(adm, name, email, phone, year);
        }
        s.setName(name);
        s.setEmail(email);
        s.setPhone(phone);
        s.setYearOfStudy(year);
        return s;
    }

    private void registerStudent() {
        try {
            Student s = readForm();
            if (dao.registerStudent(s)) {
                JOptionPane.showMessageDialog(this, "Student registered.");
                clearForm();
                showStudents(dao.viewStudents());
            } else {
                JOptionPane.showMessageDialog(this, "Could not register. Admission number may already exist.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Year must be a number.");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void updateStudent() {
        try {
            Student s = readForm();
            if (dao.updateStudent(s)) {
                JOptionPane.showMessageDialog(this, "Student updated.");
                showStudents(dao.viewStudents());
            } else {
                JOptionPane.showMessageDialog(this, "Student not found.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Year must be a number.");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void deleteStudent() {
        String adm = admField.getText().trim();
        if (adm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a student first.");
            return;
        }
        int choice = JOptionPane.showConfirmDialog(this, "Delete " + adm + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION && dao.deleteStudent(adm)) {
            clearForm();
            showStudents(dao.viewStudents());
        }
    }

    private void showStudents(ArrayList<Student> students) {
        tableModel.setRowCount(0);
        for (Student s : students) {
            tableModel.addRow(new Object[]{s.getAdmissionNo(), s.getName(), s.getEmail(),
                    s.getPhone(), s.getYearOfStudy(), s.getCategory()});
        }
    }

    private void fillFormFromTable() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        admField.setText(tableModel.getValueAt(row, 0).toString());
        nameField.setText(tableModel.getValueAt(row, 1).toString());
        emailField.setText(tableModel.getValueAt(row, 2).toString());
        phoneField.setText(tableModel.getValueAt(row, 3).toString());
        yearField.setText(tableModel.getValueAt(row, 4).toString());
        categoryBox.setSelectedItem(tableModel.getValueAt(row, 5).toString());
    }

    private void clearForm() {
        admField.setText("");
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        yearField.setText("");
        table.clearSelection();
    }
}