package mr.demonid.service.catalog.services.filters;

import jakarta.persistence.criteria.Predicate;
import mr.demonid.service.catalog.domain.ProductEntity;
import mr.demonid.service.catalog.dto.ProduceFilter;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    public static Specification<ProductEntity> filter(ProduceFilter produceFilter, boolean isIncludeEmpty) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (!isIncludeEmpty) {
                predicates.add(criteriaBuilder.greaterThan(root.get("stock"), 0));
            }
            if (produceFilter.getCategoryId() != null && produceFilter.getCategoryId() > 0) {
                predicates.add(criteriaBuilder.equal(root.get("category").get("id"), produceFilter.getCategoryId()));
            }
            if (produceFilter.getProductName() != null && !produceFilter.getProductName().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + produceFilter.getProductName().toLowerCase() + "%"));
            }
            if (produceFilter.getMinPrice() != null && produceFilter.getMinPrice().compareTo(BigDecimal.ZERO) > 0) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), produceFilter.getMinPrice()));
            }
            if (produceFilter.getMaxPrice() != null && produceFilter.getMaxPrice().compareTo(BigDecimal.ZERO) >0) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), produceFilter.getMaxPrice()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
