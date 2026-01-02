package com.connectly.partnerAdmin.module.qna.enums;

import com.connectly.partnerAdmin.module.common.enums.EnumType;

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
