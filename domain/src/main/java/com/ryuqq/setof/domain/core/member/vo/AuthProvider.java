package com.ryuqq.setof.domain.core.member.vo;

/**
 * 인증 제공자 Enum
 *
 * <p>회원 가입/로그인 방식:</p>
 * <ul>
 *     <li>LOCAL: 자체 회원가입 (이메일/비밀번호)</li>
 *     <li>KAKAO: 카카오 소셜 로그인</li>
 * </ul>
 */
public enum AuthProvider {
    LOCAL,
    KAKAO
}
