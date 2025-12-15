package com.setof.connectly.module.qna.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.product.dto.brand.BrandDto;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderQnaTarget extends ProductQnaTarget {

    private long paymentId;
    private long orderId;

    private long orderAmount;
    private int quantity;

    @JsonIgnore private List<String> options = new ArrayList<>();
    private String option = "";

    @QueryProjection
    public OrderQnaTarget(
            long productGroupId,
            String productGroupName,
            String productGroupMainImageUrl,
            BrandDto brand,
            long paymentId,
            long orderId,
            long orderAmount,
            int quantity,
            List<String> options) {
        super(productGroupId, productGroupName, productGroupMainImageUrl, brand);
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.orderAmount = orderAmount;
        this.quantity = quantity;
        this.options = options;
    }

    public void setOption() {
        this.option = String.join(" ", options);
    }
}
