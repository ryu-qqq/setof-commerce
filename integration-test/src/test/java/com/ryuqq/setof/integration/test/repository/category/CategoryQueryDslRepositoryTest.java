package com.ryuqq.setof.integration.test.repository.category;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.category.CategoryJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.category.entity.CategoryJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.category.repository.CategoryJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.category.repository.CategoryQueryDslRepository;
import com.ryuqq.setof.domain.category.id.CategoryId;
import com.ryuqq.setof.domain.category.query.CategorySearchCriteria;
import com.ryuqq.setof.domain.category.query.CategorySortKey;
import com.ryuqq.setof.domain.category.vo.CategoryType;
import com.ryuqq.setof.domain.category.vo.TargetGroup;
import com.ryuqq.setof.domain.common.vo.PageRequest;
import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import com.ryuqq.setof.integration.test.common.base.RepositoryTestBase;
import com.ryuqq.setof.integration.test.common.tag.TestTags;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Category QueryDSL Repository 통합 테스트.
 *
 * <p>QueryDSL 기반의 복잡한 쿼리 동작을 검증합니다.
 *
 * <ul>
 *   <li>Soft Delete 필터링 (deletedAt IS NULL)
 *   <li>동적 검색 조건 (ConditionBuilder)
 *   <li>정렬 및 페이징
 *   <li>부모/자식 카테고리 조회
 * </ul>
 */
@Tag(TestTags.CATEGORY)
@DisplayName("카테고리 QueryDSL Repository 테스트")
class CategoryQueryDslRepositoryTest extends RepositoryTestBase {

    @Autowired private CategoryJpaRepository jpaRepository;
    @Autowired private CategoryQueryDslRepository queryDslRepository;

    @Nested
    @DisplayName("findById 테스트")
    class FindByIdTest {

        @Test
        @DisplayName("활성 상태 카테고리 조회 성공")
        void shouldFindActiveEntity() {
            // given
            CategoryJpaEntity entity = jpaRepository.save(CategoryJpaEntityFixtures.newEntity());
            flushAndClear();

            // when
            Optional<CategoryJpaEntity> found = queryDslRepository.findById(entity.getId());

            // then
            assertThat(found).isPresent();
            assertThat(found.get().getCategoryName())
                    .isEqualTo(CategoryJpaEntityFixtures.DEFAULT_CATEGORY_NAME);
        }

        @Test
        @DisplayName("삭제된 카테고리는 조회되지 않음 (Soft Delete 필터)")
        void shouldNotFindDeletedEntity() {
            // given
            CategoryJpaEntity entity = jpaRepository.save(createDeletedEntity());
            flushAndClear();

            // when
            Optional<CategoryJpaEntity> found = queryDslRepository.findById(entity.getId());

            // then
            assertThat(found).isEmpty();
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회시 빈 Optional 반환")
        void shouldReturnEmptyWhenNotFound() {
            // when
            Optional<CategoryJpaEntity> found = queryDslRepository.findById(999999L);

            // then
            assertThat(found).isEmpty();
        }
    }

    @Nested
    @DisplayName("findByIds 테스트")
    class FindByIdsTest {

        @Test
        @DisplayName("ID 목록으로 카테고리 목록 조회 성공")
        void shouldFindByIds() {
            // given
            CategoryJpaEntity entity1 =
                    jpaRepository.save(createEntityWithName("카테고리A", "카테고리A 표시명"));
            CategoryJpaEntity entity2 =
                    jpaRepository.save(createEntityWithName("카테고리B", "카테고리B 표시명"));
            CategoryJpaEntity entity3 =
                    jpaRepository.save(createEntityWithName("카테고리C", "카테고리C 표시명"));
            flushAndClear();

            // when
            List<CategoryJpaEntity> found =
                    queryDslRepository.findByIds(List.of(entity1.getId(), entity3.getId()));

            // then
            assertThat(found).hasSize(2);
            assertThat(found)
                    .extracting(CategoryJpaEntity::getCategoryName)
                    .containsExactlyInAnyOrder("카테고리A", "카테고리C");
        }

