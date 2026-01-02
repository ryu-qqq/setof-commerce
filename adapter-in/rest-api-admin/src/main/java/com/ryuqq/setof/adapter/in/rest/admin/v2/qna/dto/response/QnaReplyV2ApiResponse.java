package com.ryuqq.setof.adapter.in.rest.admin.v2.qna.dto.response;

import com.ryuqq.setof.application.qna.dto.response.QnaReplyResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * QnA Reply V2 API Response
 *
 * <p>문의 답변 API 응답 DTO
 *
 * @param id 답변 ID
 * @param qnaId 문의 ID
 * @param parentReplyId 부모 답변 ID (루트 답변인 경우 null)
 * @param writerId 작성자 ID
 * @param writerType 작성자 유형 (SELLER, CUSTOMER, ADMIN)
 * @param writerName 작성자 이름
 * @param content 답변 내용
 * @param path 답변 경로 (Materialized Path, 예: "001/001/001")
 * @param depth 답변 깊이 (0부터 시작)
 * @param createdAt 생성일시
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "문의 답변 응답")
public record QnaReplyV2ApiResponse(
        @Schema(description = "답변 ID", example = "1")
        Long id,

        @Schema(description = "문의 ID", example = "1")
        Long qnaId,

        @Schema(description = "부모 답변 ID (루트 답변인 경우 null)", example = "null")
        Long parentReplyId,

        @Schema(description = "작성자 ID", example = "01912345-6789-7abc-def0-123456789012")
        String writerId,

        @Schema(description = "작성자 유형 (SELLER, CUSTOMER, ADMIN)", example = "SELLER")
        String writerType,

        @Schema(description = "작성자 이름", example = "관리자")
        String writerName,

        @Schema(description = "답변 내용", example = "해당 상품의 M 사이즈 실측은...")
        String content,

        @Schema(description = "답변 경로 (Materialized Path)", example = "001/001")
        String path,

        @Schema(description = "답변 깊이 (0부터 시작)", example = "1")
        int depth,

        @Schema(description = "생성일시")
        LocalDateTime createdAt) {

    public static QnaReplyV2ApiResponse from(QnaReplyResponse response) {
        return new QnaReplyV2ApiResponse(
                response.id(),
                response.qnaId(),
                response.parentReplyId(),
                response.writerId(),
                response.writerType(),
                response.writerName(),
                response.content(),
                response.path(),
                response.depth(),
                response.createdAt());
    }
}
