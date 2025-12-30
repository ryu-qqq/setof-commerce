package com.connectly.partnerAdmin.module.seller.entity;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;



@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Table(name = "SELLER_SHIPPING_INFO")
@Entity
public class SellerShippingInfo extends BaseEntity {

    @Id
    @Column(name = "SELLER_ID")
    private long id;
    private String returnAddressLine1;
    private String returnAddressLine2;
    private String returnAddressZipCode;


    public void update(SellerShippingInfo sellerShippingInfo){
        returnAddressLine1 = sellerShippingInfo.getReturnAddressLine1();
        returnAddressLine2 = sellerShippingInfo.getReturnAddressLine2();
        returnAddressZipCode = sellerShippingInfo.getReturnAddressZipCode();
    }
}
