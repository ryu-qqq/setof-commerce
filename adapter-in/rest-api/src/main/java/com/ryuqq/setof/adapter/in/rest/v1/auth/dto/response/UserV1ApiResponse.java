package com.ryuqq.setof.adapter.in.rest.v1.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * UserV1ApiResponse - 사용자 프로필 V1 응답 DTO.
 *
 * <p>API-DTO-003: record 기반 Response DTO.
 *
 * <p>레거시 JoinedUser 호환: joined + joinedUser(name, userId, ...).
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
@Schema(description = "사용자 프로필 응답")
public record UserV1ApiResponse(
        @Schema(description = "가입 여부") boolean joined,
        @Schema(description = "회원 정보") UserDetailResponse joinedUser) {

    @Schema(description = "회원 상세 정보")
    public record UserDetailResponse(
            @Schema(description = "이름") String name,
            @Schema(description = "사용자 ID") long userId,
            @Schema(description = "소셜 로그인 타입") String socialLoginType,
            @Schema(description = "전화번호") String phoneNumber,
            @Schema(description = "소셜 PK ID") String socialPkId,
            @Schema(description = "현재 마일리지") double currentMileage,
            @Schema(description = "가입일시") String joinedDate,
            @Schema(description = "삭제 여부") String deleteYn) {}
}
