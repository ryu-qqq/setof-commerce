package com.ryuqq.setof.adapter.in.rest.admin.v1.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * V1Response - V1 API 응답 메타 정보.
 *
 * <p>레거시 호환을 위한 응답 상태 및 메시지를 담는 객체입니다.
 *
 * <p>레거시 Response.java 구조:
 *
 * <pre>
 * {
 *   "status": 200,
 *   "message": "success"
 * }
 * </pre>
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "V1 API 응답 메타 정보")
public record V1Response(
        @Schema(description = "HTTP 상태 코드", example = "200") int status,
        @Schema(description = "응답 메시지", example = "success") String message) {

    /**
     * 성공 응답 생성.
     *
     * @return status 200, message "success"
     */
    public static V1Response success() {
        return new V1Response(200, "success");
    }

    /**
     * 커스텀 상태/메시지 응답 생성.
     *
     * @param status HTTP 상태 코드
     * @param message 응답 메시지
     * @return V1Response 인스턴스
     */
    public static V1Response of(int status, String message) {
        return new V1Response(status, message);
    }
}
