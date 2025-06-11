package mr.demonid.store.commons.dto;

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
}
