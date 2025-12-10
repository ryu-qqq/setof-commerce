package com.ryuqq.setof.adapter.in.rest.v1.qna.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "QnA 답변 수정 응답")
public record UpdateQnaAnswerV1ApiResponse(@Schema(description = "리뷰 답변 ID", example = "100") Long id) {
}
