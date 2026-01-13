package com.connectly.partnerAdmin.module.external.dto.order.sellic;


import com.connectly.partnerAdmin.module.external.exception.InvalidExternalOrderStatusException;
import com.connectly.partnerAdmin.module.external.core.ExMallOrder;
import com.connectly.partnerAdmin.module.external.core.ExMallOrderProduct;
import com.connectly.partnerAdmin.module.external.core.ExMallShipping;
import com.connectly.partnerAdmin.module.external.core.ExMallUser;
import com.connectly.partnerAdmin.module.external.dto.user.ExMallUserInfo;
import com.connectly.partnerAdmin.module.order.enums.OrderStatus;
import com.connectly.partnerAdmin.module.order.enums.SiteName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonTypeName("sellicOrder")
public class SellicOrder implements ExMallOrder, ExMallShipping, ExMallOrderProduct {

    @JsonProperty("IDX")
    private long idx;

    @JsonProperty("ORDER_ID")
    private String orderId;

    @JsonProperty("ORDER_SUB_ID")
    private String orderSubId;

    @JsonProperty("ORDER_STATUS")
    private long orderStatus;

    @JsonProperty("ORDER_DATE")
    private String orderDate;

    @JsonProperty("ORDER_TYPE")
    private long orderType;

    @JsonProperty("USER_NAME")
    private String userName;

    @JsonProperty("USER_TEL")
    private String userTel;

    @JsonProperty("USER_CEL")
    private String userCel;

    @JsonProperty("DELV_MSG")
    private String deliveryMsg;

    @JsonProperty("RECEIVE_NAME")
    private String receiveName;

    @JsonProperty("RECEIVE_CEL")
    private String recevierCel;

    @JsonProperty("RECEIVE_ZIPCODE")
    private String zipCode;

    @JsonProperty("RECEIVE_ADDR")
    private String receiveAddr;

    @JsonProperty("PAYMENT_PRICE")
    private long paymentAmount;

    @JsonProperty("PRODUCT_ID")
    private long externalProductGroupId;

    @JsonProperty("MALL_PRODUCT_ID")
    private String mallProductId;

    @JsonProperty("OPTION_NAME")
    private String optionName;

    @JsonProperty("SALE_CNT")
    private int qty;

    @Setter
    @JsonProperty("OWN_CODE")
    private String productGroupId;

    @JsonProperty("SELLIC_MALL_ID")
    private long siteId;

    @JsonProperty("MALL_ID")
    private String mallId;

    public Long getProductGroupId() {
        return Long.parseLong(productGroupId);
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
    public String getBuyerName() {
        return userName;
    }

    @Override
    public String getBuyerPhoneNumber() {
        return userCel;
    }

    @Override
    public String getBuyerEmail() {
        return "";
    }

    @Override
    public ExMallUser getExMallUser() {
        return new ExMallUserInfo(userName, userCel, getSiteName());
    }

    @Override
    public long getExMallOrderId() {
        return idx;
    }

    @Override
    public String getExMallOrderCode() {
        if (StringUtils.hasText(orderSubId)) {
            return orderId + "_" + orderSubId;
        }
        return orderId;
    }

    @Override
    public List<Long> getProductGroupIds() {
        return Collections.singletonList(Long.parseLong(productGroupId));
    }

    @Override
    public String getZipCode(){
        return zipCode;
    }

    @Override
    public String getDeliveryAddress() {
        return receiveAddr;
    }

    @Override
    public String getReceiverName() {
        return receiveName;
    }

    @Override
    public String getReceiverPhoneNumber() {
        return recevierCel.replaceAll("-","");
    }

    @Override
    public String getDeliveryRequest() {
        return deliveryMsg;
    }

    @Override
    public OrderStatus getOrderStatus() {
        if(orderStatus == 1443) return OrderStatus.ORDER_COMPLETED;
        else if (orderStatus == 1528) return OrderStatus.RETURN_REQUEST;

        throw new InvalidExternalOrderStatusException(getSiteName(), String.valueOf(orderStatus));
    }

    @Override
    public int getQuantity() {
        return qty;
    }

    public String getOptionName() {
        if(StringUtils.hasText(optionName)){
            return optionName.replace("멀티/","");
        }
        return "";
    }

}
