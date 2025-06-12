package mr.demonid.web.client.dto.logs;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mr.demonid.web.client.dto.orders.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


/**
 * Информация о заказе на странице админа.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatistic {
    private UUID orderId;
    private UUID userId;
    private String fio;
    private String paymentMethod;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private OrderStatus status;
}
