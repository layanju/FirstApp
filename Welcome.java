package pack305;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Welcome extends JFrame {

    private static final String URL = "jdbc:mysql://localhost:3306/project2";
    private static final String USER = "root";
    private static final String PASSWORD = "layanasdf";

    // Main method to start the consultation system
    public void Start() {
        // Create a new JFrame (window)
        JFrame myFrame = new JFrame("Consultation System");
        myFrame.setSize(900, 700);
        myFrame.setResizable(false);
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a title label at the top of the window
        JLabel titleLabel = new JLabel("Welcome to the Consultation System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBounds(0, 420, myFrame.getWidth(), 50);
        myFrame.getContentPane().add(titleLabel);

        // Add a background image to the frame
        ImageIcon backgroundImage = new ImageIcon(Welcome.class.getResource("/image/background2.png"));
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setBounds(0, -40, myFrame.getWidth(), myFrame.getHeight());
        myFrame.setLayout(null);
        myFrame.getContentPane().add(backgroundLabel);

        // Create login and sign-up buttons
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(275, 500, 150, 45);
        loginButton.setBackground(Color.decode("#74b4c4"));

        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setBounds(475, 500, 150, 45);
        signUpButton.setBackground(Color.decode("#74b4c4"));

        // Make the frame visible
        myFrame.setVisible(true);

        // Add action listeners for the buttons
        loginButton.addActionListener(e -> openLoginFrame());
        signUpButton.addActionListener(e -> openSignUpFrame());

        myFrame.getContentPane().add(loginButton);
        myFrame.getContentPane().setComponentZOrder(loginButton, 0);
        myFrame.getContentPane().add(signUpButton);
        myFrame.getContentPane().setComponentZOrder(signUpButton, 0);
    }

    // Method to open the sign-up frame when the "Sign Up" button is clicked
    public void openSignUpFrame() {
        // Create a new JFrame for the sign-up window
        JFrame signUpFrame = new JFrame("Sign Up");
        signUpFrame.setSize(900, 700);
        signUpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Add a background image to the sign-up frame
        ImageIcon signUpBackground = new ImageIcon(Welcome.class.getResource("/image/signup1.png"));
        JLabel backgroundLabel = new JLabel(signUpBackground);
        backgroundLabel.setBounds(0, 0, signUpFrame.getWidth(), signUpFrame.getHeight());

        signUpFrame.setLayout(null);

        // Add text fields for first name, last name, username, and email
        JTextField firstNameField = addTextFieldWithLabel(signUpFrame, "First Name:", 500, 200, 600, 200);
        JTextField lastNameField = addTextFieldWithLabel(signUpFrame, "Last Name:", 500, 250, 600, 250);
        JTextField usernameField = addTextFieldWithLabel(signUpFrame, "Username:", 500, 300, 600, 300);
        JTextField emailField = addTextFieldWithLabel(signUpFrame, "Email:", 500, 350, 600, 350);

        // Add a password field for the user to enter their password
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(500, 400, 100, 25);
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 18));
        signUpFrame.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(600, 400, 250, 35);
        signUpFrame.add(passwordField);

        // Add radio buttons for the user to select their role (Consultative or User)
        JRadioButton consultativeButton = new JRadioButton("Consultative");
        consultativeButton.setBounds(550, 450, 150, 30);
        JRadioButton normalUserButton = new JRadioButton("User");
        normalUserButton.setBounds(700, 450, 150, 30);

        // Group the role radio buttons together to ensure only one is selected
        ButtonGroup group = new ButtonGroup();
        group.add(normalUserButton);
        group.add(consultativeButton);

        signUpFrame.add(consultativeButton);
        signUpFrame.add(normalUserButton);

        // Add profession radio buttons (optional for consultants)
        JRadioButton drawingButton = createRadioButton(signUpFrame, "Drawing", 550, 500, false);
        JRadioButton musicButton = createRadioButton(signUpFrame, "Music", 700, 500, false);
        JRadioButton writingButton = createRadioButton(signUpFrame, "Writing", 550, 530, false);
        JRadioButton photographyButton = createRadioButton(signUpFrame, "Photography", 700, 530, false);

        // Group profession buttons together
        ButtonGroup professionGroup = new ButtonGroup();
        professionGroup.add(drawingButton);
        professionGroup.add(musicButton);
        professionGroup.add(writingButton);
        professionGroup.add(photographyButton);

        signUpFrame.add(drawingButton);
        signUpFrame.add(musicButton);
        signUpFrame.add(writingButton);
        signUpFrame.add(photographyButton);

        consultativeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                drawingButton.setEnabled(true);
                musicButton.setEnabled(true);
                writingButton.setEnabled(true);
                photographyButton.setEnabled(true);
            }
        });

        normalUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                drawingButton.setEnabled(false);
                musicButton.setEnabled(false);
                writingButton.setEnabled(false);
                photographyButton.setEnabled(false);

                drawingButton.setSelected(false);
                musicButton.setSelected(false);
                writingButton.setSelected(false);
                photographyButton.setSelected(false);

            }
        });

        // Create Back Button to go back to the previous screen
        JButton backButton = new JButton("← Back");
        backButton.setBounds(3, 3, 80, 45); // set button size and position
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setContentAreaFilled(false);
        // Add the back button to the frame
        signUpFrame.add(backButton);
        backButton.setBackground(Color.decode("#74b4c4")); // set button background color
        // Action listener for the back button to close the window
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signUpFrame.dispose(); // Close the consultation frame
            }
        });
        signUpFrame.setVisible(true);

        // Add the "Sign Up" button to complete the registration
        JButton createAccountButton = new JButton("Sign Up");
        createAccountButton.setBounds(648, 580, 150, 45);
        createAccountButton.setBackground(Color.decode("#74b4c4"));
        signUpFrame.add(createAccountButton);
        signUpFrame.getContentPane().add(backgroundLabel);
        signUpFrame.setVisible(true);

        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

