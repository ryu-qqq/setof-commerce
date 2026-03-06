package com.ryuqq.setof.application.seller.internal;

import com.ryuqq.setof.application.seller.dto.bundle.SellerRegistrationBundle;
import com.ryuqq.setof.application.seller.manager.SellerBusinessInfoCommandManager;
import com.ryuqq.setof.application.seller.manager.SellerCommandManager;
import com.ryuqq.setof.application.seller.manager.SellerCsCommandManager;
import com.ryuqq.setof.domain.seller.aggregate.Seller;
import com.ryuqq.setof.domain.seller.aggregate.SellerBusinessInfo;
import com.ryuqq.setof.domain.seller.aggregate.SellerCs;
import com.ryuqq.setof.domain.seller.id.SellerId;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SellerCommandFacade {

    private final SellerCommandManager sellerCommandManager;
    private final SellerBusinessInfoCommandManager sellerBusinessInfoCommandManager;
    private final SellerCsCommandManager sellerCsCommandManager;

    public SellerCommandFacade(
            SellerCommandManager sellerCommandManager,
            SellerBusinessInfoCommandManager sellerBusinessInfoCommandManager,
            SellerCsCommandManager sellerCsCommandManager) {
        this.sellerCommandManager = sellerCommandManager;
        this.sellerBusinessInfoCommandManager = sellerBusinessInfoCommandManager;
        this.sellerCsCommandManager = sellerCsCommandManager;
    }

    @Transactional
    public Long registerSeller(SellerRegistrationBundle bundle) {
        Seller persistedSeller = sellerCommandManager.persist(bundle.seller());
        SellerId sellerId = persistedSeller.id();
        bundle.assignSellerId(sellerId);
        sellerBusinessInfoCommandManager.persist(bundle.businessInfo());
        return sellerId.value();
    }

    @Transactional
    public void updateSeller(Seller seller, SellerBusinessInfo businessInfo, SellerCs sellerCs) {
        sellerCommandManager.persist(seller);
        if (businessInfo != null) {
            sellerBusinessInfoCommandManager.persist(businessInfo);
        }
        if (sellerCs != null) {
            sellerCsCommandManager.persist(sellerCs);
        }
    }
}
