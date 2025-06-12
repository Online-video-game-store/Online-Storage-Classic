package mr.demonid.web.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


/**
 * Запрос на изменение данных о товаре.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    private Long productId;
    private String name;
    private BigDecimal price;
    private Long category;
    private int stock;
    private String description;
}
