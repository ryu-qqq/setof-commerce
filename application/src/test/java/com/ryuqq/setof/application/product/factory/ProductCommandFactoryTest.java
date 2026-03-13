package com.ryuqq.setof.application.product.factory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import com.ryuqq.setof.application.common.dto.command.StatusChangeContext;
import com.ryuqq.setof.application.common.time.TimeProvider;
import com.ryuqq.setof.application.product.ProductCommandFixtures;
import com.ryuqq.setof.application.product.dto.command.ProductDiffUpdateEntry;
import com.ryuqq.setof.application.product.dto.command.RegisterProductsCommand;
import com.ryuqq.setof.application.product.dto.command.SelectedOption;
import com.ryuqq.setof.application.product.dto.command.UpdateProductPriceCommand;
import com.ryuqq.setof.application.product.dto.command.UpdateProductStockCommand;
import com.ryuqq.setof.application.product.dto.command.UpdateProductsCommand;
import com.ryuqq.setof.application.selleroption.dto.command.UpdateSellerOptionGroupsCommand;
import com.ryuqq.setof.domain.product.id.ProductId;
import com.ryuqq.setof.domain.product.vo.ProductCreationData;
import com.ryuqq.setof.domain.product.vo.ProductUpdateData;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.productgroup.id.SellerOptionValueId;
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
@DisplayName("ProductCommandFactory 단위 테스트")
class ProductCommandFactoryTest {

    @InjectMocks private ProductCommandFactory sut;

    @Mock private TimeProvider timeProvider;

    private static final Instant FIXED_NOW = Instant.parse("2024-01-01T00:00:00Z");

    @Nested
    @DisplayName("createPriceUpdateContext() - 가격 수정 컨텍스트 생성")
    class CreatePriceUpdateContextTest {

        @Test
        @DisplayName("커맨드로 가격 수정 컨텍스트를 생성한다")
        void createPriceUpdateContext_ValidCommand_ReturnsContext() {
            // given
            UpdateProductPriceCommand command = ProductCommandFixtures.updatePriceCommand(1L);

            given(timeProvider.now()).willReturn(FIXED_NOW);

            // when
            StatusChangeContext<ProductId> result = sut.createPriceUpdateContext(command);

            // then
            assertThat(result).isNotNull();
            assertThat(result.id().value()).isEqualTo(command.productId());
            assertThat(result.changedAt()).isEqualTo(FIXED_NOW);
        }
    }

    @Nested
    @DisplayName("createStockUpdateContext() - 재고 수정 컨텍스트 생성")
    class CreateStockUpdateContextTest {

        @Test
        @DisplayName("커맨드로 재고 수정 컨텍스트를 생성한다")
        void createStockUpdateContext_ValidCommand_ReturnsContext() {
            // given
            UpdateProductStockCommand command = ProductCommandFixtures.updateStockCommand(1L);

            given(timeProvider.now()).willReturn(FIXED_NOW);

            // when
            StatusChangeContext<ProductId> result = sut.createStockUpdateContext(command);

            // then
            assertThat(result).isNotNull();
            assertThat(result.id().value()).isEqualTo(command.productId());
            assertThat(result.changedAt()).isEqualTo(FIXED_NOW);
        }
    }

    @Nested
    @DisplayName("toCreationDataList() - 상품 등록 데이터 목록 변환")
    class ToCreationDataListTest {

        @Test
        @DisplayName("옵션 이름으로 SellerOptionValueId를 resolve하여 ProductCreationData 목록을 생성한다")
        void toCreationDataList_ValidData_ResolvesOptionIds() {
            // given
            List<RegisterProductsCommand.ProductData> products =
                    List.of(
                            new RegisterProductsCommand.ProductData(
                                    null,
                                    "SKU-001",
                                    50000,
                                    45000,
                                    10,
                                    1,
                                    List.of(new SelectedOption("색상", "블랙"))));

            List<UpdateSellerOptionGroupsCommand.OptionGroupCommand> optionGroups =
                    List.of(
                            new UpdateSellerOptionGroupsCommand.OptionGroupCommand(
                                    null,
                                    "색상",
                                    1,
                                    List.of(
                                            new UpdateSellerOptionGroupsCommand.OptionValueCommand(
                                                    null, "블랙", 1))));

            SellerOptionValueId resolvedId = SellerOptionValueId.of(100L);
            List<SellerOptionValueId> allOptionValueIds = List.of(resolvedId);

            // when
            List<ProductCreationData> result =
                    sut.toCreationDataList(products, optionGroups, allOptionValueIds);

            // then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).resolvedOptionValueIds()).containsExactly(resolvedId);
            assertThat(result.get(0).regularPrice().value()).isEqualTo(50000);
            assertThat(result.get(0).stockQuantity()).isEqualTo(10);
        }

