package com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.admin.productgroup.ProductGroupQueryApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.response.ProductGroupDetailApiResponse;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupDetailCompositeResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * ProductGroupQueryApiMapper 단위 테스트.
 *
 * <p>toDetailResponse() 변환 로직의 정합성을 검증합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("ProductGroupQueryApiMapper 단위 테스트")
class ProductGroupQueryApiMapperTest {

    private ProductGroupQueryApiMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ProductGroupQueryApiMapper();
    }

    @Nested
    @DisplayName("toDetailResponse")
    class ToDetailResponseTest {

        @Test
        @DisplayName("모든 필드가 존재하는 CompositeResult를 ProductGroupDetailApiResponse로 변환한다")
        void toDetailResponse_AllFieldsPresent_Success() {
            // given
            ProductGroupDetailCompositeResult result =
                    ProductGroupQueryApiFixtures.compositeResult();

            // when
            ProductGroupDetailApiResponse response = mapper.toDetailResponse(result);

            // then
            assertThat(response.id()).isEqualTo(result.id());
            assertThat(response.sellerId()).isEqualTo(result.sellerId());
            assertThat(response.sellerName()).isEqualTo(result.sellerName());
            assertThat(response.brandId()).isEqualTo(result.brandId());
            assertThat(response.brandName()).isEqualTo(result.brandName());
            assertThat(response.categoryId()).isEqualTo(result.categoryId());
            assertThat(response.categoryName()).isEqualTo(result.categoryName());
            assertThat(response.categoryPath()).isEqualTo(result.categoryPath());
            assertThat(response.productGroupName()).isEqualTo(result.productGroupName());
            assertThat(response.optionType()).isEqualTo(result.optionType());
            assertThat(response.status()).isEqualTo(result.status());
            assertThat(response.createdAt()).isNotNull();
            assertThat(response.updatedAt()).isNotNull();
            assertThat(response.images()).hasSize(1);
            assertThat(response.optionProductMatrix()).isNotNull();
            assertThat(response.shippingPolicy()).isNotNull();
            assertThat(response.refundPolicy()).isNotNull();
            assertThat(response.description()).isNotNull();
            assertThat(response.productNotice()).isNotNull();
        }

        @Test
        @DisplayName("이미지 목록을 올바르게 변환한다")
        void toDetailResponse_Images_MappedCorrectly() {
            // given
            ProductGroupDetailCompositeResult result =
                    ProductGroupQueryApiFixtures.compositeResult();

            // when
            ProductGroupDetailApiResponse response = mapper.toDetailResponse(result);

            // then
            assertThat(response.images()).hasSize(1);
            assertThat(response.images().get(0).id()).isEqualTo(result.images().get(0).id());
            assertThat(response.images().get(0).imageUrl())
                    .isEqualTo(result.images().get(0).imageUrl());
            assertThat(response.images().get(0).imageType())
                    .isEqualTo(result.images().get(0).imageType());
            assertThat(response.images().get(0).sortOrder())
                    .isEqualTo(result.images().get(0).sortOrder());
        }

        @Test
        @DisplayName("shippingPolicy가 null이면 응답의 shippingPolicy도 null이다")
        void toDetailResponse_NullShippingPolicy_ReturnsNullShippingPolicy() {
            // given
            ProductGroupDetailCompositeResult result =
                    ProductGroupQueryApiFixtures.compositeResultWithNullShippingPolicy();

            // when
            ProductGroupDetailApiResponse response = mapper.toDetailResponse(result);

            // then
            assertThat(response.shippingPolicy()).isNull();
            assertThat(response.refundPolicy()).isNotNull();
        }

        @Test
        @DisplayName("refundPolicy가 null이면 응답의 refundPolicy도 null이다")
        void toDetailResponse_NullRefundPolicy_ReturnsNullRefundPolicy() {
            // given
            ProductGroupDetailCompositeResult result =
                    ProductGroupQueryApiFixtures.compositeResultWithNullRefundPolicy();

            // when
            ProductGroupDetailApiResponse response = mapper.toDetailResponse(result);

            // then
            assertThat(response.refundPolicy()).isNull();
            assertThat(response.shippingPolicy()).isNotNull();
        }

        @Test
        @DisplayName("description이 null이면 응답의 description도 null이다")
        void toDetailResponse_NullDescription_ReturnsNullDescription() {
            // given
            ProductGroupDetailCompositeResult result =
                    ProductGroupQueryApiFixtures.compositeResultWithNullDescription();

            // when
            ProductGroupDetailApiResponse response = mapper.toDetailResponse(result);

            // then
            assertThat(response.description()).isNull();
            assertThat(response.productNotice()).isNotNull();
        }

        @Test
        @DisplayName("productNotice가 null이면 응답의 productNotice도 null이다")
        void toDetailResponse_NullProductNotice_ReturnsNullProductNotice() {
            // given
            ProductGroupDetailCompositeResult result =
                    ProductGroupQueryApiFixtures.compositeResultWithNullProductNotice();

            // when
            ProductGroupDetailApiResponse response = mapper.toDetailResponse(result);

            // then
            assertThat(response.productNotice()).isNull();
            assertThat(response.description()).isNotNull();
        }

        @Test
        @DisplayName("images가 빈 리스트이면 응답의 images도 빈 리스트다")
        void toDetailResponse_EmptyImages_ReturnsEmptyImageList() {
            // given
            ProductGroupDetailCompositeResult result =
                    ProductGroupQueryApiFixtures.compositeResultWithEmptyImages();

            // when
            ProductGroupDetailApiResponse response = mapper.toDetailResponse(result);

            // then
            assertThat(response.images()).isEmpty();
        }

        @Test
        @DisplayName("optionProductMatrix의 optionGroups와 products를 올바르게 변환한다")
        void toDetailResponse_OptionMatrix_MappedCorrectly() {
            // given
            ProductGroupDetailCompositeResult result =
                    ProductGroupQueryApiFixtures.compositeResult();

            // when
            ProductGroupDetailApiResponse response = mapper.toDetailResponse(result);

            // then
            assertThat(response.optionProductMatrix().optionGroups()).hasSize(1);
            assertThat(response.optionProductMatrix().products()).hasSize(1);

            assertThat(response.optionProductMatrix().optionGroups().get(0).id())
                    .isEqualTo(result.optionProductMatrix().optionGroups().get(0).id());
            assertThat(response.optionProductMatrix().optionGroups().get(0).optionGroupName())
                    .isEqualTo(
                            result.optionProductMatrix().optionGroups().get(0).optionGroupName());
            assertThat(response.optionProductMatrix().optionGroups().get(0).optionValues())
                    .hasSize(1);
        }

        @Test
        @DisplayName("shippingPolicy의 기본 필드를 올바르게 변환한다")
        void toDetailResponse_ShippingPolicy_FieldsMappedCorrectly() {
            // given
            ProductGroupDetailCompositeResult result =
                    ProductGroupQueryApiFixtures.compositeResult();

            // when
            ProductGroupDetailApiResponse response = mapper.toDetailResponse(result);

            // then
            assertThat(response.shippingPolicy().policyId())
                    .isEqualTo(result.shippingPolicy().policyId());
            assertThat(response.shippingPolicy().policyName())
                    .isEqualTo(result.shippingPolicy().policyName());
            assertThat(response.shippingPolicy().defaultPolicy())
                    .isEqualTo(result.shippingPolicy().defaultPolicy());
            assertThat(response.shippingPolicy().active())
                    .isEqualTo(result.shippingPolicy().active());
            assertThat(response.shippingPolicy().shippingFeeType())
                    .isEqualTo(result.shippingPolicy().shippingFeeType());
            assertThat(response.shippingPolicy().baseFee())
                    .isEqualTo(result.shippingPolicy().baseFee().intValue());
            assertThat(response.shippingPolicy().freeThreshold())
                    .isEqualTo(result.shippingPolicy().freeThreshold().intValue());
            assertThat(response.shippingPolicy().createdAt()).isNotNull();
        }

        @Test
        @DisplayName("refundPolicy의 기본 필드를 올바르게 변환한다")
        void toDetailResponse_RefundPolicy_FieldsMappedCorrectly() {
            // given
            ProductGroupDetailCompositeResult result =
                    ProductGroupQueryApiFixtures.compositeResult();

            // when
            ProductGroupDetailApiResponse response = mapper.toDetailResponse(result);

            // then
            assertThat(response.refundPolicy().policyId())
                    .isEqualTo(result.refundPolicy().policyId());
            assertThat(response.refundPolicy().policyName())
                    .isEqualTo(result.refundPolicy().policyName());
            assertThat(response.refundPolicy().returnPeriodDays())
                    .isEqualTo(result.refundPolicy().returnPeriodDays());
            assertThat(response.refundPolicy().exchangePeriodDays())
                    .isEqualTo(result.refundPolicy().exchangePeriodDays());
            assertThat(response.refundPolicy().nonReturnableConditions()).hasSize(1);
            assertThat(response.refundPolicy().nonReturnableConditions().get(0).code())
                    .isEqualTo(result.refundPolicy().nonReturnableConditions().get(0).code());
        }

        @Test
        @DisplayName("description의 기본 필드를 올바르게 변환한다")
        void toDetailResponse_Description_FieldsMappedCorrectly() {
            // given
            ProductGroupDetailCompositeResult result =
                    ProductGroupQueryApiFixtures.compositeResult();

            // when
            ProductGroupDetailApiResponse response = mapper.toDetailResponse(result);

            // then
            assertThat(response.description().id()).isEqualTo(result.description().id());
            assertThat(response.description().content()).isEqualTo(result.description().content());
            assertThat(response.description().cdnPath()).isEqualTo(result.description().cdnPath());
            assertThat(response.description().images()).hasSize(1);
        }

        @Test
        @DisplayName("productNotice의 기본 필드를 올바르게 변환한다")
        void toDetailResponse_ProductNotice_FieldsMappedCorrectly() {
            // given
            ProductGroupDetailCompositeResult result =
                    ProductGroupQueryApiFixtures.compositeResult();

            // when
            ProductGroupDetailApiResponse response = mapper.toDetailResponse(result);

            // then
            assertThat(response.productNotice().id()).isEqualTo(result.productNotice().id());
            assertThat(response.productNotice().entries()).hasSize(2);
            assertThat(response.productNotice().entries().get(0).id())
                    .isEqualTo(result.productNotice().entries().get(0).id());
            assertThat(response.productNotice().entries().get(0).noticeFieldId())
                    .isEqualTo(result.productNotice().entries().get(0).noticeFieldId());
            assertThat(response.productNotice().entries().get(0).fieldValue())
                    .isEqualTo(result.productNotice().entries().get(0).fieldValue());
            assertThat(response.productNotice().createdAt()).isNotNull();
            assertThat(response.productNotice().updatedAt()).isNotNull();
        }
    }
}
