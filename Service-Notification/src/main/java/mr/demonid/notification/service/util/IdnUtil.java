package mr.demonid.notification.service.util;

import jakarta.servlet.http.Cookie;
import lombok.AllArgsConstructor;
import mr.demonid.notification.service.config.AppConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class IdnUtil {

    private AppConfiguration config;


    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal());
    }

    /**
     * Возвращает идентификатор авторизированного пользователя.
     */
    public UUID getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtToken) {
            return UUID.fromString(jwtToken.getToken().getClaimAsString(config.getClaimUserId()));
        } else if (authentication.getPrincipal() instanceof DefaultOidcUser oidcUser) {
            return UUID.fromString(oidcUser.getIdToken().getClaimAsString(config.getClaimUserId()));
        }
        return null;
    }

    public String getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication == null ? null : authentication.getName();
    }

}
