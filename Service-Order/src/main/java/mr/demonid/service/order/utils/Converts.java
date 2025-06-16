package mr.demonid.service.order.utils;


import mr.demonid.osc.commons.dto.cart.CartItemResponse;
import mr.demonid.osc.commons.dto.catalog.CatalogReserveRequest;
import mr.demonid.osc.commons.dto.payment.PaymentRequest;
import mr.demonid.service.order.domain.Order;
import mr.demonid.service.order.dto.OrderResponse;
import mr.demonid.service.order.saga.SagaContext;

public class Converts {


    public static CatalogReserveRequest sagaToCatalogReserveRequest(SagaContext context) {
        return new CatalogReserveRequest(
                context.getOrderId(),
                context.getUserId(),
                context.getPaymentId(),
                context.getCardId(),
                context.getTotalAmount(),
                context.getItems().stream().map(e -> new CartItemResponse(e.getProductId(), e.getQuantity())).toList()
        );
    }

    public static PaymentRequest sagaToPaymentRequest(SagaContext context) {
        return new PaymentRequest(
                context.getOrderId(),
                context.getUserId(),
                context.getPaymentId(),
                context.getCardId(),
                context.getTotalAmount()
        );
    }

    public static OrderResponse orderToDto(Order order) {
        return  new OrderResponse(
                order.getOrderId(),
                order.getUserId(),
                order.getTotalAmount(),
                order.getPaymentId(),
                order.getCardId(),
                order.getCreatedAt(),
                order.getStatus()
        );
    }

    public static Order dtoToOrder(OrderResponse dto) {
        return Order.builder()
                .orderId(dto.getOrderId())
                .userId(dto.getUserId())
                .paymentId(dto.getPaymentId())
                .cardId(dto.getCardId())
                .createdAt(dto.getCreatedAt())
                .totalAmount(dto.getTotalAmount())
                .status(dto.getStatus())
                .build();
    }

}