        @Test
        @DisplayName("삭제된 카테고리는 ID 목록 조회에서 제외됨")
        void shouldExcludeDeletedEntities() {
            // given
            CategoryJpaEntity activeEntity =
                    jpaRepository.save(createEntityWithName("활성카테고리", "활성 표시명"));
            CategoryJpaEntity deletedEntity = jpaRepository.save(createDeletedEntity());
            flushAndClear();

            // when
            List<CategoryJpaEntity> found =
                    queryDslRepository.findByIds(
                            List.of(activeEntity.getId(), deletedEntity.getId()));

            // then
            assertThat(found).hasSize(1);
            assertThat(found.get(0).getCategoryName()).isEqualTo("활성카테고리");
        }

        @Test
        @DisplayName("빈 ID 목록으로 조회시 빈 목록 반환")
        void shouldReturnEmptyListWhenEmptyIds() {
            // when
            List<CategoryJpaEntity> found = queryDslRepository.findByIds(List.of());

            // then
            assertThat(found).isEmpty();
        }
    }

    @Nested
    @DisplayName("existsById 테스트")
    class ExistsByIdTest {

        @Test
        @DisplayName("활성 카테고리 존재 확인 - true")
        void shouldReturnTrueForActiveEntity() {
            // given
            CategoryJpaEntity entity = jpaRepository.save(CategoryJpaEntityFixtures.newEntity());
            flushAndClear();

            // when
            boolean exists = queryDslRepository.existsById(entity.getId());

            // then
            assertThat(exists).isTrue();
        }

        @Test
        @DisplayName("삭제된 카테고리는 존재하지 않음으로 판단 (Soft Delete)")
        void shouldReturnFalseForDeletedEntity() {
            // given
            CategoryJpaEntity entity = jpaRepository.save(createDeletedEntity());
            flushAndClear();

            // when
            boolean exists = queryDslRepository.existsById(entity.getId());

            // then
            assertThat(exists).isFalse();
        }

        @Test
        @DisplayName("존재하지 않는 ID - false")
        void shouldReturnFalseForNonExistentId() {
            // when
            boolean exists = queryDslRepository.existsById(999999L);

            // then
            assertThat(exists).isFalse();
        }
    }

    @Nested
    @DisplayName("findByCriteria 테스트")
    class FindByCriteriaTest {

        private CategoryJpaEntity activeCategory1;
        private CategoryJpaEntity activeCategory2;
        private CategoryJpaEntity activeCategory3;
        private CategoryJpaEntity inactiveCategory;
        private CategoryJpaEntity deletedCategory;

        @BeforeEach
        void setUp() {
            activeCategory1 = jpaRepository.save(createEntityWithName("의류카테고리", "의류 표시명"));
            activeCategory2 = jpaRepository.save(createEntityWithName("신발카테고리", "신발 표시명"));
            activeCategory3 = jpaRepository.save(createEntityWithName("가방카테고리", "가방 표시명"));
            inactiveCategory =
                    jpaRepository.save(createInactiveEntityWithName("비활성카테고리", "비활성 표시명"));
            deletedCategory = jpaRepository.save(createDeletedEntityWithName("삭제카테고리", "삭제 표시명"));
            flushAndClear();
        }

        @Test
        @DisplayName("전체 조회 - 삭제된 카테고리 제외")
        void shouldFindAllExcludingDeleted() {
            // given
            CategorySearchCriteria criteria = CategorySearchCriteria.defaultOf();

            // when
            List<CategoryJpaEntity> result = queryDslRepository.findByCriteria(criteria);

            // then
            assertThat(result).hasSize(4);
            assertThat(result)
                    .extracting(CategoryJpaEntity::getId)
                    .doesNotContain(deletedCategory.getId());
        }

        @Test
        @DisplayName("카테고리명 검색 - 부분 일치")
        void shouldSearchByCategoryName() {
            // given
            QueryContext<CategorySortKey> queryContext =
                    QueryContext.defaultOf(CategorySortKey.CREATED_AT);
            CategorySearchCriteria criteria =
                    CategorySearchCriteria.of(null, null, null, null, null, "카테고리", queryContext);

            // when
            List<CategoryJpaEntity> result = queryDslRepository.findByCriteria(criteria);

            // then
            // 의류 카테고리, 신발 카테고리, 가방 카테고리, 비활성카테고리 = 4개
            assertThat(result).hasSize(4);
        }

        @Test
        @DisplayName("타겟 그룹 필터링")
        void shouldFilterByTargetGroup() {
            // given
            jpaRepository.save(createEntityWithTargetAndType(TargetGroup.FEMALE, CategoryType.BAG));
            flushAndClear();

            QueryContext<CategorySortKey> queryContext =
                    QueryContext.defaultOf(CategorySortKey.CREATED_AT);
            CategorySearchCriteria criteria =
                    CategorySearchCriteria.of(
                            null, null, TargetGroup.FEMALE, null, null, null, queryContext);

            // when
            List<CategoryJpaEntity> result = queryDslRepository.findByCriteria(criteria);

            // then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getTargetGroup()).isEqualTo(TargetGroup.FEMALE);
        }

