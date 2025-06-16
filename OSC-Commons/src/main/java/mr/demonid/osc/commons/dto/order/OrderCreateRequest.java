package mr.demonid.osc.commons.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mr.demonid.osc.commons.dto.cart.CartItemResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateRequest {
    private UUID userId;
    private Long paymentId;
    private Long cardId;
    private List<CartItemResponse> items;
}
