package com.ryuqq.setof.integration.test.repository.legacy.seller;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.seller.query.SellerSearchCriteria;
import com.ryuqq.setof.integration.test.common.base.RepositoryTestBase;
import com.ryuqq.setof.integration.test.common.tag.TestTags;
import com.ryuqq.setof.storage.legacy.seller.LegacySellerEntityFixtures;
import com.ryuqq.setof.storage.legacy.seller.entity.LegacySellerEntity;
import com.ryuqq.setof.storage.legacy.seller.repository.LegacySellerJpaRepository;
import com.ryuqq.setof.storage.legacy.seller.repository.LegacySellerQueryDslRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * LegacySellerQueryDslRepository 통합 테스트.
 *
 * <p>H2 인메모리 DB에서 레거시 셀러 QueryDSL Repository 기능을 검증합니다.
 */
@DisplayName("레거시 셀러 QueryDSL Repository 테스트")
@Tag(TestTags.LEGACY)
@Tag(TestTags.SELLER)
class LegacySellerQueryDslRepositoryTest extends RepositoryTestBase {

    @Autowired private LegacySellerJpaRepository jpaRepository;

    @Autowired private LegacySellerQueryDslRepository queryDslRepository;

    @Nested
    @DisplayName("findById 테스트")
    class FindByIdTest {

        @Test
        @DisplayName("존재하는 셀러 조회 성공")
        void shouldFindExistingSeller() {
            // given
            LegacySellerEntity saved =
                    jpaRepository.save(
                            LegacySellerEntityFixtures.builder()
                                    .id(100L)
                                    .sellerName("테스트 셀러")
                                    .build());
            flushAndClear();

            // when
            Optional<LegacySellerEntity> found = queryDslRepository.findById(saved.getId());

            // then
            assertThat(found).isPresent();
            assertThat(found.get().getSellerName()).isEqualTo("테스트 셀러");
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회시 빈 Optional 반환")
        void shouldReturnEmptyForNonExistentId() {
            // when
            Optional<LegacySellerEntity> found = queryDslRepository.findById(999999L);

            // then
            assertThat(found).isEmpty();
        }
    }

    @Nested
    @DisplayName("findByIds 테스트")
    class FindByIdsTest {

        @Test
        @DisplayName("ID 목록으로 셀러 목록 조회 성공")
        void shouldFindByIds() {
            // given
            LegacySellerEntity entity1 =
                    jpaRepository.save(
                            LegacySellerEntityFixtures.builder()
                                    .id(200L)
                                    .sellerName("셀러A")
                                    .build());
            LegacySellerEntity entity2 =
                    jpaRepository.save(
                            LegacySellerEntityFixtures.builder()
                                    .id(201L)
                                    .sellerName("셀러B")
                                    .build());
            LegacySellerEntity entity3 =
                    jpaRepository.save(
                            LegacySellerEntityFixtures.builder()
                                    .id(202L)
                                    .sellerName("셀러C")
                                    .build());
            flushAndClear();

            // when
            List<LegacySellerEntity> found =
                    queryDslRepository.findByIds(List.of(entity1.getId(), entity3.getId()));

            // then
            assertThat(found).hasSize(2);
            assertThat(found)
                    .extracting(LegacySellerEntity::getSellerName)
                    .containsExactlyInAnyOrder("셀러A", "셀러C");
        }

        @Test
        @DisplayName("빈 ID 목록으로 조회시 빈 목록 반환")
        void shouldReturnEmptyListForEmptyIds() {
            // when
            List<LegacySellerEntity> found = queryDslRepository.findByIds(List.of());

            // then
            assertThat(found).isEmpty();
        }
    }

    @Nested
    @DisplayName("existsById 테스트")
    class ExistsByIdTest {

