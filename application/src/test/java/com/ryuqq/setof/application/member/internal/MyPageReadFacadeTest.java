package com.ryuqq.setof.application.member.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.member.MemberQueryFixtures;
import com.ryuqq.setof.application.member.dto.query.MemberProfile;
import com.ryuqq.setof.application.member.manager.MemberReadManager;
import com.ryuqq.setof.application.order.dto.response.OrderStatusCountResult;
import com.ryuqq.setof.application.order.manager.OrderReadManager;
import com.ryuqq.setof.domain.member.MemberFixtures;
import com.ryuqq.setof.domain.order.vo.OrderStatusCount;
import java.util.Collections;
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
@DisplayName("MyPageReadFacade 단위 테스트")
class MyPageReadFacadeTest {

    @InjectMocks private MyPageReadFacade sut;

    @Mock private MemberReadManager memberReadManager;
    @Mock private OrderReadManager orderReadManager;

    @Nested
    @DisplayName("fetchProfile() - 회원 프로필 조회")
    class FetchProfileTest {

        @Test
        @DisplayName("userId로 MemberProfile을 조회하여 반환한다")
        void fetchProfile_ValidUserId_ReturnsMemberProfile() {
            // given
            long userId = MemberFixtures.DEFAULT_LEGACY_MEMBER_ID;
            MemberProfile expected = MemberQueryFixtures.memberProfile();

            given(memberReadManager.getProfileByLegacyId(userId)).willReturn(expected);

            // when
            MemberProfile result = sut.fetchProfile(userId);

            // then
            assertThat(result).isEqualTo(expected);
            then(memberReadManager).should().getProfileByLegacyId(userId);
        }
    }

    @Nested
    @DisplayName("fetchOrderCounts() - 주문 상태별 건수 조회")
    class FetchOrderCountsTest {

        @Test
        @DisplayName("userId로 주문 상태별 건수를 조회하여 OrderStatusCountResult 목록을 반환한다")
        void fetchOrderCounts_ValidUserId_ReturnsOrderStatusCountResults() {
            // given
            long userId = MemberFixtures.DEFAULT_LEGACY_MEMBER_ID;
            List<OrderStatusCount> domainCounts =
                    List.of(
                            new OrderStatusCount("ORDER_PROCESSING", 2),
                            new OrderStatusCount("ORDER_COMPLETED", 5),
                            new OrderStatusCount("DELIVERY_PENDING", 1));

            given(
                            orderReadManager.countByStatus(
                                    userId,
                                    List.of(
                                            "ORDER_PROCESSING",
                                            "ORDER_COMPLETED",
                                            "DELIVERY_PENDING",
                                            "DELIVERY_PROCESSING",
                                            "DELIVERY_COMPLETED")))
                    .willReturn(domainCounts);

            // when
            List<OrderStatusCountResult> results = sut.fetchOrderCounts(userId);

            // then
            assertThat(results).hasSize(3);
            assertThat(results.get(0).orderStatus()).isEqualTo("ORDER_PROCESSING");
            assertThat(results.get(0).count()).isEqualTo(2);
            assertThat(results.get(1).orderStatus()).isEqualTo("ORDER_COMPLETED");
            assertThat(results.get(1).count()).isEqualTo(5);
        }

        @Test
        @DisplayName("주문이 없으면 빈 목록을 반환한다")
        void fetchOrderCounts_NoOrders_ReturnsEmptyList() {
            // given
            long userId = MemberFixtures.DEFAULT_LEGACY_MEMBER_ID;

            given(
                            orderReadManager.countByStatus(
                                    userId,
                                    List.of(
                                            "ORDER_PROCESSING",
                                            "ORDER_COMPLETED",
                                            "DELIVERY_PENDING",
                                            "DELIVERY_PROCESSING",
                                            "DELIVERY_COMPLETED")))
                    .willReturn(Collections.emptyList());

            // when
            List<OrderStatusCountResult> results = sut.fetchOrderCounts(userId);

            // then
            assertThat(results).isEmpty();
        }

        @Test
        @DisplayName("OrderStatusCount 도메인 객체가 OrderStatusCountResult로 변환된다")
        void fetchOrderCounts_ConvertsDomainToResult() {
            // given
            long userId = MemberFixtures.DEFAULT_LEGACY_MEMBER_ID;
            String expectedStatus = "DELIVERY_COMPLETED";
            long expectedCount = 10L;
            List<OrderStatusCount> domainCounts =
                    List.of(new OrderStatusCount(expectedStatus, expectedCount));

            given(
                            orderReadManager.countByStatus(
                                    userId,
                                    List.of(
                                            "ORDER_PROCESSING",
                                            "ORDER_COMPLETED",
                                            "DELIVERY_PENDING",
                                            "DELIVERY_PROCESSING",
                                            "DELIVERY_COMPLETED")))
                    .willReturn(domainCounts);

            // when
            List<OrderStatusCountResult> results = sut.fetchOrderCounts(userId);

            // then
            assertThat(results).hasSize(1);
            assertThat(results.get(0).orderStatus()).isEqualTo(expectedStatus);
            assertThat(results.get(0).count()).isEqualTo(expectedCount);
        }
    }
}
