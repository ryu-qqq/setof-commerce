package com.connectly.partnerAdmin.module.external.core;

import java.util.List;

import com.connectly.partnerAdmin.module.external.dto.order.ExcelOrderSheet;
import com.connectly.partnerAdmin.module.external.dto.order.buyma.BuymaOrder;
import com.connectly.partnerAdmin.module.external.dto.order.lf.LfOrder;
import com.connectly.partnerAdmin.module.external.dto.order.oco.OcoOrder;
import com.connectly.partnerAdmin.module.external.dto.order.sellic.SellicOrder;
import com.connectly.partnerAdmin.module.order.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", defaultImpl = OcoOrder.class)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SellicOrder.class, name = "sellicOrder"),
        @JsonSubTypes.Type(value = OcoOrder.class, name = "ocoOrder"),
        @JsonSubTypes.Type(value = ExcelOrderSheet.class, name = "excelOrderSheet"),
        @JsonSubTypes.Type(value = BuymaOrder.class, name = "buymaOrder"),
        @JsonSubTypes.Type(value = LfOrder.class, name = "lfOrder"),


})
public interface ExMallOrder extends ExMall {

    String getBuyerName();
    String getBuyerPhoneNumber();
    OrderStatus getOrderStatus();
    String getBuyerEmail();
    ExMallUser getExMallUser();

    long getExMallOrderId();
    String getExMallOrderCode();
    List<Long> getProductGroupIds();
}
