package com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * SellerInfoContextV1ApiRequest - 셀러 등록 요청 DTO (V1 레거시).
 *
 * <p>셀러 기본정보 + 사업자정보 + 배송정보를 한번에 등록합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "셀러 등록 요청 DTO (V1)")
public record SellerInfoContextV1ApiRequest(
        @Schema(description = "셀러 기본 정보") @NotNull(message = "셀러 기본 정보는 필수입니다") @Valid
                SellerInfoInsertRequest sellerInfo,
        @Schema(description = "사업자 정보") @NotNull(message = "사업자 정보는 필수입니다") @Valid
                SellerBusinessInfoRequest sellerBusinessInfo,
        @Schema(description = "배송 정보") @NotNull(message = "배송 정보는 필수입니다") @Valid
                SellerShippingInfoRequest sellerShippingInfo) {

    @Schema(description = "셀러 기본 정보")
    public record SellerInfoInsertRequest(
            @Schema(description = "셀러명", example = "테스트셀러") @NotBlank(message = "셀러명은 필수입니다")
                    String sellerName,
            @Schema(description = "셀러 로고 URL", example = "https://example.com/logo.png")
                    @NotBlank(message = "셀러 로고 URL은 필수입니다")
                    String sellerLogoUrl,
            @Schema(description = "셀러 설명", example = "테스트 셀러 설명입니다.")
                    @NotBlank(message = "셀러 설명은 필수입니다")
                    String sellerDescription,
            @Schema(description = "수수료율", example = "10.0") @NotNull(message = "수수료율은 필수입니다")
                    Double commissionRate,
            @Schema(description = "개인정보 동의 여부", example = "Y")
                    @NotBlank(message = "개인정보 동의 여부는 필수입니다")
                    String privacyAgreementYn,
            @Schema(description = "이용약관 동의 여부", example = "Y")
                    @NotBlank(message = "이용약관 동의 여부는 필수입니다")
                    String termsUseAgreementYn) {}

    @Schema(description = "사업자 정보")
    public record SellerBusinessInfoRequest(
            @Schema(description = "사업자등록번호", example = "123-45-67890")
                    @NotBlank(message = "사업자등록번호는 필수입니다")
                    String registrationNumber,
            @Schema(description = "회사명", example = "테스트컴퍼니") @NotBlank(message = "회사명은 필수입니다")
                    String companyName,
            @Schema(description = "사업장 주소", example = "서울시 강남구")
                    @NotBlank(message = "사업장 주소는 필수입니다")
                    String businessAddressLine1,
            @Schema(description = "사업장 상세주소", example = "테헤란로 123")
                    @NotBlank(message = "사업장 상세주소는 필수입니다")
                    String businessAddressLine2,
            @Schema(description = "사업장 우편번호", example = "12345")
                    @NotBlank(message = "사업장 우편번호는 필수입니다")
                    String businessAddressZipCode,
            @Schema(description = "은행명", example = "국민은행") @NotBlank(message = "은행명은 필수입니다")
                    String bankName,
            @Schema(description = "계좌번호", example = "1234567890123")
                    @NotBlank(message = "계좌번호는 필수입니다")
                    String accountNumber,
            @Schema(description = "예금주", example = "홍길동") @NotBlank(message = "예금주는 필수입니다")
                    String accountHolderName,
            @Schema(description = "CS 전화번호", example = "02-1234-5678")
                    @NotBlank(message = "CS 전화번호는 필수입니다")
                    String csNumber,
            @Schema(description = "CS 휴대폰번호", example = "010-1234-5678")
                    @NotBlank(message = "CS 휴대폰번호는 필수입니다")
                    String csPhoneNumber,
            @Schema(description = "CS 이메일", example = "cs@example.com")
                    @NotBlank(message = "CS 이메일은 필수입니다")
                    String csEmail,
            @Schema(description = "통신판매업 신고번호", example = "제2025-서울강남-1234호")
                    @NotBlank(message = "통신판매업 신고번호는 필수입니다")
                    String saleReportNumber,
            @Schema(description = "대표자명", example = "홍길동") @NotBlank(message = "대표자명은 필수입니다")
                    String representative) {}

    @Schema(description = "배송 정보")
    public record SellerShippingInfoRequest(
            @Schema(description = "반품지 주소", example = "서울시 강남구")
                    @NotBlank(message = "반품지 주소는 필수입니다")
                    String returnAddressLine1,
            @Schema(description = "반품지 상세주소", example = "테헤란로 123")
                    @NotBlank(message = "반품지 상세주소는 필수입니다")
                    String returnAddressLine2,
            @Schema(description = "반품지 우편번호", example = "12345")
                    @NotBlank(message = "반품지 우편번호는 필수입니다")
                    String returnAddressZipCode) {}
}
