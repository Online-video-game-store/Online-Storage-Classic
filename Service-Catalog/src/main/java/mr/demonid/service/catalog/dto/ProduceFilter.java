package mr.demonid.service.catalog.dto;

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
public class ProduceFilter {
    private Long categoryId;
    private String productName;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}
