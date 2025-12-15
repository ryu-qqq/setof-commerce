package com.setof.connectly.module.review.filter;

import com.setof.connectly.module.display.enums.component.OrderType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ReviewFilter {
    private OrderType orderType;
    private Long productGroupId;
    private Long lastDomainId;
}
