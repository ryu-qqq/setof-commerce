package com.ryuqq.setof.application.seller.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.seller.SellerDomainFixtures;
import com.ryuqq.setof.application.seller.port.out.command.SellerCommandPort;
import com.ryuqq.setof.domain.seller.aggregate.Seller;
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
@DisplayName("SellerCommandManager 단위 테스트")
class SellerCommandManagerTest {

    @InjectMocks private SellerCommandManager sut;

    @Mock private SellerCommandPort sellerCommandPort;

    @Nested
    @DisplayName("persist() - 셀러 저장")
    class PersistTest {

        @Test
        @DisplayName("셀러를 저장하고 저장된 셀러를 반환한다")
        void persist_SavesSeller_ReturnsSavedSeller() {
            // given
            Seller seller = SellerDomainFixtures.activeSeller();

            given(sellerCommandPort.persist(seller)).willReturn(seller);

            // when
            Seller result = sut.persist(seller);

            // then
            assertThat(result).isEqualTo(seller);
            then(sellerCommandPort).should().persist(seller);
        }
    }

    @Nested
    @DisplayName("persistAll() - 셀러 목록 저장")
    class PersistAllTest {

        @Test
        @DisplayName("셀러 목록을 저장하고 저장된 셀러 목록을 반환한다")
        void persistAll_SavesAllSellers_ReturnsSavedSellers() {
            // given
            List<Seller> sellers = SellerDomainFixtures.activeSellers();

            given(sellerCommandPort.persistAll(sellers)).willReturn(sellers);

            // when
            List<Seller> result = sut.persistAll(sellers);

            // then
            assertThat(result).hasSize(2);
            then(sellerCommandPort).should().persistAll(sellers);
        }
    }
}
