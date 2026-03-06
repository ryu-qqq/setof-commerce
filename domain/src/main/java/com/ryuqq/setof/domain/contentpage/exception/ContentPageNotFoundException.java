package com.ryuqq.setof.domain.contentpage.exception;

/**
 * 콘텐츠 페이지를 찾을 수 없는 경우 예외.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class ContentPageNotFoundException extends ContentPageException {

    private static final ContentPageErrorCode ERROR_CODE =
            ContentPageErrorCode.CONTENT_PAGE_NOT_FOUND;

    public ContentPageNotFoundException() {
        super(ERROR_CODE);
    }

    public ContentPageNotFoundException(String detail) {
        super(ERROR_CODE, String.format("콘텐츠 페이지를 찾을 수 없습니다: %s", detail));
    }
}
