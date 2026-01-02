package com.ryuqq.setof.domain.review.aggregate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.domain.review.ReviewFixture;
import com.ryuqq.setof.domain.review.exception.ReviewAlreadyDeletedException;
import com.ryuqq.setof.domain.review.exception.ReviewNotOwnedException;
import com.ryuqq.setof.domain.review.vo.Rating;
import com.ryuqq.setof.domain.review.vo.ReviewContent;
import com.ryuqq.setof.domain.review.vo.ReviewImages;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Review Aggregate 테스트
 *
 * <p>리뷰 도메인 로직을 테스트합니다.
 */
@DisplayName("Review Aggregate")
class ReviewTest {

    private static final UUID DEFAULT_MEMBER_ID =
            UUID.fromString("01929b9e-0d4f-7ab0-b4d8-1c2d3e4f5a6b");
    private static final Long DEFAULT_ORDER_ID = 200L;
    private static final Long DEFAULT_PRODUCT_GROUP_ID = 300L;
    private static final UUID OTHER_MEMBER_ID =
            UUID.fromString("01929b9e-1111-7ab0-b4d8-999999999999");
    private static final Clock FIXED_CLOCK =
            Clock.fixed(Instant.parse("2025-01-01T00:00:00Z"), ZoneOffset.UTC);

    @Nested
    @DisplayName("create() - 신규 리뷰 생성")
    class Create {

        @Test
        @DisplayName("신규 리뷰를 생성할 수 있다")
        void shouldCreateNewReview() {
            // given
            Rating rating = Rating.of(5);
            ReviewContent content = ReviewContent.of("좋은 상품입니다.");
            ReviewImages images = ReviewImages.empty();

            // when
            Review review =
                    Review.create(
                            DEFAULT_MEMBER_ID,
                            DEFAULT_ORDER_ID,
                            DEFAULT_PRODUCT_GROUP_ID,
                            rating,
                            content,
                            images,
                            FIXED_CLOCK);

            // then
            assertNotNull(review);
            assertNull(review.getId());
            assertEquals(DEFAULT_MEMBER_ID, review.getMemberId());
            assertEquals(DEFAULT_ORDER_ID, review.getOrderId());
            assertEquals(DEFAULT_PRODUCT_GROUP_ID, review.getProductGroupId());
            assertEquals(5, review.getRatingValue());
            assertNotNull(review.getCreatedAt());
            assertNull(review.getUpdatedAt());
            assertNull(review.getDeletedAt());
            assertTrue(review.isActive());
            assertFalse(review.isDeleted());
        }

        @Test
        @DisplayName("내용 없이 리뷰를 생성할 수 있다")
        void shouldCreateReviewWithoutContent() {
            // given
            Rating rating = Rating.of(4);

            // when
            Review review =
                    Review.create(
                            DEFAULT_MEMBER_ID,
                            DEFAULT_ORDER_ID,
                            DEFAULT_PRODUCT_GROUP_ID,
                            rating,
                            null,
                            null,
                            FIXED_CLOCK);

            // then
            assertNotNull(review);
            assertNotNull(review.getContent());
            assertFalse(review.hasImages());
        }

        @Test
        @DisplayName("이미지와 함께 리뷰를 생성할 수 있다")
        void shouldCreateReviewWithImages() {
            // given
            Rating rating = Rating.of(5);
            ReviewContent content = ReviewContent.of("사진 첨부합니다.");
            ReviewImages images = ReviewFixture.createImages(3);

            // when
            Review review =
                    Review.create(
                            DEFAULT_MEMBER_ID,
                            DEFAULT_ORDER_ID,
                            DEFAULT_PRODUCT_GROUP_ID,
                            rating,
                            content,
                            images,
                            FIXED_CLOCK);

            // then
            assertNotNull(review);
            assertTrue(review.hasImages());
            assertEquals(3, review.getImageList().size());
        }

        @Test
        @DisplayName("userId가 null이면 예외가 발생한다")
        void shouldThrowExceptionWhenUserIdIsNull() {
            // given
            Rating rating = Rating.of(5);

            // when & then
            assertThrows(
                    IllegalArgumentException.class,
                    () ->
                            Review.create(
                                    null,
                                    DEFAULT_ORDER_ID,
                                    DEFAULT_PRODUCT_GROUP_ID,
                                    rating,
                                    null,
                                    null,
                                    FIXED_CLOCK));
        }

        @Test
        @DisplayName("orderId가 null이면 예외가 발생한다")
        void shouldThrowExceptionWhenOrderIdIsNull() {
            // given
            Rating rating = Rating.of(5);

            // when & then
            assertThrows(
                    IllegalArgumentException.class,
                    () ->
                            Review.create(
                                    DEFAULT_MEMBER_ID,
                                    null,
                                    DEFAULT_PRODUCT_GROUP_ID,
                                    rating,
                                    null,
                                    null,
                                    FIXED_CLOCK));
        }

