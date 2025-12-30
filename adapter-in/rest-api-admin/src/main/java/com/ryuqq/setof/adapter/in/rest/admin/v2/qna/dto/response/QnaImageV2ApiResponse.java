package com.ryuqq.setof.adapter.in.rest.admin.v2.qna.dto.response;

import com.ryuqq.setof.application.qna.dto.response.QnaImageResponse;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * QnA Image V2 API Response
 *
 * <p>문의 이미지 API 응답 DTO
 *
 * @param imageUrl 이미지 URL
 * @param displayOrder 표시 순서
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "문의 이미지 응답")
public record QnaImageV2ApiResponse(
        @Schema(description = "이미지 URL", example = "https://example.com/image.jpg")
        String imageUrl,

        @Schema(description = "표시 순서", example = "1")
        int displayOrder) {

    public static QnaImageV2ApiResponse from(QnaImageResponse response) {
        return new QnaImageV2ApiResponse(response.imageUrl(), response.displayOrder());
    }
}
