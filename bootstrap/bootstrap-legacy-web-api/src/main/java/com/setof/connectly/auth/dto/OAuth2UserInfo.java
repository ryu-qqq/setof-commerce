package com.setof.connectly.auth.dto;

import com.setof.connectly.module.user.enums.Gender;
import com.setof.connectly.module.user.enums.SocialLoginType;
import com.setof.connectly.module.user.enums.UserGradeEnum;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class OAuth2UserInfo implements OAuth2User {

    private String id;
    private String phoneNumber;
    private String email;
    private String name;
    private LocalDate dateOfBirth;
    private Gender gender;
    private SocialLoginType socialLoginType;
    private UserGradeEnum userGrade;

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(userGrade.name()));
    }
}
