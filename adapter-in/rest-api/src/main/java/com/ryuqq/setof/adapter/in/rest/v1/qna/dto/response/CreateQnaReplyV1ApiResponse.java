package com.ryuqq.setof.adapter.in.rest.v1.qna.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * CreateQnaReplyV1ApiResponse - Q&A 답변 등록/수정 응답 DTO.
 *
 * <p>레거시 QnaAnswer(엔티티 직렬화) 기반 변환. POST /api/v1/qna/{qnaId}/reply 및 PUT
 * /api/v1/qna/{qnaId}/reply/{qnaAnswerId} 응답에 공통으로 사용.
 *
 * <p>레거시에서는 QnaAnswer 엔티티를 직접 반환했으나, 신규 아키텍처에서는 전용 Response DTO를 사용합니다.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-003: Response는 @Schema 어노테이션.
 *
 * @param qnaAnswerId 답변 ID
 * @param qnaId 연결된 Q&A ID
 * @param parentAnswerId 부모 답변 ID (대댓글인 경우, null 가능)
 * @param writerType 작성자 유형 (SELLER / CUSTOMER)
 * @param answerStatus 답변 상태 (OPEN / CLOSED)
 * @param title 답변 제목
 * @param content 답변 내용
 * @param insertDate 등록일시
 * @param updateDate 수정일시
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "Q&A 답변 등록/수정 응답")
public record CreateQnaReplyV1ApiResponse(
        @Schema(description = "답변 ID", example = "321") long qnaAnswerId,
        @Schema(description = "연결된 Q&A ID", example = "42") long qnaId,
        @Schema(description = "부모 답변 ID (대댓글인 경우)", example = "null", nullable = true)
                Long parentAnswerId,
        @Schema(
                        description = "작성자 유형",
                        example = "CUSTOMER",
                        allowableValues = {"SELLER", "CUSTOMER"})
                String writerType,
        @Schema(
                        description = "답변 상태",
                        example = "OPEN",
                        allowableValues = {"OPEN", "CLOSED"})
                String answerStatus,
        @Schema(description = "답변 제목", example = "문의 답변드립니다") String title,
        @Schema(description = "답변 내용", example = "해당 상품은 S, M, L 사이즈 모두 재고 있습니다.") String content,
        @Schema(description = "등록일시", example = "2024-01-01T00:00:00") LocalDateTime insertDate,
        @Schema(description = "수정일시", example = "2024-01-01T00:00:00") LocalDateTime updateDate) {}
