package mr.demonid.service.order.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class CustomJwtAuthenticationConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private final JwtGrantedAuthoritiesConverter delegate = new JwtGrantedAuthoritiesConverter();


    public CustomJwtAuthenticationConverter() {
        delegate.setAuthorityPrefix("SCOPE_");
        delegate.setAuthoritiesClaimName("scope");
    }

    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {
        Set<GrantedAuthority> authorities = new HashSet<>(delegate.convert(source));

        // Обрабатываем поле "authorities" (с ролями)
        List<String> roles = source.getClaimAsStringList("authorities");
        if (roles != null) {
            authorities.addAll(roles.stream()
                    .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet()));
        }

        return authorities;
    }
}
