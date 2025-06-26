package mr.demonid.notification.service.service;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.notification.service.domain.NotifyType;
import mr.demonid.notification.service.service.events.JwtService;
import mr.demonid.notification.service.service.events.MessageMapper;
import mr.demonid.notification.service.service.events.TokenTool;
import mr.demonid.osc.commons.events.OrderDoneEvent;
import mr.demonid.osc.commons.events.OrderFailEvent;
import org.springframework.messaging.Message;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
@Log4j2
public class EventsService {

    private JwtService jwtService;
    private TokenTool tokenTool;
    private MessageMapper messageMapper;

    private NotifyService notifyService;



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
        log.info("-- order notification successes: {}", event);
        notifyService.broadcast(NotifyType.INFO, event.getOrderId(), event.getMessage());
    }

    /*
    Заказ завершился ошибкой.
    */
    private void failOrder(OrderFailEvent event) {
        log.error("-- order notification failed: {}", event);
        notifyService.broadcast(NotifyType.ERROR, event.getOrderId(), event.getMessage());
    }


}
