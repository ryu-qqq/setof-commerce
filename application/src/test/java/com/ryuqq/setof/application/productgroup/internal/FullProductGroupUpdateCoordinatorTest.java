package com.ryuqq.setof.application.productgroup.internal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;

import com.ryuqq.setof.application.product.factory.ProductCommandFactory;
import com.ryuqq.setof.application.product.internal.ProductCommandCoordinator;
import com.ryuqq.setof.application.productdescription.internal.DescriptionCommandCoordinator;
import com.ryuqq.setof.application.productgroup.ProductGroupCommandFixtures;
import com.ryuqq.setof.application.productgroup.dto.bundle.ProductGroupUpdateBundle;
import com.ryuqq.setof.application.productgroup.manager.ProductGroupCommandManager;
import com.ryuqq.setof.application.productgroupimage.internal.ImageCommandCoordinator;
import com.ryuqq.setof.application.productnotice.internal.ProductNoticeCommandCoordinator;
import com.ryuqq.setof.application.selleroption.dto.result.SellerOptionUpdateResult;
import com.ryuqq.setof.application.selleroption.internal.SellerOptionCommandCoordinator;
import com.ryuqq.setof.domain.product.vo.ProductUpdateData;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import java.time.Instant;
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
@DisplayName("FullProductGroupUpdateCoordinator 단위 테스트")
class FullProductGroupUpdateCoordinatorTest {

    @InjectMocks private FullProductGroupUpdateCoordinator sut;

    @Mock private ProductGroupCommandManager productGroupCommandManager;
    @Mock private ImageCommandCoordinator imageCommandCoordinator;
    @Mock private SellerOptionCommandCoordinator sellerOptionCommandCoordinator;
    @Mock private DescriptionCommandCoordinator descriptionCommandCoordinator;
    @Mock private ProductNoticeCommandCoordinator noticeCommandCoordinator;
    @Mock private ProductCommandCoordinator productCommandCoordinator;
    @Mock private ProductCommandFactory productCommandFactory;

    @Nested
    @DisplayName("update() - 상품그룹 전체 수정")
    class UpdateTest {

        @Test
        @DisplayName("수정 번들의 모든 per-package Coordinator에 위임한다")
        void update_ValidBundle_DelegatesToEachCoordinator() {
            // given
            long productGroupId = 1L;
            ProductGroupUpdateBundle bundle =
                    ProductGroupCommandFixtures.updateBundle(productGroupId);

            SellerOptionUpdateResult optionResult =
                    new SellerOptionUpdateResult(List.of(), Instant.now(), false);

            given(sellerOptionCommandCoordinator.update(bundle.optionGroupCommand()))
                    .willReturn(optionResult);
            given(
                            productCommandFactory.toUpdateData(
                                    any(ProductGroupId.class),
                                    eq(bundle.productEntries()),
                                    any(),
                                    any(),
                                    any()))
                    .willReturn(
                            new ProductUpdateData(
                                    ProductGroupId.of(productGroupId), List.of(), Instant.now()));
            doNothing()
                    .when(productCommandCoordinator)
                    .update(any(ProductGroupId.class), any(ProductUpdateData.class));

            // when
            sut.update(bundle);

            // then
            then(productGroupCommandManager).should().persist(bundle.productGroup());
            then(imageCommandCoordinator).should().update(bundle.imageCommand());
            then(sellerOptionCommandCoordinator).should().update(bundle.optionGroupCommand());
            then(descriptionCommandCoordinator).should().update(bundle.descriptionCommand());
            then(noticeCommandCoordinator).should().update(bundle.noticeCommand());
            then(productCommandCoordinator).should().update(any(ProductGroupId.class), any());
        }

        @Test
        @DisplayName("이미지 커맨드가 null이면 imageCommandCoordinator를 호출하지 않는다")
        void update_NullImageCommand_SkipsImageCoordinator() {
            // given
            long productGroupId = 1L;
            ProductGroupUpdateBundle bundle =
                    ProductGroupCommandFixtures.updateBundle(productGroupId);
            ProductGroupUpdateBundle bundleWithoutImage =
                    new ProductGroupUpdateBundle(
                            bundle.productGroup(),
                            bundle.regularPrice(),
                            bundle.currentPrice(),
                            null,
                            bundle.optionGroupCommand(),
                            bundle.descriptionCommand(),
                            bundle.noticeCommand(),
                            bundle.productEntries(),
                            bundle.updatedAt());

            SellerOptionUpdateResult optionResult =
                    new SellerOptionUpdateResult(List.of(), Instant.now(), false);
            given(sellerOptionCommandCoordinator.update(bundleWithoutImage.optionGroupCommand()))
                    .willReturn(optionResult);
            given(
                            productCommandFactory.toUpdateData(
                                    any(ProductGroupId.class), any(), any(), any(), any()))
                    .willReturn(
                            new ProductUpdateData(
                                    ProductGroupId.of(productGroupId), List.of(), Instant.now()));
            doNothing()
                    .when(productCommandCoordinator)
                    .update(any(ProductGroupId.class), any(ProductUpdateData.class));

            // when
            sut.update(bundleWithoutImage);

            // then
            then(imageCommandCoordinator).shouldHaveNoInteractions();
        }
    }
}
