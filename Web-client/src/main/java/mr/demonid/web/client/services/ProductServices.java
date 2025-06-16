package mr.demonid.web.client.services;

import mr.demonid.osc.commons.dto.PageDTO;
import mr.demonid.osc.commons.dto.catalog.CategoryResponse;
import mr.demonid.osc.commons.dto.catalog.ProductResponse;
import mr.demonid.web.client.dto.filters.ProductFilter;
import mr.demonid.web.client.dto.logs.ProductLogResponse;
import mr.demonid.web.client.dto.ProductRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface ProductServices {
    PageDTO<ProductResponse> getProductsWithoutEmpty(ProductFilter filter, Pageable pageable);
    PageDTO<ProductResponse> getAllProducts(ProductFilter filter, Pageable pageable);
    List<CategoryResponse> getAllCategories();
    ProductResponse getProductById(Long id);

    ResponseEntity<?> createProduct(ProductRequest product);
    ResponseEntity<?> updateProduct(ProductRequest product);
    ResponseEntity<?> deleteProduct(Long productId);
    ResponseEntity<?> uploadImage(Long productId, MultipartFile file, String replaceFileName);
    ResponseEntity<?> deleteImage(Long productId, String fileName);
    List<ProductLogResponse> getOrderDetails(UUID orderId);
}
