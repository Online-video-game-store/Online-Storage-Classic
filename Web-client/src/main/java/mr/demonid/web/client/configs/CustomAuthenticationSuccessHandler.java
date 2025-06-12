package mr.demonid.web.client.configs;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mr.demonid.web.client.services.CartServices;
import mr.demonid.web.client.utils.IdnUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

/**
 * Перенос данных анонимного пользователя в его авторизированный
 * профиль.
 */
@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final String COOKIE_NAME = "ANON_ID";

    CartServices cartService;

    public CustomAuthenticationSuccessHandler(CartServices cartService) {
        this.cartService = cartService;
    }

    /**
     * Этот фильтр вызывается если аутентификация пользователя прошла успешно,
     * можем сделать что-то с его данными (если нужно). Или
     * просто разослать извещение всем заинтересованным сервисам.
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        Cookie anonCookie = IdnUtil.getCookie(COOKIE_NAME, request.getCookies());
        if (anonCookie != null) {
            // Переносим данные из анонимного контекста в авторизованный
            cartService.authUser(UUID.fromString(anonCookie.getValue()), IdnUtil.getUserId());

            // Удаляем куки после успешной авторизации
            anonCookie.setMaxAge(0);
            anonCookie.setPath("/");
            response.addCookie(anonCookie);
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }

}