package mr.demonid.service.order.services.filters;

import jakarta.persistence.criteria.Predicate;
import mr.demonid.osc.commons.dto.order.filter.OrderFilter;
import mr.demonid.service.order.domain.Order;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderSpecifications {

    public static Specification<Order> filter(OrderFilter filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // настраиваем диапазон дат, по дефолту последние сутки
            LocalDateTime to = filter.getEndTime() != null ? filter.getEndTime() : LocalDateTime.now();
            LocalDateTime from = filter.getStartTime() != null ? filter.getStartTime() : to.minusDays(1);
            predicates.add(criteriaBuilder.between(root.get("createdAt"), from, to));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
