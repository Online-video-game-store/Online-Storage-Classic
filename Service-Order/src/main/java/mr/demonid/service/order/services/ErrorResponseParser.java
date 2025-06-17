package mr.demonid.service.order.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import mr.demonid.osc.commons.dto.ErrorResponse;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;


/**
 * Класс для ручного преобразования JSON-строки в объект.
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
