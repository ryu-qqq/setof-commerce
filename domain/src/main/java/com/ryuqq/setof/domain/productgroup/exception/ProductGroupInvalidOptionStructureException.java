package com.ryuqq.setof.domain.productgroup.exception;

import com.ryuqq.setof.domain.productgroup.vo.OptionType;

/** OptionType과 옵션 그룹 수가 일치하지 않을 때 발생하는 예외. */
public class ProductGroupInvalidOptionStructureException extends ProductGroupException {

    public ProductGroupInvalidOptionStructureException(
            OptionType optionType, int expectedCount, int actualCount) {
        super(
                ProductGroupErrorCode.INVALID_OPTION_STRUCTURE,
                String.format(
                        "옵션 타입 %s는 옵션 그룹 %d개가 필요하지만 %d개가 전달되었습니다",
                        optionType, expectedCount, actualCount));
    }
}
