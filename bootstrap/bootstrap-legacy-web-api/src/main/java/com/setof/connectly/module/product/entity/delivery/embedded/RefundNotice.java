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

    @Column(name = "return_method_domestic")
    @Enumerated(EnumType.STRING)
    private ReturnMethod returnMethodDomestic;

    @Column(name = "return_courier_domestic")
    @Enumerated(EnumType.STRING)
    private ShipmentCompanyCode returnCourierDomestic;

    @Column(name = "return_charge_domestic")
    private int returnChargeDomestic;

    @Column(name = "return_exchange_area_domestic")
    private String returnExchangeAreaDomestic;
}
