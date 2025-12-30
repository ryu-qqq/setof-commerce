package com.ryuqq.setof.adapter.in.rest.admin.v2.shippingpolicy.controller;

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
import com.ryuqq.setof.adapter.in.rest.admin.v2.shippingpolicy.mapper.ShippingPolicyAdminV2ApiMapper;
import com.ryuqq.setof.application.shippingpolicy.dto.query.ShippingPolicySearchQuery;
import com.ryuqq.setof.application.shippingpolicy.dto.response.ShippingPolicyResponse;
import com.ryuqq.setof.application.shippingpolicy.port.in.query.GetShippingPoliciesUseCase;
import com.ryuqq.setof.application.shippingpolicy.port.in.query.GetShippingPolicyUseCase;
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
 * ShippingPolicyAdminQueryController REST Docs 테스트
 *
 * <p>배송 정책 조회 API 문서 생성을 위한 테스트 (CQRS Query 분리)
 *
 * @author development-team
 * @since 2.0.0
 */
@WebMvcTest(controllers = ShippingPolicyAdminQueryController.class)
@Import({
    ShippingPolicyAdminQueryController.class,
    ShippingPolicyAdminQueryControllerDocsTest.TestSecurityConfig.class
})
@DisplayName("ShippingPolicyAdminQueryController REST Docs")
class ShippingPolicyAdminQueryControllerDocsTest extends RestDocsTestSupport {

    private static final String BASE_URL = "/api/v2/admin/sellers/{sellerId}/shipping-policies";

    @MockitoBean private GetShippingPolicyUseCase getShippingPolicyUseCase;
    @MockitoBean private GetShippingPoliciesUseCase getShippingPoliciesUseCase;
    @MockitoBean private ShippingPolicyAdminV2ApiMapper mapper;
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
    @DisplayName("GET /api/v2/admin/sellers/{sellerId}/shipping-policies - 배송 정책 목록 조회 API 문서")
    void getShippingPolicies() throws Exception {
        // Given
        Long sellerId = 1L;
        List<ShippingPolicyResponse> policies =
                List.of(
                        new ShippingPolicyResponse(
                                1L, sellerId, "기본 배송 정책", 3000, 50000, "주문 후 2-3일 내 배송", true, 1),
                        new ShippingPolicyResponse(
                                2L,
                                sellerId,
                                "제주/도서산간 배송 정책",
                                5000,
                                null,
                                "주문 후 3-5일 내 배송",
                                false,
                                2));

        given(mapper.toSearchQuery(sellerId, false))
                .willReturn(new ShippingPolicySearchQuery(sellerId, false));
        given(getShippingPoliciesUseCase.execute(any(ShippingPolicySearchQuery.class)))
                .willReturn(policies);

        // When & Then
        mockMvc.perform(get(BASE_URL, sellerId).param("includeDeleted", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(
                        document(
                                "admin-shipping-policy-list",
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
                                                .description("배송 정책 목록"),
                                        fieldWithPath("data[].shippingPolicyId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("배송 정책 ID"),
                                        fieldWithPath("data[].sellerId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("셀러 ID"),
                                        fieldWithPath("data[].policyName")
                                                .type(JsonFieldType.STRING)
                                                .description("정책명"),
                                        fieldWithPath("data[].defaultDeliveryCost")
                                                .type(JsonFieldType.NUMBER)
                                                .description("기본 배송비"),
                                        fieldWithPath("data[].freeShippingThreshold")
                                                .type(JsonFieldType.NUMBER)
                                                .description("무료 배송 기준 금액")
                                                .optional(),
                                        fieldWithPath("data[].deliveryGuide")
                                                .type(JsonFieldType.STRING)
                                                .description("배송 안내")
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
            "GET /api/v2/admin/sellers/{sellerId}/shipping-policies/{shippingPolicyId} - 배송 정책 상세"
                    + " 조회 API 문서")
    void getShippingPolicy() throws Exception {
        // Given
        Long sellerId = 1L;
        Long shippingPolicyId = 1L;
        ShippingPolicyResponse response =
                new ShippingPolicyResponse(
                        shippingPolicyId,
                        sellerId,
                        "기본 배송 정책",
                        3000,
                        50000,
                        "주문 후 2-3일 내 배송",
                        true,
                        1);

        given(getShippingPolicyUseCase.execute(shippingPolicyId, sellerId)).willReturn(response);

        // When & Then
        mockMvc.perform(get(BASE_URL + "/{shippingPolicyId}", sellerId, shippingPolicyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(
                        document(
                                "admin-shipping-policy-detail",
                                pathParameters(
                                        parameterWithName("sellerId").description("셀러 ID"),
                                        parameterWithName("shippingPolicyId")
                                                .description("배송 정책 ID")),
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
                                        fieldWithPath("data.shippingPolicyId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("배송 정책 ID"),
                                        fieldWithPath("data.sellerId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("셀러 ID"),
                                        fieldWithPath("data.policyName")
                                                .type(JsonFieldType.STRING)
                                                .description("정책명"),
                                        fieldWithPath("data.defaultDeliveryCost")
                                                .type(JsonFieldType.NUMBER)
                                                .description("기본 배송비"),
                                        fieldWithPath("data.freeShippingThreshold")
                                                .type(JsonFieldType.NUMBER)
                                                .description("무료 배송 기준 금액")
                                                .optional(),
                                        fieldWithPath("data.deliveryGuide")
                                                .type(JsonFieldType.STRING)
                                                .description("배송 안내")
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
