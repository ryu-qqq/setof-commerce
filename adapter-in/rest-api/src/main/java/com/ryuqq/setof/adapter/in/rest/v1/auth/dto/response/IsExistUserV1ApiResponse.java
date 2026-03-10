package com.ryuqq.setof.adapter.in.rest.v1.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * IsExistUserV1ApiResponse - 회원 존재 여부 V1 응답 DTO.
 *
 * <p>API-DTO-003: record 기반 Response DTO.
 *
 * <p>레거시 JoinedUser 호환: joined + joinedUser(name, userId, ...).
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
@Schema(description = "회원 존재 여부 응답")
public record IsExistUserV1ApiResponse(
        @Schema(description = "가입 여부") boolean joined,
        @Schema(description = "가입된 회원 정보 (미가입 시 null)", nullable = true)
                JoinedUserResponse joinedUser) {

    @Schema(description = "가입 회원 정보")
    public record JoinedUserResponse(
            @Schema(description = "이름") String name,
            @Schema(description = "사용자 ID") Long userId,
            @Schema(description = "소셜 로그인 타입") String socialLoginType,
            @Schema(description = "전화번호") String phoneNumber,
            @Schema(description = "소셜 PK ID") String socialPkId,
            @Schema(description = "현재 마일리지") double currentMileage,
            @Schema(description = "가입일시") String joinedDate,
            @Schema(description = "삭제 여부") String deleteYn) {}
}
