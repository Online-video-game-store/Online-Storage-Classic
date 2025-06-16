package mr.demonid.service.order.services;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.osc.commons.dto.cart.CartItemResponse;
import mr.demonid.osc.commons.dto.catalog.ProductResponse;
import mr.demonid.osc.commons.dto.order.OrderCreateRequest;
import mr.demonid.osc.commons.dto.order.filter.OrderFilter;
import mr.demonid.service.order.domain.Order;
import mr.demonid.service.order.domain.OrderStatus;
import mr.demonid.service.order.dto.OrderResponse;
import mr.demonid.service.order.exceptions.CreateOrderException;
import mr.demonid.service.order.links.CatalogServiceClient;
import mr.demonid.service.order.repository.OrderRepository;
import mr.demonid.service.order.saga.*;
import mr.demonid.service.order.services.filters.OrderSpecifications;
import mr.demonid.service.order.utils.Converts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Log4j2
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;
    private CatalogServiceClient catalogServiceClient;


    @Override
    public void createOrder(OrderCreateRequest request) {
        if (request.getPaymentId() == null || request.getUserId() == null || request.getItems() == null || request.getItems().isEmpty()) {
            throw new CreateOrderException("Неверные параметры для заказа.");
        }
        // вычисляем общую сумму покупки
        List<SagaContextItems> list = request.getItems().stream()
                .map(e -> new SagaContextItems(
                        e.getProductId(),
                        e.getQuantity()))
                .toList();
        if (list.isEmpty()) {
            throw new CreateOrderException("Корзина пуста");
        }
        SagaContext context = new SagaContext(
                null,
                request.getUserId(),
                request.getPaymentId(),
                request.getCardId(),
                getTotalAmount(request),
                list);

        // Задаем последовательность действий.
        SagaOrchestrator<SagaContext> orchestrator = new SagaOrchestrator<>();
        orchestrator.addStep(new CreateOrderStep(orderRepository, catalogServiceClient));  // Шаг 1. открываем заказ
//        orchestrator.addStep(new ProductReservationStep(catalogServiceClient));     // резервируем товар
//        orchestrator.addStep(new PaymentTransferStep(paymentServiceClient));        // Списываем средства в пользу магазина
//        orchestrator.addStep(new ApprovedStep(orderRepository, catalogServiceClient)); // завершение сделки
//        orchestrator.addStep(new InformationStep(informationService));                                // оповещаем пользователя

        // Запускаем выполнение и возвращаем результат.
        orchestrator.execute(context);
    }

    @Override
    public OrderResponse getOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            return Converts.orderToDto(order);
        }
        return null;
    }

    @Override
    public void deleteOrder(UUID orderId) {
        orderRepository.deleteById(orderId);
    }

    @Override
    public Page<OrderResponse> getAllOrders(OrderFilter filter, Pageable pageable) {
        Page<Order> items = orderRepository.findAll(OrderSpecifications.filter(filter), pageable);
        return items.map(Converts::orderToDto);
    }


    private BigDecimal getTotalAmount(OrderCreateRequest  context) {
        BigDecimal total = context.getItems().stream()
                .map(e -> {
                    ProductResponse product = catalogServiceClient.getProductById(e.getProductId()).getBody();
                    if (product != null) {
                        return product.getPrice().multiply(new BigDecimal(e.getQuantity()));
                    }
                    return BigDecimal.ZERO;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        log.info("-- total amount = {}", total);
        return total;
    }

}
