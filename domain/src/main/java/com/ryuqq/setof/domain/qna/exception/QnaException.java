package com.ryuqq.setof.domain.qna.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * QnaException - Q&A 도메인 예외 기본 클래스.
 *
 * <p>DOM-EXC-001: DomainException(RuntimeException) 상속 필수.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class QnaException extends DomainException {

    public QnaException(QnaErrorCode errorCode) {
        super(errorCode);
    }

    public QnaException(QnaErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }

    public QnaException(QnaErrorCode errorCode, String customMessage, Map<String, ?> args) {
        super(errorCode, customMessage);
    }

    public QnaException(QnaErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
