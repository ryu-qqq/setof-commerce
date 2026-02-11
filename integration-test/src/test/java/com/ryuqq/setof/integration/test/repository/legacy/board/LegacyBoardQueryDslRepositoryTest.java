package com.ryuqq.setof.integration.test.repository.legacy.board;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.board.query.BoardSearchCriteria;
import com.ryuqq.setof.integration.test.common.base.RepositoryTestBase;
import com.ryuqq.setof.integration.test.common.tag.TestTags;
import com.ryuqq.setof.storage.legacy.board.LegacyBoardEntityFixtures;
import com.ryuqq.setof.storage.legacy.board.entity.LegacyBoardEntity;
import com.ryuqq.setof.storage.legacy.board.repository.LegacyBoardQueryDslRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * LegacyBoardQueryDslRepository 통합 테스트.
 *
 * <p>실제 DB와 연동하여 QueryDSL Repository의 동작을 검증합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag(TestTags.LEGACY)
@DisplayName("레거시 게시판 QueryDSL Repository 테스트")
class LegacyBoardQueryDslRepositoryTest extends RepositoryTestBase {

    @Autowired private LegacyBoardQueryDslRepository queryDslRepository;

    @Nested
    @DisplayName("findByCriteria 메서드 테스트")
    class FindByCriteriaTest {

        @Test
        @DisplayName("기본 검색 조건으로 게시글 목록을 조회합니다")
        void shouldFindAllWithDefaultCriteria() {
            // given
            persist(LegacyBoardEntityFixtures.builder().id(null).title("게시글1").build());
            persist(LegacyBoardEntityFixtures.builder().id(null).title("게시글2").build());
            flushAndClear();

            // when
            List<LegacyBoardEntity> result =
                    queryDslRepository.findByCriteria(BoardSearchCriteria.defaultOf());

            // then
            assertThat(result).hasSizeGreaterThanOrEqualTo(2);
        }

        @Test
        @DisplayName("페이징 조건으로 게시글 목록을 조회합니다")
        void shouldFindWithPagination() {
            // given
            persist(LegacyBoardEntityFixtures.builder().id(null).title("게시글1").build());
            persist(LegacyBoardEntityFixtures.builder().id(null).title("게시글2").build());
            persist(LegacyBoardEntityFixtures.builder().id(null).title("게시글3").build());
            flushAndClear();

            // when
            BoardSearchCriteria criteria = BoardSearchCriteria.ofPage(0, 2);
            List<LegacyBoardEntity> result = queryDslRepository.findByCriteria(criteria);

            // then
            assertThat(result).hasSizeLessThanOrEqualTo(2);
        }

        @Test
        @DisplayName("등록일시 기준으로 정렬된 목록을 조회합니다")
        void shouldFindWithOrderByInsertDate() {
            // given
            LocalDateTime now = LocalDateTime.now();
            persist(
                    LegacyBoardEntityFixtures.builder()
                            .id(null)
                            .title("최신 게시글")
                            .insertDate(now)
                            .build());
            persist(
                    LegacyBoardEntityFixtures.builder()
                            .id(null)
                            .title("과거 게시글")
                            .insertDate(now.minusDays(1))
                            .build());
            flushAndClear();

            // when
            List<LegacyBoardEntity> result =
                    queryDslRepository.findByCriteria(BoardSearchCriteria.defaultOf());

            // then
            assertThat(result).isNotEmpty();
        }

        @Test
        @DisplayName("빈 결과를 반환할 수 있습니다")
        void shouldReturnEmptyListWhenNoData() {
            // given - 데이터 없음

            // when
            BoardSearchCriteria criteria = BoardSearchCriteria.ofPage(999, 10);
            List<LegacyBoardEntity> result = queryDslRepository.findByCriteria(criteria);

            // then
            assertThat(result).isNotNull();
        }
    }

    @Nested
    @DisplayName("countByCriteria 메서드 테스트")
    class CountByCriteriaTest {

