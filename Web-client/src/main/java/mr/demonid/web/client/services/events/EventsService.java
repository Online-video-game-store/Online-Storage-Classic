package mr.demonid.web.client.services.events;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.osc.commons.events.OrderDoneEvent;
import mr.demonid.osc.commons.events.OrderFailEvent;
import mr.demonid.web.client.utils.IdnUtil;
import org.springframework.messaging.Message;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;


@Service
@AllArgsConstructor
@Log4j2
public class EventsService {

    private JwtService jwtService;
    private TokenTool tokenTool;
    private MessageMapper messageMapper;
    private IdnUtil idnUtil;

    private WebSocketService webSocketService;


    public void doProcess(Message<Object> message) {
        try {
            String jwtToken = tokenTool.getCurrentToken(message);
            if (jwtToken != null && jwtService.createSecurityContextFromJwt(jwtToken)) {
                String eventType = (String) message.getHeaders().get("routingKey");

                if ("order.done".equals(eventType)) {
                    OrderDoneEvent event = messageMapper.map(message, OrderDoneEvent.class);
                    finishOrder(event);
                } else if ("order.fail".equals(eventType)) {
                    OrderFailEvent event = messageMapper.map(message, OrderFailEvent.class);
                    failOrder(event);

                } else {
                    log.warn("Неизвестный тип события: {}", eventType);
                }

            } else {
                log.error("Недействительный Jwt-токен");
            }

        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    /*
     * Успешное завершение заказа.
     */
    private void finishOrder(OrderDoneEvent event)
    {
        // информируем юзера
        UUID userId = idnUtil.getUserId();
        CompletableFuture.runAsync(() ->
                        webSocketService.sendMessage(userId, event.getMessage()),
                CompletableFuture.delayedExecutor(3, TimeUnit.SECONDS)
        );
    }

    /*
    Заказ завершился ошибкой.
    */
    private void failOrder(OrderFailEvent event) {
        log.error("-- order {} cancelled", event);
        UUID userId = idnUtil.getUserId();
        CompletableFuture.runAsync(() ->
                webSocketService.sendMessage(userId, "Ошибка формирования заказа: " + event.getMessage()),
                CompletableFuture.delayedExecutor(3, TimeUnit.SECONDS)
        );

    }

}
