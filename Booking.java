package pack305;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Booking {

    private static final String URL = "jdbc:mysql://localhost:3306/project2";
    private static final String USER = "root";
    private static final String PASSWORD = "marya";
    String appointmentDate = "";
    String appointmentTime = "";

    // Constructor to handle booking logic for a consultant
    public Booking(String fullName) throws SQLException {
        // Split full name into first and last name
        String[] nameParts = fullName.trim().split(" ");
        String firstName = nameParts[1];
        String lastName = nameParts[2];

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // Query to retrieve unbooked appointments for the given consultant.
            String query = "SELECT AppointmentDate, AppointmentTime FROM appointments WHERE TRIM(Fname) = ? AND TRIM(Lname) = ? AND Booked = true";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                // Set parameters for the query.
                preparedStatement.setString(1, firstName);
                preparedStatement.setString(2, lastName);
                ResultSet resultSet = preparedStatement.executeQuery();

                ArrayList<String> availableAppointments = new ArrayList<>();

                // Execute the query and process the results.
                while (resultSet.next()) {
                    appointmentDate = resultSet.getString("AppointmentDate");
                    appointmentTime = resultSet.getString("AppointmentTime");

                    // Convert 24-hour time to 12-hour format with AM/PM
                    String formattedTime = convertTo12HourFormat(appointmentTime);

                    availableAppointments.add(appointmentDate + " " + formattedTime);
                }


                // If there are available appointments, display them.
                if (!availableAppointments.isEmpty()) {
                    showAvailableTimesGUI(fullName, availableAppointments);
                } else {
                    JOptionPane.showMessageDialog(null, "No appointments available for the " + fullName);
                }
            }
        }
    }

    private String convertTo12HourFormat(String time24) {
        try {
            // Parse the time in 24-hour format
            SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm:ss"); // Adjust if the time format doesn't include seconds
            SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm a"); // 12-hour format with AM/PM
            java.util.Date date = inputFormat.parse(time24);
            return outputFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return time24; // Return the original time if there's an error
        }
    }

    // Method to show the available appointment times in a GUI
private void showAvailableTimesGUI(String consultantName, ArrayList<String> appointments) {
    JFrame frame = new JFrame("Available appointments for " + consultantName);
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setSize(400, 300);
    frame.setResizable(false);

    // Create a custom panel with a background image
    JPanel backgroundPanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            ImageIcon background = new ImageIcon(getClass().getResource("/image/bground2.png")); // Replace with your image path
            g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
    };
    backgroundPanel.setLayout(new GridLayout(0, 1)); // Use GridLayout to make buttons span the full width of the window

    JLabel titleLabel = new JLabel("Available Appointment Times", SwingConstants.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
    backgroundPanel.add(titleLabel);

    for (String appointment : appointments) {
        JButton appointmentButton = new JButton(appointment);
        appointmentButton.setFont(new Font("Arial", Font.PLAIN, 14)); // Set font size
        appointmentButton.setBackground(new Color(0xE2F2F5)); // Set background color to match advisorButton
        appointmentButton.addActionListener(e -> {
            try {
                bookAppointment(consultantName, appointment);
            } catch (SQLException ex) {
                Logger.getLogger(Booking.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        backgroundPanel.add(appointmentButton);
    }

    frame.setContentPane(backgroundPanel); // Set the background panel as the content pane
    frame.setVisible(true);
}


    private void bookAppointment(String consultantName, String appointment) throws SQLException {
        String[] appointmentDetails = appointment.split(" "); // Split the date and time
        String Date = appointmentDetails[0];// Get the date
        String Time = appointmentDetails[1]; // Get the time

        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

        // Check if the appointment already exists for the user
        String checkQuery = "SELECT * FROM appointments WHERE AppointmentDate = ? AND AppointmentTime = ? AND Beneficiary_Name = ?";
        PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
        checkStatement.setString(1, Date);
        checkStatement.setString(2, Time);
        checkStatement.setString(3, Session.getUsername());

        ResultSet resultSet = checkStatement.executeQuery();

        if (resultSet.next()) {
            // If a matching record is found, the appointment already exists for the user.
            JOptionPane.showMessageDialog(null, "You already have an appointment at this time.");
        } else {
            // Query to update the appointment as booked by setting "Booked" to false 
            // and assigning the user's name to "Beneficiary_Name".
            String updateQuery = "UPDATE appointments SET Booked = false, Beneficiary_Name = ? WHERE Fname = ? AND Lname = ? AND AppointmentDate = ? AND AppointmentTime = ?";
            // Prepare the query with placeholders for dynamic data.
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            // Split the consultant's full name into first and last name for the query.
            String[] nameParts = consultantName.trim().split(" ");
            String firstName = nameParts[1];
            String lastName = nameParts[2];
            
            // Set the values for the placeholders in the query.
            preparedStatement.setString(1, Session.getUsername());
            preparedStatement.setString(2, firstName);
            preparedStatement.setString(3, lastName);
            preparedStatement.setString(4, appointmentDate);
            preparedStatement.setString(5, appointmentTime);
            
            // Execute the update query to book the appointment.
            int rowsAffected = preparedStatement.executeUpdate();

            // Show a success message if the appointment was booked successfully.
            JOptionPane.showMessageDialog(null, "Appointment booked with: " + consultantName + " at " + appointment);

        }
    }
}
