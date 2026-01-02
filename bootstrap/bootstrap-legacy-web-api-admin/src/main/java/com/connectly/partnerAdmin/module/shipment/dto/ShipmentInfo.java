package com.connectly.partnerAdmin.module.shipment.dto;

import org.hibernate.validator.constraints.Length;

import com.connectly.partnerAdmin.module.shipment.enums.ShipmentCompanyCode;
import com.connectly.partnerAdmin.module.shipment.enums.ShipmentType;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ShipmentInfo {

    @Length(max = 80, message = "본문의 내용은 100자를 넘어갈 수 없습니다.")
    private String invoiceNo;

    @NotNull(message = "shipmentType은 필수입니다.")
    private ShipmentType shipmentType;

    @NotNull(message = "companyCode는 필수입니다.")
    private ShipmentCompanyCode companyCode;

}
