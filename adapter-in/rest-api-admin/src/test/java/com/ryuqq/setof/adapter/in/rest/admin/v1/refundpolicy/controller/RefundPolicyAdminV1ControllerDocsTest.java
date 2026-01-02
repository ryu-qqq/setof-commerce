package com.ryuqq.setof.adapter.in.rest.admin.v1.refundpolicy.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ryuqq.setof.adapter.in.rest.admin.common.docs.RestDocsTestSupport;
import com.ryuqq.setof.adapter.in.rest.admin.common.error.ErrorMapperRegistry;
import com.ryuqq.setof.adapter.in.rest.admin.v1.refundpolicy.mapper.RefundPolicyAdminV1ApiMapper;
import com.ryuqq.setof.application.refundpolicy.dto.command.UpdateRefundPolicyCommand;
import com.ryuqq.setof.application.refundpolicy.dto.response.RefundPolicyResponse;
import com.ryuqq.setof.application.refundpolicy.port.in.command.UpdateRefundPolicyUseCase;
import com.ryuqq.setof.application.refundpolicy.port.in.query.GetDefaultRefundPolicyUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * RefundPolicyAdminV1Controller REST Docs 테스트
 *
 * @author development-team
 * @since 1.0.0
 * @deprecated V2 API로 마이그레이션 권장
 */
@WebMvcTest(controllers = RefundPolicyAdminV1Controller.class)
@Import({
    RefundPolicyAdminV1Controller.class,
    RefundPolicyAdminV1ControllerDocsTest.TestSecurityConfig.class
})
@DisplayName("RefundPolicyAdminV1Controller REST Docs (Legacy)")
class RefundPolicyAdminV1ControllerDocsTest extends RestDocsTestSupport {

    private static final String BASE_URL = "/api/v1";

    @MockitoBean private GetDefaultRefundPolicyUseCase getDefaultRefundPolicyUseCase;
    @MockitoBean private RefundPolicyAdminV1ApiMapper mapper;
    @MockitoBean private UpdateRefundPolicyUseCase updateRefundPolicyUseCase;
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
    @DisplayName("PUT /api/v1/product/group/{productGroupId}/notice/refund - [Legacy] 환불 고지 수정")
    void updateProductRefundNotice() throws Exception {
        // Given
        Long productGroupId = 1L;
        String requestBody =
                """
                {
                    "returnMethodDomestic": "택배 반품",
                    "returnCourierDomestic": "CJ대한통운",
                    "returnChargeDomestic": 3000,
                    "returnExchangeAreaDomestic": "전국"
                }
                """;

        RefundPolicyResponse refundPolicyResponse =
                new RefundPolicyResponse(
                        1L, // refundPolicyId
                        0L, // sellerId
                        "기본 환불 정책", // policyName
                        "서울시 강남구 테헤란로 123", // returnAddressLine1
                        "세트오브 빌딩 1층", // returnAddressLine2
                        "06236", // returnZipCode
                        7, // refundPeriodDays
                        3000, // refundDeliveryCost
                        "환불 안내 문구", // refundGuide
                        true, // isDefault
                        1); // displayOrder

        given(getDefaultRefundPolicyUseCase.execute(anyLong())).willReturn(refundPolicyResponse);
        given(mapper.toUpdateRefundPolicyCommand(any(), any()))
                .willReturn(
                        new UpdateRefundPolicyCommand(
                                1L, // refundPolicyId
                                0L, // sellerId
                                "기본 환불 정책", // policyName
                                "서울시 강남구 테헤란로 123", // returnAddressLine1
                                "세트오브 빌딩 1층", // returnAddressLine2
                                "06236", // returnZipCode
                                7, // refundPeriodDays
                                3000, // refundDeliveryCost
                                "환불 안내 문구")); // refundGuide
        doNothing().when(updateRefundPolicyUseCase).execute(any(UpdateRefundPolicyCommand.class));

        // When & Then
        mockMvc.perform(
                        put(
                                        BASE_URL + "/product/group/{productGroupId}/notice/refund",
                                        productGroupId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.status").value(200))
                .andDo(
                        document(
                                "v1-admin-refund-policy-update",
                                pathParameters(
                                        parameterWithName("productGroupId")
                                                .description("상품 그룹 ID")),
                                requestFields(
                                        fieldWithPath("returnMethodDomestic")
                                                .type(JsonFieldType.STRING)
                                                .description("국내 반품 방법"),
                                        fieldWithPath("returnCourierDomestic")
                                                .type(JsonFieldType.STRING)
                                                .description("국내 반품 택배사"),
                                        fieldWithPath("returnChargeDomestic")
                                                .type(JsonFieldType.NUMBER)
                                                .description("국내 반품비"),
                                        fieldWithPath("returnExchangeAreaDomestic")
                                                .type(JsonFieldType.STRING)
                                                .description("국내 반품/교환 지역")),
                                responseFields(
                                        fieldWithPath("data")
                                                .type(JsonFieldType.NUMBER)
                                                .description("수정된 상품 그룹 ID"),
                                        fieldWithPath("response.status")
                                                .type(JsonFieldType.NUMBER)
                                                .description("HTTP 상태 코드"),
                                        fieldWithPath("response.message")
                                                .type(JsonFieldType.STRING)
                                                .description("응답 메시지"))));
    }
}
