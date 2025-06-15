package mr.demonid.web.client.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.web.client.dto.*;
import mr.demonid.web.client.services.CartServices;
import mr.demonid.web.client.utils.IdnUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@Log4j2
@RequestMapping("/pk8000/catalog/api")
public class ApiController {

    private CartServices cartServices;
    private IdnUtil idnUtil;

    /**
     * Проверка аутентификации пользователя.
     */
    @GetMapping("/check")
    public ResponseEntity<Void> check() {
        if (idnUtil.isAuthenticated()) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }


    /**
     * Добавление товара в корзину
     */
    @PostMapping("/add-to-cart")
    public ResponseEntity<?> addToCart(@RequestBody CartItemResponse request) {
        CartItemResponse res = cartServices.addItem(request.getProductId(), request.getQuantity());
        if (res != null) {
            return ResponseEntity.ok(Map.of("success", true, "message", ""));
        }
        return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Не удалось добавить товар в корзину. Попробуйте позже."));
    }

    /**
     * Возвращает кол-во товаров в корзине.
     */
    @GetMapping("/get-cart-count")
    public ResponseEntity<Integer> getCartCount() {
        return ResponseEntity.ok(cartServices.getCountItems());
    }


    /**
     * Возвращает список товаров в корзине.
     */
    @GetMapping("/get-cart-items")
    private ResponseEntity<?> getCartItems() {
        List<CartItem> res = cartServices.getItems();
        return ResponseEntity.ok(res);
    }


    /**
     * Удаляет товар из корзины по его ID.
     */
    @DeleteMapping("/remove-cart-item/{itemId}")
    public ResponseEntity<?> removeCartItem(@PathVariable Long itemId) {
        if (itemId == null) {
            return ResponseEntity.badRequest().build();
        }
        cartServices.removeItem(itemId);
        return ResponseEntity.ok().build();
    }

}

