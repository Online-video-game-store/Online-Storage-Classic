package mr.demonid.osc.commons.dto.catalog;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Товар, ушедший с резерва в комплектацию
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductTransferred {
    private Long productId;
    private int quantity;
}
