package mr.demonid.web.client.dto.filters;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


/**
 * Фильтр для выборки товаров из БД.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductFilter {
    private Long categoryId;
    private String productName;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}
