package com.setof.connectly.module.utils;

import com.setof.connectly.auth.dto.UserPrincipal;
import com.setof.connectly.module.exception.auth.AuthForbiddenException;
import com.setof.connectly.module.exception.auth.UnAuthorizedException;
import com.setof.connectly.module.user.enums.UserGradeEnum;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static long currentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal))
            return 0L;
        return ((UserPrincipal) authentication.getPrincipal()).getUserId();
    }

    public static String currentUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal))
            throw new UnAuthorizedException();
        return ((UserPrincipal) authentication.getPrincipal()).getName();
    }

    public static UserPrincipal getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal))
            throw new UnAuthorizedException();
        return ((UserPrincipal) authentication.getPrincipal());
    }

    public static String currentUserForLogging() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal))
            return "GUEST";
        return ((UserPrincipal) authentication.getPrincipal()).getName();
    }

    public static UserGradeEnum getUserGrade() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) return UserGradeEnum.GUEST;
        return authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .map(UserGradeEnum::of)
                .orElseThrow(AuthForbiddenException::new);
    }
}
