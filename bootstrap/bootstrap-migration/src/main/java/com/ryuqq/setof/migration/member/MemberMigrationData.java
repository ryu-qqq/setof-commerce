package com.ryuqq.setof.migration.member;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Member 마이그레이션 데이터
 *
 * <p>레거시 → 신규 변환된 데이터를 담는 DTO입니다.
 *
 * @param memberId 신규 회원 ID (UUID v7)
 * @param legacyUserId 레거시 사용자 ID (매핑용)
 * @param socialPkId 소셜 ID
 * @param phoneNumber 휴대폰 번호
 * @param email 이메일
 * @param passwordHash 비밀번호 해시
 * @param name 회원명
 * @param dateOfBirth 생년월일
 * @param gender 성별
 * @param provider 인증 제공자
 * @param status 회원 상태
 * @param privacyConsent 개인정보 수집 동의
 * @param serviceTermsConsent 서비스 이용약관 동의
 * @param adConsent 광고 수신 동의
 * @param withdrawalReason 탈퇴 사유
 * @param withdrawnAt 탈퇴 일시
 * @param createdAt 생성 일시 (레거시 유지)
 * @param updatedAt 수정 일시 (레거시 유지)
 * @param deletedAt 삭제 일시
 * @author development-team
 * @since 1.0.0
 */
public record MemberMigrationData(
        UUID memberId,
        Long legacyUserId,
        String socialPkId,
        String phoneNumber,
        String email,
        String passwordHash,
        String name,
        LocalDate dateOfBirth,
        String gender,
        String provider,
        String status,
        boolean privacyConsent,
        boolean serviceTermsConsent,
        boolean adConsent,
        String withdrawalReason,
        LocalDateTime withdrawnAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt) {}
