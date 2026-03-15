package com.ryuqq.setof.application.productgroup.assembler;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.application.productgroup.ProductGroupCompositeQueryFixtures;
import com.ryuqq.setof.application.productgroup.ProductGroupQueryFixtures;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupDetailBundle;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupDetailCompositeResult;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupDetailImageResults;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupListBundle;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupListCompositeResult;
import com.ryuqq.setof.application.productgroup.dto.composite.WebProductGroupDetailCompositeResult;
import com.ryuqq.setof.application.productgroup.dto.response.ProductGroupSliceResult;
import com.ryuqq.setof.domain.productgroup.ProductGroupFixtures;
import com.ryuqq.setof.domain.productgroup.query.ProductGroupSortKey;
import com.setof.commerce.domain.product.ProductFixtures;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductGroupAssembler 단위 테스트")
class ProductGroupAssemblerTest {

    private ProductGroupAssembler sut;

    @BeforeEach
    void setUp() {
        sut = new ProductGroupAssembler();
    }

    @Nested
    @DisplayName("toSliceResult() - 목록 번들 → SliceResult 변환")
    class ToSliceResultTest {

        @Test
        @DisplayName("번들이 비어있으면 empty SliceResult를 반환한다")
        void toSliceResult_EmptyBundle_ReturnsEmptySliceResult() {
            // given
            ProductGroupListBundle bundle = ProductGroupQueryFixtures.emptyListBundle();
            int requestedSize = 20;

            // when
            ProductGroupSliceResult result = sut.toSliceResult(bundle, requestedSize);

            // then
            assertThat(result.content()).isEmpty();
            assertThat(result.totalElements()).isZero();
            assertThat(result.sliceMeta().hasNext()).isFalse();
        }

        @Test
        @DisplayName("결과 수가 요청 크기 이하면 hasNext가 false이다")
        void toSliceResult_ResultsLessThanSize_HasNextFalse() {
            // given
            ProductGroupListBundle bundle = ProductGroupQueryFixtures.listBundleWithSize(2);
            int requestedSize = 20;

            // when
            ProductGroupSliceResult result = sut.toSliceResult(bundle, requestedSize);

            // then
            assertThat(result.content()).hasSize(2);
            assertThat(result.sliceMeta().hasNext()).isFalse();
        }

        @Test
        @DisplayName("결과 수가 요청 크기 초과면 hasNext가 true이고 nextCursor가 설정된다")
        void toSliceResult_ResultsMoreThanSize_HasNextTrue() {
            // given
            int requestedSize = 2;
            ProductGroupListBundle bundle =
                    ProductGroupQueryFixtures.listBundleWithSize(requestedSize + 1);

            // when
            ProductGroupSliceResult result = sut.toSliceResult(bundle, requestedSize);

            // then
            assertThat(result.content()).hasSize(requestedSize);
            assertThat(result.sliceMeta().hasNext()).isTrue();
            assertThat(result.sliceMeta().cursor()).isNotNull();
        }

        @Test
        @DisplayName("CREATED_AT sortKey이면 커서에 id와 createdAt가 포함된다")
        void toSliceResult_CreatedAtSortKey_CursorContainsIdAndDate() {
            // given
            List<ProductGroupListCompositeResult> results =
                    List.of(ProductGroupQueryFixtures.listCompositeResult(1L));
            ProductGroupListBundle bundle =
                    new ProductGroupListBundle(results, 1L, ProductGroupSortKey.CREATED_AT);
            int requestedSize = 20;

            // when
            ProductGroupSliceResult result = sut.toSliceResult(bundle, requestedSize);

            // then
            assertThat(result.sliceMeta().cursor()).isNotNull();
            assertThat(result.sliceMeta().cursor()).startsWith("1,");
        }

        @Test
        @DisplayName("totalElements가 번들에서 그대로 전달된다")
        void toSliceResult_TotalElementsFromBundle() {
            // given
            long totalElements = 100L;
            List<ProductGroupListCompositeResult> results =
                    List.of(ProductGroupQueryFixtures.listCompositeResult(1L));
            ProductGroupListBundle bundle =
                    new ProductGroupListBundle(
                            results, totalElements, ProductGroupSortKey.SALE_PRICE);

            // when
            ProductGroupSliceResult result = sut.toSliceResult(bundle, 20);

            // then
            assertThat(result.totalElements()).isEqualTo(totalElements);
        }
    }

