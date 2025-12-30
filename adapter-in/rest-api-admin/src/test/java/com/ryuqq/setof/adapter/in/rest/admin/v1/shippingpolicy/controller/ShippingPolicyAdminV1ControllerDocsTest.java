package com.ryuqq.setof.adapter.in.rest.admin.v1.shippingpolicy.controller;

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
import com.ryuqq.setof.adapter.in.rest.admin.v1.shippingpolicy.mapper.ShippingPolicyAdminV1ApiMapper;
import com.ryuqq.setof.application.shippingpolicy.dto.command.UpdateShippingPolicyCommand;
import com.ryuqq.setof.application.shippingpolicy.dto.response.ShippingPolicyResponse;
import com.ryuqq.setof.application.shippingpolicy.port.in.command.UpdateShippingPolicyUseCase;
import com.ryuqq.setof.application.shippingpolicy.port.in.query.GetDefaultShippingAddressUseCase;
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
 * ShippingPolicyAdminV1Controller REST Docs 테스트
 *
 * @author development-team
 * @since 1.0.0
 * @deprecated V2 API로 마이그레이션 권장
 */
@WebMvcTest(controllers = ShippingPolicyAdminV1Controller.class)
@Import({
    ShippingPolicyAdminV1Controller.class,
    ShippingPolicyAdminV1ControllerDocsTest.TestSecurityConfig.class
})
@DisplayName("ShippingPolicyAdminV1Controller REST Docs (Legacy)")
class ShippingPolicyAdminV1ControllerDocsTest extends RestDocsTestSupport {

    private static final String BASE_URL = "/api/v1";

    @MockitoBean private GetDefaultShippingAddressUseCase getDefaultShippingAddressUseCase;
    @MockitoBean private UpdateShippingPolicyUseCase updateShippingPolicyUseCase;
    @MockitoBean private ShippingPolicyAdminV1ApiMapper mapper;
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
    @DisplayName("PUT /api/v1/product/group/{productGroupId}/notice/delivery - [Legacy] 배송 고지 수정")
    void updateProductDeliveryNotice() throws Exception {
        // Given
        Long productGroupId = 1L;
        String requestBody =
                """
                {
                    "deliveryArea": "전국",
                    "deliveryFee": 3000,
                    "deliveryPeriodAverage": 3
                }
                """;

        ShippingPolicyResponse shippingPolicyResponse =
                new ShippingPolicyResponse(
                        1L, // shippingPolicyId
                        0L, // sellerId
                        "기본 배송 정책", // policyName
                        3000, // defaultDeliveryCost
                        50000, // freeShippingThreshold
                        "2-3일 이내 발송", // deliveryGuide
                        true, // isDefault
                        1); // displayOrder

        given(getDefaultShippingAddressUseCase.execute(anyLong()))
                .willReturn(shippingPolicyResponse);
        given(mapper.toUpdateCommand(any(), any()))
                .willReturn(
                        new UpdateShippingPolicyCommand(
                                1L, // shippingPolicyId
                                0L, // sellerId
                                "기본 배송 정책", // policyName
                                3000, // defaultDeliveryCost
                                50000, // freeShippingThreshold
                                "2-3일 이내 발송", // deliveryGuide
                                1)); // displayOrder
        doNothing()
                .when(updateShippingPolicyUseCase)
                .execute(any(UpdateShippingPolicyCommand.class));

        // When & Then
        mockMvc.perform(
                        put(
                                        BASE_URL
                                                + "/product/group/{productGroupId}/notice/delivery",
                                        productGroupId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.status").value(200))
                .andDo(
                        document(
                                "v1-admin-shipping-policy-update",
                                pathParameters(
                                        parameterWithName("productGroupId")
                                                .description("상품 그룹 ID")),
                                requestFields(
                                        fieldWithPath("deliveryArea")
                                                .type(JsonFieldType.STRING)
                                                .description("배송 지역"),
                                        fieldWithPath("deliveryFee")
                                                .type(JsonFieldType.NUMBER)
                                                .description("배송비"),
                                        fieldWithPath("deliveryPeriodAverage")
                                                .type(JsonFieldType.NUMBER)
                                                .description("평균 배송 기간")),
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
