package mr.demonid.service.order.events;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.osc.commons.events.OrderDoneEvent;
import mr.demonid.osc.commons.events.OrderFailEvent;
import mr.demonid.service.order.utils.TokenTool;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;


/**
 * Отправка сообщений через Spring Cloud Bus.
 * Для безопасности в заголовок внедряем Jwt-токен текущего пользователя.
 */
@Service
@AllArgsConstructor
@Log4j2
public class OrderPublisher {

    private StreamBridge streamBridge;
    private TokenTool tokenTool;


    /**
     * Отправка сообщения об успешном завершении заказа.
     */
    public void sendFinishOrderEvent(OrderDoneEvent event) {
        send("ch-pk8000-order-out", "order.done", event);
    }

    /**
     * Отправка сообщения о неудаче оформления заказа
     */
    public void sendFailOrderEvent(OrderFailEvent event) {
        send("ch-pk8000-cancel-out", "order.fail", event);
    }


    /*
     * Непосредственно отправка сообщения на заданный канал и с заданным ключом маршрутизации.
     */
    private void send(String channel, String routingKey, Object obj) {
        String jwtToken = tokenTool.getCurrentToken();
        if (jwtToken != null) {
            streamBridge.send(channel,
                    MessageBuilder.withPayload(obj)
                            .setHeader("routingKey", routingKey)
                            .setHeader("Authorization", "Bearer " + jwtToken)
                            .build());
            log.info("-- Отправлено событие {}: {}", routingKey, obj.toString());
        } else {
            log.error("Can't send message: unknown user!");
        }
    }

}
