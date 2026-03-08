package com.ryuqq.setof.domain.qna.exception;

import com.ryuqq.setof.domain.qna.id.LegacyQnaId;

/**
 * QnaAlreadyClosedException - 종료된 Q&A에 작업 시도 시 발생.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class QnaAlreadyClosedException extends QnaException {

    public QnaAlreadyClosedException(LegacyQnaId legacyQnaId) {
        super(
                QnaErrorCode.QNA_ALREADY_CLOSED,
                String.format("종료된 Q&A에는 작업을 수행할 수 없습니다: %d", legacyQnaId.value()));
    }
}
