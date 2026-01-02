package com.connectly.partnerAdmin.module.qna.dto.fetch;


import com.connectly.partnerAdmin.module.brand.core.BaseBrandContext;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductQnaTarget implements QnaTarget{

    private long productGroupId;
    private String productGroupName;
    private String productGroupMainImageUrl;
    private BaseBrandContext brand;

    @QueryProjection
    public ProductQnaTarget(long productGroupId, String productGroupName, String productGroupMainImageUrl, BaseBrandContext brand) {
        this.productGroupId = productGroupId;
        this.productGroupName = productGroupName;
        this.productGroupMainImageUrl = productGroupMainImageUrl;
        this.brand = brand;
    }
}
