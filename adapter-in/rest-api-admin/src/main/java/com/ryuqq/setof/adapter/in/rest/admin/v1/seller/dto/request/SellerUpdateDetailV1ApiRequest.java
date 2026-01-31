package com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * SellerUpdateDetailV1ApiRequest - 셀러 정보 수정 요청 DTO (V1 레거시).
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "셀러 정보 수정 요청 DTO (V1)")
public record SellerUpdateDetailV1ApiRequest(
        @Schema(description = "셀러명", example = "테스트셀러") @NotBlank(message = "셀러명은 필수입니다")
                String sellerName,
        @Schema(description = "CS 이메일", example = "cs@example.com")
                @NotBlank(message = "CS 이메일은 필수입니다")
                String csEmail,
        @Schema(description = "CS 전화번호", example = "02-1234-5678")
                @NotBlank(message = "CS 전화번호는 필수입니다")
                String csNumber,
        @Schema(description = "수수료율", example = "10.0") @NotNull(message = "수수료율은 필수입니다")
                Double commissionRate,
        @Schema(description = "반품지 주소", example = "서울시 강남구") @NotBlank(message = "반품지 주소는 필수입니다")
                String returnAddressLine1,
        @Schema(description = "반품지 상세주소", example = "테헤란로 123")
                @NotBlank(message = "반품지 상세주소는 필수입니다")
                String returnAddressLine2,
        @Schema(description = "반품지 우편번호", example = "12345") @NotBlank(message = "반품지 우편번호는 필수입니다")
                String returnAddressZipCode,
        @Schema(description = "사업자등록번호", example = "123-45-67890")
                @NotBlank(message = "사업자등록번호는 필수입니다")
                String registrationNumber,
        @Schema(description = "회사명", example = "테스트컴퍼니") @NotBlank(message = "회사명은 필수입니다")
                String companyName,
        @Schema(description = "대표자명", example = "홍길동") @NotBlank(message = "대표자명은 필수입니다")
                String representative,
        @Schema(description = "통신판매업 신고번호", example = "제2025-서울강남-1234호")
                @NotBlank(message = "통신판매업 신고번호는 필수입니다")
                String saleReportNumber,
        @Schema(description = "사업장 주소", example = "서울시 강남구") @NotBlank(message = "사업장 주소는 필수입니다")
                String businessAddressLine1,
        @Schema(description = "사업장 상세주소", example = "테헤란로 123")
                @NotBlank(message = "사업장 상세주소는 필수입니다")
                String businessAddressLine2,
        @Schema(description = "사업장 우편번호", example = "12345") @NotBlank(message = "사업장 우편번호는 필수입니다")
                String businessAddressZipCode,
        @Schema(description = "사이트 ID 목록", example = "[1, 2, 3]")
                @NotEmpty(message = "사이트 ID 목록은 필수입니다")
                List<Long> siteIds) {}
