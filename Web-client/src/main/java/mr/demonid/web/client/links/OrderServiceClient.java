package mr.demonid.web.client.links;

import mr.demonid.osc.commons.dto.PageDTO;
import mr.demonid.osc.commons.dto.order.OrderCreateRequest;
import mr.demonid.web.client.dto.filters.OrderFilter;
import mr.demonid.web.client.dto.orders.OrderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "ORDER-SERVICE", url = "http://localhost:9010/pk8000/api/order")
public interface OrderServiceClient {

    @PostMapping("/create-order")
    ResponseEntity<?> createOrder(@RequestBody OrderCreateRequest orderCreateRequest);

    @PostMapping("/get_orders")
    ResponseEntity<PageDTO<OrderResponse>> getOrders(@RequestBody OrderFilter orderFilter, Pageable page);

}
