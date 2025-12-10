package com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.command;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import org.hibernate.validator.constraints.Length;

/**
 * V1 할인 정책 생성 Request
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "할인 정책 생성 요청")
public record CreateDiscountV1ApiRequest(
        @Schema(description = "할인 정책 ID (복사 생성 시 사용)", example = "1")
                @JsonInclude(JsonInclude.Include.NON_NULL)
                Long discountPolicyId,
        @Schema(description = "할인 상세 정보", requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "할인 상세 정보는 필수입니다.")
                CreateDiscountDetailsV1ApiRequest discountDetails) {

    @Schema(description = "할인 상세 정보")
    public record CreateDiscountDetailsV1ApiRequest(
            @Schema(
                            description = "할인 정책명",
                            example = "신년 특가",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "할인 정책명은 필수입니다.")
                    @Length(max = 50, message = "정책명은 50자를 넘을 수 없습니다.")
                    String discountPolicyName,
            @Schema(
                            description = "할인 타입",
                            example = "RATIO",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotNull(message = "할인 타입은 필수입니다.")
                    String discountType,
            @Schema(
                            description = "발행자 타입",
                            example = "ADMIN",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotNull(message = "발행자 타입은 필수입니다.")
                    String publisherType,
            @Schema(
                            description = "발행 타입",
                            example = "PRODUCT",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotNull(message = "발행 타입은 필수입니다.")
                    String issueType,
            @Schema(
                            description = "할인 한도 여부 (Y/N)",
                            example = "Y",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotNull(message = "할인 한도 여부는 필수입니다.")
                    String discountLimitYn,
            @Schema(
                            description = "최대 할인 금액",
                            example = "10000",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @Min(value = 0, message = "최대 할인 금액은 0 이상이어야 합니다.")
                    Long maxDiscountPrice,
            @Schema(
                            description = "공유 여부 (Y/N)",
                            example = "Y",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotNull(message = "공유 여부는 필수입니다.")
                    String shareYn,
            @Schema(
                            description = "공유 비율",
                            example = "50.0",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @Max(value = 100, message = "공유 비율은 100을 넘을 수 없습니다.")
                    @Min(value = 0, message = "공유 비율은 0 이상이어야 합니다.")
                    Double shareRatio,
            @Schema(
                            description = "할인 비율",
                            example = "10.0",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @Max(value = 100, message = "할인 비율은 100을 넘을 수 없습니다.")
                    @Min(value = 0, message = "할인 비율은 0 이상이어야 합니다.")
                    Double discountRatio,
            @Schema(
                            description = "정책 시작 일시",
                            example = "2024-01-01 00:00:00",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                    @NotNull(message = "정책 시작 일시는 필수입니다.")
                    LocalDateTime policyStartDate,
            @Schema(
                            description = "정책 종료 일시",
                            example = "2024-12-31 23:59:59",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                    @NotNull(message = "정책 종료 일시는 필수입니다.")
                    LocalDateTime policyEndDate,
            @Schema(description = "메모", example = "신년 특가 이벤트")
                    @Length(max = 100, message = "메모는 100자를 넘을 수 없습니다.")
                    String memo,
            @Schema(
                            description = "우선순위",
                            example = "1",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @Max(value = 100, message = "우선순위는 100을 넘을 수 없습니다.")
                    @Min(value = 0, message = "우선순위는 0 이상이어야 합니다.")
                    Integer priority,
            @Schema(
                            description = "활성 여부 (Y/N)",
                            example = "Y",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotNull(message = "활성 여부는 필수입니다.")
                    String activeYn) {}
}