// Retrieve user input
                String firstName = firstNameField.getText().trim();
                String lastName = lastNameField.getText().trim();
                String username = usernameField.getText().trim();
                String email = emailField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();

                String role = "";
                String profession = "";

                // Validate user input
                if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(signUpFrame, "All fields are required!");
                    return;
                }

                if (!email.matches("^(?!.*\\.\\.)[a-zA-Z0-9]+([._+-][a-zA-Z0-9]+)*@gmail\\.com$")) {
                    JOptionPane.showMessageDialog(signUpFrame, "Invalid email, You should use a valid Gmail address (e.g., example@gmail.com)");
                    return;
                }
                if (!(consultativeButton.isSelected() || normalUserButton.isSelected())) {
                    JOptionPane.showMessageDialog(signUpFrame, "Please select a role.");
                    return;
                }

                if (consultativeButton.isSelected()) {
                    role = "Consultative";
                    if (drawingButton.isSelected()) {
                        profession = "Drawing";
                    } else if (musicButton.isSelected()) {
                        profession = "Music";
                    } else if (writingButton.isSelected()) {
                        profession = "Writing";
                    } else if (photographyButton.isSelected()) {
                        profession = "Photography";
                    } else {
                        JOptionPane.showMessageDialog(signUpFrame, "Please select a profession.");
                        return;
                    }
                } else {
                    role = "User";
                }

                // Attempt to register the user
                try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {
                    // Create a connection to the database
                    String recipient = email;  // Recipient email
                    String subject = "Account Created Successfully!";  // Email subject
                    String body = "Hello! Your account has been created successfully";  // Email content

                    // Check email and username availability
                    if (checkEmail(email) == true && checkUsername(username) == true) {
                        // If both email and username are available
                        new EmailSender(recipient, subject, body);  // Send confirmation email

                        PreparedStatement ps;// Declare a PreparedStatement for executing SQL queries
                        // If the user role is "Consultative", insert into the "consultative" table
                        if (role.equalsIgnoreCase("Consultative")) {
                            ps = createPreparedStatement(con,
                                    "INSERT INTO consultative (FirstName, LastName, Username, Email, Role, Password, Profession) VALUES (?, ?, ?, ?, ?, ?, ?)",
                                    firstName, lastName, username, email, role, password, profession);

                            // Otherwise, insert into the "users" table
                        } else {
                            ps = createPreparedStatement(con,
                                    "INSERT INTO users (FirstName, LastName, Username, Email, Password, Role) VALUES (?, ?, ?, ?, ?, ?)",
                                    firstName, lastName, username, email, password, role);

                        }
                        ps.executeUpdate(); // Execute the query and insert the record into the database
                        Session.setUsername(username);// Save the username in the session
                        SignUpSuccess(role); // Call the method to show a success message to the user
                    } else if (checkEmail(email) == false && checkUsername(username) == true) {//call method checkEmail and checkUsername
                        JOptionPane.showMessageDialog(signUpFrame, "Email already exists. Please use another email.");
                    } else if (checkUsername(username) == false && checkEmail(email) == true) {
                        JOptionPane.showMessageDialog(signUpFrame, "Username already exists. Please choose another username.");
                    } else {
                        JOptionPane.showMessageDialog(signUpFrame, "Both email and username are already in use.");
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Welcome.class.getName()).log(Level.SEVERE, "Database error: " + ex.getMessage(), ex);
                    JOptionPane.showMessageDialog(signUpFrame, "An error occurred while processing your request. Please try again.");
                }
                dispose();
            }

            // Utility method for handling successful sign-up
            private void SignUpSuccess(String role) {
                JOptionPane.showMessageDialog(signUpFrame, "Sign-up successful!");
                HomePage home = new HomePage(role);
                Session.setRole(role);
                signUpFrame.dispose();
                home.setVisible(true);

            }
        });

    }

    /*method creates a JLabel and a JTextField, positions them on the frame,
    and returns the created JTextField for further use*/
    private JTextField addTextFieldWithLabel(JFrame frame, String labelText, int labelX, int labelY, int fieldX, int fieldY) {
        JLabel label = new JLabel(labelText);
        label.setBounds(labelX, labelY, 150, 25);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        frame.add(label);

        JTextField textField = new JTextField();
        textField.setBounds(fieldX, fieldY, 250, 35);
        frame.add(textField);

        return textField;
    }

    //method to create radio buttons, allowing for enabling/disabling
    private JRadioButton createRadioButton(JFrame frame, String text, int x, int y, boolean enabled) {
        JRadioButton button = new JRadioButton(text);
        button.setBounds(x, y, 150, 30);
        button.setEnabled(enabled);
        frame.add(button);
        return button;
    }

    // Helper method to create a prepared statement with the provided parameters
    private PreparedStatement createPreparedStatement(Connection con, String query, String... params) throws SQLException {
        PreparedStatement ps = con.prepareStatement(query);
        for (int i = 0; i < params.length; i++) {
            ps.setString(i + 1, params[i]);
        }
        return ps;
    }

    public void openLoginFrame() {
        JFrame logInUpFrame = new JFrame("Log in");
        logInUpFrame.setSize(900, 700);
        logInUpFrame.setResizable(false);
        logInUpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Set up the background image
        ImageIcon loginBackground = new ImageIcon(Welcome.class.getResource("/image/login1.png"));
        JLabel backgroundLabel = new JLabel(loginBackground);
        backgroundLabel.setBounds(0, 0, logInUpFrame.getWidth(), logInUpFrame.getHeight());
        logInUpFrame.setLayout(null);

        // Add user and password fields to the frame
        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(500, 250, 100, 25);
        userLabel.setFont(new Font("Arial", Font.BOLD, 18));
        logInUpFrame.add(userLabel);

        JTextField usernameField = new JTextField();
        usernameField.setBounds(600, 250, 250, 35);
        logInUpFrame.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(500, 300, 100, 25);
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 18));
        logInUpFrame.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(600, 300, 250, 35);
        logInUpFrame.add(passwordField);

        // Create Back Button to go back to the previous screen
        JButton backButton = new JButton("← Back");
        backButton.setBounds(3, 3, 80, 45); // set button size and position
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setContentAreaFilled(false);
        // Add the back button to the frame
        logInUpFrame.add(backButton);
        backButton.setBackground(Color.decode("#74b4c4")); // set button background color
        // Action listener for the back button to close the window
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logInUpFrame.dispose(); // Close the consultation frame
            }
        });
        logInUpFrame.setVisible(true);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(650, 420, 150, 45);
        loginButton.setBackground(Color.decode("#74b4c4"));
        logInUpFrame.add(loginButton);
        logInUpFrame.getContentPane().add(backgroundLabel);
        logInUpFrame.setVisible(true);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

