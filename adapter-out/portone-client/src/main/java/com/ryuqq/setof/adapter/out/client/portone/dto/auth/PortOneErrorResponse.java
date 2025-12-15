package com.ryuqq.setof.adapter.out.client.portone.dto.auth;

/**
 * PortOne V2 에러 응답 DTO
 *
 * <p>API 호출 실패 시 반환되는 에러 응답 객체입니다.
 *
 * <p><strong>에러 타입:</strong>
 *
 * <ul>
 *   <li>{@code INVALID_REQUEST} - 요청된 입력 정보가 유효하지 않은 경우 (400)
 *   <li>{@code UNAUTHORIZED} - 인증 정보가 올바르지 않은 경우 (401)
 * </ul>
 *
 * @param type 에러 타입 (INVALID_REQUEST, UNAUTHORIZED)
 * @param message 에러 메시지
 * @author development-team
 * @since 2.0.0
 */
public record PortOneErrorResponse(String type, String message) {

    /** 유효하지 않은 요청 에러 타입 */
    public static final String INVALID_REQUEST = "INVALID_REQUEST";

    /** 인증 실패 에러 타입 */
    public static final String UNAUTHORIZED = "UNAUTHORIZED";

    /**
     * 유효하지 않은 요청 에러인지 확인
     *
     * @return INVALID_REQUEST 타입이면 true
     */
    public boolean isInvalidRequest() {
        return INVALID_REQUEST.equals(type);
    }

    /**
     * 인증 실패 에러인지 확인
     *
     * @return UNAUTHORIZED 타입이면 true
     */
    public boolean isUnauthorized() {
        return UNAUTHORIZED.equals(type);
    }
}
