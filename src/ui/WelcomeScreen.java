package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class WelcomeScreen extends JFrame {

    public WelcomeScreen() {
        setTitle("Student Management System");
        setSize(600, 380);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 20));
        panel.setBackground(new Color(37, 99, 235));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 80, 50, 80));

        JLabel title = new JLabel("Student Management System", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Manage students, courses and results", SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitle.setForeground(new Color(219, 234, 254));

        JButton startButton = new JButton("Get Started");
        startButton.setBackground(Color.WHITE);
        startButton.setForeground(new Color(37, 99, 235));
        startButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        startButton.addActionListener(e -> {
            new DashboardScreen();
            dispose();
        });

        panel.add(title);
        panel.add(subtitle);
        panel.add(startButton);

        add(panel);
        setVisible(true);
    }
}