package mr.demonid.service.cart.services;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.service.cart.dto.CartItemResponse;
import mr.demonid.service.cart.utils.TokenTools;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Фабрика корзин.
 * Возвращает определенный тип корзины, в зависимости от
 * текущей стратегии (пользователя).
 */
@Service
@AllArgsConstructor
@Log4j2
public class CartFactory {

    private final ObjectProvider<AnonCart> anonCartProvider;
    private final ObjectProvider<AuthCart> authCartProvider;
    private final EmptyCart emptyCart;
    private TokenTools tokenTools;

    /**
     * Возвращает корзину текущего пользователя.
     */
    public Cart getCart(HttpServletRequest request) {
        if (tokenTools.isAnonymous()) {
            UUID id = tokenTools.getAnonymousId(request);
            log.info("-- Anon with ID = {}", id);
            if (id != null) {
                // создаем новый экземпляр корзины
                AnonCart anonCart = anonCartProvider.getObject();
                anonCart.setUserId(id);
                return anonCart;
            }
            return emptyCart;   // кто-то без идентификатора
        }
        UUID id = tokenTools.getUserIdFromToken();
        if (id != null) {
            log.info("-- User with ID = {}", id);
            // создаем новый экземпляр корзины для аутентифицированного пользователя
            AuthCart authCart = authCartProvider.getObject();
            authCart.setUserId(id);
            return authCart;
        }
        return emptyCart;   // кто-то без идентификатора
    }

    /**
     * Корректирует данные авторизировавшегося пользователя, перенося его
     * товары с анонимной корзины в БД.
     * @param anonId Идентификатор до авторизации
     */
    public void registerUser(UUID anonId, UUID userId) {
        // инициализируем объекты
        AnonCart anonCart = anonCartProvider.getObject();
        anonCart.setUserId(anonId);
        AuthCart authCart = authCartProvider.getObject();
        authCart.setUserId(userId);

        List<CartItemResponse> source = anonCart.getItems();
        anonCart.clearCart();
        // Отправляем в корзину авторизированного пользователя, т.е. в БД
        if (source != null && !source.isEmpty()) {
            source.forEach(e -> authCart.addItem(e.getProductId(), e.getQuantity()));
        }
    }

}
