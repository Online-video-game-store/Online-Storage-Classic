package mr.demonid.notification.service.service.events;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

/**
 * Сервис для валидации Jwt-токенов и созданию контекста безопасности для текущего потока.
 * Поскольку по дефолту контекст создается только для HTTP-запросов, придется сделать это самим.
 */
@Service
@Log4j2
public class JwtService {

    private final JwtDecoder jwtDecoder;


    public JwtService(@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String issuerUri) {
//        this.jwtDecoder = JwtDecoders.fromIssuerLocation(issuerUri);
        this.jwtDecoder = JwtDecoders.fromOidcIssuerLocation(issuerUri);
    }


    /**
     * Создает контекст безопасности для текущего потока.
     * @param token Jwt-токен, из которого и создадим контекст.
     */
    public boolean createSecurityContextFromJwt(String token) {
        try {
            Jwt jwt = jwtDecoder.decode(token.replace("Bearer ", ""));
            // создаем контекст безопасности для текущего потока
            Authentication authentication = new JwtAuthenticationToken(jwt);
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authentication);
            SecurityContextHolder.setContext(securityContext);
            return true;
        } catch (JwtException e) {
            log.error("Invalid Jwt-token: {}", e.getMessage());
        }
        return false;
    }


}
