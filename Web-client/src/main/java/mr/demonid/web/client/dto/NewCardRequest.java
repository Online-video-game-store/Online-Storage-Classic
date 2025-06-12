package mr.demonid.web.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Запрос от фронтенда на добавление новой банковской карты.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCardRequest {
    private String cardNumber;
    private String expiryDate;
    private String cvv;

}
