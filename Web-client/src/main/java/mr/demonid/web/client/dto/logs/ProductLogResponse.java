package mr.demonid.web.client.dto.logs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mr.demonid.web.client.dto.ReservationStatus;

import java.math.BigDecimal;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductLogResponse {
    private UUID orderId;
    private String name;
    private int quantity;
    private BigDecimal price;
}
