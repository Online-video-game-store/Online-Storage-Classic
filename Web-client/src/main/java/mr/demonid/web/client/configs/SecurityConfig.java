package mr.demonid.web.client.configs;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextHolderFilter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private AnonymousCookieFilter anonymousCookieFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults()) // Разрешаем CORS
                .csrf(AbstractHttpConfigurer::disable)                      // Отключаем CSRF для запросов API
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/pk8000/catalog/**").permitAll()  // Главная и публичные ресурсы
                        .requestMatchers(HttpMethod.GET,"/pk8000/api/catalog/images/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/css/**", "/js/**").permitAll()
                        .anyRequest().authenticated()  // Остальные требуют аутентификации
                )
                .anonymous(Customizer.withDefaults()) // Включение анонимных пользователей
                .addFilterBefore(anonymousCookieFilter, UsernamePasswordAuthenticationFilter.class)
//                .sessionManagement(session ->
//                        session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS) // Создавать сессии (для анонимов)
//                )
                .oauth2Login(Customizer.withDefaults()); // Настройка OAuth2 Login
        return http.build();
    }

}
