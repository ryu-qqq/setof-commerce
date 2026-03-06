package com.ryuqq.setof.domain.productgroupimage.vo;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.productgroup.vo.ImageType;
import com.ryuqq.setof.domain.productgroup.vo.ImageUrl;
import com.ryuqq.setof.domain.productgroupimage.aggregate.ProductGroupImage;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductGroupImageUpdateData VO 테스트")
class ProductGroupImageUpdateDataTest {

    @Nested
    @DisplayName("of() - 팩토리 메서드")
    class OfTest {

        @Test
        @DisplayName("이미지 목록과 수정 시각으로 수정 데이터를 생성한다")
        void createWithImagesAndUpdatedAt() {
            // given
            ProductGroupImages newImages =
                    ProductGroupImages.of(
                            List.of(
                                    ProductGroupImage.forNew(
                                            ImageType.THUMBNAIL,
                                            ImageUrl.of("https://example.com/thumb.png"),
                                            0)));
            Instant updatedAt = Instant.now();

            // when
            ProductGroupImageUpdateData updateData =
                    ProductGroupImageUpdateData.of(newImages, updatedAt);

            // then
            assertThat(updateData).isNotNull();
            assertThat(updateData.newImages()).isEqualTo(newImages);
            assertThat(updateData.updatedAt()).isEqualTo(updatedAt);
        }
    }

    @Nested
    @DisplayName("newImages() - 이미지 목록 접근")
    class NewImagesAccessorTest {

        @Test
        @DisplayName("설정한 이미지 목록을 반환한다")
        void returnsNewImages() {
            // given
            ProductGroupImages newImages =
                    ProductGroupImages.of(
                            List.of(
                                    ProductGroupImage.forNew(
                                            ImageType.THUMBNAIL,
                                            ImageUrl.of("https://example.com/thumb.png"),
                                            0),
                                    ProductGroupImage.forNew(
                                            ImageType.DETAIL,
                                            ImageUrl.of("https://example.com/detail.png"),
                                            1)));
            Instant updatedAt = Instant.now();
            ProductGroupImageUpdateData updateData =
                    ProductGroupImageUpdateData.of(newImages, updatedAt);

            // when & then
            assertThat(updateData.newImages().toList()).hasSize(2);
            assertThat(updateData.newImages().toList().get(0).isThumbnail()).isTrue();
        }
    }

    @Nested
    @DisplayName("updatedAt() - 수정 시각 접근")
    class UpdatedAtAccessorTest {

        @Test
        @DisplayName("설정한 수정 시각을 반환한다")
        void returnsUpdatedAt() {
            // given
            ProductGroupImages newImages =
                    ProductGroupImages.of(
                            List.of(
                                    ProductGroupImage.forNew(
                                            ImageType.THUMBNAIL,
                                            ImageUrl.of("https://example.com/thumb.png"),
                                            0)));
            Instant expectedUpdatedAt = Instant.parse("2025-01-01T00:00:00Z");
            ProductGroupImageUpdateData updateData =
                    ProductGroupImageUpdateData.of(newImages, expectedUpdatedAt);

            // when & then
            assertThat(updateData.updatedAt()).isEqualTo(expectedUpdatedAt);
        }
    }

    @Nested
    @DisplayName("불변성 테스트")
    class ImmutabilityTest {

        @Test
        @DisplayName("동일한 인자로 생성된 두 객체는 같은 값을 가진다")
        void sameArgumentsProduceSameValues() {
            // given
            ProductGroupImages newImages =
                    ProductGroupImages.of(
                            List.of(
                                    ProductGroupImage.forNew(
                                            ImageType.THUMBNAIL,
                                            ImageUrl.of("https://example.com/thumb.png"),
                                            0)));
            Instant updatedAt = Instant.parse("2025-06-01T12:00:00Z");

            // when
            ProductGroupImageUpdateData data1 =
                    ProductGroupImageUpdateData.of(newImages, updatedAt);
            ProductGroupImageUpdateData data2 =
                    ProductGroupImageUpdateData.of(newImages, updatedAt);

            // then
            assertThat(data1.updatedAt()).isEqualTo(data2.updatedAt());
            assertThat(data1.newImages()).isEqualTo(data2.newImages());
        }
    }
}
