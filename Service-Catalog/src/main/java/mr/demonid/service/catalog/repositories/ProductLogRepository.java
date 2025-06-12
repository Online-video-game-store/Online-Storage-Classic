package mr.demonid.service.catalog.repositories;

import mr.demonid.service.catalog.domain.ProductLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface ProductLogRepository extends JpaRepository<ProductLogEntity, Long> {

    Optional<List<ProductLogEntity>> findByOrderId(UUID orderId);

}
