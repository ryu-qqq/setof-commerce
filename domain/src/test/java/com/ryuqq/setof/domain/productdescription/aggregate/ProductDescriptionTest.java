package com.ryuqq.setof.domain.productdescription.aggregate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ryuqq.setof.domain.product.vo.ProductGroupId;
import com.ryuqq.setof.domain.productdescription.ProductDescriptionFixture;
import com.ryuqq.setof.domain.productdescription.vo.DescriptionImage;
import com.ryuqq.setof.domain.productdescription.vo.HtmlContent;
import com.ryuqq.setof.domain.productdescription.vo.ImageUrl;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("ProductDescription Aggregate 테스트")
class ProductDescriptionTest {

    private static final Instant NOW = Instant.parse("2024-01-01T00:00:00Z");
    private static final Instant UPDATED = Instant.parse("2024-01-02T00:00:00Z");

    @Nested
    @DisplayName("create 메서드는")
    class CreateMethod {

        @Test
        @DisplayName("유효한 값으로 ProductDescription을 생성한다")
        void shouldCreateProductDescription() {
            // given
            ProductGroupId productGroupId = ProductGroupId.of(100L);
            HtmlContent htmlContent = HtmlContent.of("<p>테스트</p>");
            List<DescriptionImage> images = ProductDescriptionFixture.createDescriptionImages(3);

            // when
            ProductDescription description =
                    ProductDescription.create(productGroupId, htmlContent, images, NOW);

            // then
            assertThat(description.getId().isNew()).isTrue();
            assertThat(description.getProductGroupIdValue()).isEqualTo(100L);
            assertThat(description.getHtmlContentValue()).isEqualTo("<p>테스트</p>");
            assertThat(description.getImageCount()).isEqualTo(3);
            assertThat(description.getCreatedAt()).isEqualTo(NOW);
            assertThat(description.getUpdatedAt()).isEqualTo(NOW);
        }

        @Test
        @DisplayName("이미지 없이도 생성 가능하다")
        void shouldCreateWithoutImages() {
            // given
            ProductGroupId productGroupId = ProductGroupId.of(100L);
            HtmlContent htmlContent = HtmlContent.of("<p>테스트</p>");

            // when
            ProductDescription description =
                    ProductDescription.create(productGroupId, htmlContent, List.of(), NOW);

            // then
            assertThat(description.hasImages()).isFalse();
            assertThat(description.getImageCount()).isZero();
        }

        @Test
        @DisplayName("ProductGroupId가 null이면 예외를 발생시킨다")
        void shouldThrowWhenProductGroupIdIsNull() {
            // given
            HtmlContent htmlContent = HtmlContent.of("<p>테스트</p>");
            List<DescriptionImage> images = List.of();

            // when & then
            assertThatThrownBy(() -> ProductDescription.create(null, htmlContent, images, NOW))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("ProductGroupId is required");
        }

