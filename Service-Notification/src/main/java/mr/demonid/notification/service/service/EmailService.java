package mr.demonid.notification.service.service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.notification.service.config.AppConfiguration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;


@Service
@AllArgsConstructor
@Log4j2
public class EmailService {

    private final JavaMailSender mailSender;
    private AppConfiguration config;


    /**
     * Асинхронная функция отправки письма
     * @param to      Адресат
     * @param subject Тема письма
     * @param text    Содержание. Предполагается в виде HTML.
     */
    @Async
    public void sendEmail(String to, String subject, String text) {
        try {
            if (config.getEmailPassword().isBlank()) {
                throw new MessagingException("Не задан ключ для доступа к mail API");
            }
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
