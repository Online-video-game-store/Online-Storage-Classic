package mr.demonid.web.client.services;

import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.osc.commons.dto.PageDTO;
import mr.demonid.web.client.dto.CartItem;
import mr.demonid.web.client.dto.CartItemResponse;
import mr.demonid.web.client.dto.filters.OrderFilter;
import mr.demonid.web.client.dto.orders.OrderResponse;
import mr.demonid.web.client.dto.payment.PaymentRequest;
import mr.demonid.web.client.dto.orders.OrderCreateRequest;
import mr.demonid.web.client.exceptions.CreateOrderException;
import mr.demonid.web.client.links.OrderServiceClient;
import mr.demonid.web.client.utils.FeignErrorUtils;
import mr.demonid.web.client.utils.IdnUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Log4j2
public class OrderService {

    private CartServices cartServices;
    private OrderServiceClient orderServiceClient;
    private IdnUtil idnUtil;


    public ResponseEntity<?> createOrder(PaymentRequest request) {
        log.info("-- Creating order with payment: {}", request);

        if (request.getPaymentMethodId() == null) {
            throw new CreateOrderException("Выберите платежную систему");
        }
        UUID userId = idnUtil.getUserId();
        if (userId == null) {
            throw new CreateOrderException("Нет активного пользователя");
        }
        // получаем товары из корзины и вычисляем общую сумму покупки
        List<CartItem> items = cartServices.getItems();
        List<CartItemResponse> list = items.stream().map(e -> new CartItemResponse(e.getProductId(), e.getQuantity())).toList();
        BigDecimal totalAmount = items.stream().map(CartItem::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (list.isEmpty()) {
            throw new CreateOrderException("Корзина пуста");
        }
        OrderCreateRequest order = new OrderCreateRequest(
                userId,
                request.getPaymentMethodId(),
                request.getCardId(),
                totalAmount,
                list
        );
        log.info("-- Order created: {}", order);
        // отсылаем заказ
        try {
            return orderServiceClient.createOrder(order);
        } catch (FeignException e) {
            return FeignErrorUtils.toResponse(e, "Ошибка микросервиса Order-Service");
        }
    }


    public PageDTO<OrderResponse> getAllOrders(OrderFilter filter, Pageable page) {
        try {
            return orderServiceClient.getOrders(filter, page).getBody();
        } catch (FeignException e) {
            log.error("Feign exception: {}",e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
            return PageDTO.empty();
        }
    }

}