        @Test
        @DisplayName("이미지를 displayOrder 순으로 정렬한다")
        void shouldSortImagesByDisplayOrder() {
            // given
            ProductGroupId productGroupId = ProductGroupId.of(100L);
            HtmlContent htmlContent = HtmlContent.of("<p>테스트</p>");
            List<DescriptionImage> images =
                    List.of(
                            ProductDescriptionFixture.createDescriptionImage(3),
                            ProductDescriptionFixture.createDescriptionImage(1),
                            ProductDescriptionFixture.createDescriptionImage(2));

            // when
            ProductDescription description =
                    ProductDescription.create(productGroupId, htmlContent, images, NOW);

            // then
            List<DescriptionImage> sortedImages = description.getImages();
            assertThat(sortedImages.get(0).displayOrder()).isEqualTo(1);
            assertThat(sortedImages.get(1).displayOrder()).isEqualTo(2);
            assertThat(sortedImages.get(2).displayOrder()).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("reconstitute 메서드는")
    class ReconstituteMethod {

        @Test
        @DisplayName("모든 필드를 복원한다")
        void shouldReconstituteAllFields() {
            // given & when
            ProductDescription description = ProductDescriptionFixture.reconstitute(1L, 100L);

            // then
            assertThat(description.getIdValue()).isEqualTo(1L);
            assertThat(description.getProductGroupIdValue()).isEqualTo(100L);
            assertThat(description.hasId()).isTrue();
        }
    }

    @Nested
    @DisplayName("updateHtmlContent 메서드는")
    class UpdateHtmlContentMethod {

        @Test
        @DisplayName("HTML 컨텐츠를 업데이트한다")
        void shouldUpdateHtmlContent() {
            // given
            ProductDescription description = ProductDescriptionFixture.reconstitute();
            HtmlContent newContent = HtmlContent.of("<p>새로운 컨텐츠</p>");

            // when
            ProductDescription updated = description.updateHtmlContent(newContent, UPDATED);

            // then
            assertThat(updated.getHtmlContentValue()).isEqualTo("<p>새로운 컨텐츠</p>");
            assertThat(updated.getUpdatedAt()).isEqualTo(UPDATED);
            assertThat(updated.getIdValue()).isEqualTo(description.getIdValue());
        }
    }

    @Nested
    @DisplayName("updateImages 메서드는")
    class UpdateImagesMethod {

        @Test
        @DisplayName("이미지 목록을 업데이트한다")
        void shouldUpdateImages() {
            // given
            ProductDescription description = ProductDescriptionFixture.reconstitute();
            List<DescriptionImage> newImages = ProductDescriptionFixture.createDescriptionImages(5);

            // when
            ProductDescription updated = description.updateImages(newImages, UPDATED);

            // then
            assertThat(updated.getImageCount()).isEqualTo(5);
            assertThat(updated.getUpdatedAt()).isEqualTo(UPDATED);
        }
    }

    @Nested
    @DisplayName("addImage 메서드는")
    class AddImageMethod {

        @Test
        @DisplayName("이미지를 추가한다")
        void shouldAddImage() {
            // given
            ProductDescription description =
                    ProductDescriptionFixture.builder().imageCount(3).build();
            DescriptionImage newImage = ProductDescriptionFixture.createDescriptionImage(4);

            // when
            ProductDescription updated = description.addImage(newImage, UPDATED);

            // then
            assertThat(updated.getImageCount()).isEqualTo(4);
        }
    }

    @Nested
    @DisplayName("updateImageCdnUrl 메서드는")
    class UpdateImageCdnUrlMethod {

        @Test
        @DisplayName("이미지의 CDN URL을 업데이트하고 HTML 내 URL도 치환한다")
        void shouldUpdateCdnUrlAndReplaceInHtml() {
            // given
            String originUrl = "https://origin.example.com/test.jpg";
            String cdnUrl = "https://cdn.example.com/test.jpg";
            String htmlWithOrigin = "<img src=\"" + originUrl + "\"/>";

            ProductDescription description =
                    ProductDescriptionFixture.builder()
                            .htmlContent(htmlWithOrigin)
                            .images(
                                    List.of(
                                            DescriptionImage.of(
                                                    1,
                                                    ImageUrl.of(originUrl),
                                                    ImageUrl.of(originUrl),
                                                    NOW)))
                            .build();

            // when
            ProductDescription updated =
                    description.updateImageCdnUrl(
                            ImageUrl.of(originUrl), ImageUrl.of(cdnUrl), UPDATED);

            // then
            assertThat(updated.getHtmlContentValue()).contains(cdnUrl);
            assertThat(updated.getHtmlContentValue()).doesNotContain(originUrl);
            assertThat(updated.getImages().get(0).getCdnUrlValue()).isEqualTo(cdnUrl);
        }
    }

    @Nested
    @DisplayName("상태 확인 메서드는")
    class StateCheckMethods {

        @Test
        @DisplayName("hasContent는 HTML 또는 이미지가 있으면 true를 반환한다")
        void hasContentShouldReturnTrue() {
            // given
            ProductDescription withHtml =
                    ProductDescriptionFixture.builder()
                            .htmlContent("<p>내용</p>")
                            .images(List.of())
                            .build();

            ProductDescription withImages =
                    ProductDescriptionFixture.builder().htmlContent("").imageCount(1).build();

            // then
            assertThat(withHtml.hasContent()).isTrue();
            assertThat(withImages.hasContent()).isTrue();
        }

        @Test
        @DisplayName("allImagesCdnConverted는 모든 이미지가 CDN 변환되었을 때 true를 반환한다")
        void allImagesCdnConvertedShouldReturnTrue() {
            // given
            ProductDescription description =
                    ProductDescriptionFixture.builder()
                            .images(
                                    List.of(
                                            DescriptionImage.of(
                                                    1,
                                                    ImageUrl.of("https://origin.com/1.jpg"),
                                                    ImageUrl.of("https://cdn.com/1.jpg"),
                                                    NOW),
                                            DescriptionImage.of(
                                                    2,
                                                    ImageUrl.of("https://origin.com/2.jpg"),
                                                    ImageUrl.of("https://cdn.com/2.jpg"),
                                                    NOW)))
                            .build();

            // then
            assertThat(description.allImagesCdnConverted()).isTrue();
        }

        @Test
        @DisplayName("allImagesCdnConverted는 변환되지 않은 이미지가 있으면 false를 반환한다")
        void allImagesCdnConvertedShouldReturnFalse() {
            // given
            ProductDescription description =
                    ProductDescriptionFixture.builder()
                            .images(
                                    List.of(
                                            DescriptionImage.withSameUrl(
                                                    1,
                                                    ImageUrl.of("https://origin.com/1.jpg"),
                                                    NOW)))
                            .build();

            // then
            assertThat(description.allImagesCdnConverted()).isFalse();
        }
    }
}
