package com.ryuqq.setof.domain.seller.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 잘못된 셀러 설명 예외
 *
 * <p>셀러 설명이 최대 길이를 초과한 경우 발생합니다.
 */
public class InvalidDescriptionException extends DomainException {

    public InvalidDescriptionException(String value) {
        super(
                SellerErrorCode.INVALID_DESCRIPTION,
                "셀러 설명은 2000자를 초과할 수 없습니다.",
                Map.of("length", value != null ? value.length() : 0));
    }
}
