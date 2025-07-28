package com.tomtom.locator.map.map_locator.mok.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "accounts")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Getter @ToString.Include @EqualsAndHashCode.Include
    private UUID id;

    @Version
    @Getter @ToString.Include
    private long version;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    @ToString.Include
    private boolean archived = false;

    @Column(unique = true, nullable = false, updatable = false)
    @ToString.Include @EqualsAndHashCode.Include
    private String login;

    @Column(unique = true, nullable = false)
    @Getter @ToString.Include
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @ToString.Include
    private Set<AccountRole> roles;

    @Enumerated(EnumType.STRING)
    @ToString.Include
    private AccountState state;

    public static Account withEmailAndCredentials(@NonNull String email, @NonNull Credentials credentials) {
        return new Account(null, 0, false, credentials.login(), email, credentials.password(), EnumSet.of(AccountRole.TENANT), AccountState.NOT_VERIFIED);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.copyOf(roles);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !state.equals(AccountState.LOCKED);
    }

    @Override
    public boolean isEnabled() {
        return !archived && state.equals(AccountState.ACTIVE);
    }

    public void lock() {
        state = AccountState.LOCKED;
    }

    public void activate() {
        state = AccountState.ACTIVE;
    }

    public void archive() {
        archived = true;
    }
}
