package mr.demonid.service.payment.repository;

import mr.demonid.service.payment.domain.PaymentLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface PaymentLogRepository extends JpaRepository<PaymentLog, Long>, JpaSpecificationExecutor<PaymentLog> {
    PaymentLog findByOrderId(UUID orderId);

}