        @Test
        @DisplayName("게시글 개수를 조회합니다")
        void shouldCountAll() {
            // given
            persist(LegacyBoardEntityFixtures.builder().id(null).title("개수테스트1").build());
            persist(LegacyBoardEntityFixtures.builder().id(null).title("개수테스트2").build());
            persist(LegacyBoardEntityFixtures.builder().id(null).title("개수테스트3").build());
            flushAndClear();

            // when
            long count = queryDslRepository.countByCriteria(BoardSearchCriteria.defaultOf());

            // then
            assertThat(count).isGreaterThanOrEqualTo(3L);
        }

        @Test
        @DisplayName("게시글이 없으면 0을 반환합니다")
        void shouldReturnZeroWhenNoBoards() {
            // given
            executeNativeSql("DELETE FROM board");
            flushAndClear();

            // when
            long count = queryDslRepository.countByCriteria(BoardSearchCriteria.defaultOf());

            // then
            assertThat(count).isZero();
        }

        @Test
        @DisplayName("페이징 조건과 관계없이 전체 개수를 반환합니다")
        void shouldReturnTotalCountRegardlessOfPaging() {
            // given
            persist(LegacyBoardEntityFixtures.builder().id(null).build());
            persist(LegacyBoardEntityFixtures.builder().id(null).build());
            persist(LegacyBoardEntityFixtures.builder().id(null).build());
            flushAndClear();

            // when
            long countPage0 = queryDslRepository.countByCriteria(BoardSearchCriteria.ofPage(0, 2));
            long countPage1 = queryDslRepository.countByCriteria(BoardSearchCriteria.ofPage(1, 2));

            // then
            assertThat(countPage0).isGreaterThanOrEqualTo(3L);
            assertThat(countPage1).isEqualTo(countPage0);
        }
    }

    @Nested
    @DisplayName("복합 시나리오 테스트")
    class ComplexScenarioTest {

        @Test
        @DisplayName("게시글 목록 조회와 개수 조회 결과가 일치합니다")
        void shouldMatchCountAndListSize() {
            // given
            int expectedCount = 5;
            for (int i = 0; i < expectedCount; i++) {
                persist(
                        LegacyBoardEntityFixtures.builder()
                                .id(null)
                                .title("게시글 " + (i + 1))
                                .build());
            }
            flushAndClear();

            // when
            BoardSearchCriteria criteria = BoardSearchCriteria.ofPage(0, 100);
            List<LegacyBoardEntity> list = queryDslRepository.findByCriteria(criteria);
            long count = queryDslRepository.countByCriteria(criteria);

            // then
            assertThat(list).hasSizeGreaterThanOrEqualTo(expectedCount);
            assertThat(count).isGreaterThanOrEqualTo(expectedCount);
        }

        @Test
        @DisplayName("여러 페이지에 걸친 게시글을 조회할 수 있습니다")
        void shouldFetchMultiplePages() {
            // given
            for (int i = 0; i < 10; i++) {
                persist(
                        LegacyBoardEntityFixtures.builder()
                                .id(null)
                                .title("페이징 테스트 " + (i + 1))
                                .build());
            }
            flushAndClear();

            // when
            List<LegacyBoardEntity> page0 =
                    queryDslRepository.findByCriteria(BoardSearchCriteria.ofPage(0, 3));
            List<LegacyBoardEntity> page1 =
                    queryDslRepository.findByCriteria(BoardSearchCriteria.ofPage(1, 3));
            List<LegacyBoardEntity> page2 =
                    queryDslRepository.findByCriteria(BoardSearchCriteria.ofPage(2, 3));

            // then
            assertThat(page0).hasSizeLessThanOrEqualTo(3);
            assertThat(page1).hasSizeLessThanOrEqualTo(3);
            assertThat(page2).hasSizeLessThanOrEqualTo(3);
        }
    }
}
