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
        @DisplayName("수정 커맨드로 per-package Command가 포함된 ProductGroupUpdateBundle을 생성한다")
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
            assertThat(result.updatedAt()).isEqualTo(now);
            then(productGroupReadManager).should().getById(ProductGroupId.of(productGroupId));
        }

        @Test
        @DisplayName("수정 번들에 이미지, 옵션, 설명, 고시 커맨드가 포함된다")
        void createUpdateBundle_ValidCommand_ContainsPerPackageCommands() {
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
            assertThat(result.imageCommand()).isNotNull();
            assertThat(result.imageCommand().productGroupId()).isEqualTo(productGroupId);
            assertThat(result.optionGroupCommand()).isNotNull();
            assertThat(result.optionGroupCommand().productGroupId()).isEqualTo(productGroupId);
            assertThat(result.descriptionCommand()).isNotNull();
            assertThat(result.descriptionCommand().productGroupId()).isEqualTo(productGroupId);
            assertThat(result.noticeCommand()).isNotNull();
            assertThat(result.noticeCommand().productGroupId()).isEqualTo(productGroupId);
            assertThat(result.productEntries()).hasSize(command.products().size());
        }

        @Test
        @DisplayName("이미지 커맨드가 없으면 imageCommand는 null이다")
        void createUpdateBundle_NoImages_ImageCommandIsNull() {
            // given
            long productGroupId = 2L;
            UpdateProductGroupFullCommand command =
                    new UpdateProductGroupFullCommand(
                            productGroupId,
                            "수정된 상품그룹",
                            2L,
                            2L,
                            2L,
                            2L,
                            "NONE",
                            30000,
                            25000,
                            null,
                            null,
                            null,
                            null,
                            null);
            ProductGroup existingGroup = ProductGroupFixtures.activeProductGroup(productGroupId);
            Instant now = Instant.parse("2024-01-01T00:00:00Z");

            given(timeProvider.now()).willReturn(now);
            given(productGroupReadManager.getById(ProductGroupId.of(productGroupId)))
                    .willReturn(existingGroup);

            // when
            ProductGroupUpdateBundle result = sut.createUpdateBundle(command);

            // then
            assertThat(result.imageCommand()).isNull();
            assertThat(result.optionGroupCommand()).isNull();
            assertThat(result.descriptionCommand()).isNull();
            assertThat(result.noticeCommand()).isNull();
            assertThat(result.productEntries()).isEmpty();
        }
    }
}
