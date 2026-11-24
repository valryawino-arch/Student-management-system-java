package ui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;

public class NavBar {

    public static JMenuBar back(JFrame frame) {
        JMenuBar bar = new JMenuBar();
        JButton button = new JButton("Back to Dashboard");
        button.addActionListener(e -> frame.dispose());
        bar.add(button);
        return bar;
    }
}