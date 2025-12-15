package com.ryuqq.setof.adapter.out.client.portone.dto.auth;

/**
 * PortOne V2 토큰 응답 DTO
 *
 * <p>API Secret 로그인 또는 토큰 갱신 성공 시 반환되는 응답 객체입니다.
 *
 * @param accessToken 인증에 사용하는 액세스 토큰 (30분 유효기간)
 * @param refreshToken 토큰 재발급 및 유효기간 연장을 위한 리프레시 토큰 (24시간 유효기간)
 * @author development-team
 * @since 2.0.0
 */
public record PortOneTokenResponse(String accessToken, String refreshToken) {

    /**
     * 액세스 토큰 존재 여부 확인
     *
     * @return 액세스 토큰이 존재하면 true
     */
    public boolean hasAccessToken() {
        return accessToken != null && !accessToken.isBlank();
    }

    /**
     * 리프레시 토큰 존재 여부 확인
     *
     * @return 리프레시 토큰이 존재하면 true
     */
    public boolean hasRefreshToken() {
        return refreshToken != null && !refreshToken.isBlank();
    }

    /**
     * 유효한 토큰 응답인지 확인
     *
     * @return 액세스 토큰과 리프레시 토큰 모두 존재하면 true
     */
    public boolean isValid() {
        return hasAccessToken() && hasRefreshToken();
    }
}
