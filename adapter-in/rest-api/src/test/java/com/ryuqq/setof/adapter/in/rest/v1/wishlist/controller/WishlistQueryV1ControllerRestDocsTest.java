package com.ryuqq.setof.adapter.in.rest.v1.wishlist.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ryuqq.setof.adapter.in.rest.common.RestDocsTestSupport;
import com.ryuqq.setof.adapter.in.rest.v1.wishlist.WishlistApiFixtures;
import com.ryuqq.setof.adapter.in.rest.v1.wishlist.WishlistV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.v1.wishlist.dto.response.WishlistItemSliceV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.wishlist.mapper.WishlistV1ApiMapper;
import com.ryuqq.setof.application.wishlist.dto.response.WishlistItemSliceResult;
import com.ryuqq.setof.application.wishlist.port.in.query.GetWishlistItemsUseCase;
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
 * WishlistQueryV1Controller REST Docs 테스트.
 *
 * <p>찜 Query API의 REST Docs 스니펫을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("WishlistQueryV1Controller REST Docs 테스트")
@WebMvcTest(WishlistQueryV1Controller.class)
@WithMockUser(username = "1", authorities = "NORMAL_GRADE")
@Import(com.ryuqq.setof.adapter.in.rest.TestConfiguration.class)
class WishlistQueryV1ControllerRestDocsTest extends RestDocsTestSupport {

    @MockBean private GetWishlistItemsUseCase getWishlistItemsUseCase;

    @MockBean private WishlistV1ApiMapper mapper;

    @Nested
    @DisplayName("찜 목록 조회 API")
    class GetMyFavoritesTest {

        @Test
        @DisplayName("찜 목록 조회 성공")
        void getMyFavorites_Success() throws Exception {
            // given
            WishlistItemSliceResult sliceResult = WishlistApiFixtures.wishlistItemSliceResult();
            WishlistItemSliceV1ApiResponse response =
                    WishlistApiFixtures.wishlistItemSliceResponse();

            given(getWishlistItemsUseCase.execute(any())).willReturn(sliceResult);
            given(mapper.toSearchParams(any(), any(), any(int.class))).willReturn(null);
            given(mapper.toSliceResponse(eq(sliceResult), any(int.class))).willReturn(response);

            // when & then
            mockMvc.perform(
                            get(WishlistV1Endpoints.MY_FAVORITES)
                                    .param("lastDomainId", "5")
                                    .param("size", "20"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.content").isArray())
                    .andExpect(jsonPath("$.data.content.length()").value(1))
                    .andExpect(jsonPath("$.data.last").value(false))
                    .andExpect(jsonPath("$.data.totalElements").value(42))
                    .andExpect(jsonPath("$.data.content[0].userFavoriteId").value(1L))
                    .andExpect(jsonPath("$.data.content[0].productGroupId").value(1001L))
                    .andExpect(jsonPath("$.data.content[0].productGroupName").value("나이키 에어맥스 90"))
                    .andExpect(jsonPath("$.data.content[0].brand.brandName").value("Nike"))
                    .andExpect(jsonPath("$.response.status").value(200))
                    .andDo(
                            document.document(
                                    queryParameters(
                                            parameterWithName("lastDomainId")
                                                    .description("마지막 조회된 찜 ID (커서, 첫 조회 시 생략)")
                                                    .optional(),
                                            parameterWithName("size")
                                                    .description("페이지 크기 (기본값: 20)")
                                                    .optional()),
                                    responseFields(
                                            fieldWithPath("data.content[]")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("찜 항목 목록"),
                                            fieldWithPath("data.content[].userFavoriteId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("찜 ID"),
                                            fieldWithPath("data.content[].productGroupId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("상품 그룹 ID"),
                                            fieldWithPath("data.content[].sellerId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("셀러 ID"),
                                            fieldWithPath("data.content[].productGroupName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("상품 그룹명"),
                                            fieldWithPath("data.content[].brand")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("브랜드 정보"),
                                            fieldWithPath("data.content[].brand.brandId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("브랜드 ID"),
                                            fieldWithPath("data.content[].brand.brandName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("브랜드명"),
                                            fieldWithPath("data.content[].imageUrl")
                                                    .type(JsonFieldType.STRING)
                                                    .description("상품 이미지 URL"),
                                            fieldWithPath("data.content[].price")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("가격 정보"),
                                            fieldWithPath("data.content[].price.regularPrice")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("정상가"),
                                            fieldWithPath("data.content[].price.currentPrice")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("판매가"),
                                            fieldWithPath("data.content[].price.discountRate")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("할인율 (%)"),
                                            fieldWithPath("data.content[].insertDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description("찜 등록일 (yyyy-MM-dd'T'HH:mm:ss)"),
                                            fieldWithPath("data.content[].productStatus")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("상품 상태 정보"),
                                            fieldWithPath("data.content[].productStatus.soldOut")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("품절 여부"),
                                            fieldWithPath("data.content[].productStatus.displayed")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("전시 여부"),
                                            fieldWithPath("data.last")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("마지막 페이지 여부"),
                                            fieldWithPath("data.first")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("첫 페이지 여부"),
                                            fieldWithPath("data.number")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("현재 페이지 번호"),
                                            fieldWithPath("data.sort")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("정렬 정보"),
                                            fieldWithPath("data.sort.sorted")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("정렬 여부"),
                                            fieldWithPath("data.sort.unsorted")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("미정렬 여부"),
                                            fieldWithPath("data.sort.empty")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("정렬 정보 비어있는지 여부"),
                                            fieldWithPath("data.size")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("요청 페이지 크기"),
                                            fieldWithPath("data.numberOfElements")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("현재 페이지 요소 수"),
                                            fieldWithPath("data.empty")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("컨텐츠 비어있는지 여부"),
                                            fieldWithPath("data.lastDomainId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("마지막 조회된 찜 ID (커서)"),
                                            fieldWithPath("data.cursorValue")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("커서 값"),
                                            fieldWithPath("data.totalElements")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("전체 요소 수"),
                                            fieldWithPath("response.status")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("HTTP 상태 코드"),
                                            fieldWithPath("response.message")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 메시지"))));
        }

        @Test
        @DisplayName("찜 빈 목록 조회 성공")
        void getMyFavorites_Empty_Success() throws Exception {
            // given
            WishlistItemSliceResult sliceResult = WishlistApiFixtures.emptySliceResult();
            WishlistItemSliceV1ApiResponse response = WishlistApiFixtures.emptySliceResponse();

            given(getWishlistItemsUseCase.execute(any())).willReturn(sliceResult);
            given(mapper.toSearchParams(any(), any(), any(int.class))).willReturn(null);
            given(mapper.toSliceResponse(eq(sliceResult), any(int.class))).willReturn(response);

            // when & then
            mockMvc.perform(get(WishlistV1Endpoints.MY_FAVORITES))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.content").isArray())
                    .andExpect(jsonPath("$.data.content.length()").value(0))
                    .andExpect(jsonPath("$.data.last").value(true))
                    .andExpect(jsonPath("$.data.totalElements").value(0))
                    .andExpect(jsonPath("$.response.status").value(200));
        }
    }
}
