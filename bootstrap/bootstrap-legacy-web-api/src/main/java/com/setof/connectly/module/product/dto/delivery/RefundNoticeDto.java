package com.setof.connectly.module.product.dto.delivery;

import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.product.enums.delivery.ReturnMethod;
import com.setof.connectly.module.shipment.enums.ShipmentCompanyCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefundNoticeDto {
    private String returnMethodDomestic;
    private String returnCourierDomestic;
    private int returnChargeDomestic;
    private String returnExchangeAreaDomestic;

    @QueryProjection
    public RefundNoticeDto(
            ReturnMethod returnMethodDomestic,
            ShipmentCompanyCode returnCourierDomestic,
            int returnChargeDomestic,
            String returnExchangeAreaDomestic) {
        this.returnMethodDomestic = returnMethodDomestic.getDisplayName();
        this.returnCourierDomestic = returnCourierDomestic.getDisplayName();
        this.returnChargeDomestic = returnChargeDomestic;
        this.returnExchangeAreaDomestic = returnExchangeAreaDomestic;
    }
}
