package com.ryuqq.setof.application.review.service.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.review.ReviewQueryFixtures;
import com.ryuqq.setof.application.review.assembler.ReviewAssembler;
import com.ryuqq.setof.application.review.dto.query.AvailableReviewSearchParams;
import com.ryuqq.setof.application.review.dto.response.AvailableReviewSliceResult;
import com.ryuqq.setof.application.review.factory.ReviewQueryFactory;
import com.ryuqq.setof.application.review.manager.ReviewCompositeReadManager;
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
@DisplayName("GetAvailableReviewsService 단위 테스트")
class GetAvailableReviewsServiceTest {

    @InjectMocks private GetAvailableReviewsService sut;

    @Mock private ReviewQueryFactory queryFactory;
    @Mock private ReviewCompositeReadManager compositeReadManager;
    @Mock private ReviewAssembler assembler;

    @Mock private AvailableReviewSearchCriteria mockCriteria;

    @Nested
    @DisplayName("execute() - 작성 가능한 리뷰 주문 조회")
    class ExecuteTest {

        @Test
        @DisplayName("유효한 파라미터로 작성 가능한 리뷰 목록을 조회한다")
        void execute_ValidParams_ReturnsAvailableReviewSliceResult() {
            // given
            AvailableReviewSearchParams params = ReviewQueryFixtures.availableReviewSearchParams();
            List<ReviewableOrder> orders = List.of();
            long totalElements = 5L;
            AvailableReviewSliceResult expected = ReviewQueryFixtures.availableReviewSliceResult();

            given(queryFactory.createAvailableReviewCriteria(params)).willReturn(mockCriteria);
            given(compositeReadManager.fetchAvailableReviewOrders(mockCriteria)).willReturn(orders);
            given(compositeReadManager.countAvailableReviewOrders(mockCriteria))
                    .willReturn(totalElements);
            given(mockCriteria.size()).willReturn(20);
            given(assembler.toAvailableReviewSliceResult(orders, 20, totalElements))
                    .willReturn(expected);

            // when
            AvailableReviewSliceResult result = sut.execute(params);

            // then
            assertThat(result).isEqualTo(expected);
            then(queryFactory).should().createAvailableReviewCriteria(params);
            then(compositeReadManager).should().fetchAvailableReviewOrders(mockCriteria);
            then(compositeReadManager).should().countAvailableReviewOrders(mockCriteria);
            then(assembler).should().toAvailableReviewSliceResult(orders, 20, totalElements);
        }

        @Test
        @DisplayName("작성 가능한 리뷰가 없으면 빈 결과를 반환한다")
        void execute_NoAvailableReviews_ReturnsEmptyResult() {
            // given
            AvailableReviewSearchParams params = ReviewQueryFixtures.availableReviewSearchParams();
            List<ReviewableOrder> emptyOrders = List.of();
            long totalElements = 0L;
            AvailableReviewSliceResult expected =
                    ReviewQueryFixtures.emptyAvailableReviewSliceResult();

            given(queryFactory.createAvailableReviewCriteria(params)).willReturn(mockCriteria);
            given(compositeReadManager.fetchAvailableReviewOrders(mockCriteria))
                    .willReturn(emptyOrders);
            given(compositeReadManager.countAvailableReviewOrders(mockCriteria))
                    .willReturn(totalElements);
            given(mockCriteria.size()).willReturn(20);
            given(assembler.toAvailableReviewSliceResult(emptyOrders, 20, totalElements))
                    .willReturn(expected);

            // when
            AvailableReviewSliceResult result = sut.execute(params);

            // then
            assertThat(result).isEqualTo(expected);
            assertThat(result.content()).isEmpty();
        }
    }
}
