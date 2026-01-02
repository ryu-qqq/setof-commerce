package com.ryuqq.setof.domain.qna.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

public enum QnaErrorCode implements ErrorCode {

    QNA_NOT_FOUND("QNA-001", 404, "문의를 찾을 수 없습니다."),
    QNA_ALREADY_CLOSED("QNA-002", 400, "이미 종료된 문의입니다."),
    INVALID_WRITER_ID("QNA-003", 400, "유효하지 않은 작성자 ID입니다."),
    IMAGE_LIMIT_EXCEEDED("QNA-004", 400, "이미지는 최대 3개까지 첨부할 수 있습니다."),
    INVALID_QNA_CONTENT("QNA-005", 400, "유효하지 않은 문의 내용입니다."),
    INVALID_REPLY_PATH("QNA-006", 400, "유효하지 않은 답변 경로입니다."),
    QNA_REPLY_NOT_FOUND("QNA-007", 404, "답변을 찾을 수 없습니다."),
    PRODUCT_QNA_CANNOT_HAVE_IMAGES("QNA-008", 400, "상품 문의에는 이미지를 첨부할 수 없습니다.");

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
