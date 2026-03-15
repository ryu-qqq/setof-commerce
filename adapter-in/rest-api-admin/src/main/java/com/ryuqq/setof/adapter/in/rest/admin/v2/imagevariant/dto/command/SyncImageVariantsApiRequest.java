package com.ryuqq.setof.adapter.in.rest.admin.v2.imagevariant.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * SyncImageVariantsApiRequest - 이미지 Variant 동기화 API Request.
 *
 * <p>API-REQ-001: Record 패턴 사용
 *
 * <p>API-VAL-001: jakarta.validation 사용
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "이미지 Variant 동기화 요청")
public record SyncImageVariantsApiRequest(
        @Schema(
                        description = "원본 이미지 ID",
                        example = "123",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "원본 이미지 ID는 필수입니다")
                @Min(value = 1, message = "원본 이미지 ID는 1 이상이어야 합니다")
                Long sourceImageId,
        @Schema(
                        description = "이미지 소스 타입 (PRODUCT_GROUP_IMAGE, DESCRIPTION_IMAGE)",
                        example = "PRODUCT_GROUP_IMAGE",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "이미지 소스 타입은 필수입니다")
                String sourceType,
        @Schema(description = "Variant 목록", requiredMode = Schema.RequiredMode.REQUIRED)
                @NotEmpty(message = "Variant는 최소 1개 이상 필요합니다")
                @Valid
                List<VariantApiRequest> variants) {

    /**
     * Variant 데이터.
     *
     * @param variantType Variant 타입
     * @param resultAssetId FileFlow 변환 결과 에셋 ID
     * @param variantUrl 변환된 이미지 CDN URL
     * @param width 이미지 너비 (px)
     * @param height 이미지 높이 (px)
     */
    @Schema(description = "Variant 데이터")
    public record VariantApiRequest(
            @Schema(
                            description =
                                    "Variant 타입 (SMALL_WEBP, MEDIUM_WEBP, LARGE_WEBP,"
                                            + " ORIGINAL_WEBP)",
                            example = "SMALL_WEBP",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "Variant 타입은 필수입니다")
                    String variantType,
            @Schema(
                            description = "FileFlow 변환 결과 에셋 ID",
                            example = "asset-abc-123",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "결과 에셋 ID는 필수입니다")
                    String resultAssetId,
            @Schema(
                            description = "변환된 이미지 CDN URL",
                            example = "https://stage-cdn.set-of.com/public/2026/03/732c9b01.webp",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "Variant URL은 필수입니다")
                    String variantUrl,
            @Schema(description = "이미지 너비 (px)", example = "300") Integer width,
            @Schema(description = "이미지 높이 (px)", example = "300") Integer height) {}
}
