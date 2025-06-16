package mr.demonid.service.order.links;

import mr.demonid.osc.commons.dto.catalog.CatalogReserveRequest;
import mr.demonid.osc.commons.dto.catalog.ProductResponse;
import mr.demonid.service.order.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;


/**
 * Эти сервисы вызываем напрямую через Eureka,
 * поскольку нет нужды напрасно перегружать Gateway.
 */
@FeignClient(name = "CATALOG-SERVICE", configuration = FeignClientConfig.class)
public interface CatalogServiceClient {

    /**
     * Резервирование товара.
     * @param request Параметры запроса (код товара, кто резервирует, сколько)
     */
    @PostMapping("/pk8000/api/catalog/reservation/reserve")
    ResponseEntity<String> reserveCatalog(@RequestBody CatalogReserveRequest request);

    /**
     * Отмена резерва.
     */
    @PostMapping("/pk8000/api/catalog/reservation/cancel")
    ResponseEntity<Void> unblock(@RequestBody UUID orderId);

    /**
     * Завершение заказа, списываем его из резерва.
     */
    @PostMapping("/pk8000/api/catalog/reservation/approved")
    ResponseEntity<Void> approve(@RequestBody UUID orderId);


    @GetMapping("/pk8000/api/catalog/products/get-product/{id}")
    ResponseEntity<ProductResponse> getProductById(@PathVariable Long id);

}

