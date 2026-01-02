package com.ryuqq.setof.migration.member;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 레거시 Users 테이블 조회 DTO
 *
 * <p>레거시 DB의 Users 테이블 데이터를 담는 DTO입니다.
 *
 * @param userId 레거시 사용자 ID (Long PK)
 * @param socialPkId 소셜 ID
 * @param phoneNumber 휴대폰 번호
 * @param email 이메일
 * @param passwordHash 비밀번호 해시
 * @param name 회원명
 * @param dateOfBirth 생년월일
 * @param gender 성별 (MALE, FEMALE, OTHER)
 * @param provider 인증 제공자 (LOCAL, KAKAO 등)
 * @param status 회원 상태 (ACTIVE, INACTIVE, WITHDRAWN)
 * @param privacyConsent 개인정보 수집 동의
 * @param serviceTermsConsent 서비스 이용약관 동의
 * @param adConsent 광고 수신 동의
 * @param withdrawalReason 탈퇴 사유
 * @param withdrawnAt 탈퇴 일시
 * @param createdAt 생성 일시
 * @param updatedAt 수정 일시
 * @param deletedAt 삭제 일시 (소프트 딜리트)
 * @author development-team
 * @since 1.0.0
 */
public record LegacyUserDto(
        Long userId,
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
