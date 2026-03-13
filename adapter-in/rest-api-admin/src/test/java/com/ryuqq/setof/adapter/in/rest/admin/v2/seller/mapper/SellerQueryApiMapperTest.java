package com.ryuqq.setof.adapter.in.rest.admin.v2.seller.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.admin.common.dto.PageApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.seller.SellerApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.query.SearchSellersApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.response.SellerApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.response.SellerDetailApiResponse;
import com.ryuqq.setof.application.seller.dto.composite.SellerCompositeResult;
import com.ryuqq.setof.application.seller.dto.query.SellerSearchParams;
import com.ryuqq.setof.application.seller.dto.response.SellerPageResult;
import com.ryuqq.setof.application.seller.dto.response.SellerResult;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * SellerQueryApiMapper 단위 테스트.
 *
 * <p>Query API Mapper의 변환 로직을 테스트합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("SellerQueryApiMapper 단위 테스트")
class SellerQueryApiMapperTest {

    private SellerQueryApiMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new SellerQueryApiMapper();
    }

    @Nested
    @DisplayName("toSearchParams")
    class ToSearchParamsTest {

        @Test
        @DisplayName("검색 요청을 SellerSearchParams로 변환한다")
        void toSearchParams_Success() {
            // given
            SearchSellersApiRequest request = SellerApiFixtures.searchRequest();

            // when
            SellerSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.active()).isEqualTo(request.active());
            assertThat(params.searchField()).isEqualTo(request.searchField());
            assertThat(params.searchWord()).isEqualTo(request.searchWord());
            assertThat(params.sortKey()).isEqualTo(request.sortKey());
            assertThat(params.sortDirection()).isEqualTo(request.sortDirection());
            assertThat(params.page()).isEqualTo(request.page());
            assertThat(params.size()).isEqualTo(request.size());
        }

        @Test
        @DisplayName("null 페이지 값을 기본값(0, 20)으로 처리한다")
        void toSearchParams_NullPage_UsesDefault() {
            // given
            SearchSellersApiRequest request = SellerApiFixtures.searchRequestWithNullPage();

            // when
            SellerSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.page()).isZero();
            assertThat(params.size()).isEqualTo(20);
        }

        @Test
        @DisplayName("active 필터를 포함한 검색 요청을 변환한다")
        void toSearchParams_WithActiveFilter() {
            // given
            SearchSellersApiRequest request = SellerApiFixtures.searchRequest(true, 1, 10);

            // when
            SellerSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.active()).isTrue();
            assertThat(params.page()).isEqualTo(1);
            assertThat(params.size()).isEqualTo(10);
        }
    }

    @Nested
    @DisplayName("toResponse")
    class ToResponseTest {

        @Test
        @DisplayName("SellerResult를 SellerApiResponse로 변환한다")
        void toResponse_Success() {
            // given
            SellerResult result = SellerApiFixtures.sellerResult();

            // when
            SellerApiResponse response = mapper.toResponse(result);

            // then
            assertThat(response.id()).isEqualTo(result.id());
            assertThat(response.sellerName()).isEqualTo(result.sellerName());
            assertThat(response.displayName()).isEqualTo(result.displayName());
            assertThat(response.logoUrl()).isEqualTo(result.logoUrl());
            assertThat(response.description()).isEqualTo(result.description());
            assertThat(response.active()).isEqualTo(result.active());
            assertThat(response.createdAt()).isNotNull();
            assertThat(response.updatedAt()).isNotNull();
        }

        @Test
        @DisplayName("비활성 셀러 Result를 Response로 변환한다")
        void toResponse_InactiveSeller() {
            // given
            SellerResult result = SellerApiFixtures.sellerResult(2L, false);

            // when
            SellerApiResponse response = mapper.toResponse(result);

            // then
            assertThat(response.id()).isEqualTo(2L);
            assertThat(response.active()).isFalse();
        }
    }

    @Nested
    @DisplayName("toPageResponse")
    class ToPageResponseTest {

        @Test
        @DisplayName("SellerPageResult를 PageApiResponse로 변환한다")
        void toPageResponse_Success() {
            // given
            SellerPageResult pageResult = SellerApiFixtures.sellerPageResult();

            // when
            PageApiResponse<SellerApiResponse> response = mapper.toPageResponse(pageResult);

            // then
            assertThat(response.content()).hasSize(1);
            assertThat(response.page()).isZero();
            assertThat(response.size()).isEqualTo(20);
            assertThat(response.totalElements()).isEqualTo(1);
        }

        @Test
        @DisplayName("빈 SellerPageResult를 빈 PageApiResponse로 변환한다")
        void toPageResponse_Empty_Success() {
            // given
            SellerPageResult emptyResult = SellerApiFixtures.emptySellerPageResult();

            // when
            PageApiResponse<SellerApiResponse> response = mapper.toPageResponse(emptyResult);

            // then
            assertThat(response.content()).isEmpty();
            assertThat(response.totalElements()).isZero();
        }

        @Test
        @DisplayName("여러 결과를 포함한 PageResult를 변환한다")
        void toPageResponse_Multiple_Success() {
            // given
            List<SellerResult> results = SellerApiFixtures.multipleSellerResults();
            SellerPageResult pageResult = SellerApiFixtures.sellerPageResult(results, 0, 20, 3);

            // when
            PageApiResponse<SellerApiResponse> response = mapper.toPageResponse(pageResult);

            // then
            assertThat(response.content()).hasSize(3);
            assertThat(response.totalElements()).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("toDetailResponse")
    class ToDetailResponseTest {

        @Test
        @DisplayName("SellerCompositeResult를 SellerDetailApiResponse로 변환한다")
        void toDetailResponse_Success() {
            // given
            SellerCompositeResult result = SellerApiFixtures.sellerCompositeResult();

            // when
            SellerDetailApiResponse response = mapper.toDetailResponse(result);

            // then
            assertThat(response.sellerInfo()).isNotNull();
            assertThat(response.sellerInfo().id()).isEqualTo(result.seller().id());
            assertThat(response.sellerInfo().sellerName()).isEqualTo(result.seller().sellerName());
            assertThat(response.sellerInfo().displayName())
                    .isEqualTo(result.seller().displayName());
            assertThat(response.sellerInfo().logoUrl()).isEqualTo(result.seller().logoUrl());
            assertThat(response.sellerInfo().description())
                    .isEqualTo(result.seller().description());
            assertThat(response.sellerInfo().active()).isEqualTo(result.seller().active());
            assertThat(response.sellerInfo().createdAt()).isNotNull();
            assertThat(response.sellerInfo().updatedAt()).isNotNull();
        }

        @Test
        @DisplayName(
                "SellerCompositeResult의 businessInfo를 SellerDetailApiResponse.BusinessInfo로 변환한다")
        void toDetailResponse_BusinessInfo_Success() {
            // given
            SellerCompositeResult result = SellerApiFixtures.sellerCompositeResult();

            // when
            SellerDetailApiResponse response = mapper.toDetailResponse(result);

            // then
            assertThat(response.businessInfo()).isNotNull();
            assertThat(response.businessInfo().id()).isEqualTo(result.businessInfo().id());
            assertThat(response.businessInfo().registrationNumber())
                    .isEqualTo(result.businessInfo().registrationNumber());
            assertThat(response.businessInfo().companyName())
                    .isEqualTo(result.businessInfo().companyName());
            assertThat(response.businessInfo().representative())
                    .isEqualTo(result.businessInfo().representative());
        }

        @Test
        @DisplayName("SellerCompositeResult의 csInfo를 SellerDetailApiResponse.CsInfo로 변환한다")
        void toDetailResponse_CsInfo_Success() {
            // given
            SellerCompositeResult result = SellerApiFixtures.sellerCompositeResult();

            // when
            SellerDetailApiResponse response = mapper.toDetailResponse(result);

            // then
            assertThat(response.csInfo()).isNotNull();
            assertThat(response.csInfo().id()).isEqualTo(result.csInfo().id());
            assertThat(response.csInfo().csPhone()).isEqualTo(result.csInfo().csPhone());
            assertThat(response.csInfo().csMobile()).isEqualTo(result.csInfo().csMobile());
            assertThat(response.csInfo().csEmail()).isEqualTo(result.csInfo().csEmail());
            assertThat(response.csInfo().operatingStartTime()).isNotNull();
            assertThat(response.csInfo().operatingEndTime()).isNotNull();
        }

        @Test
        @DisplayName("seller가 null인 SellerCompositeResult를 변환하면 sellerInfo가 null이다")
        void toDetailResponse_NullSeller_ReturnsNullSellerInfo() {
            // given
            SellerCompositeResult result = new SellerCompositeResult(null, null, null, null);

            // when
            SellerDetailApiResponse response = mapper.toDetailResponse(result);

            // then
            assertThat(response.sellerInfo()).isNull();
            assertThat(response.businessInfo()).isNull();
            assertThat(response.csInfo()).isNull();
        }
    }
}
