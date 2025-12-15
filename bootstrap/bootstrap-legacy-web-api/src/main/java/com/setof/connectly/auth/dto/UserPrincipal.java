package com.setof.connectly.auth.dto;

import com.setof.connectly.module.user.dto.UserDto;
import com.setof.connectly.module.user.entity.Users;
import com.setof.connectly.module.user.enums.UserGradeEnum;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class UserPrincipal implements UserDetails {
    private long userId;
    private String name;

    private String phoneNumber;
    private final Collection<GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> authList = new ArrayList<GrantedAuthority>(authorities);
        return authList;
    }

    @Override
    public String getPassword() {
        return String.valueOf(userId);
    }

    @Override
    public String getUsername() {
        return String.valueOf(name);
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
        return true;
    }

    public static UserPrincipal create(UserDto user) {
        return new UserPrincipal(
                user.getUserId(),
                user.getName(),
                user.getPhoneNumber(),
                Collections.singletonList(
                        new SimpleGrantedAuthority(user.getUserGrade().getName())));
    }

    public static UserPrincipal create(Users user) {
        return new UserPrincipal(
                user.getId(),
                user.getName(),
                user.getPhoneNumber(),
                Collections.singletonList(
                        new SimpleGrantedAuthority(UserGradeEnum.NORMAL_GRADE.name())));
    }
}
