package com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.admin.productgroup.ProductGroupApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.RegisterProductGroupApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.UpdateProductGroupBasicInfoApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.command.UpdateProductGroupFullApiRequest;
import com.ryuqq.setof.application.productgroup.dto.command.RegisterProductGroupCommand;
import com.ryuqq.setof.application.productgroup.dto.command.UpdateProductGroupBasicInfoCommand;
import com.ryuqq.setof.application.productgroup.dto.command.UpdateProductGroupFullCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * ProductGroupCommandApiMapper 단위 테스트.
 *
 * <p>상품 그룹 Command API Mapper의 변환 로직을 테스트합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("ProductGroupCommandApiMapper 단위 테스트")
class ProductGroupCommandApiMapperTest {

    private ProductGroupCommandApiMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ProductGroupCommandApiMapper();
    }

    @Nested
    @DisplayName("toCommand(RegisterProductGroupApiRequest)")
    class ToRegisterCommandTest {

        @Test
        @DisplayName("등록 요청을 RegisterProductGroupCommand로 변환한다")
        void toCommand_Register_Success() {
            // given
            RegisterProductGroupApiRequest request = ProductGroupApiFixtures.registerRequest();

            // when
            RegisterProductGroupCommand command = mapper.toCommand(request);

            // then
            assertThat(command.sellerId()).isEqualTo(request.sellerId());
            assertThat(command.brandId()).isEqualTo(request.brandId());
            assertThat(command.categoryId()).isEqualTo(request.categoryId());
            assertThat(command.shippingPolicyId()).isEqualTo(request.shippingPolicyId());
            assertThat(command.refundPolicyId()).isEqualTo(request.refundPolicyId());
            assertThat(command.productGroupName()).isEqualTo(request.productGroupName());
            assertThat(command.optionType()).isEqualTo(request.optionType());
            assertThat(command.regularPrice()).isEqualTo(request.regularPrice());
            assertThat(command.currentPrice()).isEqualTo(request.currentPrice());
        }

        @Test
        @DisplayName("이미지 목록을 ImageCommand 목록으로 변환한다")
        void toCommand_Register_ImagesMapping() {
            // given
            RegisterProductGroupApiRequest request = ProductGroupApiFixtures.registerRequest();

            // when
            RegisterProductGroupCommand command = mapper.toCommand(request);

            // then
            assertThat(command.images()).hasSize(1);
            RegisterProductGroupCommand.ImageCommand imageCommand = command.images().get(0);
            RegisterProductGroupApiRequest.ImageApiRequest imageRequest = request.images().get(0);
            assertThat(imageCommand.imageType()).isEqualTo(imageRequest.imageType());
            assertThat(imageCommand.imageUrl()).isEqualTo(imageRequest.imageUrl());
            assertThat(imageCommand.sortOrder()).isEqualTo(imageRequest.sortOrder());
        }

        @Test
        @DisplayName("옵션 그룹과 옵션 값을 Command로 변환한다")
        void toCommand_Register_OptionGroupsMapping() {
            // given
            RegisterProductGroupApiRequest request = ProductGroupApiFixtures.registerRequest();

            // when
            RegisterProductGroupCommand command = mapper.toCommand(request);

            // then
            assertThat(command.optionGroups()).hasSize(1);
            RegisterProductGroupCommand.OptionGroupCommand groupCommand =
                    command.optionGroups().get(0);
            RegisterProductGroupApiRequest.OptionGroupApiRequest groupRequest =
                    request.optionGroups().get(0);
            assertThat(groupCommand.optionGroupName()).isEqualTo(groupRequest.optionGroupName());
            assertThat(groupCommand.sortOrder()).isEqualTo(groupRequest.sortOrder());
            assertThat(groupCommand.optionValues()).hasSize(groupRequest.optionValues().size());
        }

        @Test
        @DisplayName("상품 목록을 ProductCommand 목록으로 변환한다")
        void toCommand_Register_ProductsMapping() {
            // given
            RegisterProductGroupApiRequest request = ProductGroupApiFixtures.registerRequest();

            // when
            RegisterProductGroupCommand command = mapper.toCommand(request);

            // then
            assertThat(command.products()).hasSize(1);
            RegisterProductGroupCommand.ProductCommand productCommand = command.products().get(0);
            RegisterProductGroupApiRequest.ProductApiRequest productRequest =
                    request.products().get(0);
            assertThat(productCommand.skuCode()).isEqualTo(productRequest.skuCode());
            assertThat(productCommand.regularPrice()).isEqualTo(productRequest.regularPrice());
            assertThat(productCommand.currentPrice()).isEqualTo(productRequest.currentPrice());
            assertThat(productCommand.stockQuantity()).isEqualTo(productRequest.stockQuantity());
            assertThat(productCommand.sortOrder()).isEqualTo(productRequest.sortOrder());
        }

        @Test
        @DisplayName("상세설명을 DescriptionCommand로 변환한다")
        void toCommand_Register_DescriptionMapping() {
            // given
            RegisterProductGroupApiRequest request = ProductGroupApiFixtures.registerRequest();

            // when
            RegisterProductGroupCommand command = mapper.toCommand(request);

            // then
            assertThat(command.description()).isNotNull();
            assertThat(command.description().content()).isEqualTo(request.description().content());
            assertThat(command.description().descriptionImages()).hasSize(2);
        }

        @Test
        @DisplayName("고시정보를 NoticeCommand로 변환한다")
        void toCommand_Register_NoticeMapping() {
            // given
            RegisterProductGroupApiRequest request = ProductGroupApiFixtures.registerRequest();

            // when
            RegisterProductGroupCommand command = mapper.toCommand(request);

            // then
            assertThat(command.notice()).isNotNull();
            assertThat(command.notice().entries()).hasSize(2);
            RegisterProductGroupCommand.NoticeEntryCommand entryCommand =
                    command.notice().entries().get(0);
            RegisterProductGroupApiRequest.NoticeEntryApiRequest entryRequest =
                    request.notice().entries().get(0);
            assertThat(entryCommand.noticeFieldId()).isEqualTo(entryRequest.noticeFieldId());
            assertThat(entryCommand.fieldName()).isEqualTo(entryRequest.fieldName());
            assertThat(entryCommand.fieldValue()).isEqualTo(entryRequest.fieldValue());
        }

        @Test
        @DisplayName("optionGroups가 null이면 optionType을 NONE으로 결정한다")
        void toCommand_Register_ResolveOptionType_None() {
            // given
            RegisterProductGroupApiRequest request =
                    ProductGroupApiFixtures.registerRequestWithoutOptions();

            // when
            RegisterProductGroupCommand command = mapper.toCommand(request);

            // then
            assertThat(command.optionType()).isEqualTo("NONE");
            assertThat(command.optionGroups()).isNull();
        }

        @Test
        @DisplayName("optionGroups가 2개 이상이면 optionType을 COMBINATION으로 결정한다")
        void toCommand_Register_ResolveOptionType_Combination() {
            // given
            RegisterProductGroupApiRequest request =
                    ProductGroupApiFixtures.registerRequestWithCombinationOptions();

            // when
            RegisterProductGroupCommand command = mapper.toCommand(request);

            // then
            assertThat(command.optionType()).isEqualTo("COMBINATION");
            assertThat(command.optionGroups()).hasSize(2);
        }

        @Test
        @DisplayName("description과 notice가 null이면 null Command를 반환한다")
        void toCommand_Register_NullDescriptionAndNotice() {
            // given
            RegisterProductGroupApiRequest request =
                    ProductGroupApiFixtures.registerRequestWithoutOptions();

            // when
            RegisterProductGroupCommand command = mapper.toCommand(request);

            // then
            assertThat(command.description()).isNull();
            assertThat(command.notice()).isNull();
        }
    }

    @Nested
    @DisplayName("toCommand(Long, UpdateProductGroupFullApiRequest)")
    class ToUpdateFullCommandTest {

        @Test
        @DisplayName("전체 수정 요청을 UpdateProductGroupFullCommand로 변환한다")
        void toCommand_UpdateFull_Success() {
            // given
            Long productGroupId = 1L;
            UpdateProductGroupFullApiRequest request = ProductGroupApiFixtures.updateFullRequest();

            // when
            UpdateProductGroupFullCommand command = mapper.toCommand(productGroupId, request);

            // then
            assertThat(command.productGroupId()).isEqualTo(productGroupId);
            assertThat(command.productGroupName()).isEqualTo(request.productGroupName());
            assertThat(command.brandId()).isEqualTo(request.brandId());
            assertThat(command.categoryId()).isEqualTo(request.categoryId());
            assertThat(command.shippingPolicyId()).isEqualTo(request.shippingPolicyId());
            assertThat(command.refundPolicyId()).isEqualTo(request.refundPolicyId());
            assertThat(command.optionType()).isEqualTo(request.optionType());
            assertThat(command.regularPrice()).isEqualTo(request.regularPrice());
            assertThat(command.currentPrice()).isEqualTo(request.currentPrice());
        }

        @Test
        @DisplayName("이미지 목록을 ImageCommand 목록으로 변환한다")
        void toCommand_UpdateFull_ImagesMapping() {
            // given
            Long productGroupId = 1L;
            UpdateProductGroupFullApiRequest request = ProductGroupApiFixtures.updateFullRequest();

            // when
            UpdateProductGroupFullCommand command = mapper.toCommand(productGroupId, request);

            // then
            assertThat(command.images()).hasSize(1);
            UpdateProductGroupFullCommand.ImageCommand imageCommand = command.images().get(0);
            UpdateProductGroupFullApiRequest.ImageApiRequest imageRequest = request.images().get(0);
            assertThat(imageCommand.imageType()).isEqualTo(imageRequest.imageType());
            assertThat(imageCommand.imageUrl()).isEqualTo(imageRequest.imageUrl());
            assertThat(imageCommand.sortOrder()).isEqualTo(imageRequest.sortOrder());
        }

        @Test
        @DisplayName("옵션 그룹과 옵션 값(ID 포함)을 Command로 변환한다")
        void toCommand_UpdateFull_OptionGroupsMapping() {
            // given
            Long productGroupId = 1L;
            UpdateProductGroupFullApiRequest request = ProductGroupApiFixtures.updateFullRequest();

            // when
            UpdateProductGroupFullCommand command = mapper.toCommand(productGroupId, request);

            // then
            assertThat(command.optionGroups()).hasSize(1);
            UpdateProductGroupFullCommand.OptionGroupCommand groupCommand =
                    command.optionGroups().get(0);
            UpdateProductGroupFullApiRequest.OptionGroupApiRequest groupRequest =
                    request.optionGroups().get(0);
            assertThat(groupCommand.sellerOptionGroupId())
                    .isEqualTo(groupRequest.sellerOptionGroupId());
            assertThat(groupCommand.optionGroupName()).isEqualTo(groupRequest.optionGroupName());
            assertThat(groupCommand.sortOrder()).isEqualTo(groupRequest.sortOrder());
            assertThat(groupCommand.optionValues()).hasSize(2);
        }

        @Test
        @DisplayName("상품 목록(ID 포함)을 ProductCommand 목록으로 변환한다")
        void toCommand_UpdateFull_ProductsMapping() {
            // given
            Long productGroupId = 1L;
            UpdateProductGroupFullApiRequest request = ProductGroupApiFixtures.updateFullRequest();

            // when
            UpdateProductGroupFullCommand command = mapper.toCommand(productGroupId, request);

            // then
            assertThat(command.products()).hasSize(1);
            UpdateProductGroupFullCommand.ProductCommand productCommand = command.products().get(0);
            UpdateProductGroupFullApiRequest.ProductApiRequest productRequest =
                    request.products().get(0);
            assertThat(productCommand.productId()).isEqualTo(productRequest.productId());
            assertThat(productCommand.skuCode()).isEqualTo(productRequest.skuCode());
            assertThat(productCommand.regularPrice()).isEqualTo(productRequest.regularPrice());
            assertThat(productCommand.currentPrice()).isEqualTo(productRequest.currentPrice());
            assertThat(productCommand.stockQuantity()).isEqualTo(productRequest.stockQuantity());
            assertThat(productCommand.sortOrder()).isEqualTo(productRequest.sortOrder());
        }

        @Test
        @DisplayName("optionGroups가 null이면 optionType을 NONE으로 결정한다")
        void toCommand_UpdateFull_ResolveOptionType_None() {
            // given
            Long productGroupId = 1L;
            UpdateProductGroupFullApiRequest request =
                    ProductGroupApiFixtures.updateFullRequestWithoutOptions();

            // when
            UpdateProductGroupFullCommand command = mapper.toCommand(productGroupId, request);

            // then
            assertThat(command.optionType()).isEqualTo("NONE");
            assertThat(command.optionGroups()).isNull();
        }
    }

    @Nested
    @DisplayName("toCommand(Long, UpdateProductGroupBasicInfoApiRequest)")
    class ToUpdateBasicInfoCommandTest {

        @Test
        @DisplayName("기본 정보 수정 요청을 UpdateProductGroupBasicInfoCommand로 변환한다")
        void toCommand_UpdateBasicInfo_Success() {
            // given
            Long productGroupId = 1L;
            UpdateProductGroupBasicInfoApiRequest request =
                    ProductGroupApiFixtures.updateBasicInfoRequest();

            // when
            UpdateProductGroupBasicInfoCommand command = mapper.toCommand(productGroupId, request);

            // then
            assertThat(command.productGroupId()).isEqualTo(productGroupId);
            assertThat(command.productGroupName()).isEqualTo(request.productGroupName());
            assertThat(command.brandId()).isEqualTo(request.brandId());
            assertThat(command.categoryId()).isEqualTo(request.categoryId());
            assertThat(command.shippingPolicyId()).isEqualTo(request.shippingPolicyId());
            assertThat(command.refundPolicyId()).isEqualTo(request.refundPolicyId());
        }

        @Test
        @DisplayName("PathVariable의 productGroupId를 Command에 올바르게 설정한다")
        void toCommand_UpdateBasicInfo_ProductGroupIdFromPath() {
            // given
            Long productGroupId = 999L;
            UpdateProductGroupBasicInfoApiRequest request =
                    ProductGroupApiFixtures.updateBasicInfoRequest();

            // when
            UpdateProductGroupBasicInfoCommand command = mapper.toCommand(productGroupId, request);

            // then
            assertThat(command.productGroupId()).isEqualTo(999L);
        }
    }
}
