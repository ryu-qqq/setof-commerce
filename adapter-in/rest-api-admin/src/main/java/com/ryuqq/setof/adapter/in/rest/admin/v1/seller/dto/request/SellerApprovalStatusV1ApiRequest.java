package com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * SellerApprovalStatusV1ApiRequest - 셀러 승인 상태 변경 요청 DTO (V1 레거시).
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "셀러 승인 상태 변경 요청 DTO (V1)")
public record SellerApprovalStatusV1ApiRequest(
        @Schema(description = "셀러 ID 목록", example = "[1, 2, 3]") List<Long> sellerIds,
        @Schema(description = "승인 상태", example = "APPROVED") String approvalStatus) {}
