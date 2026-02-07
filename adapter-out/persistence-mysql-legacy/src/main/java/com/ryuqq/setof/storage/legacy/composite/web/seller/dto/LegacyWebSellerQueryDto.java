package com.ryuqq.setof.storage.legacy.composite.web.seller.dto;

/**
 * LegacyWebSellerQueryDto - 레거시 Web 판매자 조회 Projection DTO.
 *
 * <p>Projections.constructor()로 매핑 (Q클래스 생성 불필요).
 *
 * @param sellerId 판매자 ID
 * @param sellerName 판매자명 (회사명)
 * @param logoUrl 로고 URL
 * @param sellerDescription 판매자 설명
 * @param address 주소 (주소1 + 주소2 + 우편번호)
 * @param csPhoneNumber CS 전화번호
 * @param alimTalkPhoneNumber 알림톡 전화번호
 * @param registrationNumber 사업자등록번호
 * @param saleReportNumber 통신판매업 신고번호
 * @param representative 대표자명
 * @param email 이메일
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebSellerQueryDto(
        long sellerId,
        String sellerName,
        String logoUrl,
        String sellerDescription,
        String address,
        String csPhoneNumber,
        String alimTalkPhoneNumber,
        String registrationNumber,
        String saleReportNumber,
        String representative,
        String email) {}
