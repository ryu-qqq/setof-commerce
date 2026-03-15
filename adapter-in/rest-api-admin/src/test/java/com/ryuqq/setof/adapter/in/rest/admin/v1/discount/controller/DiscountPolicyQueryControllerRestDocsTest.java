package com.ryuqq.setof.adapter.in.rest.admin.v1.discount.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
import com.ryuqq.setof.adapter.in.rest.admin.discount.DiscountPolicyApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.response.DiscountPolicyV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.mapper.DiscountPolicyCommandApiMapper;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.mapper.DiscountPolicyQueryApiMapper;
import com.ryuqq.setof.application.discount.dto.response.DiscountPolicyPageResult;
import com.ryuqq.setof.application.discount.dto.response.DiscountPolicyResult;
import com.ryuqq.setof.application.discount.port.in.query.GetDiscountPolicyDetailUseCase;
import com.ryuqq.setof.application.discount.port.in.query.SearchDiscountPoliciesUseCase;
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
 * DiscountPolicyQueryController REST Docs 테스트.
 *
 * <p>할인 정책 조회 v1 API의 REST Docs 스니펫을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("DiscountPolicyQueryController REST Docs 테스트")
@WebMvcTest(DiscountPolicyQueryController.class)
@WithMockUser
class DiscountPolicyQueryControllerRestDocsTest extends RestDocsTestSupport {

    @MockBean private GetDiscountPolicyDetailUseCase getDetailUseCase;

    @MockBean private SearchDiscountPoliciesUseCase searchUseCase;

    @MockBean private DiscountPolicyCommandApiMapper commandMapper;

    @MockBean private DiscountPolicyQueryApiMapper queryMapper;

    private static final Long POLICY_ID = DiscountPolicyApiFixtures.DEFAULT_DISCOUNT_POLICY_ID;

    @Nested
    @DisplayName("할인 정책 단건 조회 API")
    class GetDiscountPolicyDetailTest {

