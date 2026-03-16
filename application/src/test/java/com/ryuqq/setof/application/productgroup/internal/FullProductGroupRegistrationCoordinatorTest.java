package com.ryuqq.setof.application.productgroup.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.product.factory.ProductCommandFactory;
import com.ryuqq.setof.application.product.internal.ProductCommandCoordinator;
import com.ryuqq.setof.application.productdescription.internal.DescriptionCommandCoordinator;
import com.ryuqq.setof.application.productgroup.ProductGroupCommandFixtures;
import com.ryuqq.setof.application.productgroup.dto.bundle.ProductGroupRegistrationBundle;
import com.ryuqq.setof.application.productgroup.manager.ProductGroupCommandManager;
import com.ryuqq.setof.application.productgroup.manager.ProductGroupPriceCommandManager;
import com.ryuqq.setof.application.productgroupimage.internal.ImageCommandCoordinator;
import com.ryuqq.setof.application.productnotice.internal.ProductNoticeCommandCoordinator;
import com.ryuqq.setof.application.selleroption.internal.SellerOptionCommandCoordinator;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
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
@DisplayName("FullProductGroupRegistrationCoordinator 단위 테스트")
class FullProductGroupRegistrationCoordinatorTest {

    @InjectMocks private FullProductGroupRegistrationCoordinator sut;

    @Mock private ProductGroupCommandManager productGroupCommandManager;
    @Mock private ProductGroupPriceCommandManager priceCommandManager;
    @Mock private ImageCommandCoordinator imageCommandCoordinator;
    @Mock private SellerOptionCommandCoordinator sellerOptionCommandCoordinator;
    @Mock private DescriptionCommandCoordinator descriptionCommandCoordinator;
    @Mock private ProductNoticeCommandCoordinator noticeCommandCoordinator;
    @Mock private ProductCommandCoordinator productCommandCoordinator;
    @Mock private ProductCommandFactory productCommandFactory;

    @Nested
    @DisplayName("register() - 상품그룹 전체 등록")
    class RegisterTest {

        @Test
        @DisplayName("각 per-package Coordinator에 위임하고 상품그룹 ID를 반환한다")
        void register_ValidBundle_DelegatesToCoordinatorsAndReturnsId() {
            // given
            ProductGroupRegistrationBundle bundle =
                    ProductGroupCommandFixtures.registrationBundle();
            Long expectedId = 1L;

            given(productGroupCommandManager.persist(bundle.productGroup())).willReturn(expectedId);
            given(sellerOptionCommandCoordinator.register(eq(ProductGroupId.of(expectedId)), any()))
                    .willReturn(List.of());

            // when
            Long result = sut.register(bundle);

            // then
            assertThat(result).isEqualTo(expectedId);
            then(productGroupCommandManager).should().persist(bundle.productGroup());
            then(priceCommandManager).should().persist(expectedId);
            then(imageCommandCoordinator).should().register(any());
            then(sellerOptionCommandCoordinator).should().register(any(), any());
        }
    }
}
