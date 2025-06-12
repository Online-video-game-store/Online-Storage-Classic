package mr.demonid.service.catalog.repositories;

import mr.demonid.service.catalog.domain.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long>, JpaSpecificationExecutor<ProductEntity> {

    /**
     * Выборка товара по его ID.
     */
    @Query("SELECT p FROM ProductEntity p JOIN FETCH p.category WHERE p.id = :id")
    Optional<ProductEntity> findByIdWithCategory(@Param("id") Long id);
}
