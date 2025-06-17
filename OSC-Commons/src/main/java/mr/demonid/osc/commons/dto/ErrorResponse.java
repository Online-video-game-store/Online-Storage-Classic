package mr.demonid.osc.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;                 // HttpStatus.BAD_REQUEST.value()
    private String error;               // "Bad Request"
    private String message;             // e.getMessage()
    private String path;                // request.getRequestURI()

    /*
        Проверка валидности полей.
     */
    public boolean isValid() {
        return timestamp != null
                && status >= 200
                && status <= 600
                && error != null
                && !error.isBlank()
                && message != null
                && !message.isBlank()
                && path != null
                && !path.isBlank();
    }

}
