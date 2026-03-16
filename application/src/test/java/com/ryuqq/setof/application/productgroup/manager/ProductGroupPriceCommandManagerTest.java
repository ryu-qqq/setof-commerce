package com.ryuqq.setof.application.productgroup.manager;

import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.productgroup.port.out.command.ProductGroupPriceCommandPort;
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
@DisplayName("ProductGroupPriceCommandManager 단위 테스트")
class ProductGroupPriceCommandManagerTest {

    @InjectMocks private ProductGroupPriceCommandManager sut;

    @Mock private ProductGroupPriceCommandPort priceCommandPort;

    @Nested
    @DisplayName("persistAll() - 가격 일괄 갱신")
    class PersistAllTest {

        @Test
        @DisplayName("업데이트 목록을 Port에 위임한다")
        void persistAll_ValidUpdates_DelegatesToPort() {
            // given
            List<ProductGroupPriceUpdateData> updates =
                    List.of(new ProductGroupPriceUpdateData(1L, 50000, 10, 5, 2500));

            // when
            sut.persistAll(updates);

            // then
            then(priceCommandPort).should().persistAll(updates);
        }

        @Test
        @DisplayName("빈 목록이면 Port를 호출하지 않는다")
        void persistAll_EmptyList_SkipsPort() {
            // when
            sut.persistAll(List.of());

            // then
            then(priceCommandPort).shouldHaveNoInteractions();
        }
    }

    @Nested
    @DisplayName("persist() - 가격 초기 레코드 생성")
    class PersistTest {

        @Test
        @DisplayName("상품그룹 ID로 초기 가격 레코드 생성을 Port에 위임한다")
        void persist_ValidId_DelegatesToPort() {
            // given
            long productGroupId = 1L;

            // when
            sut.persist(productGroupId);

            // then
            then(priceCommandPort).should().persist(productGroupId);
        }
    }
}
