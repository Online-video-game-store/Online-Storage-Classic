package mr.demonid.service.catalog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mr.demonid.service.catalog.domain.ReservationStatus;

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
    private ReservationStatus reservationStatus;
}
