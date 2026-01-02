package com.connectly.partnerAdmin.module.order.entity.snapshot.option.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SnapShotProductOption {

    @Column(name = "PRODUCT_OPTION_ID")
    private long productOptionId;

    @Column(name = "PRODUCT_ID")
    private long productId;

    @Column(name = "OPTION_GROUP_ID")
    private long optionGroupId;

    @Column(name = "OPTION_DETAIL_ID")
    private long optionDetailId;

    @Column(name = "ADDITIONAL_PRICE")
    private BigDecimal additionalPrice;

    public SnapShotProductOption(long productOptionId, long productId, long optionGroupId, long optionDetailId, BigDecimal additionalPrice) {
        this.productOptionId = productOptionId;
        this.productId = productId;
        this.optionGroupId = optionGroupId;
        this.optionDetailId = optionDetailId;
        this.additionalPrice = additionalPrice;
    }
}
