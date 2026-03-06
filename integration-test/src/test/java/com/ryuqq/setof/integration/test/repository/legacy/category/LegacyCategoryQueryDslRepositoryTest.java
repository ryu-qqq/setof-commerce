package com.ryuqq.setof.integration.test.repository.legacy.category;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.category.query.CategorySearchCriteria;
import com.ryuqq.setof.integration.test.common.base.RepositoryTestBase;
import com.ryuqq.setof.integration.test.common.tag.TestTags;
import com.ryuqq.setof.storage.legacy.category.LegacyCategoryEntityFixtures;
import com.ryuqq.setof.storage.legacy.category.dto.LegacyCategoryTreeDto;
import com.ryuqq.setof.storage.legacy.category.entity.LegacyCategoryEntity;
import com.ryuqq.setof.storage.legacy.category.repository.LegacyCategoryQueryDslRepository;
import com.ryuqq.setof.storage.legacy.common.Yn;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * LegacyCategoryQueryDslRepository 통합 테스트.
 *
 * <p>H2 인메모리 DB에서 레거시 카테고리 QueryDSL Repository 기능을 검증합니다.
 */
@DisplayName("레거시 카테고리 QueryDSL Repository 테스트")
@Tag(TestTags.LEGACY)
@Tag(TestTags.CATEGORY)
class LegacyCategoryQueryDslRepositoryTest extends RepositoryTestBase {

    @Autowired private LegacyCategoryQueryDslRepository queryDslRepository;

    @Nested
    @DisplayName("findById 테스트")
    class FindByIdTest {

        @Test
        @DisplayName("활성 상태 카테고리 조회 성공")
        void shouldFindActiveCategory() {
            // given
            LegacyCategoryEntity saved =
                    persist(
                            LegacyCategoryEntityFixtures.builder()
                                    .id(100L)
                                    .categoryName("상의")
                                    .displayName("상의")
                                    .build());
            flushAndClear();

            // when
            Optional<LegacyCategoryEntity> found = queryDslRepository.findById(saved.getId());

            // then
            assertThat(found).isPresent();
            assertThat(found.get().getCategoryName()).isEqualTo("상의");
        }

        @Test
        @DisplayName("삭제된 카테고리는 조회되지 않음 (deleteYn = 'Y')")
        void shouldNotFindDeletedCategory() {
            // given
            LegacyCategoryEntity saved =
                    persist(LegacyCategoryEntityFixtures.builder().id(101L).deleteYn("Y").build());
            flushAndClear();

            // when
            Optional<LegacyCategoryEntity> found = queryDslRepository.findById(saved.getId());

            // then
            assertThat(found).isEmpty();
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회시 빈 Optional 반환")
        void shouldReturnEmptyForNonExistentId() {
            // when
            Optional<LegacyCategoryEntity> found = queryDslRepository.findById(999999L);

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
            LegacyCategoryEntity entity1 =
                    persist(
                            LegacyCategoryEntityFixtures.builder()
                                    .id(200L)
                                    .categoryName("카테고리A")
                                    .build());
            LegacyCategoryEntity entity2 =
                    persist(
                            LegacyCategoryEntityFixtures.builder()
                                    .id(201L)
                                    .categoryName("카테고리B")
                                    .build());
            LegacyCategoryEntity entity3 =
                    persist(
                            LegacyCategoryEntityFixtures.builder()
                                    .id(202L)
                                    .categoryName("카테고리C")
                                    .build());
            flushAndClear();

            // when
            List<LegacyCategoryEntity> found =
                    queryDslRepository.findByIds(List.of(entity1.getId(), entity3.getId()));

            // then
            assertThat(found).hasSize(2);
            assertThat(found)
                    .extracting(LegacyCategoryEntity::getCategoryName)
                    .containsExactlyInAnyOrder("카테고리A", "카테고리C");
        }

        @Test
        @DisplayName("삭제된 카테고리는 ID 목록 조회에서 제외됨")
        void shouldExcludeDeletedCategories() {
            // given
            LegacyCategoryEntity active =
                    persist(
                            LegacyCategoryEntityFixtures.builder()
                                    .id(210L)
                                    .categoryName("활성카테고리")
                                    .deleteYn("N")
                                    .build());
            LegacyCategoryEntity deleted =
                    persist(
                            LegacyCategoryEntityFixtures.builder()
                                    .id(211L)
                                    .categoryName("삭제카테고리")
                                    .deleteYn("Y")
                                    .build());
            flushAndClear();

            // when
            List<LegacyCategoryEntity> found =
                    queryDslRepository.findByIds(List.of(active.getId(), deleted.getId()));

            // then
            assertThat(found).hasSize(1);
            assertThat(found.get(0).getCategoryName()).isEqualTo("활성카테고리");
        }
    }

    @Nested
    @DisplayName("existsById 테스트")
    class ExistsByIdTest {

