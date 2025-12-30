package com.connectly.partnerAdmin.module.order.dto;

import com.connectly.partnerAdmin.module.product.entity.option.OptionDetail;
import com.connectly.partnerAdmin.module.product.entity.option.OptionGroup;
import com.connectly.partnerAdmin.module.product.entity.option.ProductOption;
import com.connectly.partnerAdmin.module.product.entity.stock.Product;
import com.connectly.partnerAdmin.module.product.entity.stock.ProductStock;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderSnapShotProductQueryDto {

    private Product product;
    private ProductStock productStock;
    private Set<ProductOption> productOptions;
    private Set<OptionGroup> optionGroups;
    private Set<OptionDetail> optionDetails;

    @QueryProjection
    public OrderSnapShotProductQueryDto(Product product, ProductStock productStock, Set<ProductOption> productOptions, Set<OptionGroup> optionGroups, Set<OptionDetail> optionDetails) {
        this.product = product;
        this.productStock = productStock;
        this.productOptions = productOptions;
        this.optionGroups = optionGroups;
        this.optionDetails = optionDetails;
    }
}
