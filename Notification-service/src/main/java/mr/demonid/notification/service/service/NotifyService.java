package mr.demonid.notification.service.service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.notification.service.domain.NotifyType;
import org.springframework.stereotype.Service;

import java.util.UUID;


/**
 * Сервис уведомлений пользователя.
 * Пока доступно только мыло.
 */
@Service
@AllArgsConstructor
@Log4j2
public class NotifyService {

    private final NotifyEmail notifyEmail;


    public void broadcast(NotifyType type, UUID orderId, String message) {
        notifyEmail.notifyEmail(type, orderId, message);
    }


}
