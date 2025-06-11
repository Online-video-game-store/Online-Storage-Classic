package mr.demonid.service.cart.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import mr.demonid.service.cart.dto.CartItemResponse;
import mr.demonid.service.cart.services.Cart;
import mr.demonid.service.cart.services.CartFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Контроллер корзины.
 * Данные по авторизированным пользователям извлекаются из токена Jwt.
 * Для анонимных пользователей используются куки.
 */
@RestController
@Slf4j
@RequestMapping("/pk8000/api/public/cart")
public class CartController {

    @Autowired
    private CartFactory cartFactory;


    /**
     * Добавление товара в корзину.
     * @param productId Товар
     * @param quantity  Количество
     * @param request   Запрос, для идентификации пользователя.
     */
    @PostMapping("/add")
    public ResponseEntity<CartItemResponse> addItem(@RequestParam Long productId, @RequestParam Integer quantity, HttpServletRequest request) {

        log.info("-- cart add product = {}, quantity = {}", productId, quantity);
        Cart cart = cartFactory.getCart(request);
        return ResponseEntity.ok(cart.addItem(productId, quantity));
    }

    /**
     * Возвращает список товаров в корзине пользователя.
     */
    @GetMapping("/read")
    public ResponseEntity<List<CartItemResponse>> getItems(HttpServletRequest request) {
        Cart cart = cartFactory.getCart(request);
        return ResponseEntity.ok(cart.getItems());
    }

    /**
     * Возвращает общее количество товаров в корзине.
     */
    @GetMapping("/count")
    public ResponseEntity<Integer> getItemQuantity(HttpServletRequest request) {
        Cart cart = cartFactory.getCart(request);
        return ResponseEntity.ok(cart.getQuantity());
    }

    /**
     * Очищает корзину.
     */
    @GetMapping("/clear")
    public ResponseEntity<Void> clearCart(HttpServletRequest request) {
        Cart cart = cartFactory.getCart(request);
        cart.clearCart();
        return ResponseEntity.ok().build();
    }

    /**
     * Удаление товара из корзины.
     */
    @PostMapping("/remove")
    public ResponseEntity<Void> removeItem(@RequestParam Long productId, HttpServletRequest request) {
        Cart cart = cartFactory.getCart(request);
        cart.removeItem(productId);
        return ResponseEntity.ok().build();
    }

    /**
     * Регистрирует пользователя.
     * @param anonId Идентификатор пользователя до авторизации.
     * @param userId Текущий идентификатор пользователя.
     */
    @PostMapping("/register-user")
    public ResponseEntity<Void> registerUser(@RequestParam UUID anonId, @RequestParam UUID userId) {
        log.info("-- Registering anon {} to user with id {}", anonId, userId);
        cartFactory.registerUser(anonId, userId);
        return ResponseEntity.ok().build();
    }

}
