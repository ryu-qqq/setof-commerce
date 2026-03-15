package com.ryuqq.setof.domain.imagevariant.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/** 이미지 Variant 도메인 예외. */
public class ImageVariantException extends DomainException {

    public ImageVariantException(ImageVariantErrorCode errorCode) {
        super(errorCode);
    }

    public ImageVariantException(ImageVariantErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }

    public ImageVariantException(
            ImageVariantErrorCode errorCode, String customMessage, Map<String, Object> args) {
        super(errorCode, customMessage, args);
    }

    public ImageVariantException(ImageVariantErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
