package com.ryuqq.setof.adapter.in.rest.admin.v2.productgroupimage.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * RegisterProductGroupImagesApiRequest - 상품 그룹 이미지 등록 API Request.
 *
 * <p>API-REQ-001: Record 패턴 사용
 *
 * <p>API-VAL-001: jakarta.validation 사용
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "상품 그룹 이미지 등록 요청")
public record RegisterProductGroupImagesApiRequest(
        @Schema(description = "등록할 이미지 목록", requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "이미지 목록은 필수입니다")
                @Valid
                List<ImageApiRequest> images) {

    /**
     * 이미지 데이터.
     *
     * @param imageType 이미지 유형 (THUMBNAIL, DETAIL)
     * @param imageUrl 이미지 URL
     * @param sortOrder 정렬 순서
     */
    @Schema(description = "이미지 데이터")
    public record ImageApiRequest(
            @Schema(
                            description = "이미지 유형 (THUMBNAIL, DETAIL)",
                            example = "THUMBNAIL",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "이미지 타입은 필수입니다")
                    String imageType,
            @Schema(
                            description = "이미지 URL",
                            example = "https://example.com/image.jpg",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @NotBlank(message = "이미지 URL은 필수입니다")
                    String imageUrl,
            @Schema(
                            description = "정렬 순서",
                            example = "0",
                            requiredMode = Schema.RequiredMode.REQUIRED)
                    @Min(value = 0, message = "정렬 순서는 0 이상이어야 합니다")
                    int sortOrder) {}
}
