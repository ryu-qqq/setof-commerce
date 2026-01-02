package com.connectly.partnerAdmin.module.discount.dto.query;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateDiscount {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long discountPolicyId;

    private CreateDiscountDetails discountDetails;

    public CreateDiscount(CreateDiscountDetails createDiscountDetails) {
        this.discountDetails = createDiscountDetails;
    }


    public boolean isCopyCreate() {
        return discountPolicyId != null && discountPolicyId > 0;
    }
}
