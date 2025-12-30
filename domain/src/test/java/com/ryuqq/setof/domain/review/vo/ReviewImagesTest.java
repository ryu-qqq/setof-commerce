package com.ryuqq.setof.domain.review.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.domain.review.ReviewFixture;
import com.ryuqq.setof.domain.review.exception.ReviewImageLimitExceededException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * ReviewImages Value Object 테스트
 *
 * <p>리뷰 이미지 컬렉션 값 객체의 유효성 검증 로직을 테스트합니다.
 */
@DisplayName("ReviewImages Value Object")
class ReviewImagesTest {

    @Nested
    @DisplayName("of() - 생성")
    class Of {

        @Test
        @DisplayName("이미지 리스트로 생성할 수 있다")
        void shouldCreateWithImageList() {
            // given
            List<ReviewImage> imageList =
                    List.of(
                            ReviewImage.of(ReviewImageType.PHOTO, "https://example.com/1.jpg", 0),
                            ReviewImage.of(ReviewImageType.PHOTO, "https://example.com/2.jpg", 1));

            // when
            ReviewImages images = ReviewImages.of(imageList);

            // then
            assertEquals(2, images.size());
            assertTrue(images.hasImages());
        }

        @Test
        @DisplayName("최대 3개까지 이미지를 추가할 수 있다")
        void shouldCreateWithMaxThreeImages() {
            // given
            List<ReviewImage> imageList =
                    List.of(
                            ReviewImage.of(ReviewImageType.PHOTO, "https://example.com/1.jpg", 0),
                            ReviewImage.of(ReviewImageType.PHOTO, "https://example.com/2.jpg", 1),
                            ReviewImage.of(ReviewImageType.PHOTO, "https://example.com/3.jpg", 2));

            // when
            ReviewImages images = ReviewImages.of(imageList);

            // then
            assertEquals(3, images.size());
        }

        @Test
        @DisplayName("4개 이상의 이미지는 예외가 발생한다")
        void shouldThrowExceptionWhenExceedsMaxImages() {
            // given
            List<ReviewImage> imageList =
                    List.of(
                            ReviewImage.of(ReviewImageType.PHOTO, "https://example.com/1.jpg", 0),
                            ReviewImage.of(ReviewImageType.PHOTO, "https://example.com/2.jpg", 1),
                            ReviewImage.of(ReviewImageType.PHOTO, "https://example.com/3.jpg", 2),
                            ReviewImage.of(ReviewImageType.PHOTO, "https://example.com/4.jpg", 3));

            // when & then
            assertThrows(ReviewImageLimitExceededException.class, () -> ReviewImages.of(imageList));
        }
    }

    @Nested
    @DisplayName("empty() - 빈 이미지 컬렉션 생성")
    class Empty {

        @Test
        @DisplayName("빈 이미지 컬렉션을 생성할 수 있다")
        void shouldCreateEmptyImages() {
            // when
            ReviewImages images = ReviewImages.empty();

            // then
            assertFalse(images.hasImages());
            assertEquals(0, images.size());
            assertTrue(images.getImages().isEmpty());
        }
    }

    @Nested
    @DisplayName("상태 확인 메서드")
    class StatusChecks {

        @Test
        @DisplayName("hasImages()는 이미지가 있을 때 true를 반환한다")
        void shouldReturnTrueWhenHasImages() {
            // given
            ReviewImages images = ReviewFixture.createImages(2);

            // then
            assertTrue(images.hasImages());
        }

        @Test
        @DisplayName("hasImages()는 이미지가 없을 때 false를 반환한다")
        void shouldReturnFalseWhenNoImages() {
            // given
            ReviewImages images = ReviewImages.empty();

            // then
            assertFalse(images.hasImages());
        }

        @Test
        @DisplayName("size()는 이미지 개수를 반환한다")
        void shouldReturnImageCount() {
            // given
            ReviewImages images = ReviewFixture.createImages(3);

            // then
            assertEquals(3, images.size());
        }

        @Test
        @DisplayName("getImages()는 불변 리스트를 반환한다")
        void shouldReturnImmutableList() {
            // given
            ReviewImages images = ReviewFixture.createImages(2);
            List<ReviewImage> imageList = images.getImages();

            // when & then
            assertThrows(
                    UnsupportedOperationException.class,
                    () ->
                            imageList.add(
                                    ReviewImage.of(
                                            ReviewImageType.PHOTO,
                                            "https://example.com/new.jpg",
                                            3)));
        }
    }

    @Nested
    @DisplayName("equals & hashCode")
    class EqualsAndHashCode {

        @Test
        @DisplayName("같은 이미지를 가진 ReviewImages는 동등하다")
        void shouldBeEqualWhenSameImages() {
            // given
            List<ReviewImage> imageList =
                    List.of(ReviewImage.of(ReviewImageType.PHOTO, "https://example.com/1.jpg", 0));
            ReviewImages images1 = ReviewImages.of(imageList);
            ReviewImages images2 = ReviewImages.of(imageList);

            // then
            assertEquals(images1, images2);
            assertEquals(images1.hashCode(), images2.hashCode());
        }
    }
}
