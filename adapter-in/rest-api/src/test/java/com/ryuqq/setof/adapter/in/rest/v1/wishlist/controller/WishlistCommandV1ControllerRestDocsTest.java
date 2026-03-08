package com.ryuqq.setof.adapter.in.rest.v1.wishlist.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ryuqq.setof.adapter.in.rest.common.RestDocsTestSupport;
import com.ryuqq.setof.adapter.in.rest.v1.wishlist.WishlistApiFixtures;
import com.ryuqq.setof.adapter.in.rest.v1.wishlist.WishlistV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.v1.wishlist.dto.request.AddWishlistItemV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.wishlist.mapper.WishlistV1ApiMapper;
import com.ryuqq.setof.application.wishlist.dto.command.AddWishlistItemCommand;
import com.ryuqq.setof.application.wishlist.dto.command.DeleteWishlistItemCommand;
import com.ryuqq.setof.application.wishlist.port.in.command.AddWishlistItemUseCase;
import com.ryuqq.setof.application.wishlist.port.in.command.DeleteWishlistItemUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

/**
 * WishlistCommandV1Controller REST Docs 테스트.
 *
 * <p>찜 Command API의 REST Docs 스니펫을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("WishlistCommandV1Controller REST Docs 테스트")
@WebMvcTest(WishlistCommandV1Controller.class)
@WithMockUser(username = "1", authorities = "NORMAL_GRADE")
@Import(com.ryuqq.setof.adapter.in.rest.TestConfiguration.class)
class WishlistCommandV1ControllerRestDocsTest extends RestDocsTestSupport {

    @MockBean private AddWishlistItemUseCase addWishlistItemUseCase;

    @MockBean private DeleteWishlistItemUseCase deleteWishlistItemUseCase;

    @MockBean private WishlistV1ApiMapper mapper;

    @Nested
    @DisplayName("찜 추가 API")
    class AddFavoriteTest {

        @Test
        @DisplayName("찜 추가 성공")
        void addFavorite_Success() throws Exception {
            // given
            Long newWishlistItemId = 1L;
            AddWishlistItemV1ApiRequest request = WishlistApiFixtures.addRequest();
            AddWishlistItemCommand command = WishlistApiFixtures.addCommand(1L);

            given(mapper.toAddCommand(any(), any())).willReturn(command);
            given(addWishlistItemUseCase.execute(command)).willReturn(newWishlistItemId);

            // when & then
            mockMvc.perform(
                            post(WishlistV1Endpoints.MY_FAVORITE)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(toJson(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").value(newWishlistItemId))
                    .andExpect(jsonPath("$.response.status").value(200))
                    .andDo(
                            document.document(
                                    requestFields(
                                            fieldWithPath("productGroupId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("찜할 상품 그룹 ID")),
                                    responseFields(
                                            fieldWithPath("data")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("등록된 찜 항목 ID"),
                                            fieldWithPath("response.status")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("HTTP 상태 코드"),
                                            fieldWithPath("response.message")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 메시지"))));
        }
    }

    @Nested
    @DisplayName("찜 삭제 API")
    class DeleteFavoriteTest {

        @Test
        @DisplayName("찜 삭제 성공")
        void deleteFavorite_Success() throws Exception {
            // given
            long productGroupId = 1001L;
            DeleteWishlistItemCommand command =
                    WishlistApiFixtures.deleteCommand(1L, productGroupId);

            given(mapper.toDeleteCommand(any(), any(long.class))).willReturn(command);
            willDoNothing().given(deleteWishlistItemUseCase).execute(command);

            // when & then
            mockMvc.perform(
                            delete(
                                    WishlistV1Endpoints.MY_FAVORITE_BY_PRODUCT_GROUP_ID,
                                    productGroupId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").value(productGroupId))
                    .andExpect(jsonPath("$.response.status").value(200))
                    .andDo(
                            document.document(
                                    pathParameters(
                                            parameterWithName(
                                                            WishlistV1Endpoints
                                                                    .PATH_PRODUCT_GROUP_ID)
                                                    .description("삭제할 상품 그룹 ID")),
                                    responseFields(
                                            fieldWithPath("data")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("삭제된 상품 그룹 ID"),
                                            fieldWithPath("response.status")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("HTTP 상태 코드"),
                                            fieldWithPath("response.message")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 메시지"))));
        }
    }
}
