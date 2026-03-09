package com.ryuqq.setof.adapter.out.sqs.exception;

/**
 * SQS 발행 예외.
 *
 * <p>SQS 메시지 발행 실패 시 발생하는 예외입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class SqsPublishException extends RuntimeException {

    public SqsPublishException(String message) {
        super(message);
    }

    public SqsPublishException(String message, Throwable cause) {
        super(message, cause);
    }
}
