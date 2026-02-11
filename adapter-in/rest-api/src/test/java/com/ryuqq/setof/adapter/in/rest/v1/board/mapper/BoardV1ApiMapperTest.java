package com.ryuqq.setof.adapter.in.rest.v1.board.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.v1.board.BoardApiFixtures;
import com.ryuqq.setof.adapter.in.rest.v1.board.dto.request.SearchBoardsV1ApiRequest;
import com.ryuqq.setof.application.board.dto.query.BoardSearchParams;
import com.ryuqq.setof.application.board.dto.response.BoardPageResult;
import com.ryuqq.setof.application.board.dto.response.BoardResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * BoardV1ApiMapper 단위 테스트.
 *
 * <p>Query API Mapper의 변환 로직을 테스트합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("BoardV1ApiMapper 단위 테스트")
class BoardV1ApiMapperTest {

    private BoardV1ApiMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new BoardV1ApiMapper();
    }

    @Nested
    @DisplayName("toSearchParams")
    class ToSearchParamsTest {

        @Test
        @DisplayName("검색 요청을 BoardSearchParams로 변환한다")
        void toSearchParams_Success() {
            // given
            SearchBoardsV1ApiRequest request = BoardApiFixtures.searchRequest();

            // when
            BoardSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.page()).isEqualTo(0);
            assertThat(params.size()).isEqualTo(20);
            assertThat(params.sortKey()).isEqualTo("createdAt");
            assertThat(params.sortDirection()).isEqualTo("DESC");
        }

        @Test
        @DisplayName("page/size가 null이면 기본값이 적용된다")
        void toSearchParams_NullPageSize() {
            // given
            SearchBoardsV1ApiRequest request = BoardApiFixtures.searchRequestNullPageSize();

            // when
            BoardSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.page()).isEqualTo(0);
            assertThat(params.size()).isEqualTo(20);
        }

        @Test
        @DisplayName("page/size가 지정되면 해당 값으로 변환한다")
        void toSearchParams_CustomPageSize() {
            // given
            SearchBoardsV1ApiRequest request = BoardApiFixtures.searchRequest(1, 10);

            // when
            BoardSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.page()).isEqualTo(1);
            assertThat(params.size()).isEqualTo(10);
        }
    }

    @Nested
    @DisplayName("toPageResponse")
    class ToPageResponseTest {

        @Test
        @DisplayName("BoardPageResult를 CustomPageableV1ApiResponse로 변환한다")
        void toPageResponse_Success() {
            // given
            BoardPageResult pageResult = BoardApiFixtures.boardPageResult();

            // when
            var response = mapper.toPageResponse(pageResult);

            // then
            assertThat(response.content()).hasSize(2);
            assertThat(response.content().get(0).boardId()).isEqualTo(1L);
            assertThat(response.content().get(0).title()).isEqualTo("공지사항 제목입니다");
            assertThat(response.content().get(1).boardId()).isEqualTo(2L);
            assertThat(response.number()).isEqualTo(0);
            assertThat(response.size()).isEqualTo(20);
            assertThat(response.totalElements()).isEqualTo(2L);
        }

        @Test
        @DisplayName("빈 페이지 결과를 빈 응답으로 변환한다")
        void toPageResponse_Empty() {
            // given
            BoardPageResult pageResult = BoardApiFixtures.boardPageResultEmpty();

            // when
            var response = mapper.toPageResponse(pageResult);

            // then
            assertThat(response.content()).isEmpty();
            assertThat(response.totalElements()).isEqualTo(0L);
        }
    }

    @Nested
    @DisplayName("toResponse")
    class ToResponseTest {

        @Test
        @DisplayName("BoardResult를 BoardV1ApiResponse로 변환한다")
        void toResponse_Success() {
            // given
            BoardResult result = BoardApiFixtures.boardResult(1L);

            // when
            var response = mapper.toResponse(result);

            // then
            assertThat(response.boardId()).isEqualTo(1L);
            assertThat(response.title()).isEqualTo("공지사항 제목입니다");
            assertThat(response.contents()).isEqualTo("공지사항 내용입니다");
        }

        @Test
        @DisplayName("커스텀 제목/내용으로 변환한다")
        void toResponse_CustomContent() {
            // given
            BoardResult result = BoardApiFixtures.boardResult(2L, "두 번째 공지", "두 번째 공지 내용입니다");

            // when
            var response = mapper.toResponse(result);

            // then
            assertThat(response.boardId()).isEqualTo(2L);
            assertThat(response.title()).isEqualTo("두 번째 공지");
            assertThat(response.contents()).isEqualTo("두 번째 공지 내용입니다");
        }
    }
}
