package mr.demonid.osc.commons.dto.cart;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для получения списка товаров из корзины.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartItemResponse {
    private Long productId;
    private int quantity;
}
