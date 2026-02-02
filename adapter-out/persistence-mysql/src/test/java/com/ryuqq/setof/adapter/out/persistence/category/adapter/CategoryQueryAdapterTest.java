package com.ryuqq.setof.adapter.out.persistence.category.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.adapter.out.persistence.category.CategoryJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.category.dto.CategoryTreeDto;
import com.ryuqq.setof.adapter.out.persistence.category.entity.CategoryJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.category.mapper.CategoryJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.category.repository.CategoryQueryDslRepository;
import com.ryuqq.setof.domain.category.CategoryFixtures;
import com.ryuqq.setof.domain.category.aggregate.Category;
import com.ryuqq.setof.domain.category.id.CategoryId;
import com.ryuqq.setof.domain.category.query.CategorySearchCriteria;
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
 * CategoryQueryAdapterTest - 카테고리 Query Adapter 단위 테스트.
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
@DisplayName("CategoryQueryAdapter 단위 테스트")
class CategoryQueryAdapterTest {

    @Mock private CategoryQueryDslRepository queryDslRepository;

    @Mock private CategoryJpaEntityMapper mapper;

    @Mock private CategorySearchCriteria criteria;

    @InjectMocks private CategoryQueryAdapter queryAdapter;

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
            CategoryId categoryId = CategoryId.of(1L);
            CategoryJpaEntity entity = CategoryJpaEntityFixtures.activeEntity();
            Category domain = CategoryFixtures.activeCategory();

            given(queryDslRepository.findById(1L)).willReturn(Optional.of(entity));
            given(mapper.toDomain(entity)).willReturn(domain);

            // when
            Optional<Category> result = queryAdapter.findById(categoryId);

            // then
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(domain);
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회 시 빈 Optional을 반환합니다")
        void findById_WithNonExistingId_ReturnsEmpty() {
            // given
            CategoryId categoryId = CategoryId.of(999L);
            given(queryDslRepository.findById(999L)).willReturn(Optional.empty());

            // when
            Optional<Category> result = queryAdapter.findById(categoryId);

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
            List<CategoryId> categoryIds = List.of(CategoryId.of(1L), CategoryId.of(2L));
            CategoryJpaEntity entity1 = CategoryJpaEntityFixtures.activeEntity(1L);
            CategoryJpaEntity entity2 = CategoryJpaEntityFixtures.activeEntity(2L);
            Category domain1 = CategoryFixtures.activeCategory(1L);
            Category domain2 = CategoryFixtures.activeCategory(2L);

            given(queryDslRepository.findByIds(List.of(1L, 2L)))
                    .willReturn(List.of(entity1, entity2));
            given(mapper.toDomain(entity1)).willReturn(domain1);
            given(mapper.toDomain(entity2)).willReturn(domain2);

            // when
            List<Category> result = queryAdapter.findByIds(categoryIds);

            // then
            assertThat(result).hasSize(2);
            assertThat(result).containsExactly(domain1, domain2);
        }

