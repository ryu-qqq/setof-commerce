package com.ryuqq.setof.adapter.in.rest.admin.v2.seller.controller;

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
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.command.RegisterSellerV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.command.UpdateSellerV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.mapper.SellerAdminV2ApiMapper;
import com.ryuqq.setof.application.seller.dto.command.DeleteSellerCommand;
import com.ryuqq.setof.application.seller.dto.command.RegisterSellerCommand;
import com.ryuqq.setof.application.seller.dto.command.UpdateApprovalStatusCommand;
import com.ryuqq.setof.application.seller.dto.command.UpdateSellerCommand;
import com.ryuqq.setof.application.seller.port.in.command.DeleteSellerUseCase;
import com.ryuqq.setof.application.seller.port.in.command.RegisterSellerUseCase;
import com.ryuqq.setof.application.seller.port.in.command.UpdateApprovalStatusUseCase;
import com.ryuqq.setof.application.seller.port.in.command.UpdateSellerUseCase;
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
 * SellerAdminCommandController REST Docs 테스트
 *
 * <p>셀러 등록/수정/상태변경 API 문서 생성을 위한 테스트 (CQRS Command 분리)
 *
 * @author development-team
 * @since 2.0.0
 */
@WebMvcTest(controllers = SellerAdminCommandController.class)
@Import({
    SellerAdminCommandController.class,
    SellerAdminCommandControllerDocsTest.TestSecurityConfig.class
})
@DisplayName("SellerAdminCommandController REST Docs")
class SellerAdminCommandControllerDocsTest extends RestDocsTestSupport {

    private static final String BASE_URL = "/api/v2/admin/sellers";

