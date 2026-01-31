package com.ryuqq.setof.application.seller.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;

import com.ryuqq.setof.application.seller.dto.bundle.SellerRegistrationBundle;
import com.ryuqq.setof.application.seller.dto.bundle.SellerUpdateBundle;
import com.ryuqq.setof.application.seller.manager.SellerAddressCommandManager;
import com.ryuqq.setof.application.seller.manager.SellerBusinessInfoCommandManager;
import com.ryuqq.setof.application.seller.manager.SellerCommandManager;
import com.ryuqq.setof.application.seller.manager.SellerContractCommandManager;
import com.ryuqq.setof.application.seller.manager.SellerCsCommandManager;
import com.ryuqq.setof.application.seller.manager.SellerSettlementCommandManager;
import com.ryuqq.setof.domain.seller.SellerFixtures;
import com.ryuqq.setof.domain.seller.aggregate.Seller;
import com.ryuqq.setof.domain.seller.aggregate.SellerAddress;
import com.ryuqq.setof.domain.seller.aggregate.SellerBusinessInfo;
import com.ryuqq.setof.domain.seller.aggregate.SellerContract;
import com.ryuqq.setof.domain.seller.aggregate.SellerCs;
import com.ryuqq.setof.domain.seller.aggregate.SellerSettlement;
import com.ryuqq.setof.domain.seller.id.SellerId;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("SellerCommandFacade 단위 테스트")
class SellerCommandFacadeTest {

    @InjectMocks private SellerCommandFacade sut;

    @Mock private SellerCommandManager sellerCommandManager;
    @Mock private SellerBusinessInfoCommandManager businessInfoCommandManager;
    @Mock private SellerAddressCommandManager addressCommandManager;
    @Mock private SellerCsCommandManager csCommandManager;
    @Mock private SellerContractCommandManager contractCommandManager;
    @Mock private SellerSettlementCommandManager settlementCommandManager;

    @Nested
    @DisplayName("registerSeller() - 셀러 등록")
    class RegisterSellerTest {

        @Test
        @DisplayName("Seller → BusinessInfo → Address 순서로 저장한다")
        void registerSeller_SavesInCorrectOrder() {
            // given
            Seller seller = SellerFixtures.newSeller();
            SellerBusinessInfo businessInfo = SellerFixtures.newSellerBusinessInfo();
            SellerAddress address = SellerFixtures.newShippingAddress();
            SellerRegistrationBundle bundle =
                    new SellerRegistrationBundle(seller, businessInfo, address);

            Long expectedSellerId = 1L;
            given(sellerCommandManager.persist(seller)).willReturn(expectedSellerId);

            // when
            Long result = sut.registerSeller(bundle);

            // then
            assertThat(result).isEqualTo(expectedSellerId);

            InOrder inOrder =
                    inOrder(
                            sellerCommandManager,
                            businessInfoCommandManager,
                            addressCommandManager);
            inOrder.verify(sellerCommandManager).persist(seller);
            inOrder.verify(businessInfoCommandManager).persist(businessInfo);
            inOrder.verify(addressCommandManager).persist(address);
        }

        @Test
        @DisplayName("등록 후 생성된 ID를 반환한다")
        void registerSeller_ReturnsSellerId() {
            // given
            SellerRegistrationBundle bundle =
                    new SellerRegistrationBundle(
                            SellerFixtures.newSeller(),
                            SellerFixtures.newSellerBusinessInfo(),
                            SellerFixtures.newShippingAddress());

            Long expectedSellerId = 100L;
            given(sellerCommandManager.persist(bundle.seller())).willReturn(expectedSellerId);

            // when
            Long result = sut.registerSeller(bundle);

            // then
            assertThat(result).isEqualTo(expectedSellerId);
        }
    }

    @Nested
    @DisplayName("updateSeller() - 셀러 수정")
    class UpdateSellerTest {

        @Test
        @DisplayName("Seller → BusinessInfo → Address → CS → Contract → Settlement 순서로 수정 및 저장한다")
        void updateSeller_UpdatesAndSavesInCorrectOrder() {
            // given
            Long sellerId = 1L;
            Instant changedAt = Instant.now();

            Seller seller = SellerFixtures.activeSeller(sellerId);
            SellerBusinessInfo businessInfo = SellerFixtures.activeSellerBusinessInfo();
            SellerAddress address = SellerFixtures.activeShippingAddress();
            SellerCs sellerCs = SellerFixtures.activeSellerCs();
            SellerContract sellerContract = SellerFixtures.activeSellerContract();
            SellerSettlement sellerSettlement = SellerFixtures.activeSellerSettlement();

            SellerUpdateBundle bundle =
                    new SellerUpdateBundle(
                            SellerId.of(sellerId),
                            SellerFixtures.sellerUpdateData(),
                            SellerFixtures.sellerBusinessInfoUpdateData(),
                            SellerFixtures.sellerAddressUpdateData(),
                            SellerFixtures.sellerCsUpdateData(),
                            SellerFixtures.sellerContractUpdateData(),
                            SellerFixtures.sellerSettlementUpdateData(),
                            changedAt);

            bundle.withValidatedEntities(
                    seller, businessInfo, address, sellerCs, sellerContract, sellerSettlement);

            // when
            sut.updateSeller(bundle);

            // then
            InOrder inOrder =
                    inOrder(
                            sellerCommandManager,
                            businessInfoCommandManager,
                            addressCommandManager,
                            csCommandManager,
                            contractCommandManager,
                            settlementCommandManager);
            inOrder.verify(sellerCommandManager).persist(seller);
            inOrder.verify(businessInfoCommandManager).persist(businessInfo);
            inOrder.verify(addressCommandManager).persist(address);
            inOrder.verify(csCommandManager).persist(sellerCs);
            inOrder.verify(contractCommandManager).persist(sellerContract);
            inOrder.verify(settlementCommandManager).persist(sellerSettlement);
        }
    }
}
