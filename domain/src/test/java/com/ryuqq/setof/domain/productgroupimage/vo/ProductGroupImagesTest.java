package com.ryuqq.setof.domain.productgroupimage.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ryuqq.setof.domain.productgroup.vo.ImageType;
import com.ryuqq.setof.domain.productgroup.vo.ImageUrl;
import com.ryuqq.setof.domain.productgroupimage.aggregate.ProductGroupImage;
import com.ryuqq.setof.domain.productgroupimage.exception.ProductGroupImageNoThumbnailException;
import com.ryuqq.setof.domain.productgroupimage.id.ProductGroupImageId;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductGroupImages VO 테스트")
class ProductGroupImagesTest {

    @Nested
    @DisplayName("of() - 이미지 목록 생성")
    class OfTest {

        @Test
        @DisplayName("THUMBNAIL 1개 포함 시 정상 생성된다")
        void createWithOneThumbnail() {
            // given
            var thumbnail =
                    ProductGroupImage.forNew(
                            ImageType.THUMBNAIL, ImageUrl.of("https://t.com/1.png"), 1);
            var detail =
                    ProductGroupImage.forNew(
                            ImageType.DETAIL, ImageUrl.of("https://d.com/1.png"), 2);

            // when
            var images = ProductGroupImages.of(List.of(thumbnail, detail));

            // then
            assertThat(images.toList()).hasSize(2);
            assertThat(images.isEmpty()).isFalse();
        }

        @Test
        @DisplayName("THUMBNAIL 0개이면 ProductGroupImageNoThumbnailException이 발생한다")
        void throwsWhenNoThumbnail() {
            var detail1 =
                    ProductGroupImage.forNew(
                            ImageType.DETAIL, ImageUrl.of("https://d.com/1.png"), 1);
            var detail2 =
                    ProductGroupImage.forNew(
                            ImageType.DETAIL, ImageUrl.of("https://d.com/2.png"), 2);

            assertThatThrownBy(() -> ProductGroupImages.of(List.of(detail1, detail2)))
                    .isInstanceOf(ProductGroupImageNoThumbnailException.class);
        }

        @Test
        @DisplayName("THUMBNAIL 2개이면 ProductGroupImageNoThumbnailException이 발생한다")
        void throwsWhenTwoThumbnails() {
            var thumb1 =
                    ProductGroupImage.forNew(
                            ImageType.THUMBNAIL, ImageUrl.of("https://t.com/1.png"), 1);
            var thumb2 =
                    ProductGroupImage.forNew(
                            ImageType.THUMBNAIL, ImageUrl.of("https://t.com/2.png"), 2);

            assertThatThrownBy(() -> ProductGroupImages.of(List.of(thumb1, thumb2)))
                    .isInstanceOf(ProductGroupImageNoThumbnailException.class);
        }

