package com.ryuqq.setof.domain.contentpage.vo;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductThumbnailSnapshot resolvedName/resolvedImageUrl 단위 테스트")
class ProductThumbnailSnapshotResolvedTest {

    private ProductThumbnailSnapshot snapshotWithDisplay(
            String productGroupName,
            String productImageUrl,
            String displayName,
            String displayImageUrl) {
        return new ProductThumbnailSnapshot(
                1L,
                1L,
                productGroupName,
                10L,
                "브랜드A",
                productImageUrl,
                displayName,
                displayImageUrl,
                10000,
                8000,
                8000,
                0,
                0,
                20,
                Instant.parse("2025-01-01T00:00:00Z"),
                true,
                false);
    }

    @Nested
    @DisplayName("resolvedName() - 전시 노출 상품명 결정")
    class ResolvedNameTest {

        @Test
        @DisplayName("displayName이 null이면 productGroupName을 반환한다")
        void nullDisplayNameReturnsProductGroupName() {
            // given
            ProductThumbnailSnapshot snapshot =
                    snapshotWithDisplay("원본 상품명", "https://example.com/img.jpg", null, null);

            // when & then
            assertThat(snapshot.resolvedName()).isEqualTo("원본 상품명");
        }

        @Test
        @DisplayName("displayName이 공백 문자열이면 productGroupName을 반환한다")
        void blankDisplayNameReturnsProductGroupName() {
            // given
            ProductThumbnailSnapshot snapshot =
                    snapshotWithDisplay("원본 상품명", "https://example.com/img.jpg", "   ", null);

            // when & then
            assertThat(snapshot.resolvedName()).isEqualTo("원본 상품명");
        }

        @Test
        @DisplayName("displayName이 빈 문자열이면 productGroupName을 반환한다")
        void emptyDisplayNameReturnsProductGroupName() {
            // given
            ProductThumbnailSnapshot snapshot =
                    snapshotWithDisplay("원본 상품명", "https://example.com/img.jpg", "", null);

            // when & then
            assertThat(snapshot.resolvedName()).isEqualTo("원본 상품명");
        }

        @Test
        @DisplayName("displayName이 유효한 값이면 displayName을 반환한다")
        void validDisplayNameReturnsDisplayName() {
            // given
            ProductThumbnailSnapshot snapshot =
                    snapshotWithDisplay("원본 상품명", "https://example.com/img.jpg", "전시용 상품명", null);

            // when & then
            assertThat(snapshot.resolvedName()).isEqualTo("전시용 상품명");
        }
    }

    @Nested
    @DisplayName("resolvedImageUrl() - 전시 노출 이미지 URL 결정")
    class ResolvedImageUrlTest {

        @Test
        @DisplayName("displayImageUrl이 null이면 productImageUrl을 반환한다")
        void nullDisplayImageUrlReturnsProductImageUrl() {
            // given
            ProductThumbnailSnapshot snapshot =
                    snapshotWithDisplay("상품명", "https://example.com/product.jpg", null, null);

            // when & then
            assertThat(snapshot.resolvedImageUrl()).isEqualTo("https://example.com/product.jpg");
        }

        @Test
        @DisplayName("displayImageUrl이 공백 문자열이면 productImageUrl을 반환한다")
        void blankDisplayImageUrlReturnsProductImageUrl() {
            // given
            ProductThumbnailSnapshot snapshot =
                    snapshotWithDisplay("상품명", "https://example.com/product.jpg", null, "   ");

            // when & then
            assertThat(snapshot.resolvedImageUrl()).isEqualTo("https://example.com/product.jpg");
        }

        @Test
        @DisplayName("displayImageUrl이 빈 문자열이면 productImageUrl을 반환한다")
        void emptyDisplayImageUrlReturnsProductImageUrl() {
            // given
            ProductThumbnailSnapshot snapshot =
                    snapshotWithDisplay("상품명", "https://example.com/product.jpg", null, "");

            // when & then
            assertThat(snapshot.resolvedImageUrl()).isEqualTo("https://example.com/product.jpg");
        }

        @Test
        @DisplayName("displayImageUrl이 유효한 값이면 displayImageUrl을 반환한다")
        void validDisplayImageUrlReturnsDisplayImageUrl() {
            // given
            ProductThumbnailSnapshot snapshot =
                    snapshotWithDisplay(
                            "상품명",
                            "https://example.com/product.jpg",
                            null,
                            "https://example.com/display.jpg");

            // when & then
            assertThat(snapshot.resolvedImageUrl()).isEqualTo("https://example.com/display.jpg");
        }
    }

    @Nested
    @DisplayName("displayName과 displayImageUrl 동시 오버라이드")
    class BothOverrideTest {

        @Test
        @DisplayName("displayName과 displayImageUrl 모두 설정 시 둘 다 오버라이드된다")
        void bothFieldsOverriddenWhenSet() {
            // given
            ProductThumbnailSnapshot snapshot =
                    snapshotWithDisplay(
                            "원본 상품명",
                            "https://example.com/product.jpg",
                            "전시용 상품명",
                            "https://example.com/display.jpg");

            // when & then
            assertThat(snapshot.resolvedName()).isEqualTo("전시용 상품명");
            assertThat(snapshot.resolvedImageUrl()).isEqualTo("https://example.com/display.jpg");
        }

        @Test
        @DisplayName("displayName과 displayImageUrl 모두 null이면 원본 값을 사용한다")
        void bothNullUsesOriginalValues() {
            // given
            ProductThumbnailSnapshot snapshot =
                    snapshotWithDisplay("원본 상품명", "https://example.com/product.jpg", null, null);

            // when & then
            assertThat(snapshot.resolvedName()).isEqualTo("원본 상품명");
            assertThat(snapshot.resolvedImageUrl()).isEqualTo("https://example.com/product.jpg");
        }
    }
}
