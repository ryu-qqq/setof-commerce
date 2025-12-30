package com.connectly.partnerAdmin.module.qna.enums;

import com.connectly.partnerAdmin.module.common.enums.EnumType;

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
    public boolean isClosed(){
        return this.equals(CLOSED);
    }

}
