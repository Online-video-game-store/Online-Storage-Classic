package mr.demonid.service.order.saga;

import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.osc.commons.dto.cart.CartItemResponse;
import mr.demonid.osc.commons.dto.catalog.ProductResponse;
import mr.demonid.service.order.domain.Order;
import mr.demonid.service.order.domain.OrderStatus;
import mr.demonid.service.order.exceptions.SagaStepException;
import mr.demonid.service.order.links.CatalogServiceClient;
import mr.demonid.service.order.repository.OrderRepository;

import java.math.BigDecimal;


/**
 * Шаг: создание заказа.
 */
@AllArgsConstructor
@Log4j2
public class CreateOrderStep implements SagaStep<SagaContext> {

    private final OrderRepository orderRepository;
    private final CatalogServiceClient catalogServiceClient;


    @Override
    public void execute(SagaContext context) throws SagaStepException {
        Order order = Order.builder()
                .userId(context.getUserId())
                .totalAmount(context.getTotalAmount())
                .paymentId(context.getPaymentId())
                .cardId(context.getCardId())
                .status(OrderStatus.Pending)
                .createAtNow()
                .build();

//        order = orderRepository.save(order);
//        context.setOrderId(order.getOrderId());
//        if (order.getOrderId() == null) {
//            throw new SagaStepException("Проблемы с БД: не могу создать запись заказа.");      // ошибка создания заказа, возможно БД недоступна
//        }
    }

    @Override
    public void rollback(SagaContext context) {
        if (context.getOrderId() != null) {
            Order order = orderRepository.findById(context.getOrderId()).orElse(null);
            if (order != null) {
                // меняем статус заказа на "отменен"
                order.setStatus(OrderStatus.Cancelled);
                orderRepository.save(order);
            }
            context.setOrderId(null);
        }
//        informationService.sendMessage("Заказ отменен. Попробуйте попозже.");
    }



}
