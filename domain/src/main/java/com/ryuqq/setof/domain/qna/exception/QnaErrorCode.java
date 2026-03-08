package com.ryuqq.setof.domain.qna.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/**
 * QnaErrorCode - Q&A 도메인 에러 코드.
 *
 * <p>DOM-ERR-001: ErrorCode 인터페이스 구현 필수. Spring HttpStatus 금지.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public enum QnaErrorCode implements ErrorCode {

    QNA_NOT_FOUND("QNA-001", 404, "Q&A를 찾을 수 없습니다"),
    QNA_ALREADY_ANSWERED("QNA-002", 409, "이미 답변이 등록된 Q&A입니다"),
    QNA_ALREADY_CLOSED("QNA-003", 400, "종료된 Q&A에는 작업을 수행할 수 없습니다"),
    QNA_IMAGE_LIMIT_EXCEEDED("QNA-004", 400, "Q&A 이미지 수가 최대 한도를 초과했습니다"),
    QNA_DUPLICATE_PENDING("QNA-005", 409, "동일 대상에 미답변 질문이 존재합니다"),
    QNA_IMAGE_NOT_ALLOWED("QNA-006", 400, "해당 유형의 Q&A에는 이미지 첨부가 불가합니다");

    private final String code;
    private final int httpStatus;
    private final String message;

    QnaErrorCode(String code, int httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public int getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
