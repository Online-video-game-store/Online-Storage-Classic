package mr.demonid.service.order.controller;

import lombok.AllArgsConstructor;
import mr.demonid.osc.commons.dto.PageDTO;
import mr.demonid.osc.commons.dto.order.OrderCreateRequest;
import mr.demonid.osc.commons.dto.order.filter.OrderFilter;
import mr.demonid.service.order.dto.OrderResponse;
import mr.demonid.service.order.services.OrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@AllArgsConstructor
@RestController
@RequestMapping("/pk8000/api/order")
public class OrderController {

    private OrderService orderService;


    /**
     * Оформление и проведение заказа.
     */
    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestBody OrderCreateRequest orderCreateRequest) {
        orderService.createOrder(orderCreateRequest);
        return ResponseEntity.ok().build();
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
    @PostMapping("/get_orders")
    public ResponseEntity<PageDTO<OrderResponse>> getOrders(@RequestBody OrderFilter orderFilter, Pageable page) {
        return ResponseEntity.ok(new PageDTO<>(orderService.getAllOrders(orderFilter, page)));
    }

}
