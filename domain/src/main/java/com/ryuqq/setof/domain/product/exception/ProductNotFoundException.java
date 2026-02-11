package com.ryuqq.setof.domain.product.exception;

/** 상품을 찾을 수 없을 때 발생하는 예외. */
public class ProductNotFoundException extends ProductException {

    public ProductNotFoundException(Long productId) {
        super(ProductErrorCode.PRODUCT_NOT_FOUND, String.format("상품을 찾을 수 없습니다: %d", productId));
    }
}
