package mr.demonid.notification.service.service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.notification.service.domain.NotifyTargetEmail;
import mr.demonid.notification.service.domain.NotifyType;
import mr.demonid.notification.service.repository.NotifyEmailRepository;
import mr.demonid.notification.service.util.IdnUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Сервис рассылки уведомлений по мылу.
 */
@Service
@AllArgsConstructor
@Log4j2
public class NotifyEmail {

    private final EmailService emailService;
    private final NotifyEmailRepository notifyEmailRepository;
    private IdnUtil idnUtil;


    public void notifyEmail(NotifyType type, UUID orderId, String message) {
        String text = String.format("%s!\n%s по заказу {%s}.\n%s\n", idnUtil.getUserName(), type.getType(), orderId.toString(), message);
        List<NotifyTargetEmail> targets = notifyEmailRepository.findNotifyTargetEmailByUserId(idnUtil.getUserId());
        targets.forEach(e -> {
            log.info("-- send to {}, subj: {}, message: {}", e.getEmail(), "Information on order", text);
// TODO: раскомментировать в окончательной редакции.
            emailService.sendEmail(e.getEmail(), "Information on order", text);
        });
    }

}
