package mr.demonid.service.order.saga;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Контекст для передачи данных между шагами.
 */
@Data
@AllArgsConstructor
public class SagaContext {
    private UUID orderId;
    private UUID userId;
    private Long paymentId;
    private Long cardId;
    private BigDecimal totalAmount;

    private List<SagaContextItems> items;

}
