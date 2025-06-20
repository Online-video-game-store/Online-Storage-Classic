package mr.demonid.service.catalog.services;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.service.catalog.domain.ProductLogEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mr.demonid.service.catalog.domain.ReservationStatus;
import mr.demonid.service.catalog.domain.ReservedProductEntity;
import mr.demonid.service.catalog.domain.ProductEntity;
import mr.demonid.service.catalog.exceptions.CatalogException;
import mr.demonid.service.catalog.exceptions.NotAvailableException;
import mr.demonid.service.catalog.repositories.ProductRepository;
import mr.demonid.service.catalog.repositories.ReservedProductRepository;

import mr.demonid.osc.commons.dto.cart.CartItemResponse;
import mr.demonid.osc.commons.dto.cart.CartNeededResponse;
import mr.demonid.osc.commons.dto.catalog.ProductTransferred;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Слой сервиса по резервированию товаров для последующей оплаты или отмены резерва.
 */
@Service
@Transactional
@AllArgsConstructor
@Log4j2
public class ReservedService {
    private final ProductRepository productRepository;
    private final ReservedProductRepository reservedRepository;
    private final ProductLogService productLogService;


    /**
     * Резервирование товаров для совершения покупки.
     * В случае отсутствия какого-либо товара, или недостаточного количества, бросит исключение.
     */
    @Transactional
    public void reserve(UUID orderId, List<CartItemResponse> items) throws CatalogException {
        List<CartNeededResponse> products = new ArrayList<>();
        List<ProductLogEntity> logEntities = new ArrayList<>();

        items.forEach(item -> {
            ProductEntity productEntity = productRepository.findByIdWithCategory(item.getProductId()).orElse(null);
            logEntities.add(new ProductLogEntity(
                    null,
                    orderId,
                    item.getProductId(),
                    productEntity == null ? "" : productEntity.getName(),
                    item.getQuantity(),
                    productEntity == null ? BigDecimal.ZERO : productEntity.getPrice(),
                    ReservationStatus.CREATED
            ));
            if (productEntity == null) {
                products.add(new CartNeededResponse(
                        item.getProductId(), "",
                        item.getQuantity(),
                        0));
                log.error("Product with id {} not found", item.getProductId());
            } else if (productEntity.getStock() < item.getQuantity()) {
                products.add(new CartNeededResponse(
                        item.getProductId(),
                        productEntity.getName(),
                        item.getQuantity(),
                        productEntity.getStock()));
                log.error("Not enough stock! Request = {}, in stock = {}", item.getQuantity(), productEntity.getStock());
            } else {
                // резервируем товар
                productEntity.setStock(productEntity.getStock() - item.getQuantity());
                productRepository.save(productEntity);
                reservedRepository.save(new ReservedProductEntity(null, orderId, productEntity.getId(), item.getQuantity(), productEntity.getPrice()));
            }
        });
        productLogService.save(logEntities);
        if (!products.isEmpty()) {
            throw new NotAvailableException(products);  // отменяем транзакцию.
        }
    }


    /**
     * Отмена резерва товара.
     */
    @Transactional
    public void cancelReserved(UUID orderId) {
        List<ReservedProductEntity> reservedProductEntity = proofOfPurchaseOrder(orderId);
        if (reservedProductEntity != null) {
            for (ReservedProductEntity item : reservedProductEntity) {
                ProductEntity productEntity = productRepository.findByIdWithCategory(item.getProductId()).orElse(null);
                if (productEntity != null) {
                    // возвращаем товар на место
                    productEntity.setStock(productEntity.getStock() + item.getQuantity());
                    productRepository.save(productEntity);
                }
            }
            productLogService.setStatus(orderId, ReservationStatus.CANCELLED);
        }
    }

    /**
     * Списание товара из резерва.
     * Возвращает список этих товаров.
     */
    public List<ProductTransferred> approvedReservation(UUID orderId) {
        List<ReservedProductEntity> reservedProductEntity = proofOfPurchaseOrder(orderId);
        if (reservedProductEntity != null) {
            // да собственно больше ничего и не нужно делать, разве что в историю отправить.
            productLogService.setStatus(orderId, ReservationStatus.APPROVED);
            return reservedProductEntity.stream().map(e -> new ProductTransferred(e.getProductId(), e.getQuantity())).toList();
        }
        return List.of();
    }


    /*
        Подтверждение покупки.
     */
    private List<ReservedProductEntity> proofOfPurchaseOrder(UUID orderId) {
        List<ReservedProductEntity> reservedProductEntity = reservedRepository.findAllByOrderId(orderId);
        if (reservedProductEntity != null) {
            for (ReservedProductEntity item : reservedProductEntity) {
                reservedRepository.deleteById(item.getId());
            }
        }
        return reservedProductEntity;
    }

}
