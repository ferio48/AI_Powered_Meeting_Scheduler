package com.gemini.aichatbot.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.gemini.aichatbot.enums.Permission.*;

/**
 * Enumeration representing roles in the system.
 *
 * Each role is associated with a set of permissions.
 * The role can also generate a list of {@link SimpleGrantedAuthority}
 * objects for Spring Security's authorization system.
 */
@RequiredArgsConstructor
public enum Role {

    /**
     * Regular user role with full user-related permissions.
     */
    USER(
            Set.of(
                    USER_READ,
                    USER_CREATE,
                    USER_UPDATE,
                    USER_DELETE
            )
    );

    /**
     * Permissions granted to the role.
     */
    @Getter
    private final Set<Permission> permissions;

    /**
     * Returns a list of authorities granted to this role.
     *
     * This includes all permission strings and an additional authority prefixed with "ROLE_".
     *
     * @return a list of {@link SimpleGrantedAuthority} representing this role's authorities
     */
    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());

        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        return authorities;
    }
}
