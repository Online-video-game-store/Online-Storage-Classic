package mr.demonid.service.catalog.services;


import lombok.AllArgsConstructor;
import mr.demonid.service.catalog.domain.ProductLogEntity;
import mr.demonid.service.catalog.domain.ReservationStatus;
import mr.demonid.service.catalog.domain.ReservedProductEntity;
import mr.demonid.service.catalog.dto.ProductLogResponse;
import mr.demonid.service.catalog.repositories.ProductLogRepository;
import mr.demonid.service.catalog.utils.Converts;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@AllArgsConstructor
public class ProductLogService {

    private ProductLogRepository productLogRepository;
    private ProductService productService;
    private Converts converts;


    public void store(List<ReservedProductEntity> products, ReservationStatus status) {
        if (products != null) {
            for (ReservedProductEntity product : products) {
                String name = productService.getProductById(product.getProductId()).getName();
                if (name != null) {
                    ProductLogEntity entity = new ProductLogEntity(
                            null,
                            product.getOrderId(),
                            product.getProductId(),
                            name,
                            product.getQuantity(),
                            product.getPrice(),
                            status
                    );
                    productLogRepository.save(entity);
                }
            }
        }
    }


    /**
     * Возвращает список товаров для конкретного заказа.
     * @param orderId Заказ.
     */
    public List<ProductLogResponse> getProductsOnOrderId(UUID orderId) {
        List<ProductLogEntity> products = productLogRepository.findByOrderId(orderId).orElse(null);
        if (products != null) {
            return products.stream().map(converts::productLogToResponse).toList();
        }
        return List.of();
    }


}