        @Test
        @DisplayName("productGroupId가 null이면 예외가 발생한다")
        void shouldThrowExceptionWhenProductGroupIdIsNull() {
            // given
            Rating rating = Rating.of(5);

            // when & then
            assertThrows(
                    IllegalArgumentException.class,
                    () ->
                            Review.create(
                                    DEFAULT_MEMBER_ID,
                                    DEFAULT_ORDER_ID,
                                    null,
                                    rating,
                                    null,
                                    null,
                                    FIXED_CLOCK));
        }

        @Test
        @DisplayName("rating이 null이면 예외가 발생한다")
        void shouldThrowExceptionWhenRatingIsNull() {
            // when & then
            assertThrows(
                    IllegalArgumentException.class,
                    () ->
                            Review.create(
                                    DEFAULT_MEMBER_ID,
                                    DEFAULT_ORDER_ID,
                                    DEFAULT_PRODUCT_GROUP_ID,
                                    null,
                                    null,
                                    null,
                                    FIXED_CLOCK));
        }
    }

    @Nested
    @DisplayName("reconstitute() - Persistence 복원")
    class Reconstitute {

        @Test
        @DisplayName("Persistence에서 모든 필드를 복원할 수 있다")
        void shouldReconstituteReviewFromPersistence() {
            // given & when
            Review review = ReviewFixture.reconstitute();

            // then
            assertNotNull(review.getId());
            assertEquals(1L, review.getId().getValue());
            assertEquals(DEFAULT_MEMBER_ID, review.getMemberId());
            assertEquals(DEFAULT_ORDER_ID, review.getOrderId());
            assertEquals(DEFAULT_PRODUCT_GROUP_ID, review.getProductGroupId());
            assertNotNull(review.getCreatedAt());
        }

        @Test
        @DisplayName("삭제된 리뷰를 복원할 수 있다")
        void shouldReconstituteDeletedReview() {
            // given & when
            Review review = ReviewFixture.reconstituteDeleted();

            // then
            assertTrue(review.isDeleted());
            assertFalse(review.isActive());
            assertNotNull(review.getDeletedAt());
        }
    }

    @Nested
    @DisplayName("update() - 리뷰 수정")
    class Update {

        @Test
        @DisplayName("본인이 작성한 리뷰를 수정할 수 있다")
        void shouldUpdateOwnReview() {
            // given
            Review review = ReviewFixture.reconstitute();
            Rating newRating = Rating.of(3);
            ReviewContent newContent = ReviewContent.of("수정된 내용입니다.");

            // when
            review.update(newRating, newContent, null, DEFAULT_MEMBER_ID, FIXED_CLOCK);

            // then
            assertEquals(3, review.getRatingValue());
            assertNotNull(review.getUpdatedAt());
        }

        @Test
        @DisplayName("평점만 수정할 수 있다")
        void shouldUpdateOnlyRating() {
            // given
            Review review = ReviewFixture.reconstitute();
            Rating newRating = Rating.of(2);

            // when
            review.update(newRating, null, null, DEFAULT_MEMBER_ID, FIXED_CLOCK);

            // then
            assertEquals(2, review.getRatingValue());
        }

        @Test
        @DisplayName("이미지를 수정할 수 있다")
        void shouldUpdateImages() {
            // given
            Review review = ReviewFixture.reconstitute();
            ReviewImages newImages = ReviewFixture.createImages(2);

            // when
            review.update(null, null, newImages, DEFAULT_MEMBER_ID, FIXED_CLOCK);

            // then
            assertTrue(review.hasImages());
            assertEquals(2, review.getImageList().size());
        }

        @Test
        @DisplayName("본인이 아니면 수정할 수 없다")
        void shouldNotUpdateOthersReview() {
            // given
            Review review = ReviewFixture.reconstitute();
            Rating newRating = Rating.of(3);

            // when & then
            assertThrows(
                    ReviewNotOwnedException.class,
                    () -> review.update(newRating, null, null, OTHER_MEMBER_ID, FIXED_CLOCK));
        }

        @Test
        @DisplayName("삭제된 리뷰는 수정할 수 없다")
        void shouldNotUpdateDeletedReview() {
            // given
            Review review = ReviewFixture.reconstituteDeleted();
            Rating newRating = Rating.of(3);

            // when & then
            assertThrows(
                    ReviewAlreadyDeletedException.class,
                    () -> review.update(newRating, null, null, DEFAULT_MEMBER_ID, FIXED_CLOCK));
        }
    }

    @Nested
    @DisplayName("delete() - 리뷰 삭제")
    class Delete {

        @Test
        @DisplayName("본인이 작성한 리뷰를 삭제할 수 있다")
        void shouldDeleteOwnReview() {
            // given
            Review review = ReviewFixture.reconstitute();
            assertTrue(review.isActive());

            // when
            review.delete(DEFAULT_MEMBER_ID, FIXED_CLOCK);

            // then
            assertTrue(review.isDeleted());
            assertFalse(review.isActive());
            assertNotNull(review.getDeletedAt());
        }

