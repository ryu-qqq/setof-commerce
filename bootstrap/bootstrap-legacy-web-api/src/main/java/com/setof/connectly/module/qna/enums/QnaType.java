package com.setof.connectly.module.qna.enums;

import com.setof.connectly.module.common.enums.EnumType;

public enum QnaType implements EnumType {
    PRODUCT,
    ORDER;



    public boolean isProductQna(){
        return this.equals(PRODUCT);
    }

    public boolean isOrderQna(){
        return this.equals(ORDER);
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getDescription() {
        return name();
    }
}
