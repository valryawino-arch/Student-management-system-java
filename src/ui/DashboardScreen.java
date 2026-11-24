package ui;

import database.DashboardDAO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class DashboardScreen extends JFrame {

    public DashboardScreen() {
        DashboardDAO dao = new DashboardDAO();

        setTitle("Dashboard");
        setSize(750, 480);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel();
        header.setBackground(new Color(37, 99, 235));
        header.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel title = new JLabel("Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        header.add(title);

        // Stat cards
        JPanel stats = new JPanel(new GridLayout(1, 3, 20, 20));
        stats.setBackground(new Color(245, 247, 250));
        stats.setBorder(BorderFactory.createEmptyBorder(30, 30, 15, 30));
        stats.add(createCard("Students", dao.countStudents()));
        stats.add(createCard("Courses", dao.countCourses()));
        stats.add(createCard("Exam Records", dao.countResults()));

        // Navigation buttons
        JPanel buttons = new JPanel(new GridLayout(1, 4, 15, 15));
        buttons.setBackground(new Color(245, 247, 250));
        buttons.setBorder(BorderFactory.createEmptyBorder(15, 30, 30, 30));
        String[] names = {"Students", "Courses", "Results", "Reports"};
        for (String name : names) {
            JButton b = new JButton(name);
            b.addActionListener(e -> {
                if (name.equals("Students")) {
                    new StudentScreen();
                } else if (name.equals("Courses")) {
                    new CourseScreen();
                } else if (name.equals("Results")) {
                    new ResultScreen();
                } else {
                    new ReportScreen();
                }
            });
            buttons.add(b);
        }

        add(header, BorderLayout.NORTH);
        add(stats, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createCard(String label, int value) {
        JPanel card = new JPanel(new GridLayout(2, 1));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        JLabel number = new JLabel(String.valueOf(value), SwingConstants.CENTER);
        number.setFont(new Font("Segoe UI", Font.BOLD, 40));
        number.setForeground(new Color(37, 99, 235));

        JLabel text = new JLabel(label, SwingConstants.CENTER);
        text.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        card.add(number);
        card.add(text);
        return card;
    }
}