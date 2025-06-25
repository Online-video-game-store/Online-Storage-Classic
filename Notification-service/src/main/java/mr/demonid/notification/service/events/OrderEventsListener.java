package mr.demonid.notification.service.events;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.notification.service.service.EventsService;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;


/**
 * Слушатель для канала orderEvents-in-0.
 * Все сообщения должны содержать в заголовке Jwt-токен,
 * который проверяется на сервере-аутентификации.
 */
@Component
@AllArgsConstructor
@Log4j2
public class OrderEventsListener {

    private EventsService eventsService;


    @Bean
    public Consumer<Message<Object>> channelOrderEvents() {
        return message -> eventsService.doProcess(message);
    }


}
