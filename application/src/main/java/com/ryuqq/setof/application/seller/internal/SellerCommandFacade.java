package com.ryuqq.setof.application.seller.internal;

import com.ryuqq.setof.application.seller.dto.bundle.SellerRegistrationBundle;
import com.ryuqq.setof.application.seller.dto.bundle.SellerUpdateBundle;
import com.ryuqq.setof.application.seller.manager.SellerAddressCommandManager;
import com.ryuqq.setof.application.seller.manager.SellerBusinessInfoCommandManager;
import com.ryuqq.setof.application.seller.manager.SellerCommandManager;
import com.ryuqq.setof.application.seller.manager.SellerContractCommandManager;
import com.ryuqq.setof.application.seller.manager.SellerCsCommandManager;
import com.ryuqq.setof.application.seller.manager.SellerSettlementCommandManager;
import com.ryuqq.setof.domain.seller.aggregate.Seller;
import com.ryuqq.setof.domain.seller.aggregate.SellerAddress;
import com.ryuqq.setof.domain.seller.aggregate.SellerBusinessInfo;
import com.ryuqq.setof.domain.seller.aggregate.SellerContract;
import com.ryuqq.setof.domain.seller.aggregate.SellerCs;
import com.ryuqq.setof.domain.seller.aggregate.SellerCsUpdateData;
import com.ryuqq.setof.domain.seller.aggregate.SellerSettlement;
import com.ryuqq.setof.domain.seller.id.SellerId;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 셀러 Command Facade.
 *
 * <p>Seller + BusinessInfo + Address + CS + Contract + Settlement를 하나의 트랜잭션으로 처리합니다. (모두 1:1 관계)
 */
@Component
public class SellerCommandFacade {

    private final SellerCommandManager sellerCommandManager;
    private final SellerBusinessInfoCommandManager businessInfoCommandManager;
    private final SellerAddressCommandManager addressCommandManager;
    private final SellerCsCommandManager csCommandManager;
    private final SellerContractCommandManager contractCommandManager;
    private final SellerSettlementCommandManager settlementCommandManager;

    public SellerCommandFacade(
            SellerCommandManager sellerCommandManager,
            SellerBusinessInfoCommandManager businessInfoCommandManager,
            SellerAddressCommandManager addressCommandManager,
            SellerCsCommandManager csCommandManager,
            SellerContractCommandManager contractCommandManager,
            SellerSettlementCommandManager settlementCommandManager) {
        this.sellerCommandManager = sellerCommandManager;
        this.businessInfoCommandManager = businessInfoCommandManager;
        this.addressCommandManager = addressCommandManager;
        this.csCommandManager = csCommandManager;
        this.contractCommandManager = contractCommandManager;
        this.settlementCommandManager = settlementCommandManager;
    }

    /**
     * 셀러 등록 번들을 저장합니다.
     *
     * <p>Seller → BusinessInfo → Address 순서로 저장하며, 모든 저장이 하나의 트랜잭션으로 처리됩니다. (모두 1:1 관계, 필수)
     *
     * @param bundle 셀러 등록 번들
     * @return 생성된 셀러 ID
     */
    @Transactional
    public Long registerSeller(SellerRegistrationBundle bundle) {
        Long sellerId = sellerCommandManager.persist(bundle.seller());
        bundle.withSellerId(SellerId.of(sellerId));

        businessInfoCommandManager.persist(bundle.businessInfo());
        addressCommandManager.persist(bundle.address());

        return sellerId;
    }

    /**
     * 셀러 수정 번들을 저장합니다.
     *
     * <p>Seller → BusinessInfo → Address → CS → Contract → Settlement 순서로 수정하며, 모든 수정이 하나의 트랜잭션으로
     * 처리됩니다. (모두 1:1 관계, 필수)
     *
     * @param bundle 셀러 수정 번들 (검증된 Domain 객체 포함)
     */
    @Transactional
    public void updateSeller(SellerUpdateBundle bundle) {
        Seller seller = bundle.seller();
        SellerBusinessInfo businessInfo = bundle.businessInfo();
        SellerAddress address = bundle.address();
        SellerCs sellerCs = bundle.sellerCs();
        SellerContract sellerContract = bundle.sellerContract();
        SellerSettlement sellerSettlement = bundle.sellerSettlement();
        SellerCsUpdateData csUpdateData = bundle.csUpdateData();

        // 도메인 업데이트
        seller.update(bundle.sellerUpdateData(), bundle.changedAt());
        businessInfo.update(bundle.businessInfoUpdateData(), bundle.changedAt());
        address.update(bundle.addressUpdateData(), bundle.changedAt());
        sellerCs.update(
                csUpdateData.csContact(),
                csUpdateData.operatingHours(),
                csUpdateData.operatingDays(),
                csUpdateData.kakaoChannelUrl(),
                bundle.changedAt());
        sellerContract.update(bundle.contractUpdateData(), bundle.changedAt());
        sellerSettlement.update(bundle.settlementUpdateData(), bundle.changedAt());

        // 영속화
        sellerCommandManager.persist(seller);
        businessInfoCommandManager.persist(businessInfo);
        addressCommandManager.persist(address);
        csCommandManager.persist(sellerCs);
        contractCommandManager.persist(sellerContract);
        settlementCommandManager.persist(sellerSettlement);
    }
}
