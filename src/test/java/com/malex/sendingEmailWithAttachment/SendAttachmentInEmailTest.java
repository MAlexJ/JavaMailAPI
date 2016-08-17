package com.malex.sendingEmailWithAttachment;

import org.junit.Test;

import javax.mail.*;
import java.io.IOException;
import java.util.*;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

public class SendAttachmentInEmailTest {

    /**
     * The filename of the attachment.
     */
    private String fileName;


    @Test
    public void testSendMessage() {
        //given
        String recipient = "iposcashregister@gmail.com";  // Recipient's email ID needs RECIPIENT be mentioned.
        String subjectMessage = "New Message " + new Date();
        String fileName = "log/iPOS_0_0.log";

        String sender = "iposcashregister@gmail.com";
        String userEmail = "iposcashregister";
        String passwordEmail = "0672687484a";

        //when
        SendAttachmentInEmail.send(sender, userEmail, passwordEmail, recipient, subjectMessage, fileName);

        //then
        assertMessage(recipient, passwordEmail, subjectMessage, fileName);
    }


    private void assertMessage(String email, String password, String subjectMessage, String expectAttachmentFile) {
        try {

            //sleep thread
            sleep(200);

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

            for (Message message : messages) {

                if (message.getSubject().equals(subjectMessage)) {
                    //when
                    String actualAttachmentFile = getAttachmentFileName(message);
                    message.setFlag(Flags.Flag.SEEN, true);
                    message.setFlag(Flags.Flag.DELETED, true);

                    //then
                    assertNotNull(actualAttachmentFile);
                    assertEquals(actualAttachmentFile, expectAttachmentFile);
                    assertEquals(message.getSubject(), subjectMessage);
                }

            }

            //close the store and folder objects
            emailFolder.close(false);
            store.close();

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    /**
     * This method checks for content-type
     * based on which, it processes and
     * fetches the content of the message
     */
    private String getAttachmentFileName(Part p) {
        try {
            if (p.isMimeType("multipart/*")) {
                Multipart mp = (Multipart) p.getContent();
                int count = mp.getCount();

                for (int i = 0; i < count; i++) {
                    getAttachmentFileName(mp.getBodyPart(i));
                }

            } else if (p.getContentType().startsWith("application/octet-stream")) {

                fileName = p.getFileName();

            }
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }

    private void sleep(int millis) {

        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            //ignore
        }
    }

}
