package mr.demonid.osc.commons.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mr.demonid.osc.commons.dto.cart.CartItemResponse;

import java.util.List;
import java.util.UUID;

/**
 * Сообщение об успешной передаче товаров из резерва в службу комплектации и доставки.
 * Это последний этап заказа и свидетельствует об успешном проведении заказа.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDoneEvent {
    private UUID orderId;
    private String message;
    private List<CartItemResponse> items;
}
