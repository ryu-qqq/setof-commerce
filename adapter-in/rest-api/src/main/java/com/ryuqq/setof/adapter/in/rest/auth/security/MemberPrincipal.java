package com.ryuqq.setof.adapter.in.rest.auth.security;

import java.io.Serial;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Member Principal - Spring Security UserDetails 구현체
 *
 * <p>JWT 인증 후 SecurityContext에 저장되는 사용자 정보
 *
 * <p>컨벤션 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Plain Java 사용
 *   <li>불변성 보장
 *   <li>getMemberId() 메서드 제공
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public final class MemberPrincipal implements UserDetails {

    @Serial private static final long serialVersionUID = 1L;

    private final String memberId;
    private final String phoneNumber;
    private final Collection<? extends GrantedAuthority> authorities;

    private MemberPrincipal(
            String memberId,
            String phoneNumber,
            Collection<? extends GrantedAuthority> authorities) {
        this.memberId = memberId;
        this.phoneNumber = phoneNumber;
        this.authorities = authorities;
    }

    /**
     * Static Factory Method - 기본 권한(ROLE_MEMBER)으로 생성
     *
     * @param memberId 회원 ID (UUID v7)
     * @param phoneNumber 핸드폰 번호
     * @return MemberPrincipal
     */
    public static MemberPrincipal of(String memberId, String phoneNumber) {
        return new MemberPrincipal(
                memberId, phoneNumber, List.of(new SimpleGrantedAuthority("ROLE_MEMBER")));
    }

    /**
     * Static Factory Method - 권한 지정 생성
     *
     * @param memberId 회원 ID (UUID v7)
     * @param phoneNumber 핸드폰 번호
     * @param authorities 권한 목록
     * @return MemberPrincipal
     */
    public static MemberPrincipal of(
            String memberId,
            String phoneNumber,
            Collection<? extends GrantedAuthority> authorities) {
        return new MemberPrincipal(memberId, phoneNumber, authorities);
    }

    /**
     * 회원 ID 반환 (UUID v7)
     *
     * @return 회원 ID
     */
    public String getMemberId() {
        return memberId;
    }

    /**
     * 핸드폰 번호 반환
     *
     * @return 핸드폰 번호
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.unmodifiableCollection(authorities);
    }

    @Override
    public String getPassword() {
        return null; // JWT 인증이므로 비밀번호 불필요
    }

    @Override
    public String getUsername() {
        return memberId; // memberId를 username으로 사용
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
}
