package com.ryuqq.setof.domain.qna.exception;

/**
 * QnaAnswerNotFoundException - Q&A 답변을 찾을 수 없을 때 발생하는 예외.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class QnaAnswerNotFoundException extends QnaException {

    public QnaAnswerNotFoundException(Long qnaAnswerId) {
        super(
                QnaErrorCode.QNA_ANSWER_NOT_FOUND,
                String.format("해당 QNA 답변이 존재하지 않습니다: %d", qnaAnswerId));
    }
}
