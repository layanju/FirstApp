package pack305;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.*;
import java.net.*;
import javax.swing.*;

/**
 * ChatClient manages the connection and messaging between the client and the
 * server.
 */
public class ChatClient {

    // Connection and messaging variables
    private Socket socket;
    private PrintWriter out; // To send messages
    private BufferedReader in; // To receive messages

    private String serverIP = "127.0.0.1"; // Server IP address
    private int serverPort = 8080; // Server port number
    private String Recipient_fullname; // Recipient's full name
    private String RecipientUser; // Recipient's username
    private String username; // Current user's username
    public static boolean bye = false;

    // GUI components
    private JFrame frame; // Main window
    private JTextArea chatArea; // Display chat messages
    private JTextField messageField; // Input field for messages

    /**
     * Constructor to set up the client with recipient details.
     */
    public ChatClient(String fullname, String RecipientUser) {
        this.Recipient_fullname = fullname;
        this.RecipientUser = RecipientUser;
        setupGUI(); // Set up the interface
    }

    /**
     * Sets up the GUI for the chat client.
     */
   private void setupGUI() {
        // Create the main chat window with recipient's name
        JFrame frame = new JFrame(RecipientUser);

        // Load the background image
        ImageIcon backgroundImage = new ImageIcon(getClass().getResource("/image/bground1.png"));
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setLayout(new BorderLayout());

        // Set the size and layout of the frame
        frame.setSize(550, 500);
        frame.setLayout(new BorderLayout());

        // Chat area for displaying messages
        chatArea = new JTextArea();
        chatArea.setEditable(false); // Read-only chat area
        chatArea.setOpaque(false); // Transparent background
        JScrollPane scrollPane = new JScrollPane(chatArea); // Scroll for long messages
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        backgroundLabel.add(scrollPane, BorderLayout.CENTER);
        frame.add(backgroundLabel, BorderLayout.CENTER);

        // Input area for typing messages and buttons
        JPanel inputPanel = new JPanel(new BorderLayout());
        messageField = new JTextField(); // Field to type messages
        messageField.setBorder(BorderFactory.createTitledBorder("Message")); // Add a border
        inputPanel.add(messageField, BorderLayout.CENTER);

        // Send button
        JButton sendButton = new JButton("SEND");
        sendButton.setBackground(Color.decode("#74b4c4")); // Button color
        sendButton.addActionListener(e -> { // Action for send button
            try {
                sendMessage();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        inputPanel.add(sendButton, BorderLayout.EAST);

        // Back button
        JButton backButton = new JButton("BACK");
        backButton.setBackground(Color.decode("#c47474")); // Button color
        backButton.addActionListener(e -> { // Action for back button
            
            frame.dispose(); 
            try {
                new Messages();
                
// Close the current chat window

            } catch (IOException ex) {
                System.err.println("");
            }
        });
        inputPanel.add(backButton, BorderLayout.WEST); // Add back button to the left

        // Add the input panel to the bottom of the frame
        frame.add(inputPanel, BorderLayout.SOUTH);

        // Show the window
        frame.setVisible(true);
    }
    /**
     * Connects to the server and starts listening for messages.
     */
    public void connect(String username) {
        this.username = username;

        try {
            socket = new Socket(serverIP, serverPort); // Connect to the server
            out = new PrintWriter(socket.getOutputStream(), true); // Output stream
            in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Input stream

            out.println(username); // Send username to the server

            // Thread to listen for incoming messages
            new Thread(() -> {
                try {
                    String message;
                    while ((message = in.readLine()) != null) {
                        if (socket.isClosed()) {
                            break; // Stop if socket is closed
                        }
                        chatArea.append(message + "\n"); // Show message in chat area
                    }
                } catch (IOException e) {
                    if (!socket.isClosed()) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Cannot connect to server.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Sends a message to the server. If the message is "bye", ends the chat.
     */
    private void sendMessage() throws IOException {
        String recipient = RecipientUser; // Get the recipient's username
        String message = messageField.getText(); // Get the typed message
        if (!recipient.isEmpty() && !message.isEmpty()) {
            String formattedMessage = username + " : " + message; // Format the message with the sender's username
            out.println(recipient + ":" + message); // Send the message in the format "recipient:message"
            chatArea.append(formattedMessage + "\n"); // Display the sent message in your chat window
            messageField.setText(""); // Clear the input field after sending

            if (message.equalsIgnoreCase("bye")) { // If the message is "bye", end the chat
                chatArea.append("Chat ended.\n"); // Display "Chat ended" in the chat area
                socket.close(); // Close the connection
                bye = true;
                messageField.setEditable(false); // Disable the input field
                saveChatToFile(); // Save the chat log to a file
            }
        }
    }

    /**
     * Saves the chat history to a file.
     */
    private void saveChatToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("chat_log.txt", true))) {
            writer.write(chatArea.getText()); // Save chat area content
            writer.newLine();
            writer.write("------ End of Chat ------");
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
