package mr.demonid.service.catalog.services;

import lombok.AllArgsConstructor;
import mr.demonid.service.catalog.domain.ProductEntity;
import mr.demonid.service.catalog.dto.ProduceFilter;
import mr.demonid.service.catalog.dto.ProductResponse;
import mr.demonid.service.catalog.repositories.ProductRepository;
import mr.demonid.service.catalog.services.filters.ProductSpecification;
import mr.demonid.service.catalog.utils.Mappers;
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
    private Mappers mappers;


    /**
     * Возвращает постраничный список товаров.
     */
    @Transactional(readOnly = true)
    public Page<ProductResponse> getProductsWithoutEmpty(ProduceFilter produceFilter, Pageable pageable) {
        Page<ProductEntity> items = productRepository.findAll(ProductSpecification.filter(produceFilter, false), pageable);
        return items.map(mappers::entityToProductResponse);
    }

    /**
     * Возвращает информацию по конкретному товару.
     * @param productId Идентификатор товара.
     */
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long productId) {
         Optional<ProductEntity> opt = productRepository.findByIdWithCategory(productId);
        return opt.map(mappers::entityToProductResponse).orElse(null);
    }

}
