package pack305;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import static pack305.ChatClient.bye;

/**
 * This class represents a messaging interface as a JFrame window. It allows
 * users to view and interact with messages based on their role.
 *
 * @author lyana
 */
public class Messages extends JFrame {

    // Define constants for server connection and database credentials
    private static final String SERVER_IP = "127.0.0.1"; // Server IP address
    private static final int SERVER_PORT = 8080; // Server port
    private PrintWriter output; // Output stream for server communication
    private BufferedReader input; // Input stream for server communication

    // Database configuration
    private static final String DB_URL = "jdbc:mysql://localhost:3306/project2"; // Database URL
    private static final String USER = "root"; // Database username
    private static final String PASSWORD = "layanasdf"; // Database password

    // Constructor to initialize the messaging interface
    public Messages() throws IOException {

        // Set up the main window
        setTitle("Messaging Window"); // Window title
        setSize(300, 500); // Window size
        setResizable(false); // Disable resizing
        setLayout(null); // Use absolute positioning

        // Fonts for UI elements
        Font newFont = new Font("Arial", Font.BOLD, 16); // Font for buttons
        Font newFont2 = new Font("Arial", Font.BOLD, 12); // Font for labels

        // Background image setup
        ImageIcon backgroundImage = new ImageIcon(Messages.class.getResource("/image/bground1.png")); // Load background image
        JLabel backgroundLabel = new JLabel(backgroundImage); // Label to hold background image
        backgroundLabel.setBounds(0, -40, getWidth(), getHeight()); // Set position and size

        // Title bar for the window
        JLabel bar = new JLabel("Messages");
        bar.setFont(new Font("Arial", Font.BOLD, 25)); // Set font style and size
        bar.setBounds(87, 15, 297, 90); // Position and size

        // Panel for UI elements
        JPanel panel = new JPanel();
        panel.setBackground(Color.decode("#74b4c4")); // Background color
        panel.setLayout(null); // Use absolute positioning
        panel.setBounds(0, 0, 298, 661); // Position and size

        // Back button for navigation
        JButton backButton = new JButton("← Back");
        backButton.setBounds(3, 3, 80, 45); // Position and size
        backButton.setBorderPainted(false); // Remove border
        backButton.setFocusPainted(false); // Remove focus outline
        backButton.setContentAreaFilled(false); // Transparent background

        // Add components to the frame
        add(backButton); // Add back button
        backButton.setBackground(Color.decode("#74b4c4")); // Set button color
        add(bar); // Add title bar
        add(panel); // Add main panel

        // Add message panel based on user role
        if (Session.getRole().equalsIgnoreCase("User")) {
            panel.add(createMessagePanel(newFont, true)); // Add panel for users
        } else {
            panel.add(createMessagePanel(newFont, false)); // Add panel for consultatives
        }
        add(backgroundLabel); // Add background image

        setVisible(true); // Make the frame visible

        // Back button action listener
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Session.getRole().equalsIgnoreCase("Consultative")) {
                    HomePage home = new HomePage("Consultative"); // Navigate to consultative home
                    home.setVisible(true); // Show home page
                    dispose(); // Close current window
                } else {
                    HomePage home = new HomePage("User"); // Navigate to user home
                    home.setVisible(true); // Show home page
                    dispose(); // Close current window
                }
            }
        });
    }

    // Method to create a scrollable message panel
    private JScrollPane createMessagePanel(Font newFont, boolean isForUsers) throws IOException {
        JPanel userList = new JPanel(); // Panel to hold message buttons
        userList.setLayout(new BoxLayout(userList, BoxLayout.Y_AXIS)); // Set vertical layout
        userList.setBounds(0, 2, 297, 540); // Position and size

        // Database connection and message retrieval
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            if (isForUsers) {
                handleUserMessages(connection, userList, newFont); // Load user messages
            } else {
                handleConsMessages(connection, userList, newFont); // Load consultative messages
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Print SQL exception
        }

        // Add scrollbar to the message panel
        JScrollPane listScrollPane = new JScrollPane(userList); // Add scrollable view
        listScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS); // Enable vertical scroll
        listScrollPane.setBounds(0, 120, 297, 540); // Position and size
        return listScrollPane; // Return the scrollable panel
    }

    // Method to handle messages for users
    private void handleUserMessages(Connection connection, JPanel userList, Font newFont) throws SQLException {
        String query = "SELECT Fname, Lname, Username, AppointmentDate, AppointmentTime FROM appointments WHERE Beneficiary_Name = ?"; // SQL query
        PreparedStatement preparedStatement = connection.prepareStatement(query); // Prepare statement
        preparedStatement.setString(1, Session.getUsername()); // Bind parameter

        ResultSet resultSet = preparedStatement.executeQuery(); // Execute query
        LocalDate currentDate = LocalDate.now(); // Get current date

        while (resultSet.next()) {
            String fullname = resultSet.getString("Fname") + " " + resultSet.getString("Lname"); // Get full name
            String username = resultSet.getString("Username"); // Get username
            LocalDate appointmentDate = LocalDate.parse(resultSet.getString("AppointmentDate")); // Get appointment date
            LocalTime appointmentTime = LocalTime.parse(resultSet.getString("AppointmentTime"));
            JButton userButton = createUserButton(fullname, appointmentDate, currentDate,appointmentTime,username, newFont); // Create button
            userList.add(userButton); // Add button to panel
            userList.add(Box.createVerticalStrut(1)); // Add spacing
        }
    }

    // Method to handle messages for consultatives
    private void handleConsMessages(Connection connection, JPanel userList, Font newFont) throws SQLException {
        String query = "SELECT Beneficiary_Name, AppointmentDate, AppointmentTime FROM appointments WHERE Username = ?"; // SQL query
        String secondaryQuery = "SELECT FirstName, LastName FROM users WHERE Username = ?"; // Secondary query
        PreparedStatement preparedStatement = connection.prepareStatement(query); // Prepare statement
        preparedStatement.setString(1, Session.getUsername()); // Bind parameter

        ResultSet resultSet = preparedStatement.executeQuery(); // Execute query
        LocalDate currentDate = LocalDate.now(); // Get current date

        while (resultSet.next()) {
            String beneficiaryName = resultSet.getString("Beneficiary_Name"); // Get beneficiary name
            LocalDate appointmentDate = LocalDate.parse(resultSet.getString("AppointmentDate")); // Get appointment date
            LocalTime appointmentTime = LocalTime.parse(resultSet.getString("AppointmentTime")); // Get appointment date
            PreparedStatement secondaryStatement = connection.prepareStatement(secondaryQuery); // Prepare secondary statement
            secondaryStatement.setString(1, beneficiaryName); // Bind parameter
            ResultSet secondaryResultSet = secondaryStatement.executeQuery(); // Execute secondary query

            if (secondaryResultSet.next()) {
                String fullname = secondaryResultSet.getString("FirstName") + " " + secondaryResultSet.getString("LastName"); // Get full name
                JButton userButton = createUserButton(fullname, appointmentDate, currentDate, appointmentTime, beneficiaryName, newFont); // Create button
                userList.add(userButton); // Add button to panel
                userList.add(Box.createVerticalStrut(1)); // Add spacing
            }
        }
    }

    // Method to create a button for each message entry
    private JButton createUserButton(String fullname, LocalDate appointmentDate, LocalDate currentDate, LocalTime appointmentTime, String identifier, Font newFont) {
        JButton userButton = new JButton(fullname); // Create button with full name

        // Retrieve the current time
        LocalTime currentTime = LocalTime.now(); // Current time

        // Check if appointment date and time are in the future
        if (appointmentDate.isAfter(currentDate)
                || (appointmentDate.isEqual(currentDate) && appointmentTime.isAfter(currentTime))) {
            userButton.setText(fullname + " - Waiting "+ appointmentTime+" "+appointmentDate); // Mark as waiting
        } else if (appointmentDate.isEqual(currentDate)
                && appointmentTime.plusMinutes(30).isBefore(currentTime) || bye==true) {
            userButton.setText(fullname + " - Ended "+ appointmentTime+" "+appointmentDate); // Mark as ended
        } else {
            userButton.addActionListener(e -> {
                dispose();
                ChatClient client = new ChatClient(fullname, identifier); // Create chat client
                client.connect(Session.getUsername()); // Connect client to server
            });
        }

        // Set button properties
        userButton.setFont(newFont); // Set font
        userButton.setBackground(Color.decode("#E2F2F5")); // Set background color
        userButton.setBorder(BorderFactory.createLineBorder(Color.black)); // Add border
        userButton.setHorizontalAlignment(SwingConstants.LEFT); // Align text to the left
        userButton.setMaximumSize(new Dimension(280, 30)); // Set maximum size
        userButton.setFont(new Font("Arial", Font.PLAIN, 14)); // Set font style and size

        return userButton; // Return the button
    }
    

}
