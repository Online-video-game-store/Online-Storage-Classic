package mr.demonid.web.client.dto.orders;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mr.demonid.web.client.dto.CartItemResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


/**
 * Запрос на открытие заказа.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateRequest {
    private UUID userId;
    private Long paymentId;
    private Long cardId;
    private BigDecimal totalAmount;
    private List<CartItemResponse> items;
}
