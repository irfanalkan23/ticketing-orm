package com.cydeo.entity.common;

import com.cydeo.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserPrincipal implements UserDetails {

    private User user;      // import User from cydeo.entity !!!

    public UserPrincipal(User user) {
        this.user = user;
    }

    //matching User role at database to Spring User granted authority
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> authorityList = new ArrayList<>();

        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(this.user.getRole().getDescription());

        authorityList.add(grantedAuthority);

        return authorityList;
    }

    @Override
    public String getPassword() {
        return this.user.getPassWord();
    }

    @Override
    public String getUsername() {
        return this.user.getUserName();
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
        return this.user.isEnabled();
    }
}
