package com.ryuqq.setof.application.member.dto.bundle;

/**
 * Kakao OAuth 처리 결과
 *
 * <p>KakaoOAuthFacade가 반환하는 중간 결과 DTO
 *
 * <p>시나리오별 결과:
 *
 * <ul>
 *   <li>EXISTING_KAKAO: 카카오 ID로 찾은 기존 회원
 *   <li>EXISTING_MEMBER: 핸드폰 번호로 찾은 기존 회원 (카카오 연동 없이 로그인)
 *   <li>INTEGRATED: 핸드폰 번호로 찾은 기존 회원 + 카카오 연동 완료
 *   <li>NEW_MEMBER: 신규 카카오 회원 가입 완료
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public record KakaoOAuthResult(String memberId, ResultType resultType, String memberName) {

    public enum ResultType {
        EXISTING_KAKAO,
        EXISTING_MEMBER,
        INTEGRATED,
        NEW_MEMBER
    }

    public static KakaoOAuthResult existingKakao(String memberId) {
        return new KakaoOAuthResult(memberId, ResultType.EXISTING_KAKAO, null);
    }

    public static KakaoOAuthResult existingMember(String memberId) {
        return new KakaoOAuthResult(memberId, ResultType.EXISTING_MEMBER, null);
    }

    public static KakaoOAuthResult integrated(String memberId, String memberName) {
        return new KakaoOAuthResult(memberId, ResultType.INTEGRATED, memberName);
    }

    public static KakaoOAuthResult newMember(String memberId) {
        return new KakaoOAuthResult(memberId, ResultType.NEW_MEMBER, null);
    }

    public boolean isNewMember() {
        return resultType == ResultType.NEW_MEMBER;
    }

    public boolean wasIntegrated() {
        return resultType == ResultType.INTEGRATED;
    }
}
