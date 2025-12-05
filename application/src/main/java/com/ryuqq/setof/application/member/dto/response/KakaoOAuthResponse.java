package com.ryuqq.setof.application.member.dto.response;

/**
 * Kakao OAuth Response
 *
 * <p>카카오 로그인 응답 데이터
 *
 * @author development-team
 * @since 1.0.0
 */
public record KakaoOAuthResponse(
        String memberId, TokenPairResponse tokens, boolean isNewMember, boolean needsIntegration) {

    /** 신규 카카오 회원 응답 생성 */
    public static KakaoOAuthResponse newMember(String memberId, TokenPairResponse tokens) {
        return new KakaoOAuthResponse(memberId, tokens, true, false);
    }

    /** 기존 카카오 회원 응답 생성 */
    public static KakaoOAuthResponse existingKakaoMember(
            String memberId, TokenPairResponse tokens) {
        return new KakaoOAuthResponse(memberId, tokens, false, false);
    }

    /** LOCAL 회원 통합 필요 응답 생성 */
    public static KakaoOAuthResponse needsIntegration(String memberId) {
        return new KakaoOAuthResponse(memberId, null, false, true);
    }
}
