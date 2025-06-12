package mr.demonid.osc.commons.dto.catalog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mr.demonid.osc.commons.dto.cart.CartItemResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


/**
 * Сообщение на резервирование товаров заказа.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatalogReserveRequest {
    private UUID orderId;
    private UUID userId;
    private Long paymentId;
    private Long cardId;
    private BigDecimal totalAmount;
    private List<CartItemResponse> items;
}
