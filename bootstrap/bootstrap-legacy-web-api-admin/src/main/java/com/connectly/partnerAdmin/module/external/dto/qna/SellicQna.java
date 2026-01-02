package com.connectly.partnerAdmin.module.external.dto.qna;


import com.connectly.partnerAdmin.module.external.core.ExMallQna;
import com.connectly.partnerAdmin.module.order.enums.SiteName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonTypeName("sellicQna")
public class SellicQna implements ExMallQna {

    @JsonProperty("ID")
    private long id;

    @JsonProperty("MALL_INQUIRY_ID")
    private String externalQnaId;

    @JsonProperty("MALL_PRODUCT_ID")
    private String externalProductGroupId;

    @JsonProperty("MALL_PRODUCT_NAME")
    private String productGroupName;

    @JsonProperty("MALL_ORDER_ID")
    private String externalOrderId;

    @JsonProperty("INQUIRY_TYPE")
    private int inquiryType;

    @JsonProperty("INQUIRY_AT")
    private String inquiryAt;

    @JsonProperty("INQUIRY_NAME")
    private String inquiryName;

    @JsonProperty("SUBJECT")
    private String title;

    @JsonProperty("NOTE")
    private String content;


    public boolean iSProductQna(){
        return this.inquiryType == 1;
    }

    @Override
    public long getSiteId() {
        return SiteName.SEWON.getSiteId();
    }

    @Override
    public SiteName getSiteName() {
        return SiteName.SEWON;
    }

    @Override
    public long getExternalIdx() {
        return Long.parseLong(externalQnaId);
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public long getTargetId() {
        return Long.parseLong(externalProductGroupId);
    }
}
