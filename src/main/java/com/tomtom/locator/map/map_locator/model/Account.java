package com.tomtom.locator.map.map_locator.model;

import com.tomtom.locator.map.map_locator.security.model.Credentials;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "accounts")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Account extends AbstractEntity implements UserDetails {

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

    @OneToMany
    @Getter
    private List<LocationMatch> locationMatches;

    public static Account withEmailAndCredentials(@NonNull String email, @NonNull Credentials credentials) {
        return new Account(false, credentials.login(), email, credentials.password(), EnumSet.of(AccountRole.TENANT), AccountState.NOT_VERIFIED, new ArrayList<>());
    }

    public void addLocationMatch(@NonNull LocationMatch locationMatch) {
        locationMatches.add(locationMatch);
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
