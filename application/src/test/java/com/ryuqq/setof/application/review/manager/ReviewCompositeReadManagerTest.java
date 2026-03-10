package com.ryuqq.setof.application.review.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.review.port.out.query.ReviewCompositeQueryPort;
import com.ryuqq.setof.domain.review.query.AvailableReviewSearchCriteria;
import com.ryuqq.setof.domain.review.vo.ReviewableOrder;
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
@DisplayName("ReviewCompositeReadManager 단위 테스트")
class ReviewCompositeReadManagerTest {

    @InjectMocks private ReviewCompositeReadManager sut;

    @Mock private ReviewCompositeQueryPort reviewCompositeQueryPort;

    @Mock private AvailableReviewSearchCriteria mockCriteria;
    @Mock private ReviewableOrder mockOrder;

    @Nested
    @DisplayName("fetchAvailableReviewOrders() - 작성 가능한 리뷰 주문 목록 조회")
    class FetchAvailableReviewOrdersTest {

        @Test
        @DisplayName("작성 가능한 리뷰 주문 목록을 조회한다")
        void fetchAvailableReviewOrders_ValidCriteria_ReturnsList() {
            // given
            List<ReviewableOrder> expected = List.of(mockOrder);
            given(reviewCompositeQueryPort.fetchAvailableReviewOrders(mockCriteria))
                    .willReturn(expected);

            // when
            List<ReviewableOrder> result = sut.fetchAvailableReviewOrders(mockCriteria);

            // then
            assertThat(result).isEqualTo(expected);
            then(reviewCompositeQueryPort).should().fetchAvailableReviewOrders(mockCriteria);
        }

        @Test
        @DisplayName("작성 가능한 리뷰 주문이 없으면 빈 목록을 반환한다")
        void fetchAvailableReviewOrders_NoOrders_ReturnsEmptyList() {
            // given
            given(reviewCompositeQueryPort.fetchAvailableReviewOrders(mockCriteria))
                    .willReturn(List.of());

            // when
            List<ReviewableOrder> result = sut.fetchAvailableReviewOrders(mockCriteria);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("countAvailableReviewOrders() - 작성 가능한 리뷰 주문 수 조회")
    class CountAvailableReviewOrdersTest {

        @Test
        @DisplayName("작성 가능한 리뷰 주문 수를 반환한다")
        void countAvailableReviewOrders_ReturnsCount() {
            // given
            long expectedCount = 5L;
            given(reviewCompositeQueryPort.countAvailableReviewOrders(mockCriteria))
                    .willReturn(expectedCount);

            // when
            long result = sut.countAvailableReviewOrders(mockCriteria);

            // then
            assertThat(result).isEqualTo(expectedCount);
            then(reviewCompositeQueryPort).should().countAvailableReviewOrders(mockCriteria);
        }

        @Test
        @DisplayName("작성 가능한 리뷰 주문이 없으면 0을 반환한다")
        void countAvailableReviewOrders_NoOrders_ReturnsZero() {
            // given
            given(reviewCompositeQueryPort.countAvailableReviewOrders(mockCriteria)).willReturn(0L);

            // when
            long result = sut.countAvailableReviewOrders(mockCriteria);

            // then
            assertThat(result).isZero();
        }
    }
}
