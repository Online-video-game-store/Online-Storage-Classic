package mr.demonid.web.client.configs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class CustomOidcUserService implements OAuth2UserService<OidcUserRequest, OidcUser> {

    private final OidcUserService delegate = new OidcUserService();

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("start oidc user service");

        // Получаем стандартного OidcUser
        OidcUser oidcUser = delegate.loadUser(userRequest);

        Map<String, Object> attributes = oidcUser.getAttributes();

        List<String> roles = extractStringList(attributes.get("authorities"));
        List<String> scopes = extractStringList(attributes.get("scope"));

        Set<GrantedAuthority> authorities = Stream.concat(
                roles.stream()
                        .filter(Objects::nonNull)
                        .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                        .map(SimpleGrantedAuthority::new),
                scopes.stream()
                        .filter(Objects::nonNull)
                        .map(scope -> scope.startsWith("SCOPE_") ? scope : "SCOPE_" + scope)
                        .map(SimpleGrantedAuthority::new)
        ).collect(Collectors.toSet());

        log.info("User OIDC Authorities: {}", authorities);

        // Важно: вернуть OidcUser, а не просто OAuth2User
        return new DefaultOidcUser(authorities, oidcUser.getIdToken(), oidcUser.getUserInfo(), "sub");
    }


    private List<String> extractStringList(Object claim) {
        if (claim instanceof List<?> list) {
            // фильтруем, чтобы оставить только строки
            return list.stream()
                    .filter(item -> item instanceof String)
                    .map(String.class::cast)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

}
