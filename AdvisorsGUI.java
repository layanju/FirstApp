package pack305;


import javax.swing.*;                     
import java.awt.*;                        
import java.awt.event.ActionEvent;        
import java.awt.event.ActionListener;     
import java.awt.image.BufferedImage;      
import java.io.IOException;               
import java.sql.*;                        
import java.util.logging.Level;           
import java.util.logging.Logger;          
import javax.imageio.ImageIO;             

/**
 * AdvisorsGUI is a graphical user interface (GUI) application for consulting services.
 * Users can select different talents (Drawing, Music, Writing, Photography)
 * and view a list of consultants available for each talent.
 */
public class AdvisorsGUI {

    // Main application window (frame)
    private JFrame frame;

    // Database connection details
    private static final String URL = "jdbc:mysql://localhost:3306/project2";  // Database URL
    private static final String USER = "root";                                 // Database username
    private static final String PASSWORD = "layanasdf";                             // Database password

    // GUI customization
    Color buttonColor = new Color(116, 180, 196);   // Color for buttons
    BufferedImage backgroundImage;                 // Background image for the GUI

    /**
     * Constructor initializes the AdvisorsGUI by setting up the main GUI window and loading resources.
     */
    public AdvisorsGUI() {
        try {
            // Load the background image for the application
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/image/bground1.png"));
        } catch (IOException e) {
            // Handle errors if the image cannot be loaded
            System.out.println("Error loading background image: " + e.getMessage());
        }
        // Create the main GUI
        createMainGUI();
    }

    /**
     * Creates a panel with a custom background image.
     *
     * @param backgroundImage The background image to use for the panel
     * @return JPanel with the specified background
     */
    private JPanel createBackgroundPanel(BufferedImage backgroundImage) {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    // Draw the background image to fit the panel's size
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
    }

