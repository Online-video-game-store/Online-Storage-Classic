package mr.demonid.service.cart.services;

import lombok.Setter;
import mr.demonid.service.cart.domain.CartItem;
import mr.demonid.service.cart.dto.CartItemResponse;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Scope;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Корзина для анонимных пользователей.
 * Располагается в памяти и теряется при уходе пользователя.
 */
@Component
@Scope("prototype")
public class AnonCart implements Cart {

    private static final Map<UUID, List<CartItem>> cartItems = new ConcurrentHashMap<>();
    @Setter
    private UUID userId;


    @Override
    public CartItemResponse addItem(Long productId, int quantity) {
        List<CartItem> cart = cartItems.computeIfAbsent(userId, k -> new ArrayList<>());

        CartItem item = cart.stream().filter(cartItem -> cartItem.getProductId().equals(productId)).findFirst().orElse(null);
        if (item != null) {
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            item = new CartItem(userId, productId, quantity);
            cart.add(item);
        }
        return new CartItemResponse(item.getProductId(), item.getQuantity());
    }

    @Override
    public void removeItem(Long productId) {
        List<CartItem> cart = cartItems.get(userId);
        if (cart != null) {
            cart.removeIf(cartItem -> cartItem.getProductId().equals(productId));
            if (cart.isEmpty()) {
                cartItems.remove(userId);       // корзина текущего пользователя пуста, можно удалить её
            }
        }
    }

    @Override
    public List<CartItemResponse> getItems() {
        List<CartItem> items = cartItems.getOrDefault(userId, Collections.emptyList());
        return items.stream().map(e -> new CartItemResponse(e.getProductId(), e.getQuantity())).collect(Collectors.toList());
    }

    @Override
    public int getQuantity() {
        List<CartItem> cart = cartItems.getOrDefault(userId, Collections.emptyList());
        return cart.stream().mapToInt(CartItem::getQuantity).sum();
    }

    @Override
    public void clearCart() {
        cartItems.remove(userId);
    }

}
