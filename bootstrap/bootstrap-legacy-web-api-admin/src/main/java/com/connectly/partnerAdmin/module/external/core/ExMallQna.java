package com.connectly.partnerAdmin.module.external.core;


import com.connectly.partnerAdmin.module.external.dto.qna.OcoQna;
import com.connectly.partnerAdmin.module.external.dto.qna.SellicQna;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = SellicQna.class, name = "sellicQna"),
        @JsonSubTypes.Type(value = OcoQna.class, name = "ocoQna"),

})
public interface ExMallQna extends ExMall{

    long getExternalIdx();
    String getTitle();
    String getContent();
    long getTargetId();


}
