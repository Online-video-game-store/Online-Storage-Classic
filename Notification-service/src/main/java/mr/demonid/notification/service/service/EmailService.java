package mr.demonid.notification.service.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;


    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    /**
     * Асинхронная функция отправки письма
     * @param to      Адресат
     * @param subject Тема письма
     * @param text    Содержание. Предполагается в виде HTML.
     */
    @Async
    public void sendEmail(String to, String subject, String text) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true); // true для HTML
            mailSender.send(message);
            log.info("Письмо отправлено в потоке {}", Thread.currentThread().getName());
        } catch (MessagingException e) {
            log.error("Ошибка при отправке письма: {}", e.getMessage());
        }
    }
}
