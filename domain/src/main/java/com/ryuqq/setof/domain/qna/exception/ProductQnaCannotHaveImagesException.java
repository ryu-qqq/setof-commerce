package com.ryuqq.setof.domain.qna.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

public class ProductQnaCannotHaveImagesException extends DomainException {

    public ProductQnaCannotHaveImagesException() {
        super(QnaErrorCode.PRODUCT_QNA_CANNOT_HAVE_IMAGES);
    }
}
