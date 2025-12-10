package com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * V1 셀러 승인 상태 수정 Request
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "셀러 승인 상태 수정 요청")
public record SellerApprovalStatusV1ApiRequest(
        @Schema(description = "셀러 ID 목록", example = "[1, 2, 3]") List<Long> sellerIds,
        @Schema(
                        description = "승인 상태",
                        example = "APPROVED",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                String approvalStatus) {}
