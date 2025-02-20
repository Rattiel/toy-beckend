package com.demo.auth.authorization.core;

import com.demo.auth.authorization.core.constant.MfaParameterNames;
import com.demo.auth.authorization.core.mfa.MfaDetails;
import lombok.Getter;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;

@Getter
public class Account implements UserDetails, CredentialsContainer, MfaDetails {
    private final String username;

    private String password;

    private final String email;

    private final String phone;

    private final String firstName;

    private final String lastName;

    private final boolean accountNonExpired;

    private final boolean accountNonLocked;

    private final boolean credentialsNonExpired;

    private final boolean enabled;

    private final String mfaMethod;

    private final Set<GrantedAuthority> authorities;

    public Account(
            String username,
            String password,
            String email,
            String phone,
            String firstName,
            String lastName,
            boolean enabled,
            boolean accountNonExpired,
            boolean credentialsNonExpired,
            boolean accountNonLocked,
            String mfaMethod,
            Collection<? extends GrantedAuthority> authorities) {
        Assert.isTrue(username != null && !username.isEmpty() && password != null,
                "Cannot pass null or empty values to constructor");
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.firstName = firstName;
        this.lastName = lastName;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.credentialsNonExpired = credentialsNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.mfaMethod = mfaMethod;
        this.authorities = Collections.unmodifiableSet(sortAuthorities(authorities));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public void eraseCredentials() {
        this.password = null;
    }

    @Override
    public String getVerificationMethod() {
        return this.mfaMethod;
    }

    @Override
    public String getVerificationAddress() {
        if (this.mfaMethod.equals(MfaParameterNames.PHONE)) {
            return this.phone;
        }
        if (this.mfaMethod.equals(MfaParameterNames.EMAIL)) {
            return this.email;
        }
        return null;
    }

    public static Builder withUsername(String username) {
        return builder().username(username);
    }

    public static Builder builder() {
        return new Builder();
    }

    private static SortedSet<GrantedAuthority> sortAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");
        SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet<>(new GrantedAuthorityComparator());
        for (GrantedAuthority grantedAuthority : authorities) {
            Assert.notNull(grantedAuthority, "GrantedAuthority list cannot contain any null elements");
            sortedAuthorities.add(grantedAuthority);
        }
        return sortedAuthorities;
    }

    public static final class Builder implements Serializable {
        private String username;

        private String password;

        private String email;

        private String phone;

        private String firstName;

        private String lastName;

        private boolean accountExpired;

        private boolean accountLocked;

        private boolean credentialsExpired;

        private boolean disabled;

        private String mfaMethod = null;

        private List<GrantedAuthority> authorities = new ArrayList<>();

        private Function<String, String> passwordEncoder = (password) -> password;

        private Builder() {
        }

        public Builder username(String username) {
            Assert.notNull(username, "username cannot be null");
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            Assert.notNull(password, "password cannot be null");
            this.password = password;
            return this;
        }

        public Builder passwordEncoder(Function<String, String> encoder) {
            Assert.notNull(encoder, "encoder cannot be null");
            this.passwordEncoder = encoder;
            return this;
        }

        public Builder email(String email) {
            Assert.notNull(email, "email cannot be null");
            this.email = email;
            return this;
        }

        public Builder phone(String phone) {
            Assert.notNull(phone, "phone cannot be null");
            this.phone = phone;
            return this;
        }

        public Builder firstName(String firstName) {
            Assert.notNull(firstName, "firstName cannot be null");
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            Assert.notNull(lastName, "lastName cannot be null");
            this.lastName = lastName;
            return this;
        }

        public Builder mfaMethod(String mfaMethod) {
            this.mfaMethod = mfaMethod;
            return this;
        }

        public Builder roles(String... roles) {
            List<GrantedAuthority> authorities = new ArrayList<>(roles.length);
            for (String role : roles) {
                Assert.isTrue(!role.startsWith("ROLE_"), () -> role + " cannot start with ROLE_ (it is automatically added)");
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
            }
            return this.authorities(authorities);
        }

        public Builder authorities(GrantedAuthority... authorities) {
            Assert.notNull(authorities, "authorities cannot be null");
            return this.authorities(Arrays.asList(authorities));
        }

        public Builder authorities(Collection<? extends GrantedAuthority> authorities) {
            Assert.notNull(authorities, "authorities cannot be null");
            this.authorities = new ArrayList<>(authorities);
            return this;
        }

        public Builder authorities(String... authorities) {
            Assert.notNull(authorities, "authorities cannot be null");
            return this.authorities(AuthorityUtils.createAuthorityList(authorities));
        }

        public Builder accountExpired(boolean accountExpired) {
            this.accountExpired = accountExpired;
            return this;
        }

        public Builder accountLocked(boolean accountLocked) {
            this.accountLocked = accountLocked;
            return this;
        }

        public Builder credentialsExpired(boolean credentialsExpired) {
            this.credentialsExpired = credentialsExpired;
            return this;
        }

        public Builder disabled(boolean disabled) {
            this.disabled = disabled;
            return this;
        }

        public Account build() {
            String encodedPassword = this.passwordEncoder.apply(this.password);
            return new Account(
                    this.username,
                    encodedPassword,
                    email,
                    phone,
                    firstName,
                    lastName,
                    !this.disabled,
                    !this.accountExpired,
                    !this.credentialsExpired,
                    !this.accountLocked,
                    this.mfaMethod,
                    this.authorities
            );
        }
    }
}
