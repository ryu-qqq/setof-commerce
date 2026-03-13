package com.ryuqq.setof.adapter.in.rest.admin.v2.product.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.admin.product.ProductApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v2.product.dto.command.UpdateProductPriceApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.product.dto.command.UpdateProductStockApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.product.dto.command.UpdateProductsApiRequest;
import com.ryuqq.setof.application.product.dto.command.UpdateProductPriceCommand;
import com.ryuqq.setof.application.product.dto.command.UpdateProductStockCommand;
import com.ryuqq.setof.application.product.dto.command.UpdateProductsCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * ProductCommandApiMapper 단위 테스트.
 *
 * <p>상품 Command API Mapper의 변환 로직을 테스트합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("ProductCommandApiMapper 단위 테스트")
class ProductCommandApiMapperTest {

    private ProductCommandApiMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ProductCommandApiMapper();
    }

    @Nested
    @DisplayName("toCommand(Long, UpdateProductPriceApiRequest)")
    class ToUpdatePriceCommandTest {

        @Test
        @DisplayName("가격 수정 요청을 UpdateProductPriceCommand로 변환한다")
        void toCommand_Price_Success() {
            // given
            Long productId = ProductApiFixtures.DEFAULT_PRODUCT_ID;
            UpdateProductPriceApiRequest request = ProductApiFixtures.updatePriceRequest();

            // when
            UpdateProductPriceCommand command = mapper.toCommand(productId, request);

            // then
            assertThat(command.productId()).isEqualTo(productId);
            assertThat(command.regularPrice()).isEqualTo(request.regularPrice());
            assertThat(command.currentPrice()).isEqualTo(request.currentPrice());
        }

        @Test
        @DisplayName("다른 가격 값으로 변환한다")
        void toCommand_Price_CustomValues() {
            // given
            Long productId = 99L;
            UpdateProductPriceApiRequest request =
                    ProductApiFixtures.updatePriceRequest(200_000, 150_000);

            // when
            UpdateProductPriceCommand command = mapper.toCommand(productId, request);

            // then
            assertThat(command.productId()).isEqualTo(99L);
            assertThat(command.regularPrice()).isEqualTo(200_000);
            assertThat(command.currentPrice()).isEqualTo(150_000);
        }
    }

    @Nested
    @DisplayName("toCommand(Long, UpdateProductStockApiRequest)")
    class ToUpdateStockCommandTest {

        @Test
        @DisplayName("재고 수정 요청을 UpdateProductStockCommand로 변환한다")
        void toCommand_Stock_Success() {
            // given
            Long productId = ProductApiFixtures.DEFAULT_PRODUCT_ID;
            UpdateProductStockApiRequest request = ProductApiFixtures.updateStockRequest();

            // when
            UpdateProductStockCommand command = mapper.toCommand(productId, request);

            // then
            assertThat(command.productId()).isEqualTo(productId);
            assertThat(command.stockQuantity()).isEqualTo(request.stockQuantity());
        }

        @Test
        @DisplayName("재고 0으로 수정 시 Command로 변환한다")
        void toCommand_Stock_ZeroQuantity() {
            // given
            Long productId = 5L;
            UpdateProductStockApiRequest request = ProductApiFixtures.updateStockRequest(0);

            // when
            UpdateProductStockCommand command = mapper.toCommand(productId, request);

            // then
            assertThat(command.productId()).isEqualTo(5L);
            assertThat(command.stockQuantity()).isZero();
        }
    }

    @Nested
    @DisplayName("toCommand(Long, UpdateProductsApiRequest)")
    class ToUpdateProductsCommandTest {

        @Test
        @DisplayName("상품 일괄 수정 요청을 UpdateProductsCommand로 변환한다")
        void toCommand_Products_Success() {
            // given
            Long productGroupId = ProductApiFixtures.DEFAULT_PRODUCT_GROUP_ID;
            UpdateProductsApiRequest request = ProductApiFixtures.updateProductsRequest();

            // when
            UpdateProductsCommand command = mapper.toCommand(productGroupId, request);

            // then
            assertThat(command.productGroupId()).isEqualTo(productGroupId);
            assertThat(command.optionGroups()).hasSize(1);
            assertThat(command.products()).hasSize(1);
        }

        @Test
        @DisplayName("옵션 그룹 데이터를 올바르게 변환한다")
        void toCommand_Products_OptionGroupMappedCorrectly() {
            // given
            Long productGroupId = ProductApiFixtures.DEFAULT_PRODUCT_GROUP_ID;
            UpdateProductsApiRequest request = ProductApiFixtures.updateProductsRequest();

            // when
            UpdateProductsCommand command = mapper.toCommand(productGroupId, request);

            // then
            UpdateProductsCommand.OptionGroupData group = command.optionGroups().get(0);
            UpdateProductsApiRequest.OptionGroupApiRequest requestGroup =
                    request.optionGroups().get(0);

            assertThat(group.sellerOptionGroupId()).isEqualTo(requestGroup.sellerOptionGroupId());
            assertThat(group.optionGroupName()).isEqualTo(requestGroup.optionGroupName());
            assertThat(group.sortOrder()).isEqualTo(requestGroup.sortOrder());
            assertThat(group.optionValues()).hasSize(1);
        }

        @Test
        @DisplayName("옵션 값 데이터를 올바르게 변환한다")
        void toCommand_Products_OptionValueMappedCorrectly() {
            // given
            Long productGroupId = ProductApiFixtures.DEFAULT_PRODUCT_GROUP_ID;
            UpdateProductsApiRequest request = ProductApiFixtures.updateProductsRequest();

            // when
            UpdateProductsCommand command = mapper.toCommand(productGroupId, request);

            // then
            UpdateProductsCommand.OptionValueData value =
                    command.optionGroups().get(0).optionValues().get(0);
            UpdateProductsApiRequest.OptionValueApiRequest requestValue =
                    request.optionGroups().get(0).optionValues().get(0);

            assertThat(value.sellerOptionValueId()).isEqualTo(requestValue.sellerOptionValueId());
            assertThat(value.optionValueName()).isEqualTo(requestValue.optionValueName());
            assertThat(value.sortOrder()).isEqualTo(requestValue.sortOrder());
        }

        @Test
        @DisplayName("상품 데이터를 올바르게 변환한다")
        void toCommand_Products_ProductDataMappedCorrectly() {
            // given
            Long productGroupId = ProductApiFixtures.DEFAULT_PRODUCT_GROUP_ID;
            UpdateProductsApiRequest request = ProductApiFixtures.updateProductsRequest();

            // when
            UpdateProductsCommand command = mapper.toCommand(productGroupId, request);

            // then
            UpdateProductsCommand.ProductData productData = command.products().get(0);
            UpdateProductsApiRequest.ProductApiRequest requestProduct = request.products().get(0);

            assertThat(productData.productId()).isEqualTo(requestProduct.productId());
            assertThat(productData.skuCode()).isEqualTo(requestProduct.skuCode());
            assertThat(productData.regularPrice()).isEqualTo(requestProduct.regularPrice());
            assertThat(productData.currentPrice()).isEqualTo(requestProduct.currentPrice());
            assertThat(productData.stockQuantity()).isEqualTo(requestProduct.stockQuantity());
            assertThat(productData.sortOrder()).isEqualTo(requestProduct.sortOrder());
            assertThat(productData.selectedOptions()).hasSize(1);
        }

        @Test
        @DisplayName("선택 옵션 데이터를 올바르게 변환한다")
        void toCommand_Products_SelectedOptionMappedCorrectly() {
            // given
            Long productGroupId = ProductApiFixtures.DEFAULT_PRODUCT_GROUP_ID;
            UpdateProductsApiRequest request = ProductApiFixtures.updateProductsRequest();

            // when
            UpdateProductsCommand command = mapper.toCommand(productGroupId, request);

            // then
            var selectedOption = command.products().get(0).selectedOptions().get(0);
            var requestOption = request.products().get(0).selectedOptions().get(0);

            assertThat(selectedOption.optionGroupName()).isEqualTo(requestOption.optionGroupName());
            assertThat(selectedOption.optionValueName()).isEqualTo(requestOption.optionValueName());
        }
    }
}