    @MockitoBean private RegisterSellerUseCase registerSellerUseCase;
    @MockitoBean private UpdateSellerUseCase updateSellerUseCase;
    @MockitoBean private UpdateApprovalStatusUseCase updateApprovalStatusUseCase;
    @MockitoBean private DeleteSellerUseCase deleteSellerUseCase;
    @MockitoBean private SellerAdminV2ApiMapper mapper;
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
    @DisplayName("POST /api/v2/admin/sellers - 셀러 등록 API 문서")
    void registerSeller() throws Exception {
        // Given
        RegisterSellerV2ApiRequest request =
                new RegisterSellerV2ApiRequest(
                        "테스트 셀러",
                        "https://example.com/logo.png",
                        "테스트 셀러 설명",
                        "1234567890",
                        "2024-서울강남-0001",
                        "홍길동",
                        "서울시 강남구",
                        "테헤란로 123",
                        "06234",
                        "cs@example.com",
                        "01012345678",
                        "0212345678");

        given(mapper.toRegisterCommand(any(RegisterSellerV2ApiRequest.class)))
                .willReturn(
                        new RegisterSellerCommand(
                                "test-tenant",
                                "test-org",
                                "테스트 셀러",
                                "https://example.com/logo.png",
                                "테스트 셀러 설명",
                                "1234567890",
                                "2024-서울강남-0001",
                                "홍길동",
                                "서울시 강남구",
                                "테헤란로 123",
                                "06234",
                                "cs@example.com",
                                "01012345678",
                                "0212345678"));
        given(registerSellerUseCase.execute(any(RegisterSellerCommand.class))).willReturn(1L);

        // When & Then
        mockMvc.perform(
                        post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(
                        document(
                                "admin-seller-register",
                                requestFields(
                                        fieldWithPath("sellerName")
                                                .type(JsonFieldType.STRING)
                                                .description("셀러명 (필수)"),
                                        fieldWithPath("logoUrl")
                                                .type(JsonFieldType.STRING)
                                                .description("로고 URL")
                                                .optional(),
                                        fieldWithPath("description")
                                                .type(JsonFieldType.STRING)
                                                .description("설명")
                                                .optional(),
                                        fieldWithPath("registrationNumber")
                                                .type(JsonFieldType.STRING)
                                                .description("사업자등록번호 (필수, 10자리)"),
                                        fieldWithPath("saleReportNumber")
                                                .type(JsonFieldType.STRING)
                                                .description("통신판매업 신고번호")
                                                .optional(),
                                        fieldWithPath("representative")
                                                .type(JsonFieldType.STRING)
                                                .description("대표자명 (필수)"),
                                        fieldWithPath("businessAddressLine1")
                                                .type(JsonFieldType.STRING)
                                                .description("사업장 주소 (필수)"),
                                        fieldWithPath("businessAddressLine2")
                                                .type(JsonFieldType.STRING)
                                                .description("사업장 주소 상세")
                                                .optional(),
                                        fieldWithPath("businessZipCode")
                                                .type(JsonFieldType.STRING)
                                                .description("사업장 우편번호 (필수)"),
                                        fieldWithPath("csEmail")
                                                .type(JsonFieldType.STRING)
                                                .description("CS 이메일")
                                                .optional(),
                                        fieldWithPath("csMobilePhone")
                                                .type(JsonFieldType.STRING)
                                                .description("CS 휴대폰번호")
                                                .optional(),
                                        fieldWithPath("csLandlinePhone")
                                                .type(JsonFieldType.STRING)
                                                .description("CS 유선전화번호")
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
                                                .type(JsonFieldType.NUMBER)
                                                .description("생성된 셀러 ID"),
                                        fieldWithPath("error")
                                                .type(JsonFieldType.NULL)
                                                .description("에러 정보 (성공 시 null)")
                                                .optional())));
    }

    @Test
    @DisplayName("PUT /api/v2/admin/sellers/{sellerId} - 셀러 수정 API 문서")
    void updateSeller() throws Exception {
        // Given
        Long sellerId = 1L;
        UpdateSellerV2ApiRequest request =
                new UpdateSellerV2ApiRequest(
                        "수정된 셀러",
                        "https://example.com/new-logo.png",
                        "수정된 설명",
                        "1234567890",
                        "2024-서울강남-0002",
                        "김철수",
                        "서울시 서초구",
                        "서초동 456-78",
                        "06789",
                        "updated@example.com",
                        "01098765432",
                        "0298765432");

        given(mapper.toUpdateCommand(any(), any(UpdateSellerV2ApiRequest.class)))
                .willReturn(
                        new UpdateSellerCommand(
                                sellerId,
                                "수정된 셀러",
                                "https://example.com/new-logo.png",
                                "수정된 설명",
                                "1234567890",
                                "2024-서울강남-0002",
                                "김철수",
                                "서울시 서초구",
                                "서초동 456-78",
                                "06789",
                                "updated@example.com",
                                "01098765432",
                                "0298765432"));
        doNothing().when(updateSellerUseCase).execute(any(UpdateSellerCommand.class));

        // When & Then
        mockMvc.perform(
                        put(BASE_URL + "/{sellerId}", sellerId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(
                        document(
                                "admin-seller-update",
                                pathParameters(parameterWithName("sellerId").description("셀러 ID")),
                                requestFields(
                                        fieldWithPath("sellerName")
                                                .type(JsonFieldType.STRING)
                                                .description("셀러명 (필수)"),
                                        fieldWithPath("logoUrl")
                                                .type(JsonFieldType.STRING)
                                                .description("로고 URL")
                                                .optional(),
                                        fieldWithPath("description")
                                                .type(JsonFieldType.STRING)
                                                .description("설명")
                                                .optional(),
                                        fieldWithPath("registrationNumber")
                                                .type(JsonFieldType.STRING)
                                                .description("사업자등록번호 (필수, 10자리)"),
                                        fieldWithPath("saleReportNumber")
                                                .type(JsonFieldType.STRING)
                                                .description("통신판매업 신고번호")
                                                .optional(),
                                        fieldWithPath("representative")
                                                .type(JsonFieldType.STRING)
                                                .description("대표자명 (필수)"),
                                        fieldWithPath("businessAddressLine1")
                                                .type(JsonFieldType.STRING)
                                                .description("사업장 주소 (필수)"),
                                        fieldWithPath("businessAddressLine2")
                                                .type(JsonFieldType.STRING)
                                                .description("사업장 주소 상세")
                                                .optional(),
                                        fieldWithPath("businessZipCode")
                                                .type(JsonFieldType.STRING)
                                                .description("사업장 우편번호 (필수)"),
                                        fieldWithPath("csEmail")
                                                .type(JsonFieldType.STRING)
                                                .description("CS 이메일")
                                                .optional(),
                                        fieldWithPath("csMobilePhone")
                                                .type(JsonFieldType.STRING)
                                                .description("CS 휴대폰번호")
                                                .optional(),
                                        fieldWithPath("csLandlinePhone")
                                                .type(JsonFieldType.STRING)
                                                .description("CS 유선전화번호")
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
    @DisplayName("PATCH /api/v2/admin/sellers/{sellerId}/approve - 셀러 승인 API 문서")
    void approveSeller() throws Exception {
        // Given
        Long sellerId = 1L;
        given(mapper.toApproveCommand(sellerId))
                .willReturn(new UpdateApprovalStatusCommand(sellerId, "APPROVED"));
        doNothing()
                .when(updateApprovalStatusUseCase)
                .execute(any(UpdateApprovalStatusCommand.class));

        // When & Then
        mockMvc.perform(patch(BASE_URL + "/{sellerId}/approve", sellerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(
                        document(
                                "admin-seller-approve",
                                pathParameters(parameterWithName("sellerId").description("셀러 ID")),
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
    @DisplayName("PATCH /api/v2/admin/sellers/{sellerId}/reject - 셀러 거절 API 문서")
    void rejectSeller() throws Exception {
        // Given
        Long sellerId = 1L;
        given(mapper.toRejectCommand(sellerId))
                .willReturn(new UpdateApprovalStatusCommand(sellerId, "REJECTED"));
        doNothing()
                .when(updateApprovalStatusUseCase)
                .execute(any(UpdateApprovalStatusCommand.class));

        // When & Then
        mockMvc.perform(patch(BASE_URL + "/{sellerId}/reject", sellerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(
                        document(
                                "admin-seller-reject",
                                pathParameters(parameterWithName("sellerId").description("셀러 ID")),
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
    @DisplayName("PATCH /api/v2/admin/sellers/{sellerId}/suspend - 셀러 정지 API 문서")
    void suspendSeller() throws Exception {
        // Given
        Long sellerId = 1L;
        given(mapper.toSuspendCommand(sellerId))
                .willReturn(new UpdateApprovalStatusCommand(sellerId, "SUSPENDED"));
        doNothing()
                .when(updateApprovalStatusUseCase)
                .execute(any(UpdateApprovalStatusCommand.class));

        // When & Then
        mockMvc.perform(patch(BASE_URL + "/{sellerId}/suspend", sellerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(
                        document(
                                "admin-seller-suspend",
                                pathParameters(parameterWithName("sellerId").description("셀러 ID")),
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
    @DisplayName("PATCH /api/v2/admin/sellers/{sellerId}/delete - 셀러 삭제 API 문서")
    void deleteSeller() throws Exception {
        // Given
        Long sellerId = 1L;
        given(mapper.toDeleteCommand(sellerId)).willReturn(new DeleteSellerCommand(sellerId));
        doNothing().when(deleteSellerUseCase).execute(any(DeleteSellerCommand.class));

        // When & Then
        mockMvc.perform(patch(BASE_URL + "/{sellerId}/delete", sellerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(
                        document(
                                "admin-seller-delete",
                                pathParameters(parameterWithName("sellerId").description("셀러 ID")),
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
