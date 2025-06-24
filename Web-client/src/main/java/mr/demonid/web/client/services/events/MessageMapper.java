package mr.demonid.web.client.services.events;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.io.IOException;


/**
 * По дефолту классы типа Object десериализуются в LinkedHashMap, как универсальное хранилище "key:value".
 * Для конвертации таких объектов в нужный класс оформим это отдельно.
 */
@Service
@Log4j2
public class MessageMapper {

    @Autowired
    private ObjectMapper objectMapper;


    public <T> T map(Message<?> message, Class<T> clazz) {
        byte[] payload = (byte[]) message.getPayload();
        try {
            return objectMapper.readValue(payload, clazz);
        } catch (IOException e) {
            log.error("convert error: {}", e.getMessage());
        }
        return null;
    }


}
