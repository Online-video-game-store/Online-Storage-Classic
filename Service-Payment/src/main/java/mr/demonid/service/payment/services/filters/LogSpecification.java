package mr.demonid.service.payment.services.filters;

import mr.demonid.service.payment.domain.PaymentLog;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.criteria.Predicate;


public class LogSpecification {

    public static Specification<PaymentLog> filter(UUID userId) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (userId != null) {
                predicates.add(criteriaBuilder.equal(root.get("userId"), userId));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }



}
