package mr.demonid.service.payment.repository;

import mr.demonid.service.payment.domain.PaymentMethod;
import org.springframework.context.annotation.ReflectiveScan;
import org.springframework.data.jpa.repository.JpaRepository;


@ReflectiveScan
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
}
