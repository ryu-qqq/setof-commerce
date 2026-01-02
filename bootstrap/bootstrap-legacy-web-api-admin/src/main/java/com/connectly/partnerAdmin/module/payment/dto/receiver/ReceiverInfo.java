package com.connectly.partnerAdmin.module.payment.dto.receiver;

import com.connectly.partnerAdmin.module.product.enums.group.Origin;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReceiverInfo {

    private String receiverName;
    private String receiverPhoneNumber;
    private String addressLine1;
    private String addressLine2;
    private String zipCode;
    private Origin country;
    private String deliveryRequest;

    @QueryProjection
    public ReceiverInfo(String receiverName, String receiverPhoneNumber, String addressLine1, String addressLine2, String zipCode, Origin country, String deliveryRequest) {
        this.receiverName = receiverName;
        this.receiverPhoneNumber = receiverPhoneNumber;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.zipCode = zipCode;
        this.country = country;
        this.deliveryRequest = deliveryRequest;
    }
}
