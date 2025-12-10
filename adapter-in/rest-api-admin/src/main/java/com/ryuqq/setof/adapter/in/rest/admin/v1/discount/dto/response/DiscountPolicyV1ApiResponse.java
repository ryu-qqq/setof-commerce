package com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * V1 할인 정책 Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "할인 정책 응답")
public record DiscountPolicyV1ApiResponse(
        @Schema(description = "할인 정책 ID", example = "1") Long discountPolicyId,
        @Schema(description = "할인 상세 정보") DiscountDetailsV1ApiResponse discountDetails,
        @Schema(description = "등록 일시", example = "2024-01-01 00:00:00")
                @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                LocalDateTime insertDate,
        @Schema(description = "수정 일시", example = "2024-01-01 00:00:00")
                @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                LocalDateTime updateDate,
        @Schema(description = "등록자", example = "admin") String insertOperator,
        @Schema(description = "수정자", example = "admin") String updateOperator) {

    @Schema(description = "할인 상세 정보")
    public record DiscountDetailsV1ApiResponse(
            @Schema(description = "할인 정책명", example = "신년 특가") String discountPolicyName,
            @Schema(description = "할인 타입", example = "RATIO") String discountType,
            @Schema(description = "발행자 타입", example = "ADMIN") String publisherType,
            @Schema(description = "발행 타입", example = "PRODUCT") String issueType,
            @Schema(description = "할인 한도 여부 (Y/N)", example = "Y") String discountLimitYn,
            @Schema(description = "최대 할인 금액", example = "10000") Long maxDiscountPrice,
            @Schema(description = "공유 여부 (Y/N)", example = "Y") String shareYn,
            @Schema(description = "공유 비율", example = "50.0") Double shareRatio,
            @Schema(description = "할인 비율", example = "10.0") Double discountRatio,
            @Schema(description = "정책 시작 일시", example = "2024-01-01 00:00:00")
                    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                    LocalDateTime policyStartDate,
            @Schema(description = "정책 종료 일시", example = "2024-12-31 23:59:59")
                    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                    LocalDateTime policyEndDate,
            @Schema(description = "메모", example = "신년 특가 이벤트") String memo,
            @Schema(description = "우선순위", example = "1") Integer priority,
            @Schema(description = "활성 여부 (Y/N)", example = "Y") String activeYn) {}
}