        @Test
        @DisplayName("존재하지 않는 옵션 그룹을 선택하면 IllegalArgumentException이 발생한다")
        void toCreationDataList_NonExistingOptionGroup_ThrowsException() {
            // given
            List<RegisterProductsCommand.ProductData> products =
                    List.of(
                            new RegisterProductsCommand.ProductData(
                                    null,
                                    "SKU-001",
                                    50000,
                                    45000,
                                    10,
                                    1,
                                    List.of(new SelectedOption("없는그룹", "블랙"))));

            List<UpdateSellerOptionGroupsCommand.OptionGroupCommand> optionGroups =
                    List.of(
                            new UpdateSellerOptionGroupsCommand.OptionGroupCommand(
                                    null,
                                    "색상",
                                    1,
                                    List.of(
                                            new UpdateSellerOptionGroupsCommand.OptionValueCommand(
                                                    null, "블랙", 1))));

            List<SellerOptionValueId> allOptionValueIds = List.of(SellerOptionValueId.of(100L));

            // when & then
            assertThatThrownBy(
                            () -> sut.toCreationDataList(products, optionGroups, allOptionValueIds))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("없는그룹");
        }

        @Test
        @DisplayName("존재하지 않는 옵션 값을 선택하면 IllegalArgumentException이 발생한다")
        void toCreationDataList_NonExistingOptionValue_ThrowsException() {
            // given
            List<RegisterProductsCommand.ProductData> products =
                    List.of(
                            new RegisterProductsCommand.ProductData(
                                    null,
                                    "SKU-001",
                                    50000,
                                    45000,
                                    10,
                                    1,
                                    List.of(new SelectedOption("색상", "없는값"))));

            List<UpdateSellerOptionGroupsCommand.OptionGroupCommand> optionGroups =
                    List.of(
                            new UpdateSellerOptionGroupsCommand.OptionGroupCommand(
                                    null,
                                    "색상",
                                    1,
                                    List.of(
                                            new UpdateSellerOptionGroupsCommand.OptionValueCommand(
                                                    null, "블랙", 1))));

            List<SellerOptionValueId> allOptionValueIds = List.of(SellerOptionValueId.of(100L));

            // when & then
            assertThatThrownBy(
                            () -> sut.toCreationDataList(products, optionGroups, allOptionValueIds))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("없는값");
        }
    }

    @Nested
    @DisplayName("toOptionCommand() - UpdateProductsCommand → UpdateSellerOptionGroupsCommand 변환")
    class ToOptionCommandTest {

        @Test
        @DisplayName("UpdateProductsCommand의 옵션 그룹을 UpdateSellerOptionGroupsCommand로 변환한다")
        void toOptionCommand_ValidCommand_ReturnsOptionCommand() {
            // given
            UpdateProductsCommand command = ProductCommandFixtures.updateProductsCommand(1L);

            // when
            UpdateSellerOptionGroupsCommand result = sut.toOptionCommand(command);

            // then
            assertThat(result).isNotNull();
            assertThat(result.productGroupId()).isEqualTo(command.productGroupId());
            assertThat(result.optionGroups()).hasSize(1);
            assertThat(result.optionGroups().get(0).optionGroupName()).isEqualTo("색상");
        }
    }

    @Nested
    @DisplayName("toEntries() - ProductData → ProductDiffUpdateEntry 변환")
    class ToEntriesTest {

        @Test
        @DisplayName("ProductData 목록을 ProductDiffUpdateEntry 목록으로 변환한다")
        void toEntries_ValidProductData_ReturnsEntries() {
            // given
            UpdateProductsCommand command = ProductCommandFixtures.updateProductsCommand(1L);

            // when
            List<ProductDiffUpdateEntry> result = sut.toEntries(command.products());

            // then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).productId()).isEqualTo(1L);
            assertThat(result.get(0).skuCode()).isEqualTo("SKU-001");
        }
    }

    @Nested
    @DisplayName("toUpdateData() - ProductUpdateData 생성")
    class ToUpdateDataTest {

        @Test
        @DisplayName("기존 상품 수정 엔트리로 ProductUpdateData를 생성한다")
        void toUpdateData_ExistingProduct_ReturnsUpdateData() {
            // given
            ProductGroupId pgId = ProductGroupId.of(1L);
            List<ProductDiffUpdateEntry> entries =
                    List.of(
                            new ProductDiffUpdateEntry(
                                    1L, "SKU-001", 50000, 45000, 10, 1, List.of()));

            List<UpdateSellerOptionGroupsCommand.OptionGroupCommand> optionGroups = List.of();
            List<SellerOptionValueId> resolvedValueIds = List.of();

            // when
            ProductUpdateData result =
                    sut.toUpdateData(pgId, entries, optionGroups, resolvedValueIds, FIXED_NOW);

            // then
            assertThat(result).isNotNull();
            assertThat(result.productGroupId()).isEqualTo(pgId);
            assertThat(result.entries()).hasSize(1);
            assertThat(result.updatedAt()).isEqualTo(FIXED_NOW);
        }
    }
}
