package mr.demonid.web.client.configs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) {
        log.info("start oauth service");
        OAuth2User oauth2User = new DefaultOAuth2UserService().loadUser(request);
        Map<String, Object> attributes = oauth2User.getAttributes();

        // Извлекаем "authorities" из JWT или UserInfo (в зависимости от поставщика)
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

        log.info("User Authorities: {}", authorities);

        return new DefaultOAuth2User(authorities, attributes, "sub");
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
