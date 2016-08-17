package com.malex.sendingEmail;

import org.junit.Test;

import javax.mail.*;
import java.util.*;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class SendSimpleEmailTest {

    @Test
    public void testSendMessage() {
        //given
        String recipient = "iposcashregister@gmail.com";  // Recipient's email ID needs RECIPIENT be mentioned.
        String subjectMessage = "New Message " + new Date();
        String textMessage = "Text message!!!!";

        String sender = "iposcashregister@gmail.com";
        String userEmail = "iposcashregister";
        String passwordEmail = "0672687484a";

        //when
        SendSimpleEmail.send(sender, userEmail, passwordEmail, recipient, subjectMessage, textMessage);

        //then
        assertCheckMessage(recipient, passwordEmail, subjectMessage);
    }

    @Test
    public void testSendMessages() {
        //given
        String recipient = "iposcashregister@gmail.com";  // Recipient's email ID needs RECIPIENT be mentioned.

        String subjectMessage1 = "Message One " + new Date();  // message #1
        String textMessage1 = "Text message One !!!!";

        String subjectMessage2 = "New Message Two " + new Date(); // message #2
        String textMessage2 = "Text message two !!!!";

        String sender = "iposcashregister@gmail.com";
        String userEmail = "iposcashregister";
        String passwordEmail = "0672687484a";

        //when
        SendSimpleEmail.send(sender, userEmail, passwordEmail, recipient, subjectMessage1, textMessage1);
        SendSimpleEmail.send(sender, userEmail, passwordEmail, recipient, subjectMessage2, textMessage2);

        //then
        assertCheckMessage(recipient, passwordEmail, subjectMessage1, subjectMessage2);
    }


    private void assertCheckMessage(String email, String password, String... expectSubjectMessage) {

        try {
            Thread.sleep(200);

            //create properties field
            Properties properties = new Properties();
            properties.put("mail.pop3.host", "smtp.gmail.com");
            properties.put("mail.pop3.port", "587");
            properties.put("mail.pop3.starttls.enable", "true");

            Session emailSession = Session.getDefaultInstance(properties);

            //create the POP3 store object and connect with the pop server
            Store store = emailSession.getStore("pop3s");

            store.connect("smtp.gmail.com", email, password);

            //create the folder object and open it
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            Message[] messages = emailFolder.getMessages();

            if (messages.length == 1) {
                messages[0].setFlag(Flags.Flag.SEEN, true);
                messages[0].setFlag(Flags.Flag.DELETED, true);
                assertEquals(messages[0].getSubject(), expectSubjectMessage[0]);
                System.out.println(" >>>> Message is find: " + messages[0].getSubject());
            } else {
                if (expectSubjectMessage.length == 1) {
                    boolean messageIsFound = false;
                    for (Message message : messages) {
                        if (message.getSubject().equals(expectSubjectMessage[0])) {
                            messageIsFound = true;
                            message.setFlag(Flags.Flag.SEEN, true);
                            message.setFlag(Flags.Flag.DELETED, true);
                            System.out.println("  >>>> Message is find: " + message.getSubject());
                        }
                    }
                    assertTrue(messageIsFound);
                } else {
                    List<String> actualList = new ArrayList<>();
                    for (Message message : messages) {
                        actualList.add(message.getSubject());
                        message.setFlag(Flags.Flag.SEEN, true);
                        message.setFlag(Flags.Flag.DELETED, true);
                    }
                    List expectedList = Arrays.asList(expectSubjectMessage);

                    System.out.println("  >>>> actualList size: " + actualList.size() + ",  actualList message: " + actualList);
                    System.out.println("  >>>> expectedList size: " + expectedList.size() + ", expectedList message: " + expectedList);

                    assertTrue(actualList.containsAll(expectedList));
                }
            }

            //close the store and folder objects
            emailFolder.close(false);
            store.close();

        } catch (MessagingException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
