package com.ryuqq.setof.application.review.service.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.review.ReviewQueryFixtures;
import com.ryuqq.setof.application.review.assembler.ReviewAssembler;
import com.ryuqq.setof.application.review.dto.query.MyReviewSearchParams;
import com.ryuqq.setof.application.review.dto.response.ReviewSliceResult;
import com.ryuqq.setof.application.review.factory.ReviewQueryFactory;
import com.ryuqq.setof.application.review.manager.ReviewReadManager;
import com.ryuqq.setof.domain.review.query.MyReviewSearchCriteria;
import com.ryuqq.setof.domain.review.vo.WrittenReview;
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
@DisplayName("GetMyReviewsService 단위 테스트")
class GetMyReviewsServiceTest {

    @InjectMocks private GetMyReviewsService sut;

    @Mock private ReviewQueryFactory queryFactory;
    @Mock private ReviewReadManager readManager;
    @Mock private ReviewAssembler assembler;

    @Mock private MyReviewSearchCriteria mockCriteria;

    @Nested
    @DisplayName("execute() - 내 리뷰 조회")
    class ExecuteTest {

        @Test
        @DisplayName("유효한 파라미터로 내 리뷰 목록을 조회한다")
        void execute_ValidParams_ReturnsReviewSliceResult() {
            // given
            MyReviewSearchParams params = ReviewQueryFixtures.myReviewSearchParams();
            List<WrittenReview> reviews = List.of();
            long totalElements = 3L;
            ReviewSliceResult expected = ReviewQueryFixtures.reviewSliceResult();

            given(queryFactory.createMyReviewCriteria(params)).willReturn(mockCriteria);
            given(readManager.fetchMyReviews(mockCriteria)).willReturn(reviews);
            given(readManager.countMyReviews(mockCriteria)).willReturn(totalElements);
            given(mockCriteria.size()).willReturn(20);
            given(assembler.toReviewSliceResult(reviews, 20, totalElements)).willReturn(expected);

            // when
            ReviewSliceResult result = sut.execute(params);

            // then
            assertThat(result).isEqualTo(expected);
            then(queryFactory).should().createMyReviewCriteria(params);
            then(readManager).should().fetchMyReviews(mockCriteria);
            then(readManager).should().countMyReviews(mockCriteria);
            then(assembler).should().toReviewSliceResult(reviews, 20, totalElements);
        }

        @Test
        @DisplayName("내 리뷰가 없으면 빈 결과를 반환한다")
        void execute_NoReviews_ReturnsEmptyResult() {
            // given
            MyReviewSearchParams params = ReviewQueryFixtures.myReviewSearchParams();
            List<WrittenReview> emptyReviews = List.of();
            long totalElements = 0L;
            ReviewSliceResult expected = ReviewQueryFixtures.emptyReviewSliceResult();

            given(queryFactory.createMyReviewCriteria(params)).willReturn(mockCriteria);
            given(readManager.fetchMyReviews(mockCriteria)).willReturn(emptyReviews);
            given(readManager.countMyReviews(mockCriteria)).willReturn(totalElements);
            given(mockCriteria.size()).willReturn(20);
            given(assembler.toReviewSliceResult(emptyReviews, 20, totalElements))
                    .willReturn(expected);

            // when
            ReviewSliceResult result = sut.execute(params);

            // then
            assertThat(result).isEqualTo(expected);
        }
    }
}
