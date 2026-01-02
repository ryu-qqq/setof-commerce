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
    @Column(name = "product_option_id")
    private long productOptionId;

    @Column(name = "product_id")
    private long productId;

    @Column(name = "option_group_id")
    private long optionGroupId;

    @Column(name = "option_detail_id")
    private long optionDetailId;

    @Column(name = "additional_price")
    private long additionalPrice;
}