    @Nested
    @DisplayName("toDetailResult() - 상세 번들 → Admin DetailCompositeResult 조립")
    class ToDetailResultTest {

        @Test
        @DisplayName("상세 번들을 Admin 상세 Composite 결과로 조립한다")
        void toDetailResult_ValidBundle_ReturnsDetailCompositeResult() {
            // given
            Long productGroupId = 1L;
            ProductGroupDetailBundle bundle =
                    new ProductGroupDetailBundle(
                            ProductGroupCompositeQueryFixtures.detailCompositeQueryResult(
                                    productGroupId),
                            ProductGroupDetailImageResults.create(List.of()),
                            ProductGroupFixtures.activeProductGroup(productGroupId),
                            List.of(ProductFixtures.activeProduct(1L)),
                            List.of(),
                            List.of());

            // when
            ProductGroupDetailCompositeResult result = sut.toDetailResult(bundle);

            // then
            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(productGroupId);
            assertThat(result.sellerId()).isEqualTo(10L);
            assertThat(result.brandId()).isEqualTo(20L);
        }

        @Test
        @DisplayName("상품 목록이 비어있어도 정상적으로 조립된다")
        void toDetailResult_EmptyProducts_ReturnsDetailCompositeResult() {
            // given
            Long productGroupId = 1L;
            ProductGroupDetailBundle bundle =
                    new ProductGroupDetailBundle(
                            ProductGroupCompositeQueryFixtures.detailCompositeQueryResult(
                                    productGroupId),
                            ProductGroupDetailImageResults.create(List.of()),
                            ProductGroupFixtures.activeProductGroup(productGroupId),
                            List.of(),
                            List.of(),
                            List.of());

            // when
            ProductGroupDetailCompositeResult result = sut.toDetailResult(bundle);

            // then
            assertThat(result).isNotNull();
            assertThat(result.optionProductMatrix().products()).isEmpty();
        }
    }

    @Nested
    @DisplayName("toWebDetailResult() - 상세 번들 → Web DetailCompositeResult 조립")
    class ToWebDetailResultTest {

        @Test
        @DisplayName("상세 번들을 Web 상세 Composite 결과로 조립한다")
        void toWebDetailResult_ValidBundle_ReturnsWebDetailCompositeResult() {
            // given
            Long productGroupId = 1L;
            ProductGroupDetailBundle bundle =
                    new ProductGroupDetailBundle(
                            ProductGroupCompositeQueryFixtures.detailCompositeQueryResult(
                                    productGroupId),
                            ProductGroupDetailImageResults.create(List.of()),
                            ProductGroupFixtures.activeProductGroup(productGroupId),
                            List.of(ProductFixtures.activeProduct(1L)),
                            List.of(),
                            List.of());

            // when
            WebProductGroupDetailCompositeResult result = sut.toWebDetailResult(bundle);

            // then
            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(productGroupId);
            assertThat(result.brandIconImageUrl()).isNotNull();
        }

        @Test
        @DisplayName("Web 상세 결과에 averageRating과 reviewCount가 기본값으로 설정된다")
        void toWebDetailResult_DefaultRatingValues() {
            // given
            Long productGroupId = 1L;
            ProductGroupDetailBundle bundle =
                    new ProductGroupDetailBundle(
                            ProductGroupCompositeQueryFixtures.detailCompositeQueryResult(
                                    productGroupId),
                            ProductGroupDetailImageResults.create(List.of()),
                            ProductGroupFixtures.activeProductGroup(productGroupId),
                            List.of(),
                            List.of(),
                            List.of());

            // when
            WebProductGroupDetailCompositeResult result = sut.toWebDetailResult(bundle);

            // then
            assertThat(result.averageRating()).isEqualTo(0.0);
            assertThat(result.reviewCount()).isZero();
        }
    }
}
