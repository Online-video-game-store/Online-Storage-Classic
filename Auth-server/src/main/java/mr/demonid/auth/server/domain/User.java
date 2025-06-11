package mr.demonid.auth.server.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Пользователь.
 * Замена стандартному UserDetails.
 * Содержит в себе роли и права для каждого пользователя.
 * Это позволяет администратору настраивать права индивидуально
 * для каждого пользователя.
 *
 */
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String email;

    @Column(nullable = false)
    private boolean enabled;

    // роли
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    // права
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "users_scopes", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "scope_id"))
    private Set<Scope> scopes = new HashSet<>();

    /**
     * Возвращает коллекцию ролей и прав пользователя (грантов).
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // добавляем роли
        Collection<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(addPrefixIfMissing(role.getName(), "ROLE_")))
                .collect(Collectors.toList());
        // добавляем права
        authorities.addAll(scopes.stream()
                .map(scope -> new SimpleGrantedAuthority(addPrefixIfMissing(scope.getName(), "SCOPE_")))
                .toList());
        return authorities;
    }

    private String addPrefixIfMissing(String value, String prefix) {
        return value.startsWith(prefix) ? value : prefix + value;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    /*
        Методы для правильного добавления/удаления ролей и прав пользователю.
     */
    public void addRole(Role role) {
        roles.add(role);
        role.getUsers().add(this);
    }

    public void removeRole(Role role) {
        roles.remove(role);
        role.getUsers().remove(this);
    }

    public void addScope(Scope scope) {
        scopes.add(scope);
        scope.getUsers().add(this);
    }

    public void removeScope(Scope scope) {
        scopes.remove(scope);
        scope.getUsers().remove(this);
    }


    /**
     * Возвращает строку
     * @return
     */
    public String getRolesList() {
        return roles.stream().map(Role::getName).toList().toString();
    }

    public String getScopesList() {
        return scopes.stream().map(Scope::getName).toList().toString();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", roles=" + getRolesList() +
                ", scopes=" + getScopesList() +
                '}';
    }
}
