package com.ryuqq.setof.domain.review.vo;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.review.ReviewFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("WrittenReview Value Object 테스트")
class WrittenReviewTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("기본 WrittenReview를 생성한다")
        void createDefaultWrittenReview() {
            // when
            WrittenReview review = ReviewFixtures.defaultWrittenReview();

            // then
            assertThat(review).isNotNull();
            assertThat(review.reviewId()).isEqualTo(1L);
            assertThat(review.legacyOrderId()).isEqualTo(700L);
            assertThat(review.orderId()).isEqualTo("order-uuid-0001");
            assertThat(review.userName()).isEqualTo("홍길동");
            assertThat(review.rating()).isEqualTo(4.5);
            assertThat(review.content()).isEqualTo("정말 만족스러운 상품이었습니다.");
        }

        @Test
        @DisplayName("이미지 없는 WrittenReview를 생성한다")
        void createWrittenReviewWithoutImages() {
            // when
            WrittenReview review = ReviewFixtures.writtenReviewWithoutImages();

            // then
            assertThat(review.images()).isEmpty();
            assertThat(review.rating()).isEqualTo(3.0);
        }
    }

    @Nested
    @DisplayName("내부 스냅샷 레코드 테스트")
    class SnapshotTest {

        @Test
        @DisplayName("ProductGroupSnapshot을 올바르게 생성한다")
        void createProductGroupSnapshot() {
            // when
            WrittenReview.ProductGroupSnapshot snapshot =
                    new WrittenReview.ProductGroupSnapshot(
                            500L, "테스트 상품", "https://example.com/image.jpg", "사이즈:M");

            // then
            assertThat(snapshot.productGroupId()).isEqualTo(500L);
            assertThat(snapshot.name()).isEqualTo("테스트 상품");
            assertThat(snapshot.imageUrl()).isEqualTo("https://example.com/image.jpg");
            assertThat(snapshot.option()).isEqualTo("사이즈:M");
        }

        @Test
        @DisplayName("BrandSnapshot을 올바르게 생성한다")
        void createBrandSnapshot() {
            // when
            WrittenReview.BrandSnapshot snapshot = new WrittenReview.BrandSnapshot(20L, "테스트 브랜드");

            // then
            assertThat(snapshot.brandId()).isEqualTo(20L);
            assertThat(snapshot.name()).isEqualTo("테스트 브랜드");
        }

        @Test
        @DisplayName("CategorySnapshot을 올바르게 생성한다")
        void createCategorySnapshot() {
            // when
            WrittenReview.CategorySnapshot snapshot = new WrittenReview.CategorySnapshot(30L, "상의");

            // then
            assertThat(snapshot.categoryId()).isEqualTo(30L);
            assertThat(snapshot.name()).isEqualTo("상의");
        }

        @Test
        @DisplayName("WrittenReviewImage를 올바르게 생성한다")
        void createWrittenReviewImage() {
            // when
            WrittenReview.WrittenReviewImage image =
                    new WrittenReview.WrittenReviewImage(
                            "REVIEW", "https://example.com/review-image.jpg");

            // then
            assertThat(image.reviewImageType()).isEqualTo("REVIEW");
            assertThat(image.imageUrl()).isEqualTo("https://example.com/review-image.jpg");
        }
    }

    @Nested
    @DisplayName("이미지 목록 테스트")
    class ImageListTest {

        @Test
        @DisplayName("이미지 목록을 올바르게 반환한다")
        void returnsImageList() {
            // when
            WrittenReview review = ReviewFixtures.defaultWrittenReview();

            // then
            assertThat(review.images()).hasSize(1);
            assertThat(review.images().get(0).reviewImageType()).isEqualTo("REVIEW");
        }

        @Test
        @DisplayName("이미지가 없으면 빈 목록을 반환한다")
        void returnsEmptyImageList() {
            // when
            WrittenReview review = ReviewFixtures.writtenReviewWithoutImages();

            // then
            assertThat(review.images()).isEmpty();
        }
    }

    @Nested
    @DisplayName("스냅샷 참조 테스트")
    class SnapshotReferenceTest {

        @Test
        @DisplayName("WrittenReview에서 스냅샷 정보를 올바르게 조회한다")
        void retrieveSnapshotFromWrittenReview() {
            // when
            WrittenReview review = ReviewFixtures.defaultWrittenReview();

            // then
            assertThat(review.productGroup().productGroupId()).isEqualTo(500L);
            assertThat(review.brand().brandId()).isEqualTo(20L);
            assertThat(review.category().categoryId()).isEqualTo(30L);
        }
    }
}
