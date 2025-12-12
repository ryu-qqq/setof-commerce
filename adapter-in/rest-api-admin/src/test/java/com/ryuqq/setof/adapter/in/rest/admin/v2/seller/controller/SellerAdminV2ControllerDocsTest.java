package com.ryuqq.setof.adapter.in.rest.admin.v2.seller.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.command.RegisterSellerV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.command.UpdateSellerV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.mapper.SellerAdminV2ApiMapper;
import com.ryuqq.setof.application.common.response.PageResponse;
import com.ryuqq.setof.application.seller.dto.command.DeleteSellerCommand;
import com.ryuqq.setof.application.seller.dto.command.RegisterSellerCommand;
import com.ryuqq.setof.application.seller.dto.command.UpdateApprovalStatusCommand;
import com.ryuqq.setof.application.seller.dto.command.UpdateSellerCommand;
import com.ryuqq.setof.application.seller.dto.query.SellerSearchQuery;
import com.ryuqq.setof.application.seller.dto.response.BusinessInfoResponse;
import com.ryuqq.setof.application.seller.dto.response.CsInfoResponse;
import com.ryuqq.setof.application.seller.dto.response.SellerResponse;
import com.ryuqq.setof.application.seller.dto.response.SellerSummaryResponse;
import com.ryuqq.setof.application.seller.port.in.command.DeleteSellerUseCase;
import com.ryuqq.setof.application.seller.port.in.command.RegisterSellerUseCase;
import com.ryuqq.setof.application.seller.port.in.command.UpdateApprovalStatusUseCase;
import com.ryuqq.setof.application.seller.port.in.command.UpdateSellerUseCase;
import com.ryuqq.setof.application.seller.port.in.query.GetSellerUseCase;
import com.ryuqq.setof.application.seller.port.in.query.GetSellersUseCase;
import java.util.List;
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
 * SellerAdminV2Controller REST Docs 테스트
 *
 * <p>셀러 관리 API 문서 생성을 위한 테스트
 *
 * @author development-team
 * @since 2.0.0
 */
@WebMvcTest(controllers = SellerAdminV2Controller.class)
@Import({SellerAdminV2Controller.class, SellerAdminV2ControllerDocsTest.TestSecurityConfig.class})
@DisplayName("SellerAdminV2Controller REST Docs")
class SellerAdminV2ControllerDocsTest extends RestDocsTestSupport {

    private static final String BASE_URL = "/api/v2/admin/sellers";

