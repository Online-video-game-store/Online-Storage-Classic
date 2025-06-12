package mr.demonid.web.client.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

/**
 * Запрос на добавление новой банковской карты пользователя.
 */
@Data
@AllArgsConstructor
public class CreateCardRequest {
    private UUID userId;
    private String cardNumber;
    private String expiryDate;
    private String cvv;
}
