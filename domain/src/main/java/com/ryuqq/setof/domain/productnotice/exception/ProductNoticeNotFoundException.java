package com.ryuqq.setof.domain.productnotice.exception;

import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;

/** 상품고시를 찾을 수 없을 때 발생하는 예외. */
public class ProductNoticeNotFoundException extends ProductNoticeException {

    public ProductNoticeNotFoundException(ProductGroupId productGroupId) {
        super(
                ProductNoticeErrorCode.NOTICE_NOT_FOUND,
                String.format("상품고시를 찾을 수 없습니다: productGroupId=%d", productGroupId.value()));
    }
}
