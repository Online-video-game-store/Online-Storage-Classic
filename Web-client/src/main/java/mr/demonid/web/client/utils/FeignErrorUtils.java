package mr.demonid.web.client.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Map;


@Log4j2
public class FeignErrorUtils {

    /**
     * Извлекает из FeignException сообщение об ошибке и возвращает в виде JSON.
     * @param e               Ошибка FeignException
     * @param fallbackMessage Альтернативный текст, на случай если тело ошибки пустое
     */
    public static ResponseEntity<?> toResponse(FeignException e, String fallbackMessage) {
        log.error("Feign exception: {}", e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());

        HttpStatus status = HttpStatus.resolve(e.status());
        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        String content = e.contentUTF8() == null ? fallbackMessage : e.contentUTF8();
        MediaType mediaType = content.trim().startsWith("{") ? MediaType.APPLICATION_JSON : MediaType.TEXT_PLAIN;
        return ResponseEntity.status(status).contentType(mediaType).body(content);

//        HttpStatus status = HttpStatus.resolve(e.status());
//        if (status == null) {
//            status = HttpStatus.INTERNAL_SERVER_ERROR;
//        }
//        String content = e.contentUTF8();
//        if (content == null || content.isBlank()) {
//            return ResponseEntity.status(status)
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .body(Map.of("message", fallbackMessage));
//        }
//        if (content.trim().startsWith("{")) {
//            try {
//                ObjectMapper mapper = new ObjectMapper();
//                Map<String, Object> parsed = mapper.readValue(content, new TypeReference<>() {});
//                Object message = parsed.getOrDefault("message", fallbackMessage);
//                return ResponseEntity.status(status)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .body(Map.of("message", message));
//            } catch (Exception ex) {
//                log.error("Не удалось преобразовать тело в JSON из Feign-исключения: {}", ex.getMessage());
//            }
//
//
//        }
//        return ResponseEntity.status(status)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(Map.of("message", content));
    }

//    HttpStatus status = HttpStatus.resolve(e.status());
//        if (status == null) {
//        status = HttpStatus.INTERNAL_SERVER_ERROR;
//    }
//    String content = e.contentUTF8() == null ? fallbackMessage : e.contentUTF8();
//    MediaType mediaType = content.trim().startsWith("{") ? MediaType.APPLICATION_JSON : MediaType.TEXT_PLAIN;
//        return ResponseEntity.status(status).contentType(mediaType).body(content);

}