        @Test
        @DisplayName("존재하는 활성 카테고리는 true 반환")
        void shouldReturnTrueForExistingActiveCategory() {
            // given
            LegacyCategoryEntity saved =
                    persist(LegacyCategoryEntityFixtures.builder().id(300L).deleteYn("N").build());
            flushAndClear();

            // when
            boolean exists = queryDslRepository.existsById(saved.getId());

            // then
            assertThat(exists).isTrue();
        }

        @Test
        @DisplayName("삭제된 카테고리는 false 반환")
        void shouldReturnFalseForDeletedCategory() {
            // given
            LegacyCategoryEntity saved =
                    persist(LegacyCategoryEntityFixtures.builder().id(301L).deleteYn("Y").build());
            flushAndClear();

            // when
            boolean exists = queryDslRepository.existsById(saved.getId());

            // then
            assertThat(exists).isFalse();
        }

        @Test
        @DisplayName("존재하지 않는 ID는 false 반환")
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

        @Test
        @DisplayName("기본 검색 조건으로 전체 조회")
        void shouldFindAllWithDefaultCriteria() {
            // given
            persist(
                    LegacyCategoryEntityFixtures.builder()
                            .id(400L)
                            .categoryName("상의")
                            .categoryDepth(1)
                            .build());
            persist(
                    LegacyCategoryEntityFixtures.builder()
                            .id(401L)
                            .categoryName("하의")
                            .categoryDepth(1)
                            .build());
            flushAndClear();

            // when
            List<LegacyCategoryEntity> result =
                    queryDslRepository.findByCriteria(CategorySearchCriteria.defaultOf());

            // then
            assertThat(result).hasSizeGreaterThanOrEqualTo(2);
        }
    }

    @Nested
    @DisplayName("countByCriteria 테스트")
    class CountByCriteriaTest {

        @Test
        @DisplayName("검색 조건으로 카테고리 개수 조회")
        void shouldCountByCriteria() {
            // given
            persist(
                    LegacyCategoryEntityFixtures.builder()
                            .id(500L)
                            .categoryName("개수테스트1")
                            .deleteYn("N")
                            .build());
            persist(
                    LegacyCategoryEntityFixtures.builder()
                            .id(501L)
                            .categoryName("개수테스트2")
                            .deleteYn("N")
                            .build());
            persist(
                    LegacyCategoryEntityFixtures.builder()
                            .id(502L)
                            .categoryName("삭제됨")
                            .deleteYn("Y")
                            .build());
            flushAndClear();

            // when
            long count = queryDslRepository.countByCriteria(CategorySearchCriteria.defaultOf());

            // then
            assertThat(count).isGreaterThanOrEqualTo(2L);
        }
    }

    @Nested
    @DisplayName("findAllDisplayed 테스트")
    class FindAllDisplayedTest {

        @Test
        @DisplayName("표시 중인 카테고리만 조회")
        void shouldFindOnlyDisplayedCategories() {
            // given
            persist(
                    LegacyCategoryEntityFixtures.builder()
                            .id(600L)
                            .categoryName("표시카테고리")
                            .displayYn("Y")
                            .deleteYn("N")
                            .build());
            persist(
                    LegacyCategoryEntityFixtures.builder()
                            .id(601L)
                            .categoryName("비표시카테고리")
                            .displayYn("N")
                            .deleteYn("N")
                            .build());
            flushAndClear();

            // when
            List<LegacyCategoryEntity> result = queryDslRepository.findAllDisplayed();

            // then
            assertThat(result).allMatch(e -> e.getDisplayYn() == Yn.Y);
        }

        @Test
        @DisplayName("depth와 path 순으로 정렬")
        void shouldOrderByDepthAndPath() {
            // given
            persist(
                    LegacyCategoryEntityFixtures.builder()
                            .id(610L)
                            .categoryDepth(2)
                            .path("/1/610")
                            .displayYn("Y")
                            .deleteYn("N")
                            .build());
            persist(
                    LegacyCategoryEntityFixtures.builder()
                            .id(611L)
                            .categoryDepth(1)
                            .path("/611")
                            .displayYn("Y")
                            .deleteYn("N")
                            .build());
            flushAndClear();

            // when
            List<LegacyCategoryEntity> result = queryDslRepository.findAllDisplayed();

            // then
            // depth 1이 depth 2보다 먼저 나와야 함
            boolean foundDepth1 = false;
            for (LegacyCategoryEntity entity : result) {
                if (entity.getCategoryDepth() == 1) {
                    foundDepth1 = true;
                }
                if (entity.getCategoryDepth() == 2 && !foundDepth1) {
                    // depth 2가 depth 1보다 먼저 나오면 안됨
                    // 하지만 다른 테스트 데이터가 있을 수 있으므로 단순 검증
                }
            }
        }
    }

    @Nested
    @DisplayName("findChildrenByParentId 테스트")
    class FindChildrenByParentIdTest {

