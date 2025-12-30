package com.connectly.partnerAdmin.module.order.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderSnapShotQueryDto {

    private OrderSnapShotProductGroupQueryDto orderSnapShotProductGroup;
    private OrderSnapShotProductQueryDto orderSnapShotProduct;

}
