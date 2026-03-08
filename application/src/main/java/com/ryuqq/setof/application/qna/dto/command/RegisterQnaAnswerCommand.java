package com.ryuqq.setof.application.qna.dto.command;

/**
 * RegisterQnaAnswerCommand - Q&A 답변 등록 커맨드 DTO.
 *
 * <p>POST /api/v1/qna/{qnaId}/reply 엔드포인트 대응. 답변 등록 시 Q&A 상태가 ANSWERED로 전환됨.
 *
 * @param qnaId 대상 Q&A 레거시 ID
 * @param sellerId 답변 판매자 ID
 * @param content 답변 내용
 * @author ryu-qqq
 * @since 1.1.0
 */
public record RegisterQnaAnswerCommand(Long qnaId, Long sellerId, String content) {

    public static RegisterQnaAnswerCommand of(Long qnaId, Long sellerId, String content) {
        return new RegisterQnaAnswerCommand(qnaId, sellerId, content);
    }
}
