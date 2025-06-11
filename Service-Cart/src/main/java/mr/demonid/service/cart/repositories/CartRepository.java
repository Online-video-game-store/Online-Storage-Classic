package mr.demonid.service.cart.repositories;

import mr.demonid.service.cart.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<CartItem, Long> {

    CartItem findByUserIdAndProductId(UUID userId, Long productId);

    void deleteByUserIdAndProductId(UUID userId, Long productId);

    List<CartItem> findByUserId(UUID userId);

    void deleteAllByUserId(UUID userId);

}
