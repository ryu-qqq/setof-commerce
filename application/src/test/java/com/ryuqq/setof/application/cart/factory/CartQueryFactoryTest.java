package com.ryuqq.setof.application.cart.factory;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.application.cart.CartQueryFixtures;
import com.ryuqq.setof.application.cart.dto.query.CartSearchParams;
import com.ryuqq.setof.application.common.factory.CommonVoFactory;
import com.ryuqq.setof.domain.cart.query.CartSearchCriteria;
import com.ryuqq.setof.domain.cart.query.CartSortKey;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("CartQueryFactory 단위 테스트")
class CartQueryFactoryTest {

    private final CommonVoFactory commonVoFactory = new CommonVoFactory();
    private final CartQueryFactory sut = new CartQueryFactory(commonVoFactory);

    @Nested
    @DisplayName("createCriteria() - SearchParams → SearchCriteria 변환")
    class CreateCriteriaTest {

        @Test
        @DisplayName("CartSearchParams로부터 CartSearchCriteria를 생성한다")
        void createCriteria_ValidParams_ReturnsCartSearchCriteria() {
            // given
            CartSearchParams params = CartQueryFixtures.searchParams();

            // when
            CartSearchCriteria result = sut.createCriteria(params);

            // then
            assertThat(result).isNotNull();
            assertThat(result.memberId()).isEqualTo(params.memberId());
            assertThat(result.userId()).isEqualTo(params.userId());
            assertThat(result.queryContext()).isNotNull();
        }

        @Test
        @DisplayName("기본 정렬 키가 CREATED_AT이고 내림차순으로 설정된다")
        void createCriteria_DefaultSortKey_IsCreatedAtDescending() {
            // given
            CartSearchParams params = CartQueryFixtures.searchParams();

            // when
            CartSearchCriteria result = sut.createCriteria(params);

            // then
            assertThat(result.queryContext().sortKey()).isEqualTo(CartSortKey.CREATED_AT);
            assertThat(result.queryContext().sortDirection()).isEqualTo(SortDirection.DESC);
        }

        @Test
        @DisplayName("첫 페이지 요청 시 cursor가 null이다")
        void createCriteria_FirstPage_HasNullCursor() {
            // given
            CartSearchParams params = CartQueryFixtures.searchParamsFirstPage(20);

            // when
            CartSearchCriteria result = sut.createCriteria(params);

            // then
            assertThat(result.cursor()).isNull();
            assertThat(result.hasCursor()).isFalse();
        }

        @Test
        @DisplayName("lastCartId가 설정된 경우 cursor에 반영된다")
        void createCriteria_WithLastCartId_SetsCursorCorrectly() {
            // given
            Long lastCartId = 50L;
            CartSearchParams params = CartQueryFixtures.searchParams(lastCartId, 20);

            // when
            CartSearchCriteria result = sut.createCriteria(params);

            // then
            assertThat(result.cursor()).isEqualTo(lastCartId);
            assertThat(result.hasCursor()).isTrue();
        }

        @Test
        @DisplayName("pageSize가 criteria의 size에 반영된다")
        void createCriteria_WithPageSize_SetsSizeCorrectly() {
            // given
            int pageSize = 10;
            CartSearchParams params = CartQueryFixtures.searchParamsFirstPage(pageSize);

            // when
            CartSearchCriteria result = sut.createCriteria(params);

            // then
            assertThat(result.size()).isEqualTo(pageSize);
        }

        @Test
        @DisplayName("fetchSize는 size + 1로 설정된다 (hasNext 판단용)")
        void createCriteria_FetchSize_IsSizePlusOne() {
            // given
            int pageSize = 20;
            CartSearchParams params = CartQueryFixtures.searchParamsFirstPage(pageSize);

            // when
            CartSearchCriteria result = sut.createCriteria(params);

            // then
            assertThat(result.fetchSize()).isEqualTo(pageSize + 1);
        }

        @Test
        @DisplayName("memberId와 userId가 Criteria에 정확히 반영된다")
        void createCriteria_MemberIdAndUserId_AreReflectedInCriteria() {
            // given
            CartSearchParams params = CartQueryFixtures.searchParams();

            // when
            CartSearchCriteria result = sut.createCriteria(params);

            // then
            assertThat(result.memberId()).isEqualTo(CartQueryFixtures.MEMBER_ID);
            assertThat(result.userId()).isEqualTo(CartQueryFixtures.USER_ID);
        }
    }
}
