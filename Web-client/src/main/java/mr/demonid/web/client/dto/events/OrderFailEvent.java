package mr.demonid.web.client.dto.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


/**
 * Сообщение о неудачном проведении заказа.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderFailEvent {
    private UUID orderId;
    private String message;
}