        @Test
        @DisplayName("빈 ID 목록으로 조회 시 빈 리스트를 반환합니다")
        void findByIds_WithEmptyList_ReturnsEmptyList() {
            // given
            List<CategoryId> emptyList = List.of();
            given(queryDslRepository.findByIds(List.of())).willReturn(List.of());

            // when
            List<Category> result = queryAdapter.findByIds(emptyList);

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
            CategoryId categoryId = CategoryId.of(1L);
            given(queryDslRepository.existsById(1L)).willReturn(true);

            // when
            boolean result = queryAdapter.existsById(categoryId);

            // then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회 시 false를 반환합니다")
        void existsById_WithNonExistingId_ReturnsFalse() {
            // given
            CategoryId categoryId = CategoryId.of(999L);
            given(queryDslRepository.existsById(999L)).willReturn(false);

            // when
            boolean result = queryAdapter.existsById(categoryId);

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
        @DisplayName("검색 조건으로 카테고리 목록을 조회합니다")
        void findByCriteria_WithValidCriteria_ReturnsDomainList() {
            // given
            CategoryJpaEntity entity1 = CategoryJpaEntityFixtures.activeEntity(1L);
            CategoryJpaEntity entity2 = CategoryJpaEntityFixtures.activeEntity(2L);
            Category domain1 = CategoryFixtures.activeCategory(1L);
            Category domain2 = CategoryFixtures.activeCategory(2L);

            given(queryDslRepository.findByCriteria(criteria))
                    .willReturn(List.of(entity1, entity2));
            given(mapper.toDomain(entity1)).willReturn(domain1);
            given(mapper.toDomain(entity2)).willReturn(domain2);

            // when
            List<Category> result = queryAdapter.findByCriteria(criteria);

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
            List<Category> result = queryAdapter.findByCriteria(criteria);

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
        @DisplayName("검색 조건으로 카테고리 개수를 반환합니다")
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
        @DisplayName("노출 중인 카테고리 목록을 반환합니다")
        void findAllDisplayed_ReturnsDomainList() {
            // given
            CategoryJpaEntity entity1 = CategoryJpaEntityFixtures.activeEntity(1L);
            CategoryJpaEntity entity2 = CategoryJpaEntityFixtures.activeEntity(2L);
            Category domain1 = CategoryFixtures.activeCategory(1L);
            Category domain2 = CategoryFixtures.activeCategory(2L);

            given(queryDslRepository.findAllDisplayed()).willReturn(List.of(entity1, entity2));
            given(mapper.toDomain(entity1)).willReturn(domain1);
            given(mapper.toDomain(entity2)).willReturn(domain2);

            // when
            List<Category> result = queryAdapter.findAllDisplayed();

            // then
            assertThat(result).hasSize(2);
            then(queryDslRepository).should().findAllDisplayed();
        }

        @Test
        @DisplayName("노출 중인 카테고리가 없으면 빈 리스트를 반환합니다")
        void findAllDisplayed_WithNoResults_ReturnsEmptyList() {
            // given
            given(queryDslRepository.findAllDisplayed()).willReturn(List.of());

            // when
            List<Category> result = queryAdapter.findAllDisplayed();

            // then
            assertThat(result).isEmpty();
        }
    }

    // ========================================================================
    // 7. findChildrenByParentId 테스트
    // ========================================================================

    @Nested
    @DisplayName("findChildrenByParentId 메서드 테스트")
    class FindChildrenByParentIdTest {

        @Test
        @DisplayName("부모 ID로 자식 카테고리 목록을 반환합니다")
        void findChildrenByParentId_WithValidParentId_ReturnsDomainList() {
            // given
            CategoryId parentId = CategoryId.of(1L);
            CategoryJpaEntity childEntity1 = CategoryJpaEntityFixtures.childEntity(10L, 1L, 2);
            CategoryJpaEntity childEntity2 = CategoryJpaEntityFixtures.childEntity(11L, 1L, 2);
            Category childDomain1 = CategoryFixtures.activeChildCategory(10L, 1L);
            Category childDomain2 = CategoryFixtures.activeChildCategory(11L, 1L);

            given(queryDslRepository.findChildrenByParentId(1L))
                    .willReturn(List.of(childEntity1, childEntity2));
            given(mapper.toDomain(childEntity1)).willReturn(childDomain1);
            given(mapper.toDomain(childEntity2)).willReturn(childDomain2);

            // when
            List<Category> result = queryAdapter.findChildrenByParentId(parentId);

            // then
            assertThat(result).hasSize(2);
            then(queryDslRepository).should().findChildrenByParentId(1L);
        }

        @Test
        @DisplayName("자식 카테고리가 없으면 빈 리스트를 반환합니다")
        void findChildrenByParentId_WithNoChildren_ReturnsEmptyList() {
            // given
            CategoryId parentId = CategoryId.of(999L);
            given(queryDslRepository.findChildrenByParentId(999L)).willReturn(List.of());

            // when
            List<Category> result = queryAdapter.findChildrenByParentId(parentId);

            // then
            assertThat(result).isEmpty();
        }
    }

    // ========================================================================
    // 8. findParentsByChildId 테스트
    // ========================================================================

    @Nested
    @DisplayName("findParentsByChildId 메서드 테스트")
    class FindParentsByChildIdTest {

        @Test
        @DisplayName("자식 ID로 부모 카테고리 목록을 반환합니다 (Recursive CTE)")
        void findParentsByChildId_WithValidChildId_ReturnsDomainList() {
            // given
            CategoryId childId = CategoryId.of(10L);
            CategoryTreeDto parentDto1 = CategoryJpaEntityFixtures.treeDto(1L, 0L, 1);
            CategoryTreeDto parentDto2 = CategoryJpaEntityFixtures.treeDto(5L, 1L, 2);
            Category parentDomain1 = CategoryFixtures.activeCategory(1L);
            Category parentDomain2 = CategoryFixtures.activeChildCategory(5L, 1L);

            given(queryDslRepository.findAncestorsByChildId(10L))
                    .willReturn(List.of(parentDto1, parentDto2));
            given(mapper.toDomain(parentDto1)).willReturn(parentDomain1);
            given(mapper.toDomain(parentDto2)).willReturn(parentDomain2);

            // when
            List<Category> result = queryAdapter.findParentsByChildId(childId);

            // then
            assertThat(result).hasSize(2);
            then(queryDslRepository).should().findAncestorsByChildId(10L);
        }

        @Test
        @DisplayName("루트 카테고리의 경우 빈 리스트를 반환합니다")
        void findParentsByChildId_WithRootCategory_ReturnsEmptyList() {
            // given
            CategoryId rootId = CategoryId.of(1L);
            given(queryDslRepository.findAncestorsByChildId(1L)).willReturn(List.of());

            // when
            List<Category> result = queryAdapter.findParentsByChildId(rootId);

            // then
            assertThat(result).isEmpty();
        }
    }
}
