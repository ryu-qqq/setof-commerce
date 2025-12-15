package com.ryuqq.setof.adapter.in.rest.admin.v2.productnotice.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * 상품고시 수정 요청
 *
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "상품고시 수정 요청")
public record UpdateProductNoticeV2ApiRequest(
        @Schema(description = "상품고시 ID", example = "1") @NotNull(message = "상품고시 ID는 필수입니다")
                Long productNoticeId,
        @Schema(description = "고시 항목 목록") @NotEmpty(message = "최소 1개 이상의 고시 항목이 필요합니다") @Valid
                List<NoticeItemV2ApiRequest> items) {

    @Schema(description = "고시 항목 요청")
    public record NoticeItemV2ApiRequest(
            @Schema(description = "필드 키", example = "material") @NotBlank(message = "필드 키는 필수입니다")
                    String fieldKey,
            @Schema(description = "필드 값", example = "면 100%") @NotBlank(message = "필드 값은 필수입니다")
                    String fieldValue,
            @Schema(description = "표시 순서", example = "1") int displayOrder) {}
}
