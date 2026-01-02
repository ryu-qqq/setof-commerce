package com.connectly.partnerAdmin.module.product.dto.query;

import com.connectly.partnerAdmin.module.product.enums.group.Origin;
import com.connectly.partnerAdmin.module.product.enums.group.ProductCondition;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateClothesDetail {

    @NotNull(message = "productCondition is required")
    private ProductCondition productCondition;

    private Origin origin;

    @Length(max = 50, message = "Product Style Code name cannot exceed 50 characters.")
    private String styleCode;


    public Origin getOrigin() {
        if(origin == null) return Origin.OTHER;
        return origin;
    }

    public @Length(max = 50, message = "Product Style Code name cannot exceed 50 characters.") String getStyleCode() {
        if (styleCode == null) return "";
        return styleCode;
    }
}