        @Test
        @DisplayName("본인이 아니면 삭제할 수 없다")
        void shouldNotDeleteOthersReview() {
            // given
            Review review = ReviewFixture.reconstitute();

            // when & then
            assertThrows(
                    ReviewNotOwnedException.class,
                    () -> review.delete(OTHER_MEMBER_ID, FIXED_CLOCK));
        }

        @Test
        @DisplayName("이미 삭제된 리뷰는 다시 삭제할 수 없다")
        void shouldNotDeleteAlreadyDeletedReview() {
            // given
            Review review = ReviewFixture.reconstituteDeleted();

            // when & then
            assertThrows(
                    ReviewAlreadyDeletedException.class,
                    () -> review.delete(DEFAULT_MEMBER_ID, FIXED_CLOCK));
        }
    }

    @Nested
    @DisplayName("forceDelete() - 강제 삭제 (관리자)")
    class ForceDelete {

        @Test
        @DisplayName("관리자가 리뷰를 강제 삭제할 수 있다")
        void shouldForceDeleteReview() {
            // given
            Review review = ReviewFixture.reconstitute();
            assertTrue(review.isActive());

            // when
            review.forceDelete(FIXED_CLOCK);

            // then
            assertTrue(review.isDeleted());
            assertFalse(review.isActive());
        }

        @Test
        @DisplayName("이미 삭제된 리뷰는 강제 삭제할 수 없다")
        void shouldNotForceDeleteAlreadyDeletedReview() {
            // given
            Review review = ReviewFixture.reconstituteDeleted();

            // when & then
            assertThrows(
                    ReviewAlreadyDeletedException.class, () -> review.forceDelete(FIXED_CLOCK));
        }
    }

    @Nested
    @DisplayName("상태 확인 메서드")
    class StatusChecks {

        @Test
        @DisplayName("isOwnedBy()는 소유자 ID가 일치하면 true를 반환한다")
        void shouldReturnTrueWhenOwnedByUser() {
            // given
            Review review = ReviewFixture.reconstitute();

            // then
            assertTrue(review.isOwnedBy(DEFAULT_MEMBER_ID));
        }

        @Test
        @DisplayName("isOwnedBy()는 소유자 ID가 일치하지 않으면 false를 반환한다")
        void shouldReturnFalseWhenNotOwnedByUser() {
            // given
            Review review = ReviewFixture.reconstitute();

            // then
            assertFalse(review.isOwnedBy(OTHER_MEMBER_ID));
        }

        @Test
        @DisplayName("hasImages()는 이미지가 있으면 true를 반환한다")
        void shouldReturnTrueWhenHasImages() {
            // given
            Review review = ReviewFixture.createWithImages();

            // then
            assertTrue(review.hasImages());
        }

        @Test
        @DisplayName("hasImages()는 이미지가 없으면 false를 반환한다")
        void shouldReturnFalseWhenNoImages() {
            // given
            Review review = ReviewFixture.create();

            // then
            assertFalse(review.hasImages());
        }
    }

    @Nested
    @DisplayName("equals & hashCode")
    class EqualsAndHashCode {

        @Test
        @DisplayName("같은 ID를 가진 리뷰는 동등하다")
        void shouldBeEqualWhenSameId() {
            // given
            Review review1 = ReviewFixture.reconstitute(1L);
            Review review2 = ReviewFixture.reconstitute(1L);

            // then
            assertEquals(review1, review2);
            assertEquals(review1.hashCode(), review2.hashCode());
        }

        @Test
        @DisplayName("다른 ID를 가진 리뷰는 동등하지 않다")
        void shouldNotBeEqualWhenDifferentId() {
            // given
            Review review1 = ReviewFixture.reconstitute(1L);
            Review review2 = ReviewFixture.reconstitute(2L);

            // then
            assertFalse(review1.equals(review2));
        }
    }

    @Nested
    @DisplayName("Builder Pattern")
    class BuilderPatternTest {

        @Test
        @DisplayName("Builder를 사용하여 리뷰를 생성할 수 있다")
        void shouldCreateReviewUsingBuilder() {
            // given
            UUID customMemberId = UUID.fromString("01929b9e-2222-7ab0-b4d8-aaaaaaaaaaaa");

            // when
            Review review =
                    ReviewFixture.builder()
                            .id(10L)
                            .memberId(customMemberId)
                            .orderId(600L)
                            .productGroupId(700L)
                            .rating(4)
                            .content("빌더로 생성한 리뷰")
                            .imagesCount(2)
                            .build();

            // then
            assertEquals(10L, review.getId().getValue());
            assertEquals(customMemberId, review.getMemberId());
            assertEquals(600L, review.getOrderId());
            assertEquals(700L, review.getProductGroupId());
            assertEquals(4, review.getRatingValue());
            assertEquals(2, review.getImageList().size());
        }

        @Test
        @DisplayName("Builder로 새 리뷰를 생성할 수 있다")
        void shouldCreateNewReviewUsingBuilder() {
            // given & when
            Review review = ReviewFixture.builder().rating(3).content("새 리뷰").buildNew();

            // then
            assertNull(review.getId());
            assertEquals(3, review.getRatingValue());
            assertNotNull(review.getCreatedAt());
        }
    }
}
