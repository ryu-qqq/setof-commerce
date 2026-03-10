package com.ryuqq.setof.adapter.in.rest.v1.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * MyPageV1ApiResponse - 마이페이지 V1 응답 DTO.
 *
 * <p>API-DTO-003: record 기반 Response DTO.
 *
 * <p>레거시 MyPageResponse 호환: 회원 정보 + 주문 상태별 건수.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
@Schema(description = "마이페이지 응답")
public record MyPageV1ApiResponse(
        @Schema(description = "이름") String name,
        @Schema(description = "전화번호") String phoneNumber,
        @Schema(description = "이메일") String email,
        @Schema(description = "소셜 로그인 타입") String socialLoginType,
        @Schema(description = "가입일시") String registrationDate,
        @Schema(description = "등급") String userGrade,
        @Schema(description = "현재 마일리지") double currentMileage,
        @Schema(description = "주문 상태별 건수") List<OrderCountResponse> orderCounts) {

    @Schema(description = "주문 상태별 건수")
    public record OrderCountResponse(
            @Schema(description = "주문 상태") String orderStatus,
            @Schema(description = "건수") long count) {}
}
