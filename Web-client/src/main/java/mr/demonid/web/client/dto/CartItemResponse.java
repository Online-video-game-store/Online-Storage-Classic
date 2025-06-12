package mr.demonid.web.client.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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
