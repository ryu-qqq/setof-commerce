package com.setof.connectly.module.product.dto.group;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.setof.connectly.module.product.dto.option.OptionDto;
import com.setof.connectly.module.product.entity.group.embedded.ProductStatus;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductDto {

    public long productId;
    public int stockQuantity;
    private ProductStatus productStatus;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public String option = "";

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public Set<OptionDto> options = new HashSet<>();

    public ProductDto(
            long productId,
            int stockQuantity,
            ProductStatus productStatus,
            String option,
            Set<OptionDto> options) {
        this.productId = productId;
        this.stockQuantity = stockQuantity;
        this.productStatus = productStatus;
        this.option = option;
        this.options = options;
    }
}
