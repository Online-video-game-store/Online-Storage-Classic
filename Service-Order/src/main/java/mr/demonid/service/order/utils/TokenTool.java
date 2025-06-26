package mr.demonid.service.order.utils;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;


@Service
@Log4j2
public class TokenTool {

    /**
     * Возвращает токен текущего пользователя.
     * Null - если пользователь не аутентифицирован.
     */
    public String getCurrentToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken jwtToken) {
            // Пользователь авторизован через Jwt
            Jwt jwt = jwtToken.getToken();
            return jwt.getTokenValue();
        } else if (authentication.getPrincipal() instanceof DefaultOidcUser oidcUser) {
            // Пользователь авторизован через OIDC
            return oidcUser.getIdToken().getTokenValue();
        }
        // Пользователь не авторизован
        log.error("TokenTool.getToken(): This user is anonymous");
        return null;
    }

}
