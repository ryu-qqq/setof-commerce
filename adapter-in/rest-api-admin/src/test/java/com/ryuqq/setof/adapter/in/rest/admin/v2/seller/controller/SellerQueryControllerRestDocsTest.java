package com.ryuqq.setof.adapter.in.rest.admin.v2.seller.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ryuqq.setof.adapter.in.rest.admin.common.RestDocsTestSupport;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.PageApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.seller.SellerApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.response.SellerApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.response.SellerDetailApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.mapper.SellerQueryApiMapper;
import com.ryuqq.setof.application.seller.dto.composite.SellerCompositeResult;
import com.ryuqq.setof.application.seller.dto.response.SellerPageResult;
import com.ryuqq.setof.application.seller.port.in.query.GetSellerForAdminUseCase;
import com.ryuqq.setof.application.seller.port.in.query.SearchSellerByOffsetUseCase;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

/**
 * SellerQueryController REST Docs 테스트.
 *
 * <p>셀러 Query API의 REST Docs 스니펫을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("SellerQueryController REST Docs 테스트")
@WebMvcTest(SellerQueryController.class)
@WithMockUser
class SellerQueryControllerRestDocsTest extends RestDocsTestSupport {

    @MockBean private GetSellerForAdminUseCase getSellerForAdminUseCase;

    @MockBean private SearchSellerByOffsetUseCase searchSellerByOffsetUseCase;

    @MockBean private SellerQueryApiMapper mapper;

    private static final Long SELLER_ID = SellerApiFixtures.DEFAULT_SELLER_ID;

    @Nested
    @DisplayName("셀러 상세 조회 API")
    class GetSellerTest {

        @Test
        @DisplayName("셀러 상세 조회 성공")
        void getSeller_Success() throws Exception {
            // given
            SellerCompositeResult compositeResult = SellerApiFixtures.sellerCompositeResult();
            SellerDetailApiResponse detailResponse = SellerApiFixtures.sellerDetailApiResponse();

            given(getSellerForAdminUseCase.execute(any())).willReturn(compositeResult);
            given(mapper.toDetailResponse(any())).willReturn(detailResponse);

            // when & then
            mockMvc.perform(get("/api/v2/sellers/{sellerId}", SELLER_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.sellerInfo.id").value(SELLER_ID))
                    .andExpect(
                            jsonPath("$.data.sellerInfo.sellerName")
                                    .value(SellerApiFixtures.DEFAULT_SELLER_NAME))
                    .andDo(
                            document.document(
                                    pathParameters(
                                            parameterWithName("sellerId").description("셀러 ID")),
                                    responseFields(
                                            fieldWithPath("data.sellerInfo")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("셀러 기본 정보"),
                                            fieldWithPath("data.sellerInfo.id")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("셀러 ID"),
                                            fieldWithPath("data.sellerInfo.sellerName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("셀러명"),
                                            fieldWithPath("data.sellerInfo.displayName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("표시명"),
                                            fieldWithPath("data.sellerInfo.logoUrl")
                                                    .type(JsonFieldType.STRING)
                                                    .description("로고 URL"),
                                            fieldWithPath("data.sellerInfo.description")
                                                    .type(JsonFieldType.STRING)
                                                    .description("설명"),
                                            fieldWithPath("data.sellerInfo.active")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("활성화 여부"),
                                            fieldWithPath("data.sellerInfo.createdAt")
                                                    .type(JsonFieldType.STRING)
                                                    .description("생성일시 (ISO 8601)"),
                                            fieldWithPath("data.sellerInfo.updatedAt")
                                                    .type(JsonFieldType.STRING)
                                                    .description("수정일시 (ISO 8601)"),
                                            fieldWithPath("data.businessInfo")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("사업자 정보"),
                                            fieldWithPath("data.businessInfo.id")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("사업자 정보 ID"),
                                            fieldWithPath("data.businessInfo.registrationNumber")
                                                    .type(JsonFieldType.STRING)
                                                    .description("사업자등록번호"),
                                            fieldWithPath("data.businessInfo.companyName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("회사명"),
                                            fieldWithPath("data.businessInfo.representative")
                                                    .type(JsonFieldType.STRING)
                                                    .description("대표자명"),
                                            fieldWithPath("data.businessInfo.saleReportNumber")
                                                    .type(JsonFieldType.STRING)
                                                    .description("통신판매업 신고번호"),
                                            fieldWithPath("data.businessInfo.zipCode")
                                                    .type(JsonFieldType.STRING)
                                                    .description("우편번호"),
                                            fieldWithPath("data.businessInfo.address")
                                                    .type(JsonFieldType.STRING)
                                                    .description("주소"),
                                            fieldWithPath("data.businessInfo.addressDetail")
                                                    .type(JsonFieldType.STRING)
                                                    .description("상세주소"),
                                            fieldWithPath("data.csInfo")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("CS 정보"),
                                            fieldWithPath("data.csInfo.id")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("CS 정보 ID"),
                                            fieldWithPath("data.csInfo.csPhone")
                                                    .type(JsonFieldType.STRING)
                                                    .description("CS 전화번호"),
                                            fieldWithPath("data.csInfo.csMobile")
                                                    .type(JsonFieldType.STRING)
                                                    .description("CS 휴대폰"),
                                            fieldWithPath("data.csInfo.csEmail")
                                                    .type(JsonFieldType.STRING)
                                                    .description("CS 이메일"),
                                            fieldWithPath("data.csInfo.operatingStartTime")
                                                    .type(JsonFieldType.STRING)
                                                    .description("운영 시작 시간"),
                                            fieldWithPath("data.csInfo.operatingEndTime")
                                                    .type(JsonFieldType.STRING)
                                                    .description("운영 종료 시간"),
                                            fieldWithPath("data.csInfo.operatingDays")
                                                    .type(JsonFieldType.STRING)
                                                    .description("운영 요일"),
                                            fieldWithPath("data.csInfo.kakaoChannelUrl")
                                                    .type(JsonFieldType.STRING)
                                                    .description("카카오 채널 URL"),
                                            fieldWithPath("timestamp")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 시각")
                                                    .optional(),
                                            fieldWithPath("requestId")
                                                    .type(JsonFieldType.STRING)
                                                    .description("요청 추적 ID")
                                                    .optional())));
        }
    }

    @Nested
    @DisplayName("셀러 목록 검색 API")
    class SearchSellersTest {

        @Test
        @DisplayName("셀러 목록 조회 성공")
        void searchSellers_Success() throws Exception {
            // given
            SellerPageResult pageResult = SellerApiFixtures.sellerPageResult();
            PageApiResponse<SellerApiResponse> response = createPageResponse();

            given(searchSellerByOffsetUseCase.execute(any())).willReturn(pageResult);
            given(mapper.toSearchParams(any())).willReturn(null);
            given(mapper.toPageResponse(any())).willReturn(response);

            // when & then
            mockMvc.perform(
                            get("/api/v2/sellers")
                                    .param("active", "true")
                                    .param("searchField", "sellerName")
                                    .param("searchWord", "테스트")
                                    .param("sortKey", "CREATED_AT")
                                    .param("sortDirection", "DESC")
                                    .param("page", "0")
                                    .param("size", "20"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.content").isArray())
                    .andDo(
                            document.document(
                                    queryParameters(
                                            parameterWithName("active")
                                                    .description(
                                                            "활성화 여부 필터 +\n"
                                                                    + "true: 활성만, false: 비활성만 +\n"
                                                                    + "미입력 시 전체 조회")
                                                    .optional(),
                                            parameterWithName("searchField")
                                                    .description("검색 필드 (sellerName 등)")
                                                    .optional(),
                                            parameterWithName("searchWord")
                                                    .description("검색어")
                                                    .optional(),
                                            parameterWithName("sortKey")
                                                    .description(
                                                            "정렬 키 +\n"
                                                                    + "CREATED_AT: 생성일 (기본값), "
                                                                    + "SELLER_NAME: 셀러명")
                                                    .optional(),
                                            parameterWithName("sortDirection")
                                                    .description(
                                                            "정렬 방향 +\n"
                                                                    + "DESC: 내림차순 (기본값), "
                                                                    + "ASC: 오름차순")
                                                    .optional(),
                                            parameterWithName("page")
                                                    .description("페이지 번호 [0 이상] +\n" + "기본값: 0")
                                                    .optional(),
                                            parameterWithName("size")
                                                    .description("페이지 크기 [1~100] +\n" + "기본값: 20")
                                                    .optional()),
                                    responseFields(
                                            fieldWithPath("data.content[]")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("셀러 목록"),
                                            fieldWithPath("data.content[].id")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("셀러 ID"),
                                            fieldWithPath("data.content[].sellerName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("셀러명"),
                                            fieldWithPath("data.content[].displayName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("표시명"),
                                            fieldWithPath("data.content[].logoUrl")
                                                    .type(JsonFieldType.STRING)
                                                    .description("로고 URL"),
                                            fieldWithPath("data.content[].description")
                                                    .type(JsonFieldType.STRING)
                                                    .description("설명"),
                                            fieldWithPath("data.content[].active")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("활성화 여부"),
                                            fieldWithPath("data.content[].createdAt")
                                                    .type(JsonFieldType.STRING)
                                                    .description("생성일시 (ISO 8601)"),
                                            fieldWithPath("data.content[].updatedAt")
                                                    .type(JsonFieldType.STRING)
                                                    .description("수정일시 (ISO 8601)"),
                                            fieldWithPath("data.page")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("현재 페이지 번호"),
                                            fieldWithPath("data.size")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("페이지 크기"),
                                            fieldWithPath("data.totalElements")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("전체 요소 수"),
                                            fieldWithPath("data.totalPages")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("전체 페이지 수"),
                                            fieldWithPath("data.first")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("첫 페이지 여부"),
                                            fieldWithPath("data.last")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("마지막 페이지 여부"),
                                            fieldWithPath("timestamp")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 시각")
                                                    .optional(),
                                            fieldWithPath("requestId")
                                                    .type(JsonFieldType.STRING)
                                                    .description("요청 추적 ID")
                                                    .optional())));
        }

        @Test
        @DisplayName("셀러 빈 목록 조회 성공")
        void searchSellers_Empty_Success() throws Exception {
            // given
            SellerPageResult emptyResult = SellerApiFixtures.emptySellerPageResult();
            PageApiResponse<SellerApiResponse> emptyResponse =
                    PageApiResponse.of(List.of(), 0, 20, 0);

            given(searchSellerByOffsetUseCase.execute(any())).willReturn(emptyResult);
            given(mapper.toSearchParams(any())).willReturn(null);
            given(mapper.toPageResponse(any())).willReturn(emptyResponse);

            // when & then
            mockMvc.perform(get("/api/v2/sellers").param("page", "0").param("size", "20"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.content").isEmpty());
        }
    }

    private PageApiResponse<SellerApiResponse> createPageResponse() {
        SellerApiResponse sellerApiResponse = SellerApiFixtures.sellerApiResponse();
        return PageApiResponse.of(List.of(sellerApiResponse), 0, 20, 1);
    }
}
