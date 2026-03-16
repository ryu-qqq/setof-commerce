package com.ryuqq.setof.application.productgroup.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.product.manager.ProductReadManager;
import com.ryuqq.setof.application.productgroup.ProductGroupCompositeQueryFixtures;
import com.ryuqq.setof.application.productgroup.ProductGroupQueryFixtures;
import com.ryuqq.setof.application.productgroup.dto.composite.LegacyProductGroupDetailCompositeResult;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupDetailBundle;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupDetailCompositeQueryResult;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupDetailImageResults;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupListBundle;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupListCompositeResult;
import com.ryuqq.setof.application.productgroup.manager.ProductGroupCompositionReadManager;
import com.ryuqq.setof.application.productgroup.manager.ProductGroupReadManager;
import com.ryuqq.setof.domain.common.vo.CursorQueryContext;
import com.ryuqq.setof.domain.product.aggregate.Product;
import com.ryuqq.setof.domain.productgroup.ProductGroupFixtures;
import com.ryuqq.setof.domain.productgroup.aggregate.ProductGroup;
import com.ryuqq.setof.domain.productgroup.exception.ProductGroupNotFoundException;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.productgroup.query.ProductGroupSearchCriteria;
import com.ryuqq.setof.domain.productgroup.query.ProductGroupSortKey;
import com.setof.commerce.domain.product.ProductFixtures;
import java.util.List;
import java.util.Optional;
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
@DisplayName("ProductGroupReadFacade 단위 테스트")
class ProductGroupReadFacadeTest {

    @InjectMocks private ProductGroupReadFacade sut;

    @Mock private ProductGroupCompositionReadManager compositionReadManager;
    @Mock private ProductGroupReadManager productGroupReadManager;
    @Mock private ProductReadManager productReadManager;

    @Nested
    @DisplayName("getDetailBundle() - 레거시 상세 번들 조회")
    class GetDetailBundleTest {

