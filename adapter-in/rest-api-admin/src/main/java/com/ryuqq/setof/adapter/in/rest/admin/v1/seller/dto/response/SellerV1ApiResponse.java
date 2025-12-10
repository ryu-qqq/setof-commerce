package com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * V1 셀러 Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "셀러 응답")
public record SellerV1ApiResponse(
        @Schema(description = "셀러 ID", example = "1") Long sellerId,
        @Schema(description = "셀러명", example = "셀러명") String sellerName,
        @Schema(description = "수수료율", example = "5.0") Double commissionRate,
        @Schema(description = "승인 상태", example = "APPROVED") String approvalStatus,
        @Schema(description = "CS 전화번호", example = "1588-0000") String csPhoneNumber,
        @Schema(description = "CS 이메일", example = "cs@example.com") String csEmail,
        @Schema(description = "등록 일시", example = "2024-01-01 00:00:00")
                @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                LocalDateTime insertDate) {}
