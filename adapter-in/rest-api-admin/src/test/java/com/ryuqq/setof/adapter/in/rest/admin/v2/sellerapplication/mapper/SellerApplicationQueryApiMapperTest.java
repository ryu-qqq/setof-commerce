package com.ryuqq.setof.adapter.in.rest.admin.v2.sellerapplication.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.admin.common.dto.PageApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.sellerapplication.SellerApplicationApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v2.sellerapplication.dto.query.SearchSellerApplicationsApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.sellerapplication.dto.response.SellerApplicationApiResponse;
import com.ryuqq.setof.application.sellerapplication.dto.query.SellerApplicationSearchParams;
import com.ryuqq.setof.application.sellerapplication.dto.response.SellerApplicationPageResult;
import com.ryuqq.setof.application.sellerapplication.dto.response.SellerApplicationResult;
import com.ryuqq.setof.domain.sellerapplication.vo.ApplicationStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * SellerApplicationQueryApiMapper 단위 테스트.
 *
 * <p>Query API Mapper의 변환 로직을 테스트합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("SellerApplicationQueryApiMapper 단위 테스트")
class SellerApplicationQueryApiMapperTest {

    private SellerApplicationQueryApiMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new SellerApplicationQueryApiMapper();
    }

    @Nested
    @DisplayName("toSearchParams(SearchSellerApplicationsApiRequest)")
    class ToSearchParamsTest {

        @Test
        @DisplayName("검색 요청을 SearchParams로 변환한다")
        void toSearchParams_Success() {
            // given
            SearchSellerApplicationsApiRequest request =
                    new SearchSellerApplicationsApiRequest(
                            "PENDING", "sellerName", "테스트", "id", "ASC", 1, 10);

            // when
            SellerApplicationSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.status()).isEqualTo(ApplicationStatus.PENDING);
            assertThat(params.searchField()).isEqualTo("sellerName");
            assertThat(params.searchWord()).isEqualTo("테스트");
            assertThat(params.searchParams().page()).isEqualTo(1);
            assertThat(params.searchParams().size()).isEqualTo(10);
        }

        @Test
        @DisplayName("null 값에 대해 기본값을 적용한다")
        void toSearchParams_WithNullValues_AppliesDefaults() {
            // given
            SearchSellerApplicationsApiRequest request =
                    new SearchSellerApplicationsApiRequest(
                            null, null, null, null, null, null, null);

            // when
            SellerApplicationSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.status()).isNull();
            assertThat(params.searchParams().page()).isEqualTo(0);
            assertThat(params.searchParams().size()).isEqualTo(20);
            assertThat(params.searchParams().sortKey()).isEqualTo("appliedAt");
            assertThat(params.searchParams().sortDirection()).isEqualTo("DESC");
        }

        @Test
        @DisplayName("잘못된 상태값은 null로 변환한다")
        void toSearchParams_InvalidStatus_ReturnsNull() {
            // given
            SearchSellerApplicationsApiRequest request =
                    new SearchSellerApplicationsApiRequest(
                            "INVALID_STATUS", null, null, null, null, null, null);

            // when
            SellerApplicationSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.status()).isNull();
        }
    }

    @Nested
    @DisplayName("toResponse(SellerApplicationResult)")
    class ToResponseTest {

        @Test
        @DisplayName("SellerApplicationResult를 SellerApplicationApiResponse로 변환한다")
        void toResponse_Success() {
            // given
            SellerApplicationResult result = SellerApplicationApiFixtures.applicationResult(1L);

            // when
            SellerApplicationApiResponse response = mapper.toResponse(result);

            // then
            assertThat(response.id()).isEqualTo(result.id());
            assertThat(response.status()).isEqualTo(result.status());
            assertThat(response.sellerInfo()).isNotNull();
            assertThat(response.businessInfo()).isNotNull();
            assertThat(response.csContact()).isNotNull();
        }
    }

    @Nested
    @DisplayName("toPageResponse(SellerApplicationPageResult)")
    class ToPageResponseTest {

        @Test
        @DisplayName("SellerApplicationPageResult를 PageApiResponse로 변환한다")
        void toPageResponse_Success() {
            // given
            SellerApplicationPageResult pageResult =
                    SellerApplicationApiFixtures.applicationPageResult();

            // when
            PageApiResponse<SellerApplicationApiResponse> response =
                    mapper.toPageResponse(pageResult);

            // then
            assertThat(response.content()).hasSize(2);
            assertThat(response.page()).isEqualTo(pageResult.page());
            assertThat(response.size()).isEqualTo(pageResult.size());
            assertThat(response.totalElements()).isEqualTo(pageResult.totalCount());
        }
    }
}
