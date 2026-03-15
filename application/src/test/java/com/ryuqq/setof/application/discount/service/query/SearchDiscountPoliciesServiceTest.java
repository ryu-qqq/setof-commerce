package com.ryuqq.setof.application.discount.service.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.discount.DiscountQueryFixtures;
import com.ryuqq.setof.application.discount.dto.query.DiscountPolicySearchParams;
import com.ryuqq.setof.application.discount.dto.response.DiscountPolicyPageResult;
import com.ryuqq.setof.application.discount.factory.DiscountPolicyQueryFactory;
import com.ryuqq.setof.application.discount.manager.DiscountPolicyReadManager;
import com.ryuqq.setof.domain.discount.DiscountFixtures;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import com.ryuqq.setof.domain.discount.query.DiscountPolicySearchCriteria;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("SearchDiscountPoliciesService 단위 테스트")
class SearchDiscountPoliciesServiceTest {

    @InjectMocks private SearchDiscountPoliciesService sut;

    @Mock private DiscountPolicyReadManager readManager;
    @Mock private DiscountPolicyQueryFactory queryFactory;

    @Nested
    @DisplayName("execute() - 할인 정책 목록 검색")
    class ExecuteTest {

        @Test
        @DisplayName("검색 파라미터로 정책 목록과 페이지 결과를 반환한다")
        void execute_ValidParams_ReturnsPolicyPageResult() {
            // given
            DiscountPolicySearchParams params = DiscountQueryFixtures.defaultSearchParams();
            DiscountPolicySearchCriteria criteria = DiscountPolicySearchCriteria.defaultCriteria();
            List<DiscountPolicy> policies =
                    List.of(
                            DiscountFixtures.activeRatePolicy(1L),
                            DiscountFixtures.activeFixedPolicy(2L));

            given(queryFactory.create(params)).willReturn(criteria);
            given(readManager.findByCriteria(criteria)).willReturn(policies);
            given(readManager.countByCriteria(criteria)).willReturn(2L);

            // when
            DiscountPolicyPageResult result = sut.execute(params);

            // then
            assertThat(result).isNotNull();
            assertThat(result.items()).hasSize(2);
            assertThat(result.totalCount()).isEqualTo(2L);
            then(queryFactory).should().create(params);
            then(readManager).should().findByCriteria(criteria);
            then(readManager).should().countByCriteria(criteria);
        }

        @Test
        @DisplayName("검색 결과가 없으면 빈 목록과 totalCount 0을 반환한다")
        void execute_NoResults_ReturnsEmptyPageResult() {
            // given
            DiscountPolicySearchParams params = DiscountQueryFixtures.defaultSearchParams();
            DiscountPolicySearchCriteria criteria = DiscountPolicySearchCriteria.defaultCriteria();

            given(queryFactory.create(params)).willReturn(criteria);
            given(readManager.findByCriteria(criteria)).willReturn(List.of());
            given(readManager.countByCriteria(criteria)).willReturn(0L);

            // when
            DiscountPolicyPageResult result = sut.execute(params);

            // then
            assertThat(result.items()).isEmpty();
            assertThat(result.totalCount()).isZero();
        }

        @Test
        @DisplayName("페이지 정보가 결과 DTO에 반영된다")
        void execute_WithPageInfo_ReflectsPageInResult() {
            // given
            DiscountPolicySearchParams params = DiscountQueryFixtures.searchParams(1, 10);
            DiscountPolicySearchCriteria criteria = DiscountPolicySearchCriteria.defaultCriteria();
            List<DiscountPolicy> policies = List.of(DiscountFixtures.activeRatePolicy(1L));

            given(queryFactory.create(params)).willReturn(criteria);
            given(readManager.findByCriteria(criteria)).willReturn(policies);
            given(readManager.countByCriteria(criteria)).willReturn(1L);

            // when
            DiscountPolicyPageResult result = sut.execute(params);

            // then
            assertThat(result.page()).isEqualTo(params.page());
            assertThat(result.size()).isEqualTo(params.size());
        }

        @Test
        @DisplayName("도메인 정책 목록이 DiscountPolicyResult 목록으로 변환된다")
        void execute_PolicyList_ConvertedToResultList() {
            // given
            DiscountPolicySearchParams params = DiscountQueryFixtures.defaultSearchParams();
            DiscountPolicySearchCriteria criteria = DiscountPolicySearchCriteria.defaultCriteria();
            DiscountPolicy policy = DiscountFixtures.activeRatePolicy(1L);

            given(queryFactory.create(params)).willReturn(criteria);
            given(readManager.findByCriteria(criteria)).willReturn(List.of(policy));
            given(readManager.countByCriteria(criteria)).willReturn(1L);

            // when
            DiscountPolicyPageResult result = sut.execute(params);

            // then
            assertThat(result.items()).hasSize(1);
            assertThat(result.items().get(0).id()).isEqualTo(policy.idValue());
            assertThat(result.items().get(0).name()).isEqualTo(policy.nameValue());
        }
    }
}
