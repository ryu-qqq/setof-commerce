package com.connectly.partnerAdmin.module.qna.dto.query;

import com.connectly.partnerAdmin.module.qna.enums.QnaDetailType;
import com.connectly.partnerAdmin.module.qna.enums.QnaType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CreateProductQna.class, name = "productQna"),
        @JsonSubTypes.Type(value = CreateOrderQna.class, name = "orderQna"),

})
public interface CreateQna {

    CreateQnaContents getQnaContents();
    boolean isPrivate();
    QnaType getQnaType();
    QnaDetailType getQnaDetailType();
    long getSellerId();
    long getTargetId();


}
