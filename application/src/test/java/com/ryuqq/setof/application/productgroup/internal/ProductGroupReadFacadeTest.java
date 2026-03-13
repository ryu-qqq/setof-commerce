package com.ryuqq.setof.application.productgroup.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.product.port.out.query.ProductQueryPort;
import com.ryuqq.setof.application.productdescription.port.out.query.ProductGroupDescriptionQueryPort;
import com.ryuqq.setof.application.productgroup.ProductGroupCompositeQueryFixtures;
import com.ryuqq.setof.application.productgroup.ProductGroupQueryFixtures;
import com.ryuqq.setof.application.productgroup.dto.composite.LegacyProductGroupDetailCompositeResult;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupDetailBundle;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupListBundle;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupThumbnailCompositeResult;
import com.ryuqq.setof.application.productgroup.manager.ProductGroupCompositionReadManager;
import com.ryuqq.setof.application.productgroup.port.out.query.ProductGroupCompositeQueryPort;
import com.ryuqq.setof.application.productgroup.port.out.query.ProductGroupQueryPort;
import com.ryuqq.setof.application.productnotice.port.out.query.ProductNoticeQueryPort;
import com.ryuqq.setof.domain.legacy.product.dto.query.LegacyProductGroupSearchCondition;
import com.ryuqq.setof.domain.legacy.search.dto.query.LegacySearchCondition;
import com.ryuqq.setof.domain.product.aggregate.Product;
import com.ryuqq.setof.domain.productgroup.ProductGroupFixtures;
import com.ryuqq.setof.domain.productgroup.aggregate.ProductGroup;
import com.ryuqq.setof.domain.productgroup.exception.ProductGroupNotFoundException;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
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
    @Mock private ProductGroupCompositeQueryPort compositeQueryPort;
    @Mock private ProductGroupQueryPort productGroupQueryPort;
    @Mock private ProductQueryPort productQueryPort;
    @Mock private ProductGroupDescriptionQueryPort descriptionQueryPort;
    @Mock private ProductNoticeQueryPort noticeQueryPort;

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
            ProductGroup group = ProductGroupFixtures.activeProductGroup(productGroupId);
            List<Product> products = List.of(ProductFixtures.activeProduct(1L));

            given(compositeQueryPort.findDetailCompositeById(productGroupId))
                    .willReturn(
                            Optional.of(
                                    ProductGroupCompositeQueryFixtures.detailCompositeQueryResult(
                                            productGroupId)));
            given(productGroupQueryPort.findById(pgId)).willReturn(Optional.of(group));
            given(productQueryPort.findByProductGroupId(pgId)).willReturn(products);
            given(descriptionQueryPort.findByProductGroupId(pgId)).willReturn(Optional.empty());
            given(noticeQueryPort.findByProductGroupId(pgId)).willReturn(Optional.empty());

            // when
            ProductGroupDetailBundle result = sut.getProductGroupDetailBundle(productGroupId);

            // then
            assertThat(result).isNotNull();
            assertThat(result.group()).isEqualTo(group);
            assertThat(result.products()).hasSize(1);
        }

        @Test
        @DisplayName("compositeQueryPort에서 결과가 없으면 ProductGroupNotFoundException이 발생한다")
        void getProductGroupDetailBundle_CompositeNotFound_ThrowsException() {
            // given
            Long productGroupId = 999L;

            given(compositeQueryPort.findDetailCompositeById(productGroupId))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> sut.getProductGroupDetailBundle(productGroupId))
                    .isInstanceOf(ProductGroupNotFoundException.class);
        }

        @Test
        @DisplayName("ProductGroup Aggregate가 없으면 ProductGroupNotFoundException이 발생한다")
        void getProductGroupDetailBundle_GroupNotFound_ThrowsException() {
            // given
            Long productGroupId = 1L;
            ProductGroupId pgId = ProductGroupId.of(productGroupId);

            given(compositeQueryPort.findDetailCompositeById(productGroupId))
                    .willReturn(
                            Optional.of(
                                    ProductGroupCompositeQueryFixtures.detailCompositeQueryResult(
                                            productGroupId)));
            given(productGroupQueryPort.findById(pgId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> sut.getProductGroupDetailBundle(productGroupId))
                    .isInstanceOf(ProductGroupNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("getListBundle() - 상품그룹 목록 번들 조회")
    class GetListBundleTest {

        @Test
        @DisplayName("검색 조건으로 썸네일 목록과 전체 건수를 포함한 번들을 반환한다")
        void getListBundle_ValidCondition_ReturnsListBundle() {
            // given
            LegacyProductGroupSearchCondition condition =
                    org.mockito.Mockito.mock(LegacyProductGroupSearchCondition.class);
            List<ProductGroupThumbnailCompositeResult> thumbnails =
                    ProductGroupQueryFixtures.thumbnailCompositeResults();
            long totalElements = 2L;

            given(compositionReadManager.fetchProductGroupThumbnails(condition))
                    .willReturn(thumbnails);
            given(compositionReadManager.fetchProductGroupCount(condition))
                    .willReturn(totalElements);
            given(condition.orderType()).willReturn("RECOMMEND");

            // when
            ProductGroupListBundle result = sut.getListBundle(condition);

            // then
            assertThat(result.thumbnails()).hasSize(2);
            assertThat(result.totalElements()).isEqualTo(totalElements);
            then(compositionReadManager).should().fetchProductGroupThumbnails(condition);
            then(compositionReadManager).should().fetchProductGroupCount(condition);
        }
    }

    @Nested
    @DisplayName("getListBundleByIds() - ID 목록 기반 번들 조회")
    class GetListBundleByIdsTest {

        @Test
        @DisplayName("ID 목록으로 썸네일 번들을 반환한다")
        void getListBundleByIds_ValidIds_ReturnsBundle() {
            // given
            List<Long> productGroupIds = List.of(1L, 2L);
            List<ProductGroupThumbnailCompositeResult> thumbnails =
                    ProductGroupQueryFixtures.thumbnailCompositeResults();

            given(compositionReadManager.fetchProductGroupsByIds(productGroupIds))
                    .willReturn(thumbnails);

            // when
            ProductGroupListBundle result = sut.getListBundleByIds(productGroupIds);

            // then
            assertThat(result.thumbnails()).hasSize(2);
            assertThat(result.totalElements()).isEqualTo(productGroupIds.size());
        }
    }

    @Nested
    @DisplayName("getSearchBundle() - 키워드 검색 번들 조회")
    class GetSearchBundleTest {

        @Test
        @DisplayName("키워드 검색 결과와 전체 건수를 포함한 번들을 반환한다")
        void getSearchBundle_ValidCondition_ReturnsSearchBundle() {
            // given
            LegacySearchCondition condition = org.mockito.Mockito.mock(LegacySearchCondition.class);
            List<ProductGroupThumbnailCompositeResult> thumbnails =
                    ProductGroupQueryFixtures.thumbnailCompositeResults();
            long totalElements = 2L;

            given(compositionReadManager.fetchSearchResults(condition)).willReturn(thumbnails);
            given(compositionReadManager.fetchSearchCount(condition)).willReturn(totalElements);
            given(condition.orderType()).willReturn("RECOMMEND");

            // when
            ProductGroupListBundle result = sut.getSearchBundle(condition);

            // then
            assertThat(result.thumbnails()).hasSize(2);
            assertThat(result.totalElements()).isEqualTo(totalElements);
        }
    }
}
