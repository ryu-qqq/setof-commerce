package com.ryuqq.setof.application.discount.manager;

import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.discount.DiscountDomainFixtures;
import com.ryuqq.setof.application.discount.port.out.command.LegacyProductGroupPriceCommandPort;
import com.ryuqq.setof.domain.discount.dto.ProductGroupPriceUpdateData;
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
@DisplayName("LegacyProductGroupPriceCommandManager 단위 테스트")
class LegacyProductGroupPriceCommandManagerTest {

    @InjectMocks private LegacyProductGroupPriceCommandManager sut;

    @Mock private LegacyProductGroupPriceCommandPort priceCommandPort;

    @Nested
    @DisplayName("persistAll() - 가격 일괄 갱신")
    class PersistAllTest {

        @Test
        @DisplayName("갱신 데이터 목록을 저장 포트에 위임한다")
        void persistAll_ValidUpdates_DelegatesToCommandPort() {
            // given
            List<ProductGroupPriceUpdateData> updates =
                    DiscountDomainFixtures.priceUpdateDataList();

            // when
            sut.persistAll(updates);

            // then
            then(priceCommandPort).should().persistAll(updates);
        }

        @Test
        @DisplayName("빈 목록이면 저장 포트를 호출하지 않는다")
        void persistAll_EmptyList_DoesNotCallCommandPort() {
            // given
            List<ProductGroupPriceUpdateData> emptyUpdates = List.of();

            // when
            sut.persistAll(emptyUpdates);

            // then
            then(priceCommandPort).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("단건 갱신 데이터도 저장 포트에 위임한다")
        void persistAll_SingleUpdate_DelegatesToCommandPort() {
            // given
            List<ProductGroupPriceUpdateData> singleUpdate =
                    List.of(DiscountDomainFixtures.priceUpdateData());

            // when
            sut.persistAll(singleUpdate);

            // then
            then(priceCommandPort).should().persistAll(singleUpdate);
        }
    }
}
