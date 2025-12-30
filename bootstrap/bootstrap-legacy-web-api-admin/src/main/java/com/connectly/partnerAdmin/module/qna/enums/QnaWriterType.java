package com.connectly.partnerAdmin.module.qna.enums;

import com.connectly.partnerAdmin.module.common.enums.EnumType;

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
