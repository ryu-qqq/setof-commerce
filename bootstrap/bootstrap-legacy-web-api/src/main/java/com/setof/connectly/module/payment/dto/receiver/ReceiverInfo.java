package com.setof.connectly.module.payment.dto.receiver;

import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.product.enums.group.Origin;
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
    private String phoneNumber;

    @QueryProjection
    public ReceiverInfo(
            String receiverName,
            String receiverPhoneNumber,
            String addressLine1,
            String addressLine2,
            String zipCode,
            Origin country,
            String deliveryRequest,
            String phoneNumber) {
        this.receiverName = receiverName;
        this.receiverPhoneNumber = receiverPhoneNumber;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.zipCode = zipCode;
        this.country = country;
        this.deliveryRequest = deliveryRequest;
        this.phoneNumber = phoneNumber;
    }
}
