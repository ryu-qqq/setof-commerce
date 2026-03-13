package com.ryuqq.setof.storage.legacy.brand.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.ryuqq.setof.domain.brand.aggregate.Brand;
import com.ryuqq.setof.domain.brand.id.BrandId;
import com.ryuqq.setof.domain.brand.query.BrandSearchCriteria;
import com.ryuqq.setof.domain.brand.query.BrandSearchField;
import com.ryuqq.setof.domain.brand.query.BrandSortKey;
import com.ryuqq.setof.domain.brand.vo.BrandIconImageUrl;
import com.ryuqq.setof.domain.brand.vo.BrandName;
import com.ryuqq.setof.domain.brand.vo.DisplayEnglishName;
import com.ryuqq.setof.domain.brand.vo.DisplayKoreanName;
import com.ryuqq.setof.domain.brand.vo.DisplayOrder;
import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.storage.legacy.brand.LegacyBrandEntityFixtures;
import com.ryuqq.setof.storage.legacy.brand.entity.LegacyBrandEntity;
import com.ryuqq.setof.storage.legacy.brand.mapper.LegacyBrandEntityMapper;
import com.ryuqq.setof.storage.legacy.brand.repository.LegacyBrandQueryDslRepository;
import java.time.Instant;
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

/**
 * LegacyBrandQueryAdapter 단위 테스트.
 *
 * <p>Mockito 기반 어댑터 레이어 검증.
 */
@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("LegacyBrandQueryAdapter 테스트")
class LegacyBrandQueryAdapterTest {

    @Mock private LegacyBrandQueryDslRepository queryDslRepository;

    @Mock private LegacyBrandEntityMapper mapper;

    @InjectMocks private LegacyBrandQueryAdapter adapter;

    @Nested
    @DisplayName("findById 메서드 테스트")
    class FindByIdTest {

        @Test
        @DisplayName("ID로 브랜드 조회 성공")
        void shouldFindByIdSuccessfully() {
            // given
            BrandId brandId = BrandId.of(1L);
            LegacyBrandEntity entity = LegacyBrandEntityFixtures.builder().id(1L).build();
            Brand expectedBrand = createMockBrand(1L, "나이키", true);

            given(queryDslRepository.findById(1L)).willReturn(Optional.of(entity));
            given(mapper.toDomain(entity)).willReturn(expectedBrand);

            // when
            Optional<Brand> result = adapter.findById(brandId);

            // then
            assertThat(result).isPresent();
            assertThat(result.get().id().value()).isEqualTo(1L);
            assertThat(result.get().brandName().value()).isEqualTo("나이키");

            then(queryDslRepository).should(times(1)).findById(1L);
            then(mapper).should(times(1)).toDomain(entity);
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회 시 빈 Optional 반환")
        void shouldReturnEmptyWhenNotFound() {
            // given
            BrandId brandId = BrandId.of(999L);
            given(queryDslRepository.findById(999L)).willReturn(Optional.empty());

            // when
            Optional<Brand> result = adapter.findById(brandId);

            // then
            assertThat(result).isEmpty();

            then(queryDslRepository).should(times(1)).findById(999L);
            then(mapper).shouldHaveNoInteractions();
        }
    }

    @Nested
    @DisplayName("findByIds 메서드 테스트")
    class FindByIdsTest {

