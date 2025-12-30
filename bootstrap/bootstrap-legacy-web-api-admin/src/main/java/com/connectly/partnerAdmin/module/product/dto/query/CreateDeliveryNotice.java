package com.connectly.partnerAdmin.module.product.dto.query;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;



@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateDeliveryNotice {

    @NotBlank(message = "Delivery area is required.")
    @Length(max = 200, message = "Delivery area cannot exceed 200 characters.")
    private String deliveryArea;

    @Min(value = 0, message = "Delivery fee must be at least 0.")
    @Max(value = 100000, message = "Delivery fee must be at most 100,000.")
    private long deliveryFee;

    @Min(value = 0, message = "Average delivery period must be at least 0 days.")
    @Max(value = 30, message = "Average delivery period must be at most 30 days.")
    private int deliveryPeriodAverage;

}