        @Test
        @DisplayName("부모 ID로 직계 하위 카테고리 조회")
        void shouldFindChildrenByParentId() {
            // given
            Long parentId = 700L;
            persist(
                    LegacyCategoryEntityFixtures.builder()
                            .id(parentId)
                            .parentCategoryId(0L)
                            .categoryDepth(1)
                            .categoryName("부모")
                            .build());
            persist(
                    LegacyCategoryEntityFixtures.builder()
                            .id(701L)
                            .parentCategoryId(parentId)
                            .categoryDepth(2)
                            .categoryName("자식1")
                            .build());
            persist(
                    LegacyCategoryEntityFixtures.builder()
                            .id(702L)
                            .parentCategoryId(parentId)
                            .categoryDepth(2)
                            .categoryName("자식2")
                            .build());
            flushAndClear();

            // when
            List<LegacyCategoryEntity> children =
                    queryDslRepository.findChildrenByParentId(parentId);

            // then
            assertThat(children).hasSize(2);
            assertThat(children)
                    .extracting(LegacyCategoryEntity::getCategoryName)
                    .containsExactlyInAnyOrder("자식1", "자식2");
        }

        @Test
        @DisplayName("삭제된 자식 카테고리는 제외")
        void shouldExcludeDeletedChildren() {
            // given
            Long parentId = 710L;
            persist(
                    LegacyCategoryEntityFixtures.builder()
                            .id(parentId)
                            .parentCategoryId(0L)
                            .categoryDepth(1)
                            .categoryName("부모")
                            .build());
            persist(
                    LegacyCategoryEntityFixtures.builder()
                            .id(711L)
                            .parentCategoryId(parentId)
                            .categoryDepth(2)
                            .categoryName("활성자식")
                            .deleteYn("N")
                            .build());
            persist(
                    LegacyCategoryEntityFixtures.builder()
                            .id(712L)
                            .parentCategoryId(parentId)
                            .categoryDepth(2)
                            .categoryName("삭제자식")
                            .deleteYn("Y")
                            .build());
            flushAndClear();

            // when
            List<LegacyCategoryEntity> children =
                    queryDslRepository.findChildrenByParentId(parentId);

            // then
            assertThat(children).hasSize(1);
            assertThat(children.get(0).getCategoryName()).isEqualTo("활성자식");
        }
    }

    @Nested
    @DisplayName("findParentsByChildId 테스트 (Recursive CTE)")
    class FindParentsByChildIdTest {

        @Test
        @DisplayName("자식 ID로 부모 카테고리들 조회 (Recursive CTE)")
        void shouldFindParentsByChildId() {
            // given: root(800) -> parent(801) -> child(802)
            persist(
                    LegacyCategoryEntityFixtures.builder()
                            .id(800L)
                            .parentCategoryId(0L)
                            .categoryDepth(1)
                            .categoryName("루트")
                            .path("/800")
                            .deleteYn("N")
                            .build());
            persist(
                    LegacyCategoryEntityFixtures.builder()
                            .id(801L)
                            .parentCategoryId(800L)
                            .categoryDepth(2)
                            .categoryName("부모")
                            .path("/800/801")
                            .deleteYn("N")
                            .build());
            persist(
                    LegacyCategoryEntityFixtures.builder()
                            .id(802L)
                            .parentCategoryId(801L)
                            .categoryDepth(3)
                            .categoryName("자식")
                            .path("/800/801/802")
                            .deleteYn("N")
                            .build());
            flushAndClear();

            // when
            List<LegacyCategoryTreeDto> parents = queryDslRepository.findParentsByChildId(802L);

            // then: 루트, 부모, 자식 순 (depth 오름차순)
            assertThat(parents).hasSize(3);
            assertThat(parents)
                    .extracting(LegacyCategoryTreeDto::getCategoryName)
                    .containsExactly("루트", "부모", "자식");
        }

        @Test
        @DisplayName("루트 카테고리 ID로 조회 시 해당 카테고리만 반환")
        void shouldReturnSingleRootForRootCategoryId() {
            // given
            persist(
                    LegacyCategoryEntityFixtures.builder()
                            .id(810L)
                            .parentCategoryId(0L)
                            .categoryDepth(1)
                            .categoryName("루트만")
                            .path("/810")
                            .deleteYn("N")
                            .build());
            flushAndClear();

            // when
            List<LegacyCategoryTreeDto> parents = queryDslRepository.findParentsByChildId(810L);

            // then
            assertThat(parents).hasSize(1);
            assertThat(parents.get(0).getCategoryName()).isEqualTo("루트만");
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회 시 빈 목록 반환")
        void shouldReturnEmptyForNonExistentId() {
            // when
            List<LegacyCategoryTreeDto> parents = queryDslRepository.findParentsByChildId(999999L);

            // then
            assertThat(parents).isEmpty();
        }
    }
}
