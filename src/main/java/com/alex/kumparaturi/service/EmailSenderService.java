package com.alex.kumparaturi.service;

import com.alex.kumparaturi.model.MailInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

@Service
public class EmailSenderService {

    private static final Logger logger = LoggerFactory.getLogger(EmailSenderService.class);

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    TemplateEngine templateEngine;

    @Async
    public void sendEmail(SimpleMailMessage email) {
        javaMailSender.send(email);
    }

    @Async
    public void sendHtmlMail(MailInfo mailInfo, String templateName) throws MessagingException {
        MimeMessage mail = javaMailSender.createMimeMessage();
        Context context = new Context();
        context.setVariables(mailInfo.getModel());

        String bodyHtml = templateEngine.process(templateName, context);

        MimeMessageHelper helper = new MimeMessageHelper(mail, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        helper.setFrom(mailInfo.getFrom());
        helper.setTo(mailInfo.getTo());
        helper.setSubject(mailInfo.getSubject());
        helper.setText(bodyHtml, true);

        javaMailSender.send(mail);
    }
}
