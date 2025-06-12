package mr.demonid.web.client.dto.orders;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


/**
 * Информация о заказе от сервиса Order.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private UUID orderId;
    private UUID userId;
    private BigDecimal totalAmount;
    private Long paymentId;
    private Long cardId;
    private LocalDateTime createdAt;
    private OrderStatus status;
}
