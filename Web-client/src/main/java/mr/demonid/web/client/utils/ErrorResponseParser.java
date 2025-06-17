package mr.demonid.web.client.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mr.demonid.osc.commons.dto.ErrorResponse;
import org.springframework.stereotype.Component;

import java.util.Optional;


/**
 * Пытается создать из JSON-строки объект класса ErrorResponse.
 * Или возвращает Optional.empty(), если в строке не ErrorResponse.
 */
@Component
public class ErrorResponseParser {
    private final ObjectMapper mapper;

    public ErrorResponseParser(ObjectMapper mapper) {
        this.mapper = mapper;
    }


    public Optional<ErrorResponse> parse(String json) {
        if (json == null || !json.startsWith("{") || !json.endsWith("}"))
            return Optional.empty();
        try {
            ErrorResponse er = mapper.readValue(json, ErrorResponse.class);
            return er.isValid() ? Optional.of(er) : Optional.empty();
        } catch (JsonProcessingException e) {
            return Optional.empty();
        }
    }

}
