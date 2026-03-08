package com.ryuqq.setof.domain.qna.exception;

import com.ryuqq.setof.domain.qna.id.LegacyQnaId;
import com.ryuqq.setof.domain.qna.id.QnaId;

/**
 * QnaNotFoundException - Q&A를 찾을 수 없을 때 발생하는 예외.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class QnaNotFoundException extends QnaException {

    public QnaNotFoundException(QnaId qnaId) {
        super(
                QnaErrorCode.QNA_NOT_FOUND,
                String.format("Q&A를 찾을 수 없습니다: %s", qnaId.value()));
    }

    public QnaNotFoundException(LegacyQnaId legacyQnaId) {
        super(
                QnaErrorCode.QNA_NOT_FOUND,
                String.format("Q&A를 찾을 수 없습니다: %d", legacyQnaId.value()));
    }
}
