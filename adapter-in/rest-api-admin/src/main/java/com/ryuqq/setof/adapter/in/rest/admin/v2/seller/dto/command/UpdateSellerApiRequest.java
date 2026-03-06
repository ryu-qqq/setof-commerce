package com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

/**
 * UpdateSellerApiRequest - 셀러 수정 API Request.
 *
 * <p>API-DTO-001: API Request DTO는 Record로 정의.
 *
 * <p>API-DTO-004: Update Request에 ID 포함 금지 → PathVariable에서 전달.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "셀러 수정 요청")
public record UpdateSellerApiRequest(
        @NotBlank @Schema(description = "셀러명") String sellerName,
        @NotBlank @Schema(description = "표시명") String displayName,
        @NotBlank @Schema(description = "로고 URL") String logoUrl,
        @NotBlank @Schema(description = "설명") String description,
        @Valid @Schema(description = "CS 정보 (선택)") CsInfoRequest csInfo,
        @Valid @Schema(description = "사업자 정보 (선택)") BusinessInfoRequest businessInfo) {

    @Schema(description = "CS 정보")
    public record CsInfoRequest(
            @NotBlank @Schema(description = "전화번호") String phone,
            @NotBlank @Schema(description = "이메일") String email,
            @Schema(description = "휴대폰번호") String mobile) {}

    @Schema(description = "사업자 정보")
    public record BusinessInfoRequest(
            @NotBlank @Schema(description = "사업자등록번호") String registrationNumber,
            @NotBlank @Schema(description = "회사명") String companyName,
            @NotBlank @Schema(description = "대표자명") String representative,
            @NotBlank @Schema(description = "통신판매업 신고번호") String saleReportNumber,
            @Valid @Schema(description = "사업자 주소") AddressRequest businessAddress) {}

    @Schema(description = "주소 정보")
    public record AddressRequest(
            @NotBlank @Schema(description = "우편번호") String zipCode,
            @NotBlank @Schema(description = "주소") String line1,
            @NotBlank @Schema(description = "상세주소") String line2) {}
}