        @Test
        @DisplayName("존재하는 셀러는 true 반환")
        void shouldReturnTrueForExistingSeller() {
            // given
            LegacySellerEntity saved =
                    jpaRepository.save(LegacySellerEntityFixtures.builder().id(300L).build());
            flushAndClear();

            // when
            boolean exists = queryDslRepository.existsById(saved.getId());

            // then
            assertThat(exists).isTrue();
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
    @DisplayName("existsBySellerName 테스트")
    class ExistsBySellerNameTest {

        @Test
        @DisplayName("존재하는 셀러명은 true 반환")
        void shouldReturnTrueForExistingSellerName() {
            // given
            jpaRepository.save(
                    LegacySellerEntityFixtures.builder().id(400L).sellerName("유니크 셀러").build());
            flushAndClear();

            // when
            boolean exists = queryDslRepository.existsBySellerName("유니크 셀러");

            // then
            assertThat(exists).isTrue();
        }

        @Test
        @DisplayName("존재하지 않는 셀러명은 false 반환")
        void shouldReturnFalseForNonExistentSellerName() {
            // when
            boolean exists = queryDslRepository.existsBySellerName("존재하지 않는 셀러");

            // then
            assertThat(exists).isFalse();
        }
    }

    @Nested
    @DisplayName("existsBySellerNameExcluding 테스트")
    class ExistsBySellerNameExcludingTest {

        @Test
        @DisplayName("다른 셀러가 같은 이름을 사용하면 true 반환")
        void shouldReturnTrueWhenOtherSellerHasSameName() {
            // given
            jpaRepository.save(
                    LegacySellerEntityFixtures.builder().id(500L).sellerName("중복 셀러").build());
            jpaRepository.save(
                    LegacySellerEntityFixtures.builder().id(501L).sellerName("중복 셀러").build());
            flushAndClear();

            // when
            boolean exists = queryDslRepository.existsBySellerNameExcluding("중복 셀러", 500L);

            // then
            assertThat(exists).isTrue();
        }

        @Test
        @DisplayName("자기 자신만 해당 이름을 사용하면 false 반환")
        void shouldReturnFalseWhenOnlySelfHasName() {
            // given
            jpaRepository.save(
                    LegacySellerEntityFixtures.builder().id(510L).sellerName("유일한 셀러").build());
            flushAndClear();

            // when
            boolean exists = queryDslRepository.existsBySellerNameExcluding("유일한 셀러", 510L);

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
            jpaRepository.save(
                    LegacySellerEntityFixtures.builder().id(600L).sellerName("셀러1").build());
            jpaRepository.save(
                    LegacySellerEntityFixtures.builder().id(601L).sellerName("셀러2").build());
            flushAndClear();

            // when
            List<LegacySellerEntity> result =
                    queryDslRepository.findByCriteria(SellerSearchCriteria.defaultCriteria());

            // then
            assertThat(result).hasSizeGreaterThanOrEqualTo(2);
        }

        @Test
        @DisplayName("셀러명으로 검색 (LIKE)")
        void shouldSearchBySellerName() {
            // given
            jpaRepository.save(
                    LegacySellerEntityFixtures.builder().id(610L).sellerName("테스트 검색 셀러").build());
            jpaRepository.save(
                    LegacySellerEntityFixtures.builder().id(611L).sellerName("다른 셀러").build());
            flushAndClear();

            // when
            SellerSearchCriteria criteria =
                    SellerSearchCriteria.of(
                            null,
                            com.ryuqq.setof.domain.seller.query.SellerSearchField.SELLER_NAME,
                            "검색",
                            null);
            List<LegacySellerEntity> result = queryDslRepository.findByCriteria(criteria);

            // then
            assertThat(result).extracting(LegacySellerEntity::getSellerName).contains("테스트 검색 셀러");
            assertThat(result)
                    .extracting(LegacySellerEntity::getSellerName)
                    .doesNotContain("다른 셀러");
        }
    }

    @Nested
    @DisplayName("countByCriteria 테스트")
    class CountByCriteriaTest {

        @Test
        @DisplayName("검색 조건으로 셀러 개수 조회")
        void shouldCountByCriteria() {
            // given
            jpaRepository.save(
                    LegacySellerEntityFixtures.builder().id(700L).sellerName("개수테스트1").build());
            jpaRepository.save(
                    LegacySellerEntityFixtures.builder().id(701L).sellerName("개수테스트2").build());
            flushAndClear();

            // when
            long count = queryDslRepository.countByCriteria(SellerSearchCriteria.defaultCriteria());

            // then
            assertThat(count).isGreaterThanOrEqualTo(2L);
        }
    }
}
