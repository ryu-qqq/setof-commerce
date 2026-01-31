package com.ryuqq.setof.adapter.in.rest.admin.v2.seller.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ryuqq.setof.adapter.in.rest.admin.common.RestDocsTestSupport;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.PageApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.seller.SellerApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.response.SellerApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.response.SellerDetailApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.mapper.SellerQueryApiMapper;
import com.ryuqq.setof.application.seller.dto.composite.SellerFullCompositeResult;
import com.ryuqq.setof.application.seller.dto.response.SellerPageResult;
import com.ryuqq.setof.application.seller.port.in.query.GetSellerForAdminUseCase;
import com.ryuqq.setof.application.seller.port.in.query.SearchSellerByOffsetUseCase;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

/**
 * SellerQueryV2Controller REST Docs 테스트.
 *
 * <p>셀러 Query V2 API의 REST Docs 스니펫을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("SellerQueryV2Controller REST Docs 테스트")
@WebMvcTest(SellerQueryV2Controller.class)
@WithMockUser
class SellerQueryV2ControllerRestDocsTest extends RestDocsTestSupport {

    @MockBean private GetSellerForAdminUseCase getSellerForAdminUseCase;

    @MockBean private SearchSellerByOffsetUseCase searchSellerByOffsetUseCase;

    @MockBean private SellerQueryApiMapper mapper;

    private static final Long SELLER_ID = 1L;

    @Nested
    @DisplayName("셀러 상세 조회 API")
    class GetSellerTest {

        @Test
        @DisplayName("셀러 상세 조회 성공")
        void getSeller_Success() throws Exception {
            // given
            SellerFullCompositeResult compositeResult =
                    SellerApiFixtures.sellerFullCompositeResult(SELLER_ID);

            SellerDetailApiResponse detailResponse =
                    SellerApiFixtures.sellerDetailApiResponse(SELLER_ID);

            given(getSellerForAdminUseCase.execute(SELLER_ID)).willReturn(compositeResult);
            given(mapper.toDetailResponse(compositeResult)).willReturn(detailResponse);

            // when & then
            mockMvc.perform(get("/api/v2/admin/sellers/{sellerId}", SELLER_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.seller.id").value(SELLER_ID))
                    .andExpect(jsonPath("$.data.seller.sellerName").value("테스트셀러"))
                    .andDo(
                            document.document(
                                    pathParameters(
                                            parameterWithName("sellerId").description("셀러 ID")),
                                    responseFields(
                                            fieldWithPath("data.seller")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("셀러 기본 정보"),
                                            fieldWithPath("data.seller.id")
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
                                            fieldWithPath("data.address")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("주소 정보"),
                                            fieldWithPath("data.address.id")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("주소 ID"),
                                            fieldWithPath("data.address.addressType")
                                                    .type(JsonFieldType.STRING)
                                                    .description("주소 타입"),
                                            fieldWithPath("data.address.addressName")
                                                    .type(JsonFieldType.STRING)
                                                    .description("주소 이름"),
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
                                                    .description("담당자명"),
                                            fieldWithPath("data.address.contactPhone")
                                                    .type(JsonFieldType.STRING)
                                                    .description("담당자 연락처"),
                                            fieldWithPath("data.address.defaultAddress")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("기본 주소 여부"),
                                            fieldWithPath("data.businessInfo")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("사업자 정보"),
                                            fieldWithPath("data.businessInfo.id")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("사업자 정보 ID"),
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
                                            fieldWithPath("data.csInfo")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("CS 정보"),
                                            fieldWithPath("data.csInfo.id")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("CS ID"),
                                            fieldWithPath("data.csInfo.csPhone")
                                                    .type(JsonFieldType.STRING)
                                                    .description("고객센터 전화번호"),
                                            fieldWithPath("data.csInfo.csMobile")
                                                    .type(JsonFieldType.STRING)
                                                    .description("고객센터 휴대폰번호"),
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
                                            fieldWithPath("data.contractInfo")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("계약 정보"),
                                            fieldWithPath("data.contractInfo.id")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("계약 ID"),
                                            fieldWithPath("data.contractInfo.commissionRate")
                                                    .type(JsonFieldType.STRING)
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
                                                    .description("특별 조건"),
                                            fieldWithPath("data.contractInfo.createdAt")
                                                    .type(JsonFieldType.STRING)
                                                    .description("생성일시"),
                                            fieldWithPath("data.contractInfo.updatedAt")
                                                    .type(JsonFieldType.STRING)
                                                    .description("수정일시"),
                                            fieldWithPath("data.settlementInfo")
                                                    .type(JsonFieldType.OBJECT)
                                                    .description("정산 정보"),
                                            fieldWithPath("data.settlementInfo.id")
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
                                            fieldWithPath("timestamp")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 시각")
                                                    .optional(),
                                            fieldWithPath("requestId")
                                                    .type(JsonFieldType.STRING)
                                                    .description("요청 ID")
                                                    .optional())));
        }
    }

    @Nested
    @DisplayName("셀러 목록 검색 API")
    class SearchSellersTest {

        @Test
        @DisplayName("셀러 목록 검색 성공")
        void searchSellers_Success() throws Exception {
            // given
            SellerPageResult pageResult = SellerApiFixtures.sellerPageResult();
            PageApiResponse<SellerApiResponse> pageResponse =
                    PageApiResponse.of(
                            List.of(
                                    new SellerApiResponse(
                                            1L,
                                            "테스트셀러1",
                                            "테스트 브랜드1",
                                            "https://example.com/logo1.png",
                                            "테스트 설명1",
                                            true,
                                            "2025-01-23T10:30:00+09:00",
                                            "2025-01-23T10:30:00+09:00"),
                                    new SellerApiResponse(
                                            2L,
                                            "테스트셀러2",
                                            "테스트 브랜드2",
                                            "https://example.com/logo2.png",
                                            "테스트 설명2",
                                            true,
                                            "2025-01-23T10:30:00+09:00",
                                            "2025-01-23T10:30:00+09:00")),
                            0,
                            20,
                            2L);

            given(mapper.toSearchParams(any())).willReturn(null);
            given(searchSellerByOffsetUseCase.execute(any())).willReturn(pageResult);
            given(mapper.toPageResponse(any())).willReturn(pageResponse);

            // when & then
            mockMvc.perform(
                            get("/api/v2/admin/sellers")
                                    .param("active", "true")
                                    .param("searchField", "sellerName")
                                    .param("searchValue", "테스트")
                                    .param("page", "0")
                                    .param("size", "20"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.content").isArray())
                    .andExpect(jsonPath("$.data.totalElements").value(2))
                    .andDo(
                            document.document(
                                    queryParameters(
                                            parameterWithName("active")
                                                    .description(
                                                            "활성화 여부 필터 +\n"
                                                                    + "true: 활성만, false: 비활성만 +\n"
                                                                    + "미입력 시 전체 조회")
                                                    .optional(),
                                            parameterWithName("searchField")
                                                    .description(
                                                            "검색 필드 +\n"
                                                                    + "sellerId: 셀러 ID, "
                                                                    + "sellerName: 셀러명")
                                                    .optional(),
                                            parameterWithName("searchValue")
                                                    .description("검색어 +\n" + "searchField 지정 시 필수")
                                                    .optional(),
                                            parameterWithName("sortKey")
                                                    .description(
                                                            "정렬 키 +\n" + "CREATED_AT: 생성일 (기본값)")
                                                    .optional(),
                                            parameterWithName("sortDirection")
                                                    .description(
                                                            "정렬 방향 +\n"
                                                                    + "DESC: 내림차순 (기본값), "
                                                                    + "ASC: 오름차순")
                                                    .optional(),
                                            parameterWithName("page")
                                                    .description("페이지 번호 [0 이상] +\n" + "기본값: 0")
                                                    .optional(),
                                            parameterWithName("size")
                                                    .description("페이지 크기 [1~100] +\n" + "기본값: 20")
                                                    .optional()),
                                    responseFields(
                                            fieldWithPath("data.content")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("셀러 목록"),
                                            fieldWithPath("data.content[].id")
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
                                            fieldWithPath("data.page")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("현재 페이지"),
                                            fieldWithPath("data.size")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("페이지 크기"),
                                            fieldWithPath("data.totalElements")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("총 개수"),
                                            fieldWithPath("data.totalPages")
                                                    .type(JsonFieldType.NUMBER)
                                                    .description("전체 페이지 수"),
                                            fieldWithPath("data.first")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("첫 페이지 여부"),
                                            fieldWithPath("data.last")
                                                    .type(JsonFieldType.BOOLEAN)
                                                    .description("마지막 페이지 여부"),
                                            fieldWithPath("timestamp")
                                                    .type(JsonFieldType.STRING)
                                                    .description("응답 시각")
                                                    .optional(),
                                            fieldWithPath("requestId")
                                                    .type(JsonFieldType.STRING)
                                                    .description("요청 ID")
                                                    .optional())));
        }
    }
}
