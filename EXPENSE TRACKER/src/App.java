import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.swing.table.DefaultTableModel;

class LoginForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel registerLabel;
    private JCheckBox staySignedInCheckbox;

    private final String DB_URL = "jdbc:mysql://localhost:3306/java_s4_mini_project";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "4112003";

    public LoginForm() {
        setTitle("Login Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(5, 2));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(15);

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(15);

        JButton loginButton = new JButton("Login");

        registerLabel = new JLabel("<html><u>New to the App?</u></html>");
        registerLabel.setForeground(Color.BLUE);
        registerLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        staySignedInCheckbox = new JCheckBox("Stay Signed In");

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performLogin();
                }
            }
        });
        // Empty label for spacing
        panel.add(new JLabel());
        panel.add(loginButton);
        // Empty label for spacing
        panel.add(new JLabel());
        panel.add(registerLabel);
        // Empty label for spacing
        panel.add(new JLabel());
        panel.add(staySignedInCheckbox);

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
                performLogin();
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

    private void performLogin() {
        String username = usernameField.getText();
        char[] password = passwordField.getPassword();
        boolean staySignedIn = staySignedInCheckbox.isSelected();

        if (authenticate(username, password)) {
            // Code for successful login
            JOptionPane.showMessageDialog(null, "Login Successful");
            // Close the login form
            dispose();
            // Open the Home page
            new HomePage(username, staySignedIn).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Invalid username or password!");
        }
    }
}

class RegisterForm extends JFrame {
    private JTextField nameField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField ageField;
    private JTextField salaryField;
    private JButton registerButton;
    private JButton cancelButton;

    private final String DB_URL = "jdbc:mysql://localhost:3306/java_s4_mini_project";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "4112003";

    public RegisterForm() {
        setTitle("Register Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(7, 3));

        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField(15);

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(15);

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(15);

        JLabel ageLabel = new JLabel("Age:");
        ageField = new JTextField(15);

        JLabel salaryLabel = new JLabel("Monthly Salary:");
        salaryField = new JTextField(15);

        registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String username = usernameField.getText();
                char[] password = passwordField.getPassword();
                String age = ageField.getText();
                String salary = salaryField.getText();

                if (name.isEmpty() || salary.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill in Your Name and Salary.");
                } else {
                    if (registerUser(name, username, password, age, salary)) {
                        // Code for successful registration
                        JOptionPane.showMessageDialog(null, "Registration Successful. Please log in.");
                        // Close the registration form
                        dispose();
                        // Open the login form
                        new LoginForm().setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(null, "Registration Failed. Please try again.");
                    }
                }
            }
        });

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the form
                // Open the login form
                new LoginForm().setVisible(true);
            }
        });

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(ageLabel);
        panel.add(ageField);
        panel.add(salaryLabel);
        panel.add(salaryField);
        panel.add(cancelButton);
        panel.add(registerButton);

        add(panel, BorderLayout.CENTER);
        pack();
        // Center the form on the screen
        setLocationRelativeTo(null);
    }

    private boolean registerUser(String name, String username, char[] password, String age, String salary) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO Users (Name, UserName, Password, Age, Salary) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, name);
                stmt.setString(2, username);
                stmt.setString(3, new String(password));
                stmt.setString(4, age);
                stmt.setString(5, salary);
                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

class HomePage extends JFrame {
    private final String DB_URL = "jdbc:mysql://localhost:3306/java_s4_mini_project";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "4112003";

    private JLabel welcomeLabel;
    private JLabel totalExpensesLabel;
    private JTable expensesTable;
    private JButton addExpenseButton;
    private JButton deleteExpenseButton;
    private JButton logoutButton;
    private JCheckBox groupByCategoryCheckbox;

