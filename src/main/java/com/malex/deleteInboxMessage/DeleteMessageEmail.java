package com.malex.deleteInboxMessage;

import javax.mail.*;
import java.util.Properties;

public class DeleteMessageEmail {

    // Email settings
    private static final String HOST_EMAIL = "smtp.gmail.com";
    private static final String SMTP_PORT = "995"; //or 587
    private static final String START_TLS = "true";
    private static final String PROTOCOL = "pop3";

    public static void main(String[] args) {
        // User settings
        String senderEmail = "iposcashregister@gmail.com";
        String passwordEmail = "0672687484a";

        delete(senderEmail, passwordEmail);
    }

    private static void delete(String senderEmail, String passwordEmail) {
        try {
            // get the session object
            Properties properties = new Properties();
            properties.put("mail.store.protocol", PROTOCOL);
            properties.put("mail.pop3s.host", HOST_EMAIL);
            properties.put("mail.pop3s.port", SMTP_PORT);
            properties.put("mail.pop3.starttls.enable", START_TLS);
            Session emailSession = Session.getDefaultInstance(properties);
            // emailSession.setDebug(true);

            // create the POP3 store object and connect with the pop server
            Store store = emailSession.getStore("pop3s");

            store.connect(HOST_EMAIL, senderEmail, passwordEmail);

            // create the folder object and open it
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_WRITE);

            // retrieve the messages from the folder in an array and print it
            Message[] messages = emailFolder.getMessages();
            System.out.println("messages.length---" + messages.length);
            for (int i = 0; i < messages.length; i++) {
                Message message = messages[i];
                System.out.println("---------------------------------");
                System.out.println("Email Number " + (i + 1));
                System.out.println("Subject: " + message.getSubject());
                System.out.println("From: " + message.getFrom()[0]);

                String subject = message.getSubject();

                message.setFlag(Flags.Flag.SEEN, true);
                message.setFlag(Flags.Flag.DELETED, true);

                System.out.println("Marked DELETE for message: " + subject);
            }

            emailFolder.close(true);

            store.close();

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * For test only
     */
    public static void deleteMessage(String senderEmail, String passwordEmail, String messageSubject) {
        try {
            // get the session object
            Properties properties = new Properties();
            properties.put("mail.store.protocol", PROTOCOL);
            properties.put("mail.pop3s.host", HOST_EMAIL);
            properties.put("mail.pop3s.port", SMTP_PORT);
            properties.put("mail.pop3.starttls.enable", START_TLS);
            Session emailSession = Session.getDefaultInstance(properties);
            // emailSession.setDebug(true);

            // create the POP3 store object and connect with the pop server
            Store store = emailSession.getStore("pop3s");
            store.connect(HOST_EMAIL, senderEmail, passwordEmail);

            // create the folder object and open it
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_WRITE);

            // retrieve the messages from the folder in an array and print it
            Message[] messages = emailFolder.getMessages();

            for (Message message : messages) {
                if (message.getSubject().equals(messageSubject)) {
                    message.setFlag(Flags.Flag.SEEN, true);
                    message.setFlag(Flags.Flag.DELETED, true);
                }
            }
            emailFolder.close(true);
            store.close();

        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

}
