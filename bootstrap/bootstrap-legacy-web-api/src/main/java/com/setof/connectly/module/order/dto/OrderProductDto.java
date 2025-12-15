package com.setof.connectly.module.order.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.common.mapper.LastDomainIdProvider;
import com.setof.connectly.module.order.enums.OrderStatus;
import com.setof.connectly.module.payment.dto.refund.RefundNotice;
import com.setof.connectly.module.product.dto.brand.BrandDto;
import com.setof.connectly.module.product.dto.option.OptionDto;
import com.setof.connectly.module.shipment.dto.PaymentShipmentInfo;
import com.setof.connectly.module.utils.NumberUtils;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class OrderProductDto implements LastDomainIdProvider {

    private long paymentId;
    private long sellerId;
    private long orderId;
    private BrandDto brand;
    private long productGroupId;
    private String productGroupName;
    private long productId;
    private String sellerName;
    private String productGroupMainImageUrl;
    private int productQuantity;
    private OrderStatus orderStatus;
    private long regularPrice;
    private long salePrice;
    private long discountPrice;
    private long directDiscountPrice;
    private long orderAmount;
    private String option = "";
    @JsonIgnore private Set<OptionDto> options = new HashSet<>();
    private double totalExpectedRefundMileageAmount;
    private RefundNotice refundNotice;
    private Yn reviewYn;
    private Yn claimRejected;
    private OrderRejectReason orderRejectReason;
    private PaymentShipmentInfo shipmentInfo;

    @QueryProjection
    public OrderProductDto(
            long paymentId,
            long sellerId,
            long orderId,
            BrandDto brand,
            long productGroupId,
            String productGroupName,
            long productId,
            String sellerName,
            String productGroupMainImageUrl,
            int productQuantity,
            OrderStatus orderStatus,
            long regularPrice,
            long salePrice,
            long directDiscountPrice,
            long orderAmount,
            Set<OptionDto> options,
            RefundNotice refundNotice,
            PaymentShipmentInfo shipmentInfo,
            Yn reviewYn) {
        this.paymentId = paymentId;
        this.sellerId = sellerId;
        this.orderId = orderId;
        this.brand = brand;
        this.productGroupId = productGroupId;
        this.productGroupName = productGroupName;
        this.productId = productId;
        this.sellerName = sellerName;
        this.productGroupMainImageUrl = productGroupMainImageUrl;
        this.productQuantity = productQuantity;
        this.orderStatus = orderStatus;
        this.regularPrice = regularPrice;
        this.salePrice = salePrice;
        this.directDiscountPrice = directDiscountPrice;
        this.discountPrice = regularPrice - salePrice;
        this.orderAmount = orderAmount;
        this.options = options;
        this.refundNotice = refundNotice;
        this.shipmentInfo = shipmentInfo;
        this.reviewYn = reviewYn;
        this.claimRejected = Yn.N;
        this.totalExpectedRefundMileageAmount = 0;
    }

    @QueryProjection
    public OrderProductDto(
            long paymentId,
            long sellerId,
            long orderId,
            BrandDto brand,
            long productGroupId,
            String productGroupName,
            long productId,
            String sellerName,
            String productGroupMainImageUrl,
            int productQuantity,
            OrderStatus orderStatus,
            long regularPrice,
            long salePrice,
            long orderAmount,
            Set<OptionDto> options) {
        this.paymentId = paymentId;
        this.sellerId = sellerId;
        this.orderId = orderId;
        this.brand = brand;
        this.productGroupId = productGroupId;
        this.productGroupName = productGroupName;
        this.productId = productId;
        this.sellerName = sellerName;
        this.productGroupMainImageUrl = productGroupMainImageUrl;
        this.productQuantity = productQuantity;
        this.orderStatus = orderStatus;
        this.regularPrice = regularPrice;
        this.salePrice = salePrice;
        this.discountPrice = regularPrice - salePrice;
        this.orderAmount = orderAmount;
        this.options = options;
    }

    @QueryProjection
    public OrderProductDto(long orderId, Set<OptionDto> options) {
        this.orderId = orderId;
        this.options = options;
    }

    @QueryProjection
    public OrderProductDto(
            long orderId, long productId, int productQuantity, OrderStatus orderStatus) {
        this.orderId = orderId;
        this.productId = productId;
        this.productQuantity = productQuantity;
        this.orderStatus = orderStatus;
    }

    public OrderProductDto setOption() {
        this.option =
                options.stream().map(OptionDto::getOptionValue).collect(Collectors.joining(" "));
        return this;
    }

    public OrderProductDto setOptions(Set<OptionDto> options) {
        this.options = options;
        setOption();
        return this;
    }

    public void setOrderRejectReason(OrderRejectReason orderRejectReason) {
        this.orderRejectReason = orderRejectReason;
    }

    public void setClaimRejected(Yn claimRejected) {
        this.claimRejected = claimRejected;
    }

    public void setTotalExpectedRefundMileageAmount(long payAmount, double usedMileageAmount) {
        if (usedMileageAmount > 0) {
            this.totalExpectedRefundMileageAmount =
                    NumberUtils.getProPortion(payAmount, orderAmount, usedMileageAmount)
                            .doubleValue();
        }
    }

    public long getTotalSaleAmount() {
        return discountPrice;
    }

    @Override
    public int hashCode() {
        return String.valueOf(orderId).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof OrderProductDto) {
            OrderProductDto p = (OrderProductDto) obj;
            return this.hashCode() == p.hashCode();
        }
        return false;
    }

    @Override
    public Long getId() {
        return orderId;
    }
}
