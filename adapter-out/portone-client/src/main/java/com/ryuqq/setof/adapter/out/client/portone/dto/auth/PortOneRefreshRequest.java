package com.ryuqq.setof.adapter.out.client.portone.dto.auth;

/**
 * PortOne V2 토큰 갱신 요청 DTO
 *
 * <p>리프레시 토큰을 사용하여 새로운 액세스 토큰을 발급받기 위한 요청 객체입니다.
 *
 * @param refreshToken 리프레시 토큰
 * @author development-team
 * @since 2.0.0
 */
public record PortOneRefreshRequest(String refreshToken) {

    /**
     * 리프레시 토큰으로 요청 객체 생성
     *
     * @param refreshToken 리프레시 토큰
     * @return PortOneRefreshRequest 인스턴스
     */
    public static PortOneRefreshRequest of(String refreshToken) {
        return new PortOneRefreshRequest(refreshToken);
    }
}
