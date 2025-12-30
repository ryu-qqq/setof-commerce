package com.connectly.partnerAdmin.module.discount.dto.query;

import com.connectly.partnerAdmin.module.discount.entity.embedded.DiscountDetails;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UpdateDiscount {
    private DiscountDetails discountDetails;
    private List<Long> targetIds;

}
