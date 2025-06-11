package mr.demonid.auth.server.configs;

import jakarta.annotation.PostConstruct;
import mr.demonid.auth.server.domain.Role;
import mr.demonid.auth.server.domain.Scope;
import mr.demonid.auth.server.domain.User;
import mr.demonid.auth.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.security.oauth2.core.AuthorizationGrantType.*;
import static org.springframework.security.oauth2.core.ClientAuthenticationMethod.CLIENT_SECRET_BASIC;


/**
 * Конфигурация сервера авторизации
 *
 * http://localhost:8090/.well-known/openid-configuration
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private AuthorizationProperties properties;

    @Autowired
    private UserRepository userRepository;

    /**
     * Включаем в цепочку security свои AuthenticationProvider + UserDetailService + PasswordEncoder
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth, CustomAuthenticationProvider authenticationProvider) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }

    /**
     * Репозиторий для хранения зарегистрированных клиентов.
     *
     * @param jdbcTemplate объект подключения к БД.
     * @return репозиторий.
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcRegisteredClientRepository(jdbcTemplate);
    }

    /**
     * Регистрируем (добавляем в БД) клиента по умолчанию.
     */
    @Bean
    ApplicationRunner clientRunner(RegisteredClientRepository registeredClientRepository, CustomPasswordEncoder passwordEncoder) {
        return args -> {
            // Создание веб-клиента
            if (registeredClientRepository.findByClientId(properties.getClientId()) == null) {
                registeredClientRepository.save(RegisteredClient
                        .withId(UUID.randomUUID().toString())
                        .clientId(properties.getClientId())
                        .clientSecret(passwordEncoder.encode(properties.getClientSecret()))
                        .authorizationGrantType(AUTHORIZATION_CODE)
                        .authorizationGrantType(CLIENT_CREDENTIALS)
                        .authorizationGrantType(REFRESH_TOKEN)
                        .redirectUris(u -> u.addAll(properties.getClientUrls()))
                        .scope("openid")
                        .clientAuthenticationMethod(CLIENT_SECRET_BASIC)
                        .clientSettings(ClientSettings.builder()
                                .requireAuthorizationConsent(false)      // запрос пользователя разрешения на "read", "write" и тд.
                                .build())
                        .tokenSettings(TokenSettings.builder()
                                .accessTokenTimeToLive(Duration.ofMinutes(properties.getExpirationTime()))  // время действия токена
                                .build())
                        .build()
                );
            }
            // Создание клиента для программы, работающей без участия пользователя
            if (registeredClientRepository.findByClientId(properties.getApmId()) == null) {
                registeredClientRepository.save(RegisteredClient
                        .withId(UUID.randomUUID().toString())
                        .clientId(properties.getApmId())
                        .clientSecret(passwordEncoder.encode(properties.getApmSecret()))
                        .authorizationGrantType(CLIENT_CREDENTIALS)
                        .scope("read")
                        .scope("write")
                        .clientAuthenticationMethod(CLIENT_SECRET_BASIC)
                        .tokenSettings(TokenSettings.builder()
                                .accessTokenTimeToLive(Duration.ofMinutes(properties.getExpirationTime()))  // время действия токена
                                .build())
                        .build()
                );
            }
        };
    }


    /**
     * Зададим корневой URL нашего сервера.
     */
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .issuer(properties.getIssuerUrl())
                .build();
    }

    /**
     * Модифицируем Jwt-токен.
     * Для CLIENT_CREDENTIALS добавляем роль 'SERVICE'
     * Для остальных, добавляем в токен роли и права пользователя.
     */
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
        return context -> {
            if (context.getAuthorizationGrantType().getValue().equals("client_credentials")) {
                // Это токен для служебного микросервиса, назначаем ему роль "SERVICE"
                context.getClaims().claim("authorities", List.of("ROLE_SERVICE"));
            } else {
                // Похоже это авторизировался пользователь
                String username = context.getPrincipal().getName();
                userRepository.findByUsername(username).ifPresent(user -> {

                    // добавляем в токен роли
                    List<String> roles = user.getRoles().stream()
                            .map(Role::getName) // "ROLE_USER", "ROLE_ADMIN" и т.д.
                            .collect(Collectors.toList());
                    context.getClaims().claim("authorities", roles);

                    // добавляем в токен права
                    List<String> scopes = new ArrayList<>(context.getAuthorizedScopes().stream().toList());
                    List<String> userScopes = user.getScopes().stream().map(Scope::getName).toList();
                    scopes.addAll(userScopes);
                    context.getClaims().claim("scope", scopes);

                    // добавляем дополнительные данные о пользователе.
                    context.getClaims().claim("user_id", user.getId().toString());      // в поле "sub" есть имя пользователя
                    context.getClaims().claim("email", user.getEmail());
                });
            }
        };
    }

//    @PostConstruct
//    public void clientRunner() {
//        System.out.println("Client runner started");
//        Map<String, String> env = System.getenv();
//        env.forEach((key, value) -> System.out.println(key + " = " + value));
//    }

}