        @Test
        @DisplayName("빈 목록이면 예외가 발생한다")
        void throwsWhenEmpty() {
            assertThatThrownBy(() -> ProductGroupImages.of(List.of()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("이미지 목록은 비어있을 수 없습니다");
        }

        @Test
        @DisplayName("null이면 예외가 발생한다")
        void throwsWhenNull() {
            assertThatThrownBy(() -> ProductGroupImages.of(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("이미지 목록은 비어있을 수 없습니다");
        }

        @Test
        @DisplayName("THUMBNAIL이 sortOrder 0으로, DETAIL은 1부터 re-index된다")
        void sortsThumbnailFirstAndReindexes() {
            var detail =
                    ProductGroupImage.forNew(
                            ImageType.DETAIL, ImageUrl.of("https://d.com/1.png"), 3);
            var thumbnail =
                    ProductGroupImage.forNew(
                            ImageType.THUMBNAIL, ImageUrl.of("https://t.com/1.png"), 5);

            var images = ProductGroupImages.of(List.of(detail, thumbnail));

            // THUMBNAIL은 sortOrder 0, DETAIL은 sortOrder 1
            assertThat(images.toList().get(0).isThumbnail()).isTrue();
            assertThat(images.toList().get(0).sortOrder()).isZero();
            assertThat(images.toList().get(1).isThumbnail()).isFalse();
            assertThat(images.toList().get(1).sortOrder()).isEqualTo(1);
        }

        @Test
        @DisplayName("thumbnail()은 첫 번째 이미지를 반환한다")
        void thumbnailReturnsFirst() {
            var thumbnail =
                    ProductGroupImage.forNew(
                            ImageType.THUMBNAIL, ImageUrl.of("https://t.com/1.png"), 0);
            var detail =
                    ProductGroupImage.forNew(
                            ImageType.DETAIL, ImageUrl.of("https://d.com/1.png"), 1);

            var images = ProductGroupImages.of(List.of(detail, thumbnail));

            assertThat(images.thumbnail().isThumbnail()).isTrue();
        }

        @Test
        @DisplayName("detailImages()는 DETAIL 이미지만 반환한다")
        void detailImagesExcludesThumbnail() {
            var thumbnail =
                    ProductGroupImage.forNew(
                            ImageType.THUMBNAIL, ImageUrl.of("https://t.com/1.png"), 0);
            var detail1 =
                    ProductGroupImage.forNew(
                            ImageType.DETAIL, ImageUrl.of("https://d.com/1.png"), 1);
            var detail2 =
                    ProductGroupImage.forNew(
                            ImageType.DETAIL, ImageUrl.of("https://d.com/2.png"), 2);

            var images = ProductGroupImages.of(List.of(thumbnail, detail1, detail2));

            assertThat(images.detailImages()).hasSize(2);
            assertThat(images.detailImages()).allMatch(img -> !img.isThumbnail());
        }
    }

    @Nested
    @DisplayName("reconstitute() - 영속성 복원")
    class ReconstituteTest {

        @Test
        @DisplayName("validation 없이 복원된다")
        void reconstituteSkipsValidation() {
            var detail1 =
                    ProductGroupImage.reconstitute(
                            ProductGroupImageId.of(1L),
                            ImageType.DETAIL,
                            ImageUrl.of("https://d.com/1.png"),
                            1);

            // THUMBNAIL이 없어도 예외 없음
            var images = ProductGroupImages.reconstitute(List.of(detail1));
            assertThat(images.toList()).hasSize(1);
        }

        @Test
        @DisplayName("빈 목록으로 복원된다")
        void reconstituteEmpty() {
            var images = ProductGroupImages.reconstitute(List.of());
            assertThat(images.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("null로 복원하면 빈 목록이다")
        void reconstituteNull() {
            var images = ProductGroupImages.reconstitute(null);
            assertThat(images.isEmpty()).isTrue();
        }
    }

    @Nested
    @DisplayName("update() - diff 계산")
    class UpdateTest {

        @Test
        @DisplayName("동일 이미지 목록이면 변경 없음")
        void noChangesWhenSameImages() {
            // given
            var existing =
                    ProductGroupImages.reconstitute(
                            List.of(
                                    ProductGroupImage.reconstitute(
                                            ProductGroupImageId.of(1L),
                                            ImageType.THUMBNAIL,
                                            ImageUrl.of("https://t.com/1.png"),
                                            0),
                                    ProductGroupImage.reconstitute(
                                            ProductGroupImageId.of(2L),
                                            ImageType.DETAIL,
                                            ImageUrl.of("https://d.com/1.png"),
                                            1)));

            var newImages =
                    ProductGroupImages.of(
                            List.of(
                                    ProductGroupImage.forNew(
                                            ImageType.THUMBNAIL,
                                            ImageUrl.of("https://t.com/1.png"),
                                            1),
                                    ProductGroupImage.forNew(
                                            ImageType.DETAIL,
                                            ImageUrl.of("https://d.com/1.png"),
                                            2)));

            var updateData = ProductGroupImageUpdateData.of(newImages, Instant.now());

            // when
            var diff = existing.update(updateData);

            // then
            assertThat(diff.hasNoChanges()).isTrue();
            assertThat(diff.added()).isEmpty();
            assertThat(diff.removed()).isEmpty();
            assertThat(diff.retained()).hasSize(2);
        }

        @Test
        @DisplayName("새 이미지가 추가되면 added에 포함된다")
        void detectsAddedImages() {
            // given
            var existing =
                    ProductGroupImages.reconstitute(
                            List.of(
                                    ProductGroupImage.reconstitute(
                                            ProductGroupImageId.of(1L),
                                            ImageType.THUMBNAIL,
                                            ImageUrl.of("https://t.com/1.png"),
                                            0)));

            var newImages =
                    ProductGroupImages.of(
                            List.of(
                                    ProductGroupImage.forNew(
                                            ImageType.THUMBNAIL,
                                            ImageUrl.of("https://t.com/1.png"),
                                            1),
                                    ProductGroupImage.forNew(
                                            ImageType.DETAIL,
                                            ImageUrl.of("https://d.com/new.png"),
                                            2)));

            var updateData = ProductGroupImageUpdateData.of(newImages, Instant.now());

            // when
            var diff = existing.update(updateData);

            // then
            assertThat(diff.added()).hasSize(1);
            assertThat(diff.removed()).isEmpty();
            assertThat(diff.retained()).hasSize(1);
        }

        @Test
        @DisplayName("기존 이미지가 삭제되면 removed에 포함된다")
        void detectsRemovedImages() {
            // given
            var existing =
                    ProductGroupImages.reconstitute(
                            List.of(
                                    ProductGroupImage.reconstitute(
                                            ProductGroupImageId.of(1L),
                                            ImageType.THUMBNAIL,
                                            ImageUrl.of("https://t.com/1.png"),
                                            0),
                                    ProductGroupImage.reconstitute(
                                            ProductGroupImageId.of(2L),
                                            ImageType.DETAIL,
                                            ImageUrl.of("https://d.com/old.png"),
                                            1)));

            var newImages =
                    ProductGroupImages.of(
                            List.of(
                                    ProductGroupImage.forNew(
                                            ImageType.THUMBNAIL,
                                            ImageUrl.of("https://t.com/1.png"),
                                            1)));

            Instant now = Instant.now();
            var updateData = ProductGroupImageUpdateData.of(newImages, now);

            // when
            var diff = existing.update(updateData);

            // then
            assertThat(diff.added()).isEmpty();
            assertThat(diff.removed()).hasSize(1);
            assertThat(diff.removed().get(0).isDeleted()).isTrue();
            assertThat(diff.removed().get(0).deletedAt()).isEqualTo(now);
            assertThat(diff.occurredAt()).isEqualTo(now);
            assertThat(diff.retained()).hasSize(1);
        }

        @Test
        @DisplayName("sortOrder가 변경되면 retained에 반영된다")
        void detectsSortOrderChange() {
            // given
            var existing =
                    ProductGroupImages.reconstitute(
                            List.of(
                                    ProductGroupImage.reconstitute(
                                            ProductGroupImageId.of(1L),
                                            ImageType.THUMBNAIL,
                                            ImageUrl.of("https://t.com/1.png"),
                                            0)));

            var newImages =
                    ProductGroupImages.of(
                            List.of(
                                    ProductGroupImage.forNew(
                                            ImageType.THUMBNAIL,
                                            ImageUrl.of("https://t.com/1.png"),
                                            5)));

            var updateData = ProductGroupImageUpdateData.of(newImages, Instant.now());

            // when
            var diff = existing.update(updateData);

            // then
            assertThat(diff.hasNoChanges()).isTrue();
            assertThat(diff.retained()).hasSize(1);
            // of()에서 re-index 되므로 sortOrder는 0 (THUMBNAIL)
            assertThat(diff.retained().get(0).sortOrder()).isZero();
            assertThat(diff.retained().get(0).idValue()).isEqualTo(1L);
        }

        @Test
        @DisplayName("추가와 삭제가 동시에 발생한다")
        void detectsAddedAndRemoved() {
            // given
            var existing =
                    ProductGroupImages.reconstitute(
                            List.of(
                                    ProductGroupImage.reconstitute(
                                            ProductGroupImageId.of(1L),
                                            ImageType.THUMBNAIL,
                                            ImageUrl.of("https://t.com/old.png"),
                                            0),
                                    ProductGroupImage.reconstitute(
                                            ProductGroupImageId.of(2L),
                                            ImageType.DETAIL,
                                            ImageUrl.of("https://d.com/keep.png"),
                                            1)));

            var newImages =
                    ProductGroupImages.of(
                            List.of(
                                    ProductGroupImage.forNew(
                                            ImageType.THUMBNAIL,
                                            ImageUrl.of("https://t.com/new.png"),
                                            1),
                                    ProductGroupImage.forNew(
                                            ImageType.DETAIL,
                                            ImageUrl.of("https://d.com/keep.png"),
                                            2)));

            var updateData = ProductGroupImageUpdateData.of(newImages, Instant.now());

            // when
            var diff = existing.update(updateData);

            // then
            assertThat(diff.added()).hasSize(1);
            assertThat(diff.added().get(0).imageUrlValue()).isEqualTo("https://t.com/new.png");
            assertThat(diff.removed()).hasSize(1);
            assertThat(diff.removed().get(0).imageUrlValue()).isEqualTo("https://t.com/old.png");
            assertThat(diff.retained()).hasSize(1);
            assertThat(diff.retained().get(0).imageUrlValue()).isEqualTo("https://d.com/keep.png");
        }
    }
}
