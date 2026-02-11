package com.ryuqq.setof.storage.legacy.category.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.domain.category.aggregate.Category;
import com.ryuqq.setof.domain.category.id.CategoryId;
import com.ryuqq.setof.domain.category.query.CategorySearchCriteria;
import com.ryuqq.setof.domain.category.vo.CategoryType;
import com.ryuqq.setof.domain.category.vo.TargetGroup;
import com.ryuqq.setof.storage.legacy.category.LegacyCategoryEntityFixtures;
import com.ryuqq.setof.storage.legacy.category.dto.LegacyCategoryTreeDto;
import com.ryuqq.setof.storage.legacy.category.entity.LegacyCategoryEntity;
import com.ryuqq.setof.storage.legacy.category.mapper.LegacyCategoryEntityMapper;
import com.ryuqq.setof.storage.legacy.category.repository.LegacyCategoryQueryDslRepository;
import com.ryuqq.setof.storage.legacy.common.Yn;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * LegacyCategoryQueryAdapter 단위 테스트.
 *
 * <p>레거시 카테고리 조회 어댑터의 동작을 검증합니다.
 */
@DisplayName("레거시 카테고리 Query Adapter 테스트")
@Tag("unit")
@ExtendWith(MockitoExtension.class)
class LegacyCategoryQueryAdapterTest {

    @Mock private LegacyCategoryQueryDslRepository queryDslRepository;

    private LegacyCategoryEntityMapper mapper;
    private LegacyCategoryQueryAdapter adapter;

    @BeforeEach
    void setUp() {
        mapper = new LegacyCategoryEntityMapper();
        adapter = new LegacyCategoryQueryAdapter(queryDslRepository, mapper);
    }

    @Nested
    @DisplayName("findById 메서드 테스트")
    class FindByIdTest {

        @Test
        @DisplayName("ID로 카테고리 조회 성공")
        void shouldFindCategoryById() {
            // given
            CategoryId categoryId = CategoryId.of(100L);
            LegacyCategoryEntity entity =
                    LegacyCategoryEntityFixtures.builder().id(100L).categoryName("상의").build();

            given(queryDslRepository.findById(100L)).willReturn(Optional.of(entity));

            // when
            Optional<Category> result = adapter.findById(categoryId);

            // then
            assertThat(result).isPresent();
            assertThat(result.get().id().value()).isEqualTo(100L);
            assertThat(result.get().name().value()).isEqualTo("상의");

            then(queryDslRepository).should().findById(100L);
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회시 빈 Optional 반환")
        void shouldReturnEmptyWhenCategoryNotFound() {
            // given
            CategoryId categoryId = CategoryId.of(999L);

            given(queryDslRepository.findById(999L)).willReturn(Optional.empty());

            // when
            Optional<Category> result = adapter.findById(categoryId);

            // then
            assertThat(result).isEmpty();

            then(queryDslRepository).should().findById(999L);
        }
    }

    @Nested
    @DisplayName("findByIds 메서드 테스트")
    class FindByIdsTest {

        @Test
        @DisplayName("ID 목록으로 카테고리 목록 조회 성공")
        void shouldFindCategoriesByIds() {
            // given
            List<CategoryId> categoryIds = List.of(CategoryId.of(100L), CategoryId.of(200L));

            LegacyCategoryEntity entity1 =
                    LegacyCategoryEntityFixtures.builder().id(100L).categoryName("상의").build();
            LegacyCategoryEntity entity2 =
                    LegacyCategoryEntityFixtures.builder().id(200L).categoryName("하의").build();

            given(queryDslRepository.findByIds(List.of(100L, 200L)))
                    .willReturn(List.of(entity1, entity2));

            // when
            List<Category> result = adapter.findByIds(categoryIds);

            // then
            assertThat(result).hasSize(2);
            assertThat(result).extracting(category -> category.id().value()).contains(100L, 200L);
            assertThat(result).extracting(category -> category.name().value()).contains("상의", "하의");

            then(queryDslRepository).should().findByIds(List.of(100L, 200L));
        }

        @Test
        @DisplayName("빈 ID 목록으로 조회시 빈 목록 반환")
        void shouldReturnEmptyListWhenIdsAreEmpty() {
            // given
            List<CategoryId> categoryIds = List.of();

            given(queryDslRepository.findByIds(List.of())).willReturn(List.of());

            // when
            List<Category> result = adapter.findByIds(categoryIds);

            // then
            assertThat(result).isEmpty();

            then(queryDslRepository).should().findByIds(List.of());
        }
    }