        @Test
        @DisplayName("존재하는 상품그룹 ID로 조회하면 상세 결과를 반환한다")
        void getDetailBundle_ExistingId_ReturnsDetailResult() {
            // given
            Long productGroupId = 1L;
            LegacyProductGroupDetailCompositeResult expected =
                    org.mockito.Mockito.mock(LegacyProductGroupDetailCompositeResult.class);

            given(compositionReadManager.fetchProductGroupDetail(productGroupId))
                    .willReturn(Optional.of(expected));

            // when
            LegacyProductGroupDetailCompositeResult result = sut.getDetailBundle(productGroupId);

            // then
            assertThat(result).isEqualTo(expected);
            then(compositionReadManager).should().fetchProductGroupDetail(productGroupId);
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회하면 ProductGroupNotFoundException이 발생한다")
        void getDetailBundle_NonExistingId_ThrowsProductGroupNotFoundException() {
            // given
            Long productGroupId = 999L;

            given(compositionReadManager.fetchProductGroupDetail(productGroupId))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> sut.getDetailBundle(productGroupId))
                    .isInstanceOf(ProductGroupNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("getProductGroupDetailBundle() - Admin/Web 상세 번들 조회")
    class GetProductGroupDetailBundleTest {

        @Test
        @DisplayName("존재하는 ID로 조회하면 상세 번들을 반환한다")
        void getProductGroupDetailBundle_ExistingId_ReturnsDetailBundle() {
            // given
            Long productGroupId = 1L;
            ProductGroupId pgId = ProductGroupId.of(productGroupId);
            ProductGroupDetailCompositeQueryResult queryResult =
                    ProductGroupCompositeQueryFixtures.detailCompositeQueryResult(productGroupId);
            ProductGroupDetailImageResults imageResults =
                    ProductGroupDetailImageResults.create(List.of());
            ProductGroup group = ProductGroupFixtures.activeProductGroup(productGroupId);
            List<Product> products = List.of(ProductFixtures.activeProduct(1L));

            given(compositionReadManager.getDetailComposite(productGroupId))
                    .willReturn(queryResult);
            given(compositionReadManager.fetchDetailImageResults(productGroupId))
                    .willReturn(imageResults);
            given(productGroupReadManager.getById(pgId)).willReturn(group);
            given(productReadManager.findByProductGroupId(pgId)).willReturn(products);
            given(compositionReadManager.fetchNoticeEntries(queryResult.noticeId()))
                    .willReturn(List.of());
            given(compositionReadManager.fetchDescriptionImages(queryResult.descriptionId()))
                    .willReturn(List.of());

            // when
            ProductGroupDetailBundle result = sut.getProductGroupDetailBundle(productGroupId);

            // then
            assertThat(result).isNotNull();
            assertThat(result.group()).isEqualTo(group);
            assertThat(result.products()).hasSize(1);
            then(compositionReadManager).should().getDetailComposite(productGroupId);
        }

        @Test
        @DisplayName("compositionReadManager에서 예외가 발생하면 그대로 전파된다")
        void getProductGroupDetailBundle_CompositeThrowsException_PropagatesException() {
            // given
            Long productGroupId = 999L;

            given(compositionReadManager.getDetailComposite(productGroupId))
                    .willThrow(new ProductGroupNotFoundException(productGroupId));

            // when & then
            assertThatThrownBy(() -> sut.getProductGroupDetailBundle(productGroupId))
                    .isInstanceOf(ProductGroupNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("getListBundle() - 상품그룹 목록 번들 조회")
    class GetListBundleTest {

        @Test
        @DisplayName("검색 조건으로 목록과 전체 건수를 포함한 번들을 반환한다")
        void getListBundle_ValidCondition_ReturnsListBundle() {
            // given
            CursorQueryContext<ProductGroupSortKey, Long> queryContext =
                    CursorQueryContext.defaultOf(ProductGroupSortKey.CREATED_AT);
            ProductGroupSearchCriteria criteria = ProductGroupSearchCriteria.of(queryContext);

            List<ProductGroupListCompositeResult> results =
                    ProductGroupQueryFixtures.listCompositeResults();
            long totalElements = 2L;

            given(compositionReadManager.fetchThumbnailResults(criteria)).willReturn(results);
            given(compositionReadManager.fetchProductGroupCount(criteria))
                    .willReturn(totalElements);

            // when
            ProductGroupListBundle result = sut.getListBundle(criteria);

            // then
            assertThat(result.results()).hasSize(2);
            assertThat(result.totalElements()).isEqualTo(totalElements);
            then(compositionReadManager).should().fetchThumbnailResults(criteria);
            then(compositionReadManager).should().fetchProductGroupCount(criteria);
        }
    }

    @Nested
    @DisplayName("getListBundleByIds() - ID 목록 기반 번들 조회")
    class GetListBundleByIdsTest {

        @Test
        @DisplayName("ID 목록으로 목록 번들을 반환한다")
        void getListBundleByIds_ValidIds_ReturnsBundle() {
            // given
            List<Long> productGroupIds = List.of(1L, 2L);
            List<ProductGroupListCompositeResult> results =
                    ProductGroupQueryFixtures.listCompositeResults();

            given(compositionReadManager.fetchThumbnailResultsByIds(productGroupIds))
                    .willReturn(results);

            // when
            ProductGroupListBundle result = sut.getListBundleByIds(productGroupIds);

            // then
            assertThat(result.results()).hasSize(2);
            assertThat(result.totalElements()).isEqualTo(productGroupIds.size());
        }
    }

    @Nested
    @DisplayName("getListBundleByBrand() - 브랜드별 상품그룹 목록 번들 조회")
    class GetListBundleByBrandTest {

        @Test
        @DisplayName("브랜드 ID와 페이지 크기로 목록 번들을 반환한다")
        void getListBundleByBrand_ValidBrandIdAndPageSize_ReturnsBundle() {
            // given
            Long brandId = 20L;
            int pageSize = 20;
            List<ProductGroupListCompositeResult> results =
                    ProductGroupQueryFixtures.listCompositeResults();

            given(compositionReadManager.fetchThumbnailResultsByBrand(brandId, pageSize))
                    .willReturn(results);

            // when
            ProductGroupListBundle result = sut.getListBundleByBrand(brandId, pageSize);

            // then
            assertThat(result.results()).hasSize(2);
            assertThat(result.totalElements()).isEqualTo(results.size());
            then(compositionReadManager).should().fetchThumbnailResultsByBrand(brandId, pageSize);
        }

        @Test
        @DisplayName("브랜드에 속한 상품그룹이 없으면 빈 번들을 반환한다")
        void getListBundleByBrand_NoResults_ReturnsEmptyBundle() {
            // given
            Long brandId = 999L;
            int pageSize = 10;

            given(compositionReadManager.fetchThumbnailResultsByBrand(brandId, pageSize))
                    .willReturn(List.of());

            // when
            ProductGroupListBundle result = sut.getListBundleByBrand(brandId, pageSize);

            // then
            assertThat(result.results()).isEmpty();
            assertThat(result.totalElements()).isZero();
        }
    }

    @Nested
    @DisplayName("getListBundleBySeller() - 셀러별 상품그룹 목록 번들 조회")
    class GetListBundleBySellerTest {

        @Test
        @DisplayName("셀러 ID와 페이지 크기로 목록 번들을 반환한다")
        void getListBundleBySeller_ValidSellerIdAndPageSize_ReturnsBundle() {
            // given
            Long sellerId = 10L;
            int pageSize = 20;
            List<ProductGroupListCompositeResult> results =
                    ProductGroupQueryFixtures.listCompositeResults();

            given(compositionReadManager.fetchThumbnailResultsBySeller(sellerId, pageSize))
                    .willReturn(results);

            // when
            ProductGroupListBundle result = sut.getListBundleBySeller(sellerId, pageSize);

            // then
            assertThat(result.results()).hasSize(2);
            assertThat(result.totalElements()).isEqualTo(results.size());
            then(compositionReadManager).should().fetchThumbnailResultsBySeller(sellerId, pageSize);
        }

        @Test
        @DisplayName("셀러에 속한 상품그룹이 없으면 빈 번들을 반환한다")
        void getListBundleBySeller_NoResults_ReturnsEmptyBundle() {
            // given
            Long sellerId = 999L;
            int pageSize = 10;

            given(compositionReadManager.fetchThumbnailResultsBySeller(sellerId, pageSize))
                    .willReturn(List.of());

            // when
            ProductGroupListBundle result = sut.getListBundleBySeller(sellerId, pageSize);

            // then
            assertThat(result.results()).isEmpty();
            assertThat(result.totalElements()).isZero();
        }
    }

    @Nested
    @DisplayName("getSearchBundle() - 키워드 검색 번들 조회")
    class GetSearchBundleTest {

        @Test
        @DisplayName("키워드 검색 결과와 전체 건수를 포함한 번들을 반환한다")
        void getSearchBundle_ValidCondition_ReturnsSearchBundle() {
            // given
            CursorQueryContext<ProductGroupSortKey, Long> queryContext =
                    CursorQueryContext.defaultOf(ProductGroupSortKey.CREATED_AT);
            ProductGroupSearchCriteria criteria =
                    new ProductGroupSearchCriteria(
                            null,
                            null,
                            null,
                            List.of(),
                            List.of(),
                            null,
                            null,
                            "테스트",
                            null,
                            null,
                            null,
                            queryContext);

            List<ProductGroupListCompositeResult> results =
                    ProductGroupQueryFixtures.listCompositeResults();
            long totalElements = 2L;

            given(compositionReadManager.fetchSearchThumbnailResults(criteria)).willReturn(results);
            given(compositionReadManager.fetchSearchCount(criteria)).willReturn(totalElements);

            // when
            ProductGroupListBundle result = sut.getSearchBundle(criteria);

            // then
            assertThat(result.results()).hasSize(2);
            assertThat(result.totalElements()).isEqualTo(totalElements);
        }
    }
}
