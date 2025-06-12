package mr.demonid.service.catalog.services;

import lombok.AllArgsConstructor;
import mr.demonid.service.catalog.domain.ProductCategoryEntity;
import mr.demonid.service.catalog.domain.ProductEntity;
import mr.demonid.service.catalog.dto.ProduceFilter;
import mr.demonid.service.catalog.dto.ProductRequest;
import mr.demonid.service.catalog.dto.ProductResponse;
import mr.demonid.service.catalog.exceptions.UpdateProductException;
import mr.demonid.service.catalog.repositories.CategoryRepository;
import mr.demonid.service.catalog.repositories.ProductRepository;
import mr.demonid.service.catalog.services.filters.ProductSpecification;
import mr.demonid.service.catalog.utils.Converts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Слой сервис по работе с БД товаров.
 */
@Service
@Transactional
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private Converts converts;


    /**
     * Возвращает постраничный список товаров.
     */
    @Transactional(readOnly = true)
    public Page<ProductResponse> getProductsWithoutEmpty(ProduceFilter produceFilter, Pageable pageable) {
        Page<ProductEntity> items = productRepository.findAll(ProductSpecification.filter(produceFilter, false), pageable);
        return items.map(converts::entityToProductResponse);
    }

    /**
     * Возвращает информацию по конкретному товару.
     * @param productId Идентификатор товара.
     */
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long productId) {
         Optional<ProductEntity> opt = productRepository.findByIdWithCategory(productId);
        return opt.map(converts::entityToProductResponse).orElse(null);
    }


//    /**
//     * Конвертирует файл в строку Base64.
//     */
//    private String encodeImageToBase64(String fileName) {
//        try {
//            // ClassPathResource строит путь из src/main/resource, независимо от того, упакован файл в JAR, или выполняется из IDEA.
//            ClassPathResource imgFile = new ClassPathResource("pics/" + fileName);
//            Path imagePath = imgFile.getFile().toPath();
//            // Читаем байты и кодируем в Base64
//            byte[] imageBytes = Files.readAllBytes(imagePath);
//            return Base64.getEncoder().encodeToString(imageBytes);
//
//        } catch (IOException e) {
//            return "";
//        }
//    }

}
