package com.ryuqq.setof.application.productgroup.internal;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;

import com.ryuqq.setof.application.productgroup.ProductGroupCommandFixtures;
import com.ryuqq.setof.application.productgroup.dto.bundle.ProductGroupUpdateBundle;
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
@DisplayName("FullProductGroupUpdateCoordinator 단위 테스트")
class FullProductGroupUpdateCoordinatorTest {

    @InjectMocks private FullProductGroupUpdateCoordinator sut;

    @Mock private ProductGroupPersistFacade persistFacade;

    @Nested
    @DisplayName("update() - 상품그룹 전체 수정")
    class UpdateTest {

        @Test
        @DisplayName("수정 번들을 PersistFacade에 위임한다")
        void update_ValidBundle_DelegatesToFacade() {
            // given
            long productGroupId = 1L;
            ProductGroupUpdateBundle bundle =
                    ProductGroupCommandFixtures.updateBundle(productGroupId);

            doNothing()
                    .when(persistFacade)
                    .updateAll(bundle.productGroup(), bundle.command(), bundle.updatedAt());

            // when
            sut.update(bundle);

            // then
            then(persistFacade)
                    .should()
                    .updateAll(bundle.productGroup(), bundle.command(), bundle.updatedAt());
        }
    }
}
