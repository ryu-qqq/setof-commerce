package com.ryuqq.setof.domain.productgroupimage.aggregate;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.productgroup.ProductGroupFixtures;
import com.ryuqq.setof.domain.productgroup.vo.ImageType;
import com.ryuqq.setof.domain.productgroup.vo.ImageUrl;
import com.ryuqq.setof.domain.productgroupimage.id.ProductGroupImageId;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductGroupImage 엔티티 테스트")
class ProductGroupImageTest {

    @Nested
    @DisplayName("forNew() - 신규 이미지 생성")
    class ForNewTest {

        @Test
        @DisplayName("신규 이미지를 생성한다")
        void createNewImage() {
            // given
            ImageType imageType = ImageType.THUMBNAIL;
            ImageUrl imageUrl = ImageUrl.of("https://example.com/new-image.png");
            int sortOrder = 1;

            // when
            var image = ProductGroupImage.forNew(imageType, imageUrl, sortOrder);

            // then
            assertThat(image.isNew()).isTrue();
            assertThat(image.imageType()).isEqualTo(ImageType.THUMBNAIL);
            assertThat(image.imageUrlValue()).isEqualTo("https://example.com/new-image.png");
            assertThat(image.sortOrder()).isEqualTo(1);
        }

        @Test
        @DisplayName("신규 생성된 이미지의 ID는 null이다")
        void newImageIdIsNull() {
            // when
            var image =
                    ProductGroupImage.forNew(
                            ImageType.DETAIL, ImageUrl.of("https://example.com/detail.png"), 2);

            // then
            assertThat(image.isNew()).isTrue();
            assertThat(image.idValue()).isNull();
        }
    }

    @Nested
    @DisplayName("reconstitute() - 영속성 복원")
    class ReconstituteTest {

        @Test
        @DisplayName("이미지를 영속성에서 복원한다")
        void reconstituteImage() {
            // given
            ProductGroupImageId id = ProductGroupImageId.of(10L);
            ImageType imageType = ImageType.THUMBNAIL;
            ImageUrl imageUrl = ImageUrl.of("https://example.com/restored.png");
            int sortOrder = 1;

            // when
            var image = ProductGroupImage.reconstitute(id, imageType, imageUrl, sortOrder);

            // then
            assertThat(image.isNew()).isFalse();
            assertThat(image.idValue()).isEqualTo(10L);
            assertThat(image.imageType()).isEqualTo(ImageType.THUMBNAIL);
            assertThat(image.imageUrlValue()).isEqualTo("https://example.com/restored.png");
            assertThat(image.sortOrder()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("isThumbnail() - 썸네일 여부 확인")
    class IsThumbnailTest {

        @Test
        @DisplayName("THUMBNAIL 타입이면 true를 반환한다")
        void thumbnailReturnsTrue() {
            // given
            var image = ProductGroupFixtures.thumbnailImage();

            // when & then
            assertThat(image.isThumbnail()).isTrue();
        }

        @Test
        @DisplayName("DETAIL 타입이면 false를 반환한다")
        void detailReturnsFalse() {
            // given
            var image = ProductGroupFixtures.detailImage();

            // when & then
            assertThat(image.isThumbnail()).isFalse();
        }
    }

    @Nested
    @DisplayName("updateSortOrder() - 정렬 순서 변경")
    class UpdateSortOrderTest {

        @Test
        @DisplayName("정렬 순서를 변경한다")
        void updateSortOrder() {
            // given
            var image = ProductGroupFixtures.thumbnailImage();
            assertThat(image.sortOrder()).isEqualTo(1);

            // when
            image.updateSortOrder(5);

            // then
            assertThat(image.sortOrder()).isEqualTo(5);
        }
    }

    @Nested
    @DisplayName("delete() - 소프트 삭제")
    class DeleteTest {

        @Test
        @DisplayName("소프트 삭제하면 deletedAt이 설정된다")
        void deleteImage() {
            // given
            var image = ProductGroupFixtures.thumbnailImage();
            Instant now = Instant.now();
            assertThat(image.isDeleted()).isFalse();
            assertThat(image.deletedAt()).isNull();

            // when
            image.delete(now);

            // then
            assertThat(image.isDeleted()).isTrue();
            assertThat(image.deletedAt()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("reconstitute() with deletedAt - deletedAt 포함 복원")
    class ReconstituteWithDeletedAtTest {

        @Test
        @DisplayName("deletedAt 포함하여 복원한다")
        void reconstituteWithDeletedAt() {
            // given
            Instant deletedAt = Instant.now();

            // when
            var image =
                    ProductGroupImage.reconstitute(
                            ProductGroupImageId.of(10L),
                            ImageType.THUMBNAIL,
                            ImageUrl.of("https://example.com/img.png"),
                            1,
                            deletedAt);

            // then
            assertThat(image.isDeleted()).isTrue();
            assertThat(image.deletedAt()).isEqualTo(deletedAt);
        }

        @Test
        @DisplayName("deletedAt null로 복원하면 활성 상태이다")
        void reconstituteWithNullDeletedAt() {
            // when
            var image =
                    ProductGroupImage.reconstitute(
                            ProductGroupImageId.of(10L),
                            ImageType.THUMBNAIL,
                            ImageUrl.of("https://example.com/img.png"),
                            1,
                            null);

            // then
            assertThat(image.isDeleted()).isFalse();
            assertThat(image.deletedAt()).isNull();
        }
    }

    @Nested
    @DisplayName("Accessor Methods 테스트")
    class AccessorMethodTest {

        @Test
        @DisplayName("id()는 ProductGroupImageId를 반환한다")
        void returnsId() {
            var image = ProductGroupFixtures.thumbnailImage();
            assertThat(image.id()).isNotNull();
            assertThat(image.idValue()).isEqualTo(1L);
        }

        @Test
        @DisplayName("imageType()은 ImageType을 반환한다")
        void returnsImageType() {
            var image = ProductGroupFixtures.thumbnailImage();
            assertThat(image.imageType()).isEqualTo(ImageType.THUMBNAIL);
        }

        @Test
        @DisplayName("imageUrl()은 ImageUrl을 반환한다")
        void returnsImageUrl() {
            var image = ProductGroupFixtures.thumbnailImage();
            assertThat(image.imageUrl()).isNotNull();
            assertThat(image.imageUrlValue()).isEqualTo(ProductGroupFixtures.DEFAULT_IMAGE_URL);
        }

        @Test
        @DisplayName("sortOrder()는 정렬 순서를 반환한다")
        void returnsSortOrder() {
            var image = ProductGroupFixtures.thumbnailImage();
            assertThat(image.sortOrder()).isEqualTo(1);
        }
    }
}
