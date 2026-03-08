package com.ryuqq.setof.application.qna.dto.response;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * QnaAnswerDetailResult - Q&A 답변 상세 결과 DTO.
 *
 * <p>상품 Q&A 응답 내 answers 필드에 사용됩니다.
 *
 * @param qnaAnswerId 답변 ID
 * @param qnaParentId 부모 답변 ID (대댓글, null 가능)
 * @param qnaWriterType 답변 작성자 유형 (SELLER / CUSTOMER)
 * @param title 답변 제목
 * @param content 답변 내용
 * @param insertDate 등록일
 * @param updateDate 수정일
 * @author ryu-qqq
 * @since 1.1.0
 */
public record QnaAnswerDetailResult(
        long qnaAnswerId,
        Long qnaParentId,
        String qnaWriterType,
        String title,
        String content,
        LocalDateTime insertDate,
        LocalDateTime updateDate) {

    public static QnaAnswerDetailResult of(
            long qnaAnswerId,
            Long qnaParentId,
            String qnaWriterType,
            String title,
            String content,
            LocalDateTime insertDate,
            LocalDateTime updateDate) {
        return new QnaAnswerDetailResult(
                qnaAnswerId, qnaParentId, qnaWriterType, title, content, insertDate, updateDate);
    }
}
