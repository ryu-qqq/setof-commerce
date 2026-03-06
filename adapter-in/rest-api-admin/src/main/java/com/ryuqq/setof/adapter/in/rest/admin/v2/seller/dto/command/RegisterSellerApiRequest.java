package com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * RegisterSellerApiRequest - 셀러 등록 API Request.
 *
 * <p>API-DTO-001: API Request DTO는 Record로 정의.
 *
 * <p>API-DTO-003: Validation 어노테이션은 API Request에만 적용.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "셀러 등록 요청")
public record RegisterSellerApiRequest(
        @Valid @NotNull @Schema(description = "셀러 기본 정보") SellerInfoRequest sellerInfo,
        @Valid @NotNull @Schema(description = "사업자 정보") BusinessInfoRequest businessInfo) {

    @Schema(description = "셀러 기본 정보")
    public record SellerInfoRequest(
            @NotBlank @Schema(description = "셀러명", example = "테스트 셀러") String sellerName,
            @NotBlank @Schema(description = "표시명", example = "테스트") String displayName,
            @NotBlank @Schema(description = "로고 URL") String logoUrl,
            @NotBlank @Schema(description = "설명") String description) {}

    @Schema(description = "사업자 정보")
    public record BusinessInfoRequest(
            @NotBlank @Schema(description = "사업자등록번호") String registrationNumber,
            @NotBlank @Schema(description = "회사명") String companyName,
            @NotBlank @Schema(description = "대표자명") String representative,
            @NotBlank @Schema(description = "통신판매업 신고번호") String saleReportNumber,
            @Valid @NotNull @Schema(description = "사업자 주소") AddressRequest businessAddress,
            @Valid @NotNull @Schema(description = "CS 연락처") CsContactRequest csContact) {}

    @Schema(description = "주소 정보")
    public record AddressRequest(
            @NotBlank @Schema(description = "우편번호") String zipCode,
            @NotBlank @Schema(description = "주소") String line1,
            @NotBlank @Schema(description = "상세주소") String line2) {}

    @Schema(description = "CS 연락처 정보")
    public record CsContactRequest(
            @NotBlank @Schema(description = "전화번호") String phone,
            @NotBlank @Schema(description = "이메일") String email,
            @NotBlank @Schema(description = "휴대폰번호") String mobile) {}
}
