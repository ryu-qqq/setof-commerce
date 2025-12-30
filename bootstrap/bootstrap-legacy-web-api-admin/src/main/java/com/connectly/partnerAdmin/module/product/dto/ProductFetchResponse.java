package com.connectly.partnerAdmin.module.product.dto;

import java.math.BigDecimal;
import java.util.Set;

import com.connectly.partnerAdmin.module.product.dto.option.OptionDto;
import com.connectly.partnerAdmin.module.product.entity.group.embedded.ProductStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class ProductFetchResponse {

    @JsonIgnore
    private long productGroupId;
    private long productId;
    private int stockQuantity;
    private ProductStatus productStatus;
    private String option;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Set<OptionDto> options;
    private BigDecimal additionalPrice;

}
