package mr.demonid.service.catalog.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@Log4j2
public class SecurityConfig {


    /**
     * Цепочка фильтров безопасности.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults()) // Разрешаем CORS
                .csrf(AbstractHttpConfigurer::disable)                      // Отключаем CSRF для запросов API
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET, "/pk8000/api/catalog/images/**").permitAll()
                        .requestMatchers("/pk8000/api/catalog/products/**").permitAll()
                        .requestMatchers("/pk8000/api/catalog/edit/**").hasAnyRole("ADMIN", "DEVELOPER")
                        .anyRequest().authenticated()              // Остальные требуют аутентификации
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jt -> jt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                )
                .formLogin(AbstractHttpConfigurer::disable)    // Отключаем перенаправление на форму входа
                .httpBasic(AbstractHttpConfigurer::disable);   // Отключаем Basic Auth

        return http.build();
    }

    /**
     * Извлекает из полей запроса значения ROLE и SCOPE.
     */
    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("SCOPE_");       // Для scope
        grantedAuthoritiesConverter.setAuthoritiesClaimName("scope");   // Из поля "scope"

        JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
        authenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Set<GrantedAuthority> authorities = new HashSet<>(grantedAuthoritiesConverter.convert(jwt));
            // добавляем все роли
            List<String> roles = jwt.getClaimAsStringList("authorities"); // Из поля "authorities"
            if (roles != null) {
                roles.forEach(role -> {
                    if (role.startsWith("ROLE_")) {
                        authorities.add(new SimpleGrantedAuthority(role)); // Роль уже с префиксом
                    } else {
                        authorities.add(new SimpleGrantedAuthority("ROLE_" + role)); // Добавляем префикс
                    }
                });
            }
            return authorities;
        });
        return authenticationConverter;
    }


}

