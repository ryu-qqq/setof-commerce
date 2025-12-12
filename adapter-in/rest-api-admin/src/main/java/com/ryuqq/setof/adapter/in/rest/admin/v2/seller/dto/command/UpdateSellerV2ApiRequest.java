package com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 셀러 수정 API 요청 DTO
 *
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "셀러 수정 요청")
public record UpdateSellerV2ApiRequest(
        @Schema(
                        description = "셀러명",
                        example = "테스트 셀러",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "셀러명은 필수입니다")
                @Size(max = 100, message = "셀러명은 100자를 초과할 수 없습니다")
                String sellerName,
        @Schema(description = "로고 URL", example = "https://example.com/logo.png") String logoUrl,
        @Schema(description = "설명", example = "테스트 셀러 설명") String description,
        @Schema(
                        description = "사업자등록번호",
                        example = "1234567890",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "사업자등록번호는 필수입니다")
                @Size(min = 10, max = 10, message = "사업자등록번호는 10자리여야 합니다")
                String registrationNumber,
        @Schema(description = "통신판매업 신고번호", example = "2024-서울강남-0001") String saleReportNumber,
        @Schema(description = "대표자명", example = "홍길동", requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "대표자명은 필수입니다")
                @Size(max = 50, message = "대표자명은 50자를 초과할 수 없습니다")
                String representative,
        @Schema(
                        description = "사업장 주소 (기본)",
                        example = "서울시 강남구",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "사업장 주소는 필수입니다")
                String businessAddressLine1,
        @Schema(description = "사업장 주소 (상세)", example = "테헤란로 123") String businessAddressLine2,
        @Schema(
                        description = "사업장 우편번호",
                        example = "06234",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "우편번호는 필수입니다")
                String businessZipCode,
        @Schema(description = "CS 이메일", example = "cs@example.com")
                @Email(message = "올바른 이메일 형식이 아닙니다")
                String csEmail,
        @Schema(description = "CS 휴대폰번호", example = "01012345678")
                @Size(max = 11, message = "휴대폰번호는 11자를 초과할 수 없습니다")
                String csMobilePhone,
        @Schema(description = "CS 유선전화번호", example = "0212345678")
                @Size(max = 11, message = "유선전화번호는 11자를 초과할 수 없습니다")
                String csLandlinePhone) {}
