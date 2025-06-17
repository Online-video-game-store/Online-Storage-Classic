package mr.demonid.web.client.controllers;

import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import mr.demonid.osc.commons.dto.ErrorResponse;
import mr.demonid.web.client.exceptions.WebClientException;
import mr.demonid.web.client.utils.ErrorResponseParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;


@RestControllerAdvice
@Log4j2
public class ExceptionController {

    @Autowired
    private ErrorResponseParser parser;


    @ExceptionHandler(WebClientException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleBaseOrderException(WebClientException e, HttpServletRequest request) {
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


    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorResponse> handleFeignException(FeignException e, HttpServletRequest request) {
        ResponseEntity<ErrorResponse> body =
                parser.parse(e.contentUTF8())
                        .map(er -> ResponseEntity.status(e.status()).body(er))
                        .orElseGet(() -> ResponseEntity.status(e.status()).body(
                                new ErrorResponse(LocalDateTime.now(),
                                        e.status(),
                                        HttpStatus.valueOf(e.status()).getReasonPhrase(),
                                        e.getMessage(),
                                        request.getRequestURI())));
        log.error("Ошибка Feign: {}", body.getBody());
        return body;
    }

}
