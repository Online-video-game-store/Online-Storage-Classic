package mr.demonid.osc.commons.dto.payment;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;


/**
 * Запрос на проведение платежа.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
    private UUID orderId;
    private UUID userId;
    private Long paymentId;
    private Long cardId;
    private BigDecimal totalAmount;
}
