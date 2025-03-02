
package pack305;

import com.toedter.calendar.JCalendar;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class ScheduleManager extends JFrame {

    private JCalendar dateChooser;
    private JComboBox<String> timeComboBox;
    private JButton addButton;
    private DefaultListModel<String> availableTimesModel;
    private JList<String> scheduleList;
    private boolean booking;
    private static final String URL = "jdbc:mysql://localhost:3306/project2";
    private static final String USER = "root";
    private static final String PASSWORD = "layanasdf";
    private BufferedImage backgroundImage;

    // Constructor
    public ScheduleManager(boolean booking) {
        this.booking = booking;
    }

    public ScheduleManager() {
        try {
            // Load the background image
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/image/bground1.png"));
        } catch (IOException e) {
            System.out.println("Error loading background image: " + e.getMessage());
        }

        availableTimesModel = new DefaultListModel<>();
        scheduleList = new JList<>(availableTimesModel); // Initialize the schedule list

        // Set up the frame
        setTitle("Schedule Manager");
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Create a custom JPanel with the background
        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Initialize components
        JLabel dateLabel = new JLabel("Select Date:");
        dateChooser = new JCalendar();
        dateChooser.getDayChooser().setMinSelectableDate(new Date());

        JLabel timeLabel = new JLabel("Select Time:");
        timeComboBox = new JComboBox<>(new String[]{"8:00 am", "8:30 am", "9:00 am", "9:30 am", "10:00 am", "10:10 am","10:30 am","10:20 am", "11:00 am","11:30 am","12:00 pm","8:55 pm", "11:10 pm","11:33 pm"});

        addButton = new JButton("Add Time");        
        addButton.setBackground(Color.decode("#74b4c4")); // Set background color
        addButton.setForeground(Color.BLACK);
        

        // Add components to layout
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(dateLabel, gbc);
        gbc.gridx = 1;
        panel.add(dateChooser, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(timeLabel, gbc);
        gbc.gridx = 1;
        panel.add(timeComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(addButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;

        // Add panel to frame
        setContentPane(panel);

        // Add action listener to the button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    addTime();
                } catch (SQLException | ParseException ex) {
                    Logger.getLogger(ScheduleManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    private void addTime() throws SQLException, ParseException {
        String username = Session.getUsername(); // Assuming Session is implemented elsewhere
        Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
        PreparedStatement ps = null;
        ResultSet rs = null;

        Date dateFromBox = dateChooser.getDate();
        String dateString = new SimpleDateFormat("yyyy-MM-dd").format(dateFromBox);
        String timeString = (String) timeComboBox.getSelectedItem();

        LocalDate date = LocalDate.parse(dateString);

        SimpleDateFormat originalFormat = new SimpleDateFormat("h:mm a");
        SimpleDateFormat targetFormat = new SimpleDateFormat("HH:mm:ss");
        timeString = targetFormat.format(originalFormat.parse(timeString));

        LocalTime time = LocalTime.parse(timeString);

        // Query to check if the appointment exists with the same date and time for the current user
        String query = "SELECT * FROM appointments WHERE AppointmentDate = ? AND AppointmentTime = ? AND username = ?";
        ps = con.prepareStatement(query);
        ps.setString(1, dateString);
        ps.setString(2, timeString);
        ps.setString(3, username);

        rs = ps.executeQuery();
        String AppointmentTime = "";
        String AppointmentDate = "";

        if ((LocalDate.now().isBefore(date)) || (LocalDate.now().isEqual(date) && LocalTime.now().isBefore(time))) {

            if (rs.next()) {
                // If a record is found, the appointment already exists
                JOptionPane.showMessageDialog(this, "You already have an appointment at this time.");
            } else {

                try {
                    query = "SELECT FirstName, LastName FROM consultative WHERE Username = ?";
                    ps = con.prepareStatement(query);
                    ps.setString(1, username);
                    rs = ps.executeQuery();

                    String firstName = null;
                    String lastName = null;
                    if (rs.next()) {
                        firstName = rs.getString("FirstName");
                        lastName = rs.getString("LastName");
                    }

                    String sql = "INSERT INTO appointments (Username, Fname, Lname, AppointmentDate, AppointmentTime, Booked) VALUES (?,?,?,?,?,?)";
                    ps = con.prepareStatement(sql);
                    ps.setString(1, username);
                    ps.setString(2, firstName);
                    ps.setString(3, lastName);
                    ps.setString(4, dateString);
                    ps.setString(5, timeString);
                    ps.setBoolean(6, true);

                    ps.executeUpdate();
                    availableTimesModel.addElement("Date: " + date + ", Time: " + time); // Add to the list
                    JOptionPane.showMessageDialog(this, "Appointment added successfully.");
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    if (rs != null) {
                        rs.close();
                    }
                    if (ps != null) {
                        ps.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                }
            }

        } else {
            JOptionPane.showMessageDialog(this, "You cannot book an appointment with an old date or time.");
        }
        
    }
}
