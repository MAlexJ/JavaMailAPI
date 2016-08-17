package com.malex.sendingEmailWithAttachment;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Date;
import java.util.Properties;

/**
 * Send the email with an attached file
 * <p>
 * To send a email with an inline image, the steps followed are:
 * <p>
 * Step #1: Get a Session
 * Step #2: Create a default MimeMessage object and set From, To, Subject in the message.
 * Step #3: Set the actual message as below:
 * <pre>
 *      messageBodyPart.setText("This is message body");
 * </pre>
 * Step #4: Create a MimeMultipart object. Add the above messageBodyPart with actual message set in it, to this multipart object.
 * Step #5: Next add the attachment by creating a Datahandler as follows:
 * <pre>
 *      messageBodyPart = new MimeBodyPart();
 *      String filename = "/home/manisha/file.txt";
 *      DataSource source = new FileDataSource(filename);
 *      messageBodyPart.setDataHandler(new DataHandler(source));
 *      messageBodyPart.setFileName(filename);
 *      multipart.addBodyPart(messageBodyPart);
 * </pre>
 * Step #6: Next set the multipart in the message as follows:
 * <pre>
 *     message.setContent(multipart);
 * </pre>
 * Step #7: Send the message using the Transport object.
 * <p>
 * http://www.tutorialspoint.com/javamail_api/javamail_api_send_email_with_attachment.htm
 */
public class SendAttachmentInEmail {

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
        String subjectMessage = new Date() + " Log files of the application IPOS.";
        String fileName = "log/iPOS_0_0.log";

        send(sender, userEmail, passwordEmail, recipient, subjectMessage, fileName);

    }

    static void send(String sender, final String userEmail, final String passwordEmail, String recipient, String subjectMessage, String pathFile) {
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

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Now set the actual message
            messageBodyPart.setText("The email contains the following files: \n "
                    + pathFile + "\n "
                    + "Date: "
                    + new Date() + "\n \n"
                    + "Author: Malex");

            // Create a multipart message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Part two is attachment
            messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(pathFile);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(pathFile);
            multipart.addBodyPart(messageBodyPart);

            // Send the complete message parts
            message.setContent(multipart);

            // Send message
            Transport.send(message);

            System.out.println("Sent message successfully....");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
