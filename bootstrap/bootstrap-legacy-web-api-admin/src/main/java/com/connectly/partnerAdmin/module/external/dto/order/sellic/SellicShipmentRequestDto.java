package com.connectly.partnerAdmin.module.external.dto.order.sellic;

import com.connectly.partnerAdmin.module.external.dto.SeWonRequestDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SellicShipmentRequestDto extends SeWonRequestDto{

    private List<SellicShipmentInfo> ships;

}
