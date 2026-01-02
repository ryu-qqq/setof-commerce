package com.connectly.partnerAdmin.module.external.dto.order.buyma;


import com.connectly.partnerAdmin.module.external.core.ExMallOrder;
import com.connectly.partnerAdmin.module.external.core.ExMallOrderProduct;
import com.connectly.partnerAdmin.module.external.core.ExMallShipping;
import com.connectly.partnerAdmin.module.external.core.ExMallUser;
import com.connectly.partnerAdmin.module.external.dto.user.ExMallUserInfo;
import com.connectly.partnerAdmin.module.external.exception.InvalidExternalOrderStatusException;
import com.connectly.partnerAdmin.module.order.enums.OrderStatus;
import com.connectly.partnerAdmin.module.order.enums.SiteName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@JsonTypeName("buymaOrder")
public class BuymaOrder implements ExMallOrder, ExMallShipping, ExMallOrderProduct {

    @JsonProperty("status")
    private String status;

    @JsonProperty("id")
    private Long id;

    @JsonProperty("seller_id")
    private Long sellerId;

    @JsonProperty("customer_id")
    private Long customerId;

    @JsonProperty("product")
    private BuymaOrderItem product;

    @JsonProperty("shipping_deadline")
    private String shippingDeadline;

    @JsonProperty("recipient")
    private BuymaRecipient recipient;

    @JsonProperty("unit_price")
    private Integer unitPrice;

    @JsonProperty("subtotal_price")
    private Integer subtotalPrice;

    @JsonProperty("coupon")
    private BuymaCoupon coupon;

    @JsonProperty("amount")
    private Integer amount;

    @JsonProperty("request_shipment")
    private BuymaRequestShipment requestShipment;

    @JsonProperty("pre_ordered_at")
    private String preOrderedAt;

    @JsonProperty("payment_deadline")
    private String paymentDeadline;

    @JsonProperty("ordered_at")
    private String orderedAt;

    @JsonProperty("color_size_text")
    private String colorSizeText;

    @JsonProperty("order_message")
    private String orderMessage;

    @JsonProperty("order_shipment")
    private BuymaOrderShipment orderShipment;


    @Override
    public long getSiteId() {
        return SiteName.BUYMA.getSiteId();
    }

    @Override
    public SiteName getSiteName() {
        return SiteName.BUYMA;
    }


    @Override
    public String getBuyerName() {
        return recipient.getReceiverName();
    }

    @Override
    public String getBuyerPhoneNumber() {
        return recipient.getPhoneNumber();
    }

    @Override
    public OrderStatus getOrderStatus() {
        if(status.equals("new")) return OrderStatus.ORDER_COMPLETED;
        else if(status.equals("canceled")) return OrderStatus.CANCEL_REQUEST_COMPLETED;

        throw new InvalidExternalOrderStatusException(getSiteName(), status);
    }

    @Override
    public String getBuyerEmail() {
        return "";
    }


    @Override
    public String getZipCode() {
        return recipient.getZipCode();
    }

    @Override
    public String getDeliveryAddress() {
        return recipient.getDeliveryAddress();
    }

    @Override
    public String getReceiverName() {
        return recipient.getReceiverName();
    }

    @Override
    public String getReceiverPhoneNumber() {
        return recipient.getPhoneNumber();
    }

    @Override
    public String getDeliveryRequest() {
        return orderMessage;
    }


    @Override
    public ExMallUser getExMallUser() {
        return new ExMallUserInfo(recipient.getReceiverName(), recipient.getPhoneNumber(), getSiteName());
    }

    @Override
    public long getExMallOrderId() {
        return id;
    }

    @Override
    public String getExMallOrderCode() {
        return String.valueOf(id);
    }

    @Override
    public List<Long> getProductGroupIds() {
        return Collections.singletonList(product.getId());
    }

    @Override
    public String getOptionName() {
        return colorSizeText;
    }

    @Override
    public int getQuantity() {
        return amount;
    }
}
