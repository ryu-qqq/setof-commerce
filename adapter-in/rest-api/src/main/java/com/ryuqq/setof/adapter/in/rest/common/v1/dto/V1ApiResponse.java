package com.ryuqq.setof.adapter.in.rest.common.v1.dto;

/**
 * V1 ApiResponse - Legacy API 호환 응답 래퍼
 *
 * <p>Legacy API와 동일한 응답 구조를 제공하여 Strangler Fig 패턴 마이그레이션을 지원합니다.
 *
 * <p><strong>사용 예시:</strong>
 *
 * <pre>{@code
 * // 성공 응답
 * V1ApiResponse<UserDto> response = V1ApiResponse.success(userDto);
 *
 * // 에러 응답
 * V1ApiResponse<Void> response = V1ApiResponse.error(null, "User not found");
 * }</pre>
 *
 * <p><strong>응답 형식:</strong>
 *
 * <pre>{@code
 * {
 *   "data": { ... },
 *   "response": {
 *     "status": 200,
 *     "message": "success"
 *   }
 * }
 * }</pre>
 *
 * @param <T> 응답 데이터 타입
 * @author development-team
 * @since 1.0.0
 */
public record V1ApiResponse<T>(T data, V1Response response) {

    /**
     * 성공 응답 생성
     *
     * @param data 응답 데이터
     * @param <T> 데이터 타입
     * @return 성공 V1ApiResponse
     */
    public static <T> V1ApiResponse<T> success(T data) {
        return new V1ApiResponse<>(data, V1Response.success());
    }

    /**
     * 에러 응답 생성 (데이터 포함)
     *
     * @param data 응답 데이터 (nullable)
     * @param message 에러 메시지
     * @param <T> 데이터 타입
     * @return 에러 V1ApiResponse
     */
    public static <T> V1ApiResponse<T> error(T data, String message) {
        return new V1ApiResponse<>(data, V1Response.error(message));
    }

    /**
     * Not Found 응답 생성
     *
     * @param message 에러 메시지
     * @param <T> 데이터 타입
     * @return Not Found V1ApiResponse
     */
    public static <T> V1ApiResponse<T> notFound(String message) {
        return new V1ApiResponse<>(null, V1Response.notFound(message));
    }

    /**
     * 서버 에러 응답 생성
     *
     * @param data 응답 데이터 (nullable)
     * @param message 에러 메시지
     * @param <T> 데이터 타입
     * @return 서버 에러 V1ApiResponse
     */
    public static <T> V1ApiResponse<T> serverError(T data, String message) {
        return new V1ApiResponse<>(data, V1Response.serverError(message));
    }
}