    public HomePage(String username, boolean staySignedIn) {
        setTitle("Home Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());

        // Get user's name from the database
        String name = getUserName(username);
        welcomeLabel = new JLabel("Welcome, " +name+ "!");

        // Create the expenses table
        expensesTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(expensesTable);

        // Create the table model
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Expense Name");
        tableModel.addColumn("Amount");
        tableModel.addColumn("Date");
        tableModel.addColumn("Category");
        expensesTable.setModel(tableModel);

        // Get expenses for the given username
        getExpenses(username, tableModel);

        addExpenseButton = new JButton("Add Expense");
        addExpenseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the add expense form
                AddExpenseForm addExpenseForm = new AddExpenseForm(username);
                addExpenseForm.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                        refreshTable(username, tableModel); // Refresh the table when the Add Expense form is closed
                    }
                });
                addExpenseForm.setVisible(true);
            }
        });

        deleteExpenseButton = new JButton("Delete Expense");
        deleteExpenseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Check if a row is selected
                int selectedRow = expensesTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Please select an expense to delete.");
                } else {
                    // Get the expense ID from the selected row
                    String expenseID = (String) expensesTable.getValueAt(selectedRow, 0);
                    // Confirm deletion
                    int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this expense?", "Delete Expense", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        // Delete the expense from the database
                        if (deleteExpense(expenseID)) {
                            // Remove the row from the table
                            tableModel.removeRow(selectedRow);
                            JOptionPane.showMessageDialog(null, "Expense deleted successfully.");
                        } else {
                            JOptionPane.showMessageDialog(null, "Failed to delete expense.");
                        }
                    }
                }
            }
        });

        logoutButton = new JButton("Log Out");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Prompt the user to log in again
                int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to log out?", "Log Out", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    dispose(); // Close the current window
                    // Open the login form
                    new LoginForm().setVisible(true);
                }
            }
        });

        groupByCategoryCheckbox = new JCheckBox("Group by Category");
        groupByCategoryCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Refresh the table with grouping if checkbox is selected
                refreshTable(username, tableModel);
            }
        });

        totalExpensesLabel = new JLabel("Total Expenses: " + getTotalExpenses(username));

        panel.add(welcomeLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addExpenseButton);
        buttonPanel.add(deleteExpenseButton);
        buttonPanel.add(logoutButton);
        buttonPanel.add(groupByCategoryCheckbox);

        panel.add(buttonPanel, BorderLayout.SOUTH);
        panel.add(totalExpensesLabel, BorderLayout.SOUTH);

        add(panel);
        pack();
        // Center the form on the screen
        setLocationRelativeTo(null);

        // Stay signed in functionality
        if (!staySignedIn) {
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }
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

    private void getExpenses(String username, DefaultTableModel tableModel) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM Expenses WHERE UserName = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String ID = rs.getString("ExpenseID");
                    String Ename = rs.getString("ExpenseName");
                    double amount = rs.getDouble("ExpenseAmt");
                    String date = rs.getString("Date");
                    String category = rs.getString("Category");
                    
                    // Add the expense to the table
                    tableModel.addRow(new Object[]{ID, Ename, amount, date, category});
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void refreshTable(String username, DefaultTableModel tableModel) {
        // Clear the table
        tableModel.setRowCount(0);
        // Get the latest expenses
        getExpenses(username, tableModel);
        // Get expenses based on grouping
        if (groupByCategoryCheckbox.isSelected()) {
            getGroupedExpenses(username, tableModel);
        } else {
            getExpenses(username, tableModel);
        }
        // Update the total expenses label
        totalExpensesLabel.setText("Total Expenses: " + getTotalExpenses(username));
    }

    private void getGroupedExpenses(String username, DefaultTableModel tableModel) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT Category, SUM(ExpenseAmt) AS TotalAmount FROM Expenses WHERE UserName = ? GROUP BY Category";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String category = rs.getString("Category");
                    double totalAmount = rs.getDouble("TotalAmount");
                    // Add the category and total amount as a row in the table
                    tableModel.addRow(new Object[]{"", "", totalAmount, "", category});
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean deleteExpense(String expenseID) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "DELETE FROM Expenses WHERE ExpenseID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, expenseID);
                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private double getTotalExpenses(String username) {
        double totalExpenses = 0.0;

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT SUM(ExpenseAmt) AS TotalAmount FROM Expenses WHERE UserName = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    totalExpenses = rs.getDouble("TotalAmount");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalExpenses;
    }
}

class AddExpenseForm extends JFrame {
    private final String DB_URL = "jdbc:mysql://localhost:3306/java_s4_mini_project";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "4112003";

    private JTextField expenseNameField;
    private JTextField categoryField;
    private JTextField amountField;
    private JButton addExpenseButton;
    private JButton cancelButton;

    public AddExpenseForm(String username) {
        setTitle("Add Expense");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(5, 2));

        JLabel expenseNameLabel = new JLabel("Expense Name:");
        expenseNameField = new JTextField(15);

        JLabel categoryLabel = new JLabel("Category:");
        categoryField = new JTextField(15);

        JLabel amountLabel = new JLabel("Amount:");
        amountField = new JTextField(15);

        addExpenseButton = new JButton("Add Expense");
        addExpenseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String expenseName = expenseNameField.getText();
                double amount = Double.parseDouble(amountField.getText());
                String category = categoryField.getText();

                if (addExpense(username, expenseName, amount, category)) {
                    JOptionPane.showMessageDialog(null, "Expense added successfully.");
                    dispose(); // Close the form
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to add expense.");
                }
            }
        });

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the form
            }
        });

        panel.add(expenseNameLabel);
        panel.add(expenseNameField);
        panel.add(amountLabel);
        panel.add(amountField);
        panel.add(categoryLabel);
        panel.add(categoryField);
        panel.add(cancelButton);
        panel.add(addExpenseButton);

        add(panel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
    }

    private boolean addExpense(String username, String expenseName, double amount, String category) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO Expenses (UserName, ExpenseID, ExpenseName, ExpenseAmt, Date, Category) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);

                String uniqueID = generateUniqueID(conn);
                stmt.setString(2, uniqueID);

                stmt.setString(3, expenseName);
                stmt.setDouble(4, amount);

                LocalDateTime currentDateTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = currentDateTime.format(formatter);
                stmt.setString(4, formattedDateTime);

                stmt.setString(5, category);
                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String generateUniqueID(Connection conn) {
        String uniqueID;
        boolean isUnique;
        do {
            uniqueID = generateRandomID();
            isUnique = checkIDUniqueness(conn, uniqueID);
        } while (!isUnique);
        return uniqueID;
    }

    private String generateRandomID() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(10);
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }

    private boolean checkIDUniqueness(Connection conn, String id) {
        boolean isUnique = true;
        try {
            String sql = "SELECT COUNT(*) FROM Expenses WHERE ExpenseID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int count = rs.getInt(1);
                        isUnique = count == 0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isUnique;
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