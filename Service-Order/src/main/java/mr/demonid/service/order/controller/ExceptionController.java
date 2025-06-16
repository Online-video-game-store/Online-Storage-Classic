package mr.demonid.service.order.controller;

import lombok.extern.log4j.Log4j2;
import mr.demonid.service.order.exceptions.BaseOrderException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Log4j2
public class ExceptionController {

    @ExceptionHandler(BaseOrderException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String catalogException(BaseOrderException e) {
        log.error("handle error: {}", e.getMessage());
        if (e.getMessage().startsWith("Ошибка: ")) {
            return e.getMessage();
        }
        return "Ошибка: " + e.getMessage();
    }

}
