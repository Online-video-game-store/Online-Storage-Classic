package mr.demonid.web.client.controllers;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.osc.commons.dto.PageDTO;
import mr.demonid.osc.commons.dto.catalog.ProductResponse;
import mr.demonid.web.client.configs.AppConfiguration;
import mr.demonid.web.client.dto.ProductRequest;
import mr.demonid.web.client.dto.logs.ProductLogResponse;
import mr.demonid.web.client.services.ProductServices;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@Log4j2
@RequestMapping("/pk8000/catalog/api/product")
public class AdminController {

    private ProductServices productServices;
    private AppConfiguration appConfiguration;


    /**
     * Добавление нового продукта.
     */
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DEVELOPER')")
    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@ModelAttribute ProductRequest product) {
        return productServices.createProduct(product);
    }

    /**
     * Обновление данных о продукте.
     */
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DEVELOPER')")
    @PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProduct(@ModelAttribute ProductRequest product) {
        return productServices.updateProduct(product);
    }

    /**
     * Удаление продукта.
     */
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DEVELOPER')")
    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable long id) {
        return productServices.deleteProduct(id);
    }


    /**
     * Получение списка изображений продукта.
     */
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DEVELOPER')")
    @GetMapping("/{id}")
    public ResponseEntity<List<String>> listImages(@PathVariable Long id) {
        ProductResponse res = productServices.getProductById(id);
        if (res == null) {
            return ResponseEntity.notFound().build();
        }
        String gateway = appConfiguration.getGatewayUrl();
        List<String> fixedUrl = res.getImageUrls().stream()
                .map(url -> url.replace("\\", "/"))
                .map(url -> gateway + url)
                .toList();
        return ResponseEntity.ok(fixedUrl);
    }


    /**
     * Загрузка на сервер нового изображения, или замена старому.
     * @param id              Продукт
     * @param file            Новый файл.
     * @param replaceFileName Имя старого файла, или null (если просто добавляем новый файл)
     * @return                Проксируем ответ удаленного сервера во фронтенд.
     */
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DEVELOPER')")
    @PostMapping("/{id}/upload")
    public ResponseEntity<?> uploadImage(@PathVariable Long id,
                                              @RequestPart("file") MultipartFile file,
                                              @RequestParam(value = "replace", required = false) String replaceFileName) {
        return productServices.uploadImage(id, file, replaceFileName);
    }


    /**
     * Удаление изображения.
     * @param productId Продукт
     * @param fileName  Имя удаляемого файла.
     */
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DEVELOPER')")
    @DeleteMapping("/image/{productId}/{fileName}")
    public ResponseEntity<?> deleteImage(
            @PathVariable Long productId,
            @PathVariable String fileName) {
        return productServices.deleteImage(productId, fileName);
    }


    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DEVELOPER')")
    @GetMapping("/statistics/get-products-form-order/{orderId}")
    public ResponseEntity<List<ProductLogResponse>> getProductsFormOrder(@PathVariable UUID orderId) {
        return ResponseEntity.ok(productServices.getOrderDetails(orderId));
    }


}
