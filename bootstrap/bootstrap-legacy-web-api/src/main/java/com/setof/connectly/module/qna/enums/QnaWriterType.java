package com.setof.connectly.module.qna.enums;

import com.setof.connectly.module.common.enums.EnumType;

public enum QnaWriterType implements EnumType {

    SELLER,
    CUSTOMER;

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getDescription() {
        return name();
    }
}
