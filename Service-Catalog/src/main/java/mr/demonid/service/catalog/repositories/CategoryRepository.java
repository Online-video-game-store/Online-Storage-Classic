package mr.demonid.service.catalog.repositories;

import mr.demonid.service.catalog.domain.ProductCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<ProductCategoryEntity, Long> {

    ProductCategoryEntity findByName(String name);
}
