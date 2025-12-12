package com.ryuqq.setof.application.seller.dto.command;

/**
 * Update Seller Command
 *
 * <p>셀러 수정 요청 데이터를 담는 순수한 불변 객체
 *
 * @param sellerId 수정할 셀러 ID
 * @param sellerName 셀러명
 * @param logoUrl 로고 URL (nullable)
 * @param description 설명 (nullable)
 * @param registrationNumber 사업자 등록번호
 * @param saleReportNumber 통신판매업 신고번호 (nullable)
 * @param representative 대표자명
 * @param businessAddressLine1 사업장 주소 (기본)
 * @param businessAddressLine2 사업장 주소 (상세, nullable)
 * @param businessZipCode 사업장 우편번호
 * @param csEmail CS 이메일 (nullable)
 * @param csMobilePhone CS 휴대폰 (nullable)
 * @param csLandlinePhone CS 유선전화 (nullable)
 * @author development-team
 * @since 1.0.0
 */
public record UpdateSellerCommand(
        Long sellerId,
        String sellerName,
        String logoUrl,
        String description,
        String registrationNumber,
        String saleReportNumber,
        String representative,
        String businessAddressLine1,
        String businessAddressLine2,
        String businessZipCode,
        String csEmail,
        String csMobilePhone,
        String csLandlinePhone) {}