    @Nested
    @DisplayName("existsById 메서드 테스트")
    class ExistsByIdTest {

        @Test
        @DisplayName("존재하는 카테고리 ID로 조회시 true 반환")
        void shouldReturnTrueWhenCategoryExists() {
            // given
            CategoryId categoryId = CategoryId.of(100L);

            given(queryDslRepository.existsById(100L)).willReturn(true);

            // when
            boolean result = adapter.existsById(categoryId);

            // then
            assertThat(result).isTrue();

            then(queryDslRepository).should().existsById(100L);
        }

        @Test
        @DisplayName("존재하지 않는 카테고리 ID로 조회시 false 반환")
        void shouldReturnFalseWhenCategoryNotExists() {
            // given
            CategoryId categoryId = CategoryId.of(999L);

            given(queryDslRepository.existsById(999L)).willReturn(false);

            // when
            boolean result = adapter.existsById(categoryId);

            // then
            assertThat(result).isFalse();

            then(queryDslRepository).should().existsById(999L);
        }
    }

    @Nested
    @DisplayName("findByCriteria 메서드 테스트")
    class FindByCriteriaTest {

        @Test
        @DisplayName("검색 조건으로 카테고리 목록 조회 성공")
        void shouldFindCategoriesByCriteria() {
            // given
            CategorySearchCriteria criteria = CategorySearchCriteria.defaultOf();

            LegacyCategoryEntity entity1 =
                    LegacyCategoryEntityFixtures.builder().id(100L).categoryName("상의").build();
            LegacyCategoryEntity entity2 =
                    LegacyCategoryEntityFixtures.builder().id(200L).categoryName("하의").build();

            given(queryDslRepository.findByCriteria(criteria))
                    .willReturn(List.of(entity1, entity2));

            // when
            List<Category> result = adapter.findByCriteria(criteria);

            // then
            assertThat(result).hasSize(2);
            assertThat(result).extracting(category -> category.name().value()).contains("상의", "하의");

            then(queryDslRepository).should().findByCriteria(criteria);
        }

        @Test
        @DisplayName("검색 결과가 없으면 빈 목록 반환")
        void shouldReturnEmptyListWhenNoCategoriesFound() {
            // given
            CategorySearchCriteria criteria = CategorySearchCriteria.defaultOf();

            given(queryDslRepository.findByCriteria(criteria)).willReturn(List.of());

            // when
            List<Category> result = adapter.findByCriteria(criteria);

            // then
            assertThat(result).isEmpty();

            then(queryDslRepository).should().findByCriteria(criteria);
        }
    }

    @Nested
    @DisplayName("countByCriteria 메서드 테스트")
    class CountByCriteriaTest {

        @Test
        @DisplayName("검색 조건으로 카테고리 개수 조회 성공")
        void shouldCountCategoriesByCriteria() {
            // given
            CategorySearchCriteria criteria = CategorySearchCriteria.defaultOf();

            given(queryDslRepository.countByCriteria(criteria)).willReturn(10L);

            // when
            long result = adapter.countByCriteria(criteria);

            // then
            assertThat(result).isEqualTo(10L);

            then(queryDslRepository).should().countByCriteria(criteria);
        }

        @Test
        @DisplayName("검색 결과가 없으면 0 반환")
        void shouldReturnZeroWhenNoCategoriesFound() {
            // given
            CategorySearchCriteria criteria = CategorySearchCriteria.defaultOf();

            given(queryDslRepository.countByCriteria(criteria)).willReturn(0L);

            // when
            long result = adapter.countByCriteria(criteria);

            // then
            assertThat(result).isEqualTo(0L);

            then(queryDslRepository).should().countByCriteria(criteria);
        }
    }

    @Nested
    @DisplayName("findAllDisplayed 메서드 테스트")
    class FindAllDisplayedTest {

        @Test
        @DisplayName("노출 중인 카테고리 전체 조회 성공")
        void shouldFindAllDisplayedCategories() {
            // given
            LegacyCategoryEntity entity1 =
                    LegacyCategoryEntityFixtures.builder()
                            .id(100L)
                            .categoryName("상의")
                            .displayYn(Yn.Y)
                            .build();
            LegacyCategoryEntity entity2 =
                    LegacyCategoryEntityFixtures.builder()
                            .id(200L)
                            .categoryName("하의")
                            .displayYn(Yn.Y)
                            .build();

            given(queryDslRepository.findAllDisplayed()).willReturn(List.of(entity1, entity2));

            // when
            List<Category> result = adapter.findAllDisplayed();

            // then
            assertThat(result).hasSize(2);
            assertThat(result).allMatch(Category::displayed);

            then(queryDslRepository).should().findAllDisplayed();
        }

