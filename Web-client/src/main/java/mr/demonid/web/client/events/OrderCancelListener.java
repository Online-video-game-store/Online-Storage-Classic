package mr.demonid.web.client.events;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.web.client.services.events.EventsService;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;


/**
 * Слушатель для канала orderCancel-in-0.
 * Все сообщения должны содержать в заголовке Jwt-токен,
 * который проверяется на сервере-аутентификации.
 */
@Component
@AllArgsConstructor
@Log4j2
public class OrderCancelListener {

    private EventsService eventsService;


    @Bean
    public Consumer<Message<Object>> channelOrderCancel() {
        return message -> eventsService.doProcess(message);
    }

}
