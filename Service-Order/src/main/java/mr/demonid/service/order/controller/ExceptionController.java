package mr.demonid.service.order.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import mr.demonid.osc.commons.dto.ErrorResponse;
import mr.demonid.service.order.exceptions.BaseOrderException;
import mr.demonid.service.order.services.ErrorResponseParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;


@RestControllerAdvice
@Log4j2
public class ExceptionController {

    @Autowired
    private ErrorResponseParser parser;

    @ExceptionHandler(BaseOrderException.class)
    public ResponseEntity<ErrorResponse> handleBaseOrderException(BaseOrderException e, HttpServletRequest request) {
        return parser.parse(e.getMessage())
                .map(er -> new ResponseEntity<>(er, HttpStatus.BAD_REQUEST))
                .orElseGet(() -> {
                    ErrorResponse fallback = new ErrorResponse(
                            LocalDateTime.now(),
                            HttpStatus.BAD_REQUEST.value(),
                            "Bad Request",
                            e.getMessage(),
                            request.getRequestURI()
                    );
                    return new ResponseEntity<>(fallback, HttpStatus.BAD_REQUEST);
                });
    }
}

