package mr.demonid.service.cart.services;

import mr.demonid.service.cart.dto.CartItemResponse;

import java.util.List;

public interface Cart {

    /**
     * Добавление элемента в корзину текущего пользователя.
     * @param productId Идентификатор товара
     * @param quantity  Количество товара
     */
    CartItemResponse addItem(Long productId, int quantity);

    /**
     * Удаление товара из корзины текущего пользователя.
     * @param productId Идентификатор товара
     */
    void removeItem(Long productId);

    /**
     * Возвращает полный список товаров в корзине текущего пользователя.
     */
    List<CartItemResponse> getItems();

    /**
     * Возвращает количество всех товаров в корзине текущего пользователя.
     */
    int getQuantity();

    /**
     * Очистка корзины текущего пользователя.
     */
    void clearCart();
}
