package com.setof.connectly.module.product.entity.delivery.embedded;

import com.setof.connectly.module.product.enums.delivery.ReturnMethod;
import com.setof.connectly.module.shipment.enums.ShipmentCompanyCode;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Embeddable
public class RefundNotice {

    @Column(name = "RETURN_METHOD_DOMESTIC")
    @Enumerated(EnumType.STRING)
    private ReturnMethod returnMethodDomestic;

    @Column(name = "RETURN_COURIER_DOMESTIC")
    @Enumerated(EnumType.STRING)
    private ShipmentCompanyCode returnCourierDomestic;

    @Column(name = "RETURN_CHARGE_DOMESTIC")
    private int returnChargeDomestic;

    @Column(name = "RETURN_EXCHANGE_AREA_DOMESTIC")
    private String returnExchangeAreaDomestic;
}
