package com.ryuqq.setof.adapter.out.persistence.brand.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.adapter.out.persistence.brand.BrandJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.brand.entity.BrandJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.brand.mapper.BrandJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.brand.repository.BrandQueryDslRepository;
import com.ryuqq.setof.domain.brand.BrandFixtures;
import com.ryuqq.setof.domain.brand.aggregate.Brand;
import com.ryuqq.setof.domain.brand.id.BrandId;
import com.ryuqq.setof.domain.brand.query.BrandSearchCriteria;
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
 * BrandQueryAdapterTest - 브랜드 Query Adapter 단위 테스트.
 *
 * <p>PER-ADP-004: QueryAdapter는 QueryDslRepository만 사용.
 *
 * <p>PER-ADP-005: Entity -> Domain 변환 (Mapper 사용).
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("BrandQueryAdapter 단위 테스트")
class BrandQueryAdapterTest {

    @Mock private BrandQueryDslRepository queryDslRepository;

    @Mock private BrandJpaEntityMapper mapper;

    @Mock private BrandSearchCriteria criteria;

    @InjectMocks private BrandQueryAdapter queryAdapter;

    // ========================================================================
    // 1. findById 테스트
    // ========================================================================

    @Nested
    @DisplayName("findById 메서드 테스트")
    class FindByIdTest {

        @Test
        @DisplayName("존재하는 ID로 조회 시 Domain을 반환합니다")
        void findById_WithExistingId_ReturnsDomain() {
            // given
            BrandId brandId = BrandId.of(1L);
            BrandJpaEntity entity = BrandJpaEntityFixtures.activeEntity();
            Brand domain = BrandFixtures.activeBrand();

            given(queryDslRepository.findById(1L)).willReturn(Optional.of(entity));
            given(mapper.toDomain(entity)).willReturn(domain);

            // when
            Optional<Brand> result = queryAdapter.findById(brandId);

            // then
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(domain);
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회 시 빈 Optional을 반환합니다")
        void findById_WithNonExistingId_ReturnsEmpty() {
            // given
            BrandId brandId = BrandId.of(999L);
            given(queryDslRepository.findById(999L)).willReturn(Optional.empty());

            // when
            Optional<Brand> result = queryAdapter.findById(brandId);

            // then
            assertThat(result).isEmpty();
        }
    }

    // ========================================================================
    // 2. findByIds 테스트
    // ========================================================================

    @Nested
    @DisplayName("findByIds 메서드 테스트")
    class FindByIdsTest {

        @Test
        @DisplayName("ID 목록으로 조회 시 Domain 목록을 반환합니다")
        void findByIds_WithValidIds_ReturnsDomainList() {
            // given
            List<BrandId> brandIds = List.of(BrandId.of(1L), BrandId.of(2L));
            BrandJpaEntity entity1 = BrandJpaEntityFixtures.activeEntity(1L);
            BrandJpaEntity entity2 = BrandJpaEntityFixtures.activeEntity(2L);
            Brand domain1 = BrandFixtures.activeBrand(1L);
            Brand domain2 = BrandFixtures.activeBrand(2L);

            given(queryDslRepository.findByIds(List.of(1L, 2L)))
                    .willReturn(List.of(entity1, entity2));
            given(mapper.toDomain(entity1)).willReturn(domain1);
            given(mapper.toDomain(entity2)).willReturn(domain2);

            // when
            List<Brand> result = queryAdapter.findByIds(brandIds);

            // then
            assertThat(result).hasSize(2);
            assertThat(result).containsExactly(domain1, domain2);
        }

        @Test
        @DisplayName("빈 ID 목록으로 조회 시 빈 리스트를 반환합니다")
        void findByIds_WithEmptyList_ReturnsEmptyList() {
            // given
            List<BrandId> emptyList = List.of();
            given(queryDslRepository.findByIds(List.of())).willReturn(List.of());

            // when
            List<Brand> result = queryAdapter.findByIds(emptyList);

            // then
            assertThat(result).isEmpty();
        }
    }

    // ========================================================================
    // 3. existsById 테스트
    // ========================================================================

    @Nested
    @DisplayName("existsById 메서드 테스트")
    class ExistsByIdTest {

