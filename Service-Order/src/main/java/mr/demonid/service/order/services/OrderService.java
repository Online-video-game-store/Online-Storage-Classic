package mr.demonid.service.order.services;

import mr.demonid.osc.commons.dto.order.OrderCreateRequest;
import mr.demonid.osc.commons.dto.order.filter.OrderFilter;
import mr.demonid.service.order.dto.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;


public interface OrderService {

    void createOrder(OrderCreateRequest request);

    OrderResponse getOrder(UUID orderId);
    void deleteOrder(UUID orderId);

    Page<OrderResponse> getAllOrders(OrderFilter filter, Pageable pageable);
}
