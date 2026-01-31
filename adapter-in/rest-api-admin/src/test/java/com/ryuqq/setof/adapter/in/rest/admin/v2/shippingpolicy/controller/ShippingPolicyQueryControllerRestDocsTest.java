package com.ryuqq.setof.adapter.in.rest.admin.v2.shippingpolicy.controller;

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
import com.ryuqq.setof.adapter.in.rest.admin.shippingpolicy.ShippingPolicyApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v2.shippingpolicy.dto.response.ShippingPolicyApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.shippingpolicy.mapper.ShippingPolicyQueryApiMapper;
import com.ryuqq.setof.application.shippingpolicy.dto.response.ShippingPolicyPageResult;
import com.ryuqq.setof.application.shippingpolicy.port.in.query.SearchShippingPolicyUseCase;
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
 * ShippingPolicyQueryController REST Docs 테스트.
 *
 * <p>배송정책 Query API의 REST Docs 스니펫을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("ShippingPolicyQueryController REST Docs 테스트")
@WebMvcTest(ShippingPolicyQueryController.class)
@WithMockUser
class ShippingPolicyQueryControllerRestDocsTest extends RestDocsTestSupport {

    @MockBean private SearchShippingPolicyUseCase searchShippingPolicyUseCase;

    @MockBean private ShippingPolicyQueryApiMapper mapper;

    private static final Long SELLER_ID = 1L;

    @Nested
    @DisplayName("배송정책 목록 조회 API")
    class SearchShippingPoliciesTest {

        @Test
        @DisplayName("배송정책 목록 조회 성공")
        void searchShippingPolicies_Success() throws Exception {
            // given
            ShippingPolicyPageResult pageResult = ShippingPolicyApiFixtures.pageResult();
            PageApiResponse<ShippingPolicyApiResponse> response = createPageResponse();

            given(searchShippingPolicyUseCase.execute(any())).willReturn(pageResult);
            given(mapper.toSearchParams(any(), any())).willReturn(null);
            given(mapper.toPageResponse(any())).willReturn(response);

            // when & then
            mockMvc.perform(
                            get("/api/v2/sellers/{sellerId}/shipping-policies", SELLER_ID)
                                    .param("sortKey", "CREATED_AT")
                                    .param("sortDirection", "DESC")
                                    .param("page", "0")
                                    .param("size", "20"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.content").isArray())
                    .andDo(
                            document.document(
                                    pathParameters(
                                            parameterWithName("sellerId").description("셀러 ID")),
                                    queryParameters(
                                            parameterWithName("sortKey")
                                                    .description(
                                                            "정렬 키 +\n"
                                                                    + "CREATED_AT: 생성일 (기본값), "
                                                                    + "POLICY_NAME: 정책명")
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
                                                    .description("배송정책 목록"),
                                            fieldWithPath("data.content[].policyId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("정책 ID"),
                                            fieldWithPath("data.content[].policyName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("정책명"),
                                            fieldWithPath("data.content[].defaultPolicy")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("기본 정책 여부"),
                                            fieldWithPath("data.content[].active")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("활성화 상태"),
                                            fieldWithPath("data.content[].shippingFeeType")
                                                    .type(JsonFieldType.STRING)
                                                    .description("배송비 유형 코드"),
                                            fieldWithPath(
                                                            "data.content[].shippingFeeTypeDisplayName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("배송비 유형 표시명"),
                                            fieldWithPath("data.content[].baseFee")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("기본 배송비 (원)"),
                                            fieldWithPath("data.content[].freeThreshold")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("무료배송 기준금액 (원)"),
                                            fieldWithPath("data.content[].createdAt")
                                                    .type(JsonFieldType.STRING)
                                                    .description("생성일시 (ISO 8601)"),
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
        @DisplayName("배송정책 빈 목록 조회 성공")
        void searchShippingPolicies_Empty_Success() throws Exception {
            // given
            ShippingPolicyPageResult emptyResult = ShippingPolicyApiFixtures.emptyPageResult();
            PageApiResponse<ShippingPolicyApiResponse> emptyResponse =
                    PageApiResponse.of(List.of(), 0, 20, 0);

            given(searchShippingPolicyUseCase.execute(any())).willReturn(emptyResult);
            given(mapper.toSearchParams(any(), any())).willReturn(null);
            given(mapper.toPageResponse(any())).willReturn(emptyResponse);

            // when & then
            mockMvc.perform(
                            get("/api/v2/sellers/{sellerId}/shipping-policies", SELLER_ID)
                                    .param("page", "0")
                                    .param("size", "20"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.content").isEmpty());
        }
    }

    private PageApiResponse<ShippingPolicyApiResponse> createPageResponse() {
        ShippingPolicyApiResponse policyResponse =
                new ShippingPolicyApiResponse(
                        200L,
                        "기본 배송정책",
                        true,
                        true,
                        "CONDITIONAL_FREE",
                        "조건부 무료배송",
                        3000L,
                        50000L,
                        "2025-01-26T10:30:00+09:00");

        return PageApiResponse.of(List.of(policyResponse), 0, 20, 1);
    }
}
