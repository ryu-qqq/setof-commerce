package com.ryuqq.setof.adapter.in.rest.admin.v1.qna.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;

/**
 * V1 QNA 상세 Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "QNA 상세 응답")
public record DetailQnaV1ApiResponse(
        @Schema(description = "QNA 정보") FetchQnaV1ApiResponse qna,
        @Schema(description = "답변 QNA 목록") Set<AnswerQnaV1ApiResponse> answerQnas) {}
