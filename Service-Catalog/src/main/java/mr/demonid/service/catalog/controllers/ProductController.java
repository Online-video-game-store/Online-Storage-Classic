package mr.demonid.service.catalog.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mr.demonid.osc.commons.dto.PageDTO;
import mr.demonid.osc.commons.dto.catalog.CatalogReserveRequest;
import mr.demonid.osc.commons.dto.catalog.CategoryResponse;
import mr.demonid.service.catalog.dto.ProduceFilter;
import mr.demonid.service.catalog.dto.ProductResponse;
import mr.demonid.service.catalog.services.CategoryService;
import mr.demonid.service.catalog.services.ProductService;
import mr.demonid.service.catalog.services.ReservedService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/pk8000/api/catalog/products")
@AllArgsConstructor
public class ProductController {

    private ProductService productService;
    private ReservedService reservedService;
    private CategoryService categoryService;


    /**
     * Возвращает постраничный список доступных товаров.
     */
    @PostMapping("/get-all")
    public ResponseEntity<PageDTO<ProductResponse>> getAllProducts(@RequestBody ProduceFilter filter, Pageable pageable) {
        return ResponseEntity.ok(new PageDTO<>(productService.getProductsWithoutEmpty(filter, pageable)));
    }


    /**
     * Возвращает продукт по его ID.
     */
    @GetMapping("/get-product/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        ProductResponse dto = productService.getProductById(id);
        return dto == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
    }


    /**
     * Возвращает список категорий
     */
    @GetMapping("/get-categories")
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }


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
