package mr.demonid.gateway.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    /**
     * Настройка потока безопасности (цепочки фильтров). Обрабатывает все входящие запросы.
     * @param http Объект, предоставляющий API для настройки безопасности.
     */
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(
                                "/pk8000/api/catalog/**",
                                "/auth/**"
                        ).permitAll()                               // разрешаем доступ к эндпоинтам аутентификации
                        .anyExchange().authenticated()              // остальные запросы требуют аутентификации
                )
                .oauth2ResourceServer(oauth2 -> oauth2              // настраиваем обработку OAuth2-токенов, в данном случае Jwt
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(reactiveJwtAuthenticationConverter()) // Настройка преобразователя токенов
                        )
                )
//                // если нам не нужна централизованная полная проверка Jwt-Токенов и использование authorities в Gateway:
//                .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()))
                .csrf(ServerHttpSecurity.CsrfSpec::disable);    // отключаем для всех запросов

        return http.build();
    }

    /**
     * Извлекает из токена список привилегий и возвращает их в виде объекта GrantedAuthority.
     * Если дошли сюда, то токен уже проверен на подлинность через JWK или секретный ключ.
     *
     * После этого метода роли добавляются в SecurityContext и их можно использовать
     * для проверки авторизации, например через аннотации:
     * '@PreAuthorize("hasRole('USER')")'
     * '@GetMapping("/user")' ... или на уровне фильтров доступных адресов.
     */
    @Bean
    public Converter<Jwt, Mono<AbstractAuthenticationToken>> reactiveJwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter scopeConverter = new JwtGrantedAuthoritiesConverter();
        scopeConverter.setAuthorityPrefix("SCOPE_");
        scopeConverter.setAuthoritiesClaimName("scope");

        JwtAuthenticationConverter delegate = new JwtAuthenticationConverter();
        delegate.setJwtGrantedAuthoritiesConverter(jwt -> {
            Set<GrantedAuthority> authorities = new HashSet<>(scopeConverter.convert(jwt));
            List<String> roles = jwt.getClaimAsStringList("authorities");
            if (roles != null) {
                roles.forEach(role -> {
                    if (role.startsWith("ROLE_")) {
                        authorities.add(new SimpleGrantedAuthority(role));
                    } else {
                        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
                    }
                });
            }
            return authorities;
        });

        return new ReactiveJwtAuthenticationConverterAdapter(delegate);
    }

}
