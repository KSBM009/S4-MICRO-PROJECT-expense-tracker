import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

class LoginForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;

    private final String DB_URL = "jdbc:mysql://localhost:3306/java_s4_mini_project";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "4112003";

    public LoginForm() {
        setTitle("Login Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(15);

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(15);

        loginButton = new JButton("Login");

        registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the login form
                new RegisterForm().setVisible(true); // Open the register form
            }
        });

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(registerButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] password = passwordField.getPassword();

                if (authenticate(username, password)) {
                    // Code for successful login
                    JOptionPane.showMessageDialog(null, "Login Successful");
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username or password");
                }
            }
        });

        add(panel, BorderLayout.CENTER);
        pack();
        // Center the form on the screen
        setLocationRelativeTo(null);
    }

    private boolean authenticate(String username, char[] password) {
        boolean isValid = false;

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, new String(password));
                ResultSet rs = stmt.executeQuery();
                isValid = rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isValid;
    }
}

class RegisterForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton registerButton;

    private final String DB_URL = "jdbc:mysql://localhost:3306/your_database_name";
    private final String DB_USER = "your_username";
    private final String DB_PASSWORD = "your_password";

    public RegisterForm() {
        setTitle("Register Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(15);

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(15);

        registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] password = passwordField.getPassword();

                if (register(username, password)) {
                    // Code for successful registration
                    JOptionPane.showMessageDialog(null, "Registration Successful");
                    dispose(); // Close the register form
                    new LoginForm().setVisible(true); // Open the login form
                } else {
                    JOptionPane.showMessageDialog(null, "Registration Failed");
                }
            }
        });

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(registerButton);

        add(panel, BorderLayout.CENTER);
        pack();
        // Center the form on the screen
        setLocationRelativeTo(null);
    }

    private boolean register(String username, char[] password) {
        boolean isSuccess = false;

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, new String(password));
                int rowsAffected = stmt.executeUpdate();
                isSuccess = rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isSuccess;
    }
}

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginForm().setVisible(true);
            }
        });
    }
}
