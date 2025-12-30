package com.connectly.partnerAdmin.module.product.dto.query;

import com.connectly.partnerAdmin.module.product.enums.delivery.ReturnMethod;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateRefundNotice {

    @NotNull(message = "Return method for domestic orders is required.")
    private ReturnMethod returnMethodDomestic;

    @NotBlank(message = "Return courier for domestic is required.")
    @Length(max = 30, message = "Return courier  cannot exceed 30 characters.")
    private String returnCourierDomestic;

    @Min(value = 0, message = "Return charge for domestic orders must be at least 0.")
    @Max(value = 100000, message = "Return charge for domestic orders must be at most 100,000.")
    private int returnChargeDomestic;

    @NotBlank(message = "Return exchange area for domestic orders is required.")
    @Length(max = 200, message = "Return exchange area for domestic orders cannot exceed 200 characters.")
    private String returnExchangeAreaDomestic;

}
