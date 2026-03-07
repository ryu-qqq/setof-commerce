package com.ryuqq.setof.adapter.in.rest.v1.seller.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ryuqq.setof.adapter.in.rest.common.RestDocsTestSupport;
import com.ryuqq.setof.adapter.in.rest.v1.seller.SellerApiFixtures;
import com.ryuqq.setof.adapter.in.rest.v1.seller.SellerV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.v1.seller.dto.response.SellerV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.seller.mapper.SellerV1ApiMapper;
import com.ryuqq.setof.application.seller.dto.composite.SellerCompositeResult;
import com.ryuqq.setof.application.seller.port.in.query.GetSellerForCustomerUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

/**
 * SellerQueryV1Controller REST Docs 테스트.
 *
 * <p>셀러 Query API의 REST Docs 스니펫을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("SellerQueryV1Controller REST Docs 테스트")
@WebMvcTest(SellerQueryV1Controller.class)
@WithMockUser
@Import(com.ryuqq.setof.adapter.in.rest.TestConfiguration.class)
class SellerQueryV1ControllerRestDocsTest extends RestDocsTestSupport {

    @MockBean private GetSellerForCustomerUseCase getSellerForCustomerUseCase;

    @MockBean private SellerV1ApiMapper mapper;

    @Nested
    @DisplayName("셀러 단건 조회 API")
    class GetSellerTest {

        @Test
        @DisplayName("셀러 단건 조회 성공")
        void getSeller_Success() throws Exception {
            // given
            long sellerId = 1L;
            SellerCompositeResult result = SellerApiFixtures.sellerCompositeResult(sellerId);
            SellerV1ApiResponse response = SellerApiFixtures.sellerResponse(sellerId);

            given(getSellerForCustomerUseCase.execute(eq(sellerId))).willReturn(result);
            given(mapper.toResponse(result)).willReturn(response);

            // when & then
            mockMvc.perform(get(SellerV1Endpoints.SELLER_BY_ID, sellerId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.sellerId").value(sellerId))
                    .andExpect(jsonPath("$.data.sellerName").value("나이키코리아 유한회사"))
                    .andExpect(
                            jsonPath("$.data.logoUrl")
                                    .value("https://cdn.example.com/sellers/nike.png"))
                    .andExpect(jsonPath("$.data.sellerDescription").value("나이키 공식 판매처"))
                    .andExpect(jsonPath("$.data.csPhoneNumber").value("1588-0000"))
                    .andExpect(jsonPath("$.data.alimTalkPhoneNumber").value("010-1234-5678"))
                    .andExpect(jsonPath("$.data.registrationNumber").value("123-45-67890"))
                    .andExpect(jsonPath("$.data.saleReportNumber").value("2024-서울강남-12345"))
                    .andExpect(jsonPath("$.data.representative").value("홍길동"))
                    .andExpect(jsonPath("$.data.email").value("cs@nike.co.kr"))
                    .andExpect(jsonPath("$.response.status").value(200))
                    .andDo(
                            document.document(
                                    pathParameters(
                                            parameterWithName(SellerV1Endpoints.PATH_SELLER_ID)
                                                    .description("셀러 ID")),
                                    responseFields(
                                            fieldWithPath("data.sellerId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("셀러 ID"),
                                            fieldWithPath("data.sellerName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("상호명"),
                                            fieldWithPath("data.logoUrl")
                                                    .type(JsonFieldType.STRING)
                                                    .description("로고 URL"),
                                            fieldWithPath("data.sellerDescription")
                                                    .type(JsonFieldType.STRING)
                                                    .description("셀러 설명"),
                                            fieldWithPath("data.address")
                                                    .type(JsonFieldType.STRING)
                                                    .description("사업장 주소"),
                                            fieldWithPath("data.csPhoneNumber")
                                                    .type(JsonFieldType.STRING)
                                                    .description("CS 전화번호"),
                                            fieldWithPath("data.alimTalkPhoneNumber")
                                                    .type(JsonFieldType.STRING)
                                                    .description("알림톡 전화번호"),
                                            fieldWithPath("data.registrationNumber")
                                                    .type(JsonFieldType.STRING)
                                                    .description("사업자등록번호"),
                                            fieldWithPath("data.saleReportNumber")
                                                    .type(JsonFieldType.STRING)
                                                    .description("통신판매업 신고번호"),
                                            fieldWithPath("data.representative")
                                                    .type(JsonFieldType.STRING)
                                                    .description("대표자명"),
                                            fieldWithPath("data.email")
                                                    .type(JsonFieldType.STRING)
                                                    .description("CS 이메일"),
                                            fieldWithPath("response.status")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("HTTP 상태 코드"),
                                            fieldWithPath("response.message")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 메시지"))));
        }

        @Test
        @DisplayName("셀러 단건 조회 성공 - CsInfo 없음")
        void getSeller_Success_WithoutCsInfo() throws Exception {
            // given
            long sellerId = 2L;
            SellerCompositeResult result =
                    SellerApiFixtures.sellerCompositeResultWithoutCsInfo(sellerId);
            SellerV1ApiResponse response = SellerApiFixtures.sellerResponseWithoutCsInfo(sellerId);

            given(getSellerForCustomerUseCase.execute(eq(sellerId))).willReturn(result);
            given(mapper.toResponse(result)).willReturn(response);

            // when & then
            mockMvc.perform(get(SellerV1Endpoints.SELLER_BY_ID, sellerId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.sellerId").value(sellerId))
                    .andExpect(jsonPath("$.data.sellerName").value("나이키코리아 유한회사"))
                    .andExpect(jsonPath("$.data.csPhoneNumber").value(""))
                    .andExpect(jsonPath("$.data.registrationNumber").value("123-45-67890"))
                    .andExpect(jsonPath("$.response.status").value(200));
        }

        @Test
        @DisplayName("셀러 단건 조회 성공 - BusinessInfo 없음")
        void getSeller_Success_WithoutBusinessInfo() throws Exception {
            // given
            long sellerId = 3L;
            SellerCompositeResult result =
                    SellerApiFixtures.sellerCompositeResultWithoutBusinessInfo(sellerId);
            SellerV1ApiResponse response =
                    SellerApiFixtures.sellerResponseWithoutBusinessInfo(sellerId);

            given(getSellerForCustomerUseCase.execute(eq(sellerId))).willReturn(result);
            given(mapper.toResponse(result)).willReturn(response);

            // when & then
            mockMvc.perform(get(SellerV1Endpoints.SELLER_BY_ID, sellerId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.sellerId").value(sellerId))
                    .andExpect(jsonPath("$.data.sellerName").value(""))
                    .andExpect(jsonPath("$.data.csPhoneNumber").value("1588-0000"))
                    .andExpect(jsonPath("$.data.registrationNumber").value(""))
                    .andExpect(jsonPath("$.response.status").value(200));
        }

        @Test
        @DisplayName("셀러 단건 조회 성공 - CsInfo와 BusinessInfo 모두 없음")
        void getSeller_Success_WithoutOptionalFields() throws Exception {
            // given
            long sellerId = 4L;
            SellerCompositeResult result =
                    SellerApiFixtures.sellerCompositeResultWithNullOptionalFields(sellerId);
            SellerV1ApiResponse response =
                    SellerApiFixtures.sellerResponseWithNullOptionalFields(sellerId);

            given(getSellerForCustomerUseCase.execute(eq(sellerId))).willReturn(result);
            given(mapper.toResponse(result)).willReturn(response);

            // when & then
            mockMvc.perform(get(SellerV1Endpoints.SELLER_BY_ID, sellerId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.sellerId").value(sellerId))
                    .andExpect(jsonPath("$.data.sellerName").value(""))
                    .andExpect(jsonPath("$.data.csPhoneNumber").value(""))
                    .andExpect(jsonPath("$.data.registrationNumber").value(""))
                    .andExpect(jsonPath("$.response.status").value(200));
        }
    }
}
