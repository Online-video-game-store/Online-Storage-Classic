package mr.demonid.web.client.services;

import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.osc.commons.dto.PageDTO;
import mr.demonid.osc.commons.dto.catalog.CategoryResponse;
import mr.demonid.web.client.dto.filters.ProductFilter;
import mr.demonid.web.client.dto.logs.ProductLogResponse;
import mr.demonid.web.client.dto.ProductRequest;
import mr.demonid.web.client.dto.ProductResponse;
import mr.demonid.web.client.links.ProductServiceClient;
import mr.demonid.web.client.utils.FeignErrorUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

/**
 * Реализация интерфейса ProductServices
 */
@Service
@AllArgsConstructor
@Log4j2
public class ProductServiceImpl implements ProductServices {

    ProductServiceClient productServiceClient;


    @Override
    public PageDTO<ProductResponse> getProductsWithoutEmpty(ProductFilter filter, Pageable pageable) {
        try {
            return productServiceClient.getAllProductsWithoutEmpty(filter, pageable).getBody();
        } catch (FeignException e) {
            log.error("ProductServiceImpl.getProductsWithoutEmpty(): {}",e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
            return PageDTO.empty();
        }
    }

    @Override
    public PageDTO<ProductResponse> getAllProducts(ProductFilter filter, Pageable pageable) {
        try {
            return productServiceClient.getAllProducts(filter, pageable).getBody();
        } catch (FeignException e) {
            log.error("ProductServiceImpl.getAllProducts(): {}",e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
            return PageDTO.empty();
        }
    }

    @Override
    public ProductResponse getProductById(Long id) {
        try {
            return productServiceClient.getProductById(id).getBody();
        } catch (FeignException e) {
            log.error("ProductServiceImpl.getProductById(): {}",e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
            return null;
        }
    }


    @Override
    public List<CategoryResponse> getAllCategories() {
        try {
            return productServiceClient.getAllCategories().getBody();
        } catch (FeignException e) {
            log.error("ProductServiceImpl.getAllCategories(): {}",e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
            return List.of(new CategoryResponse(0L, "All", "Все категории"));
        }
    }


    /**
     * Добавление нового товара.
     * @return Просто проксирует ответ от удаленого микросервиса дальше.
     */
    @Override
    public ResponseEntity<?> createProduct(ProductRequest product) {
        try {
            return productServiceClient.createProduct(product);
        } catch (FeignException e) {
            return FeignErrorUtils.toResponse(e, "Ошибка микросервиса Catalog-Service");
        }
    }

    /**
     * Обновление данных о товаре.
     * @return Просто проксирует ответ от удаленого микросервиса дальше.
     */
    @Override
    public ResponseEntity<?> updateProduct(ProductRequest product) {
        try {
            return productServiceClient.updateProduct(product);
        } catch (FeignException e) {
            return FeignErrorUtils.toResponse(e, "Ошибка микросервиса Catalog-Service");
        }
    }

    @Override
    public ResponseEntity<?> deleteProduct(Long productId) {
        try {
            return productServiceClient.deleteProduct(productId);
        } catch (FeignException e) {
            return FeignErrorUtils.toResponse(e, "Ошибка микросервиса Catalog-Service");
        }
    }

    /**
     * Обновление или добавление картинки к товару.
     * @param productId       Продукт.
     * @param file            Новый файл для загрузки.
     * @param replaceFileName Заменяемый файл или null.
     * @return Просто проксирует ответ от удаленого микросервиса дальше.
     */
    @Override
    public ResponseEntity<?> uploadImage(Long productId, MultipartFile file, String replaceFileName) {
        try {
            return productServiceClient.uploadImage(productId, file, replaceFileName);
        } catch (FeignException e) {
            return FeignErrorUtils.toResponse(e, "Ошибка микросервиса Catalog-Service");
        }
    }

    /**
     * Удаление изображения на удаленном сервере-ресурсов.
     * @param productId Номер продукта.
     * @param fileName  Имя файла.
     */
    @Override
    public ResponseEntity<?> deleteImage(Long productId, String fileName) {
        try {
            return productServiceClient.deleteImage(productId, fileName);
        } catch (FeignException e) {
            return FeignErrorUtils.toResponse(e, "Ошибка микросервиса Catalog-Service");
        }
    }

    /**
     * Возвращает детализацию товаров заказа.
     * @param orderId Номер заказа.
     */
    @Override
    public List<ProductLogResponse> getOrderDetails(UUID orderId) {
        try {
            return productServiceClient.getOrderDetails(orderId);
        } catch (FeignException e) {
            log.error("Ошибка микросервиса Catalog-Service: {}",e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
        }
        return List.of();
    }


}
