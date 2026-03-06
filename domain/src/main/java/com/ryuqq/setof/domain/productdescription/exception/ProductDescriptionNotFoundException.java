package com.ryuqq.setof.domain.productdescription.exception;

import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;

/** 상세설명을 찾을 수 없을 때 발생하는 예외. */
public class ProductDescriptionNotFoundException extends ProductDescriptionException {

    public ProductDescriptionNotFoundException(ProductGroupId productGroupId) {
        super(
                ProductDescriptionErrorCode.DESCRIPTION_NOT_FOUND,
                String.format("상세설명을 찾을 수 없습니다: productGroupId=%d", productGroupId.value()));
    }
}
