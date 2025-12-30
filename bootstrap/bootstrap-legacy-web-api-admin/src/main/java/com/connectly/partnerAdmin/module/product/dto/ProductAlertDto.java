package com.connectly.partnerAdmin.module.product.dto;


import com.connectly.partnerAdmin.module.product.dto.option.OptionDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductAlertDto {

    private String productGroupName;
    private int quantity;
    private String option= "";
    @JsonIgnore
    private Set<OptionDto> options = new HashSet<>();

    @QueryProjection
    public ProductAlertDto(String productGroupName, int quantity, Set<OptionDto> options) {
        this.productGroupName = productGroupName;
        this.quantity = quantity;
        this.options = options;
        this.option = setOption();
    }


    private String setOption() {
        return options.stream()
                .map(OptionDto::getOptionValue)
                .collect(Collectors.joining(" "));
    }

}
