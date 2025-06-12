package mr.demonid.service.catalog.repositories;

import mr.demonid.service.catalog.domain.ReservedProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Резервирование товаров.
 */
@Repository
public interface ReservedProductRepository extends JpaRepository<ReservedProductEntity, Long> {

    List<ReservedProductEntity> findAllByOrderId(UUID orderId);

}
