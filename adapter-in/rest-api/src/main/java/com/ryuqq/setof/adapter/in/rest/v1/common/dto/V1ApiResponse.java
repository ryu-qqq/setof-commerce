package com.ryuqq.setof.adapter.in.rest.v1.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * V1ApiResponse - V1 API 표준 성공 응답 래퍼.
 *
 * <p>레거시 호환을 위한 V1 응답 구조입니다.
 *
 * <p>레거시 ApiResponse.java 구조:
 *
 * <pre>
 * {
 *   "data": T,
 *   "response": {
 *     "status": 200,
 *     "message": "success"
 *   }
 * }
 * </pre>
 *
 * <p>V2 ApiResponse와의 차이점:
 *
 * <ul>
 *   <li>V1: data + response (status, message)
 *   <li>V2: data + timestamp + requestId
 * </ul>
 *
 * @param <T> 응답 데이터 타입
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "V1 API 표준 성공 응답")
public record V1ApiResponse<T>(
        @Schema(description = "응답 데이터", nullable = true) T data,
        @Schema(description = "응답 메타 정보") V1Response response) {

    /**
     * 성공 응답 생성.
     *
     * <p>status: 200, message: "success"
     *
     * @param data 응답 데이터
     * @param <T> 데이터 타입
     * @return V1ApiResponse 인스턴스
     */
    public static <T> V1ApiResponse<T> success(T data) {
        return new V1ApiResponse<>(data, V1Response.success());
    }

    /**
     * 데이터 없는 성공 응답 생성.
     *
     * @param <T> 데이터 타입
     * @return V1ApiResponse 인스턴스 (data: null)
     */
    public static <T> V1ApiResponse<T> success() {
        return success(null);
    }

    /**
     * 404 Not Found 응답 생성.
     *
     * <p>레거시 호환: HTTP 200으로 응답하되 body에 status 404를 포함합니다.
     *
     * @param errorMessage 에러 메시지
     * @param <T> 데이터 타입
     * @return V1ApiResponse 인스턴스 (data: null, status: 404)
     */
    public static <T> V1ApiResponse<T> dataNotFoundWithErrorMessage(String errorMessage) {
        return new V1ApiResponse<>(null, V1Response.of(404, errorMessage));
    }

    /**
     * 커스텀 상태 응답 생성.
     *
     * @param data 응답 데이터
     * @param status HTTP 상태 코드
     * @param message 응답 메시지
     * @param <T> 데이터 타입
     * @return V1ApiResponse 인스턴스
     */
    public static <T> V1ApiResponse<T> of(T data, int status, String message) {
        return new V1ApiResponse<>(data, V1Response.of(status, message));
    }
}