        @Test
        @DisplayName("할인 정책 단건 조회 성공")
        void getDetail_Success() throws Exception {
            // given
            DiscountPolicyResult result = DiscountPolicyApiFixtures.discountPolicyResult(POLICY_ID);
            DiscountPolicyV1ApiResponse response =
                    DiscountPolicyApiFixtures.discountPolicyApiResponse(POLICY_ID);

            given(getDetailUseCase.execute(anyLong())).willReturn(result);
            given(commandMapper.toResponse(any())).willReturn(response);

            // when & then
            mockMvc.perform(get("/api/v1/discount/{discountPolicyId}", POLICY_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.discountPolicyId").value(POLICY_ID))
                    .andExpect(
                            jsonPath("$.data.discountDetails.discountPolicyName")
                                    .value(DiscountPolicyApiFixtures.DEFAULT_POLICY_NAME))
                    .andDo(
                            document.document(
                                    pathParameters(
                                            parameterWithName("discountPolicyId")
                                                    .description("할인 정책 ID")),
                                    responseFields(
                                            fieldWithPath("data.discountPolicyId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("할인 정책 ID"),
                                            fieldWithPath("data.discountDetails")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("할인 상세 정보"),
                                            fieldWithPath("data.discountDetails.discountPolicyName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("할인 정책명"),
                                            fieldWithPath("data.discountDetails.discountType")
                                                    .type(JsonFieldType.STRING)
                                                    .description("할인 유형 (RATE: 비율, PRICE: 금액)"),
                                            fieldWithPath("data.discountDetails.publisherType")
                                                    .type(JsonFieldType.STRING)
                                                    .description("발행자 유형 (ADMIN, SELLER)"),
                                            fieldWithPath("data.discountDetails.issueType")
                                                    .type(JsonFieldType.STRING)
                                                    .description(
                                                            "적용 대상 유형 (PRODUCT, SELLER, BRAND)")
                                                    .optional(),
                                            fieldWithPath("data.discountDetails.discountLimitYn")
                                                    .type(JsonFieldType.STRING)
                                                    .description("할인 한도 적용 여부 (Y/N)"),
                                            fieldWithPath("data.discountDetails.maxDiscountPrice")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("최대 할인 금액"),
                                            fieldWithPath("data.discountDetails.shareYn")
                                                    .type(JsonFieldType.STRING)
                                                    .description("분담 여부 (Y/N)"),
                                            fieldWithPath("data.discountDetails.shareRatio")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("분담 비율 (%)"),
                                            fieldWithPath("data.discountDetails.discountRatio")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("할인율 또는 할인금액"),
                                            fieldWithPath("data.discountDetails.policyStartDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description("정책 시작일 (yyyy-MM-dd HH:mm:ss)"),
                                            fieldWithPath("data.discountDetails.policyEndDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description("정책 종료일 (yyyy-MM-dd HH:mm:ss)"),
                                            fieldWithPath("data.discountDetails.memo")
                                                    .type(JsonFieldType.STRING)
                                                    .description("메모"),
                                            fieldWithPath("data.discountDetails.priority")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("우선순위"),
                                            fieldWithPath("data.discountDetails.activeYn")
                                                    .type(JsonFieldType.STRING)
                                                    .description("활성화 여부 (Y/N)"),
                                            fieldWithPath("data.insertDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description("등록일시 (yyyy-MM-dd HH:mm:ss)"),
                                            fieldWithPath("data.updateDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description("수정일시 (yyyy-MM-dd HH:mm:ss)"),
                                            fieldWithPath("data.insertOperator")
                                                    .type(JsonFieldType.STRING)
                                                    .description("등록자"),
                                            fieldWithPath("data.updateOperator")
                                                    .type(JsonFieldType.STRING)
                                                    .description("수정자"),
                                            fieldWithPath("response.status")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("응답 상태 코드"),
                                            fieldWithPath("response.message")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 메시지"))));
        }
    }

    @Nested
    @DisplayName("할인 정책 목록 조회 API")
    class SearchDiscountPoliciesTest {

        @Test
        @DisplayName("할인 정책 목록 조회 성공")
        void search_Success() throws Exception {
            // given
            DiscountPolicyPageResult pageResult =
                    DiscountPolicyApiFixtures.discountPolicyPageResult();
            List<DiscountPolicyV1ApiResponse> responses =
                    List.of(
                            DiscountPolicyApiFixtures.discountPolicyApiResponse(1L),
                            DiscountPolicyApiFixtures.discountPolicyApiResponse(2L));

            given(queryMapper.toSearchParams(any())).willReturn(null);
            given(searchUseCase.execute(any())).willReturn(pageResult);
            given(commandMapper.toResponse(any()))
                    .willReturn(DiscountPolicyApiFixtures.discountPolicyApiResponse(1L))
                    .willReturn(DiscountPolicyApiFixtures.discountPolicyApiResponse(2L));

            // when & then
            mockMvc.perform(
                            get("/api/v1/discounts")
                                    .param("publisherType", "ADMIN")
                                    .param("activeYn", "Y")
                                    .param("page", "0")
                                    .param("size", "20")
                                    .param("sortBy", "id")
                                    .param("sortDirection", "DESC"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.items").isArray())
                    .andExpect(jsonPath("$.data.totalCount").value(2))
                    .andDo(
                            document.document(
                                    queryParameters(
                                            parameterWithName("discountPolicyId")
                                                    .description("할인 정책 ID 필터 [선택]")
                                                    .optional(),
                                            parameterWithName("periodType")
                                                    .description("기간 유형 [선택] (POLICY, INSERT 등)")
                                                    .optional(),
                                            parameterWithName("startDate")
                                                    .description("조회 시작일 [선택, yyyy-MM-dd HH:mm:ss]")
                                                    .optional(),
                                            parameterWithName("endDate")
                                                    .description("조회 종료일 [선택, yyyy-MM-dd HH:mm:ss]")
                                                    .optional(),
                                            parameterWithName("activeYn")
                                                    .description("활성화 여부 [선택, Y/N]")
                                                    .optional(),
                                            parameterWithName("publisherType")
                                                    .description("발행자 유형 [선택, ADMIN/SELLER]")
                                                    .optional(),
                                            parameterWithName("issueType")
                                                    .description(
                                                            "적용 대상 유형 [선택, PRODUCT/SELLER/BRAND]")
                                                    .optional(),
                                            parameterWithName("userId")
                                                    .description("사용자 ID 필터 [선택]")
                                                    .optional(),
                                            parameterWithName("searchKeyword")
                                                    .description("검색 키워드 유형 [선택]")
                                                    .optional(),
                                            parameterWithName("searchWord")
                                                    .description("검색어 [선택]")
                                                    .optional(),
                                            parameterWithName("page")
                                                    .description("페이지 번호 [기본값: 0]")
                                                    .optional(),
                                            parameterWithName("size")
                                                    .description("페이지 크기 [기본값: 20]")
                                                    .optional(),
                                            parameterWithName("sortBy")
                                                    .description("정렬 기준 [기본값: id]")
                                                    .optional(),
                                            parameterWithName("sortDirection")
                                                    .description("정렬 방향 [기본값: DESC, ASC/DESC]")
                                                    .optional()),
                                    responseFields(
                                            fieldWithPath("data.items")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("할인 정책 목록"),
                                            fieldWithPath("data.items[].discountPolicyId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("할인 정책 ID"),
                                            fieldWithPath("data.items[].discountDetails")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("할인 상세 정보"),
                                            fieldWithPath(
                                                            "data.items[].discountDetails.discountPolicyName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("할인 정책명"),
                                            fieldWithPath(
                                                            "data.items[].discountDetails.discountType")
                                                    .type(JsonFieldType.STRING)
                                                    .description("할인 유형 (RATE/PRICE)"),
                                            fieldWithPath(
                                                            "data.items[].discountDetails.publisherType")
                                                    .type(JsonFieldType.STRING)
                                                    .description("발행자 유형"),
                                            fieldWithPath("data.items[].discountDetails.issueType")
                                                    .type(JsonFieldType.STRING)
                                                    .description("적용 대상 유형")
                                                    .optional(),
                                            fieldWithPath(
                                                            "data.items[].discountDetails.discountLimitYn")
                                                    .type(JsonFieldType.STRING)
                                                    .description("할인 한도 적용 여부"),
                                            fieldWithPath(
                                                            "data.items[].discountDetails.maxDiscountPrice")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("최대 할인 금액"),
                                            fieldWithPath("data.items[].discountDetails.shareYn")
                                                    .type(JsonFieldType.STRING)
                                                    .description("분담 여부"),
                                            fieldWithPath("data.items[].discountDetails.shareRatio")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("분담 비율"),
                                            fieldWithPath(
                                                            "data.items[].discountDetails.discountRatio")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("할인율 또는 할인금액"),
                                            fieldWithPath(
                                                            "data.items[].discountDetails.policyStartDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description("정책 시작일"),
                                            fieldWithPath(
                                                            "data.items[].discountDetails.policyEndDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description("정책 종료일"),
                                            fieldWithPath("data.items[].discountDetails.memo")
                                                    .type(JsonFieldType.STRING)
                                                    .description("메모"),
                                            fieldWithPath("data.items[].discountDetails.priority")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("우선순위"),
                                            fieldWithPath("data.items[].discountDetails.activeYn")
                                                    .type(JsonFieldType.STRING)
                                                    .description("활성화 여부"),
                                            fieldWithPath("data.items[].insertDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description("등록일시"),
                                            fieldWithPath("data.items[].updateDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description("수정일시"),
                                            fieldWithPath("data.items[].insertOperator")
                                                    .type(JsonFieldType.STRING)
                                                    .description("등록자"),
                                            fieldWithPath("data.items[].updateOperator")
                                                    .type(JsonFieldType.STRING)
                                                    .description("수정자"),
                                            fieldWithPath("data.totalCount")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("전체 항목 수"),
                                            fieldWithPath("data.page")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("현재 페이지 번호"),
                                            fieldWithPath("data.size")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("페이지 크기"),
                                            fieldWithPath("response.status")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("응답 상태 코드"),
                                            fieldWithPath("response.message")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 메시지"))));
        }

        @Test
        @DisplayName("빈 목록 조회 성공")
        void search_Empty_Success() throws Exception {
            // given
            DiscountPolicyPageResult emptyResult =
                    DiscountPolicyApiFixtures.emptyDiscountPolicyPageResult();

            given(queryMapper.toSearchParams(any())).willReturn(null);
            given(searchUseCase.execute(any())).willReturn(emptyResult);

            // when & then
            mockMvc.perform(get("/api/v1/discounts").param("page", "0").param("size", "20"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.items").isEmpty())
                    .andExpect(jsonPath("$.data.totalCount").value(0));
        }
    }
}
