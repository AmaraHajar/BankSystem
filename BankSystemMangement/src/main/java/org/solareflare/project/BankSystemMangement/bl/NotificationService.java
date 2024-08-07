package org.solareflare.project.BankSystemMangement.bl;


import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.solareflare.project.BankSystemMangement.utils.NotificationSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService implements NotificationSystem {

    @Autowired
    private JavaMailSender mailSender;

    // Replace with your Twilio credentials
    private final String ACCOUNT_SID = "ACcaa696d689dfcde8f800da5531a86db2";
    private final String AUTH_TOKEN = "6b0d80b522418a0510f2316fc7c5a91a";
    private final String FROM_PHONE = "+19782679591";

//    A=QDGEMK9T3HUTU3HHSUYSGLXW


    @Override
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    @Override
    public void sendSms(String to, String message) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        System.out.println("phone number "+to);
        Message.creator(
                new PhoneNumber(to),
                new PhoneNumber(FROM_PHONE),
                message
        ).create();

}
}


