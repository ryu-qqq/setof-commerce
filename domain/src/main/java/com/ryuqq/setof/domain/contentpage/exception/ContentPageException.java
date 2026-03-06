package com.ryuqq.setof.domain.contentpage.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

/** 콘텐츠 페이지 도메인 예외. */
public class ContentPageException extends DomainException {

    public ContentPageException(ContentPageErrorCode errorCode) {
        super(errorCode);
    }

    public ContentPageException(ContentPageErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }

    public ContentPageException(ContentPageErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