    @MockitoBean private RegisterSellerUseCase registerSellerUseCase;
    @MockitoBean private GetSellerUseCase getSellerUseCase;
    @MockitoBean private GetSellersUseCase getSellersUseCase;
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
    @DisplayName("GET /api/v2/admin/sellers - 셀러 목록 조회 API 문서")
    void getSellers() throws Exception {
        // Given
        List<SellerSummaryResponse> sellers =
                List.of(
                        new SellerSummaryResponse(
                                1L, "테스트 셀러1", "https://example.com/logo1.png", "APPROVED"),
                        new SellerSummaryResponse(
                                2L, "테스트 셀러2", "https://example.com/logo2.png", "PENDING"));

        PageResponse<SellerSummaryResponse> pageResponse =
                PageResponse.of(sellers, 0, 20, 2L, 1, true, true);

        given(mapper.toSearchQuery(any(), any(), anyInt(), anyInt()))
                .willReturn(new SellerSearchQuery(null, null, 0, 20));
        given(getSellersUseCase.execute(any(SellerSearchQuery.class))).willReturn(pageResponse);

        // When & Then
        mockMvc.perform(get(BASE_URL).param("page", "0").param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(
                        document(
                                "admin-seller-list",
                                queryParameters(
                                        parameterWithName("page")
                                                .description("페이지 번호 (0부터 시작)")
                                                .optional(),
                                        parameterWithName("size").description("페이지 크기").optional(),
                                        parameterWithName("sellerName")
                                                .description("셀러명 검색 키워드")
                                                .optional(),
                                        parameterWithName("approvalStatus")
                                                .description(
                                                        "승인 상태"
                                                            + " (PENDING/APPROVED/REJECTED/SUSPENDED)")
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
                                        fieldWithPath("data.sellers")
                                                .type(JsonFieldType.ARRAY)
                                                .description("셀러 목록"),
                                        fieldWithPath("data.sellers[].sellerId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("셀러 ID"),
                                        fieldWithPath("data.sellers[].sellerName")
                                                .type(JsonFieldType.STRING)
                                                .description("셀러명"),
                                        fieldWithPath("data.sellers[].logoUrl")
                                                .type(JsonFieldType.STRING)
                                                .description("로고 URL"),
                                        fieldWithPath("data.sellers[].approvalStatus")
                                                .type(JsonFieldType.STRING)
                                                .description("승인 상태"),
                                        fieldWithPath("data.page")
                                                .type(JsonFieldType.NUMBER)
                                                .description("현재 페이지"),
                                        fieldWithPath("data.size")
                                                .type(JsonFieldType.NUMBER)
                                                .description("페이지 크기"),
                                        fieldWithPath("data.totalCount")
                                                .type(JsonFieldType.NUMBER)
                                                .description("전체 요소 수"),
                                        fieldWithPath("data.totalPages")
                                                .type(JsonFieldType.NUMBER)
                                                .description("전체 페이지 수"),
                                        fieldWithPath("data.isLast")
                                                .type(JsonFieldType.BOOLEAN)
                                                .description("마지막 페이지 여부"),
                                        fieldWithPath("error")
                                                .type(JsonFieldType.NULL)
                                                .description("에러 정보 (성공 시 null)")
                                                .optional())));
    }

    @Test
    @DisplayName("GET /api/v2/admin/sellers/{sellerId} - 셀러 상세 조회 API 문서")
    void getSeller() throws Exception {
        // Given
        Long sellerId = 1L;
        BusinessInfoResponse businessInfo =
                new BusinessInfoResponse(
                        "1234567890", "2024-서울강남-0001", "홍길동", "서울시 강남구", "테헤란로 123", "06234");
        CsInfoResponse csInfo = new CsInfoResponse("cs@example.com", "01012345678", "0212345678");
        SellerResponse sellerResponse =
                new SellerResponse(
                        sellerId,
                        "테스트 셀러",
                        "https://example.com/logo.png",
                        "테스트 셀러 설명",
                        "APPROVED",
                        businessInfo,
                        csInfo);

        given(getSellerUseCase.execute(sellerId)).willReturn(sellerResponse);

        // When & Then
        mockMvc.perform(get(BASE_URL + "/{sellerId}", sellerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(
                        document(
                                "admin-seller-detail",
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
                                        fieldWithPath("data.sellerId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("셀러 ID"),
                                        fieldWithPath("data.sellerName")
                                                .type(JsonFieldType.STRING)
                                                .description("셀러명"),
                                        fieldWithPath("data.logoUrl")
                                                .type(JsonFieldType.STRING)
                                                .description("로고 URL"),
                                        fieldWithPath("data.description")
                                                .type(JsonFieldType.STRING)
                                                .description("설명"),
                                        fieldWithPath("data.approvalStatus")
                                                .type(JsonFieldType.STRING)
                                                .description("승인 상태"),
                                        fieldWithPath("data.businessNumber")
                                                .type(JsonFieldType.STRING)
                                                .description("사업자등록번호"),
                                        fieldWithPath("data.representative")
                                                .type(JsonFieldType.STRING)
                                                .description("대표자명"),
                                        fieldWithPath("data.csEmail")
                                                .type(JsonFieldType.STRING)
                                                .description("CS 이메일"),
                                        fieldWithPath("data.csMobilePhone")
                                                .type(JsonFieldType.STRING)
                                                .description("CS 휴대폰번호"),
                                        fieldWithPath("data.csLandlinePhone")
                                                .type(JsonFieldType.STRING)
                                                .description("CS 유선전화번호"),
                                        fieldWithPath("error")
                                                .type(JsonFieldType.NULL)
                                                .description("에러 정보 (성공 시 null)")
                                                .optional())));
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
