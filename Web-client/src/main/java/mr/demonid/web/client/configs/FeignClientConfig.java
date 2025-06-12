package mr.demonid.web.client.configs;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.form.spring.SpringFormEncoder;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import feign.codec.Encoder;

import java.util.Set;

@Configuration
@Slf4j
public class FeignClientConfig {

    private static final Set<String> TARGET_COOKIES = Set.of("ANON_ID");    // потом добавим еще куков

    @Bean
    public Encoder feignFormEncoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        return new SpringFormEncoder(new SpringEncoder(messageConverters));
    }

    /**
     * Вставляем в Feign-запрос Jwt-Токен и куки, поскольку сам Feign этого делать не умеет.
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            // Переносим нужные куки в Feign-запрос.
            copyCookies(requestTemplate);
            // Получаем текущую аутентификацию из SecurityContext
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication instanceof JwtAuthenticationToken jwtToken) {
                // Пользователь авторизован через Jwt
                Jwt jwt = jwtToken.getToken();
                requestTemplate.header("Authorization", "Bearer " + jwt.getTokenValue());
//                log.info("-- RequestInterceptor(): JWT token: {}", jwt.getTokenValue());
            } else if (authentication.getPrincipal() instanceof DefaultOidcUser oidcUser) {
                // Пользователь авторизован через OIDC
                String tokenValue = oidcUser.getIdToken().getTokenValue();
                requestTemplate.header("Authorization", "Bearer " + tokenValue);
//                log.info("-- RequestInterceptor(): OidcUser: {}", oidcUser.getIdToken().getTokenValue());
//            } else {
//                // Пользователь не авторизован
//                log.info("-- RequestInterceptor(): Anonymous");
            }
        };
    }


    public void copyCookies(RequestTemplate requestTemplate) {
        // Получаем текущий HTTP-запрос
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            // Извлекаем все куки из запроса
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if (TARGET_COOKIES.contains(cookie.getName())) {
                        // нашли заданный, переносим в Feign
                        requestTemplate.header("Cookie", cookie.getName() + "=" + cookie.getValue());
                    }
                }
            }
        }
    }

}