        @Test
        @DisplayName("카테고리 타입 필터링")
        void shouldFilterByCategoryType() {
            // given
            jpaRepository.save(createEntityWithTargetAndType(TargetGroup.MALE, CategoryType.SHOSE));
            flushAndClear();

            QueryContext<CategorySortKey> queryContext =
                    QueryContext.defaultOf(CategorySortKey.CREATED_AT);
            CategorySearchCriteria criteria =
                    CategorySearchCriteria.of(
                            null, null, null, CategoryType.SHOSE, null, null, queryContext);

            // when
            List<CategoryJpaEntity> result = queryDslRepository.findByCriteria(criteria);

            // then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getCategoryType()).isEqualTo(CategoryType.SHOSE);
        }

        @Nested
        @DisplayName("정렬 테스트")
        class SortingTest {

            @Test
            @DisplayName("카테고리명 오름차순 정렬")
            void shouldSortByCategoryNameAsc() {
                // given
                QueryContext<CategorySortKey> queryContext =
                        QueryContext.of(
                                CategorySortKey.CATEGORY_NAME,
                                SortDirection.ASC,
                                PageRequest.defaultPage(),
                                false);
                CategorySearchCriteria criteria =
                        CategorySearchCriteria.of(null, true, null, null, null, null, queryContext);

                // when
                List<CategoryJpaEntity> result = queryDslRepository.findByCriteria(criteria);

                // then
                assertThat(result).hasSize(3);
                assertThat(result)
                        .extracting(CategoryJpaEntity::getCategoryName)
                        .containsExactly("가방카테고리", "신발카테고리", "의류카테고리");
            }
        }

        @Nested
        @DisplayName("페이징 테스트")
        class PagingTest {

            @Test
            @DisplayName("첫 페이지 조회")
            void shouldReturnFirstPage() {
                // given
                QueryContext<CategorySortKey> queryContext =
                        QueryContext.of(
                                CategorySortKey.CREATED_AT,
                                SortDirection.ASC,
                                PageRequest.of(0, 2),
                                false);
                CategorySearchCriteria criteria =
                        CategorySearchCriteria.of(null, true, null, null, null, null, queryContext);

                // when
                List<CategoryJpaEntity> result = queryDslRepository.findByCriteria(criteria);

                // then
                assertThat(result).hasSize(2);
            }

            @Test
            @DisplayName("두 번째 페이지 조회")
            void shouldReturnSecondPage() {
                // given
                QueryContext<CategorySortKey> queryContext =
                        QueryContext.of(
                                CategorySortKey.CREATED_AT,
                                SortDirection.ASC,
                                PageRequest.of(1, 2),
                                false);
                CategorySearchCriteria criteria =
                        CategorySearchCriteria.of(null, true, null, null, null, null, queryContext);

                // when
                List<CategoryJpaEntity> result = queryDslRepository.findByCriteria(criteria);

                // then
                assertThat(result).hasSize(1);
            }
        }
    }

    @Nested
    @DisplayName("countByCriteria 테스트")
    class CountByCriteriaTest {

        @BeforeEach
        void setUp() {
            jpaRepository.save(createEntityWithName("카테고리1", "표시명1"));
            jpaRepository.save(createEntityWithName("카테고리2", "표시명2"));
            jpaRepository.save(createEntityWithName("카테고리3", "표시명3"));
            jpaRepository.save(createInactiveEntityWithName("비활성카테고리", "비활성 표시명"));
            jpaRepository.save(createDeletedEntityWithName("삭제카테고리", "삭제 표시명"));
            flushAndClear();
        }

        @Test
        @DisplayName("전체 카운트 - 삭제된 카테고리 제외")
        void shouldCountAllExcludingDeleted() {
            // given
            CategorySearchCriteria criteria = CategorySearchCriteria.defaultOf();

            // when
            long count = queryDslRepository.countByCriteria(criteria);

            // then
            assertThat(count).isEqualTo(4);
        }