    /**
     * Sets up the main GUI window, including talent selection buttons and navigation options.
     */
    private void createMainGUI() {
        // Initialize the main application window
        frame = new JFrame("Consultment App");
        frame.setSize(900, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the main background panel
        JPanel backgroundPanel = createBackgroundPanel(backgroundImage);
        backgroundPanel.setLayout(null);  // Use absolute layout for precise positioning
        frame.setContentPane(backgroundPanel);

        // Add a title label
        JLabel label = new JLabel("Choose a talent");
        label.setBounds(150, 50, 200, 70);                 // Position and size
        label.setFont(new Font("Serif", Font.BOLD, 30));   // Font styling
        backgroundPanel.add(label);

        // Create buttons for different talents
        createTalentButton(backgroundPanel, "Drawing", "/image/draw.png", 180, 150, "Click to view drawing consultants.");
        createTalentButton(backgroundPanel, "Music", "/image/music.png", 500, 150, "Click to view music consultants.");
        createTalentButton(backgroundPanel, "Writing", "/image/writing.png", 180, 300, "Click to view writing consultants.");
        createTalentButton(backgroundPanel, "Photography", "/image/pic.png", 500, 300, "Click to view photography consultants.");

        // Create a back button for navigation
        JButton backButton = new JButton("← Back");
        backButton.setBounds(10, 10, 80, 45);                 // Position and size
        backButton.setBorderPainted(false);                  // Hide border
        backButton.setFocusPainted(false);                   // Disable focus rectangle
        backButton.setContentAreaFilled(false);              // Make background transparent
        backgroundPanel.add(backButton);

        // Add action listener for the back button
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Navigate to the home page when back button is clicked
                HomePage home = new HomePage("User");
                home.setVisible(true);
                frame.dispose();
            }
        });

        // Make the window visible
        frame.setVisible(true);
    }

    /**
     * Creates a button for a specific talent.
     */
    private void createTalentButton(JPanel panel, String text, String imagePath, int x, int y, String tooltip) {
        JButton button = new JButton(text);
        button.setBounds(x, y, 250, 100);                        // Position and size
        button.setFont(new Font("Arial", Font.PLAIN, 24));       // Font styling
        button.setBackground(buttonColor);                      // Background color
        button.setToolTipText(tooltip);                         // Tooltip

        try {
            // Load and set the button's icon
            BufferedImage image = ImageIO.read(getClass().getResourceAsStream(imagePath));
            if (image != null) {
                Image scaledImage = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH); // Resize image
                ImageIcon icon = new ImageIcon(scaledImage);
                button.setIcon(icon);
                button.setVerticalTextPosition(SwingConstants.BOTTOM);  // Text position
                button.setHorizontalTextPosition(SwingConstants.CENTER);
            }
        } catch (IOException e) {
            // Handle errors if the image cannot be loaded
            System.out.println("Error loading " + text + " image: " + e.getMessage());
        }

        // Add action listener for the button
        button.addActionListener((ActionEvent e) -> {
            // Open the consultants list for the selected talent
            createConsultationGUI(text);
        });

        // Add the button to the panel
        panel.add(button);
    }

    /**
     * Displays a window with a list of consultants for the selected talent.
     *
     * @param profession The selected talent (e.g., "Drawing", "Music")
     */
    public void createConsultationGUI(String profession) {
        try {
            // Initialize a new window for consultants
            JFrame frame = new JFrame("List of Consultants - " + profession);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Create background panel
            JPanel backgroundPanel = createBackgroundPanel(backgroundImage);
            backgroundPanel.setLayout(null); 
            frame.setContentPane(backgroundPanel);

            // Panel to display consultants
            JPanel advisorsPanel = new JPanel();
            advisorsPanel.setLayout(new BoxLayout(advisorsPanel, BoxLayout.Y_AXIS));  // Vertical layout
            advisorsPanel.setOpaque(false);  // Transparent panel

            // Load consultants from the database
            img(advisorsPanel, profession); 

            // Add a scroll pane for the consultants list
            JScrollPane scrollPane = new JScrollPane(advisorsPanel);
            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(false);
            scrollPane.setBounds(0, 50, 900, 600); 
            backgroundPanel.add(scrollPane);

            // Create a back button
            JButton backButton = new JButton("← Back");
            backButton.setBounds(10, 10, 80, 45);                 // Position and size
            backButton.setBorderPainted(false);
            backButton.setFocusPainted(false);
            backButton.setContentAreaFilled(false);
            backgroundPanel.add(backButton);

            // Add action listener for the back button
            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Close the current window
                    frame.dispose();
                }
            });

            frame.setPreferredSize(new Dimension(900, 700));
            frame.pack();
            frame.setVisible(true);

        } catch (SQLException ex) {
            // Log SQL exceptions
            Logger.getLogger(AdvisorsGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Loads consultants from the database and adds them to the panel.
     *
     * @param advisorsPanel The panel to which the consultants will be added
     * @param profession    The selected talent (e.g., "Drawing")
     * @throws SQLException If a database error occurs
     */
    public void img(JPanel advisorsPanel, String profession) throws SQLException {
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pst = con.prepareStatement(
                     "SELECT DISTINCT FirstName, LastName FROM Consultative WHERE Profession = ?")) {

            pst.setString(1, profession);  // Set the profession parameter

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                // Retrieve consultant's full name
                String fullName = rs.getString("FirstName") + " " + rs.getString("LastName");
                addAdvisorButton(advisorsPanel, "Counselor: " + fullName);  // Add a button for each consultant
            }
        } catch (SQLException ex) {
            // Log SQL exceptions
            Logger.getLogger(AdvisorsGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Adds a button for each consultant to the panel.
     *
     * @param panel    The panel to which the button will be added
     * @param fullName The consultant's full name
     */
    private void addAdvisorButton(JPanel panel, String fullName) {
        JButton advisorButton = new JButton();
        advisorButton.setPreferredSize(new Dimension(400, 80));   // Button size
        advisorButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));  // Allow resizing
        advisorButton.setBackground(new Color(0xE2F2F5));         // Background color
        advisorButton.setHorizontalAlignment(SwingConstants.CENTER);  // Center text
        advisorButton.setText(fullName);                         // Set button text

        // Add action listener for the consultant button
        advisorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Open the booking window for the selected consultant
                    Booking booking = new Booking(fullName);
                } catch (SQLException ex) {
                    // Log SQL exceptions
                    Logger.getLogger(AdvisorsGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        // Add the button to the panel
        panel.add(advisorButton);
    }
}
