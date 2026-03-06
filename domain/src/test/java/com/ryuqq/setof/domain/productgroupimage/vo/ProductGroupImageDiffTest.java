package com.ryuqq.setof.domain.productgroupimage.vo;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.productgroup.vo.ImageType;
import com.ryuqq.setof.domain.productgroup.vo.ImageUrl;
import com.ryuqq.setof.domain.productgroupimage.aggregate.ProductGroupImage;
import com.ryuqq.setof.domain.productgroupimage.id.ProductGroupImageId;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductGroupImageDiff VO 테스트")
class ProductGroupImageDiffTest {

    private static ProductGroupImage thumbnail(long id) {
        return ProductGroupImage.reconstitute(
                ProductGroupImageId.of(id),
                ImageType.THUMBNAIL,
                ImageUrl.of("https://example.com/thumb.png"),
                0);
    }

    private static ProductGroupImage detail(long id, String url) {
        return ProductGroupImage.reconstitute(
                ProductGroupImageId.of(id), ImageType.DETAIL, ImageUrl.of(url), 1);
    }

    @Nested
    @DisplayName("of() - 팩토리 메서드")
    class OfTest {

        @Test
        @DisplayName("추가/삭제/유지 목록과 발생 시각으로 Diff를 생성한다")
        void createDiffWithAllLists() {
            // given
            List<ProductGroupImage> added = List.of(thumbnail(1L));
            List<ProductGroupImage> removed = List.of(detail(2L, "https://example.com/old.png"));
            List<ProductGroupImage> retained = List.of(detail(3L, "https://example.com/keep.png"));
            Instant occurredAt = Instant.now();

            // when
            ProductGroupImageDiff diff =
                    ProductGroupImageDiff.of(added, removed, retained, occurredAt);

            // then
            assertThat(diff.added()).hasSize(1);
            assertThat(diff.removed()).hasSize(1);
            assertThat(diff.retained()).hasSize(1);
            assertThat(diff.occurredAt()).isEqualTo(occurredAt);
        }
    }

    @Nested
    @DisplayName("hasNoChanges() - 변경 없음 여부 확인")
    class HasNoChangesTest {

        @Test
        @DisplayName("added와 removed가 모두 비어있으면 true를 반환한다")
        void returnsTrueWhenNoAddedOrRemoved() {
            // given
            List<ProductGroupImage> retained = List.of(thumbnail(1L));
            ProductGroupImageDiff diff =
                    ProductGroupImageDiff.of(List.of(), List.of(), retained, Instant.now());

            // when & then
            assertThat(diff.hasNoChanges()).isTrue();
        }

        @Test
        @DisplayName("added가 있으면 false를 반환한다")
        void returnsFalseWhenAddedExists() {
            // given
            List<ProductGroupImage> added = List.of(thumbnail(1L));
            ProductGroupImageDiff diff =
                    ProductGroupImageDiff.of(added, List.of(), List.of(), Instant.now());

            // when & then
            assertThat(diff.hasNoChanges()).isFalse();
        }

        @Test
        @DisplayName("removed가 있으면 false를 반환한다")
        void returnsFalseWhenRemovedExists() {
            // given
            List<ProductGroupImage> removed = List.of(detail(2L, "https://example.com/old.png"));
            ProductGroupImageDiff diff =
                    ProductGroupImageDiff.of(List.of(), removed, List.of(), Instant.now());

            // when & then
            assertThat(diff.hasNoChanges()).isFalse();
        }

        @Test
        @DisplayName("added와 removed가 모두 있으면 false를 반환한다")
        void returnsFalseWhenBothExist() {
            // given
            List<ProductGroupImage> added = List.of(thumbnail(1L));
            List<ProductGroupImage> removed = List.of(detail(2L, "https://example.com/old.png"));
            ProductGroupImageDiff diff =
                    ProductGroupImageDiff.of(added, removed, List.of(), Instant.now());

            // when & then
            assertThat(diff.hasNoChanges()).isFalse();
        }
    }

    @Nested
    @DisplayName("불변성 테스트")
    class ImmutabilityTest {

        @Test
        @DisplayName("added 목록은 외부에서 수정할 수 없다")
        void addedListIsImmutable() {
            // given
            List<ProductGroupImage> mutableAdded =
                    new java.util.ArrayList<>(List.of(thumbnail(1L)));
            ProductGroupImageDiff diff =
                    ProductGroupImageDiff.of(mutableAdded, List.of(), List.of(), Instant.now());

            // when - 원본 목록 수정 시도
            mutableAdded.clear();

            // then - diff 내부는 변경되지 않는다
            assertThat(diff.added()).hasSize(1);
        }

        @Test
        @DisplayName("removed 목록은 외부에서 수정할 수 없다")
        void removedListIsImmutable() {
            // given
            List<ProductGroupImage> mutableRemoved =
                    new java.util.ArrayList<>(List.of(detail(2L, "https://example.com/old.png")));
            ProductGroupImageDiff diff =
                    ProductGroupImageDiff.of(List.of(), mutableRemoved, List.of(), Instant.now());

            // when - 원본 목록 수정 시도
            mutableRemoved.clear();

            // then - diff 내부는 변경되지 않는다
            assertThat(diff.removed()).hasSize(1);
        }

        @Test
        @DisplayName("retained 목록은 외부에서 수정할 수 없다")
        void retainedListIsImmutable() {
            // given
            List<ProductGroupImage> mutableRetained =
                    new java.util.ArrayList<>(List.of(thumbnail(1L)));
            ProductGroupImageDiff diff =
                    ProductGroupImageDiff.of(List.of(), List.of(), mutableRetained, Instant.now());

            // when - 원본 목록 수정 시도
            mutableRetained.clear();

            // then - diff 내부는 변경되지 않는다
            assertThat(diff.retained()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Accessor 테스트")
    class AccessorTest {

        @Test
        @DisplayName("occurredAt()은 생성 시 지정한 시각을 반환한다")
        void returnsOccurredAt() {
            // given
            Instant expected = Instant.parse("2025-03-01T10:00:00Z");
            ProductGroupImageDiff diff =
                    ProductGroupImageDiff.of(List.of(), List.of(), List.of(), expected);

            // when & then
            assertThat(diff.occurredAt()).isEqualTo(expected);
        }

        @Test
        @DisplayName("added(), removed(), retained()는 각각 지정한 목록을 반환한다")
        void returnsAllLists() {
            // given
            ProductGroupImage addedImage = thumbnail(1L);
            ProductGroupImage removedImage = detail(2L, "https://example.com/old.png");
            ProductGroupImage retainedImage = detail(3L, "https://example.com/keep.png");
            Instant occurredAt = Instant.now();

            ProductGroupImageDiff diff =
                    ProductGroupImageDiff.of(
                            List.of(addedImage),
                            List.of(removedImage),
                            List.of(retainedImage),
                            occurredAt);

            // when & then
            assertThat(diff.added()).containsExactly(addedImage);
            assertThat(diff.removed()).containsExactly(removedImage);
            assertThat(diff.retained()).containsExactly(retainedImage);
        }
    }
}
