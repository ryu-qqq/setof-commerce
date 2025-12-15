package com.setof.connectly.module.qna.dto.query;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.setof.connectly.module.qna.entity.embedded.QnaContents;
import com.setof.connectly.module.qna.enums.QnaDetailType;
import com.setof.connectly.module.qna.enums.QnaType;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = CreateProductQna.class, name = "productQna"),
    @JsonSubTypes.Type(value = CreateOrderQna.class, name = "orderQna"),
})
public interface CreateQna {

    QnaContents getQnaContents();

    boolean isPrivate();

    QnaType getQnaType();

    QnaDetailType getQnaDetailType();

    Long getSellerId();

    long getTargetId();
}
