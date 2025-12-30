package com.connectly.partnerAdmin.module.product.dto.query;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.connectly.partnerAdmin.module.product.enums.option.OptionName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateOption {

    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long productId;

    @Max(value = 9999, message = "Stock quantity cannot exceed 9999.")
    @Min(value = 0, message = "Stock quantity must be at least 0.")
    @JsonProperty("stockQuantity")
    private Integer quantity;

    private BigDecimal additionalPrice;

    @Valid
    @Size(max = 2, message = "Product options can have a maximum of 2 items.")
    private List<CreateOptionDetail> options = new LinkedList<>();

    @JsonIgnore
    public String getOption(){
        return this.options.stream()
                .map(CreateOptionDetail::getFullOptionName)
                .collect(Collectors.joining(" "));
    }

    @JsonIgnore
    public Map<OptionName, CreateOptionDetail> getOptionNameMap(){
        return this.options.stream()
                .collect(Collectors.toMap(CreateOptionDetail::getOptionName, Function.identity(),
                        (existingValue, newValue)
                                -> existingValue));
    }

    public BigDecimal getAdditionalPrice() {
        if(additionalPrice == null) return BigDecimal.ZERO;
        return additionalPrice;
    }

}





