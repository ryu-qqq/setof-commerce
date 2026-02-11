package com.ryuqq.setof.adapter.in.rest.v1.board.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ryuqq.setof.adapter.in.rest.common.RestDocsTestSupport;
import com.ryuqq.setof.adapter.in.rest.v1.board.BoardApiFixtures;
import com.ryuqq.setof.adapter.in.rest.v1.board.BoardV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.v1.board.dto.response.BoardV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.board.mapper.BoardV1ApiMapper;
import com.ryuqq.setof.adapter.in.rest.v1.common.dto.CustomPageableV1ApiResponse;
import com.ryuqq.setof.application.board.dto.response.BoardPageResult;
import com.ryuqq.setof.application.board.port.in.query.SearchBoardByOffsetUseCase;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

/**
 * BoardQueryV1Controller REST Docs 테스트.
 *
 * <p>공지사항 Query API의 REST Docs 스니펫을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("BoardQueryV1Controller REST Docs 테스트")
@WebMvcTest(BoardQueryV1Controller.class)
@WithMockUser
@Import(com.ryuqq.setof.adapter.in.rest.TestConfiguration.class)
class BoardQueryV1ControllerRestDocsTest extends RestDocsTestSupport {

    @MockBean private SearchBoardByOffsetUseCase searchBoardByOffsetUseCase;

    @MockBean private BoardV1ApiMapper mapper;

    @Nested
    @DisplayName("공지사항 목록 조회 API")
    class GetBoardsTest {

        @Test
        @DisplayName("공지사항 목록 조회 성공")
        void getBoards_Success() throws Exception {
            // given
            BoardPageResult pageResult = BoardApiFixtures.boardPageResult();
            CustomPageableV1ApiResponse<BoardV1ApiResponse> response =
                    CustomPageableV1ApiResponse.of(BoardApiFixtures.boardResponseList(), 0, 20, 2L);

            given(searchBoardByOffsetUseCase.execute(any())).willReturn(pageResult);
            given(mapper.toSearchParams(any())).willReturn(null);
            given(mapper.toPageResponse(pageResult)).willReturn(response);

            // when & then
            mockMvc.perform(get(BoardV1Endpoints.BOARDS).param("page", "0").param("size", "20"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.content").isArray())
                    .andExpect(jsonPath("$.data.content.length()").value(2))
                    .andExpect(jsonPath("$.data.totalElements").value(2))
                    .andExpect(jsonPath("$.response.status").value(200))
                    .andDo(
                            document.document(
                                    queryParameters(
                                            parameterWithName("page")
                                                    .description("페이지 번호 (0부터 시작)")
                                                    .optional(),
                                            parameterWithName("size")
                                                    .description("페이지 크기")
                                                    .optional()),
                                    responseFields(
                                            fieldWithPath("data.content[]")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("공지사항 목록"),
                                            fieldWithPath("data.content[].boardId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("공지사항 ID"),
                                            fieldWithPath("data.content[].title")
                                                    .type(JsonFieldType.STRING)
                                                    .description("공지사항 제목"),
                                            fieldWithPath("data.content[].contents")
                                                    .type(JsonFieldType.STRING)
                                                    .description("공지사항 내용"),
                                            fieldWithPath("data.pageable")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("페이지 정보"),
                                            fieldWithPath("data.pageable.pageNumber")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("현재 페이지 번호")
                                                    .optional(),
                                            fieldWithPath("data.pageable.pageSize")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("페이지 크기")
                                                    .optional(),
                                            fieldWithPath("data.totalElements")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("전체 데이터 개수"),
                                            fieldWithPath("data.totalPages")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("전체 페이지 수"),
                                            fieldWithPath("data.last")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("마지막 페이지 여부"),
                                            fieldWithPath("data.first")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("첫 페이지 여부"),
                                            fieldWithPath("data.numberOfElements")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("현재 페이지 데이터 개수"),
                                            fieldWithPath("data.size")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("페이지 크기"),
                                            fieldWithPath("data.number")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("현재 페이지 번호"),
                                            fieldWithPath("data.sort")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("정렬 정보"),
                                            fieldWithPath("data.empty")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("빈 페이지 여부"),
                                            fieldWithPath("response.status")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("HTTP 상태 코드"),
                                            fieldWithPath("response.message")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 메시지"))));
        }

        @Test
        @DisplayName("공지사항 빈 목록 조회 성공")
        void getBoards_Empty_Success() throws Exception {
            // given
            BoardPageResult pageResult = BoardApiFixtures.boardPageResultEmpty();
            CustomPageableV1ApiResponse<BoardV1ApiResponse> emptyResponse =
                    CustomPageableV1ApiResponse.of(List.of(), 0, 20, 0L);

            given(searchBoardByOffsetUseCase.execute(any())).willReturn(pageResult);
            given(mapper.toSearchParams(any())).willReturn(null);
            given(mapper.toPageResponse(pageResult)).willReturn(emptyResponse);

            // when & then
            mockMvc.perform(get(BoardV1Endpoints.BOARDS))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.content").isArray())
                    .andExpect(jsonPath("$.data.content.length()").value(0))
                    .andExpect(jsonPath("$.data.totalElements").value(0));
        }
    }
}