        @Test
        @DisplayName("검색 조건 카운트")
        void shouldCountBySearchCondition() {
            // given
            QueryContext<CategorySortKey> queryContext =
                    QueryContext.defaultOf(CategorySortKey.CREATED_AT);
            CategorySearchCriteria criteria =
                    CategorySearchCriteria.of(null, null, null, null, null, "카테고리", queryContext);

            // when
            long count = queryDslRepository.countByCriteria(criteria);

            // then
            assertThat(count).isEqualTo(4);
        }

        @Test
        @DisplayName("조건에 맞는 데이터 없을 시 0 반환")
        void shouldReturnZeroWhenNoMatch() {
            // given
            QueryContext<CategorySortKey> queryContext =
                    QueryContext.defaultOf(CategorySortKey.CREATED_AT);
            CategorySearchCriteria criteria =
                    CategorySearchCriteria.of(null, null, null, null, null, "존재하지않음", queryContext);

            // when
            long count = queryDslRepository.countByCriteria(criteria);

            // then
            assertThat(count).isZero();
        }
    }

    @Nested
    @DisplayName("findAllDisplayed 테스트")
    class FindAllDisplayedTest {

        @Test
        @DisplayName("노출 중인 카테고리만 조회")
        void shouldFindOnlyDisplayedCategories() {
            // given
            jpaRepository.save(createEntityWithName("노출카테고리1", "노출 표시명1"));
            jpaRepository.save(createEntityWithName("노출카테고리2", "노출 표시명2"));
            jpaRepository.save(createInactiveEntityWithName("비노출카테고리", "비노출 표시명"));
            jpaRepository.save(createDeletedEntityWithName("삭제카테고리", "삭제 표시명"));
            flushAndClear();

            // when
            List<CategoryJpaEntity> result = queryDslRepository.findAllDisplayed();

            // then
            assertThat(result).hasSize(2);
            assertThat(result).allMatch(CategoryJpaEntity::isDisplayed);
        }
    }

    @Nested
    @DisplayName("findChildrenByParentId 테스트")
    class FindChildrenByParentIdTest {

        @Test
        @DisplayName("부모 ID로 자식 카테고리 조회")
        void shouldFindChildrenByParentId() {
            // given
            CategoryJpaEntity parent = jpaRepository.save(createRootEntity("부모카테고리"));
            flushAndClear();

            jpaRepository.save(createChildEntity("자식1", parent.getId(), 2));
            jpaRepository.save(createChildEntity("자식2", parent.getId(), 2));
            jpaRepository.save(createChildEntity("다른자식", 999L, 2)); // 다른 부모
            flushAndClear();

            // when
            List<CategoryJpaEntity> result =
                    queryDslRepository.findChildrenByParentId(parent.getId());

            // then
            assertThat(result).hasSize(2);
            assertThat(result)
                    .extracting(CategoryJpaEntity::getCategoryName)
                    .containsExactlyInAnyOrder("자식1", "자식2");
        }

        @Test
        @DisplayName("루트 카테고리 조회 (parentId = 0)")
        void shouldFindRootCategories() {
            // given
            jpaRepository.save(createRootEntity("루트1"));
            jpaRepository.save(createRootEntity("루트2"));
            CategoryJpaEntity parent = jpaRepository.save(createRootEntity("부모"));
            flushAndClear();

            jpaRepository.save(createChildEntity("자식", parent.getId(), 2));
            flushAndClear();

            // when
            List<CategoryJpaEntity> result = queryDslRepository.findChildrenByParentId(0L);

            // then
            assertThat(result).hasSize(3);
            assertThat(result).allMatch(entity -> entity.getParentCategoryId() == 0L);
        }

