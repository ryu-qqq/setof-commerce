package com.ryuqq.setof.domain.discount.query;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.vo.PageRequest;
import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import com.ryuqq.setof.domain.discount.vo.ApplicationType;
import com.ryuqq.setof.domain.discount.vo.PublisherType;
import com.ryuqq.setof.domain.discount.vo.StackingGroup;
import com.ryuqq.setof.domain.seller.id.SellerId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("DiscountPolicySearchCriteria 테스트")
class DiscountPolicySearchCriteriaTest {

    @Nested
    @DisplayName("of() - 모든 파라미터 생성")
    class OfTest {

        @Test
        @DisplayName("모든 파라미터로 검색 조건을 생성한다")
        void createWithAllParameters() {
            // given
            SellerId sellerId = SellerId.of(1L);
            ApplicationType applicationType = ApplicationType.INSTANT;
            PublisherType publisherType = PublisherType.SELLER;
            StackingGroup stackingGroup = StackingGroup.SELLER_INSTANT;
            QueryContext<DiscountPolicySortKey> queryContext =
                    QueryContext.of(
                            DiscountPolicySortKey.CREATED_AT,
                            SortDirection.DESC,
                            PageRequest.of(0, 10));

            // when
            DiscountPolicySearchCriteria criteria =
                    DiscountPolicySearchCriteria.of(
                            sellerId,
                            applicationType,
                            publisherType,
                            stackingGroup,
                            true,
                            queryContext);

            // then
            assertThat(criteria.sellerId()).isEqualTo(sellerId);
            assertThat(criteria.applicationType()).isEqualTo(ApplicationType.INSTANT);
            assertThat(criteria.publisherType()).isEqualTo(PublisherType.SELLER);
            assertThat(criteria.stackingGroup()).isEqualTo(StackingGroup.SELLER_INSTANT);
            assertThat(criteria.activeOnly()).isTrue();
            assertThat(criteria.queryContext()).isEqualTo(queryContext);
        }

        @Test
        @DisplayName("null 파라미터로 검색 조건을 생성한다")
        void createWithNullParameters() {
            // given
            QueryContext<DiscountPolicySortKey> queryContext =
                    QueryContext.defaultOf(DiscountPolicySortKey.defaultKey());

            // when
            DiscountPolicySearchCriteria criteria =
                    DiscountPolicySearchCriteria.of(null, null, null, null, false, queryContext);

            // then
            assertThat(criteria.sellerId()).isNull();
            assertThat(criteria.applicationType()).isNull();
            assertThat(criteria.publisherType()).isNull();
            assertThat(criteria.stackingGroup()).isNull();
            assertThat(criteria.activeOnly()).isFalse();
        }
    }

    @Nested
    @DisplayName("defaultCriteria() - 기본 검색 조건")
    class DefaultCriteriaTest {

        @Test
        @DisplayName("기본 검색 조건을 생성한다")
        void createDefaultCriteria() {
            // when
            DiscountPolicySearchCriteria criteria = DiscountPolicySearchCriteria.defaultCriteria();

            // then
            assertThat(criteria.sellerId()).isNull();
            assertThat(criteria.applicationType()).isNull();
            assertThat(criteria.publisherType()).isNull();
            assertThat(criteria.stackingGroup()).isNull();
            assertThat(criteria.activeOnly()).isTrue();
            assertThat(criteria.queryContext()).isNotNull();
        }
    }

    @Nested
    @DisplayName("forSeller() - 셀러별 검색 조건")
    class ForSellerTest {

        @Test
        @DisplayName("셀러별 검색 조건을 생성한다")
        void createForSellerCriteria() {
            // given
            SellerId sellerId = SellerId.of(10L);

            // when
            DiscountPolicySearchCriteria criteria =
                    DiscountPolicySearchCriteria.forSeller(sellerId);

            // then
            assertThat(criteria.sellerId()).isEqualTo(sellerId);
            assertThat(criteria.publisherType()).isEqualTo(PublisherType.SELLER);
            assertThat(criteria.activeOnly()).isTrue();
        }
    }

