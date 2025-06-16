package mr.demonid.service.order.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import mr.demonid.osc.commons.dto.ErrorResponse;
import mr.demonid.service.order.exceptions.BaseOrderException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;


@RestControllerAdvice
@Log4j2
public class ExceptionController {


    @ExceptionHandler(BaseOrderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleBaseOrderException(BaseOrderException e, HttpServletRequest request) {
        ErrorResponse body = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                e.getMessage(),
                request.getRequestURI()
        );
        log.error("Ошибка: {}", body);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}

////    @ExceptionHandler(BaseOrderException.class)
////    @ResponseStatus(HttpStatus.NOT_FOUND)
////    public String catalogException(BaseOrderException e) {
////        log.error("handle error: {}", e.getMessage());
////        if (e.getMessage().startsWith("Ошибка: ")) {
////            return e.getMessage();
////        }
////        return e.getMessage();
////    }
