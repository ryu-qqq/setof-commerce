package com.connectly.partnerAdmin.auth.service;

import java.util.Collections;
import java.util.Optional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.connectly.partnerAdmin.auth.core.UserPrincipal;
import com.connectly.partnerAdmin.auth.exception.UnAuthorizedException;
import com.connectly.partnerAdmin.module.seller.core.SellerContext;
import com.connectly.partnerAdmin.module.seller.service.SellerFetchService;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final SellerFetchService sellerFetchService;

    @Override
    public UserPrincipal loadUserByUsername(String email) throws UsernameNotFoundException {
        SellerContext seller = getSeller(email).orElseThrow(() -> new UsernameNotFoundException(email));
        if(!seller.getApprovalStatus().isApproved()) throw new UnAuthorizedException();
        return new UserPrincipal(
                seller.getEmail(),
                seller.getPasswordHash(),
                seller.getSellerId(),
                Collections.singletonList(new SimpleGrantedAuthority(seller.getRoleType().name()))
        );
    }

    private Optional<SellerContext> getSeller(String email){
        return sellerFetchService.fetchSellerInfo(email);
    }


}
