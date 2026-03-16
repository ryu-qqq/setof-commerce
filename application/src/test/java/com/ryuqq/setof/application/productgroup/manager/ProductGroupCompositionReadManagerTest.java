package com.ryuqq.setof.application.productgroup.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.productgroup.ProductGroupCompositeQueryFixtures;
import com.ryuqq.setof.application.productgroup.ProductGroupQueryFixtures;
import com.ryuqq.setof.application.productgroup.dto.composite.LegacyProductGroupDetailCompositeResult;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupDetailCompositeQueryResult;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupDetailImageResults;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupListCompositeResult;
import com.ryuqq.setof.application.productgroup.port.out.query.LegacyProductGroupWebQueryPort;
import com.ryuqq.setof.application.productgroup.port.out.query.ProductGroupCompositeQueryPort;
import com.ryuqq.setof.application.productgroupdescription.dto.response.DescriptionImageResult;
import com.ryuqq.setof.application.productnotice.dto.response.ProductNoticeEntryResult;
import com.ryuqq.setof.domain.common.vo.CursorQueryContext;
import com.ryuqq.setof.domain.productgroup.exception.ProductGroupNotFoundException;
import com.ryuqq.setof.domain.productgroup.query.ProductGroupSearchCriteria;
import com.ryuqq.setof.domain.productgroup.query.ProductGroupSortKey;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("ProductGroupCompositionReadManager 단위 테스트")
class ProductGroupCompositionReadManagerTest {

    @InjectMocks private ProductGroupCompositionReadManager sut;

    @Mock private LegacyProductGroupWebQueryPort legacyWebQueryPort;
    @Mock private ProductGroupCompositeQueryPort compositeQueryPort;

    // ─── 레거시 Web 조회 ───

    @Nested
    @DisplayName("fetchProductGroupDetail() - 상품그룹 단건 상세 조회 (레거시)")
    class FetchProductGroupDetailTest {

