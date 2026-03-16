package com.ryuqq.setof.application.productgroup.factory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.common.time.TimeProvider;
import com.ryuqq.setof.application.productgroup.ProductGroupCommandFixtures;
import com.ryuqq.setof.application.productgroup.dto.bundle.ProductGroupRegistrationBundle;
import com.ryuqq.setof.application.productgroup.dto.bundle.ProductGroupUpdateBundle;
import com.ryuqq.setof.application.productgroup.dto.command.RegisterProductGroupCommand;
import com.ryuqq.setof.application.productgroup.dto.command.UpdateProductGroupFullCommand;
import com.ryuqq.setof.application.productgroup.manager.ProductGroupReadManager;
import com.ryuqq.setof.domain.productgroup.ProductGroupFixtures;
import com.ryuqq.setof.domain.productgroup.aggregate.ProductGroup;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import java.time.Instant;
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
@DisplayName("ProductGroupBundleFactory 단위 테스트")
class ProductGroupBundleFactoryTest {

    @InjectMocks private ProductGroupBundleFactory sut;

    @Mock private TimeProvider timeProvider;
    @Mock private ProductGroupReadManager productGroupReadManager;

    @Nested
    @DisplayName("createRegistrationBundle() - 등록 번들 생성")
    class CreateRegistrationBundleTest {

        @Test
        @DisplayName("등록 커맨드로 ProductGroupRegistrationBundle을 생성한다")
        void createRegistrationBundle_ValidCommand_ReturnsBundle() {
            // given
            RegisterProductGroupCommand command = ProductGroupCommandFixtures.registerCommand();
            Instant now = Instant.parse("2024-01-01T00:00:00Z");

            given(timeProvider.now()).willReturn(now);

            // when
            ProductGroupRegistrationBundle result = sut.createRegistrationBundle(command);

            // then
            assertThat(result).isNotNull();
            assertThat(result.productGroup()).isNotNull();
            assertThat(result.images()).hasSize(command.images().size());
            assertThat(result.optionType()).isEqualTo(command.optionType());
            assertThat(result.createdAt()).isEqualTo(now);
            then(timeProvider).should().now();
        }

        @Test
        @DisplayName("번들의 ProductGroup에 커맨드 값이 올바르게 반영된다")
        void createRegistrationBundle_ProductGroupReflectsCommand() {
            // given
            RegisterProductGroupCommand command = ProductGroupCommandFixtures.registerCommand();
            Instant now = Instant.parse("2024-01-01T00:00:00Z");

            given(timeProvider.now()).willReturn(now);

            // when
            ProductGroupRegistrationBundle result = sut.createRegistrationBundle(command);

            // then
            ProductGroup pg = result.productGroup();
            assertThat(pg.sellerId().value()).isEqualTo(command.sellerId());
            assertThat(pg.brandId().value()).isEqualTo(command.brandId());
        }
    }

    @Nested
    @DisplayName("createUpdateBundle() - 수정 번들 생성")
    class CreateUpdateBundleTest {

        @Test
        @DisplayName("수정 커맨드로 기존 상품그룹을 조회하여 ProductGroupUpdateBundle을 생성한다")
        void createUpdateBundle_ValidCommand_ReturnsBundle() {
            // given
            long productGroupId = 1L;
            UpdateProductGroupFullCommand command =
                    ProductGroupCommandFixtures.updateFullCommand(productGroupId);
            ProductGroup existingGroup = ProductGroupFixtures.activeProductGroup(productGroupId);
            Instant now = Instant.parse("2024-01-01T00:00:00Z");

            given(timeProvider.now()).willReturn(now);
            given(productGroupReadManager.getById(ProductGroupId.of(productGroupId)))
                    .willReturn(existingGroup);

            // when
            ProductGroupUpdateBundle result = sut.createUpdateBundle(command);

            // then
            assertThat(result).isNotNull();
            assertThat(result.productGroup()).isEqualTo(existingGroup);
            assertThat(result.command()).isEqualTo(command);
            assertThat(result.updatedAt()).isEqualTo(now);
            then(productGroupReadManager).should().getById(ProductGroupId.of(productGroupId));
        }
    }
}
