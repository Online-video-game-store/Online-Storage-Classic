package mr.demonid.osc.commons.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartNeededResponse {
    private Long productId;
    private String name;
    private int request;
    private int stock;
}
