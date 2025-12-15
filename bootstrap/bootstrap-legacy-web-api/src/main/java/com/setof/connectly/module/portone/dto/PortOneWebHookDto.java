package com.setof.connectly.module.portone.dto;

import com.setof.connectly.module.portone.enums.PortOnePaymentStatus;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PortOneWebHookDto extends PortOneDto {
    private PortOnePaymentStatus status;
}