        @Test
        @DisplayName("자식이 없는 경우 빈 목록 반환")
        void shouldReturnEmptyWhenNoChildren() {
            // given
            CategoryJpaEntity leafCategory = jpaRepository.save(createRootEntity("리프카테고리"));
            flushAndClear();

            // when
            List<CategoryJpaEntity> result =
                    queryDslRepository.findChildrenByParentId(leafCategory.getId());

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("부모 카테고리 ID 필터 테스트")
    class ParentCategoryIdFilterTest {

        @Test
        @DisplayName("부모 카테고리 ID로 하위 카테고리 조회")
        void shouldFindByParentCategoryId() {
            // given
            CategoryJpaEntity parent = jpaRepository.save(createRootEntity("부모"));
            flushAndClear();

            jpaRepository.save(createChildEntity("자식1", parent.getId(), 2));
            jpaRepository.save(createChildEntity("자식2", parent.getId(), 2));
            flushAndClear();

            QueryContext<CategorySortKey> queryContext =
                    QueryContext.defaultOf(CategorySortKey.CREATED_AT);
            CategorySearchCriteria criteria =
                    CategorySearchCriteria.of(
                            CategoryId.of(parent.getId()),
                            null,
                            null,
                            null,
                            null,
                            null,
                            queryContext);

            // when
            List<CategoryJpaEntity> result = queryDslRepository.findByCriteria(criteria);

            // then
            assertThat(result).hasSize(2);
            assertThat(result)
                    .allMatch(entity -> entity.getParentCategoryId().equals(parent.getId()));
        }
    }

    // ========================================================================
    // Helper Methods
    // ========================================================================

    private CategoryJpaEntity createDeletedEntity() {
        Instant now = Instant.now();
        return CategoryJpaEntity.create(
                null,
                CategoryJpaEntityFixtures.DEFAULT_CATEGORY_NAME,
                CategoryJpaEntityFixtures.DEFAULT_DEPTH,
                CategoryJpaEntityFixtures.DEFAULT_PARENT_ID,
                CategoryJpaEntityFixtures.DEFAULT_DISPLAY_NAME,
                false,
                TargetGroup.MALE,
                CategoryType.CLOTHING,
                CategoryJpaEntityFixtures.DEFAULT_PATH,
                now,
                now,
                now);
    }

    private CategoryJpaEntity createEntityWithName(String categoryName, String displayName) {
        Instant now = Instant.now();
        return CategoryJpaEntity.create(
                null,
                categoryName,
                CategoryJpaEntityFixtures.DEFAULT_DEPTH,
                CategoryJpaEntityFixtures.DEFAULT_PARENT_ID,
                displayName,
                true,
                TargetGroup.MALE,
                CategoryType.CLOTHING,
                CategoryJpaEntityFixtures.DEFAULT_PATH,
                now,
                now,
                null);
    }

    private CategoryJpaEntity createDeletedEntityWithName(String categoryName, String displayName) {
        Instant now = Instant.now();
        return CategoryJpaEntity.create(
                null,
                categoryName,
                CategoryJpaEntityFixtures.DEFAULT_DEPTH,
                CategoryJpaEntityFixtures.DEFAULT_PARENT_ID,
                displayName,
                false,
                TargetGroup.MALE,
                CategoryType.CLOTHING,
                CategoryJpaEntityFixtures.DEFAULT_PATH,
                now,
                now,
                now);
    }

    private CategoryJpaEntity createInactiveEntityWithName(
            String categoryName, String displayName) {
        Instant now = Instant.now();
        return CategoryJpaEntity.create(
                null,
                categoryName,
                CategoryJpaEntityFixtures.DEFAULT_DEPTH,
                CategoryJpaEntityFixtures.DEFAULT_PARENT_ID,
                displayName,
                false,
                TargetGroup.MALE,
                CategoryType.CLOTHING,
                CategoryJpaEntityFixtures.DEFAULT_PATH,
                now,
                now,
                null);
    }

    private CategoryJpaEntity createEntityWithTargetAndType(
            TargetGroup targetGroup, CategoryType categoryType) {
        Instant now = Instant.now();
        return CategoryJpaEntity.create(
                null,
                "타겟타입테스트",
                CategoryJpaEntityFixtures.DEFAULT_DEPTH,
                CategoryJpaEntityFixtures.DEFAULT_PARENT_ID,
                "타겟타입 표시명",
                true,
                targetGroup,
                categoryType,
                CategoryJpaEntityFixtures.DEFAULT_PATH,
                now,
                now,
                null);
    }

    private CategoryJpaEntity createRootEntity(String categoryName) {
        Instant now = Instant.now();
        return CategoryJpaEntity.create(
                null,
                categoryName,
                1,
                0L,
                categoryName + " 표시명",
                true,
                TargetGroup.MALE,
                CategoryType.CLOTHING,
                "/",
                now,
                now,
                null);
    }

    private CategoryJpaEntity createChildEntity(String categoryName, Long parentId, int depth) {
        Instant now = Instant.now();
        return CategoryJpaEntity.create(
                null,
                categoryName,
                depth,
                parentId,
                categoryName + " 표시명",
                true,
                TargetGroup.MALE,
                CategoryType.CLOTHING,
                "/" + parentId + "/",
                now,
                now,
                null);
    }
}
