package com.ryuqq.setof.domain.productdescription.vo;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.productdescription.aggregate.DescriptionImage;
import com.setof.commerce.domain.productdescription.ProductDescriptionFixtures;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("DescriptionUpdateData VO 테스트")
class DescriptionUpdateDataTest {

    @Nested
    @DisplayName("of() - 생성")
    class OfTest {

        @Test
        @DisplayName("유효한 값으로 DescriptionUpdateData를 생성한다")
        void createWithValidValues() {
            // given
            DescriptionHtml content = ProductDescriptionFixtures.defaultDescriptionHtml();
            String cdnPath = ProductDescriptionFixtures.DEFAULT_CDN_PATH;
            List<DescriptionImage> images = ProductDescriptionFixtures.defaultImages();
            Instant updatedAt = CommonVoFixtures.now();

            // when
            DescriptionUpdateData updateData =
                    DescriptionUpdateData.of(content, cdnPath, images, updatedAt);

            // then
            assertThat(updateData.content()).isEqualTo(content);
            assertThat(updateData.cdnPath()).isEqualTo(cdnPath);
            assertThat(updateData.newImages()).hasSize(3);
            assertThat(updateData.updatedAt()).isEqualTo(updatedAt);
        }

        @Test
        @DisplayName("빈 이미지 목록으로 생성한다")
        void createWithEmptyImages() {
            // given
            DescriptionHtml content = ProductDescriptionFixtures.defaultDescriptionHtml();
            Instant updatedAt = CommonVoFixtures.now();

            // when
            DescriptionUpdateData updateData =
                    DescriptionUpdateData.of(content, null, List.of(), updatedAt);

            // then
            assertThat(updateData.newImages()).isEmpty();
            assertThat(updateData.cdnPath()).isNull();
        }

        @Test
        @DisplayName("null CDN 경로로 생성한다")
        void createWithNullCdnPath() {
            // given
            DescriptionHtml content = ProductDescriptionFixtures.defaultDescriptionHtml();
            Instant updatedAt = CommonVoFixtures.now();

            // when
            DescriptionUpdateData updateData =
                    DescriptionUpdateData.of(content, null, List.of(), updatedAt);

            // then
            assertThat(updateData.cdnPath()).isNull();
        }
    }

    @Nested
    @DisplayName("Accessor 메서드 테스트")
    class AccessorTest {

        @Test
        @DisplayName("content()는 DescriptionHtml을 반환한다")
        void returnsContent() {
            // given
            DescriptionUpdateData updateData =
                    ProductDescriptionFixtures.defaultDescriptionUpdateData();

            // then
            assertThat(updateData.content()).isNotNull();
            assertThat(updateData.content().value())
                    .isEqualTo(ProductDescriptionFixtures.DEFAULT_DESCRIPTION_HTML);
        }

        @Test
        @DisplayName("cdnPath()는 CDN 경로를 반환한다")
        void returnsCdnPath() {
            // given
            DescriptionUpdateData updateData =
                    ProductDescriptionFixtures.defaultDescriptionUpdateData();

            // then
            assertThat(updateData.cdnPath()).isEqualTo(ProductDescriptionFixtures.DEFAULT_CDN_PATH);
        }

        @Test
        @DisplayName("newImages()는 이미지 목록을 반환한다")
        void returnsNewImages() {
            // given
            DescriptionUpdateData updateData =
                    ProductDescriptionFixtures.defaultDescriptionUpdateData();

            // then
            assertThat(updateData.newImages()).isNotNull();
            assertThat(updateData.newImages()).hasSize(3);
        }

        @Test
        @DisplayName("updatedAt()은 수정 시각을 반환한다")
        void returnsUpdatedAt() {
            // given
            Instant expectedTime = CommonVoFixtures.now();
            DescriptionUpdateData updateData =
                    DescriptionUpdateData.of(
                            ProductDescriptionFixtures.defaultDescriptionHtml(),
                            ProductDescriptionFixtures.DEFAULT_CDN_PATH,
                            List.of(),
                            expectedTime);

            // then
            assertThat(updateData.updatedAt()).isEqualTo(expectedTime);
        }
    }

    @Nested
    @DisplayName("불변성 테스트")
    class ImmutabilityTest {

        @Test
        @DisplayName("생성 후 외부에서 이미지 목록을 수정해도 내부 상태가 변경되지 않는다")
        void imageListIsImmutableAfterCreation() {
            // given
            List<DescriptionImage> mutableImages =
                    new ArrayList<>(ProductDescriptionFixtures.defaultImages());
            DescriptionUpdateData updateData =
                    DescriptionUpdateData.of(
                            ProductDescriptionFixtures.defaultDescriptionHtml(),
                            ProductDescriptionFixtures.DEFAULT_CDN_PATH,
                            mutableImages,
                            CommonVoFixtures.now());
            int originalSize = updateData.newImages().size();

            // when
            mutableImages.add(ProductDescriptionFixtures.descriptionImage());

            // then
            assertThat(updateData.newImages()).hasSize(originalSize);
        }

        @Test
        @DisplayName("newImages()가 반환하는 리스트는 수정할 수 없다")
        void returnedImageListIsUnmodifiable() {
            // given
            DescriptionUpdateData updateData =
                    ProductDescriptionFixtures.defaultDescriptionUpdateData();

            // when & then
            org.junit.jupiter.api.Assertions.assertThrows(
                    UnsupportedOperationException.class,
                    () ->
                            updateData
                                    .newImages()
                                    .add(ProductDescriptionFixtures.descriptionImage()));
        }
    }
}
