package com.ryuqq.setof.domain.order.vo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("MyPageOrderCounts 단위 테스트")
class MyPageOrderCountsTest {

    @Test
    @DisplayName("STATUSES는 마이페이지 주문 상태 5개를 포함한다")
    void statuses_ContainsFiveOrderStatuses() {
        assertThat(MyPageOrderCounts.STATUSES)
                .containsExactly(
                        "ORDER_PROCESSING",
                        "ORDER_COMPLETED",
                        "DELIVERY_PENDING",
                        "DELIVERY_PROCESSING",
                        "DELIVERY_COMPLETED");
    }

    @Nested
    @DisplayName("countOf() - 특정 상태 건수 조회")
    class CountOfTest {

        @Test
        @DisplayName("존재하는 상태의 건수를 반환한다")
        void countOf_ExistingStatus_ReturnsCount() {
            // given
            MyPageOrderCounts sut =
                    MyPageOrderCounts.of(
                            List.of(
                                    OrderStatusCount.of("ORDER_PROCESSING", 3),
                                    OrderStatusCount.of("DELIVERY_COMPLETED", 7)));

            // when & then
            assertThat(sut.countOf("ORDER_PROCESSING")).isEqualTo(3);
            assertThat(sut.countOf("DELIVERY_COMPLETED")).isEqualTo(7);
        }

        @Test
        @DisplayName("존재하지 않는 상태는 0을 반환한다")
        void countOf_NonExistingStatus_ReturnsZero() {
            // given
            MyPageOrderCounts sut =
                    MyPageOrderCounts.of(List.of(OrderStatusCount.of("ORDER_PROCESSING", 3)));

            // when & then
            assertThat(sut.countOf("DELIVERY_COMPLETED")).isZero();
        }
    }

    @Nested
    @DisplayName("totalCount() - 전체 건수 합계")
    class TotalCountTest {

        @Test
        @DisplayName("모든 상태의 건수 합계를 반환한다")
        void totalCount_MultipleCounts_ReturnsSum() {
            // given
            MyPageOrderCounts sut =
                    MyPageOrderCounts.of(
                            List.of(
                                    OrderStatusCount.of("ORDER_PROCESSING", 2),
                                    OrderStatusCount.of("ORDER_COMPLETED", 5),
                                    OrderStatusCount.of("DELIVERY_PENDING", 1)));

            // when & then
            assertThat(sut.totalCount()).isEqualTo(8);
        }

        @Test
        @DisplayName("빈 목록이면 0을 반환한다")
        void totalCount_EmptyList_ReturnsZero() {
            // given
            MyPageOrderCounts sut = MyPageOrderCounts.of(Collections.emptyList());

            // when & then
            assertThat(sut.totalCount()).isZero();
        }
    }
}
