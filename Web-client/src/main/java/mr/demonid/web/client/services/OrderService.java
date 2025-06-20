package mr.demonid.web.client.services;

import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.osc.commons.dto.PageDTO;
import mr.demonid.osc.commons.dto.cart.CartItemResponse;
import mr.demonid.osc.commons.dto.order.OrderCreateRequest;
import mr.demonid.web.client.dto.filters.OrderFilter;
import mr.demonid.web.client.dto.orders.OrderResponse;
import mr.demonid.web.client.dto.payment.PaymentRequest;
import mr.demonid.web.client.exceptions.CreateOrderException;
import mr.demonid.web.client.links.OrderServiceClient;
import mr.demonid.web.client.utils.IdnUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Log4j2
public class OrderService {

    private CartServices cartServices;
    private OrderServiceClient orderServiceClient;
    private IdnUtil idnUtil;


    public PageDTO<OrderResponse> getAllOrders(OrderFilter filter, Pageable page) {
        try {
            return orderServiceClient.getOrders(filter, page).getBody();
        } catch (FeignException e) {
            log.error("Feign exception: {}",e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
            return PageDTO.empty();
        }
    }

    public ResponseEntity<?> createOrder(PaymentRequest request) {
        log.info("-- Creating order with payment: {}", request);

        if (request.getPaymentMethodId() == null) {
            throw new CreateOrderException("Выберите платежную систему");
        }
        UUID userId = idnUtil.getUserId();
        OrderCreateRequest order = getOrderCreateRequest(request, userId);
        log.info("-- Order created: {}", order);
        // отсылаем заказ
        return orderServiceClient.createOrder(order);
    }


    private OrderCreateRequest getOrderCreateRequest(PaymentRequest request, UUID userId) {
        if (userId == null) {
            throw new CreateOrderException("Нет активного пользователя");
        }
        // получаем товары из корзины
        List<CartItemResponse> list = cartServices.getSimpleItems();
        if (list.isEmpty()) {
            throw new CreateOrderException("Корзина пуста");
        }
        return new OrderCreateRequest(
                userId,
                request.getPaymentMethodId(),
                request.getCardId(),
                list
        );
    }


}
