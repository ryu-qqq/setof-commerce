package com.ryuqq.setof.application.member.dto.response;

import com.ryuqq.setof.application.auth.dto.response.TokenPairResponse;

/**
 * Kakao OAuth Response
 *
 * <p>카카오 로그인 응답 데이터
 *
 * <p>응답 시나리오:
 *
 * <ul>
 *   <li>신규 카카오 회원: isNewMember=true, tokens 포함
 *   <li>기존 카카오 회원: isNewMember=false, tokens 포함
 *   <li>LOCAL 회원 통합 완료: isNewMember=false, wasIntegrated=true, tokens 포함
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public record KakaoOAuthResponse(
        String memberId,
        TokenPairResponse tokens,
        boolean isNewMember,
        boolean wasIntegrated,
        String memberName) {

    /** 신규 카카오 회원 응답 생성 */
    public static KakaoOAuthResponse newMember(String memberId, TokenPairResponse tokens) {
        return new KakaoOAuthResponse(memberId, tokens, true, false, null);
    }

    /** 기존 카카오 회원 응답 생성 */
    public static KakaoOAuthResponse existingKakaoMember(
            String memberId, TokenPairResponse tokens) {
        return new KakaoOAuthResponse(memberId, tokens, false, false, null);
    }

    /** LOCAL 회원 통합 완료 응답 생성 */
    public static KakaoOAuthResponse integrated(
            String memberId, TokenPairResponse tokens, String memberName) {
        return new KakaoOAuthResponse(memberId, tokens, false, true, memberName);
    }
}
