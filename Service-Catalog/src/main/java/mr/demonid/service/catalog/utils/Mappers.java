package mr.demonid.service.catalog.utils;


import mr.demonid.service.catalog.domain.ProductCategoryEntity;
import mr.demonid.service.catalog.domain.ProductEntity;
import mr.demonid.service.catalog.domain.ProductLogEntity;
import mr.demonid.service.catalog.dto.ProductLogResponse;
import mr.demonid.service.catalog.dto.ProductRequest;
import mr.demonid.service.catalog.dto.ProductResponse;
import mr.demonid.service.catalog.dto.events.CatalogReserveRequestEvent;
import mr.demonid.service.catalog.dto.events.PaymentRequestEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Predicate;


@Service
public class Converts {

    @Value("${app.images-url}")
    private String imagesUrl;


    public PaymentRequestEvent createdToPayment(CatalogReserveRequestEvent event) {
        return new PaymentRequestEvent(
                event.getOrderId(),
                event.getUserId(),
                event.getPaymentId(),
                event.getCardId(),
                event.getTotalAmount()
        );
    }


    public ProductResponse entityToProductResponse(ProductEntity entity) {
        return new ProductResponse(
                entity.getId(),
                entity.getName(),
                entity.getPrice(),
                entity.getCategory().getId(),
                entity.getStock(),
                entity.getDescription(),
                entity.getImageFiles().stream()
                        .filter(Objects::nonNull)
                        .filter(Predicate.not(String::isBlank))
                        .map(e -> Paths.get(imagesUrl, entity.getId().toString(), e).toString())
                        .toList()
        );
    }

    public ProductEntity requestProductToEntity(ProductRequest product, ProductCategoryEntity category) {
        return new ProductEntity(
                null,
                product.getName(),
                product.getPrice(),
                product.getStock(),
                category,
                product.getDescription(),
                new ArrayList<>()
        );
    }

    public ProductLogResponse productLogToResponse(ProductLogEntity entity) {
        return new ProductLogResponse(
                entity.getOrderId(),
                entity.getName(),
                entity.getQuantity(),
                entity.getPrice(),
                entity.getReservationStatus()
        );
    }

}
