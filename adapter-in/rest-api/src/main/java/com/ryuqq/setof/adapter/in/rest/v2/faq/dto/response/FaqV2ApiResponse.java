package com.ryuqq.setof.adapter.in.rest.v2.faq.dto.response;

import com.ryuqq.setof.application.faq.dto.response.FaqResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

/**
 * FAQ V2 API Response
 *
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "FAQ 응답")
public record FaqV2ApiResponse(
        @Schema(description = "FAQ ID", example = "1") Long id,
        @Schema(description = "카테고리 코드", example = "ORDER") String categoryCode,
        @Schema(description = "질문", example = "주문 취소는 어떻게 하나요?") String question,
        @Schema(description = "답변", example = "마이페이지에서 주문 내역을 확인 후 취소하실 수 있습니다.") String answer,
        @Schema(description = "상단 노출 여부", example = "true") boolean isTop,
        @Schema(description = "상단 노출 순서", example = "1") int topOrder,
        @Schema(description = "표시 순서", example = "1") int displayOrder,
        @Schema(description = "조회수", example = "256") int viewCount,
        @Schema(description = "생성일시") Instant createdAt) {

    /**
     * Application Response → API Response 변환
     *
     * @param response Application Response
     * @return API Response
     */
    public static FaqV2ApiResponse from(FaqResponse response) {
        return new FaqV2ApiResponse(
                response.id(),
                response.categoryCode(),
                response.question(),
                response.answer(),
                response.isTop(),
                response.topOrder(),
                response.displayOrder(),
                response.viewCount(),
                response.createdAt());
    }
}
