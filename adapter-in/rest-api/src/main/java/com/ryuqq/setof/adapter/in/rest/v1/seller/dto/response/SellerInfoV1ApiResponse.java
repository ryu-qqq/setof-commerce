package com.ryuqq.setof.adapter.in.rest.v1.seller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * SellerInfoV1ApiResponse - V1 셀러 정보 응답 DTO.
 *
 * <p>레거시 SellerInfo 구조와 호환되는 V1 응답 형태입니다.
 *
 * <p>레거시 필드 매핑:
 *
 * <ul>
 *   <li>sellerId → SellerCompositeResult.seller.id
 *   <li>sellerName → SellerCompositeResult.seller.sellerName
 *   <li>logoUrl → SellerCompositeResult.seller.logoUrl
 *   <li>sellerDescription → SellerCompositeResult.seller.description
 *   <li>address → SellerCompositeResult.address.address + addressDetail
 *   <li>csPhoneNumber → SellerCompositeResult.businessInfo.csPhone
 *   <li>alimTalkPhoneNumber → SellerCompositeResult.businessInfo.csMobile
 *   <li>registrationNumber → SellerCompositeResult.businessInfo.registrationNumber
 *   <li>saleReportNumber → SellerCompositeResult.businessInfo.saleReportNumber
 *   <li>representative → SellerCompositeResult.businessInfo.representative
 *   <li>email → SellerCompositeResult.businessInfo.csEmail
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "V1 셀러 정보 응답")
public record SellerInfoV1ApiResponse(
        @Schema(description = "셀러 ID", example = "1") long sellerId,
        @Schema(description = "셀러명", example = "테스트 셀러") String sellerName,
        @Schema(description = "로고 URL", example = "https://example.com/logo.png") String logoUrl,
        @Schema(description = "셀러 설명", example = "좋은 상품을 판매합니다") String sellerDescription,
        @Schema(description = "주소", example = "서울시 강남구 테헤란로 123") String address,
        @Schema(description = "고객센터 전화번호", example = "02-1234-5678") String csPhoneNumber,
        @Schema(description = "알림톡 전화번호", example = "010-1234-5678") String alimTalkPhoneNumber,
        @Schema(description = "사업자등록번호", example = "123-45-67890") String registrationNumber,
        @Schema(description = "통신판매업 신고번호", example = "2024-서울강남-12345") String saleReportNumber,
        @Schema(description = "대표자명", example = "홍길동") String representative,
        @Schema(description = "이메일", example = "cs@example.com") String email) {}
