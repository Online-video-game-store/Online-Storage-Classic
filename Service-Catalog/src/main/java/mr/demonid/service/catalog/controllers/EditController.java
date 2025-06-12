package mr.demonid.service.catalog.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.service.catalog.dto.ProduceFilter;
import mr.demonid.service.catalog.dto.ProductLogResponse;
import mr.demonid.service.catalog.dto.ProductRequest;
import mr.demonid.service.catalog.dto.ProductResponse;
import mr.demonid.service.catalog.services.ProductAdminService;
import mr.demonid.service.catalog.services.ProductLogService;
import mr.demonid.store.commons.dto.PageDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;


@RestController
@AllArgsConstructor
@Log4j2
@RequestMapping("/pk8000/api/catalog/edit")
public class EditController {

    private ProductAdminService productAdminService;
    private ProductLogService productLogService;


    /**
     * Возвращает постраничный список товаров, включая и тех, которых нет в наличии.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
    @PostMapping("/get-all")
    public ResponseEntity<PageDTO<ProductResponse>> getAllProducts(@RequestBody ProduceFilter filter, Pageable pageable) {
        return ResponseEntity.ok(new PageDTO<>(productAdminService.getAllProducts(filter, pageable)));
    }

    /**
     * Создание нового товара.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@RequestBody ProductRequest product) {
        productAdminService.createProduct(product);
        return ResponseEntity.ok().build();
    }

    /**
     * Обновление данных о товаре.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
    @PostMapping("/update")
    public ResponseEntity<?> updateProduct(@RequestBody ProductRequest product) {
        productAdminService.updateProduct(product);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
    @DeleteMapping("/delete/{id}")
    ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productAdminService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }
    /**
     * Добавление нового или замена существующего изображения.
     * @param productId       Продукт.
     * @param file            Новый файл.
     * @param replaceFileName Имя существующего файла или null.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
    @PostMapping("/{productId}/upload")
    public ResponseEntity<?> uploadImage(@PathVariable Long productId,
                                              @RequestPart("file") MultipartFile file,
                                              @RequestParam(value = "replace", required = false) String replaceFileName) {
        productAdminService.updateImage(productId, file, replaceFileName);
        return ResponseEntity.ok().build();
    }

    /**
     * Удаление изображения.
     * @param productId Продукт.
     * @param fileName  Имя удаляемого файла
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
    @DeleteMapping("/{productId}/delete")
    public ResponseEntity<?> deleteImage(@PathVariable Long productId, @RequestParam String fileName) throws IOException {
        productAdminService.deleteImage(productId, fileName);
        return ResponseEntity.ok(true);
    }


    /**
     * Возвращает детали операции по конкретному заказу.
     * Ошибок не генерирует, просто возвращает пустой список.
     * @param orderId Номер заказа.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
    @GetMapping("/get-order/{orderId}")
    public List<ProductLogResponse> getOrder(@PathVariable UUID orderId) {
         return productLogService.getProductsOnOrderId(orderId);
    }

}
