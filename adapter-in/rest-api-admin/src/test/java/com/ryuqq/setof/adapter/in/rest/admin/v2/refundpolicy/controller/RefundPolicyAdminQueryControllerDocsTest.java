package com.ryuqq.setof.adapter.in.rest.admin.v2.refundpolicy.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ryuqq.setof.adapter.in.rest.admin.common.docs.RestDocsTestSupport;
import com.ryuqq.setof.adapter.in.rest.admin.common.error.ErrorMapperRegistry;
import com.ryuqq.setof.adapter.in.rest.admin.v2.refundpolicy.mapper.RefundPolicyAdminV2ApiMapper;
import com.ryuqq.setof.application.refundpolicy.dto.query.RefundPolicySearchQuery;
import com.ryuqq.setof.application.refundpolicy.dto.response.RefundPolicyResponse;
import com.ryuqq.setof.application.refundpolicy.port.in.query.GetRefundPoliciesUseCase;
import com.ryuqq.setof.application.refundpolicy.port.in.query.GetRefundPolicyUseCase;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * RefundPolicyAdminQueryController REST Docs 테스트
 *
 * <p>환불 정책 조회 API 문서 생성을 위한 테스트 (CQRS Query 분리)
 *
 * @author development-team
 * @since 2.0.0
 */
@WebMvcTest(controllers = RefundPolicyAdminQueryController.class)
@Import({
    RefundPolicyAdminQueryController.class,
    RefundPolicyAdminQueryControllerDocsTest.TestSecurityConfig.class
})
@DisplayName("RefundPolicyAdminQueryController REST Docs")
class RefundPolicyAdminQueryControllerDocsTest extends RestDocsTestSupport {

    private static final String BASE_URL = "/api/v2/admin/sellers/{sellerId}/refund-policies";

    @MockitoBean private GetRefundPolicyUseCase getRefundPolicyUseCase;
    @MockitoBean private GetRefundPoliciesUseCase getRefundPoliciesUseCase;
    @MockitoBean private RefundPolicyAdminV2ApiMapper mapper;
    @MockitoBean private ErrorMapperRegistry errorMapperRegistry;

    @Autowired private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUpWithSecurity(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc =
                MockMvcBuilders.webAppContextSetup(webApplicationContext)
                        .apply(springSecurity())
                        .apply(
                                documentationConfiguration(restDocumentation)
                                        .operationPreprocessors()
                                        .withRequestDefaults(prettyPrint())
                                        .withResponseDefaults(prettyPrint()))
                        .build();
    }

