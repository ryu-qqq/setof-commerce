package com.connectly.partnerAdmin.module.qna.enums;

import com.connectly.partnerAdmin.module.common.enums.EnumType;

public enum QnaIssueType implements EnumType {
    QUESTION,
    ANSWER
    ;

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getDescription() {
        return name();
    }

    public boolean isQuestion(){
        return this.equals(QUESTION);
    }
}
