package com.ryuqq.setof.adapter.in.rest.admin.v2.refundpolicy.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.admin.common.dto.PageApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.refundpolicy.RefundPolicyApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v2.refundpolicy.dto.query.SearchRefundPoliciesPageApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.refundpolicy.dto.response.RefundPolicyApiResponse;
import com.ryuqq.setof.application.refundpolicy.dto.query.RefundPolicySearchParams;
import com.ryuqq.setof.application.refundpolicy.dto.response.RefundPolicyPageResult;
import com.ryuqq.setof.application.refundpolicy.dto.response.RefundPolicyResult;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * RefundPolicyQueryApiMapper 단위 테스트.
 *
 * <p>Query API Mapper의 변환 로직을 테스트합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("RefundPolicyQueryApiMapper 단위 테스트")
class RefundPolicyQueryApiMapperTest {

    private RefundPolicyQueryApiMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new RefundPolicyQueryApiMapper();
    }

    @Nested
    @DisplayName("toSearchParams")
    class ToSearchParamsTest {

        @Test
        @DisplayName("검색 요청을 SearchParams로 변환한다")
        void toSearchParams_Success() {
            // given
            Long sellerId = 1L;
            SearchRefundPoliciesPageApiRequest request = RefundPolicyApiFixtures.searchRequest();

            // when
            RefundPolicySearchParams params = mapper.toSearchParams(sellerId, request);

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
            SearchRefundPoliciesPageApiRequest request =
                    new SearchRefundPoliciesPageApiRequest("CREATED_AT", "DESC", null, null);

            // when
            RefundPolicySearchParams params = mapper.toSearchParams(sellerId, request);

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
            RefundPolicyResult result = RefundPolicyApiFixtures.policyResult();

            // when
            RefundPolicyApiResponse response = mapper.toResponse(result);

            // then
            assertThat(response.policyId()).isEqualTo(result.policyId());
            assertThat(response.policyName()).isEqualTo(result.policyName());
            assertThat(response.defaultPolicy()).isEqualTo(result.defaultPolicy());
            assertThat(response.active()).isEqualTo(result.active());
            assertThat(response.returnPeriodDays()).isEqualTo(result.returnPeriodDays());
            assertThat(response.exchangePeriodDays()).isEqualTo(result.exchangePeriodDays());
            assertThat(response.nonReturnableConditions()).hasSize(2);
            assertThat(response.createdAt()).isNotNull();
        }

        @Test
        @DisplayName("null nonReturnableConditions를 빈 리스트로 변환한다")
        void toResponse_NullConditions_ReturnsEmptyList() {
            // given
            RefundPolicyResult result =
                    new RefundPolicyResult(1L, "정책명", true, true, 7, 14, null, null);

            // when
            RefundPolicyApiResponse response = mapper.toResponse(result);

            // then
            assertThat(response.nonReturnableConditions()).isEmpty();
        }
    }

    @Nested
    @DisplayName("toPageResponse")
    class ToPageResponseTest {

        @Test
        @DisplayName("PageResult를 PageApiResponse로 변환한다")
        void toPageResponse_Success() {
            // given
            RefundPolicyPageResult pageResult = RefundPolicyApiFixtures.pageResult();

            // when
            PageApiResponse<RefundPolicyApiResponse> response = mapper.toPageResponse(pageResult);

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
            RefundPolicyPageResult emptyResult = RefundPolicyApiFixtures.emptyPageResult();

            // when
            PageApiResponse<RefundPolicyApiResponse> response = mapper.toPageResponse(emptyResult);

            // then
            assertThat(response.content()).isEmpty();
            assertThat(response.totalElements()).isZero();
        }

        @Test
        @DisplayName("여러 결과를 포함한 PageResult를 변환한다")
        void toPageResponse_Multiple_Success() {
            // given
            List<RefundPolicyResult> results = RefundPolicyApiFixtures.multipleResults();
            RefundPolicyPageResult pageResult =
                    RefundPolicyApiFixtures.pageResult(results, 0, 20, 3);

            // when
            PageApiResponse<RefundPolicyApiResponse> response = mapper.toPageResponse(pageResult);

            // then
            assertThat(response.content()).hasSize(3);
            assertThat(response.totalElements()).isEqualTo(3);
        }
    }
}
