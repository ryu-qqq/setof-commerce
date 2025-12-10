package com.ryuqq.setof.adapter.in.rest.member.dto.response;

import com.ryuqq.setof.application.member.dto.response.MemberDetailResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.time.LocalDate;

/**
 * Member API Response
 *
 * <p>회원 정보 응답 DTO
 *
 * <p>컨벤션 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>API 전용 DTO - Application Response와 분리
 *   <li>시간 필드는 Instant 사용 (타임존 독립적)
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "회원 정보 응답")
public record MemberApiResponse(
        @Schema(description = "회원 ID", example = "1") String memberId,
        @Schema(description = "핸드폰 번호", example = "01012345678") String phoneNumber,
        @Schema(description = "이메일 주소", example = "user@example.com") String email,
        @Schema(description = "이름", example = "홍길동") String name,
        @Schema(description = "생년월일", example = "1990-01-15") LocalDate dateOfBirth,
        @Schema(description = "성별", example = "MALE") String gender,
        @Schema(description = "인증 제공자", example = "LOCAL") String provider,
        @Schema(description = "회원 상태", example = "ACTIVE") String status,
        @Schema(description = "가입일시", example = "2025-12-05T10:30:00Z") Instant createdAt) {

    /**
     * Application Response → API Response 변환
     *
     * @param response Application 응답
     * @return MemberApiResponse
     */
    public static MemberApiResponse from(MemberDetailResponse response) {
        return new MemberApiResponse(
                response.memberId(),
                response.phoneNumber(),
                response.email(),
                response.name(),
                response.dateOfBirth(),
                response.gender(),
                response.provider(),
                response.status(),
                response.createdAt());
    }
}
