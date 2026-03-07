package com.ryuqq.setof.application.seller.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.seller.SellerDomainFixtures;
import com.ryuqq.setof.application.seller.dto.bundle.SellerRegistrationBundle;
import com.ryuqq.setof.application.seller.manager.SellerBusinessInfoCommandManager;
import com.ryuqq.setof.application.seller.manager.SellerCommandManager;
import com.ryuqq.setof.application.seller.manager.SellerCsCommandManager;
import com.ryuqq.setof.domain.seller.aggregate.Seller;
import com.ryuqq.setof.domain.seller.aggregate.SellerBusinessInfo;
import com.ryuqq.setof.domain.seller.aggregate.SellerCs;
import com.ryuqq.setof.domain.seller.id.SellerId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("SellerCommandFacade 단위 테스트")
class SellerCommandFacadeTest {

    @InjectMocks private SellerCommandFacade sut;

    @Mock private SellerCommandManager sellerCommandManager;
    @Mock private SellerBusinessInfoCommandManager sellerBusinessInfoCommandManager;
    @Mock private SellerCsCommandManager sellerCsCommandManager;

    @Nested
    @DisplayName("registerSeller() - 셀러 등록")
    class RegisterSellerTest {

        @Test
        @DisplayName("셀러 번들로 셀러를 등록하고 셀러 ID를 반환한다")
        void registerSeller_ValidBundle_ReturnsSellerId() {
            // given
            Seller seller = SellerDomainFixtures.activeSeller();
            SellerBusinessInfo businessInfo = SellerDomainFixtures.sellerBusinessInfo(1L);
            SellerRegistrationBundle bundle = new SellerRegistrationBundle(seller, businessInfo);

            SellerId persistedSellerId = SellerId.of(1L);
            Seller persistedSeller = SellerDomainFixtures.activeSeller(1L);

            given(sellerCommandManager.persist(seller)).willReturn(persistedSeller);
            given(sellerBusinessInfoCommandManager.persist(businessInfo)).willReturn(businessInfo);

            // when
            Long result = sut.registerSeller(bundle);

            // then
            assertThat(result).isEqualTo(persistedSellerId.value());
            then(sellerCommandManager).should().persist(seller);
            then(sellerBusinessInfoCommandManager).should().persist(businessInfo);
            then(sellerCsCommandManager).shouldHaveNoInteractions();
        }
    }

    @Nested
    @DisplayName("updateSeller() - 셀러 수정")
    class UpdateSellerTest {

        @Test
        @DisplayName("셀러, 사업자 정보, CS 정보를 모두 수정한다")
        void updateSeller_AllDataPresent_UpdatesAll() {
            // given
            Seller seller = SellerDomainFixtures.activeSeller();
            SellerBusinessInfo businessInfo = SellerDomainFixtures.sellerBusinessInfo(1L);
            SellerCs sellerCs = SellerDomainFixtures.sellerCs(1L);

            given(sellerCommandManager.persist(seller)).willReturn(seller);
            given(sellerBusinessInfoCommandManager.persist(businessInfo)).willReturn(businessInfo);
            given(sellerCsCommandManager.persist(sellerCs)).willReturn(sellerCs);

            // when
            sut.updateSeller(seller, businessInfo, sellerCs);

            // then
            then(sellerCommandManager).should().persist(seller);
            then(sellerBusinessInfoCommandManager).should().persist(businessInfo);
            then(sellerCsCommandManager).should().persist(sellerCs);
        }

        @Test
        @DisplayName("선택적 데이터가 null이면 해당 항목은 수정하지 않는다")
        void updateSeller_NullOptionalData_SkipsNullItems() {
            // given
            Seller seller = SellerDomainFixtures.activeSeller();

            given(sellerCommandManager.persist(seller)).willReturn(seller);

            // when
            sut.updateSeller(seller, null, null);

            // then
            then(sellerCommandManager).should().persist(seller);
            then(sellerBusinessInfoCommandManager).shouldHaveNoInteractions();
            then(sellerCsCommandManager).shouldHaveNoInteractions();
        }
    }
}
