package mr.demonid.service.order.saga;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.osc.commons.dto.cart.CartItemResponse;
import mr.demonid.osc.commons.events.OrderDoneEvent;
import mr.demonid.service.order.exceptions.SagaStepException;
import mr.demonid.service.order.events.OrderPublisher;

/**
 * Шаг: Информирование пользователя о статусе сделки.
 */
@AllArgsConstructor
@Log4j2
public class InformationStep implements SagaStep<SagaContext> {

    private OrderPublisher publisher;

    @Override
    public void execute(SagaContext context) throws SagaStepException {
        publisher.sendFinishOrderEvent(new OrderDoneEvent(
                context.getOrderId(),
                "Покупка совершена. Ваш заказ передан в службу доставки.",
                context.getItems().stream().map(e -> new CartItemResponse(e.getProductId(), e.getQuantity())).toList())
        );
    }

    @Override
    public void rollback(SagaContext context) {
        log.error("Can't send message.");
    }


}
