package com.ryuqq.setof.domain.qna.exception;

import com.ryuqq.setof.domain.qna.id.LegacyQnaId;

/**
 * QnaAlreadyAnsweredException - 이미 답변이 등록된 Q&A에 답변 시도 시 발생.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class QnaAlreadyAnsweredException extends QnaException {

    public QnaAlreadyAnsweredException(LegacyQnaId legacyQnaId) {
        super(
                QnaErrorCode.QNA_ALREADY_ANSWERED,
                String.format("이미 답변이 등록된 Q&A입니다: %d", legacyQnaId.value()));
    }
}
