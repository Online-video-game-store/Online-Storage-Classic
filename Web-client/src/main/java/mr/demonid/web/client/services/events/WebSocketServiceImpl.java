package mr.demonid.web.client.services.events;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.web.client.configs.events.NotificationWebSocketHandler;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@AllArgsConstructor
@Log4j2
public class WebSocketServiceImpl implements WebSocketService {

    private NotificationWebSocketHandler webSocketHandler;


    /**
     * Рассылка сообщения по WebSocket всем подписчикам.
     * Самый удобный способ известить frontend о чем-то.
     */
    @Override
    public void sendMessage(UUID userId, String message) {
        log.info(message);
        webSocketHandler.sendMessageToUser(userId, message);
    }

}
