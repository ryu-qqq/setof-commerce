package com.ryuqq.setof.migration.seller;

import java.time.Instant;

/**
 * SellerApplication 마이그레이션 데이터
 *
 * <p>레거시 셀러 데이터를 seller_applications 테이블에 저장하기 위한 DTO입니다.
 *
 * @param legacySellerId 레거시 셀러 ID (체크포인트용)
 * @param sellerName 셀러명
 * @param displayName 표시명
 * @param logoUrl 로고 URL
 * @param description 설명
 * @param registrationNumber 사업자등록번호
 * @param companyName 회사명
 * @param representative 대표자명
 * @param saleReportNumber 통신판매업신고번호
 * @param businessZipCode 사업장 우편번호
 * @param businessBaseAddress 사업장 기본주소
 * @param businessDetailAddress 사업장 상세주소
 * @param csPhoneNumber CS 전화번호
 * @param csEmail CS 이메일
 * @param addressType 주소 타입 (RETURN)
 * @param addressName 주소명
 * @param addressZipCode 주소 우편번호
 * @param addressBaseAddress 주소 기본주소
 * @param addressDetailAddress 주소 상세주소
 * @param contactName 담당자명
 * @param contactPhoneNumber 담당자 연락처
 * @param agreedAt 동의 일시
 * @param status 상태 (APPROVED)
 * @param appliedAt 신청 일시
 * @param processedAt 처리 일시
 * @param processedBy 처리자
 * @param createdAt 생성 일시
 * @param updatedAt 수정 일시
 * @author development-team
 * @since 1.0.0
 */
public record SellerApplicationMigrationData(
        Long legacySellerId,
        String sellerName,
        String displayName,
        String logoUrl,
        String description,
        String registrationNumber,
        String companyName,
        String representative,
        String saleReportNumber,
        String businessZipCode,
        String businessBaseAddress,
        String businessDetailAddress,
        String csPhoneNumber,
        String csEmail,
        String addressType,
        String addressName,
        String addressZipCode,
        String addressBaseAddress,
        String addressDetailAddress,
        String contactName,
        String contactPhoneNumber,
        Instant agreedAt,
        String status,
        Instant appliedAt,
        Instant processedAt,
        String processedBy,
        Instant createdAt,
        Instant updatedAt) {}
