package com.ryuqq.setof.adapter.in.rest.v1.qna.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * QnaImageRequest - Q&A 이미지 요청 DTO.
 *
 * <p>레거시 CreateQnaImage 구조를 수용합니다: { imageUrl, displayOrder }.
 *
 * @param qnaImageId 이미지 ID (수정 시 기존 이미지 식별용, 선택)
 * @param imageUrl 이미지 URL
 * @param displayOrder 표시 순서
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "Q&A 이미지")
public record QnaImageRequest(
        @Schema(description = "이미지 ID (수정 시 기존 이미지 식별용)") Long qnaImageId,
        @Schema(description = "이미지 URL", example = "https://example.com/image.jpg") String imageUrl,
        @Schema(description = "표시 순서", example = "0") int displayOrder) {}
