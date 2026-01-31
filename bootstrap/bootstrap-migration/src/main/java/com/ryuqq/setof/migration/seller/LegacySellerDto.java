package com.ryuqq.setof.migration.seller;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 레거시 Seller 테이블 조회 DTO
 *
 * <p>레거시 DB의 seller + seller_business_info + seller_shipping_info JOIN 결과를 담는 DTO입니다.
 *
 * @param sellerId 셀러 ID
 * @param sellerName 셀러명
 * @param logoUrl 로고 URL
 * @param description 셀러 설명
 * @param commissionRate 수수료율
 * @param approvalStatus 승인 상태
 * @param deleteYn 삭제 여부
 * @param insertDate 생성 일시
 * @param updateDate 수정 일시
 * @param registrationNumber 사업자등록번호
 * @param saleReportNumber 통신판매업신고번호
 * @param representative 대표자명
 * @param companyName 회사명
 * @param businessZipCode 사업장 우편번호
 * @param businessAddress 사업장 주소
 * @param businessAddressDetail 사업장 상세주소
 * @param bankName 은행명
 * @param accountNumber 계좌번호
 * @param accountHolderName 예금주명
 * @param csPhone CS 전화번호
 * @param csEmail CS 이메일
 * @param returnAddressZipCode 반품지 우편번호
 * @param returnAddressLine1 반품지 주소
 * @param returnAddressLine2 반품지 상세주소
 * @author development-team
 * @since 1.0.0
 */
public record LegacySellerDto(
        // seller 테이블
        Long sellerId,
        String sellerName,
        String logoUrl,
        String description,
        BigDecimal commissionRate,
        String approvalStatus,
        String deleteYn,
        LocalDateTime insertDate,
        LocalDateTime updateDate,
        // seller_business_info 테이블
        String registrationNumber,
        String saleReportNumber,
        String representative,
        String companyName,
        String businessZipCode,
        String businessAddress,
        String businessAddressDetail,
        String bankName,
        String accountNumber,
        String accountHolderName,
        String csPhone,
        String csEmail,
        // seller_shipping_info 테이블
        String returnAddressZipCode,
        String returnAddressLine1,
        String returnAddressLine2) {}
