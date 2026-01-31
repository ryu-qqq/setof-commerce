package com.ryuqq.setof.adapter.out.client.portone.dto.auth;

/**
 * PortOne V2 API Secret 로그인 요청 DTO
 *
 * <p>API Secret을 사용하여 인증 토큰을 발급받기 위한 요청 객체입니다.
 *
 * @param apiSecret 발급받은 API secret
 * @author development-team
 * @since 2.0.0
 */
public record PortOneAuthRequest(String apiSecret) {

    /**
     * API Secret으로 요청 객체 생성
     *
     * @param apiSecret API secret
     * @return PortOneAuthRequest 인스턴스
     */
    public static PortOneAuthRequest of(String apiSecret) {
        return new PortOneAuthRequest(apiSecret);
    }
}
