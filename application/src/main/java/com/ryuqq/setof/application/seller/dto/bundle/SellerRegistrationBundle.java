package com.ryuqq.setof.application.seller.dto.bundle;

import com.ryuqq.setof.domain.seller.aggregate.Seller;
import com.ryuqq.setof.domain.seller.aggregate.SellerBusinessInfo;
import com.ryuqq.setof.domain.seller.id.SellerId;

public class SellerRegistrationBundle {

    private final Seller seller;
    private SellerBusinessInfo businessInfo;

    public SellerRegistrationBundle(Seller seller, SellerBusinessInfo businessInfo) {
        this.seller = seller;
        this.businessInfo = businessInfo;
    }

    public void assignSellerId(SellerId sellerId) {
        this.businessInfo.assignSellerId(sellerId);
    }

    public Seller seller() {
        return seller;
    }

    public SellerBusinessInfo businessInfo() {
        return businessInfo;
    }

    public String sellerNameValue() {
        return seller.sellerNameValue();
    }

    public String registrationNumberValue() {
        return businessInfo.registrationNumberValue();
    }
}
