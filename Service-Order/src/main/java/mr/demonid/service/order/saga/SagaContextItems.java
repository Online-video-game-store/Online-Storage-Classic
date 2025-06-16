package mr.demonid.service.order.saga;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class SagaContextItems {
    private Long productId;
    private int quantity;
}
