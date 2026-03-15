package com.ryuqq.setof.domain.review.aggregate;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.member.id.MemberId;
import com.ryuqq.setof.domain.member.vo.LegacyMemberId;
import com.ryuqq.setof.domain.order.id.LegacyOrderId;
import com.ryuqq.setof.domain.order.id.OrderId;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.review.ReviewFixtures;
import com.ryuqq.setof.domain.review.vo.Rating;
import com.ryuqq.setof.domain.review.vo.ReviewContent;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("Review Aggregate 단위 테스트")
class ReviewTest {

    @Nested
    @DisplayName("forNew() - 신규 리뷰 생성")
    class ForNewTest {

        @Test
        @DisplayName("필수 필드로 새 Review를 생성한다")
        void createNewReviewWithRequiredFields() {
            // given
            LegacyMemberId legacyMemberId = ReviewFixtures.defaultLegacyMemberId();
            MemberId memberId = ReviewFixtures.defaultMemberId();
            LegacyOrderId legacyOrderId = ReviewFixtures.defaultLegacyOrderId();
            OrderId orderId = ReviewFixtures.defaultOrderId();
            ProductGroupId productGroupId = ReviewFixtures.defaultProductGroupId();
            Rating rating = ReviewFixtures.defaultRating();
            ReviewContent content = ReviewFixtures.defaultReviewContent();
            Instant now = CommonVoFixtures.now();

            // when
            Review review =
                    Review.forNew(
                            legacyMemberId,
                            memberId,
                            legacyOrderId,
                            orderId,
                            productGroupId,
                            rating,
                            content,
                            now);

            // then
            assertThat(review).isNotNull();
            assertThat(review.isNew()).isTrue();
            assertThat(review.id()).isNotNull();
            assertThat(review.isDeleted()).isFalse();
        }

        @Test
        @DisplayName("신규 생성된 리뷰는 활성 상태이다")
        void newReviewIsActive() {
            // when
            Review review = ReviewFixtures.newReview();

            // then
            assertThat(review.isDeleted()).isFalse();
            assertThat(review.isNew()).isTrue();
        }

        @Test
        @DisplayName("신규 생성 시 legacyMemberId와 memberId를 모두 설정한다")
        void newReviewSetsBothMemberIds() {
            // when
            Review review = ReviewFixtures.newReview();

            // then
            assertThat(review.legacyMemberId()).isNotNull();
            assertThat(review.legacyMemberIdValue()).isEqualTo(100L);
            assertThat(review.memberId()).isNotNull();
            assertThat(review.memberIdValue()).isEqualTo(1L);
        }

