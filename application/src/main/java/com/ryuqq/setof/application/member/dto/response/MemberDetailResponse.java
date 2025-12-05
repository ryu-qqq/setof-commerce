package com.ryuqq.setof.application.member.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Member Detail Result
 *
 * <p>회원 상세 정보 결과 데이터
 *
 * @author development-team
 * @since 1.0.0
 */
public record MemberDetailResponse(
        String memberId,
        String phoneNumber,
        String email,
        String name,
        LocalDate dateOfBirth,
        String gender,
        String provider,
        String status,
        LocalDateTime createdAt) {}
