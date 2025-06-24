package mr.demonid.web.client.configs.events;

import lombok.extern.log4j.Log4j2;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * WebSocketHandler для отправки уведомлений.
 */
@Component
@Log4j2
public class NotificationWebSocketHandler extends TextWebSocketHandler {

    private static final Map<UUID, WebSocketSession> activeSessions = new ConcurrentHashMap<>();
    private static final Map<UUID, Queue<String>> pendingMessages = new ConcurrentHashMap<>();


    /**
     * Новая сессия (смена страниц, обновление и тд).
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        if (session.isOpen()) {
            log.info("++ WebSocket established: {}", session.getId());
            // Привязываем сессию к пользователю, на случай отложенной отправки сообщений.
            UUID userId = extractUserId(session);   // id должен быть внедрен в интерцепторе
            activeSessions.put(userId, session);
            // Отправляем отложенные сообщения
            Queue<String> messages = pendingMessages.remove(userId);
            if (messages != null) {
                messages.forEach(message -> {
                    log.info("  ++ pending messages: {}", message);
                    sendMessageToUser(userId, message);
                });
            }
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Обработка входящих сообщений от клиента
    }

    /**
     * Сессия закрыта.
     */
    @SuppressWarnings("resource")
    @Override
    public void afterConnectionClosed(@Nullable WebSocketSession session, @Nullable CloseStatus status) {
        // Удаляем сессию при отключении клиента
        if (session != null) {
            UUID userId = extractUserId(session);
            log.warn("++ Web socket close session for user: {}", userId);
            if (userId != null) {
                activeSessions.remove(userId);
            }
        }
    }

    /**
     * Отправка сообщения конкретному пользователю.
     */
    public void sendMessageToUser(UUID userId, String message) {
        WebSocketSession session = activeSessions.get(userId);
        if (session != null && session.isOpen()) {
            try {
                log.info("  ++ sending message: {}", message);
                String msg = message.replaceAll("&", "\n");
                session.sendMessage(new TextMessage(msg));
                return;
            } catch (IOException e) {
                log.error("Ошибка отправки сообщения: {}", e.getMessage());
            }
        }
        // Не получилось отправить, помещаем в очередь отложенных
        log.warn("  ++ moved to pending: {}", message);
        pendingMessages.computeIfAbsent(userId, k -> new ConcurrentLinkedQueue<>()).add(message);
    }


    private UUID extractUserId(WebSocketSession session) {
        return session == null ? null : (UUID) session.getAttributes().get("userId");
    }

}
