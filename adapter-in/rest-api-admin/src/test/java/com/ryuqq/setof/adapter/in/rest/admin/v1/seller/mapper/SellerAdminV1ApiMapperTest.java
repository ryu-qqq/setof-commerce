package com.ryuqq.setof.adapter.in.rest.admin.v1.seller.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.admin.v1.common.dto.CustomPageableV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.SellerAdminApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.request.SearchSellersV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.response.SellerDetailV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.response.SellerV1ApiResponse;
import com.ryuqq.setof.application.seller.dto.composite.SellerFullCompositeResult;
import com.ryuqq.setof.application.seller.dto.query.SellerSearchParams;
import com.ryuqq.setof.application.seller.dto.response.SellerPageResult;
import com.ryuqq.setof.application.seller.dto.response.SellerResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * SellerAdminV1ApiMapper 단위 테스트.
 *
 * <p>SellerAdminV1ApiMapper의 변환 로직을 테스트합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("SellerAdminV1ApiMapper 단위 테스트")
class SellerAdminV1ApiMapperTest {

    private SellerAdminV1ApiMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new SellerAdminV1ApiMapper();
    }

    @Nested
    @DisplayName("toSearchParams 메서드 테스트")
    class ToSearchParamsTest {

        @Test
        @DisplayName("SELLER_NAME 키워드를 sellerName 필드로 변환한다")
        void toSearchParams_SellerNameKeyword() {
            // given
            SearchSellersV1ApiRequest request = SellerAdminApiFixtures.searchRequest();

            // when
            SellerSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.searchField()).isEqualTo("sellerName");
            assertThat(params.searchWord()).isEqualTo("테스트셀러");
            assertThat(params.searchParams().page()).isEqualTo(0);
            assertThat(params.searchParams().size()).isEqualTo(20);
            assertThat(params.searchParams().sortKey()).isEqualTo("createdAt");
            assertThat(params.searchParams().sortDirection()).isEqualTo("DESC");
        }

        @Test
        @DisplayName("SELLER_ID 키워드를 id 필드로 변환한다")
        void toSearchParams_SellerIdKeyword() {
            // given
            SearchSellersV1ApiRequest request = SellerAdminApiFixtures.searchRequestById();

            // when
            SellerSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.searchField()).isEqualTo("id");
            assertThat(params.searchWord()).isEqualTo("1");
        }

        @Test
        @DisplayName("null 키워드는 기본값 sellerName으로 변환한다")
        void toSearchParams_NullKeyword_DefaultSellerName() {
            // given
            SearchSellersV1ApiRequest request = SellerAdminApiFixtures.searchRequestNullKeyword();

            // when
            SellerSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.searchField()).isEqualTo("sellerName");
        }

        @Test
        @DisplayName("빈 문자열 키워드는 기본값 sellerName으로 변환한다")
        void toSearchParams_BlankKeyword_DefaultSellerName() {
            // given
            SearchSellersV1ApiRequest request = SellerAdminApiFixtures.searchRequestBlankKeyword();

            // when
            SellerSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.searchField()).isEqualTo("sellerName");
        }

        @Test
        @DisplayName("null searchWord는 null로 유지한다")
        void toSearchParams_NullSearchWord() {
            // given
            SearchSellersV1ApiRequest request =
                    SellerAdminApiFixtures.searchRequestNullSearchWord();

            // when
            SellerSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.searchWord()).isNull();
        }

        @Test
        @DisplayName("공백 searchWord는 null로 변환한다")
        void toSearchParams_BlankSearchWord() {
            // given
            SearchSellersV1ApiRequest request =
                    SellerAdminApiFixtures.searchRequestBlankSearchWord();

            // when
            SellerSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.searchWord()).isNull();
        }

        @Test
        @DisplayName("page와 size에 null이면 기본값을 적용한다")
        void toSearchParams_DefaultPageAndSize() {
            // given
            SearchSellersV1ApiRequest request = SellerAdminApiFixtures.searchRequestDefaultValues();

            // when
            SellerSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.searchParams().page()).isEqualTo(0);
            assertThat(params.searchParams().size()).isEqualTo(20);
        }

        @Test
        @DisplayName("page와 size가 지정되면 해당 값을 사용한다")
        void toSearchParams_CustomPageAndSize() {
            // given
            SearchSellersV1ApiRequest request = SellerAdminApiFixtures.searchRequestWithPage(2, 50);

            // when
            SellerSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.searchParams().page()).isEqualTo(2);
            assertThat(params.searchParams().size()).isEqualTo(50);
        }
    }

    @Nested
    @DisplayName("toResponse 메서드 테스트")
    class ToResponseTest {

        @Test
        @DisplayName("SellerResult를 SellerV1ApiResponse로 변환한다")
        void toResponse_Success() {
            // given
            SellerResult result = SellerAdminApiFixtures.sellerResult(1L);

            // when
            SellerV1ApiResponse response = mapper.toResponse(result);

            // then
            assertThat(response.sellerId()).isEqualTo(1L);
            assertThat(response.sellerName()).isEqualTo(result.sellerName());
            assertThat(response.displayName()).isEqualTo(result.displayName());
            assertThat(response.logoUrl()).isEqualTo(result.logoUrl());
            assertThat(response.description()).isEqualTo(result.description());
            assertThat(response.active()).isEqualTo(result.active());
            assertThat(response.createdAt()).isEqualTo(result.createdAt());
            assertThat(response.updatedAt()).isEqualTo(result.updatedAt());
        }

        @Test
        @DisplayName("null id는 0L로 변환한다")
        void toResponse_NullId_ConvertToZero() {
            // given
            SellerResult result = SellerAdminApiFixtures.sellerResultNullId();

            // when
            SellerV1ApiResponse response = mapper.toResponse(result);

            // then
            assertThat(response.sellerId()).isEqualTo(0L);
        }
    }

    @Nested
    @DisplayName("toPageResponse 메서드 테스트")
    class ToPageResponseTest {

        @Test
        @DisplayName("SellerPageResult를 CustomPageableV1ApiResponse로 변환한다")
        void toPageResponse_Success() {
            // given
            SellerPageResult pageResult = SellerAdminApiFixtures.sellerPageResult();

            // when
            CustomPageableV1ApiResponse<SellerV1ApiResponse> response =
                    mapper.toPageResponse(pageResult);

            // then
            assertThat(response.content()).hasSize(2);
            assertThat(response.number()).isEqualTo(pageResult.pageMeta().page());
            assertThat(response.size()).isEqualTo(pageResult.pageMeta().size());
            assertThat(response.totalElements()).isEqualTo(pageResult.pageMeta().totalElements());
        }

        @Test
        @DisplayName("빈 페이지 결과를 변환한다")
        void toPageResponse_EmptyResult() {
            // given
            SellerPageResult pageResult = SellerAdminApiFixtures.emptySellerPageResult();

            // when
            CustomPageableV1ApiResponse<SellerV1ApiResponse> response =
                    mapper.toPageResponse(pageResult);

            // then
            assertThat(response.content()).isEmpty();
            assertThat(response.totalElements()).isEqualTo(0L);
        }
    }

    @Nested
    @DisplayName("toDetailResponse 메서드 테스트")
    class ToDetailResponseTest {

        @Test
        @DisplayName("SellerFullCompositeResult를 SellerDetailV1ApiResponse로 변환한다")
        void toDetailResponse_Success() {
            // given
            SellerFullCompositeResult result = SellerAdminApiFixtures.sellerFullCompositeResult(1L);

            // when
            SellerDetailV1ApiResponse response = mapper.toDetailResponse(result);

            // then
            // Seller Info
            assertThat(response.seller()).isNotNull();
            assertThat(response.seller().sellerId()).isEqualTo(1L);
            assertThat(response.seller().sellerName()).isEqualTo("테스트셀러");
            assertThat(response.seller().displayName()).isEqualTo("테스트 브랜드");
            assertThat(response.seller().logoUrl())
                    .isEqualTo("https://cdn.example.com/sellers/test.png");
            assertThat(response.seller().description()).isEqualTo("테스트 셀러입니다");
            assertThat(response.seller().active()).isTrue();
            assertThat(response.seller().createdAt()).isNotNull();
            assertThat(response.seller().updatedAt()).isNotNull();

            // Address Info
            assertThat(response.address()).isNotNull();
            assertThat(response.address().addressId()).isEqualTo(1L);
            assertThat(response.address().addressType()).isEqualTo("BUSINESS");
            assertThat(response.address().addressName()).isEqualTo("본사");
            assertThat(response.address().zipcode()).isEqualTo("12345");
            assertThat(response.address().address()).isEqualTo("서울시 강남구 테헤란로 123");
            assertThat(response.address().addressDetail()).isEqualTo("4층");
            assertThat(response.address().contactName()).isEqualTo("홍길동");
            assertThat(response.address().contactPhone()).isEqualTo("02-1234-5678");
            assertThat(response.address().defaultAddress()).isTrue();

            // Business Info
            assertThat(response.businessInfo()).isNotNull();
            assertThat(response.businessInfo().businessInfoId()).isEqualTo(1L);
            assertThat(response.businessInfo().registrationNumber()).isEqualTo("1234567890");
            assertThat(response.businessInfo().companyName()).isEqualTo("테스트주식회사");
            assertThat(response.businessInfo().representative()).isEqualTo("홍길동");
            assertThat(response.businessInfo().saleReportNumber()).isEqualTo("2024-서울강남-12345");
            assertThat(response.businessInfo().businessZipcode()).isEqualTo("12345");
            assertThat(response.businessInfo().businessAddress()).isEqualTo("서울시 강남구 테헤란로 123");
            assertThat(response.businessInfo().businessAddressDetail()).isEqualTo("4층");

            // CS Info
            assertThat(response.csInfo()).isNotNull();
            assertThat(response.csInfo().csInfoId()).isEqualTo(1L);
            assertThat(response.csInfo().csPhone()).isEqualTo("02-1234-5678");
            assertThat(response.csInfo().csMobile()).isEqualTo("010-1234-5678");
            assertThat(response.csInfo().csEmail()).isEqualTo("cs@test.com");
            assertThat(response.csInfo().operatingStartTime()).isEqualTo("09:00");
            assertThat(response.csInfo().operatingEndTime()).isEqualTo("18:00");
            assertThat(response.csInfo().operatingDays()).isEqualTo("월-금");
            assertThat(response.csInfo().kakaoChannelUrl()).isEqualTo("https://pf.kakao.com/test");

            // Contract Info
            assertThat(response.contractInfo()).isNotNull();
            assertThat(response.contractInfo().contractId()).isEqualTo(1L);
            assertThat(response.contractInfo().commissionRate())
                    .isEqualByComparingTo(java.math.BigDecimal.valueOf(5.0));
            assertThat(response.contractInfo().contractStartDate())
                    .isEqualTo(java.time.LocalDate.of(2024, 1, 1));
            assertThat(response.contractInfo().contractEndDate())
                    .isEqualTo(java.time.LocalDate.of(2025, 12, 31));
            assertThat(response.contractInfo().status()).isEqualTo("ACTIVE");
            assertThat(response.contractInfo().specialTerms()).isEqualTo("특약사항 없음");
            assertThat(response.contractInfo().createdAt()).isNotNull();
            assertThat(response.contractInfo().updatedAt()).isNotNull();

            // Settlement Info
            assertThat(response.settlementInfo()).isNotNull();
            assertThat(response.settlementInfo().settlementId()).isEqualTo(1L);
            assertThat(response.settlementInfo().bankCode()).isEqualTo("004");
            assertThat(response.settlementInfo().bankName()).isEqualTo("KB국민은행");
            assertThat(response.settlementInfo().accountNumber()).isEqualTo("123-456789-01-001");
            assertThat(response.settlementInfo().accountHolderName()).isEqualTo("테스트주식회사");
            assertThat(response.settlementInfo().settlementCycle()).isEqualTo("WEEKLY");
            assertThat(response.settlementInfo().settlementDay()).isEqualTo(5);
            assertThat(response.settlementInfo().verified()).isTrue();
            assertThat(response.settlementInfo().verifiedAt()).isNotNull();
            assertThat(response.settlementInfo().createdAt()).isNotNull();
            assertThat(response.settlementInfo().updatedAt()).isNotNull();
        }

        @Test
        @DisplayName("null 주소/사업자/CS/계약/정산 정보를 처리한다")
        void toDetailResponse_WithNullFields() {
            // given
            SellerFullCompositeResult result =
                    SellerAdminApiFixtures.sellerFullCompositeResultNullAddress(1L);

            // when
            SellerDetailV1ApiResponse response = mapper.toDetailResponse(result);

            // then
            assertThat(response.seller()).isNotNull();
            assertThat(response.address()).isNull();
            assertThat(response.businessInfo()).isNull();
            assertThat(response.csInfo()).isNull();
            assertThat(response.contractInfo()).isNull();
            assertThat(response.settlementInfo()).isNull();
        }
    }
}
