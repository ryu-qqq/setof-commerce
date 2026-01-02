package com.connectly.partnerAdmin.module.utils;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.connectly.partnerAdmin.auth.core.UserPrincipal;
import com.connectly.partnerAdmin.auth.enums.RoleType;
import com.connectly.partnerAdmin.auth.exception.UnAuthorizedException;

public class SecurityUtils {


    public static UserPrincipal getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal)) throw new UnAuthorizedException();
        return (UserPrincipal) authentication.getPrincipal();
    }

    public static Optional<Long> currentSellerIdOpt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal)) return Optional.empty();
        RoleType authorization = getAuthorization();
        if(authorization.isMaster()) return Optional.empty();
        return Optional.of((((UserPrincipal) authentication.getPrincipal()).sellerId()));
    }

    public static long currentSellerId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal)) throw new UnAuthorizedException();
        return (((UserPrincipal) authentication.getPrincipal()).sellerId());
    }

    public static Optional<String> currentSellerEmailOpt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal)) return Optional.empty();
        return Optional.of((((UserPrincipal) authentication.getPrincipal()).email()));
    }

    public static String currentSellerEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal)) throw new UnAuthorizedException();
        return (((UserPrincipal) authentication.getPrincipal()).email());
    }

    public static RoleType getAuthorization(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) return RoleType.GUEST;
        return authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .map(RoleType::of)
                .orElse(RoleType.GUEST);
    }





}
