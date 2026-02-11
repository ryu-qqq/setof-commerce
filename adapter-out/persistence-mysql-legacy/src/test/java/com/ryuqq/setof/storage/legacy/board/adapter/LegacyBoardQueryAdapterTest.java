package com.ryuqq.setof.storage.legacy.board.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import com.ryuqq.setof.domain.board.aggregate.Board;
import com.ryuqq.setof.domain.board.query.BoardSearchCriteria;
import com.ryuqq.setof.storage.legacy.board.LegacyBoardEntityFixtures;
import com.ryuqq.setof.storage.legacy.board.entity.LegacyBoardEntity;
import com.ryuqq.setof.storage.legacy.board.mapper.LegacyBoardEntityMapper;
import com.ryuqq.setof.storage.legacy.board.repository.LegacyBoardQueryDslRepository;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * LegacyBoardQueryAdapter 단위 테스트.
 *
 * <p>Repository와 Mapper를 Mocking하여 어댑터 로직을 검증합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("LegacyBoardQueryAdapter 테스트")
class LegacyBoardQueryAdapterTest {

    @InjectMocks private LegacyBoardQueryAdapter adapter;

    @Mock private LegacyBoardQueryDslRepository queryDslRepository;

    @Mock private LegacyBoardEntityMapper mapper;

    @Nested
    @DisplayName("findByCriteria 메서드 테스트")
    class FindByCriteriaTest {

        @Test
        @DisplayName("검색 결과를 도메인 객체 리스트로 변환하여 반환합니다")
        void shouldReturnDomainList() {
            // given
            BoardSearchCriteria criteria = BoardSearchCriteria.defaultOf();
            LegacyBoardEntity entity = LegacyBoardEntityFixtures.builder().build();
            Board domain = mock(Board.class);

            given(queryDslRepository.findByCriteria(criteria)).willReturn(List.of(entity));
            given(mapper.toDomain(entity)).willReturn(domain);

            // when
            List<Board> result = adapter.findByCriteria(criteria);

            // then
            assertThat(result).hasSize(1);
            assertThat(result.get(0)).isEqualTo(domain);
            then(queryDslRepository).should(times(1)).findByCriteria(criteria);
            then(mapper).should(times(1)).toDomain(entity);
        }

        @Test
        @DisplayName("검색 결과가 없으면 빈 리스트를 반환합니다")
        void shouldReturnEmptyListWhenNoResults() {
            // given
            BoardSearchCriteria criteria = BoardSearchCriteria.defaultOf();
            given(queryDslRepository.findByCriteria(criteria)).willReturn(Collections.emptyList());

            // when
            List<Board> result = adapter.findByCriteria(criteria);

            // then
            assertThat(result).isEmpty();
            then(queryDslRepository).should(times(1)).findByCriteria(criteria);
        }

        @Test
        @DisplayName("여러 엔티티를 도메인으로 변환합니다")
        void shouldConvertMultipleEntitiesToDomains() {
            // given
            BoardSearchCriteria criteria = BoardSearchCriteria.defaultOf();
            LegacyBoardEntity entity1 = LegacyBoardEntityFixtures.builder().id(1L).build();
            LegacyBoardEntity entity2 = LegacyBoardEntityFixtures.builder().id(2L).build();
            LegacyBoardEntity entity3 = LegacyBoardEntityFixtures.builder().id(3L).build();

            Board domain1 = mock(Board.class);
            Board domain2 = mock(Board.class);
            Board domain3 = mock(Board.class);

            given(queryDslRepository.findByCriteria(criteria))
                    .willReturn(List.of(entity1, entity2, entity3));
            given(mapper.toDomain(entity1)).willReturn(domain1);
            given(mapper.toDomain(entity2)).willReturn(domain2);
            given(mapper.toDomain(entity3)).willReturn(domain3);

            // when
            List<Board> result = adapter.findByCriteria(criteria);

            // then
            assertThat(result).hasSize(3);
            assertThat(result).containsExactly(domain1, domain2, domain3);
        }
    }

    @Nested
    @DisplayName("countByCriteria 메서드 테스트")
    class CountByCriteriaTest {

        @Test
        @DisplayName("검색 조건에 맞는 게시글 수를 반환합니다")
        void shouldReturnCount() {
            // given
            BoardSearchCriteria criteria = BoardSearchCriteria.defaultOf();
            given(queryDslRepository.countByCriteria(criteria)).willReturn(5L);

            // when
            long count = adapter.countByCriteria(criteria);

            // then
            assertThat(count).isEqualTo(5L);
            then(queryDslRepository).should(times(1)).countByCriteria(criteria);
        }

        @Test
        @DisplayName("검색 결과가 없으면 0을 반환합니다")
        void shouldReturnZeroWhenNoResults() {
            // given
            BoardSearchCriteria criteria = BoardSearchCriteria.defaultOf();
            given(queryDslRepository.countByCriteria(criteria)).willReturn(0L);

            // when
            long count = adapter.countByCriteria(criteria);

            // then
            assertThat(count).isZero();
        }

        @Test
        @DisplayName("다양한 카운트 값을 올바르게 반환합니다")
        void shouldReturnVariousCounts() {
            // given
            BoardSearchCriteria criteria = BoardSearchCriteria.defaultOf();

            // when & then
            given(queryDslRepository.countByCriteria(criteria)).willReturn(1L);
            assertThat(adapter.countByCriteria(criteria)).isEqualTo(1L);

            given(queryDslRepository.countByCriteria(criteria)).willReturn(100L);
            assertThat(adapter.countByCriteria(criteria)).isEqualTo(100L);

            given(queryDslRepository.countByCriteria(criteria)).willReturn(999L);
            assertThat(adapter.countByCriteria(criteria)).isEqualTo(999L);
        }
    }
}
