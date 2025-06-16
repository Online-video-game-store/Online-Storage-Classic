package mr.demonid.gateway.server.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Извлекает из токена список привилегий и возвращает их в виде объекта GrantedAuthority.
 * Если дошли сюда, то токен уже проверен на подлинность через JWK или секретный ключ.
 *
 * После этого метода роли добавляются в SecurityContext и их можно использовать
 * для проверки авторизации, например через аннотации:
 * '@PreAuthorize("hasRole('USER')")'
 * '@GetMapping("/user")' ... или на уровне фильтров доступных адресов.
 */
public class CustomJwtAuthenticationConverter {

    /**
     * Возвращает реактивный адаптер стандартного JwtAuthenticationConverter.
     */
    public static Converter<Jwt, Mono<AbstractAuthenticationToken>> create() {
        JwtGrantedAuthoritiesConverter scopeConverter = new JwtGrantedAuthoritiesConverter();
        scopeConverter.setAuthorityPrefix("SCOPE_");
        scopeConverter.setAuthoritiesClaimName("scope");

        JwtAuthenticationConverter delegate = new JwtAuthenticationConverter();
        delegate.setJwtGrantedAuthoritiesConverter(jwt -> {
            Set<GrantedAuthority> authorities = new HashSet<>(scopeConverter.convert(jwt));
            List<String> roles = jwt.getClaimAsStringList("authorities");
            if (roles != null) {
                for (String role : roles) {
                    if (role.startsWith("ROLE_")) {
                        authorities.add(new SimpleGrantedAuthority(role));
                    } else {
                        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
                    }
                }
            }
            return authorities;
        });

        return new ReactiveJwtAuthenticationConverterAdapter(delegate);
    }
}
