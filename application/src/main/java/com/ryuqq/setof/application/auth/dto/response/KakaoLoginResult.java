package com.ryuqq.setof.application.auth.dto.response;

/**
 * 카카오 소셜 로그인 결과.
 *
 * <p>일반 LoginResult에 joined(기가입 여부) 정보를 추가합니다. 프론트엔드에서 신규 가입과 기존 로그인을 구분하여 리다이렉트합니다.
 *
 * @param loginResult 기본 로그인 결과 (토큰 포함)
 * @param joined 기가입 여부 (true: 기존 회원 로그인, false: 신규 가입)
 * @author ryu-qqq
 * @since 1.2.0
 */
public record KakaoLoginResult(LoginResult loginResult, boolean joined) {

    public static KakaoLoginResult ofNewMember(LoginResult loginResult) {
        return new KakaoLoginResult(loginResult, false);
    }

    public static KakaoLoginResult ofExistingMember(LoginResult loginResult) {
        return new KakaoLoginResult(loginResult, true);
    }

    public static KakaoLoginResult ofIntegrated(LoginResult loginResult) {
        return new KakaoLoginResult(loginResult, false);
    }
}
