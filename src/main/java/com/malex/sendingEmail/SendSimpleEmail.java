package com.malex.sendingEmail;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Send a simple email.
 * <p>
 * To send a simple email steps followed are:
 * STEP #1: Get a Session
 * STEP #2: Create a default MimeMessage object and set From, To, Subject in the message.
 * STEP #3:  Set the actual message as:
 * message.setText("your text goes here");
 * STEP #4: Send the message using the Transport object.
 * <p>
 * http://www.tutorialspoint.com/javamail_api/javamail_api_sending_simple_email.htm
 */
public class SendSimpleEmail {

    // Assuming you are sending email through relay.jangosmtp.net
    private static final String HOST_EMAIL = "smtp.gmail.com";
    private static final String SMTP_AUTH = "true";
    private static final String SMTP_PORT = "587";
    private static final String START_TLS = "true";


    public static void main(String[] args) {

        // Sender's email ID needs RECIPIENT be mentioned
        String sender = "iposcashregister@gmail.com";
        String userEmail = "iposcashregister";
        String passwordEmail = "0672687484a";

        // Recipient's email ID needs RECIPIENT be mentioned.
        String recipient = "iposcashregister@gmail.com";
        String subjectMessage = "New Message";
        String textMessage = "Text message!!!!";

        send(sender, userEmail, passwordEmail, recipient, subjectMessage, textMessage);
    }

    static void send(String sender, final String userEmail, final String passwordEmail, String recipient, String subjectMessage, String textMessage) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", SMTP_AUTH);
        props.put("mail.smtp.starttls.enable", START_TLS);
        props.put("mail.smtp.host", HOST_EMAIL);
        props.put("mail.smtp.port", SMTP_PORT);

        // Get the Session object.
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(userEmail, passwordEmail);
                    }
                });

        try {
            // Create a default MimeMessage object.
            Message message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(sender));

            // Set To: header field of the header.
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));

            // Set Subject: header field
            message.setSubject(subjectMessage);

            // Now set the actual message
            message.setText(textMessage);

            // Send message
            Transport.send(message);

            System.out.println("Sent message successfully....");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
