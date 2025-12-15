package com.ryuqq.setof.adapter.in.rest.admin.v2.productnotice.dto.response;

import com.ryuqq.setof.application.productnotice.dto.response.NoticeItemResponse;
import com.ryuqq.setof.application.productnotice.dto.response.ProductNoticeResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;

/**
 * 상품고시 조회 응답
 *
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "상품고시 조회 응답")
public record ProductNoticeV2ApiResponse(
        @Schema(description = "상품고시 ID", example = "1") Long productNoticeId,
        @Schema(description = "상품그룹 ID", example = "100") Long productGroupId,
        @Schema(description = "템플릿 ID", example = "1") Long templateId,
        @Schema(description = "고시 항목 목록") List<NoticeItemV2ApiResponse> items,
        @Schema(description = "항목 개수", example = "5") int itemCount,
        @Schema(description = "생성일시") Instant createdAt,
        @Schema(description = "수정일시") Instant updatedAt) {

    public static ProductNoticeV2ApiResponse from(ProductNoticeResponse response) {
        List<NoticeItemV2ApiResponse> itemResponses =
                response.items() != null
                        ? response.items().stream().map(NoticeItemV2ApiResponse::from).toList()
                        : List.of();

        return new ProductNoticeV2ApiResponse(
                response.productNoticeId(),
                response.productGroupId(),
                response.templateId(),
                itemResponses,
                response.itemCount(),
                response.createdAt(),
                response.updatedAt());
    }

    @Schema(description = "고시 항목 응답")
    public record NoticeItemV2ApiResponse(
            @Schema(description = "필드 키", example = "material") String fieldKey,
            @Schema(description = "필드 값", example = "면 100%") String fieldValue,
            @Schema(description = "표시 순서", example = "1") int displayOrder,
            @Schema(description = "값 존재 여부") boolean hasValue) {

        public static NoticeItemV2ApiResponse from(NoticeItemResponse response) {
            return new NoticeItemV2ApiResponse(
                    response.fieldKey(),
                    response.fieldValue(),
                    response.displayOrder(),
                    response.hasValue());
        }
    }
}
