package com.ryuqq.setof.adapter.in.rest.admin.v2.shippingpolicy.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ryuqq.setof.adapter.in.rest.admin.common.docs.RestDocsTestSupport;
import com.ryuqq.setof.adapter.in.rest.admin.common.error.ErrorMapperRegistry;
import com.ryuqq.setof.adapter.in.rest.admin.v2.shippingpolicy.dto.command.RegisterShippingPolicyV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.shippingpolicy.dto.command.UpdateShippingPolicyV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.shippingpolicy.mapper.ShippingPolicyAdminV2ApiMapper;
import com.ryuqq.setof.application.shippingpolicy.dto.command.DeleteShippingPolicyCommand;
import com.ryuqq.setof.application.shippingpolicy.dto.command.RegisterShippingPolicyCommand;
import com.ryuqq.setof.application.shippingpolicy.dto.command.SetDefaultShippingPolicyCommand;
import com.ryuqq.setof.application.shippingpolicy.dto.command.UpdateShippingPolicyCommand;
import com.ryuqq.setof.application.shippingpolicy.port.in.command.DeleteShippingPolicyUseCase;
import com.ryuqq.setof.application.shippingpolicy.port.in.command.RegisterShippingPolicyUseCase;
import com.ryuqq.setof.application.shippingpolicy.port.in.command.SetDefaultShippingPolicyUseCase;
import com.ryuqq.setof.application.shippingpolicy.port.in.command.UpdateShippingPolicyUseCase;
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
 * ShippingPolicyAdminCommandController REST Docs 테스트
 *
 * <p>배송 정책 명령 API 문서 생성을 위한 테스트 (CQRS Command 분리)
 *
 * @author development-team
 * @since 2.0.0
 */
@WebMvcTest(controllers = ShippingPolicyAdminCommandController.class)
@Import({
    ShippingPolicyAdminCommandController.class,
    ShippingPolicyAdminCommandControllerDocsTest.TestSecurityConfig.class
})
@DisplayName("ShippingPolicyAdminCommandController REST Docs")
class ShippingPolicyAdminCommandControllerDocsTest extends RestDocsTestSupport {

    private static final String BASE_URL = "/api/v2/admin/sellers/{sellerId}/shipping-policies";

