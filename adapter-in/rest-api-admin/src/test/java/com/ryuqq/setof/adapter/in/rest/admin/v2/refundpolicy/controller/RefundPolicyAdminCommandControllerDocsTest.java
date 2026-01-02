package com.ryuqq.setof.adapter.in.rest.admin.v2.refundpolicy.controller;

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
import com.ryuqq.setof.adapter.in.rest.admin.v2.refundpolicy.dto.command.RegisterRefundPolicyV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.refundpolicy.dto.command.UpdateRefundPolicyV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.refundpolicy.mapper.RefundPolicyAdminV2ApiMapper;
import com.ryuqq.setof.application.refundpolicy.dto.command.DeleteRefundPolicyCommand;
import com.ryuqq.setof.application.refundpolicy.dto.command.RegisterRefundPolicyCommand;
import com.ryuqq.setof.application.refundpolicy.dto.command.SetDefaultRefundPolicyCommand;
import com.ryuqq.setof.application.refundpolicy.dto.command.UpdateRefundPolicyCommand;
import com.ryuqq.setof.application.refundpolicy.port.in.command.DeleteRefundPolicyUseCase;
import com.ryuqq.setof.application.refundpolicy.port.in.command.RegisterRefundPolicyUseCase;
import com.ryuqq.setof.application.refundpolicy.port.in.command.SetDefaultRefundPolicyUseCase;
import com.ryuqq.setof.application.refundpolicy.port.in.command.UpdateRefundPolicyUseCase;
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
 * RefundPolicyAdminCommandController REST Docs 테스트
 *
 * <p>환불 정책 등록/수정/삭제 API 문서 생성을 위한 테스트 (CQRS Command 분리)
 *
 * @author development-team
 * @since 2.0.0
 */
@WebMvcTest(controllers = RefundPolicyAdminCommandController.class)
@Import({
    RefundPolicyAdminCommandController.class,
    RefundPolicyAdminCommandControllerDocsTest.TestSecurityConfig.class
})
@DisplayName("RefundPolicyAdminCommandController REST Docs")
class RefundPolicyAdminCommandControllerDocsTest extends RestDocsTestSupport {

    private static final String BASE_URL = "/api/v2/admin/sellers/{sellerId}/refund-policies";

