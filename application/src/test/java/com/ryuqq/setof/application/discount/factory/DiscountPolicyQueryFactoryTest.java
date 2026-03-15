package com.ryuqq.setof.application.discount.factory;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.application.discount.DiscountQueryFixtures;
import com.ryuqq.setof.application.discount.dto.query.DiscountPolicySearchParams;
import com.ryuqq.setof.domain.discount.query.DiscountPolicySearchCriteria;
import com.ryuqq.setof.domain.discount.query.DiscountPolicySortKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("DiscountPolicyQueryFactory 단위 테스트")
class DiscountPolicyQueryFactoryTest {

    private DiscountPolicyQueryFactory sut;

    @BeforeEach
    void setUp() {
        sut = new DiscountPolicyQueryFactory();
    }

    @Nested
    @DisplayName("create() - DiscountPolicySearchParams → DiscountPolicySearchCriteria 변환")
    class CreateTest {

        @Test
        @DisplayName("기본 검색 파라미터로 Criteria를 생성한다")
        void create_DefaultParams_ReturnsCriteria() {
            // given
            DiscountPolicySearchParams params = DiscountQueryFixtures.defaultSearchParams();

            // when
            DiscountPolicySearchCriteria criteria = sut.create(params);

            // then
            assertThat(criteria).isNotNull();
            assertThat(criteria.sellerId()).isNull();
            assertThat(criteria.applicationType()).isNull();
            assertThat(criteria.publisherType()).isNull();
            assertThat(criteria.stackingGroup()).isNull();
        }

        @Test
        @DisplayName("applicationType 필터가 Criteria에 반영된다")
        void create_WithApplicationType_ReflectedInCriteria() {
            // given
            DiscountPolicySearchParams params =
                    DiscountQueryFixtures.searchParamsByApplicationType("INSTANT");

            // when
            DiscountPolicySearchCriteria criteria = sut.create(params);

            // then
            assertThat(criteria.applicationType()).isNotNull();
            assertThat(criteria.applicationType().name()).isEqualTo("INSTANT");
        }

        @Test
        @DisplayName("publisherType 필터가 Criteria에 반영된다")
        void create_WithPublisherType_ReflectedInCriteria() {
            // given
            DiscountPolicySearchParams params =
                    DiscountQueryFixtures.searchParamsByPublisherType("ADMIN");

            // when
            DiscountPolicySearchCriteria criteria = sut.create(params);

            // then
            assertThat(criteria.publisherType()).isNotNull();
            assertThat(criteria.publisherType().name()).isEqualTo("ADMIN");
        }

        @Test
        @DisplayName("sellerId 필터가 Criteria에 반영된다")
        void create_WithSellerId_ReflectedInCriteria() {
            // given
            DiscountPolicySearchParams params = DiscountQueryFixtures.searchParamsBySellerId(1L);

            // when
            DiscountPolicySearchCriteria criteria = sut.create(params);

            // then
            assertThat(criteria.sellerId()).isNotNull();
            assertThat(criteria.sellerId().value()).isEqualTo(1L);
        }

        @Test
        @DisplayName("activeOnly 파라미터가 Criteria에 반영된다")
        void create_WithActiveOnly_ReflectedInCriteria() {
            // given
            DiscountPolicySearchParams params = DiscountQueryFixtures.searchParamsActiveOnly();

            // when
            DiscountPolicySearchCriteria criteria = sut.create(params);

            // then
            assertThat(criteria.activeOnly()).isTrue();
        }

        @Test
        @DisplayName("null applicationType은 null로 변환된다")
        void create_NullApplicationType_ReturnsNullInCriteria() {
            // given
            DiscountPolicySearchParams params = DiscountQueryFixtures.defaultSearchParams();

            // when
            DiscountPolicySearchCriteria criteria = sut.create(params);

            // then
            assertThat(criteria.applicationType()).isNull();
        }

        @Test
        @DisplayName("빈 문자열 sortKey는 기본 정렬 키로 처리된다")
        void create_EmptySortKey_UsesDefaultSortKey() {
            // given
            DiscountPolicySearchParams params = DiscountQueryFixtures.defaultSearchParams();

            // when
            DiscountPolicySearchCriteria criteria = sut.create(params);

            // then
            assertThat(criteria.queryContext().sortKey())
                    .isEqualTo(DiscountPolicySortKey.defaultKey());
        }

        @Test
        @DisplayName("유효하지 않은 sortKey는 기본 정렬 키로 폴백된다")
        void create_InvalidSortKey_FallsBackToDefaultKey() {
            // given
            DiscountPolicySearchParams params =
                    new DiscountPolicySearchParams(
                            null, null, null, null, null, "INVALID_KEY", "DESC", 0, 20);

            // when
            DiscountPolicySearchCriteria criteria = sut.create(params);

            // then
            assertThat(criteria.queryContext().sortKey())
                    .isEqualTo(DiscountPolicySortKey.defaultKey());
        }

        @Test
        @DisplayName("페이지 정보가 Criteria의 QueryContext에 반영된다")
        void create_WithPageInfo_ReflectedInQueryContext() {
            // given
            DiscountPolicySearchParams params = DiscountQueryFixtures.searchParams(2, 10);

            // when
            DiscountPolicySearchCriteria criteria = sut.create(params);

            // then
            assertThat(criteria.queryContext().pageRequest().page()).isEqualTo(2);
            assertThat(criteria.queryContext().pageRequest().size()).isEqualTo(10);
        }
    }
}