        @Test
        @DisplayName("존재하는 ID로 조회 시 true를 반환합니다")
        void existsById_WithExistingId_ReturnsTrue() {
            // given
            BrandId brandId = BrandId.of(1L);
            given(queryDslRepository.existsById(1L)).willReturn(true);

            // when
            boolean result = queryAdapter.existsById(brandId);

            // then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회 시 false를 반환합니다")
        void existsById_WithNonExistingId_ReturnsFalse() {
            // given
            BrandId brandId = BrandId.of(999L);
            given(queryDslRepository.existsById(999L)).willReturn(false);

            // when
            boolean result = queryAdapter.existsById(brandId);

            // then
            assertThat(result).isFalse();
        }
    }

    // ========================================================================
    // 4. findByCriteria 테스트
    // ========================================================================

    @Nested
    @DisplayName("findByCriteria 메서드 테스트")
    class FindByCriteriaTest {

        @Test
        @DisplayName("검색 조건으로 브랜드 목록을 조회합니다")
        void findByCriteria_WithValidCriteria_ReturnsDomainList() {
            // given
            BrandJpaEntity entity1 = BrandJpaEntityFixtures.activeEntity(1L);
            BrandJpaEntity entity2 = BrandJpaEntityFixtures.activeEntity(2L);
            Brand domain1 = BrandFixtures.activeBrand(1L);
            Brand domain2 = BrandFixtures.activeBrand(2L);

            given(queryDslRepository.findByCriteria(criteria))
                    .willReturn(List.of(entity1, entity2));
            given(mapper.toDomain(entity1)).willReturn(domain1);
            given(mapper.toDomain(entity2)).willReturn(domain2);

            // when
            List<Brand> result = queryAdapter.findByCriteria(criteria);

            // then
            assertThat(result).hasSize(2);
            then(queryDslRepository).should().findByCriteria(criteria);
        }

        @Test
        @DisplayName("검색 결과가 없으면 빈 리스트를 반환합니다")
        void findByCriteria_WithNoResults_ReturnsEmptyList() {
            // given
            given(queryDslRepository.findByCriteria(criteria)).willReturn(List.of());

            // when
            List<Brand> result = queryAdapter.findByCriteria(criteria);

            // then
            assertThat(result).isEmpty();
        }
    }

    // ========================================================================
    // 5. countByCriteria 테스트
    // ========================================================================

    @Nested
    @DisplayName("countByCriteria 메서드 테스트")
    class CountByCriteriaTest {

        @Test
        @DisplayName("검색 조건으로 브랜드 개수를 반환합니다")
        void countByCriteria_WithValidCriteria_ReturnsCount() {
            // given
            given(queryDslRepository.countByCriteria(criteria)).willReturn(5L);

            // when
            long result = queryAdapter.countByCriteria(criteria);

            // then
            assertThat(result).isEqualTo(5L);
        }

        @Test
        @DisplayName("검색 결과가 없으면 0을 반환합니다")
        void countByCriteria_WithNoResults_ReturnsZero() {
            // given
            given(queryDslRepository.countByCriteria(criteria)).willReturn(0L);

            // when
            long result = queryAdapter.countByCriteria(criteria);

            // then
            assertThat(result).isZero();
        }
    }

    // ========================================================================
    // 6. findAllDisplayed 테스트
    // ========================================================================

    @Nested
    @DisplayName("findAllDisplayed 메서드 테스트")
    class FindAllDisplayedTest {

        @Test
        @DisplayName("노출 중인 브랜드 목록을 반환합니다")
        void findAllDisplayed_ReturnsDomainList() {
            // given
            BrandJpaEntity entity1 = BrandJpaEntityFixtures.activeEntity(1L);
            BrandJpaEntity entity2 = BrandJpaEntityFixtures.activeEntity(2L);
            Brand domain1 = BrandFixtures.activeBrand(1L);
            Brand domain2 = BrandFixtures.activeBrand(2L);

            given(queryDslRepository.findAllDisplayed()).willReturn(List.of(entity1, entity2));
            given(mapper.toDomain(entity1)).willReturn(domain1);
            given(mapper.toDomain(entity2)).willReturn(domain2);

            // when
            List<Brand> result = queryAdapter.findAllDisplayed();

            // then
            assertThat(result).hasSize(2);
            then(queryDslRepository).should().findAllDisplayed();
        }

        @Test
        @DisplayName("노출 중인 브랜드가 없으면 빈 리스트를 반환합니다")
        void findAllDisplayed_WithNoResults_ReturnsEmptyList() {
            // given
            given(queryDslRepository.findAllDisplayed()).willReturn(List.of());

            // when
            List<Brand> result = queryAdapter.findAllDisplayed();

            // then
            assertThat(result).isEmpty();
        }
    }
}
