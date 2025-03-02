package pack305;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.*;

public class Notification {

    // Database connection constants
    private static final String DB_URL = "jdbc:mysql://localhost:3306/project2";
    private static final String USER = "root";
    private static final String PASSWORD = "layanasdf";

    public Notification() {
        // Create a new thread for repeating notifications
        Thread notificationThread = new Thread(() -> {
            while (true) {
                try {
                    // Show notification
                    showAppointmentNotification();
                    // Delay for 10 seconds
                    Thread.sleep(10000); // 10000 milliseconds = 10 seconds
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // Start the thread
        notificationThread.start();
    }

    // Method to display notification after fetching the nearest appointment from the database
    private static void showAppointmentNotification() {
        LocalTime nearestTime = null;
        LocalDate nearestDate = null;

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String query = "SELECT AppointmentTime, AppointmentDate FROM appointments WHERE Beneficiary_Name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, Session.getUsername()); // Make sure Session.getUsername() returns the correct value

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                // Get appointment time and date from the database
                String timeStr = resultSet.getString("AppointmentTime");
                LocalTime appointmentTime = LocalTime.parse(timeStr);
                String appointmentDateStr = resultSet.getString("AppointmentDate");
                LocalDate appointmentDate = LocalDate.parse(appointmentDateStr);

                // Check if the appointment is in the future
                if (appointmentDate.isAfter(LocalDate.now()) || (appointmentDate.equals(LocalDate.now()) && appointmentTime.isAfter(LocalTime.now()))) {
                    // Check if this is the nearest appointment
                    if (nearestTime == null  || appointmentDate.isBefore(nearestDate) || (appointmentDate.equals(nearestDate) && appointmentTime.isBefore(nearestTime))) {
                        nearestTime = appointmentTime;
                        nearestDate = appointmentDate;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Calculate the time remaining until the nearest appointment
        if (nearestDate != null && nearestTime != null) {
            Duration duration = Duration.between(LocalTime.now(), nearestTime);
            long hoursLeft = duration.toHours();
            long minutesLeft = duration.toMinutes() % 60;

            // Display notification
            showNotification(nearestTime.toString(), nearestDate, hoursLeft, minutesLeft);
        }
    }

    // Method to display the notification as a TrayIcon with the remaining time
    private static void showNotification(String time, LocalDate date, long hoursLeft, long minutesLeft) {
        if (SystemTray.isSupported()) {
            SystemTray systemTray = SystemTray.getSystemTray();
            TrayIcon trayIcon = null;
            try {
                Image image = Toolkit.getDefaultToolkit().getImage("path/to/image.png");
                trayIcon = new TrayIcon(image, "Appointment Reminder");
                trayIcon.setImageAutoSize(true);
                systemTray.add(trayIcon);

                trayIcon.addActionListener(e -> JOptionPane.showMessageDialog(null,
                        "Reminder! You have an appointment today at " + time + " on " + date));

                trayIcon.displayMessage("Appointment Reminder",
                        "You have an appointment today at " + time + " on " + date
                        + ". Time left: " + hoursLeft + " hours and " + minutesLeft + " minutes.", TrayIcon.MessageType.INFO);

            } catch (AWTException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("SystemTray is not supported.");
        }
    }
}
