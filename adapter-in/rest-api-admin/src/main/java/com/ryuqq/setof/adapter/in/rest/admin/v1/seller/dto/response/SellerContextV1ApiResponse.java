package com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * V1 셀러 컨텍스트 Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "셀러 컨텍스트 응답")
public record SellerContextV1ApiResponse(
        @Schema(description = "셀러 ID", example = "1") Long sellerId,
        @Schema(description = "이메일", example = "seller@example.com") String email,
        @Schema(description = "비밀번호 해시", example = "hash...") String passwordHash,
        @Schema(description = "역할 타입", example = "SELLER") String roleType,
        @Schema(description = "승인 상태", example = "APPROVED") String approvalStatus) {}
