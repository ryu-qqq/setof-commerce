package com.ryuqq.setof.domain.brand.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 유효하지 않은 영문 브랜드명 예외
 *
 * <p>영문 브랜드명이 255자를 초과할 때 발생합니다.
 */
public class InvalidBrandNameEnException extends DomainException {

    public InvalidBrandNameEnException(String value) {
        super(
                BrandErrorCode.INVALID_BRAND_NAME_EN,
                String.format("영문 브랜드명이 올바르지 않습니다. value: %s", value),
                Map.of("value", value != null ? value : "null"));
    }
}
