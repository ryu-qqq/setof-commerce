package com.connectly.partnerAdmin.module.external.dto.order.oco;


import com.connectly.partnerAdmin.module.external.exception.InvalidExternalOrderStatusException;
import com.connectly.partnerAdmin.module.external.core.ExMallOrder;
import com.connectly.partnerAdmin.module.external.core.ExMallShipping;
import com.connectly.partnerAdmin.module.external.core.ExMallUser;
import com.connectly.partnerAdmin.module.external.dto.user.ExMallUserInfo;
import com.connectly.partnerAdmin.module.order.enums.OrderStatus;
import com.connectly.partnerAdmin.module.order.enums.SiteName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@JsonTypeName("ocoOrder")
public class OcoOrder implements ExMallOrder, ExMallShipping {

    private long oid;

    @JsonProperty("ordernumber")
    private String orderNumber;

    @JsonProperty("order_type")
    private String orderType;

    @JsonProperty("ordername")
    private String orderName;

    @JsonProperty("orderemail")
    private String orderEmail;

    @JsonProperty("ordercelphone")
    private String orderCelphone;

    private int status;

    @JsonProperty("deliveryaddress1")
    private String deliveryAddress1;

    @JsonProperty("deliveryaddress2")
    private String deliveryAddress2;

    @JsonProperty("deliverymemo")
    private String deliveryMemo;

    @JsonProperty("deliveryzipcode")
    private String deliveryZipcode;

    @JsonProperty("payment_date")
    private String paymentDate;

    @JsonProperty("paymenttype")
    private int paymentType;

    @JsonProperty("recipientcelphone")
    private String recipientCelphone;

    @JsonProperty("recipientname")
    private String recipientName;

    private List<OcoOrderItem> orderItemList;


    @Override
    public long getSiteId() {
        return SiteName.OCO.getSiteId();
    }

    @Override
    public SiteName getSiteName() {
        return SiteName.OCO;
    }


    @Override
    public String getBuyerName() {
        return orderName;
    }

    @Override
    public String getBuyerPhoneNumber() {
        return orderCelphone;
    }

    @Override
    public OrderStatus getOrderStatus() {
        if(status == 1) return OrderStatus.ORDER_COMPLETED;
        else if (status == 4) return OrderStatus.CANCEL_REQUEST_COMPLETED;

        throw new InvalidExternalOrderStatusException(getSiteName(), String.valueOf(status));
    }

    @Override
    public String getBuyerEmail() {
        return orderEmail;
    }


    @Override
    public String getZipCode() {
        return deliveryZipcode;
    }

    @Override
    public String getDeliveryAddress() {
        return deliveryAddress1 + " " + deliveryAddress2;
    }

    @Override
    public String getReceiverName() {
        return recipientName;
    }

    @Override
    public String getReceiverPhoneNumber() {
        return recipientCelphone;
    }

    @Override
    public String getDeliveryRequest() {
        return deliveryMemo;
    }


    @Override
    public ExMallUser getExMallUser() {
        return new ExMallUserInfo(orderName, orderCelphone, getSiteName());
    }

    @Override
    public long getExMallOrderId() {
        return oid;
    }

    @Override
    public String getExMallOrderCode() {
        return orderNumber;
    }

    @Override
    public List<Long> getProductGroupIds() {
        return orderItemList.stream()
                .map(OcoOrderItem::getPid)
                .collect(Collectors.toList());
    }

}