        @Test
        @DisplayName("ID 목록으로 브랜드 목록 조회 성공")
        void shouldFindByIdsSuccessfully() {
            // given
            List<BrandId> brandIds = List.of(BrandId.of(1L), BrandId.of(2L), BrandId.of(3L));
            List<Long> idValues = List.of(1L, 2L, 3L);

            LegacyBrandEntity entity1 =
                    LegacyBrandEntityFixtures.builder().id(1L).brandName("나이키").build();
            LegacyBrandEntity entity2 =
                    LegacyBrandEntityFixtures.builder().id(2L).brandName("아디다스").build();
            LegacyBrandEntity entity3 =
                    LegacyBrandEntityFixtures.builder().id(3L).brandName("푸마").build();
            List<LegacyBrandEntity> entities = List.of(entity1, entity2, entity3);

            Brand brand1 = createMockBrand(1L, "나이키", true);
            Brand brand2 = createMockBrand(2L, "아디다스", true);
            Brand brand3 = createMockBrand(3L, "푸마", true);

            given(queryDslRepository.findByIds(idValues)).willReturn(entities);
            given(mapper.toDomain(entity1)).willReturn(brand1);
            given(mapper.toDomain(entity2)).willReturn(brand2);
            given(mapper.toDomain(entity3)).willReturn(brand3);

            // when
            List<Brand> result = adapter.findByIds(brandIds);

            // then
            assertThat(result).hasSize(3);
            assertThat(result)
                    .extracting(b -> b.brandName().value())
                    .containsExactly("나이키", "아디다스", "푸마");

            then(queryDslRepository).should(times(1)).findByIds(idValues);
            then(mapper).should(times(3)).toDomain(org.mockito.ArgumentMatchers.any());
        }

        @Test
        @DisplayName("빈 ID 목록으로 조회 시 빈 목록 반환")
        void shouldReturnEmptyListWhenIdsAreEmpty() {
            // given
            List<BrandId> emptyIds = List.of();
            given(queryDslRepository.findByIds(List.of())).willReturn(List.of());

            // when
            List<Brand> result = adapter.findByIds(emptyIds);

            // then
            assertThat(result).isEmpty();

            then(queryDslRepository).should(times(1)).findByIds(List.of());
        }
    }

    @Nested
    @DisplayName("existsById 메서드 테스트")
    class ExistsByIdTest {

        @Test
        @DisplayName("존재하는 ID일 때 true 반환")
        void shouldReturnTrueWhenExists() {
            // given
            BrandId brandId = BrandId.of(1L);
            given(queryDslRepository.existsById(1L)).willReturn(true);

            // when
            boolean result = adapter.existsById(brandId);

            // then
            assertThat(result).isTrue();

            then(queryDslRepository).should(times(1)).existsById(1L);
        }

        @Test
        @DisplayName("존재하지 않는 ID일 때 false 반환")
        void shouldReturnFalseWhenNotExists() {
            // given
            BrandId brandId = BrandId.of(999L);
            given(queryDslRepository.existsById(999L)).willReturn(false);

            // when
            boolean result = adapter.existsById(brandId);

            // then
            assertThat(result).isFalse();

            then(queryDslRepository).should(times(1)).existsById(999L);
        }
    }

    @Nested
    @DisplayName("findByCriteria 메서드 테스트")
    class FindByCriteriaTest {

        @Test
        @DisplayName("검색 조건으로 브랜드 목록 조회 성공")
        void shouldFindByCriteriaSuccessfully() {
            // given
            BrandSearchCriteria criteria =
                    BrandSearchCriteria.of(
                            true,
                            BrandSearchField.BRAND_NAME,
                            "나이키",
                            QueryContext.defaultOf(BrandSortKey.defaultKey()));

            LegacyBrandEntity entity1 =
                    LegacyBrandEntityFixtures.builder().id(1L).brandName("나이키").build();
            LegacyBrandEntity entity2 =
                    LegacyBrandEntityFixtures.builder().id(2L).brandName("나이키 조던").build();
            List<LegacyBrandEntity> entities = List.of(entity1, entity2);

            Brand brand1 = createMockBrand(1L, "나이키", true);
            Brand brand2 = createMockBrand(2L, "나이키 조던", true);

            given(queryDslRepository.findByCriteria(criteria)).willReturn(entities);
            given(mapper.toDomain(entity1)).willReturn(brand1);
            given(mapper.toDomain(entity2)).willReturn(brand2);

            // when
            List<Brand> result = adapter.findByCriteria(criteria);

            // then
            assertThat(result).hasSize(2);
            assertThat(result)
                    .extracting(b -> b.brandName().value())
                    .containsExactly("나이키", "나이키 조던");

            then(queryDslRepository).should(times(1)).findByCriteria(criteria);
            then(mapper).should(times(2)).toDomain(org.mockito.ArgumentMatchers.any());
        }

