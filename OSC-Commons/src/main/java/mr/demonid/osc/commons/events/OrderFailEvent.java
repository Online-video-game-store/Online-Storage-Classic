package mr.demonid.osc.commons.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mr.demonid.osc.commons.dto.cart.CartItemResponse;

import java.util.List;
import java.util.UUID;


/**
 * Сообщение о неудачном проведении заказа.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderFailEvent {
    private UUID orderId;
    private UUID userId;
    private String message;
}
