package com.ryuqq.setof.application.seller.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.seller.SellerDomainFixtures;
import com.ryuqq.setof.application.seller.port.out.command.SellerCsCommandPort;
import com.ryuqq.setof.domain.seller.aggregate.SellerCs;
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
@DisplayName("SellerCsCommandManager 단위 테스트")
class SellerCsCommandManagerTest {

    @InjectMocks private SellerCsCommandManager sut;

    @Mock private SellerCsCommandPort sellerCsCommandPort;

    @Nested
    @DisplayName("persist() - CS 정보 저장")
    class PersistTest {

        @Test
        @DisplayName("CS 정보를 저장하고 저장된 정보를 반환한다")
        void persist_SavesCs_ReturnsSavedCs() {
            // given
            SellerCs sellerCs = SellerDomainFixtures.sellerCs(1L);

            given(sellerCsCommandPort.persist(sellerCs)).willReturn(sellerCs);

            // when
            SellerCs result = sut.persist(sellerCs);

            // then
            assertThat(result).isEqualTo(sellerCs);
            then(sellerCsCommandPort).should().persist(sellerCs);
        }
    }

    @Nested
    @DisplayName("persistAll() - CS 정보 목록 저장")
    class PersistAllTest {

        @Test
        @DisplayName("CS 정보 목록을 저장하고 저장된 목록을 반환한다")
        void persistAll_SavesAllCsList_ReturnsSavedCsList() {
            // given
            List<SellerCs> csList =
                    List.of(SellerDomainFixtures.sellerCs(1L), SellerDomainFixtures.sellerCs(2L));

            given(sellerCsCommandPort.persistAll(csList)).willReturn(csList);

            // when
            List<SellerCs> result = sut.persistAll(csList);

            // then
            assertThat(result).hasSize(2);
            then(sellerCsCommandPort).should().persistAll(csList);
        }
    }
}
