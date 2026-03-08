package com.ryuqq.setof.application.qna.dto.command;

/**
 * ModifyQnaAnswerCommand - Q&A 답변 수정 커맨드 DTO.
 *
 * <p>PUT /api/v1/qna/{qnaId}/reply/{qnaAnswerId} 엔드포인트 대응.
 *
 * @param qnaId 대상 Q&A 레거시 ID
 * @param sellerId 요청자 판매자 ID (소유자 검증용)
 * @param content 수정할 답변 내용
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ModifyQnaAnswerCommand(Long qnaId, Long sellerId, String content) {

    public static ModifyQnaAnswerCommand of(Long qnaId, Long sellerId, String content) {
        return new ModifyQnaAnswerCommand(qnaId, sellerId, content);
    }
}
