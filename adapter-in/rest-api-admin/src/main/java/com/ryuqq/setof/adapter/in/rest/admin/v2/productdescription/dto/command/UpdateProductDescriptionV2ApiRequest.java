package com.ryuqq.setof.adapter.in.rest.admin.v2.productdescription.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * 상품설명 수정 요청
 *
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "상품설명 수정 요청")
public record UpdateProductDescriptionV2ApiRequest(
        @Schema(description = "상품설명 ID", example = "1") @NotNull(message = "상품설명 ID는 필수입니다")
                Long productDescriptionId,
        @Schema(description = "HTML 컨텐츠", example = "<p>상품 상세 설명</p>")
                @NotBlank(message = "HTML 컨텐츠는 필수입니다")
                String htmlContent,
        @Schema(description = "설명 이미지 목록") @Valid List<DescriptionImageV2ApiRequest> images) {

    @Schema(description = "설명 이미지 요청")
    public record DescriptionImageV2ApiRequest(
            @Schema(description = "표시 순서", example = "1") int displayOrder,
            @Schema(description = "원본 URL", example = "https://cdn.example.com/desc.jpg")
                    @NotBlank(message = "원본 URL은 필수입니다")
                    String originUrl,
            @Schema(description = "CDN URL", example = "https://cdn.example.com/desc.jpg")
                    String cdnUrl) {}
}
