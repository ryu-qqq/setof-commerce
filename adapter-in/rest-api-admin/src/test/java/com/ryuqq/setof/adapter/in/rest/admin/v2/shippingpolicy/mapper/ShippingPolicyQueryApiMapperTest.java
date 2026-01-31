package com.ryuqq.setof.adapter.in.rest.admin.v2.shippingpolicy.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.admin.common.dto.PageApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.shippingpolicy.ShippingPolicyApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v2.shippingpolicy.dto.query.SearchShippingPoliciesPageApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.shippingpolicy.dto.response.ShippingPolicyApiResponse;
import com.ryuqq.setof.application.shippingpolicy.dto.query.ShippingPolicySearchParams;
import com.ryuqq.setof.application.shippingpolicy.dto.response.ShippingPolicyPageResult;
import com.ryuqq.setof.application.shippingpolicy.dto.response.ShippingPolicyResult;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * ShippingPolicyQueryApiMapper 단위 테스트.
 *
 * <p>Query API Mapper의 변환 로직을 테스트합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("ShippingPolicyQueryApiMapper 단위 테스트")
class ShippingPolicyQueryApiMapperTest {

    private ShippingPolicyQueryApiMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ShippingPolicyQueryApiMapper();
    }

    @Nested
    @DisplayName("toSearchParams")
    class ToSearchParamsTest {

        @Test
        @DisplayName("검색 요청을 SearchParams로 변환한다")
        void toSearchParams_Success() {
            // given
            Long sellerId = 1L;
            SearchShippingPoliciesPageApiRequest request =
                    ShippingPolicyApiFixtures.searchRequest();

            // when
            ShippingPolicySearchParams params = mapper.toSearchParams(sellerId, request);

            // then
            assertThat(params.sellerId()).isEqualTo(sellerId);
            assertThat(params.searchParams().sortKey()).isEqualTo(request.sortKey());
            assertThat(params.searchParams().sortDirection()).isEqualTo(request.sortDirection());
            assertThat(params.searchParams().page()).isEqualTo(request.page());
            assertThat(params.searchParams().size()).isEqualTo(request.size());
        }

        @Test
        @DisplayName("null 페이지 값을 기본값으로 처리한다")
        void toSearchParams_NullPage_UsesDefault() {
            // given
            Long sellerId = 1L;
            SearchShippingPoliciesPageApiRequest request =
                    new SearchShippingPoliciesPageApiRequest("CREATED_AT", "DESC", null, null);

            // when
            ShippingPolicySearchParams params = mapper.toSearchParams(sellerId, request);

            // then
            assertThat(params.searchParams().page()).isZero();
            assertThat(params.searchParams().size()).isEqualTo(20);
        }
    }

    @Nested
    @DisplayName("toResponse")
    class ToResponseTest {

        @Test
        @DisplayName("Result를 Response로 변환한다")
        void toResponse_Success() {
            // given
            ShippingPolicyResult result = ShippingPolicyApiFixtures.policyResult();

            // when
            ShippingPolicyApiResponse response = mapper.toResponse(result);

            // then
            assertThat(response.policyId()).isEqualTo(result.policyId());
            assertThat(response.policyName()).isEqualTo(result.policyName());
            assertThat(response.defaultPolicy()).isEqualTo(result.defaultPolicy());
            assertThat(response.active()).isEqualTo(result.active());
            assertThat(response.shippingFeeType()).isEqualTo(result.shippingFeeType());
            assertThat(response.shippingFeeTypeDisplayName())
                    .isEqualTo(result.shippingFeeTypeDisplayName());
            assertThat(response.baseFee()).isEqualTo(result.baseFee());
            assertThat(response.freeThreshold()).isEqualTo(result.freeThreshold());
            assertThat(response.createdAt()).isNotNull();
        }
    }

    @Nested
    @DisplayName("toPageResponse")
    class ToPageResponseTest {

        @Test
        @DisplayName("PageResult를 PageApiResponse로 변환한다")
        void toPageResponse_Success() {
            // given
            ShippingPolicyPageResult pageResult = ShippingPolicyApiFixtures.pageResult();

            // when
            PageApiResponse<ShippingPolicyApiResponse> response = mapper.toPageResponse(pageResult);

            // then
            assertThat(response.content()).hasSize(1);
            assertThat(response.page()).isZero();
            assertThat(response.size()).isEqualTo(20);
            assertThat(response.totalElements()).isEqualTo(1);
        }

        @Test
        @DisplayName("빈 PageResult를 빈 PageApiResponse로 변환한다")
        void toPageResponse_Empty_Success() {
            // given
            ShippingPolicyPageResult emptyResult = ShippingPolicyApiFixtures.emptyPageResult();

            // when
            PageApiResponse<ShippingPolicyApiResponse> response =
                    mapper.toPageResponse(emptyResult);

            // then
            assertThat(response.content()).isEmpty();
            assertThat(response.totalElements()).isZero();
        }

        @Test
        @DisplayName("여러 결과를 포함한 PageResult를 변환한다")
        void toPageResponse_Multiple_Success() {
            // given
            List<ShippingPolicyResult> results = ShippingPolicyApiFixtures.multipleResults();
            ShippingPolicyPageResult pageResult =
                    ShippingPolicyApiFixtures.pageResult(results, 0, 20, 3);

            // when
            PageApiResponse<ShippingPolicyApiResponse> response = mapper.toPageResponse(pageResult);

            // then
            assertThat(response.content()).hasSize(3);
            assertThat(response.totalElements()).isEqualTo(3);
        }
    }
}
