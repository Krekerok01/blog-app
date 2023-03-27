package com.krekerok.blogapp.service.impl;



import com.krekerok.blogapp.service.MailSenderService;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MailSenderServiceImpl implements MailSenderService {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String username;

    @Override
    public void send(String emailTo, String subject, String message) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(message, true);
            helper.setTo(emailTo);
            helper.setSubject(subject);
            helper.setFrom(username);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("MessagingException: ");
            log.error(e.getMessage());
        } catch (MailSendException e){
            log.error("MailSendException: ");
            log.error(e.getMessage());
        }
    }

}
