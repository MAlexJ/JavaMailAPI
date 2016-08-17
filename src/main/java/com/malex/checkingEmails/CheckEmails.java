package com.malex.checkingEmails;

import javax.mail.*;
import java.io.IOException;
import java.util.Properties;

/**
 * Check an email using JavaMail API.
 * <p>
 * STEP #1: Get a Session
 * STEP #2: Create pop3 Store object and connect with pop server.
 * STEP #3: Create folder object. Open the appropriate folder in your mailbox.
 * STEP #4: Get your messages.
 * STEP #5: Close the Store and Folder objects.
 * <p>
 * http://www.tutorialspoint.com/javamail_api/javamail_api_checking_emails.htm
 */
public class CheckEmails {

    // Email settings
    private static final String HOST_EMAIL = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String START_TLS = "true";

    public static void main(String[] args) {

        String user = "iposcashregister@gmail.com";
        String password = "0672687484a";

        check(user, password);
    }

    static void check(String user, String password) {
        try {
            //create properties field
            Properties properties = new Properties();
            properties.put("mail.pop3.host", HOST_EMAIL);
            properties.put("mail.pop3.port", SMTP_PORT);
            properties.put("mail.pop3.starttls.enable", START_TLS);

            Session emailSession = Session.getDefaultInstance(properties);

            //create the POP3 store object and connect with the pop server
            Store store = emailSession.getStore("pop3s");

            store.connect(HOST_EMAIL, user, password);

            //create the folder object and open it

            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            // retrieve the messages from the folder in an array and print it
            Message[] messages = emailFolder.getMessages();
            System.out.println("messages.length---" + messages.length);

            for (int i = 0, n = messages.length; i < n; i++) {
                Message message = messages[i];
                System.out.println("---------------------------------");
                System.out.println("Email Number " + (i + 1));
                System.out.println("Subject: " + message.getSubject());
                System.out.println("From: " + message.getFrom()[0]);
                System.out.println("Text: " + message.getContent().toString());

            }

            //close the store and folder objects
            emailFolder.close(false);
            store.close();

        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }

}
