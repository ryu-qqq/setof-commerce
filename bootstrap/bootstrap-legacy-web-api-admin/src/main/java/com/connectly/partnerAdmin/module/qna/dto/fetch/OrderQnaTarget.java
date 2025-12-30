package com.connectly.partnerAdmin.module.qna.dto.fetch;

import com.connectly.partnerAdmin.module.brand.core.BaseBrandContext;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderQnaTarget extends ProductQnaTarget{

    private long paymentId;
    private long orderId;
    @JsonIgnore
    private List<String> options= new ArrayList<>();
    private String option= "";

    @QueryProjection
    public OrderQnaTarget(long productGroupId, String productGroupName, String productGroupMainImageUrl, BaseBrandContext brand, long paymentId, long orderId, List<String> options) {
        super(productGroupId, productGroupName, productGroupMainImageUrl, brand);
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.options = options;
        this.option = String.join(" ", options);
    }

}
