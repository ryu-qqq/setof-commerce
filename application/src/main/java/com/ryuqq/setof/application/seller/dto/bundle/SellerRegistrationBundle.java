package com.ryuqq.setof.application.seller.dto.bundle;

import com.ryuqq.setof.domain.seller.aggregate.Seller;
import com.ryuqq.setof.domain.seller.aggregate.SellerAddress;
import com.ryuqq.setof.domain.seller.aggregate.SellerBusinessInfo;
import com.ryuqq.setof.domain.seller.id.SellerId;

/**
 * 셀러 등록 번들.
 *
 * <p>Seller + BusinessInfo + Address를 한번에 묶어서 관리합니다. (모두 1:1 관계)
 */
public class SellerRegistrationBundle {

    private final Seller seller;
    private final SellerBusinessInfo businessInfo;
    private final SellerAddress address;

    public SellerRegistrationBundle(
            Seller seller, SellerBusinessInfo businessInfo, SellerAddress address) {
        this.seller = seller;
        this.businessInfo = businessInfo;
        this.address = address;
    }

    /**
     * SellerId를 BusinessInfo와 Address에 설정합니다.
     *
     * @param sellerId persist 후 확정된 Seller ID
     */
    public void withSellerId(SellerId sellerId) {
        businessInfo.assignSellerId(sellerId);
        address.assignSellerId(sellerId);
    }

    // === 편의 메서드 ===

    public String sellerNameValue() {
        return seller.sellerNameValue();
    }

    public String registrationNumberValue() {
        return businessInfo.registrationNumberValue();
    }

    // === Getter ===

    public Seller seller() {
        return seller;
    }

    public SellerBusinessInfo businessInfo() {
        return businessInfo;
    }

    public SellerAddress address() {
        return address;
    }
}
