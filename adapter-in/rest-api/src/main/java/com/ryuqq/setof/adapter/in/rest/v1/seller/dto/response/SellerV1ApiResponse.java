package com.ryuqq.setof.adapter.in.rest.v1.seller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * SellerV1ApiResponse - 판매자 상세 응답 DTO.
 *
 * <p>레거시 SellerInfo 기반 변환.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: DTO 불변성 보장.
 *
 * <p>Response DTO는 Domain 객체 의존 금지 → Result만 의존해야 하며, 변환은 Mapper에서 수행합니다.
 *
 * @param sellerId 판매자 ID
 * @param sellerName 판매자명 (회사명)
 * @param logoUrl 로고 URL
 * @param sellerDescription 판매자 설명
 * @param address 주소 (도로명 + 상세주소 + 우편번호 조합)
 * @param csPhoneNumber CS 전화번호
 * @param alimTalkPhoneNumber 알림톡 전화번호
 * @param registrationNumber 사업자등록번호
 * @param saleReportNumber 통신판매업 신고번호
 * @param representative 대표자명
 * @param email 이메일
 * @author ryu-qqq
 * @since 1.0.0
 * @see com.setof.connectly.module.seller.dto.SellerInfo
 */
@Schema(description = "판매자 상세 응답")
public record SellerV1ApiResponse(
        @Schema(description = "판매자 ID", example = "123") long sellerId,
        @Schema(description = "판매자명 (회사명)", example = "테스트 회사") String sellerName,
        @Schema(description = "로고 URL", example = "https://cdn.example.com/seller/logo.png")
                String logoUrl,
        @Schema(description = "판매자 설명", example = "테스트 판매자입니다") String sellerDescription,
        @Schema(description = "주소", example = "서울시 강남구 테헤란로 123 12345") String address,
        @Schema(description = "CS 전화번호", example = "02-1234-5678") String csPhoneNumber,
        @Schema(description = "알림톡 전화번호", example = "010-1234-5678") String alimTalkPhoneNumber,
        @Schema(description = "사업자등록번호", example = "123-45-67890") String registrationNumber,
        @Schema(description = "통신판매업 신고번호", example = "2023-서울강남-0001") String saleReportNumber,
        @Schema(description = "대표자명", example = "홍길동") String representative,
        @Schema(description = "이메일", example = "cs@example.com") String email) {}
