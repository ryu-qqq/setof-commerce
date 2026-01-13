package com.ryuqq.setof.batch.legacy.external.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/** SELLIC 주문 DTO */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SellicOrder {

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
    private String receiverCel;

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

    @JsonProperty("OWN_CODE")
    private String productGroupId;

    @JsonProperty("SELLIC_MALL_ID")
    private long siteId;

    @JsonProperty("MALL_ID")
    private String mallId;

    public SellicOrder() {}

    public long getIdx() {
        return idx;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getOrderSubId() {
        return orderSubId;
    }

    public long getOrderStatus() {
        return orderStatus;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public long getOrderType() {
        return orderType;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserTel() {
        return userTel;
    }

    public String getUserCel() {
        return userCel;
    }

    public String getDeliveryMsg() {
        return deliveryMsg;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public String getReceiverCel() {
        return receiverCel;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getReceiveAddr() {
        return receiveAddr;
    }

    public long getPaymentAmount() {
        return paymentAmount;
    }

    public long getExternalProductGroupId() {
        return externalProductGroupId;
    }

    public String getMallProductId() {
        return mallProductId;
    }

    public String getOptionName() {
        return optionName;
    }

    public int getQty() {
        return qty;
    }

    public String getProductGroupId() {
        return productGroupId;
    }

    public long getSiteId() {
        return siteId;
    }

    public String getMallId() {
        return mallId;
    }

    /** Spring Admin API로 전송할 JSON 변환 */
    public String toAdminApiJson() {
        return """
               {
                   "type": "sellicOrder",
                   "IDX": %d,
                   "ORDER_ID": "%s",
                   "ORDER_SUB_ID": "%s",
                   "ORDER_STATUS": %d,
                   "ORDER_DATE": "%s",
                   "ORDER_TYPE": %d,
                   "USER_NAME": "%s",
                   "USER_TEL": "%s",
                   "USER_CEL": "%s",
                   "DELV_MSG": "%s",
                   "RECEIVE_NAME": "%s",
                   "RECEIVE_CEL": "%s",
                   "RECEIVE_ZIPCODE": "%s",
                   "RECEIVE_ADDR": "%s",
                   "PAYMENT_PRICE": %d,
                   "PRODUCT_ID": %d,
                   "MALL_PRODUCT_ID": "%s",
                   "OPTION_NAME": "%s",
                   "SALE_CNT": %d,
                   "OWN_CODE": "%s",
                   "SELLIC_MALL_ID": %d,
                   "MALL_ID": "%s"
               }
               """
                .formatted(
                        idx,
                        orderId != null ? orderId : "",
                        orderSubId != null ? orderSubId : "",
                        orderStatus,
                        orderDate != null ? orderDate : "",
                        orderType,
                        userName != null ? userName : "",
                        userTel != null ? userTel : "",
                        userCel != null ? userCel : "",
                        deliveryMsg != null ? deliveryMsg : "",
                        receiveName != null ? receiveName : "",
                        receiverCel != null ? receiverCel : "",
                        zipCode != null ? zipCode : "",
                        receiveAddr != null ? receiveAddr.replace("\"", "\\\"") : "",
                        paymentAmount,
                        externalProductGroupId,
                        mallProductId != null ? mallProductId : "",
                        optionName != null ? optionName.replace("\"", "\\\"") : "",
                        qty,
                        productGroupId != null ? productGroupId : "",
                        siteId,
                        mallId != null ? mallId : "");
    }
}
