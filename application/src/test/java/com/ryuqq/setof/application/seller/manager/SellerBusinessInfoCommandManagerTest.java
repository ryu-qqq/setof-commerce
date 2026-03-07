package com.ryuqq.setof.application.seller.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.seller.SellerDomainFixtures;
import com.ryuqq.setof.application.seller.port.out.command.SellerBusinessInfoCommandPort;
import com.ryuqq.setof.domain.seller.aggregate.SellerBusinessInfo;
import java.util.List;
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
@DisplayName("SellerBusinessInfoCommandManager 단위 테스트")
class SellerBusinessInfoCommandManagerTest {

    @InjectMocks private SellerBusinessInfoCommandManager sut;

    @Mock private SellerBusinessInfoCommandPort sellerBusinessInfoCommandPort;

    @Nested
    @DisplayName("persist() - 사업자 정보 저장")
    class PersistTest {

        @Test
        @DisplayName("사업자 정보를 저장하고 저장된 정보를 반환한다")
        void persist_SavesBusinessInfo_ReturnsSavedBusinessInfo() {
            // given
            SellerBusinessInfo businessInfo = SellerDomainFixtures.sellerBusinessInfo(1L);

            given(sellerBusinessInfoCommandPort.persist(businessInfo)).willReturn(businessInfo);

            // when
            SellerBusinessInfo result = sut.persist(businessInfo);

            // then
            assertThat(result).isEqualTo(businessInfo);
            then(sellerBusinessInfoCommandPort).should().persist(businessInfo);
        }
    }

    @Nested
    @DisplayName("persistAll() - 사업자 정보 목록 저장")
    class PersistAllTest {

        @Test
        @DisplayName("사업자 정보 목록을 저장하고 저장된 목록을 반환한다")
        void persistAll_SavesAllBusinessInfos_ReturnsSavedBusinessInfos() {
            // given
            List<SellerBusinessInfo> businessInfos =
                    List.of(
                            SellerDomainFixtures.sellerBusinessInfo(1L),
                            SellerDomainFixtures.sellerBusinessInfo(2L));

            given(sellerBusinessInfoCommandPort.persistAll(businessInfos))
                    .willReturn(businessInfos);

            // when
            List<SellerBusinessInfo> result = sut.persistAll(businessInfos);

            // then
            assertThat(result).hasSize(2);
            then(sellerBusinessInfoCommandPort).should().persistAll(businessInfos);
        }
    }
}
