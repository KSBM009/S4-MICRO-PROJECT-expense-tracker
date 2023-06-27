import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

class LoginForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel registerLabel;

    private final String DB_URL = "jdbc:mysql://localhost:3306/java_s4_mini_project";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "4112003";

    public LoginForm() {
        setTitle("Login Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(4, 2));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(15);

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(15);

        JButton loginButton = new JButton("Login");

        registerLabel = new JLabel("<html><u>New to the App?</u></html>");
        registerLabel.setForeground(Color.BLUE);
        registerLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        // Empty label for spacing
        panel.add(new JLabel());
        panel.add(loginButton);
        // Empty label for spacing
        panel.add(new JLabel());

        registerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Open the register form
                dispose();
                new RegisterForm().setVisible(true);
            }
        });

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] password = passwordField.getPassword();

                if (authenticate(username, password)) {
                    // Code for successful login
                    JOptionPane.showMessageDialog(null, "Login Successful");
                    // Close the login form
                    dispose();
                    // Open the Home page
                    new HomePage(username).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username or password!");
                }
            }
        });

        // Updated: Added registerLabel to the panel
        panel.add(registerLabel);

        add(panel, BorderLayout.CENTER);
        pack();
        // Center the form on the screen
        setLocationRelativeTo(null);
    }

    private boolean authenticate(String username, char[] password) {
        boolean isValid = false;

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM Users WHERE UserName = ? AND Password = ?";
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
    private JTextField nameField;
    private JTextField ageField;
    private JTextField salaryField;
    private JButton registerButton;

    private final String DB_URL = "jdbc:mysql://localhost:3306/java_s4_mini_project";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "4112003";

    public RegisterForm() {
        setTitle("Register Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(6, 2));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(15);

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(15);

        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField(15);

        JLabel ageLabel = new JLabel("Age:");
        ageField = new JTextField(15);

        JLabel salaryLabel = new JLabel("Monthly Salary:");
        salaryField = new JTextField(15);

        registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] password = passwordField.getPassword();
                String name = nameField.getText();
                String age = ageField.getText();
                String salary = salaryField.getText();

                if (name.isEmpty() || salary.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill in Your Name and Salary.");
                } else {
                    if (register(username, password, name, age, salary)) {
                        // Code for successful registration
                        JOptionPane.showMessageDialog(null, "Registration Successful");
                        // Close the register form
                        dispose();
                        // Open the login form
                        new LoginForm().setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(null, "Registration Failed");
                    }
                }
            }
        });

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(ageLabel);
        panel.add(ageField);
        panel.add(salaryLabel);
        panel.add(salaryField);
        // Empty label for spacing
        panel.add(new JLabel());
        panel.add(registerButton);

        add(panel, BorderLayout.CENTER);
        pack();
        // Center the form on the screen
        setLocationRelativeTo(null);
    }

    private boolean register(String username, char[] password, String name, String age, String salary) {
        boolean isSuccess = false;

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO Users (UserName, Password, Name, Age, Salary) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, new String(password));
                stmt.setString(3, name);
                stmt.setString(4, age);
                stmt.setString(5, salary);
                int rowsAffected = stmt.executeUpdate();
                isSuccess = rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isSuccess;
    }
}

class HomePage extends JFrame {
    private final String DB_URL = "jdbc:mysql://localhost:3306/java_s4_mini_project";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "4112003";

    private JLabel welcomeLabel;
    private JButton viewExpensesButton;
    private JButton addExpenseButton;

    public HomePage(String username) {
        setTitle("Home Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 1));

        // Get user's name from the database
        String name = getUserName(username);
        welcomeLabel = new JLabel("Welcome, " + name + "!");

        viewExpensesButton = new JButton("View Expenses");
        viewExpensesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the expenses view page
                new ExpensesViewPage(username).setVisible(true);
            }
        });

        addExpenseButton = new JButton("Add Expense");
        addExpenseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the add expense form
                new AddExpenseForm(username).setVisible(true);
            }
        });

        panel.add(welcomeLabel);
        panel.add(viewExpensesButton);
        panel.add(addExpenseButton);

        add(panel, BorderLayout.CENTER);
        pack();
        // Center the form on the screen
        setLocationRelativeTo(null);
    }

    private String getUserName(String username) {
        String name = "";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT Name FROM Users WHERE UserName = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    name = rs.getString("Name");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return name;
    }
}

class ExpensesViewPage extends JFrame {
    private final String DB_URL = "jdbc:mysql://localhost:3306/java_s4_mini_project";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "4112003";

    private JTextArea expensesTextArea;

    public ExpensesViewPage(String username) {
        setTitle("Expenses");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());

        expensesTextArea = new JTextArea(10, 30);
        expensesTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(expensesTextArea);

        panel.add(scrollPane, BorderLayout.CENTER);

        // Get expenses for the given username
        String expenses = getExpenses(username);
        expensesTextArea.setText(expenses);

        add(panel);
        pack();
        // Center the form on the screen
        setLocationRelativeTo(null);
    }

    private String getExpenses(String username) {
        StringBuilder sb = new StringBuilder();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM Expenses WHERE UserName = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String date = rs.getString("Date");
                    String category = rs.getString("Category");
                    String amount = rs.getString("Amount");
                    sb.append("Date: ").append(date).append("\n");
                    sb.append("Category: ").append(category).append("\n");
                    sb.append("Amount: ").append(amount).append("\n");
                    sb.append("\n");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}

class AddExpenseForm extends JFrame {
    private final String DB_URL = "jdbc:mysql://localhost:3306/java_s4_mini_project";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "4112003";

    private JTextField dateField;
    private JTextField categoryField;
    private JTextField amountField;
    private JButton addExpenseButton;

    private String username;

    public AddExpenseForm(String username) {
        setTitle("Add Expense");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.username = username;

        JPanel panel = new JPanel(new GridLayout(4, 2));

        JLabel dateLabel = new JLabel("Date:");
        dateField = new JTextField(15);

        JLabel categoryLabel = new JLabel("Category:");
        categoryField = new JTextField(15);

        JLabel amountLabel = new JLabel("Amount:");
        amountField = new JTextField(15);

        addExpenseButton = new JButton("Add Expense");
        addExpenseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String date = dateField.getText();
                String category = categoryField.getText();
                String amount = amountField.getText();

                if (addExpense(date, category, amount)) {
                    // Code for successful expense addition
                    JOptionPane.showMessageDialog(null, "Expense added successfully");
                    // Close the add expense form
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to add expense");
                }
            }
        });

        panel.add(dateLabel);
        panel.add(dateField);
        panel.add(categoryLabel);
        panel.add(categoryField);
        panel.add(amountLabel);
        panel.add(amountField);
        // Empty label for spacing
        panel.add(new JLabel());
        panel.add(addExpenseButton);

        add(panel, BorderLayout.CENTER);
        pack();
        // Center the form on the screen
        setLocationRelativeTo(null);
    }

    private boolean addExpense(String date, String category, String amount) {
        boolean isSuccess = false;
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO Expenses (UserName, Date, Category, Amount) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, date);
                stmt.setString(3, category);
                stmt.setString(4, amount);
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