        @Test
        @DisplayName("신규 생성 시 createdAt과 updatedAt이 동일하게 설정된다")
        void newReviewCreatedAtEqualsUpdatedAt() {
            // given
            Instant now = CommonVoFixtures.now();

            // when
            Review review =
                    Review.forNew(
                            ReviewFixtures.defaultLegacyMemberId(),
                            ReviewFixtures.defaultMemberId(),
                            ReviewFixtures.defaultLegacyOrderId(),
                            ReviewFixtures.defaultOrderId(),
                            ReviewFixtures.defaultProductGroupId(),
                            ReviewFixtures.defaultRating(),
                            ReviewFixtures.defaultReviewContent(),
                            now);

            // then
            assertThat(review.createdAt()).isEqualTo(now);
            assertThat(review.updatedAt()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("reconstitute() - 영속성 복원")
    class ReconstituteTest {

        @Test
        @DisplayName("활성 리뷰를 복원한다")
        void reconstituteActiveReview() {
            // when
            Review review = ReviewFixtures.activeReview();

            // then
            assertThat(review.isNew()).isFalse();
            assertThat(review.idValue()).isEqualTo(1L);
            assertThat(review.isDeleted()).isFalse();
        }

        @Test
        @DisplayName("삭제된 리뷰를 복원한다")
        void reconstituteDeletedReview() {
            // when
            Review review = ReviewFixtures.deletedReview();

            // then
            assertThat(review.isDeleted()).isTrue();
            assertThat(review.idValue()).isEqualTo(2L);
        }

        @Test
        @DisplayName("레거시 ID만 있는 리뷰를 복원한다")
        void reconstituteReviewWithLegacyIdsOnly() {
            // when
            Review review = ReviewFixtures.reviewWithLegacyOnlyIds();

            // then
            assertThat(review.memberId()).isNull();
            assertThat(review.memberIdValue()).isNull();
            assertThat(review.orderId()).isNull();
            assertThat(review.orderIdValue()).isNull();
            assertThat(review.legacyMemberId()).isNotNull();
            assertThat(review.legacyOrderId()).isNotNull();
        }
    }

    @Nested
    @DisplayName("delete() - 소프트 삭제")
    class DeletionTest {

        @Test
        @DisplayName("활성 리뷰를 소프트 삭제한다")
        void deleteActiveReview() {
            // given
            Review review = ReviewFixtures.activeReview();
            Instant now = CommonVoFixtures.now();

            // when
            review.delete(now);

            // then
            assertThat(review.isDeleted()).isTrue();
            assertThat(review.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("삭제 후 updatedAt이 갱신된다")
        void deleteUpdatesUpdatedAt() {
            // given
            Review review = ReviewFixtures.activeReview();
            Instant beforeDelete = review.updatedAt();
            Instant deleteTime = CommonVoFixtures.now();

            // when
            review.delete(deleteTime);

            // then
            assertThat(review.updatedAt()).isEqualTo(deleteTime);
            assertThat(review.updatedAt()).isNotEqualTo(beforeDelete);
        }
    }

    @Nested
    @DisplayName("Getter 메서드 테스트")
    class GetterTest {

        @Test
        @DisplayName("id() 및 idValue()가 올바른 값을 반환한다")
        void returnsIdValues() {
            // when
            Review review = ReviewFixtures.activeReview();

            // then
            assertThat(review.id()).isNotNull();
            assertThat(review.idValue()).isEqualTo(1L);
        }

        @Test
        @DisplayName("rating() 및 ratingValue()가 올바른 값을 반환한다")
        void returnsRatingValues() {
            // when
            Review review = ReviewFixtures.activeReview();

            // then
            assertThat(review.rating()).isNotNull();
            assertThat(review.ratingValue()).isEqualTo(4.5);
        }

        @Test
        @DisplayName("content() 및 contentValue()가 올바른 값을 반환한다")
        void returnsContentValues() {
            // when
            Review review = ReviewFixtures.activeReview();

            // then
            assertThat(review.content()).isNotNull();
            assertThat(review.contentValue()).isEqualTo("정말 만족스러운 상품이었습니다. 배송도 빠르고 품질도 좋아요.");
        }

        @Test
        @DisplayName("productGroupId() 및 productGroupIdValue()가 올바른 값을 반환한다")
        void returnsProductGroupIdValues() {
            // when
            Review review = ReviewFixtures.activeReview();

            // then
            assertThat(review.productGroupId()).isNotNull();
            assertThat(review.productGroupIdValue()).isEqualTo(500L);
        }

        @Test
        @DisplayName("legacyOrderIdValue()가 올바른 값을 반환한다")
        void returnsLegacyOrderIdValue() {
            // when
            Review review = ReviewFixtures.activeReview();

            // then
            assertThat(review.legacyOrderIdValue()).isEqualTo(700L);
        }

        @Test
        @DisplayName("orderIdValue()가 올바른 값을 반환한다")
        void returnsOrderIdValue() {
            // when
            Review review = ReviewFixtures.activeReview();

            // then
            assertThat(review.orderIdValue()).isEqualTo("order-uuid-0001");
        }

        @Test
        @DisplayName("null orderId의 orderIdValue()는 null을 반환한다")
        void returnsNullOrderIdValueWhenOrderIdIsNull() {
            // when
            Review review = ReviewFixtures.reviewWithLegacyOnlyIds();

            // then
            assertThat(review.orderIdValue()).isNull();
        }

        @Test
        @DisplayName("null memberId의 memberIdValue()는 null을 반환한다")
        void returnsNullMemberIdValueWhenMemberIdIsNull() {
            // when
            Review review = ReviewFixtures.reviewWithLegacyOnlyIds();

            // then
            assertThat(review.memberIdValue()).isNull();
        }

        @Test
        @DisplayName("deletionStatus()를 반환한다")
        void returnsDeletionStatus() {
            // when
            Review review = ReviewFixtures.activeReview();

            // then
            assertThat(review.deletionStatus()).isNotNull();
            assertThat(review.deletionStatus().isDeleted()).isFalse();
        }

        @Test
        @DisplayName("createdAt()과 updatedAt()이 null이 아니다")
        void returnsTimeValues() {
            // when
            Review review = ReviewFixtures.activeReview();

            // then
            assertThat(review.createdAt()).isNotNull();
            assertThat(review.updatedAt()).isNotNull();
        }
    }

    @Nested
    @DisplayName("null 허용 필드 테스트")
    class NullableFieldTest {

        @Test
        @DisplayName("content가 null인 경우 contentValue()는 null을 반환한다")
        void contentValueIsNullWhenContentIsNull() {
            // given
            Review review =
                    ReviewFixtures.newReview(
                            ReviewFixtures.defaultRating(), ReviewFixtures.emptyReviewContent());

            // then
            assertThat(review.contentValue()).isNull();
        }
    }
}
