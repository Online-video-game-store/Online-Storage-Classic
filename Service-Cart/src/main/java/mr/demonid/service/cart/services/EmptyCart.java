package mr.demonid.service.cart.services;

import mr.demonid.service.cart.dto.CartItemResponse;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Заглушка, на случай если пользователя нельзя опознать.
 */
@Component
public class EmptyCart implements Cart {
    @Override
    public CartItemResponse addItem(Long productId, int quantity) {
        return null;
    }

    @Override
    public void removeItem(Long productId) {
    }

    @Override
    public List<CartItemResponse> getItems() {
        return List.of();
    }

    @Override
    public int getQuantity() {
        return 0;
    }

    @Override
    public void clearCart() {

    }
}
