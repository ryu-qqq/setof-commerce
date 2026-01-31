package com.ryuqq.setof.adapter.in.rest.admin.v2.seller.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.admin.common.dto.PageApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.seller.SellerApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.query.SearchSellersApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.response.SellerApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.response.SellerDetailApiResponse;
import com.ryuqq.setof.application.seller.dto.composite.SellerCompositeResult;
import com.ryuqq.setof.application.seller.dto.composite.SellerFullCompositeResult;
import com.ryuqq.setof.application.seller.dto.query.SellerSearchParams;
import com.ryuqq.setof.application.seller.dto.response.SellerPageResult;
import com.ryuqq.setof.application.seller.dto.response.SellerResult;
import java.time.Instant;
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
 * @since 1.0.0
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
    @DisplayName("toSearchParams(SearchSellersApiRequest)")
    class ToSearchParamsTest {

        @Test
        @DisplayName("검색 요청을 SearchParams로 변환한다")
        void toSearchParams_Success() {
            // given
            SearchSellersApiRequest request =
                    new SearchSellersApiRequest(true, "sellerName", "테스트", "id", "ASC", 1, 10);

            // when
            SellerSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.active()).isTrue();
            assertThat(params.searchField()).isEqualTo("sellerName");
            assertThat(params.searchWord()).isEqualTo("테스트");
            assertThat(params.searchParams().page()).isEqualTo(1);
            assertThat(params.searchParams().size()).isEqualTo(10);
            assertThat(params.searchParams().sortKey()).isEqualTo("id");
            assertThat(params.searchParams().sortDirection()).isEqualTo("ASC");
        }

        @Test
        @DisplayName("null 값에 대해 기본값을 적용한다")
        void toSearchParams_WithNullValues_AppliesDefaults() {
            // given
            SearchSellersApiRequest request =
                    new SearchSellersApiRequest(null, null, null, null, null, null, null);

            // when
            SellerSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.searchParams().page()).isEqualTo(0);
            assertThat(params.searchParams().size()).isEqualTo(20);
            assertThat(params.searchParams().sortKey()).isEqualTo("createdAt");
            assertThat(params.searchParams().sortDirection()).isEqualTo("DESC");
        }
    }

    @Nested
    @DisplayName("toResponse(SellerResult)")
    class ToResponseTest {

        @Test
        @DisplayName("SellerResult를 SellerApiResponse로 변환한다")
        void toResponse_Success() {
            // given
            SellerResult result = SellerApiFixtures.sellerResult(1L);

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
    }

    @Nested
    @DisplayName("toPageResponse(SellerPageResult)")
    class ToPageResponseTest {

        @Test
        @DisplayName("SellerPageResult를 PageApiResponse로 변환한다")
        void toPageResponse_Success() {
            // given
            SellerPageResult pageResult = SellerApiFixtures.sellerPageResult();

            // when
            PageApiResponse<SellerApiResponse> response = mapper.toPageResponse(pageResult);

            // then
            assertThat(response.content()).hasSize(2);
            assertThat(response.page()).isEqualTo(pageResult.page());
            assertThat(response.size()).isEqualTo(pageResult.size());
            assertThat(response.totalElements()).isEqualTo(pageResult.totalCount());
        }
    }

    @Nested
    @DisplayName("toDetailResponse(SellerFullCompositeResult)")
    class ToDetailResponseTest {

        @Test
        @DisplayName("SellerFullCompositeResult를 SellerDetailApiResponse로 변환한다")
        void toDetailResponse_Success() {
            // given
            SellerFullCompositeResult result = SellerApiFixtures.sellerFullCompositeResult(1L);

            // when
            SellerDetailApiResponse response = mapper.toDetailResponse(result);

            // then
            assertThat(response.seller()).isNotNull();
            assertThat(response.seller().id()).isEqualTo(1L);
            assertThat(response.seller().sellerName()).isNotNull();
        }

        @Test
        @DisplayName("null 주소 정보를 처리한다")
        void toDetailResponse_WithNullAddress() {
            // given
            Instant now = Instant.now();
            SellerCompositeResult.SellerInfo sellerInfo =
                    new SellerCompositeResult.SellerInfo(
                            1L, "테스트셀러", "테스트 브랜드", null, null, true, now, now);
            SellerCompositeResult composite =
                    new SellerCompositeResult(sellerInfo, null, null, null);
            SellerFullCompositeResult result =
                    new SellerFullCompositeResult(composite, null, null, null);

            // when
            SellerDetailApiResponse response = mapper.toDetailResponse(result);

            // then
            assertThat(response.seller()).isNotNull();
            assertThat(response.address()).isNull();
            assertThat(response.businessInfo()).isNull();
            assertThat(response.csInfo()).isNull();
        }
    }
}
