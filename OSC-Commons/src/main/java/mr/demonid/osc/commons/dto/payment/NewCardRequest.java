package mr.demonid.osc.commons.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Запрос на добавление новой банковской карты пользователя.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCardRequest {
    private UUID userId;
    private String cardNumber;
    private String expiryDate;
    private String cvv;
}
