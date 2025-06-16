package mr.demonid.service.catalog.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import mr.demonid.osc.commons.dto.ErrorResponse;
import mr.demonid.service.catalog.exceptions.CatalogException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Log4j2
public class ExceptionController {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @ExceptionHandler(CatalogException.class)
    public ResponseEntity<ErrorResponse> catalogException(CatalogException e, HttpServletRequest request) {
        String message = e.getMessage();

        // Попытка распарсить message как ErrorResponse
        if (message != null && message.trim().startsWith("{")) {
            try {
                ErrorResponse nested = objectMapper.readValue(message, ErrorResponse.class);
                log.warn("Проброс ошибки из предыдущего сервиса: {}", nested);
                return new ResponseEntity<>(nested, HttpStatus.valueOf(nested.getStatus()));
            } catch (Exception ex) {
                log.warn("Не удалось распарсить message как ErrorResponse: {}", message);
            }
        }

        ErrorResponse body = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                message,
                request.getRequestURI()
        );
        log.error("Ошибка: {}", body);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

}

//@RestControllerAdvice
//@Log4j2
//public class ExceptionController {
//
//    @ExceptionHandler(CatalogException.class)
//    public ResponseEntity<ErrorResponse> catalogException(CatalogException e, HttpServletRequest request) {
//        ErrorResponse body = new ErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.BAD_REQUEST.value(),
//                "Bad Request",
//                e.getMessage(),
//                request.getRequestURI()
//        );
//        log.error("Ошибка: {}", body);
//        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
//    }
//
//}
