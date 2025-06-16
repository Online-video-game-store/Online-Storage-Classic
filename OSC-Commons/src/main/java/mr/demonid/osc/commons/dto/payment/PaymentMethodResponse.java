package mr.demonid.osc.commons.dto.payment;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Запрос на получение списка доступных способов оплаты.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMethodResponse {
    private long id;
    private String name;
    private boolean supportsCards;
}

