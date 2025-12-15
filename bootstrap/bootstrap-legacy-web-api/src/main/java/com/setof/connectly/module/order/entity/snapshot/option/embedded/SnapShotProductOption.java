package com.setof.connectly.module.order.entity.snapshot.option.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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
    private long additionalPrice;
}
