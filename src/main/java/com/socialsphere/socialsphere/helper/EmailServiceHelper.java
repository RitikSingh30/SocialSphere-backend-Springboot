package com.socialsphere.socialsphere.helper;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailServiceHelper {

    private final JavaMailSender emailSender;

    @Value("${sender.mailId}")
    private String senderMailId;

    private final SpringTemplateEngine templateEngine;

    public void sendEmail(String to, String subject, Context context, String mailTemplateName) throws MessagingException {
        log.info("Sending email OTP journey started");
        context.setVariable("subject",subject);
        String htmlContentMailBody = templateEngine.process(mailTemplateName, context);
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "utf-8");
        mimeMessageHelper.setFrom(senderMailId);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(htmlContentMailBody,true);
        emailSender.send(mimeMessage);
        log.info("Sending email OTP journey completed");
    }
}