        @Test
        @DisplayName("검색 결과가 없을 때 빈 목록 반환")
        void shouldReturnEmptyListWhenNoResults() {
            // given
            BrandSearchCriteria criteria = BrandSearchCriteria.defaultOf();
            given(queryDslRepository.findByCriteria(criteria)).willReturn(List.of());

            // when
            List<Brand> result = adapter.findByCriteria(criteria);

            // then
            assertThat(result).isEmpty();

            then(queryDslRepository).should(times(1)).findByCriteria(criteria);
        }
    }

    @Nested
    @DisplayName("countByCriteria 메서드 테스트")
    class CountByCriteriaTest {

        @Test
        @DisplayName("검색 조건으로 브랜드 개수 조회 성공")
        void shouldCountByCriteriaSuccessfully() {
            // given
            BrandSearchCriteria criteria =
                    BrandSearchCriteria.of(
                            true,
                            BrandSearchField.BRAND_NAME,
                            "나이키",
                            QueryContext.defaultOf(BrandSortKey.defaultKey()));
            given(queryDslRepository.countByCriteria(criteria)).willReturn(5L);

            // when
            long result = adapter.countByCriteria(criteria);

            // then
            assertThat(result).isEqualTo(5L);

            then(queryDslRepository).should(times(1)).countByCriteria(criteria);
        }

        @Test
        @DisplayName("검색 결과가 없을 때 0 반환")
        void shouldReturnZeroWhenNoResults() {
            // given
            BrandSearchCriteria criteria = BrandSearchCriteria.defaultOf();
            given(queryDslRepository.countByCriteria(criteria)).willReturn(0L);

            // when
            long result = adapter.countByCriteria(criteria);

            // then
            assertThat(result).isEqualTo(0L);

            then(queryDslRepository).should(times(1)).countByCriteria(criteria);
        }
    }

    @Nested
    @DisplayName("findAllDisplayed 메서드 테스트")
    class FindAllDisplayedTest {

        @Test
        @DisplayName("노출 중인 브랜드 전체 조회 성공")
        void shouldFindAllDisplayedSuccessfully() {
            // given
            LegacyBrandEntity entity1 =
                    LegacyBrandEntityFixtures.builder()
                            .id(1L)
                            .brandName("나이키")
                            .displayOrder(1)
                            .build();
            LegacyBrandEntity entity2 =
                    LegacyBrandEntityFixtures.builder()
                            .id(2L)
                            .brandName("아디다스")
                            .displayOrder(2)
                            .build();
            List<LegacyBrandEntity> entities = List.of(entity1, entity2);

            Brand brand1 = createMockBrand(1L, "나이키", true);
            Brand brand2 = createMockBrand(2L, "아디다스", true);

            given(queryDslRepository.findAllDisplayed()).willReturn(entities);
            given(mapper.toDomain(entity1)).willReturn(brand1);
            given(mapper.toDomain(entity2)).willReturn(brand2);

            // when
            List<Brand> result = adapter.findAllDisplayed();

            // then
            assertThat(result).hasSize(2);
            assertThat(result)
                    .extracting(b -> b.brandName().value())
                    .containsExactly("나이키", "아디다스");

            then(queryDslRepository).should(times(1)).findAllDisplayed();
            then(mapper).should(times(2)).toDomain(org.mockito.ArgumentMatchers.any());
        }

        @Test
        @DisplayName("노출 중인 브랜드가 없을 때 빈 목록 반환")
        void shouldReturnEmptyListWhenNoDisplayedBrands() {
            // given
            given(queryDslRepository.findAllDisplayed()).willReturn(List.of());

            // when
            List<Brand> result = adapter.findAllDisplayed();

            // then
            assertThat(result).isEmpty();

            then(queryDslRepository).should(times(1)).findAllDisplayed();
        }
    }

    /** 테스트용 Mock Brand 생성 헬퍼 메서드 */
    private Brand createMockBrand(Long id, String brandName, boolean displayed) {
        return Brand.reconstitute(
                BrandId.of(id),
                BrandName.of(brandName),
                BrandIconImageUrl.of("https://example.com/icon.png"),
                DisplayKoreanName.of(brandName),
                DisplayEnglishName.of(brandName),
                DisplayOrder.of(1),
                displayed,
                null,
                Instant.now(),
                Instant.now());
    }
}
