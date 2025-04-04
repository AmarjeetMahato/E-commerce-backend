package com.springboot.service.EmailService;


import com.springboot.enums.EmailTemplateName;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    private  final JavaMailSender mailSender;
    private  final SpringTemplateEngine templateEngine;

    @Async
    public void sendEmail(
            String to,
            String username,
            EmailTemplateName emailTemplateName,
            String confirmationUrl,
            String code, // This can be OTP or Activation Code
            String subject
    ) throws MessagingException {
        String templateName = (emailTemplateName == null) ? "confirm-email" : emailTemplateName.name();

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED,
                StandardCharsets.UTF_8.name()
        );

        Map<String, Object> properties = new HashMap<>();
        properties.put("username", username);
        properties.put("confirmationUrl", confirmationUrl);

        // 🔥 Ensure both OTP and Activation Code are covered
        if (emailTemplateName == EmailTemplateName.OTP_LOGIN) {
            properties.put("otp", code); // ✅ Use "otp" for login OTP emails
        } else {
            properties.put("activation_code", code); // ✅ Use "activation_code" for registration
        }

        Context context = new Context();
        context.setVariables(properties);

        helper.setFrom("amarjitmahato016@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);

        String template = templateEngine.process(templateName, context);
        helper.setText(template, true);

        mailSender.send(mimeMessage);
    }

}
