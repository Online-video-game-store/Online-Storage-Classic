package mr.demonid.service.catalog.utils;


import com.rabbitmq.client.LongString;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
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
     * Извлекает из заголовка Message токен.
     */
    public String getToken(Message<?> message) {
        MessageHeaders headers = message.getHeaders();
        Object headerValue = headers.get("Authorization");

        if (headerValue instanceof LongString longStringValue) {
            return longStringValue.toString();

        } else if (headerValue instanceof String stringValue) {
            return stringValue;
        } else {
            log.error("TokenTool.getToken(): Can't get Jwt-token");
            return "";
        }
    }

    /**
     * Возвращает токен текущего пользователя.
     * null - если пользователь не аутентифицирован.
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
        log.error("TokenTool.getCurrentToken(): This user is anonymous");
        return null;
    }

}
