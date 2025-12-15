package com.setof.connectly.module.product.dto.stock;

import com.setof.connectly.module.common.enums.Yn;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductStatusUpdateEvent {
    private List<Long> productIds;
    private Yn yn;
}