// Retrieve the entered username and password from the text fields
                new ChatServer();
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Check if authentication is successful
                if (authenticateUser(username, password)) {
                    // Get the user's role from the database
                    String User = getDataByPrimaryKey(username);
                    String Consultative = getDataByPrimaryKey2(username);
                    // Set the username in the session
                    Session.setUsername(username);
                    // Direct the user to the appropriate home page based on their role
                    if (Consultative != null && User == null) {
                        HomePage home = new HomePage("Consultative");// Open consultative home page
                        Session.setRole(Consultative); // Set the consultative role in the session
                        home.setVisible(true);
                        logInUpFrame.dispose(); // Close the login frame upon successful login

                        Welcome.this.dispose();
                        new Notification(); // Show a notification

                    } else if (User != null && Consultative == null) {
                        HomePage home = new HomePage("User");// Open user home page
                        Session.setRole(User); // Set the user role in the session
                        home.setVisible(true);
                        logInUpFrame.dispose(); // Close the login frame upon successful 
                        Welcome.this.dispose();
                        new Notification();
                    }
                } else {
                    // Show a login failure message without closing the main frame
                    JOptionPane.showMessageDialog(
                            logInUpFrame,
                            "Log in failed. Username or password may be incorrect.",
                            "Login Error",
                            JOptionPane.ERROR_MESSAGE);
                }
                //logInUpFrame.dispose();
            }
        });
    }

    // Method to authenticate a user by checking their credentials in the database
    public boolean authenticateUser(String username, String password) {
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {
            ResultSet rs;
            Boolean result;

            // Query to check the USERS table for the given username and password
            String query = "SELECT * FROM USERS WHERE Username = ? AND Password = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);

            rs = ps.executeQuery();

            // If a match is found in the USERS table, return true
            if (rs.next()) {

                return true;
            }

            // Query to check the consultative table for the given username and password
            query = "SELECT * FROM consultative WHERE Username = ? AND Password = ?";
            ps = con.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);

            // Return true if a match is found in the consultative table
            rs = ps.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            // Return false if there is a database error
            return false;
        }
    }

    // Method to retrieve the role of a user from the 'users' table based on the username (primary key)
    public static String getDataByPrimaryKey(String primaryKey) {

        // SQL query to select the row where the username matches the provided primary key
        String query = "SELECT * FROM users WHERE username = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement statement = connection.prepareStatement(query)) {

            // Set the value of the primary key in the prepared statement
            statement.setString(1, primaryKey);

            // Execute the query and get the result
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("role");
                return name;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    // Method to retrieve the role of a consultative user from the 'consultative' table based on the username (primary key)
    public static String getDataByPrimaryKey2(String primaryKey) {

        // SQL query to select the row where the username matches the provided primary key in the consultative table
        String query = "SELECT * FROM consultative WHERE username = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement statement = connection.prepareStatement(query)) {

            // Set the value of the primary key in the prepared statement
            statement.setString(1, primaryKey);
            ResultSet resultSet = statement.executeQuery();

            // If the result set contains data, retrieve the user's role
            if (resultSet.next()) {

                // Get the role of the consultative user
                String name = resultSet.getString("role");
                return name;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    //Checks if an email is already registered in either the users or consultative table
    public boolean checkEmail(String email) {
        String query = "SELECT Email FROM users WHERE Email = ? "
                + "UNION "
                + "SELECT Email FROM consultative WHERE Email = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the email parameter for both tables
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, email);
            // Execute the query and check for results
            ResultSet resultSet = preparedStatement.executeQuery();

            return !resultSet.next(); // Return true if no result found, meaning email is available
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false if an error occurs, assuming email is unavailable
        }
    }

    //Checks if a username is already registered in either the users or consultative table
    public boolean checkUsername(String username) {
        String query = "SELECT Username FROM users WHERE Username = ? "
                + "UNION "
                + "SELECT Username FROM consultative WHERE Username = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the username parameter for both tables
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, username);
            // Execute the query and check for results
            ResultSet resultSet = preparedStatement.executeQuery();

            return !resultSet.next();  // Return true if no result found, meaning username is available
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false if an error occurs, assuming username is unavailable
        }
    }
}
