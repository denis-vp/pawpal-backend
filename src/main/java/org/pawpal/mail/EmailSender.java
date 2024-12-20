package org.pawpal.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

@Service
@AllArgsConstructor
public class EmailSender {

    private JavaMailSender mailSender;
    private SpringTemplateEngine templateEngine;

    public void sendHTMLEmail(String to, String subject, String body) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        mimeMessageHelper.setFrom("pawpal8787@gmail.com");
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(body, true);
        mailSender.send(mimeMessage);
    }

    public void  sendMailForNewAccount(String to, String subject, Map<String, Object> templateModel) throws MessagingException {
        Context context = new Context();
        context.setVariables(templateModel);
        String html = templateEngine.process("new_account.html", context);
        sendHTMLEmail(to, subject, html);
    }

    public void  sendMailForNewAppointment(String to, String subject, Map<String, Object> templateModel) throws MessagingException {
        Context context = new Context();
        context.setVariables(templateModel);
        String html = templateEngine.process("new_appointment.html", context);
        sendHTMLEmail(to, subject, html);
    }

    public void  sendMailForAppointmentReminder(String to, String subject, Map<String, Object> templateModel) throws MessagingException {
        Context context = new Context();
        context.setVariables(templateModel);
        String html = templateEngine.process("appointment_reminder.html", context);
        sendHTMLEmail(to, subject, html);
    }
}
