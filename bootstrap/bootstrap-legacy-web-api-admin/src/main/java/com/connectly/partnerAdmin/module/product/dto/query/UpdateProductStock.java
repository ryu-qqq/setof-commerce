package com.connectly.partnerAdmin.module.product.dto.query;

import com.connectly.partnerAdmin.module.product.core.Sku;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;



@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class UpdateProductStock  implements Sku {

    @NotNull(message = "Product ID is required.")
    protected long productId;

    @Max(value = 9999, message = "Stock quantity cannot exceed 9999.")
    @Min(value = 0, message = "Stock quantity must be at least 0.")
    protected int productStockQuantity;

    @JsonIgnore
    @Override
    public int getQuantity() {
        return productStockQuantity;
    }

}
