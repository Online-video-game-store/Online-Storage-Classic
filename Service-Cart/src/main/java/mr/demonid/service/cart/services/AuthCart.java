package mr.demonid.service.cart.services;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import mr.demonid.service.cart.domain.CartItem;
import mr.demonid.service.cart.dto.CartItemResponse;
import mr.demonid.service.cart.repositories.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Корзина авторизированного пользователя.
 * Для каждого пользователя свой экземпляр, поскольку в нем хранится userId.
 */
@Slf4j
@Component
@Scope("prototype")
public class AuthCart implements Cart {

    @Autowired
    private CartRepository cartRepository;      // внедряем БД

    @Setter
    private UUID userId;


    @Override
    @Transactional
    public CartItemResponse addItem(Long productId, int quantity) {
        CartItem item = cartRepository.findByUserIdAndProductId(userId, productId);
        if (item == null) {
            item = new CartItem(userId, productId, 0);
        }
        item.setQuantity(item.getQuantity() + quantity);
        item = cartRepository.save(item);
        return new CartItemResponse(item.getProductId(), item.getQuantity());
    }

    @Override
    @Transactional
    public void removeItem(Long productId) {
        try {
            cartRepository.deleteByUserIdAndProductId(userId, productId);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public List<CartItemResponse> getItems() {
        List<CartItem> items = cartRepository.findByUserId(userId);
        return items.stream().map(item -> new CartItemResponse(item.getProductId(), item.getQuantity())).toList();
    }

    @Override
    public int getQuantity() {
        List<CartItemResponse> cart = getItems();
        return cart.stream().mapToInt(CartItemResponse::getQuantity).sum();
    }

    @Override
    @Transactional
    public void clearCart() {
        List<CartItem> items = cartRepository.findByUserId(userId);
        cartRepository.deleteAll(items);
    }

}
