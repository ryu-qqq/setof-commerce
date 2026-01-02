package com.ryuqq.setof.adapter.in.rest.common.v1.dto;

/**
 * V1 Response - Legacy API 호환 내부 응답 객체
 *
 * <p>Legacy API의 응답 구조와 동일한 형태를 제공합니다.
 *
 * <p><strong>응답 형식:</strong>
 *
 * <pre>{@code
 * {
 *   "status": 200,
 *   "message": "success"
 * }
 * }</pre>
 *
 * @param status HTTP 상태 코드
 * @param message 응답 메시지
 * @author development-team
 * @since 1.0.0
 */
public record V1Response(int status, String message) {

    /**
     * 성공 응답 생성
     *
     * @return 성공 V1Response
     */
    public static V1Response success() {
        return new V1Response(200, "success");
    }

    /**
     * 에러 응답 생성
     *
     * @param message 에러 메시지
     * @return 에러 V1Response
     */
    public static V1Response error(String message) {
        return new V1Response(200, message);
    }

    /**
     * Not Found 응답 생성
     *
     * @param message 에러 메시지
     * @return Not Found V1Response
     */
    public static V1Response notFound(String message) {
        return new V1Response(404, message);
    }

    /**
     * 서버 에러 응답 생성
     *
     * @param message 에러 메시지
     * @return 서버 에러 V1Response
     */
    public static V1Response serverError(String message) {
        return new V1Response(500, message);
    }
}
