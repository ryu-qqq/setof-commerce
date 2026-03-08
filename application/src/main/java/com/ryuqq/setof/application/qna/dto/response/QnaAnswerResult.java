package com.ryuqq.setof.application.qna.dto.response;

import java.time.LocalDateTime;

/**
 * QnaAnswerResult - Q&A 답변 조회 결과 DTO.
 *
 * <p>APP-DTO-004: Response DTO는 *Result 네이밍.
 *
 * @param qnaAnswerId 답변 ID
 * @param qnaParentId 부모 답변 ID (대댓글 지원, null 가능)
 * @param qnaWriterType 답변 작성자 유형 (SELLER / CUSTOMER)
 * @param title 답변 제목
 * @param content 답변 내용
 * @param insertDate 등록일
 * @param updateDate 수정일
 * @author ryu-qqq
 * @since 1.1.0
 */
public record QnaAnswerResult(
        long qnaAnswerId,
        Long qnaParentId,
        String qnaWriterType,
        String title,
        String content,
        LocalDateTime insertDate,
        LocalDateTime updateDate) {

    public static QnaAnswerResult of(
            long qnaAnswerId,
            Long qnaParentId,
            String qnaWriterType,
            String title,
            String content,
            LocalDateTime insertDate,
            LocalDateTime updateDate) {
        return new QnaAnswerResult(
                qnaAnswerId, qnaParentId, qnaWriterType, title, content, insertDate, updateDate);
    }
}
