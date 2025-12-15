package com.setof.connectly.module.qna.enums;

import com.setof.connectly.module.common.enums.EnumType;

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
