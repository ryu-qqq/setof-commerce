package com.ryuqq.setof.application.review.service.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.review.ReviewQueryFixtures;
import com.ryuqq.setof.application.review.assembler.ReviewAssembler;
import com.ryuqq.setof.application.review.dto.query.ProductGroupReviewSearchParams;
import com.ryuqq.setof.application.review.dto.response.ReviewPageResult;
import com.ryuqq.setof.application.review.factory.ReviewQueryFactory;
import com.ryuqq.setof.application.review.manager.ReviewReadManager;
import com.ryuqq.setof.domain.review.query.ProductGroupReviewSearchCriteria;
import com.ryuqq.setof.domain.review.vo.WrittenReview;
import java.util.List;
import java.util.Optional;
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
@DisplayName("GetProductGroupReviewsService 단위 테스트")
class GetProductGroupReviewsServiceTest {

    @InjectMocks private GetProductGroupReviewsService sut;

    @Mock private ReviewQueryFactory queryFactory;
    @Mock private ReviewReadManager readManager;
    @Mock private ReviewAssembler assembler;

    @Mock private ProductGroupReviewSearchCriteria mockCriteria;

    @Nested
    @DisplayName("execute() - 상품그룹 리뷰 조회")
    class ExecuteTest {

        @Test
        @DisplayName("유효한 파라미터로 상품그룹 리뷰 목록을 조회한다")
        void execute_ValidParams_ReturnsReviewPageResult() {
            // given
            ProductGroupReviewSearchParams params =
                    ReviewQueryFixtures.productGroupReviewSearchParams();
            List<WrittenReview> reviews = List.of();
            long totalElements = 10L;
            double averageRating = 4.2;
            ReviewPageResult expected = ReviewQueryFixtures.reviewPageResult();

            given(queryFactory.createProductGroupReviewCriteria(params)).willReturn(mockCriteria);
            given(readManager.fetchProductGroupReviews(mockCriteria)).willReturn(reviews);
            given(mockCriteria.productGroupId())
                    .willReturn(ReviewQueryFixtures.DEFAULT_PRODUCT_GROUP_ID);
            given(readManager.countProductGroupReviews(mockCriteria.productGroupId()))
                    .willReturn(totalElements);
            given(readManager.fetchAverageRating(mockCriteria.productGroupId()))
                    .willReturn(Optional.of(averageRating));
            given(mockCriteria.page()).willReturn(0);
            given(mockCriteria.size()).willReturn(20);
            given(assembler.toReviewPageResult(reviews, 0, 20, totalElements, averageRating))
                    .willReturn(expected);

            // when
            ReviewPageResult result = sut.execute(params);

            // then
            assertThat(result).isEqualTo(expected);
            then(queryFactory).should().createProductGroupReviewCriteria(params);
            then(readManager).should().fetchProductGroupReviews(mockCriteria);
        }

        @Test
        @DisplayName("평균 평점이 없으면 0.0을 사용한다")
        void execute_NoAverageRating_UsesZeroAsDefault() {
            // given
            ProductGroupReviewSearchParams params =
                    ReviewQueryFixtures.productGroupReviewSearchParams();
            List<WrittenReview> reviews = List.of();
            long totalElements = 0L;
            double defaultRating = 0.0;
            ReviewPageResult expected = ReviewQueryFixtures.emptyReviewPageResult();

            given(queryFactory.createProductGroupReviewCriteria(params)).willReturn(mockCriteria);
            given(readManager.fetchProductGroupReviews(mockCriteria)).willReturn(reviews);
            given(mockCriteria.productGroupId())
                    .willReturn(ReviewQueryFixtures.DEFAULT_PRODUCT_GROUP_ID);
            given(readManager.countProductGroupReviews(mockCriteria.productGroupId()))
                    .willReturn(totalElements);
            given(readManager.fetchAverageRating(mockCriteria.productGroupId()))
                    .willReturn(Optional.empty());
            given(mockCriteria.page()).willReturn(0);
            given(mockCriteria.size()).willReturn(20);
            given(assembler.toReviewPageResult(reviews, 0, 20, totalElements, defaultRating))
                    .willReturn(expected);

            // when
            ReviewPageResult result = sut.execute(params);

            // then
            assertThat(result).isEqualTo(expected);
            then(assembler)
                    .should()
                    .toReviewPageResult(reviews, 0, 20, totalElements, defaultRating);
        }
    }
}
