package com.ryuqq.setof.adapter.in.rest.admin.v1.seller.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ryuqq.setof.adapter.in.rest.admin.TestConfiguration;
import com.ryuqq.setof.adapter.in.rest.admin.common.RestDocsTestSupport;
import com.ryuqq.setof.adapter.in.rest.admin.v1.common.dto.CustomPageableV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.SellerAdminApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.SellerAdminV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.response.SellerDetailV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.response.SellerV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.mapper.SellerAdminV1ApiMapper;
import com.ryuqq.setof.application.seller.dto.composite.SellerFullCompositeResult;
import com.ryuqq.setof.application.seller.dto.response.SellerPageResult;
import com.ryuqq.setof.application.seller.port.in.query.GetSellerForAdminUseCase;
import com.ryuqq.setof.application.seller.port.in.query.SearchSellerByOffsetUseCase;
import com.ryuqq.setof.application.seller.port.in.query.ValidateBusinessRegistrationUseCase;
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
 * SellerAdminQueryV1Controller REST Docs 테스트.
 *
 * <p>셀러 조회 Admin V1 API의 REST Docs 스니펫을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("SellerAdminQueryV1Controller REST Docs 테스트")
@WebMvcTest(SellerAdminQueryV1Controller.class)
@WithMockUser
@Import(TestConfiguration.class)
class SellerAdminQueryV1ControllerRestDocsTest extends RestDocsTestSupport {

    @MockBean private SearchSellerByOffsetUseCase searchSellerByOffsetUseCase;

    @MockBean private GetSellerForAdminUseCase getSellerForAdminUseCase;

    @MockBean private ValidateBusinessRegistrationUseCase validateBusinessRegistrationUseCase;

    @MockBean private SellerAdminV1ApiMapper mapper;

    private static final Long SELLER_ID = 1L;

    @Nested
    @DisplayName("셀러 목록 검색 API")
    class FetchSellersTest {

