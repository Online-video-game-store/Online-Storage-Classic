package mr.demonid.service.catalog.dto;

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
