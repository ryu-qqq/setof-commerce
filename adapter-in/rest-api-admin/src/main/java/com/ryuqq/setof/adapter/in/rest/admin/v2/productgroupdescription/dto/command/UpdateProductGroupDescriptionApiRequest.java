package com.ryuqq.setof.adapter.in.rest.admin.v2.productgroupdescription.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

/**
 * UpdateProductGroupDescriptionApiRequest - 상품 그룹 상세 설명 수정 API Request.
 *
 * <p>API-REQ-001: Record 패턴 사용
 *
 * <p>API-VAL-001: jakarta.validation 사용
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "상품 그룹 상세 설명 수정 요청")
public record UpdateProductGroupDescriptionApiRequest(
        @Schema(
                        description = "상세 설명 내용 (HTML)",
                        example = "<p>상품 상세 설명입니다.</p>",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "상세 설명 내용은 필수입니다")
                String content,
        @Schema(description = "상세 설명 이미지 목록", nullable = true) @Valid
                List<DescriptionImageApiRequest> descriptionImages) {

    /**
     * 상세 설명 이미지 데이터.
     *
     * @param imageUrl 이미지 URL
     * @param sortOrder 정렬 순서
     */
    @Schema(description = "상세 설명 이미지 데이터")
    public record DescriptionImageApiRequest(
            @Schema(
                            description = "이미지 URL",
                            example = "https://example.com/desc-image.jpg",
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
