package mr.demonid.service.cart.utils;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.UUID;

@Service
@AllArgsConstructor
@Log4j2
public class TokenTools {

    /**
     * Проверяет, является ли текущий пользователь анонимом.
     */
    public boolean isAnonymous() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    /**
     * Возвращает ID пользователя из поля "sub" Jwt-Токена.
     */
    public UUID getUserIdFromToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            log.info("  -- user name = {}", authentication.getName());
            Jwt jwt = (Jwt) authentication.getPrincipal();
            return UUID.fromString(jwt.getClaim("sub"));
        }
        log.info("  -- user name = null");
        return null;
    }

    /**
     * Возвращает ID пользователя из поля "anon_id" куков
     */
    public UUID getAnonymousId(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            return Arrays.stream(cookies)
                    .filter(cookie -> "ANON_ID".equals(cookie.getName()))
                    .map(e -> UUID.fromString(e.getValue()))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

}