        @Test
        @DisplayName("셀러 목록 검색 성공")
        void fetchSellers_Success() throws Exception {
            // given
            SellerPageResult pageResult = SellerAdminApiFixtures.sellerPageResult();
            CustomPageableV1ApiResponse<SellerV1ApiResponse> pageResponse =
                    SellerAdminApiFixtures.sellerPageResponse();

            given(mapper.toSearchParams(any())).willReturn(null);
            given(searchSellerByOffsetUseCase.execute(any())).willReturn(pageResult);
            given(mapper.toPageResponse(any())).willReturn(pageResponse);

            // when & then
            mockMvc.perform(
                            get(SellerAdminV1Endpoints.SELLERS)
                                    .param("searchKeyword", "SELLER_NAME")
                                    .param("searchWord", "테스트")
                                    .param("page", "0")
                                    .param("size", "20"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.content").isArray())
                    .andExpect(jsonPath("$.data.content[0].sellerId").value(1L))
                    .andExpect(jsonPath("$.data.totalElements").value(2L))
                    .andExpect(jsonPath("$.response.status").value(200))
                    .andExpect(jsonPath("$.response.message").value("success"))
                    .andDo(
                            document.document(
                                    queryParameters(
                                            parameterWithName("searchKeyword")
                                                    .description(
                                                            "검색 키워드 타입 (SELLER_ID, SELLER_NAME)")
                                                    .optional(),
                                            parameterWithName("searchWord")
                                                    .description("검색어")
                                                    .optional(),
                                            parameterWithName("siteIds")
                                                    .description("사이트 ID 목록")
                                                    .optional(),
                                            parameterWithName("status")
                                                    .description(
                                                            "승인 상태 (PENDING, APPROVED, REJECTED)")
                                                    .optional(),
                                            parameterWithName("page")
                                                    .description("페이지 번호 (0부터 시작)")
                                                    .optional(),
                                            parameterWithName("size")
                                                    .description("페이지 크기 (기본값 20)")
                                                    .optional()),
                                    responseFields(
                                            fieldWithPath("data")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("페이지 응답 데이터"),
                                            fieldWithPath("data.content[]")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("셀러 목록"),
                                            fieldWithPath("data.content[].sellerId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("셀러 ID"),
                                            fieldWithPath("data.content[].sellerName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("셀러명"),
                                            fieldWithPath("data.content[].displayName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("표시명"),
                                            fieldWithPath("data.content[].logoUrl")
                                                    .type(JsonFieldType.STRING)
                                                    .description("로고 URL"),
                                            fieldWithPath("data.content[].description")
                                                    .type(JsonFieldType.STRING)
                                                    .description("설명"),
                                            fieldWithPath("data.content[].active")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("활성화 여부"),
                                            fieldWithPath("data.content[].createdAt")
                                                    .type(JsonFieldType.STRING)
                                                    .description("생성일시"),
                                            fieldWithPath("data.content[].updatedAt")
                                                    .type(JsonFieldType.STRING)
                                                    .description("수정일시"),
                                            fieldWithPath("data.pageable")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("페이지 정보"),
                                            fieldWithPath("data.pageable.pageNumber")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("페이지 번호"),
                                            fieldWithPath("data.pageable.pageSize")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("페이지 크기"),
                                            fieldWithPath("data.pageable.offset")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("오프셋"),
                                            fieldWithPath("data.pageable.paged")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("페이징 여부"),
                                            fieldWithPath("data.pageable.unpaged")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("비페이징 여부"),
                                            fieldWithPath("data.totalElements")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("전체 데이터 개수"),
                                            fieldWithPath("data.totalPages")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("전체 페이지 수"),
                                            fieldWithPath("data.last")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("마지막 페이지 여부"),
                                            fieldWithPath("data.first")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("첫 페이지 여부"),
                                            fieldWithPath("data.numberOfElements")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("현재 페이지 데이터 개수"),
                                            fieldWithPath("data.size")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("페이지 크기"),
                                            fieldWithPath("data.number")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("현재 페이지 번호 (0부터 시작)"),
                                            fieldWithPath("data.sort")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("정렬 정보"),
                                            fieldWithPath("data.sort.unsorted")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("비정렬 여부"),
                                            fieldWithPath("data.sort.sorted")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("정렬 여부"),
                                            fieldWithPath("data.sort.empty")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("빈 정렬 여부"),
                                            fieldWithPath("data.empty")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("빈 페이지 여부"),
                                            fieldWithPath("data.lastDomainId")
                                                    .type(JsonFieldType.NULL)
                                                    .description("마지막 도메인 ID")
                                                    .optional(),
                                            fieldWithPath("response")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("응답 메타 정보"),
                                            fieldWithPath("response.status")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("HTTP 상태 코드"),
                                            fieldWithPath("response.message")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 메시지"))));
        }
    }

    @Nested
    @DisplayName("셀러 상세 조회 API")
    class FetchSellerTest {

        @Test
        @DisplayName("셀러 상세 조회 성공")
        void fetchSeller_Success() throws Exception {
            // given
            SellerFullCompositeResult compositeResult =
                    SellerAdminApiFixtures.sellerFullCompositeResult(SELLER_ID);
            SellerDetailV1ApiResponse detailResponse =
                    SellerAdminApiFixtures.sellerDetailApiResponse(SELLER_ID);

            given(getSellerForAdminUseCase.execute(SELLER_ID)).willReturn(compositeResult);
            given(mapper.toDetailResponse(compositeResult)).willReturn(detailResponse);

            // when & then
            mockMvc.perform(get(SellerAdminV1Endpoints.SELLERS + "/{sellerId}", SELLER_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.seller.sellerId").value(SELLER_ID))
                    .andExpect(jsonPath("$.data.seller.sellerName").value("테스트셀러"))
                    .andExpect(jsonPath("$.data.address.addressId").value(1L))
                    .andExpect(jsonPath("$.data.businessInfo.businessInfoId").value(1L))
                    .andExpect(jsonPath("$.data.csInfo.csInfoId").value(1L))
                    .andExpect(jsonPath("$.data.contractInfo.contractId").value(1L))
                    .andExpect(jsonPath("$.data.settlementInfo.settlementId").value(1L))
                    .andExpect(jsonPath("$.response.status").value(200))
                    .andExpect(jsonPath("$.response.message").value("success"))
                    .andDo(
                            document.document(
                                    pathParameters(
                                            parameterWithName("sellerId").description("셀러 ID")),
                                    responseFields(
                                            fieldWithPath("data")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("응답 데이터"),
                                            // Seller Info
                                            fieldWithPath("data.seller")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("셀러 기본 정보"),
                                            fieldWithPath("data.seller.sellerId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("셀러 ID"),
                                            fieldWithPath("data.seller.sellerName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("셀러명"),
                                            fieldWithPath("data.seller.displayName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("표시명"),
                                            fieldWithPath("data.seller.logoUrl")
                                                    .type(JsonFieldType.STRING)
                                                    .description("로고 URL"),
                                            fieldWithPath("data.seller.description")
                                                    .type(JsonFieldType.STRING)
                                                    .description("설명"),
                                            fieldWithPath("data.seller.active")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("활성화 여부"),
                                            fieldWithPath("data.seller.createdAt")
                                                    .type(JsonFieldType.STRING)
                                                    .description("생성일시"),
                                            fieldWithPath("data.seller.updatedAt")
                                                    .type(JsonFieldType.STRING)
                                                    .description("수정일시"),
                                            // Address Info
                                            fieldWithPath("data.address")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("주소 정보"),
                                            fieldWithPath("data.address.addressId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("주소 ID"),
                                            fieldWithPath("data.address.addressType")
                                                    .type(JsonFieldType.STRING)
                                                    .description("주소 유형"),
                                            fieldWithPath("data.address.addressName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("주소명"),
                                            fieldWithPath("data.address.zipcode")
                                                    .type(JsonFieldType.STRING)
                                                    .description("우편번호"),
                                            fieldWithPath("data.address.address")
                                                    .type(JsonFieldType.STRING)
                                                    .description("주소"),
                                            fieldWithPath("data.address.addressDetail")
                                                    .type(JsonFieldType.STRING)
                                                    .description("상세주소"),
                                            fieldWithPath("data.address.contactName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("연락처 이름"),
                                            fieldWithPath("data.address.contactPhone")
                                                    .type(JsonFieldType.STRING)
                                                    .description("연락처 전화번호"),
                                            fieldWithPath("data.address.defaultAddress")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("기본 주소 여부"),
                                            // Business Info
                                            fieldWithPath("data.businessInfo")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("사업자 정보"),
                                            fieldWithPath("data.businessInfo.businessInfoId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("사업자정보 ID"),
                                            fieldWithPath("data.businessInfo.registrationNumber")
                                                    .type(JsonFieldType.STRING)
                                                    .description("사업자등록번호"),
                                            fieldWithPath("data.businessInfo.companyName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("회사명"),
                                            fieldWithPath("data.businessInfo.representative")
                                                    .type(JsonFieldType.STRING)
                                                    .description("대표자명"),
                                            fieldWithPath("data.businessInfo.saleReportNumber")
                                                    .type(JsonFieldType.STRING)
                                                    .description("통신판매업 신고번호"),
                                            fieldWithPath("data.businessInfo.businessZipcode")
                                                    .type(JsonFieldType.STRING)
                                                    .description("사업장 우편번호"),
                                            fieldWithPath("data.businessInfo.businessAddress")
                                                    .type(JsonFieldType.STRING)
                                                    .description("사업장 주소"),
                                            fieldWithPath("data.businessInfo.businessAddressDetail")
                                                    .type(JsonFieldType.STRING)
                                                    .description("사업장 상세주소"),
                                            // CS Info
                                            fieldWithPath("data.csInfo")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("고객센터 정보"),
                                            fieldWithPath("data.csInfo.csInfoId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("CS ID"),
                                            fieldWithPath("data.csInfo.csPhone")
                                                    .type(JsonFieldType.STRING)
                                                    .description("고객센터 전화번호"),
                                            fieldWithPath("data.csInfo.csMobile")
                                                    .type(JsonFieldType.STRING)
                                                    .description("고객센터 휴대전화"),
                                            fieldWithPath("data.csInfo.csEmail")
                                                    .type(JsonFieldType.STRING)
                                                    .description("고객센터 이메일"),
                                            fieldWithPath("data.csInfo.operatingStartTime")
                                                    .type(JsonFieldType.STRING)
                                                    .description("운영 시작 시간"),
                                            fieldWithPath("data.csInfo.operatingEndTime")
                                                    .type(JsonFieldType.STRING)
                                                    .description("운영 종료 시간"),
                                            fieldWithPath("data.csInfo.operatingDays")
                                                    .type(JsonFieldType.STRING)
                                                    .description("운영 요일"),
                                            fieldWithPath("data.csInfo.kakaoChannelUrl")
                                                    .type(JsonFieldType.STRING)
                                                    .description("카카오 채널 URL"),
                                            // Contract Info
                                            fieldWithPath("data.contractInfo")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("계약 정보"),
                                            fieldWithPath("data.contractInfo.contractId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("계약 ID"),
                                            fieldWithPath("data.contractInfo.commissionRate")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("수수료율"),
                                            fieldWithPath("data.contractInfo.contractStartDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description("계약 시작일"),
                                            fieldWithPath("data.contractInfo.contractEndDate")
                                                    .type(JsonFieldType.STRING)
                                                    .description("계약 종료일"),
                                            fieldWithPath("data.contractInfo.status")
                                                    .type(JsonFieldType.STRING)
                                                    .description("계약 상태"),
                                            fieldWithPath("data.contractInfo.specialTerms")
                                                    .type(JsonFieldType.STRING)
                                                    .description("특약 사항"),
                                            fieldWithPath("data.contractInfo.createdAt")
                                                    .type(JsonFieldType.STRING)
                                                    .description("생성일시"),
                                            fieldWithPath("data.contractInfo.updatedAt")
                                                    .type(JsonFieldType.STRING)
                                                    .description("수정일시"),
                                            // Settlement Info
                                            fieldWithPath("data.settlementInfo")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("정산 정보"),
                                            fieldWithPath("data.settlementInfo.settlementId")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("정산 ID"),
                                            fieldWithPath("data.settlementInfo.bankCode")
                                                    .type(JsonFieldType.STRING)
                                                    .description("은행 코드"),
                                            fieldWithPath("data.settlementInfo.bankName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("은행명"),
                                            fieldWithPath("data.settlementInfo.accountNumber")
                                                    .type(JsonFieldType.STRING)
                                                    .description("계좌번호"),
                                            fieldWithPath("data.settlementInfo.accountHolderName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("예금주명"),
                                            fieldWithPath("data.settlementInfo.settlementCycle")
                                                    .type(JsonFieldType.STRING)
                                                    .description("정산 주기"),
                                            fieldWithPath("data.settlementInfo.settlementDay")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("정산일"),
                                            fieldWithPath("data.settlementInfo.verified")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("인증 여부"),
                                            fieldWithPath("data.settlementInfo.verifiedAt")
                                                    .type(JsonFieldType.STRING)
                                                    .description("인증일시"),
                                            fieldWithPath("data.settlementInfo.createdAt")
                                                    .type(JsonFieldType.STRING)
                                                    .description("생성일시"),
                                            fieldWithPath("data.settlementInfo.updatedAt")
                                                    .type(JsonFieldType.STRING)
                                                    .description("수정일시"),
                                            fieldWithPath("response")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("응답 메타 정보"),
                                            fieldWithPath("response.status")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("HTTP 상태 코드"),
                                            fieldWithPath("response.message")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 메시지"))));
        }
    }

    @Nested
    @DisplayName("사업자등록번호 유효성 검증 API")
    class ValidateBusinessRegistrationTest {

        @Test
        @DisplayName("사업자등록번호 유효성 검증 성공 - 사용 가능")
        void validateBusinessRegistration_Available() throws Exception {
            // given
            given(validateBusinessRegistrationUseCase.execute(anyString())).willReturn(true);

            // when & then
            mockMvc.perform(
                            get(SellerAdminV1Endpoints.SELLERS
                                            + SellerAdminV1Endpoints.BUSINESS_VALIDATION)
                                    .param("registrationNumber", "123-45-67890"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").value(true))
                    .andExpect(jsonPath("$.response.status").value(200))
                    .andExpect(jsonPath("$.response.message").value("success"))
                    .andDo(
                            document.document(
                                    queryParameters(
                                            parameterWithName("registrationNumber")
                                                    .description(
                                                            "사업자등록번호 (형식: 000-00-00000 또는"
                                                                    + " 0000000000)")),
                                    responseFields(
                                            fieldWithPath("data")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description(
                                                            "사용 가능 여부 (true: 사용 가능, false: 이미"
                                                                    + " 등록됨)"),
                                            fieldWithPath("response")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("응답 메타 정보"),
                                            fieldWithPath("response.status")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("HTTP 상태 코드"),
                                            fieldWithPath("response.message")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 메시지"))));
        }

        @Test
        @DisplayName("사업자등록번호 유효성 검증 성공 - 이미 등록됨")
        void validateBusinessRegistration_NotAvailable() throws Exception {
            // given
            given(validateBusinessRegistrationUseCase.execute(anyString())).willReturn(false);

            // when & then
            mockMvc.perform(
                            get(SellerAdminV1Endpoints.SELLERS
                                            + SellerAdminV1Endpoints.BUSINESS_VALIDATION)
                                    .param("registrationNumber", "123-45-67890"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").value(false));
        }
    }
}
