package com.ryuqq.setof.domain.productimage.aggregate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ryuqq.setof.domain.product.vo.ProductGroupId;
import com.ryuqq.setof.domain.productimage.ProductImageFixture;
import com.ryuqq.setof.domain.productimage.vo.ImageType;
import com.ryuqq.setof.domain.productimage.vo.ImageUrl;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@DisplayName("ProductImage Aggregate 테스트")
class ProductImageTest {

    private static final Instant NOW = Instant.parse("2024-01-01T00:00:00Z");

    @Nested
    @DisplayName("create 메서드는")
    class CreateMethod {

        @Test
        @DisplayName("유효한 값으로 ProductImage를 생성한다")
        void shouldCreateProductImage() {
            // given
            ProductGroupId productGroupId = ProductGroupId.of(100L);
            ImageType imageType = ImageType.MAIN;
            ImageUrl originUrl = ImageUrl.of("https://origin.example.com/image.jpg");
            ImageUrl cdnUrl = ImageUrl.of("https://cdn.example.com/image.jpg");

            // when
            ProductImage image =
                    ProductImage.create(productGroupId, imageType, originUrl, cdnUrl, 1, NOW);

            // then
            assertThat(image.getId().isNew()).isTrue();
            assertThat(image.getProductGroupIdValue()).isEqualTo(100L);
            assertThat(image.getImageType()).isEqualTo(ImageType.MAIN);
            assertThat(image.getDisplayOrder()).isEqualTo(1);
            assertThat(image.getCreatedAt()).isEqualTo(NOW);
        }

        @ParameterizedTest
        @EnumSource(ImageType.class)
        @DisplayName("모든 이미지 타입으로 생성할 수 있다")
        void shouldCreateWithAllImageTypes(ImageType imageType) {
            // given
            ProductGroupId productGroupId = ProductGroupId.of(100L);
            ImageUrl originUrl = ImageUrl.of("https://origin.example.com/image.jpg");
            ImageUrl cdnUrl = ImageUrl.of("https://cdn.example.com/image.jpg");

            // when
            ProductImage image =
                    ProductImage.create(productGroupId, imageType, originUrl, cdnUrl, 1, NOW);

            // then
            assertThat(image.getImageType()).isEqualTo(imageType);
        }

        @Test
        @DisplayName("ProductGroupId가 null이면 예외를 발생시킨다")
        void shouldThrowWhenProductGroupIdIsNull() {
            // given
            ImageType imageType = ImageType.MAIN;
            ImageUrl originUrl = ImageUrl.of("https://origin.example.com/image.jpg");
            ImageUrl cdnUrl = ImageUrl.of("https://cdn.example.com/image.jpg");

            // when & then
            assertThatThrownBy(
                            () -> ProductImage.create(null, imageType, originUrl, cdnUrl, 1, NOW))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("ProductGroupId is required");
        }

        @Test
        @DisplayName("displayOrder가 0 이하면 예외를 발생시킨다")
        void shouldThrowWhenDisplayOrderIsInvalid() {
            // given
            ProductGroupId productGroupId = ProductGroupId.of(100L);
            ImageType imageType = ImageType.MAIN;
            ImageUrl originUrl = ImageUrl.of("https://origin.example.com/image.jpg");
            ImageUrl cdnUrl = ImageUrl.of("https://cdn.example.com/image.jpg");

            // when & then
            assertThatThrownBy(
                            () ->
                                    ProductImage.create(
                                            productGroupId, imageType, originUrl, cdnUrl, 0, NOW))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("DisplayOrder must be positive");
        }
    }

    @Nested
    @DisplayName("createWithOriginUrl 메서드는")
    class CreateWithOriginUrlMethod {

        @Test
        @DisplayName("원본 URL로만 생성하면 CDN URL도 동일하게 설정된다")
        void shouldCreateWithSameUrls() {
            // given
            ProductGroupId productGroupId = ProductGroupId.of(100L);
            ImageType imageType = ImageType.MAIN;
            ImageUrl originUrl = ImageUrl.of("https://origin.example.com/image.jpg");

            // when
            ProductImage image =
                    ProductImage.createWithOriginUrl(productGroupId, imageType, originUrl, 1, NOW);

            // then
            assertThat(image.getOriginUrlValue()).isEqualTo(image.getCdnUrlValue());
            assertThat(image.isCdnConverted()).isFalse();
        }
    }

