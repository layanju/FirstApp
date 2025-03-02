package pack305;

import javax.mail.Session;                // Manages a mail session
import java.util.Properties;             // Used for storing email server properties
import javax.mail.*;                      // Contains classes for mail messaging
import javax.mail.internet.*;            // Handles email creation and parsing

/**
 * This class provides functionality to send an email using SMTP protocol. The
 * email is sent from a predefined sender account to a specified recipient.
 */
public class EmailSender {

    /**
     * Constructor that initializes and sends an email.
     *
     * @param recipientEmail The recipient's email address
     * @param subject The subject of the email
     * @param messageBody The body content of the email
     */
    public EmailSender(String recipientEmail, String subject, String messageBody) {
        // Define the SMTP server settings
        String host = "smtp.gmail.com";          // SMTP server address (Gmail)
        String port = "587";                    // Port for SMTP with TLS/STARTTLS
        String senderEmail = "creativeconsultingapp@gmail.com"; // Sender's email address
        String senderPassword = "aups rzql ynay blol";          // Sender's email password

        // Define the email server properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");                 // Enable authentication
        props.put("mail.smtp.starttls.enable", "true");      // Enable STARTTLS encryption
        props.put("mail.smtp.host", host);                   // Set the SMTP host
        props.put("mail.smtp.port", port);                   // Set the SMTP port
        props.put("mail.smtp.ssl.trust", host);              // Trust the server's SSL certificate

        // Create a session object with authentication
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // Authenticate with the sender's email and password
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // Create a new email message
            Message message = new MimeMessage(session);

            // Set the sender's email address
            message.setFrom(new InternetAddress(senderEmail));

            // Set the recipient's email address (multiple recipients can be added with a comma-separated list)
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));

            // Set the email subject
            message.setSubject(subject);

            // Set the email body text
            message.setText(messageBody);

            // Send the email using the Transport class
            Transport.send(message);

        } catch (MessagingException e) {
            // Handle exceptions that occur during the email sending process
            e.printStackTrace();
        }
    }

}
