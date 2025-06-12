package mr.demonid.service.catalog.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mr.demonid.service.catalog.dto.ProduceFilter;
import mr.demonid.service.catalog.dto.ProductResponse;
import mr.demonid.service.catalog.services.CategoryService;
import mr.demonid.service.catalog.services.ProductService;
import mr.demonid.store.commons.dto.PageDTO;
import mr.demonid.store.commons.dto.CategoryResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/pk8000/api/catalog/products")
@AllArgsConstructor
public class ProductController {

    private ProductService productService;
    private CategoryService categoryService;


    /**
     * Возвращает постраничный список доступных товаров.
     */
    @PostMapping("/get-all")
    public ResponseEntity<PageDTO<ProductResponse>> getAllProducts(@RequestBody ProduceFilter filter, Pageable pageable) {
        return ResponseEntity.ok(new PageDTO<>(productService.getProductsWithoutEmpty(filter, pageable)));
    }
// http://localhost:9100/pk8000/api/catalog/get-all?&page=0&size=10&sort=name,asc

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


}