    @MockitoBean private RegisterShippingPolicyUseCase registerShippingPolicyUseCase;
    @MockitoBean private UpdateShippingPolicyUseCase updateShippingPolicyUseCase;
    @MockitoBean private SetDefaultShippingPolicyUseCase setDefaultShippingPolicyUseCase;
    @MockitoBean private DeleteShippingPolicyUseCase deleteShippingPolicyUseCase;
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
    @DisplayName("POST /api/v2/admin/sellers/{sellerId}/shipping-policies - 배송 정책 등록 API 문서")
    void registerShippingPolicy() throws Exception {
        // Given
        Long sellerId = 1L;
        RegisterShippingPolicyV2ApiRequest request =
                new RegisterShippingPolicyV2ApiRequest(
                        "기본 배송 정책", 3000, 50000, "주문 후 2-3일 내 배송", true, 1);

        given(
                        mapper.toRegisterCommand(
                                any(Long.class), any(RegisterShippingPolicyV2ApiRequest.class)))
                .willReturn(
                        new RegisterShippingPolicyCommand(
                                sellerId, "기본 배송 정책", 3000, 50000, "주문 후 2-3일 내 배송", true, 1));
        given(registerShippingPolicyUseCase.execute(any(RegisterShippingPolicyCommand.class)))
                .willReturn(1L);

        // When & Then
        mockMvc.perform(
                        post(BASE_URL, sellerId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(
                        document(
                                "admin-shipping-policy-register",
                                pathParameters(parameterWithName("sellerId").description("셀러 ID")),
                                requestFields(
                                        fieldWithPath("policyName")
                                                .type(JsonFieldType.STRING)
                                                .description("정책명 (필수)"),
                                        fieldWithPath("defaultDeliveryCost")
                                                .type(JsonFieldType.NUMBER)
                                                .description("기본 배송비 (필수)"),
                                        fieldWithPath("freeShippingThreshold")
                                                .type(JsonFieldType.NUMBER)
                                                .description("무료 배송 기준 금액")
                                                .optional(),
                                        fieldWithPath("deliveryGuide")
                                                .type(JsonFieldType.STRING)
                                                .description("배송 안내")
                                                .optional(),
                                        fieldWithPath("isDefault")
                                                .type(JsonFieldType.BOOLEAN)
                                                .description("기본 정책 여부 (필수)"),
                                        fieldWithPath("displayOrder")
                                                .type(JsonFieldType.NUMBER)
                                                .description("표시 순서 (필수)")),
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
                                                .type(JsonFieldType.NUMBER)
                                                .description("생성된 배송 정책 ID"),
                                        fieldWithPath("error")
                                                .type(JsonFieldType.NULL)
                                                .description("에러 정보 (성공 시 null)")
                                                .optional())));
    }

    @Test
    @DisplayName(
            "PUT /api/v2/admin/sellers/{sellerId}/shipping-policies/{shippingPolicyId} - 배송 정책 수정"
                    + " API 문서")
    void updateShippingPolicy() throws Exception {
        // Given
        Long sellerId = 1L;
        Long shippingPolicyId = 1L;
        UpdateShippingPolicyV2ApiRequest request =
                new UpdateShippingPolicyV2ApiRequest("수정된 배송 정책", 5000, 70000, "주문 후 1-2일 내 배송", 2);

        given(
                        mapper.toUpdateCommand(
                                any(Long.class),
                                any(Long.class),
                                any(UpdateShippingPolicyV2ApiRequest.class)))
                .willReturn(
                        new UpdateShippingPolicyCommand(
                                shippingPolicyId,
                                sellerId,
                                "수정된 배송 정책",
                                5000,
                                70000,
                                "주문 후 1-2일 내 배송",
                                2));
        doNothing()
                .when(updateShippingPolicyUseCase)
                .execute(any(UpdateShippingPolicyCommand.class));

        // When & Then
        mockMvc.perform(
                        put(BASE_URL + "/{shippingPolicyId}", sellerId, shippingPolicyId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(
                        document(
                                "admin-shipping-policy-update",
                                pathParameters(
                                        parameterWithName("sellerId").description("셀러 ID"),
                                        parameterWithName("shippingPolicyId")
                                                .description("배송 정책 ID")),
                                requestFields(
                                        fieldWithPath("policyName")
                                                .type(JsonFieldType.STRING)
                                                .description("정책명 (필수)"),
                                        fieldWithPath("defaultDeliveryCost")
                                                .type(JsonFieldType.NUMBER)
                                                .description("기본 배송비 (필수)"),
                                        fieldWithPath("freeShippingThreshold")
                                                .type(JsonFieldType.NUMBER)
                                                .description("무료 배송 기준 금액")
                                                .optional(),
                                        fieldWithPath("deliveryGuide")
                                                .type(JsonFieldType.STRING)
                                                .description("배송 안내")
                                                .optional(),
                                        fieldWithPath("displayOrder")
                                                .type(JsonFieldType.NUMBER)
                                                .description("표시 순서 (필수)")),
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
                                                .type(JsonFieldType.NULL)
                                                .description("응답 데이터 (수정 시 null)")
                                                .optional(),
                                        fieldWithPath("error")
                                                .type(JsonFieldType.NULL)
                                                .description("에러 정보 (성공 시 null)")
                                                .optional())));
    }

    @Test
    @DisplayName(
            "PATCH /api/v2/admin/sellers/{sellerId}/shipping-policies/{shippingPolicyId}/default -"
                    + " 기본 배송 정책 설정 API 문서")
    void setDefaultShippingPolicy() throws Exception {
        // Given
        Long sellerId = 1L;
        Long shippingPolicyId = 1L;

        given(mapper.toSetDefaultCommand(shippingPolicyId, sellerId))
                .willReturn(new SetDefaultShippingPolicyCommand(shippingPolicyId, sellerId));
        doNothing()
                .when(setDefaultShippingPolicyUseCase)
                .execute(any(SetDefaultShippingPolicyCommand.class));

        // When & Then
        mockMvc.perform(patch(BASE_URL + "/{shippingPolicyId}/default", sellerId, shippingPolicyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(
                        document(
                                "admin-shipping-policy-set-default",
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
                                        fieldWithPath("data")
                                                .type(JsonFieldType.NULL)
                                                .description("응답 데이터")
                                                .optional(),
                                        fieldWithPath("error")
                                                .type(JsonFieldType.NULL)
                                                .description("에러 정보 (성공 시 null)")
                                                .optional())));
    }

    @Test
    @DisplayName(
            "PATCH /api/v2/admin/sellers/{sellerId}/shipping-policies/{shippingPolicyId}/delete -"
                    + " 배송 정책 삭제 API 문서")
    void deleteShippingPolicy() throws Exception {
        // Given
        Long sellerId = 1L;
        Long shippingPolicyId = 1L;

        given(mapper.toDeleteCommand(shippingPolicyId, sellerId))
                .willReturn(new DeleteShippingPolicyCommand(shippingPolicyId, sellerId));
        doNothing()
                .when(deleteShippingPolicyUseCase)
                .execute(any(DeleteShippingPolicyCommand.class));

        // When & Then
        mockMvc.perform(patch(BASE_URL + "/{shippingPolicyId}/delete", sellerId, shippingPolicyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(
                        document(
                                "admin-shipping-policy-delete",
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
                                        fieldWithPath("data")
                                                .type(JsonFieldType.NULL)
                                                .description("응답 데이터")
                                                .optional(),
                                        fieldWithPath("error")
                                                .type(JsonFieldType.NULL)
                                                .description("에러 정보 (성공 시 null)")
                                                .optional())));
    }
}
