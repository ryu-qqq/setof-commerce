package com.ryuqq.setof.adapter.in.rest.v1.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * UserV1ApiResponse - 사용자 정보 응답 DTO.
 *
 * <p>레거시 JoinedUser, JoinedDto 기반 변환.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: DTO 불변성 보장.
 *
 * <p>Response DTO는 Domain 객체 의존 금지 -> Result만 의존해야 하며, 변환은 Mapper에서 수행합니다.
 *
 * @param isJoined 가입 여부
 * @param user 사용자 정보 (미가입 시 null)
 * @author ryu-qqq
 * @since 1.0.0
 * @see com.setof.connectly.module.user.dto.join.JoinedUser
 */
@Schema(description = "사용자 정보 응답")
public record UserV1ApiResponse(
        @Schema(description = "가입 여부", example = "true") boolean isJoined,
        @Schema(description = "사용자 정보 (미가입 시 null)", nullable = true) UserDetailResponse user) {

    /**
     * UserDetailResponse - 사용자 상세 정보 응답.
     *
     * @param userId 사용자 ID
     * @param name 이름
     * @param phoneNumber 전화번호
     * @param socialLoginType 소셜 로그인 타입 (NONE, KAKAO, NAVER, GOOGLE, APPLE)
     * @param socialPkId 소셜 로그인 고유 ID
     * @param currentMileage 현재 마일리지
     * @param joinedDate 가입일시
     */
    @Schema(description = "사용자 상세 정보")
    public record UserDetailResponse(
            @Schema(description = "사용자 ID", example = "12345") long userId,
            @Schema(description = "이름", example = "홍길동") String name,
            @Schema(description = "전화번호", example = "01012345678") String phoneNumber,
            @Schema(
                            description = "소셜 로그인 타입",
                            example = "KAKAO",
                            allowableValues = {"NONE", "KAKAO", "NAVER", "GOOGLE", "APPLE"})
                    String socialLoginType,
            @Schema(description = "소셜 로그인 고유 ID", example = "kakao_123456", nullable = true)
                    String socialPkId,
            @Schema(description = "현재 마일리지", example = "5000.0") double currentMileage,
            @Schema(description = "가입일시", example = "2024-01-15T10:30:00")
                    LocalDateTime joinedDate) {}
}
