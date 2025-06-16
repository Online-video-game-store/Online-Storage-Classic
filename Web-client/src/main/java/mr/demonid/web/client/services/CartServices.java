package mr.demonid.web.client.services;

import mr.demonid.osc.commons.dto.cart.CartItemResponse;
import mr.demonid.web.client.dto.CartItem;

import java.util.List;
import java.util.UUID;

public interface CartServices {

    /**
     * Добавление товара в корзину.
     */
    CartItemResponse addItem(Long productId, Integer quantity);

    /**
     * Список всех товаров в корзине текущего пользователя.
     */
    List<CartItem> getItems();

    /**
     * Список товаров без подробностей, просто ID и количество.
     */
    List<CartItemResponse> getSimpleItems();

    /**
     * Удаление товара из корзины.
     */
    void removeItem(Long productId);

    /**
     * Полная очистка корзины.
     */
    void clearCart();

    /**
     * Количество всех товаров в корзине.
     */
    int getCountItems();

    /**
     * Аутентификация. Перевод из анонима в юзера.
     */
    boolean authUser(UUID anonId, UUID userId);

}
