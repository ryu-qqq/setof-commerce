package com.ryuqq.setof.api.member.dto.response;

import com.ryuqq.setof.application.member.dto.response.MemberDetailResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public record MemberApiResponse(
        String memberId,
        String phoneNumber,
        String email,
        String name,
        LocalDate dateOfBirth,
        String gender,
        String provider,
        String status,
        LocalDateTime createdAt) {

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
