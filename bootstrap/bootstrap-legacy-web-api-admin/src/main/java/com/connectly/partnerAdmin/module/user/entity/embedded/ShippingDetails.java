package com.connectly.partnerAdmin.module.user.entity.embedded;

import com.connectly.partnerAdmin.module.product.enums.group.Origin;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class ShippingDetails {

    @Column(name = "RECEIVER_NAME", length = 100, nullable = false)
    private String receiverName;

    @Column(name = "SHIPPING_ADDRESS_NAME", length = 100, nullable = false)
    private String shippingAddressName;

    @Column(name = "ADDRESS_LINE1", length = 100, nullable = false)
    private String addressLine1;

    @Column(name = "ADDRESS_LINE2", length = 100, nullable = true)
    private String addressLine2;

    @Column(name = "ZIP_CODE", length = 10, nullable = false)
    private String zipCode;

    @Column(name = "COUNTRY", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private Origin country;

    @Column(name = "DELIVERY_REQUEST", length = 100, nullable = true)
    private String deliveryRequest;

    @Column(name = "PHONE_NUMBER", length = 15, nullable = false)
    private String phoneNumber;

}
