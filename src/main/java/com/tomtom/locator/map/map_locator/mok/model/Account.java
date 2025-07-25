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
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Account implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Getter @EqualsAndHashCode.Include
    private UUID id;

    @Version
    @Getter
    private long version;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean archived = false;

    @Column(unique = true, nullable = false, updatable = false)
    @EqualsAndHashCode.Include
    private String login;

    @Column(unique = true, nullable = false)
    @Getter
    private String email;

    @Column(nullable = false)
    @ToString.Exclude
    private String password;

    @Enumerated(EnumType.STRING)
    private Set<AccountRole> roles;

    @Enumerated(EnumType.STRING)
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
