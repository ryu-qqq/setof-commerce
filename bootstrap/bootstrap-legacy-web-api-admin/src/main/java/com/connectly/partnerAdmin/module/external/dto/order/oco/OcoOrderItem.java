package com.connectly.partnerAdmin.module.external.dto.order.oco;

import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.external.core.ExMallOrderProduct;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OcoOrderItem implements ExMallOrderProduct {

    private long oid;
    private long pid;
    private long otid;
    private int amount;
    @JsonProperty("optionitem")
    private String optionItem;
    @JsonProperty("orderitem_cancel_yn")
    private String orderItemCancelYn;

    @JsonProperty("delivery_condition")
    private String deliveryCondition;
    private long productGroupId;


    public void setProductGroupId(long productGroupId) {
        this.productGroupId = productGroupId;
    }

    @Override
    public String getOptionName() {
        return optionItem;
    }

    @Override
    public int getQuantity() {
        return amount;
    }

    public boolean isCanceledOrder(){
        return this.orderItemCancelYn.equals(Yn.Y.getName());
    }


    public String getOptionItem(){
        return this.optionItem
                .replaceAll(" ", "")
                .replaceAll("/", "")
                .trim();
    }

}
