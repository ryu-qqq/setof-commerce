package com.connectly.partnerAdmin.module.order.dto;

import com.connectly.partnerAdmin.module.brand.core.BaseBrandContext;
import com.connectly.partnerAdmin.module.generic.money.Money;
import com.connectly.partnerAdmin.module.order.enums.OrderStatus;
import com.connectly.partnerAdmin.module.product.dto.option.OptionDto;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderProduct {
    private long orderId;
    private ProductGroupSnapShotDetails productGroupDetails;
    private BaseBrandContext brand;
    private long productGroupId;
    private long productId;
    private String sellerName;
    private String productGroupMainImageUrl;
    private String deliveryArea;
    private int productQuantity;
    private OrderStatus orderStatus;
    private Money regularPrice;
    private Money orderAmount;
    private Money totalExpectedRefundMileageAmount;
    private String option= "";
    private String skuNumber ="";
    private Set<OptionDto> options;

    @QueryProjection
    public OrderProduct(long orderId, ProductGroupSnapShotDetails productGroupDetails, BaseBrandContext brand, long productGroupId, long productId, String sellerName, String productGroupMainImageUrl, String deliveryArea, int productQuantity, OrderStatus orderStatus, BigDecimal regularPrice, BigDecimal orderAmount, BigDecimal totalExpectedRefundMileageAmount, Set<OptionDto> options) {
        this.orderId = orderId;
        this.productGroupDetails = productGroupDetails;
        this.brand = brand;
        this.productGroupId = productGroupId;
        this.productId = productId;
        this.sellerName = sellerName;
        this.productGroupMainImageUrl = productGroupMainImageUrl;
        this.deliveryArea = deliveryArea;
        this.productQuantity = productQuantity;
        this.orderStatus = orderStatus;
        this.regularPrice = Money.wons(regularPrice);
        this.orderAmount = Money.wons(orderAmount);
        this.totalExpectedRefundMileageAmount = Money.wons(totalExpectedRefundMileageAmount);
        setOptions(options);
        setOption();
        setSkuNumber();
    }

    private void setOptions(Set<OptionDto> options) {
        if(options == null || options.isEmpty()) {
            this.options = new HashSet<>();
        }else{
            this.options = options;
        }
    }

    private void setOption() {
        if(options == null || options.isEmpty()) {
            this.option =  "";
        }else{
            this.option = options.stream()
                    .map(OptionDto::getOptionValue)
                    .collect(Collectors.joining(" "));
        }
    }

    private void setSkuNumber() {
        String styleCode = productGroupDetails.getClothesDetailInfo().getStyleCode();
        StringBuilder stringBuilder = new StringBuilder();

        if (StringUtils.hasText(styleCode)) {
            stringBuilder.append(styleCode);
        }

        if (StringUtils.hasText(option)) {
            if (!stringBuilder.isEmpty()) {
                stringBuilder.append("_");
            }
            stringBuilder.append(option);
        }

        this.skuNumber = !stringBuilder.isEmpty() ? stringBuilder.toString() : "";
    }

    @Override
    public int hashCode() {
        return String.valueOf(orderId).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof OrderProduct) {
            OrderProduct p = (OrderProduct)obj;
            return this.hashCode()==p.hashCode();
        }
        return false;
    }

}

