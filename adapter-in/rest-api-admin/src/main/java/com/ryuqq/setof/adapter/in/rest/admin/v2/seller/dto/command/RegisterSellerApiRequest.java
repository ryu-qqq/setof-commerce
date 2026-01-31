package com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * RegisterSellerApiRequest - 셀러 등록 요청 DTO.
 *
 * <p>셀러 기본정보 + 사업자정보 + 주소를 한번에 등록합니다.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-003: Validation 어노테이션.
 *
 * <p>API-DTO-006: 복잡한 구조는 중첩 Record로 표현.
 *
 * <p>nullable 허용 안함 - 모든 필드 필수.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "셀러 등록 요청 DTO")
public record RegisterSellerApiRequest(
        @Schema(description = "셀러 기본 정보", requiredMode = Schema.RequiredMode.REQUIRED)
                @Valid
                @NotNull(message = "셀러 기본 정보는 필수입니다")
                SellerInfoRequest seller,
        @Schema(description = "사업자 정보", requiredMode = Schema.RequiredMode.REQUIRED)
                @Valid
                @NotNull(message = "사업자 정보는 필수입니다")
                BusinessInfoRequest businessInfo,
        @Schema(description = "주소 정보", requiredMode = Schema.RequiredMode.REQUIRED)
                @Valid
                @NotNull(message = "주소 정보는 필수입니다")
                AddressInfoRequest address) {

    @Schema(description = "셀러 기본 정보")
    public record SellerInfoRequest(
            @Schema(
                            description = "셀러명",
                            example = "테스트셀러",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "셀러명은 필수입니다")
                    String sellerName,
            @Schema(
                            description = "표시명",
                            example = "테스트 브랜드",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "표시명은 필수입니다")
                    String displayName,
            @Schema(
                            description = "로고 URL",
                            example = "https://example.com/logo.png",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "로고 URL은 필수입니다")
                    String logoUrl,
            @Schema(
                            description = "설명",
                            example = "테스트 셀러 설명입니다.",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "설명은 필수입니다")
                    String description) {}

    @Schema(description = "사업자 정보")
    public record BusinessInfoRequest(
            @Schema(
                            description = "사업자등록번호",
                            example = "123-45-67890",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "사업자등록번호는 필수입니다")
                    String registrationNumber,
            @Schema(
                            description = "회사명",
                            example = "테스트컴퍼니",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "회사명은 필수입니다")
                    String companyName,
            @Schema(
                            description = "대표자명",
                            example = "홍길동",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "대표자명은 필수입니다")
                    String representative,
            @Schema(
                            description = "통신판매업 신고번호",
                            example = "제2025-서울강남-1234호",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "통신판매업 신고번호는 필수입니다")
                    String saleReportNumber,
            @Schema(description = "사업장 주소", requiredMode = Schema.RequiredMode.REQUIRED)
                    @Valid
                    @NotNull(message = "사업장 주소는 필수입니다")
                    AddressRequest businessAddress,
            @Schema(description = "CS 연락처", requiredMode = Schema.RequiredMode.REQUIRED)
                    @Valid
                    @NotNull(message = "CS 연락처는 필수입니다")
                    CsContactRequest csContact) {}

    @Schema(description = "주소 정보 (출고지/반품지)")
    public record AddressInfoRequest(
            @Schema(
                            description = "주소 타입",
                            example = "RETURN",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "주소 타입은 필수입니다")
                    String addressType,
            @Schema(
                            description = "주소 이름",
                            example = "본사",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "주소 이름은 필수입니다")
                    String addressName,
            @Schema(description = "주소", requiredMode = Schema.RequiredMode.REQUIRED)
                    @Valid
                    @NotNull(message = "주소는 필수입니다")
                    AddressRequest address,
            @Schema(description = "담당자 연락처", requiredMode = Schema.RequiredMode.REQUIRED)
                    @Valid
                    @NotNull(message = "담당자 연락처는 필수입니다")
                    ContactInfoRequest contactInfo,
            @Schema(
                            description = "기본 주소 여부",
                            example = "true",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotNull(message = "기본 주소 여부는 필수입니다")
                    Boolean defaultAddress) {}

    @Schema(description = "주소")
    public record AddressRequest(
            @Schema(
                            description = "우편번호",
                            example = "12345",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "우편번호는 필수입니다")
                    String zipCode,
            @Schema(
                            description = "주소",
                            example = "서울시 강남구",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "주소는 필수입니다")
                    String line1,
            @Schema(
                            description = "상세주소",
                            example = "테헤란로 123",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "상세주소는 필수입니다")
                    String line2) {}

    @Schema(description = "CS 연락처")
    public record CsContactRequest(
            @Schema(
                            description = "전화번호",
                            example = "02-1234-5678",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "전화번호는 필수입니다")
                    String phone,
            @Schema(
                            description = "이메일",
                            example = "cs@example.com",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "이메일은 필수입니다")
                    String email,
            @Schema(
                            description = "휴대폰",
                            example = "010-1234-5678",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "휴대폰번호는 필수입니다")
                    String mobile) {}

    @Schema(description = "담당자 연락처")
    public record ContactInfoRequest(
            @Schema(
                            description = "담당자명",
                            example = "홍길동",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "담당자명은 필수입니다")
                    String name,
            @Schema(
                            description = "연락처",
                            example = "010-1234-5678",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "연락처는 필수입니다")
                    String phone) {}
}
