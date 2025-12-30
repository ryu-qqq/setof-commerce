package com.ryuqq.setof.application.member.dto.response;

import java.time.Instant;

/**
 * 회원 요약 정보 Response DTO
 *
 * <p>회원 목록 조회 시 반환되는 요약 정보입니다.
 *
 * @param memberId 회원 ID (UUID v7 문자열)
 * @param phoneNumber 핸드폰 번호
 * @param email 이메일 (nullable)
 * @param name 회원명
 * @param provider 인증 제공자 (LOCAL, KAKAO 등)
 * @param status 회원 상태 (ACTIVE, INACTIVE, WITHDRAWN)
 * @param createdAt 가입일시 (UTC 기준 Instant)
 * @author development-team
 * @since 1.0.0
 */
public record MemberSummaryResponse(
        String memberId,
        String phoneNumber,
        String email,
        String name,
        String provider,
        String status,
        Instant createdAt) {

    /**
     * Static Factory Method
     *
     * @param memberId 회원 ID
     * @param phoneNumber 핸드폰 번호
     * @param email 이메일
     * @param name 회원명
     * @param provider 인증 제공자
     * @param status 회원 상태
     * @param createdAt 가입일시
     * @return MemberSummaryResponse 인스턴스
     */
    public static MemberSummaryResponse of(
            String memberId,
            String phoneNumber,
            String email,
            String name,
            String provider,
            String status,
            Instant createdAt) {
        return new MemberSummaryResponse(
                memberId, phoneNumber, email, name, provider, status, createdAt);
    }
}
