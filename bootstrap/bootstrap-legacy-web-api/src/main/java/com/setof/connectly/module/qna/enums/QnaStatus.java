package com.setof.connectly.module.qna.enums;

import com.setof.connectly.module.common.enums.EnumType;

public enum QnaStatus implements EnumType {

    OPEN,
    CLOSED;

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getDescription() {
        return name();
    }

    public boolean isOpen(){
        return this.equals(OPEN);
    }
}
