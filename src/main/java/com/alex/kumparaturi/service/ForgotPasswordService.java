package com.alex.kumparaturi.service;

import com.alex.kumparaturi.model.MailInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.Map;

@Service
public class ForgotPasswordService {
    private static final Logger logger = LoggerFactory.getLogger(ForgotPasswordService.class);

    @Autowired
    EmailSenderService emailSenderService;

    public void sendResetPasswordEmail(String userEmail, String resetPasswordURL) {
        MailInfo mailInfo = new MailInfo();
        mailInfo.setFrom("no-reply@kumparaturi.com");
        mailInfo.setTo(userEmail);
        mailInfo.setSubject("Resetare parola aplicatie Kumparaturi");
        Map<String, Object> model = new HashMap<>();
        model.put("resetPasswordURL", resetPasswordURL);
        mailInfo.setModel(model);

        try {
            emailSenderService.sendHtmlMail(mailInfo, "email/reset_password_email");
        } catch (MessagingException e) {
            logger.info("error in  send activation Email " + e);
        }
    }
}
