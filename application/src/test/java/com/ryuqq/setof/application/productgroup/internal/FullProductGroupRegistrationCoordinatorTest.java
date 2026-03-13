package com.ryuqq.setof.application.productgroup.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.productgroup.ProductGroupCommandFixtures;
import com.ryuqq.setof.application.productgroup.dto.bundle.ProductGroupRegistrationBundle;
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
@DisplayName("FullProductGroupRegistrationCoordinator 단위 테스트")
class FullProductGroupRegistrationCoordinatorTest {

    @InjectMocks private FullProductGroupRegistrationCoordinator sut;

    @Mock private ProductGroupPersistFacade persistFacade;

    @Nested
    @DisplayName("register() - 상품그룹 전체 등록")
    class RegisterTest {

        @Test
        @DisplayName("등록 번들을 PersistFacade에 위임하고 반환된 ID를 그대로 반환한다")
        void register_ValidBundle_DelegatesToFacadeAndReturnsId() {
            // given
            ProductGroupRegistrationBundle bundle =
                    ProductGroupCommandFixtures.registrationBundle();
            Long expectedId = 1L;

            given(
                            persistFacade.registerAll(
                                    bundle.productGroup(), bundle.command(), bundle.createdAt()))
                    .willReturn(expectedId);

            // when
            Long result = sut.register(bundle);

            // then
            assertThat(result).isEqualTo(expectedId);
            then(persistFacade)
                    .should()
                    .registerAll(bundle.productGroup(), bundle.command(), bundle.createdAt());
        }
    }
}
