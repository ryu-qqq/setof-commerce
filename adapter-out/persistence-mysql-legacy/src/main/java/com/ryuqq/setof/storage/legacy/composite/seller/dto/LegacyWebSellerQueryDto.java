package com.ryuqq.setof.storage.legacy.composite.seller.dto;

import java.time.LocalDateTime;

/**
 * LegacyWebSellerQueryDto - 레거시 Web 판매자 조회 Projection DTO.
 *
 * <p>Projections.constructor()로 매핑 (Q클래스 생성 불필요).
 *
 * <p>SellerCompositeResult 변환에 필요한 개별 필드를 모두 포함합니다.
 *
 * @param sellerId 판매자 ID
 * @param sellerName 판매자명 (seller 테이블)
 * @param companyName 회사명 (seller_business_info 테이블)
 * @param logoUrl 로고 URL
 * @param sellerDescription 판매자 설명
 * @param commissionRate 수수료율
 * @param businessAddressLine1 사업장 주소 1
 * @param businessAddressLine2 사업장 주소 2
 * @param businessAddressZipCode 사업장 우편번호
 * @param csNumber CS 대표번호
 * @param csPhoneNumber CS 전화번호
 * @param csEmail CS 이메일
 * @param registrationNumber 사업자등록번호
 * @param saleReportNumber 통신판매업 신고번호
 * @param representative 대표자명
 * @param bankName 은행명
 * @param accountNumber 계좌번호
 * @param accountHolderName 예금주명
 * @param createdAt 생성일시
 * @param updatedAt 수정일시
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebSellerQueryDto(
        long sellerId,
        String sellerName,
        String companyName,
        String logoUrl,
        String sellerDescription,
        Double commissionRate,
        String businessAddressLine1,
        String businessAddressLine2,
        String businessAddressZipCode,
        String csNumber,
        String csPhoneNumber,
        String csEmail,
        String registrationNumber,
        String saleReportNumber,
        String representative,
        String bankName,
        String accountNumber,
        String accountHolderName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {}
