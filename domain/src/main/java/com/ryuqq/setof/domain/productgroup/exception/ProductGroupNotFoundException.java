package com.ryuqq.setof.domain.productgroup.exception;

/** 상품그룹을 찾을 수 없을 때 발생하는 예외. */
public class ProductGroupNotFoundException extends ProductGroupException {

    public ProductGroupNotFoundException(Long productGroupId) {
        super(
                ProductGroupErrorCode.PRODUCT_GROUP_NOT_FOUND,
                String.format("상품그룹을 찾을 수 없습니다: %d", productGroupId));
    }
}