    @Nested
    @DisplayName("편의 메서드 테스트")
    class ConvenienceMethodsTest {

        @Test
        @DisplayName("sellerIdValue()는 sellerId의 값을 반환한다")
        void sellerIdValueReturnsLongValue() {
            // given
            DiscountPolicySearchCriteria criteria =
                    DiscountPolicySearchCriteria.forSeller(SellerId.of(99L));

            // then
            assertThat(criteria.sellerIdValue()).isEqualTo(99L);
        }

        @Test
        @DisplayName("sellerIdValue()는 sellerId가 null이면 null을 반환한다")
        void sellerIdValueReturnsNullWhenSellerIdIsNull() {
            // given
            DiscountPolicySearchCriteria criteria = DiscountPolicySearchCriteria.defaultCriteria();

            // then
            assertThat(criteria.sellerIdValue()).isNull();
        }

        @Test
        @DisplayName("size()는 QueryContext의 size를 반환한다")
        void sizeReturnsQueryContextSize() {
            // given
            QueryContext<DiscountPolicySortKey> queryContext =
                    QueryContext.of(
                            DiscountPolicySortKey.CREATED_AT,
                            SortDirection.DESC,
                            PageRequest.of(0, 20));
            DiscountPolicySearchCriteria criteria =
                    DiscountPolicySearchCriteria.of(null, null, null, null, true, queryContext);

            // then
            assertThat(criteria.size()).isEqualTo(20);
        }

        @Test
        @DisplayName("offset()은 QueryContext의 offset을 반환한다")
        void offsetReturnsQueryContextOffset() {
            // given
            QueryContext<DiscountPolicySortKey> queryContext =
                    QueryContext.of(
                            DiscountPolicySortKey.CREATED_AT,
                            SortDirection.DESC,
                            PageRequest.of(2, 10));
            DiscountPolicySearchCriteria criteria =
                    DiscountPolicySearchCriteria.of(null, null, null, null, true, queryContext);

            // then
            assertThat(criteria.offset()).isEqualTo(20L);
        }

        @Test
        @DisplayName("page()는 QueryContext의 page를 반환한다")
        void pageReturnsQueryContextPage() {
            // given
            QueryContext<DiscountPolicySortKey> queryContext =
                    QueryContext.of(
                            DiscountPolicySortKey.CREATED_AT,
                            SortDirection.DESC,
                            PageRequest.of(3, 10));
            DiscountPolicySearchCriteria criteria =
                    DiscountPolicySearchCriteria.of(null, null, null, null, true, queryContext);

            // then
            assertThat(criteria.page()).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("DiscountPolicySortKey 테스트")
    class SortKeyTest {

        @Test
        @DisplayName("defaultKey()는 CREATED_AT을 반환한다")
        void defaultKeyIsCreatedAt() {
            // then
            assertThat(DiscountPolicySortKey.defaultKey())
                    .isEqualTo(DiscountPolicySortKey.CREATED_AT);
        }

        @Test
        @DisplayName("각 정렬 키의 fieldName을 검증한다")
        void fieldNamesAreCorrect() {
            // then
            assertThat(DiscountPolicySortKey.CREATED_AT.fieldName()).isEqualTo("createdAt");
            assertThat(DiscountPolicySortKey.NAME.fieldName()).isEqualTo("name");
            assertThat(DiscountPolicySortKey.PRIORITY.fieldName()).isEqualTo("priority");
            assertThat(DiscountPolicySortKey.START_AT.fieldName()).isEqualTo("startAt");
        }

        @Test
        @DisplayName("모든 정렬 키 값이 존재한다")
        void allSortKeysExist() {
            // then
            assertThat(DiscountPolicySortKey.values())
                    .containsExactly(
                            DiscountPolicySortKey.CREATED_AT,
                            DiscountPolicySortKey.NAME,
                            DiscountPolicySortKey.PRIORITY,
                            DiscountPolicySortKey.START_AT);
        }
    }
}
