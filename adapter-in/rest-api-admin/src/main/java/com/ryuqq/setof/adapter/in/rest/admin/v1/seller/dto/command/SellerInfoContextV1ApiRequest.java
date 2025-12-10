package com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * V1 셀러 정보 컨텍스트 Request
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "셀러 정보 컨텍스트 요청")
public record SellerInfoContextV1ApiRequest(
        @Schema(description = "셀러 정보") SellerInfoInsertV1ApiRequest sellerInfo,
        @Schema(description = "셀러 사업자 정보") SellerBusinessInfoV1ApiRequest sellerBusinessInfo,
        @Schema(description = "셀러 배송 정보") SellerShippingInfoV1ApiRequest sellerShippingInfo) {

    @Schema(description = "셀러 정보 생성 요청")
    public record SellerInfoInsertV1ApiRequest(
            @Schema(description = "셀러명", example = "셀러명") String sellerName,
            @Schema(description = "셀러 로고 URL", example = "https://example.com/logo.jpg")
                    String sellerLogoUrl,
            @Schema(description = "셀러 설명", example = "셀러 설명입니다.") String sellerDescription,
            @Schema(description = "수수료율", example = "5.0") Double commissionRate,
            @Schema(description = "개인정보 동의 여부 (Y/N)", example = "Y") String privacyAgreementYn,
            @Schema(description = "이용약관 동의 여부 (Y/N)", example = "Y") String termsUseAgreementYn) {}

    @Schema(description = "셀러 사업자 정보 요청")
    public record SellerBusinessInfoV1ApiRequest(
            @Schema(description = "사업자 등록번호", example = "123-45-67890") String registrationNumber,
            @Schema(description = "회사명", example = "회사명") String companyName,
            @Schema(description = "사업장 주소 1", example = "서울시 강남구 테헤란로 123")
                    String businessAddressLine1,
            @Schema(description = "사업장 주소 2", example = "124-1234") String businessAddressLine2,
            @Schema(description = "사업장 우편번호", example = "12345") String businessAddressZipCode,
            @Schema(description = "은행명", example = "신한은행") String bankName,
            @Schema(description = "계좌번호", example = "110123456789") String accountNumber,
            @Schema(description = "예금주명", example = "홍길동") String accountHolderName,
            @Schema(description = "CS 번호", example = "1234") String csNumber,
            @Schema(description = "CS 전화번호", example = "1588-0000") String csPhoneNumber,
            @Schema(description = "CS 이메일", example = "cs@example.com") String csEmail,
            @Schema(description = "통신판매업 신고번호", example = "2024-서울강남-1234") String saleReportNumber,
            @Schema(description = "대표자명", example = "홍길동") String representative) {}

    @Schema(description = "셀러 배송 정보 요청")
    public record SellerShippingInfoV1ApiRequest(
            @Schema(description = "반품 주소 1", example = "서울시 강남구 테헤란로 123")
                    String returnAddressLine1,
            @Schema(description = "반품 주소 2", example = "124-1234") String returnAddressLine2,
            @Schema(description = "반품 우편번호", example = "12345") String returnAddressZipCode) {}
}
