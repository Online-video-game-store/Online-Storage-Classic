package mr.demonid.service.catalog.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mr.demonid.osc.commons.dto.catalog.CatalogReserveRequest;
import mr.demonid.service.catalog.services.ReservedService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@Slf4j
@RestController
@RequestMapping("/pk8000/api/catalog/reservation")
@AllArgsConstructor
public class ReservationController {

    private ReservedService reservedService;


    /**
     * Резервирование товара.
     * @param request Параметры запроса (код товара, кто резервирует, сколько)
     */
    @PostMapping("/reserve")
    public ResponseEntity<String> reserveCatalog(@RequestBody CatalogReserveRequest request) {
        log.info("-- резервируем товар: {}", request);
        reservedService.reserve(request.getOrderId(), request.getItems());
        return ResponseEntity.ok("Товар зарезервирован.");
    }

    /**
     * Отмена резерва.
     */
    @PostMapping("/cancel")
    public ResponseEntity<Void> unblock(@RequestBody UUID orderId) {
        log.warn("-- отмена резерва товара: {}", orderId);
        reservedService.cancelReserved(orderId);
        return ResponseEntity.ok().build();
    }

    /**
     * Завершение заказа, списываем его из резерва.
     */
    @PostMapping("/approved")
    public ResponseEntity<Void> approve(@RequestBody UUID orderId) {
        log.info("-- отдаем товар в службу комплектации и доставки: {}", orderId);
        reservedService.approvedReservation(orderId);
        return ResponseEntity.ok().build();
    }

}