        @Test
        @DisplayName("존재하는 ID로 조회하면 Optional로 반환한다")
        void fetchProductGroupDetail_ExistingId_ReturnsOptional() {
            // given
            Long productGroupId = 1L;
            LegacyProductGroupDetailCompositeResult expected =
                    Mockito.mock(LegacyProductGroupDetailCompositeResult.class);

            given(legacyWebQueryPort.fetchProductGroupDetail(productGroupId))
                    .willReturn(Optional.of(expected));

            // when
            Optional<LegacyProductGroupDetailCompositeResult> result =
                    sut.fetchProductGroupDetail(productGroupId);

            // then
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(expected);
            then(legacyWebQueryPort).should().fetchProductGroupDetail(productGroupId);
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회하면 empty Optional을 반환한다")
        void fetchProductGroupDetail_NonExistingId_ReturnsEmpty() {
            // given
            Long productGroupId = 999L;

            given(legacyWebQueryPort.fetchProductGroupDetail(productGroupId))
                    .willReturn(Optional.empty());

            // when
            Optional<LegacyProductGroupDetailCompositeResult> result =
                    sut.fetchProductGroupDetail(productGroupId);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("fetchThumbnailResults() - 상품그룹 썸네일 목록 조회")
    class FetchThumbnailResultsTest {

        @Test
        @DisplayName("검색 조건으로 썸네일 목록을 반환한다")
        void fetchThumbnailResults_ValidCriteria_ReturnsList() {
            // given
            CursorQueryContext<ProductGroupSortKey, Long> queryContext =
                    CursorQueryContext.defaultOf(ProductGroupSortKey.CREATED_AT);
            ProductGroupSearchCriteria criteria = ProductGroupSearchCriteria.of(queryContext);
            List<ProductGroupListCompositeResult> expected =
                    ProductGroupQueryFixtures.listCompositeResults();

            given(legacyWebQueryPort.fetchProductGroupThumbnails(criteria)).willReturn(expected);

            // when
            List<ProductGroupListCompositeResult> result = sut.fetchThumbnailResults(criteria);

            // then
            assertThat(result).hasSize(2);
            then(legacyWebQueryPort).should().fetchProductGroupThumbnails(criteria);
        }

        @Test
        @DisplayName("결과가 없으면 빈 목록을 반환한다")
        void fetchThumbnailResults_NoResults_ReturnsEmptyList() {
            // given
            CursorQueryContext<ProductGroupSortKey, Long> queryContext =
                    CursorQueryContext.defaultOf(ProductGroupSortKey.CREATED_AT);
            ProductGroupSearchCriteria criteria = ProductGroupSearchCriteria.of(queryContext);

            given(legacyWebQueryPort.fetchProductGroupThumbnails(criteria))
                    .willReturn(Collections.emptyList());

            // when
            List<ProductGroupListCompositeResult> result = sut.fetchThumbnailResults(criteria);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("fetchProductGroupCount() - 상품그룹 전체 건수 조회")
    class FetchProductGroupCountTest {

        @Test
        @DisplayName("검색 조건에 맞는 전체 건수를 반환한다")
        void fetchProductGroupCount_ValidCriteria_ReturnsCount() {
            // given
            CursorQueryContext<ProductGroupSortKey, Long> queryContext =
                    CursorQueryContext.defaultOf(ProductGroupSortKey.CREATED_AT);
            ProductGroupSearchCriteria criteria = ProductGroupSearchCriteria.of(queryContext);
            long expectedCount = 50L;

            given(legacyWebQueryPort.fetchProductGroupCount(criteria)).willReturn(expectedCount);

            // when
            long result = sut.fetchProductGroupCount(criteria);

            // then
            assertThat(result).isEqualTo(expectedCount);
            then(legacyWebQueryPort).should().fetchProductGroupCount(criteria);
        }
    }

    @Nested
    @DisplayName("fetchThumbnailResultsByIds() - ID 목록 기반 상품그룹 조회")
    class FetchThumbnailResultsByIdsTest {

        @Test
        @DisplayName("유효한 ID 목록으로 결과 목록을 반환한다")
        void fetchThumbnailResultsByIds_ValidIds_ReturnsList() {
            // given
            List<Long> productGroupIds = List.of(1L, 2L);
            List<ProductGroupListCompositeResult> expected =
                    ProductGroupQueryFixtures.listCompositeResults();

            given(legacyWebQueryPort.fetchProductGroupsByIds(productGroupIds)).willReturn(expected);

            // when
            List<ProductGroupListCompositeResult> result =
                    sut.fetchThumbnailResultsByIds(productGroupIds);

            // then
            assertThat(result).hasSize(2);
            then(legacyWebQueryPort).should().fetchProductGroupsByIds(productGroupIds);
        }

        @Test
        @DisplayName("빈 ID 목록이면 Port를 호출하지 않고 빈 목록을 반환한다")
        void fetchThumbnailResultsByIds_EmptyIds_ReturnsEmptyListWithoutCallingPort() {
            // given
            List<Long> emptyIds = List.of();

            // when
            List<ProductGroupListCompositeResult> result = sut.fetchThumbnailResultsByIds(emptyIds);

            // then
            assertThat(result).isEmpty();
            then(legacyWebQueryPort).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("null ID 목록이면 Port를 호출하지 않고 빈 목록을 반환한다")
        void fetchThumbnailResultsByIds_NullIds_ReturnsEmptyListWithoutCallingPort() {
            // when
            List<ProductGroupListCompositeResult> result = sut.fetchThumbnailResultsByIds(null);

            // then
            assertThat(result).isEmpty();
            then(legacyWebQueryPort).shouldHaveNoInteractions();
        }
    }

    @Nested
    @DisplayName("fetchThumbnailResultsByBrand() - 브랜드별 상품그룹 조회")
    class FetchThumbnailResultsByBrandTest {

        @Test
        @DisplayName("브랜드 ID와 페이지 크기로 결과 목록을 반환한다")
        void fetchThumbnailResultsByBrand_ValidBrandId_ReturnsList() {
            // given
            Long brandId = 20L;
            int pageSize = 20;
            List<ProductGroupListCompositeResult> expected =
                    ProductGroupQueryFixtures.listCompositeResults();

            given(legacyWebQueryPort.fetchProductGroupsByBrand(brandId, pageSize))
                    .willReturn(expected);

            // when
            List<ProductGroupListCompositeResult> result =
                    sut.fetchThumbnailResultsByBrand(brandId, pageSize);

            // then
            assertThat(result).hasSize(2);
            then(legacyWebQueryPort).should().fetchProductGroupsByBrand(brandId, pageSize);
        }
    }

    @Nested
    @DisplayName("fetchThumbnailResultsBySeller() - 셀러별 상품그룹 조회")
    class FetchThumbnailResultsBySellerTest {

        @Test
        @DisplayName("셀러 ID와 페이지 크기로 결과 목록을 반환한다")
        void fetchThumbnailResultsBySeller_ValidSellerId_ReturnsList() {
            // given
            Long sellerId = 10L;
            int pageSize = 20;
            List<ProductGroupListCompositeResult> expected =
                    ProductGroupQueryFixtures.listCompositeResults();

            given(legacyWebQueryPort.fetchProductGroupsBySeller(sellerId, pageSize))
                    .willReturn(expected);

            // when
            List<ProductGroupListCompositeResult> result =
                    sut.fetchThumbnailResultsBySeller(sellerId, pageSize);

            // then
            assertThat(result).hasSize(2);
            then(legacyWebQueryPort).should().fetchProductGroupsBySeller(sellerId, pageSize);
        }
    }

    @Nested
    @DisplayName("fetchSearchThumbnailResults() - 키워드 검색 결과 목록 조회")
    class FetchSearchThumbnailResultsTest {

        @Test
        @DisplayName("검색 조건으로 키워드 검색 결과를 반환한다")
        void fetchSearchThumbnailResults_ValidCriteria_ReturnsList() {
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
                            "나이키",
                            null,
                            null,
                            null,
                            queryContext);
            List<ProductGroupListCompositeResult> expected =
                    ProductGroupQueryFixtures.listCompositeResults();

            given(legacyWebQueryPort.fetchSearchResults(criteria)).willReturn(expected);

            // when
            List<ProductGroupListCompositeResult> result =
                    sut.fetchSearchThumbnailResults(criteria);

            // then
            assertThat(result).hasSize(2);
            then(legacyWebQueryPort).should().fetchSearchResults(criteria);
        }
    }

    @Nested
    @DisplayName("fetchSearchCount() - 키워드 검색 전체 건수 조회")
    class FetchSearchCountTest {

        @Test
        @DisplayName("키워드 검색 조건으로 전체 건수를 반환한다")
        void fetchSearchCount_ValidCriteria_ReturnsCount() {
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
                            "나이키",
                            null,
                            null,
                            null,
                            queryContext);
            long expectedCount = 10L;

            given(legacyWebQueryPort.fetchSearchCount(criteria)).willReturn(expectedCount);

            // when
            long result = sut.fetchSearchCount(criteria);

            // then
            assertThat(result).isEqualTo(expectedCount);
            then(legacyWebQueryPort).should().fetchSearchCount(criteria);
        }
    }

    // ─── 새 DB: 상세 통합 쿼리 ───

    @Nested
    @DisplayName("getDetailComposite() - 상세용 1:1 Composite 조회")
    class GetDetailCompositeTest {

        @Test
        @DisplayName("존재하는 ID로 조회하면 상세 Composite 결과를 반환한다")
        void getDetailComposite_ExistingId_ReturnsDetailComposite() {
            // given
            Long productGroupId = 1L;
            ProductGroupDetailCompositeQueryResult expected =
                    ProductGroupCompositeQueryFixtures.detailCompositeQueryResult(productGroupId);

            given(compositeQueryPort.findDetailCompositeById(productGroupId))
                    .willReturn(Optional.of(expected));

            // when
            ProductGroupDetailCompositeQueryResult result = sut.getDetailComposite(productGroupId);

            // then
            assertThat(result).isEqualTo(expected);
            then(compositeQueryPort).should().findDetailCompositeById(productGroupId);
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회하면 ProductGroupNotFoundException이 발생한다")
        void getDetailComposite_NonExistingId_ThrowsProductGroupNotFoundException() {
            // given
            Long productGroupId = 999L;

            given(compositeQueryPort.findDetailCompositeById(productGroupId))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> sut.getDetailComposite(productGroupId))
                    .isInstanceOf(ProductGroupNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("fetchDetailImageResults() - 이미지 + Variant 통합 조회")
    class FetchDetailImageResultsTest {

        @Test
        @DisplayName("상품그룹 ID로 이미지 결과를 반환한다")
        void fetchDetailImageResults_ValidId_ReturnsImageResults() {
            // given
            Long productGroupId = 1L;

            given(compositeQueryPort.findImagesWithVariantsByProductGroupId(productGroupId))
                    .willReturn(List.of());

            // when
            ProductGroupDetailImageResults result = sut.fetchDetailImageResults(productGroupId);

            // then
            assertThat(result).isNotNull();
            then(compositeQueryPort)
                    .should()
                    .findImagesWithVariantsByProductGroupId(productGroupId);
        }
    }

    @Nested
    @DisplayName("fetchNoticeEntries() - 고시정보 항목 배치 조회")
    class FetchNoticeEntriesTest {

        @Test
        @DisplayName("noticeId로 고시정보 항목 목록을 반환한다")
        void fetchNoticeEntries_ValidNoticeId_ReturnsEntries() {
            // given
            Long noticeId = 1L;
            List<ProductNoticeEntryResult> expected = List.of();

            given(compositeQueryPort.findNoticeEntriesByNoticeId(noticeId)).willReturn(expected);

            // when
            List<ProductNoticeEntryResult> result = sut.fetchNoticeEntries(noticeId);

            // then
            assertThat(result).isEmpty();
            then(compositeQueryPort).should().findNoticeEntriesByNoticeId(noticeId);
        }
    }

    @Nested
    @DisplayName("fetchDescriptionImages() - 상세설명 이미지 배치 조회")
    class FetchDescriptionImagesTest {

        @Test
        @DisplayName("descriptionId로 상세설명 이미지 목록을 반환한다")
        void fetchDescriptionImages_ValidDescriptionId_ReturnsImages() {
            // given
            Long descriptionId = 1L;
            List<DescriptionImageResult> expected = List.of();

            given(compositeQueryPort.findDescriptionImagesByDescriptionId(descriptionId))
                    .willReturn(expected);

            // when
            List<DescriptionImageResult> result = sut.fetchDescriptionImages(descriptionId);

            // then
            assertThat(result).isEmpty();
            then(compositeQueryPort).should().findDescriptionImagesByDescriptionId(descriptionId);
        }
    }
}
