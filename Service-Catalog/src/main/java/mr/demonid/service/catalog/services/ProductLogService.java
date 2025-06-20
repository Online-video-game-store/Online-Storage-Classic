package mr.demonid.service.catalog.services;


import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import mr.demonid.service.catalog.domain.ProductLogEntity;
import mr.demonid.service.catalog.domain.ReservationStatus;
import mr.demonid.service.catalog.domain.ReservedProductEntity;
import mr.demonid.service.catalog.dto.ProductLogResponse;
import mr.demonid.service.catalog.repositories.ProductLogRepository;
import mr.demonid.service.catalog.utils.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;


@Service
@AllArgsConstructor
@Log4j2
public class ProductLogService {

    private ProductLogRepository productLogRepository;
    private ProductService productService;
    private Mappers mappers;


    @Transactional(propagation = REQUIRES_NEW)
    public List<ProductLogEntity> save(List<ProductLogEntity> productLogEntities) {
        try {
            List<ProductLogEntity> res =  productLogRepository.saveAll(productLogEntities);
            return res;
        } catch (Exception e) {
            log.error("ProductLogService save failed: {}", e.getMessage());
            return null;
        }
    }

    public void setStatus(UUID orderId, ReservationStatus status) {
        List<ProductLogEntity> logEntities = productLogRepository.findByOrderId(orderId).orElse(null);
        if (logEntities != null) {
            for (ProductLogEntity product : logEntities) {
                product.setReservationStatus(status);
            }
            productLogRepository.saveAll(logEntities);
        } else {
            log.error("ProductLogService setStatus failed: order {} not found", orderId);
        }
    }

    /**
     * Возвращает список товаров для конкретного заказа.
     * @param orderId Заказ.
     */
    public List<ProductLogResponse> getProductsOnOrderId(UUID orderId) {
        List<ProductLogEntity> products = productLogRepository.findByOrderId(orderId).orElse(null);
        if (products != null) {
            return products.stream().map(mappers::productLogToResponse).toList();
        }
        return List.of();
    }

}