    @TestConfiguration
    static class TestSecurityConfig {
        @Bean
        SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
            return http.csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                    .build();
        }
    }

    @Test
    @DisplayName("GET /api/v2/admin/sellers/{sellerId}/refund-policies - 환불 정책 목록 조회 API 문서")
    void getRefundPolicies() throws Exception {
        // Given
        Long sellerId = 1L;
        List<RefundPolicyResponse> policies =
                List.of(
                        new RefundPolicyResponse(
                                1L,
                                sellerId,
                                "기본 환불 정책",
                                "서울시 강남구",
                                "역삼동 123-45",
                                "06234",
                                7,
                                3000,
                                "상품 수령 후 7일 이내 교환/환불 가능",
                                true,
                                1),
                        new RefundPolicyResponse(
                                2L,
                                sellerId,
                                "이벤트 환불 정책",
                                "서울시 서초구",
                                "서초동 456-78",
                                "06789",
                                14,
                                0,
                                "이벤트 상품은 14일 이내 무료 반품",
                                false,
                                2));

        given(mapper.toSearchQuery(sellerId, false))
                .willReturn(new RefundPolicySearchQuery(sellerId, false));
        given(getRefundPoliciesUseCase.execute(any(RefundPolicySearchQuery.class)))
                .willReturn(policies);

        // When & Then
        mockMvc.perform(get(BASE_URL, sellerId).param("includeDeleted", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(
                        document(
                                "admin-refund-policy-list",
                                pathParameters(parameterWithName("sellerId").description("셀러 ID")),
                                queryParameters(
                                        parameterWithName("includeDeleted")
                                                .description("삭제된 정책 포함 여부 (기본: false)")
                                                .optional()),
                                responseFields(
                                        fieldWithPath("success")
                                                .type(JsonFieldType.BOOLEAN)
                                                .description("요청 성공 여부"),
                                        fieldWithPath("timestamp")
                                                .type(JsonFieldType.STRING)
                                                .description("응답 시각"),
                                        fieldWithPath("requestId")
                                                .type(JsonFieldType.STRING)
                                                .description("요청 추적 ID"),
                                        fieldWithPath("data")
                                                .type(JsonFieldType.ARRAY)
                                                .description("환불 정책 목록"),
                                        fieldWithPath("data[].refundPolicyId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("환불 정책 ID"),
                                        fieldWithPath("data[].sellerId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("셀러 ID"),
                                        fieldWithPath("data[].policyName")
                                                .type(JsonFieldType.STRING)
                                                .description("정책명"),
                                        fieldWithPath("data[].returnAddressLine1")
                                                .type(JsonFieldType.STRING)
                                                .description("반품 주소 1"),
                                        fieldWithPath("data[].returnAddressLine2")
                                                .type(JsonFieldType.STRING)
                                                .description("반품 주소 2")
                                                .optional(),
                                        fieldWithPath("data[].returnZipCode")
                                                .type(JsonFieldType.STRING)
                                                .description("반품 우편번호"),
                                        fieldWithPath("data[].refundPeriodDays")
                                                .type(JsonFieldType.NUMBER)
                                                .description("환불 가능 기간 (일)"),
                                        fieldWithPath("data[].refundDeliveryCost")
                                                .type(JsonFieldType.NUMBER)
                                                .description("환불 배송비"),
                                        fieldWithPath("data[].refundGuide")
                                                .type(JsonFieldType.STRING)
                                                .description("환불 안내")
                                                .optional(),
                                        fieldWithPath("data[].isDefault")
                                                .type(JsonFieldType.BOOLEAN)
                                                .description("기본 정책 여부"),
                                        fieldWithPath("data[].displayOrder")
                                                .type(JsonFieldType.NUMBER)
                                                .description("표시 순서"),
                                        fieldWithPath("error")
                                                .type(JsonFieldType.NULL)
                                                .description("에러 정보 (성공 시 null)")
                                                .optional())));
    }

    @Test
    @DisplayName(
            "GET /api/v2/admin/sellers/{sellerId}/refund-policies/{refundPolicyId} - 환불 정책 상세 조회"
                    + " API 문서")
    void getRefundPolicy() throws Exception {
        // Given
        Long sellerId = 1L;
        Long refundPolicyId = 1L;
        RefundPolicyResponse response =
                new RefundPolicyResponse(
                        refundPolicyId,
                        sellerId,
                        "기본 환불 정책",
                        "서울시 강남구",
                        "역삼동 123-45",
                        "06234",
                        7,
                        3000,
                        "상품 수령 후 7일 이내 교환/환불 가능",
                        true,
                        1);

        given(getRefundPolicyUseCase.execute(refundPolicyId, sellerId)).willReturn(response);

        // When & Then
        mockMvc.perform(get(BASE_URL + "/{refundPolicyId}", sellerId, refundPolicyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(
                        document(
                                "admin-refund-policy-detail",
                                pathParameters(
                                        parameterWithName("sellerId").description("셀러 ID"),
                                        parameterWithName("refundPolicyId")
                                                .description("환불 정책 ID")),
                                responseFields(
                                        fieldWithPath("success")
                                                .type(JsonFieldType.BOOLEAN)
                                                .description("요청 성공 여부"),
                                        fieldWithPath("timestamp")
                                                .type(JsonFieldType.STRING)
                                                .description("응답 시각"),
                                        fieldWithPath("requestId")
                                                .type(JsonFieldType.STRING)
                                                .description("요청 추적 ID"),
                                        fieldWithPath("data.refundPolicyId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("환불 정책 ID"),
                                        fieldWithPath("data.sellerId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("셀러 ID"),
                                        fieldWithPath("data.policyName")
                                                .type(JsonFieldType.STRING)
                                                .description("정책명"),
                                        fieldWithPath("data.returnAddressLine1")
                                                .type(JsonFieldType.STRING)
                                                .description("반품 주소 1"),
                                        fieldWithPath("data.returnAddressLine2")
                                                .type(JsonFieldType.STRING)
                                                .description("반품 주소 2")
                                                .optional(),
                                        fieldWithPath("data.returnZipCode")
                                                .type(JsonFieldType.STRING)
                                                .description("반품 우편번호"),
                                        fieldWithPath("data.refundPeriodDays")
                                                .type(JsonFieldType.NUMBER)
                                                .description("환불 가능 기간 (일)"),
                                        fieldWithPath("data.refundDeliveryCost")
                                                .type(JsonFieldType.NUMBER)
                                                .description("환불 배송비"),
                                        fieldWithPath("data.refundGuide")
                                                .type(JsonFieldType.STRING)
                                                .description("환불 안내")
                                                .optional(),
                                        fieldWithPath("data.isDefault")
                                                .type(JsonFieldType.BOOLEAN)
                                                .description("기본 정책 여부"),
                                        fieldWithPath("data.displayOrder")
                                                .type(JsonFieldType.NUMBER)
                                                .description("표시 순서"),
                                        fieldWithPath("error")
                                                .type(JsonFieldType.NULL)
                                                .description("에러 정보 (성공 시 null)")
                                                .optional())));
    }
}
