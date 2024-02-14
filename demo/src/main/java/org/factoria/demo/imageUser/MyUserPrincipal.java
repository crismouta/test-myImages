package org.factoria.demo.imageUser;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;

public class MyUserPrincipal implements UserDetails {

    private ImageUser imageUser;

    public MyUserPrincipal(ImageUser imageUser) {
        this.imageUser = imageUser;
    }

    public ImageUser getImageUser() {
        return imageUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(StringUtils.tokenizeToStringArray(this.imageUser.getRoles(), " "))
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .toList();
    }

    @Override
    public String getPassword() {
        return this.imageUser.getPassword();
    }

    @Override
    public String getUsername() {
        return this.imageUser.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.imageUser.isEnabled();
    }
}
