package mr.demonid.web.client.utils;

import feign.FeignException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;


@Log4j2
public class FeignErrorUtils {

    /**
     * Извлекает из FeignException сообщение об ошибке для его дальнейшего проксирования.
     * @param e               Ошибка FeignException
     * @param fallbackMessage Альтернативный текст, на случай если тело ошибки пустое
     */
    public static ResponseEntity<?> toResponse(FeignException e, String fallbackMessage) {
        log.error("Feign exception: {}",e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());

        HttpStatus status = HttpStatus.resolve(e.status());
        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        String content = e.contentUTF8() == null ? fallbackMessage : e.contentUTF8();
        MediaType mediaType = content.trim().startsWith("{") ? MediaType.APPLICATION_JSON : MediaType.TEXT_PLAIN;
        return ResponseEntity.status(status).contentType(mediaType).body(content);
    }

}


