package com.algorithm.utils;


import java.security.GeneralSecurityException;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import com.sun.mail.util.MailSSLSocketFactory;

public class SendEmail {
    public static void SentEmail (String suggest) throws GeneralSecurityException {
        // Recipient email
        String to = "2419038t@student.gla.ac.uk";

        // Sender email
        String from = "904789316@qq.com";

        // Specify the host to send mail to smtp.qq.com
        String host = "smtp.qq.com";

        // Get system properties
        Properties properties = System.getProperties();

        // Set up mail server
        properties.setProperty("mail.smtp.host", host);

        properties.put("mail.smtp.auth", "true");
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl.socketFactory", sf);
        // Get the default session object
        Session session = Session.getDefaultInstance(properties,new Authenticator(){
            public PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication("904789316@qq.com", "rsqqccqtbtvabdgj"); //Sender’s email username and password
            }
        });

        try{
            MimeMessage message = new MimeMessage(session);

            // Set From:
            message.setFrom(new InternetAddress(from));

            // Set To:
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject:
            message.setSubject("Suggest");

            message.setText("This is a suggestion" + suggest);

            Transport.send(message);
            System.out.println("Sent message successfully....");
        }catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

}