        @Test
        @DisplayName("노출 중인 카테고리가 없으면 빈 목록 반환")
        void shouldReturnEmptyListWhenNoDisplayedCategories() {
            // given
            given(queryDslRepository.findAllDisplayed()).willReturn(List.of());

            // when
            List<Category> result = adapter.findAllDisplayed();

            // then
            assertThat(result).isEmpty();

            then(queryDslRepository).should().findAllDisplayed();
        }
    }

    @Nested
    @DisplayName("findChildrenByParentId 메서드 테스트")
    class FindChildrenByParentIdTest {

        @Test
        @DisplayName("부모 ID로 자식 카테고리 조회 성공")
        void shouldFindChildrenByParentId() {
            // given
            CategoryId parentId = CategoryId.of(100L);

            LegacyCategoryEntity child1 =
                    LegacyCategoryEntityFixtures.builder()
                            .id(101L)
                            .categoryName("티셔츠")
                            .parentCategoryId(100L)
                            .build();
            LegacyCategoryEntity child2 =
                    LegacyCategoryEntityFixtures.builder()
                            .id(102L)
                            .categoryName("셔츠")
                            .parentCategoryId(100L)
                            .build();

            given(queryDslRepository.findChildrenByParentId(100L))
                    .willReturn(List.of(child1, child2));

            // when
            List<Category> result = adapter.findChildrenByParentId(parentId);

            // then
            assertThat(result).hasSize(2);
            assertThat(result)
                    .extracting(category -> category.name().value())
                    .contains("티셔츠", "셔츠");

            then(queryDslRepository).should().findChildrenByParentId(100L);
        }

        @Test
        @DisplayName("자식 카테고리가 없으면 빈 목록 반환")
        void shouldReturnEmptyListWhenNoChildren() {
            // given
            CategoryId parentId = CategoryId.of(100L);

            given(queryDslRepository.findChildrenByParentId(100L)).willReturn(List.of());

            // when
            List<Category> result = adapter.findChildrenByParentId(parentId);

            // then
            assertThat(result).isEmpty();

            then(queryDslRepository).should().findChildrenByParentId(100L);
        }
    }

    @Nested
    @DisplayName("findParentsByChildId 메서드 테스트")
    class FindParentsByChildIdTest {

        @Test
        @DisplayName("자식 ID로 부모 카테고리 목록 조회 성공 (TreeDto 변환)")
        void shouldFindParentsByChildId() {
            // given
            CategoryId childId = CategoryId.of(300L);

            LegacyCategoryTreeDto dto1 =
                    new LegacyCategoryTreeDto(
                            100L,
                            "루트",
                            1,
                            0L,
                            "루트",
                            Yn.Y,
                            TargetGroup.MALE,
                            CategoryType.CLOTHING,
                            "/100",
                            LocalDateTime.now(),
                            LocalDateTime.now());

            LegacyCategoryTreeDto dto2 =
                    new LegacyCategoryTreeDto(
                            200L,
                            "부모",
                            2,
                            100L,
                            "부모",
                            Yn.Y,
                            TargetGroup.MALE,
                            CategoryType.CLOTHING,
                            "/100/200",
                            LocalDateTime.now(),
                            LocalDateTime.now());

            LegacyCategoryTreeDto dto3 =
                    new LegacyCategoryTreeDto(
                            300L,
                            "자식",
                            3,
                            200L,
                            "자식",
                            Yn.Y,
                            TargetGroup.MALE,
                            CategoryType.CLOTHING,
                            "/100/200/300",
                            LocalDateTime.now(),
                            LocalDateTime.now());

            given(queryDslRepository.findParentsByChildId(300L))
                    .willReturn(List.of(dto1, dto2, dto3));

            // when
            List<Category> result = adapter.findParentsByChildId(childId);

            // then
            assertThat(result).hasSize(3);
            assertThat(result)
                    .extracting(category -> category.name().value())
                    .contains("루트", "부모", "자식");

            then(queryDslRepository).should().findParentsByChildId(300L);
        }

        @Test
        @DisplayName("부모 카테고리가 없으면 빈 목록 반환")
        void shouldReturnEmptyListWhenNoParents() {
            // given
            CategoryId childId = CategoryId.of(100L);

            given(queryDslRepository.findParentsByChildId(100L)).willReturn(List.of());

            // when
            List<Category> result = adapter.findParentsByChildId(childId);

            // then
            assertThat(result).isEmpty();

            then(queryDslRepository).should().findParentsByChildId(100L);
        }
    }
}
