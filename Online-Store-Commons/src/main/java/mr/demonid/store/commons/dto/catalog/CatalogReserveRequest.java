package mr.demonid.service.catalog.dto.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mr.demonid.service.catalog.dto.CartItemResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


/**
 * Сообщение на резервирование товаров заказа.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatalogReserveRequestEvent {
    private UUID orderId;
    private UUID userId;
    private Long paymentId;
    private Long cardId;
    private BigDecimal totalAmount;
    private List<CartItemResponse> items;
}
