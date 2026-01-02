package com.ryuqq.setof.adapter.in.rest.admin.v2.seller.controller;

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
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.mapper.SellerAdminV2ApiMapper;
import com.ryuqq.setof.application.common.response.PageResponse;
import com.ryuqq.setof.application.seller.dto.query.SellerSearchQuery;
import com.ryuqq.setof.application.seller.dto.response.BusinessInfoResponse;
import com.ryuqq.setof.application.seller.dto.response.CsInfoResponse;
import com.ryuqq.setof.application.seller.dto.response.SellerResponse;
import com.ryuqq.setof.application.seller.dto.response.SellerSummaryResponse;
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
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * SellerAdminQueryController REST Docs 테스트
 *
 * <p>셀러 조회 API 문서 생성을 위한 테스트 (CQRS Query 분리)
 *
 * @author development-team
 * @since 2.0.0
 */
@WebMvcTest(controllers = SellerAdminQueryController.class)
@Import({
    SellerAdminQueryController.class,
    SellerAdminQueryControllerDocsTest.TestSecurityConfig.class
})
@DisplayName("SellerAdminQueryController REST Docs")
class SellerAdminQueryControllerDocsTest extends RestDocsTestSupport {

    private static final String BASE_URL = "/api/v2/admin/sellers";

    @MockitoBean private GetSellerUseCase getSellerUseCase;
    @MockitoBean private GetSellersUseCase getSellersUseCase;
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

        given(mapper.toSearchQuery(any())).willReturn(new SellerSearchQuery(null, null, 0, 20));
        given(getSellersUseCase.execute(any(SellerSearchQuery.class))).willReturn(pageResponse);

        // When & Then
        mockMvc.perform(get(BASE_URL).param("page.number", "0").param("page.size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(
                        document(
                                "admin-seller-list",
                                queryParameters(
                                        parameterWithName("page.number")
                                                .description("페이지 번호 (0부터 시작)")
                                                .optional(),
                                        parameterWithName("page.size")
                                                .description("페이지 크기")
                                                .optional(),
                                        parameterWithName("filter.sellerName")
                                                .description("셀러명 검색 키워드")
                                                .optional(),
                                        parameterWithName("filter.approvalStatus")
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
}
