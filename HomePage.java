package pack305;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import pack305.AdvisorsGUI;

import pack305.ScheduleManager;

// class for the home page
/**
 *
 * @author lyana
 */
public class HomePage extends JFrame {

    Font newFont = new Font("Arial", Font.BOLD, 25);

    public HomePage(String role) {

        // create the profile panel
        JPanel profilePanel = new JPanel();
        profilePanel.setBackground(Color.decode("#74b4c4"));
        profilePanel.setBounds(12, 10, 870, 150);
        profilePanel.setLayout(new BorderLayout());

        // create the main window for the home page
        setTitle("home");
        setSize(900, 700);  // set the window size
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // set close operation for the exit button
        setLayout(null);

        // add background image
        ImageIcon backgroundImage = new ImageIcon(HomePage.class.getResource("/image/bground2.png"));
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setBounds(0, -40, getWidth(), getHeight());
        if (role.equalsIgnoreCase("User")) {
            // home page buttons
            JButton consultationButton = new JButton("Consultation");

            //invoke consultation page when entering button
            consultationButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    AdvisorsGUI consultationPage = new AdvisorsGUI();

                    dispose(); // close the home page when opening the messages window
                }
            });

            // display username
            JLabel username = new JLabel(Session.getFname() + Session.getLname(), SwingConstants.LEFT);
            username.setBounds(200, 50, 100, 45);
            username.setFont(new Font("Arial", Font.BOLD, 24));
            profilePanel.add(username);

            // set background color for the consultation button using a custom color code
            consultationButton.setBackground(Color.decode("#74b4c4"));
            // apply the custom font to the consultation button
            consultationButton.setFont(newFont);

            // set the position and size of the consultation button (x y width height)
            consultationButton.setBounds(170, 300, 250, 120);
            // add a black border around the consultation button with a thickness of 2 pixels
            consultationButton.setBorder(BorderFactory.createLineBorder(Color.black, 2));

            // add the consultation button to the main window
            add(consultationButton);

        } else {
            // home page buttons
            JButton Scheduling = new JButton("Scheduling");
            JButton msgButton = new JButton("Messages");

            //invoke consultation page when entering button
            Scheduling.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ScheduleManager manager = new ScheduleManager();
                    manager.setVisible(true);

                }
            });
            // set background color for the consultation button using a custom color code
            Scheduling.setBackground(Color.decode("#74b4c4"));

            // apply the custom font to the consultation button
            Scheduling.setFont(newFont);

            // set the position and size of the consultation button (x y width height)
            Scheduling.setBounds(170, 300, 250, 120);

            // add a black border around the consultation button with a thickness of 2 pixels
            Scheduling.setBorder(BorderFactory.createLineBorder(Color.black, 2));

            // add the consultation button to the main window
            add(Scheduling);

            // display username
            JLabel username = new JLabel(Session.getFname() + " " + Session.getLname(), SwingConstants.LEFT);
            username.setBounds(200, 50, 100, 45);
            username.setFont(new Font("Arial", Font.BOLD, 24));
            profilePanel.add(username);

        }

        JButton msgButton = new JButton("Messages");
        msgButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    
                    new Messages();
                } catch (IOException ex) {
                    Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, null, ex);
                }
                dispose(); // close the home page when opening the messages window
            }
        });

        // placeholder for profile image
        ImageIcon originalIcon = new ImageIcon(HomePage.class.getResource("/image/profile.png"));
        Image scaledImage = originalIcon.getImage().getScaledInstance(150, 130, Image.SCALE_SMOOTH);
        JLabel img = new JLabel(new ImageIcon(scaledImage));
        img.setBounds(10, 10, 150, 130);
        profilePanel.add(img, BorderLayout.WEST);

        // add the profile panel to the main window
        add(profilePanel);

        // apply the same custom font to the messages button
        msgButton.setFont(newFont);

        // set background color for the messages button using the same custom color code
        msgButton.setBackground(Color.decode("#74b4c4"));

        // set the position and size of the messages button (x y width height)
        msgButton.setBounds(470, 300, 250, 120);

        // add a black border around the messages button with a thickness of 2 pixels
        msgButton.setBorder(BorderFactory.createLineBorder(Color.black, 2));

        // add the messages button to the main window
        add(msgButton);
        // add the background image label to the main window to serve as the background
        add(backgroundLabel);

    }

}
