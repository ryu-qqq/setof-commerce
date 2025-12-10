package com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * V1 셀러 상세 수정 Request
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "셀러 상세 수정 요청")
public record SellerUpdateDetailV1ApiRequest(
        @Schema(description = "셀러명", example = "셀러명") String sellerName,
        @Schema(description = "CS 이메일", example = "cs@example.com") String csEmail,
        @Schema(description = "CS 번호", example = "1234") String csNumber,
        @Schema(description = "수수료율", example = "5.0") Double commissionRate,
        @Schema(description = "반품 주소 1", example = "서울시 강남구 테헤란로 123") String returnAddressLine1,
        @Schema(description = "반품 주소 2", example = "124-1234") String returnAddressLine2,
        @Schema(description = "반품 우편번호", example = "12345") String returnAddressZipCode,
        @Schema(description = "사이트 ID 목록", example = "[1, 2, 3]") List<Long> siteIds) {
}