    @MockitoBean private RegisterRefundPolicyUseCase registerRefundPolicyUseCase;
    @MockitoBean private UpdateRefundPolicyUseCase updateRefundPolicyUseCase;
    @MockitoBean private SetDefaultRefundPolicyUseCase setDefaultRefundPolicyUseCase;
    @MockitoBean private DeleteRefundPolicyUseCase deleteRefundPolicyUseCase;
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
    @DisplayName("POST /api/v2/admin/sellers/{sellerId}/refund-policies - 환불 정책 등록 API 문서")
    void registerRefundPolicy() throws Exception {
        // Given
        Long sellerId = 1L;
        RegisterRefundPolicyV2ApiRequest request =
                new RegisterRefundPolicyV2ApiRequest(
                        "기본 환불 정책",
                        "서울시 강남구",
                        "역삼동 123-45",
                        "06234",
                        7,
                        3000,
                        "상품 수령 후 7일 이내 교환/환불 가능",
                        true,
                        1);

        given(
                        mapper.toRegisterCommand(
                                any(Long.class), any(RegisterRefundPolicyV2ApiRequest.class)))
                .willReturn(
                        new RegisterRefundPolicyCommand(
                                sellerId,
                                "기본 환불 정책",
                                "서울시 강남구",
                                "역삼동 123-45",
                                "06234",
                                7,
                                3000,
                                "상품 수령 후 7일 이내 교환/환불 가능",
                                true,
                                1));
        given(registerRefundPolicyUseCase.execute(any(RegisterRefundPolicyCommand.class)))
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
                                "admin-refund-policy-register",
                                pathParameters(parameterWithName("sellerId").description("셀러 ID")),
                                requestFields(
                                        fieldWithPath("policyName")
                                                .type(JsonFieldType.STRING)
                                                .description("정책명 (필수)"),
                                        fieldWithPath("returnAddressLine1")
                                                .type(JsonFieldType.STRING)
                                                .description("반품 주소 1 (필수)"),
                                        fieldWithPath("returnAddressLine2")
                                                .type(JsonFieldType.STRING)
                                                .description("반품 주소 2")
                                                .optional(),
                                        fieldWithPath("returnZipCode")
                                                .type(JsonFieldType.STRING)
                                                .description("반품 우편번호 (필수)"),
                                        fieldWithPath("refundPeriodDays")
                                                .type(JsonFieldType.NUMBER)
                                                .description("환불 가능 기간 (일, 필수)"),
                                        fieldWithPath("refundDeliveryCost")
                                                .type(JsonFieldType.NUMBER)
                                                .description("환불 배송비 (필수)"),
                                        fieldWithPath("refundGuide")
                                                .type(JsonFieldType.STRING)
                                                .description("환불 안내")
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
                                                .description("생성된 환불 정책 ID"),
                                        fieldWithPath("error")
                                                .type(JsonFieldType.NULL)
                                                .description("에러 정보 (성공 시 null)")
                                                .optional())));
    }

    @Test
    @DisplayName(
            "PUT /api/v2/admin/sellers/{sellerId}/refund-policies/{refundPolicyId} - 환불 정책 수정 API"
                    + " 문서")
    void updateRefundPolicy() throws Exception {
        // Given
        Long sellerId = 1L;
        Long refundPolicyId = 1L;
        UpdateRefundPolicyV2ApiRequest request =
                new UpdateRefundPolicyV2ApiRequest(
                        "수정된 환불 정책", "서울시 서초구", "서초동 456-78", "06789", 14, 5000, "수정된 환불 안내");

        given(
                        mapper.toUpdateCommand(
                                any(Long.class),
                                any(Long.class),
                                any(UpdateRefundPolicyV2ApiRequest.class)))
                .willReturn(
                        new UpdateRefundPolicyCommand(
                                refundPolicyId,
                                sellerId,
                                "수정된 환불 정책",
                                "서울시 서초구",
                                "서초동 456-78",
                                "06789",
                                14,
                                5000,
                                "수정된 환불 안내"));
        doNothing().when(updateRefundPolicyUseCase).execute(any(UpdateRefundPolicyCommand.class));

        // When & Then
        mockMvc.perform(
                        put(BASE_URL + "/{refundPolicyId}", sellerId, refundPolicyId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(
                        document(
                                "admin-refund-policy-update",
                                pathParameters(
                                        parameterWithName("sellerId").description("셀러 ID"),
                                        parameterWithName("refundPolicyId")
                                                .description("환불 정책 ID")),
                                requestFields(
                                        fieldWithPath("policyName")
                                                .type(JsonFieldType.STRING)
                                                .description("정책명 (필수)"),
                                        fieldWithPath("returnAddressLine1")
                                                .type(JsonFieldType.STRING)
                                                .description("반품 주소 1 (필수)"),
                                        fieldWithPath("returnAddressLine2")
                                                .type(JsonFieldType.STRING)
                                                .description("반품 주소 2")
                                                .optional(),
                                        fieldWithPath("returnZipCode")
                                                .type(JsonFieldType.STRING)
                                                .description("반품 우편번호 (필수)"),
                                        fieldWithPath("refundPeriodDays")
                                                .type(JsonFieldType.NUMBER)
                                                .description("환불 가능 기간 (일, 필수)"),
                                        fieldWithPath("refundDeliveryCost")
                                                .type(JsonFieldType.NUMBER)
                                                .description("환불 배송비 (필수)"),
                                        fieldWithPath("refundGuide")
                                                .type(JsonFieldType.STRING)
                                                .description("환불 안내")
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
            "PATCH /api/v2/admin/sellers/{sellerId}/refund-policies/{refundPolicyId}/default - 기본"
                    + " 환불 정책 설정 API 문서")
    void setDefaultRefundPolicy() throws Exception {
        // Given
        Long sellerId = 1L;
        Long refundPolicyId = 1L;

        given(mapper.toSetDefaultCommand(refundPolicyId, sellerId))
                .willReturn(new SetDefaultRefundPolicyCommand(refundPolicyId, sellerId));
        doNothing()
                .when(setDefaultRefundPolicyUseCase)
                .execute(any(SetDefaultRefundPolicyCommand.class));

        // When & Then
        mockMvc.perform(patch(BASE_URL + "/{refundPolicyId}/default", sellerId, refundPolicyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(
                        document(
                                "admin-refund-policy-set-default",
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
            "PATCH /api/v2/admin/sellers/{sellerId}/refund-policies/{refundPolicyId}/delete - 환불 정책"
                    + " 삭제 API 문서")
    void deleteRefundPolicy() throws Exception {
        // Given
        Long sellerId = 1L;
        Long refundPolicyId = 1L;

        given(mapper.toDeleteCommand(refundPolicyId, sellerId))
                .willReturn(new DeleteRefundPolicyCommand(refundPolicyId, sellerId));
        doNothing().when(deleteRefundPolicyUseCase).execute(any(DeleteRefundPolicyCommand.class));

        // When & Then
        mockMvc.perform(patch(BASE_URL + "/{refundPolicyId}/delete", sellerId, refundPolicyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(
                        document(
                                "admin-refund-policy-delete",
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
