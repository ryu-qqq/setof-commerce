package com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * V1 셀러 상세 Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "셀러 상세 응답")
public record SellerDetailV1ApiResponse(
        @Schema(description = "셀러 ID", example = "1") Long sellerId,
        @Schema(description = "셀러명", example = "셀러명") String sellerName,
        @Schema(description = "로고 URL", example = "https://example.com/logo.jpg") String logoUrl,
        @Schema(description = "수수료율", example = "5.0") Double commissionRate,
        @Schema(description = "승인 상태", example = "APPROVED") String approvalStatus,
        @Schema(description = "셀러 설명", example = "셀러 설명입니다.") String sellerDescription,
        @Schema(description = "사업장 주소 1", example = "서울시 강남구 테헤란로 123") String businessAddressLine1,
        @Schema(description = "사업장 주소 2", example = "124-1234") String businessAddressLine2,
        @Schema(description = "사업장 우편번호", example = "12345") String businessAddressZipCode,
        @Schema(description = "반품 주소 1", example = "서울시 강남구 테헤란로 123") String returnAddressLine1,
        @Schema(description = "반품 주소 2", example = "124-1234") String returnAddressLine2,
        @Schema(description = "반품 우편번호", example = "12345") String returnAddressZipCode,
        @Schema(description = "CS 전화번호", example = "1588-0000") String csPhoneNumber,
        @Schema(description = "CS 번호", example = "1234") String csNumber,
        @Schema(description = "CS 이메일", example = "cs@example.com") String csEmail,
        @Schema(description = "사업자 등록번호", example = "123-45-67890") String registrationNumber,
        @Schema(description = "통신판매업 신고번호", example = "2024-서울강남-1234") String saleReportNumber,
        @Schema(description = "대표자명", example = "홍길동") String representative,
        @Schema(description = "은행명", example = "신한은행") String bankName,
        @Schema(description = "계좌번호", example = "110123456789") String accountNumber,
        @Schema(description = "예금주명", example = "홍길동") String accountHolderName,
        @Schema(description = "사이트 목록") List<SiteV1ApiResponse> sites) {

    @Schema(description = "사이트 응답")
    public record SiteV1ApiResponse(
            @Schema(description = "사이트 ID", example = "1") Long siteId,
            @Schema(description = "사이트명", example = "SETOF") String siteName) {}
}