    @Nested
    @DisplayName("reconstitute 메서드는")
    class ReconstituteMethod {

        @Test
        @DisplayName("모든 필드를 복원한다")
        void shouldReconstituteAllFields() {
            // given & when
            ProductImage image = ProductImageFixture.reconstitute(1L);

            // then
            assertThat(image.getIdValue()).isEqualTo(1L);
            assertThat(image.hasId()).isTrue();
        }
    }

    @Nested
    @DisplayName("updateCdnUrl 메서드는")
    class UpdateCdnUrlMethod {

        @Test
        @DisplayName("CDN URL을 업데이트한다")
        void shouldUpdateCdnUrl() {
            // given
            ProductImage image =
                    ProductImageFixture.builder()
                            .originUrl("https://origin.example.com/test.jpg")
                            .cdnUrl("https://origin.example.com/test.jpg")
                            .build();
            ImageUrl newCdnUrl = ImageUrl.of("https://cdn.example.com/test.jpg");

            // when
            ProductImage updated = image.updateCdnUrl(newCdnUrl);

            // then
            assertThat(updated.getCdnUrlValue()).isEqualTo("https://cdn.example.com/test.jpg");
            assertThat(updated.isCdnConverted()).isTrue();
        }

        @Test
        @DisplayName("null CDN URL은 예외를 발생시킨다")
        void shouldThrowWhenCdnUrlIsNull() {
            // given
            ProductImage image = ProductImageFixture.reconstitute();

            // when & then
            assertThatThrownBy(() -> image.updateCdnUrl(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("CDN URL은 필수입니다");
        }
    }

    @Nested
    @DisplayName("changeDisplayOrder 메서드는")
    class ChangeDisplayOrderMethod {

        @Test
        @DisplayName("표시 순서를 변경한다")
        void shouldChangeDisplayOrder() {
            // given
            ProductImage image = ProductImageFixture.builder().displayOrder(1).build();

            // when
            ProductImage updated = image.changeDisplayOrder(5);

            // then
            assertThat(updated.getDisplayOrder()).isEqualTo(5);
        }

        @Test
        @DisplayName("0 이하 순서는 예외를 발생시킨다")
        void shouldThrowWhenOrderIsInvalid() {
            // given
            ProductImage image = ProductImageFixture.reconstitute();

            // when & then
            assertThatThrownBy(() -> image.changeDisplayOrder(0))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("표시 순서는 1 이상");
        }
    }

    @Nested
    @DisplayName("상태 확인 메서드는")
    class StateCheckMethods {

        @Test
        @DisplayName("isMain은 MAIN 타입일 때 true를 반환한다")
        void isMainShouldReturnTrue() {
            // given
            ProductImage image = ProductImageFixture.builder().imageType(ImageType.MAIN).build();

            // then
            assertThat(image.isMain()).isTrue();
            assertThat(image.isSub()).isFalse();
            assertThat(image.isDetail()).isFalse();
        }

        @Test
        @DisplayName("isSub는 SUB 타입일 때 true를 반환한다")
        void isSubShouldReturnTrue() {
            // given
            ProductImage image = ProductImageFixture.builder().imageType(ImageType.SUB).build();

            // then
            assertThat(image.isSub()).isTrue();
            assertThat(image.isMain()).isFalse();
        }

        @Test
        @DisplayName("isDetail은 DETAIL 타입일 때 true를 반환한다")
        void isDetailShouldReturnTrue() {
            // given
            ProductImage image = ProductImageFixture.builder().imageType(ImageType.DETAIL).build();

            // then
            assertThat(image.isDetail()).isTrue();
            assertThat(image.isMain()).isFalse();
        }

        @Test
        @DisplayName("isCdnConverted는 원본과 CDN URL이 다르면 true를 반환한다")
        void isCdnConvertedShouldReturnTrue() {
            // given
            ProductImage converted =
                    ProductImageFixture.builder()
                            .originUrl("https://origin.com/image.jpg")
                            .cdnUrl("https://cdn.com/image.jpg")
                            .build();

            ProductImage notConverted =
                    ProductImageFixture.builder()
                            .originUrl("https://origin.com/image.jpg")
                            .cdnUrl("https://origin.com/image.jpg")
                            .build();

            // then
            assertThat(converted.isCdnConverted()).isTrue();
            assertThat(notConverted.isCdnConverted()).isFalse();
        }
    }
}
