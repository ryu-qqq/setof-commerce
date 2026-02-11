package com.ryuqq.setof.domain.productdescription.aggregate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ryuqq.setof.domain.productdescription.id.DescriptionImageId;
import com.setof.commerce.domain.productdescription.ProductDescriptionFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@Tag("unit")
@DisplayName("DescriptionImage Child Entity 테스트")
class DescriptionImageTest {

    @Nested
    @DisplayName("forNew() - 신규 이미지 생성")
    class ForNewTest {

        @Test
        @DisplayName("유효한 URL과 정렬 순서로 이미지를 생성한다")
        void createValidImage() {
            // when
            var image = DescriptionImage.forNew("https://cdn.example.com/image.jpg", 0);

            // then
            assertThat(image.imageUrl()).isEqualTo("https://cdn.example.com/image.jpg");
            assertThat(image.sortOrder()).isZero();
            assertThat(image.isNew()).isTrue();
        }

        @Test
        @DisplayName("URL 앞뒤 공백이 트림된다")
        void trimImageUrl() {
            // when
            var image = DescriptionImage.forNew("  https://cdn.example.com/image.jpg  ", 1);

            // then
            assertThat(image.imageUrl()).isEqualTo("https://cdn.example.com/image.jpg");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("null 또는 빈 URL이면 예외가 발생한다")
        void throwExceptionForNullOrEmptyUrl(String url) {
            // when & then
            assertThatThrownBy(() -> DescriptionImage.forNew(url, 0))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("이미지 URL");
        }

        @ParameterizedTest
        @ValueSource(strings = {"   ", "\t", "\n"})
        @DisplayName("공백 문자열 URL이면 예외가 발생한다")
        void throwExceptionForBlankUrl(String url) {
            // when & then
            assertThatThrownBy(() -> DescriptionImage.forNew(url, 0))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("이미지 URL");
        }

        @Test
        @DisplayName("다양한 정렬 순서를 지정할 수 있다")
        void createWithVariousSortOrders() {
            // when
            var image0 = DescriptionImage.forNew(ProductDescriptionFixtures.DEFAULT_IMAGE_URL, 0);
            var image5 = DescriptionImage.forNew(ProductDescriptionFixtures.DEFAULT_IMAGE_URL, 5);
            var image99 = DescriptionImage.forNew(ProductDescriptionFixtures.DEFAULT_IMAGE_URL, 99);

            // then
            assertThat(image0.sortOrder()).isZero();
            assertThat(image5.sortOrder()).isEqualTo(5);
            assertThat(image99.sortOrder()).isEqualTo(99);
        }
    }

    @Nested
    @DisplayName("reconstitute() - 영속성 복원")
    class ReconstituteTest {

        @Test
        @DisplayName("영속성에서 이미지를 복원한다")
        void reconstituteImage() {
            // given
            DescriptionImageId id = DescriptionImageId.of(10L);

            // when
            var image = DescriptionImage.reconstitute(id, "https://cdn.example.com/image.jpg", 3);

            // then
            assertThat(image.idValue()).isEqualTo(10L);
            assertThat(image.imageUrl()).isEqualTo("https://cdn.example.com/image.jpg");
            assertThat(image.sortOrder()).isEqualTo(3);
            assertThat(image.isNew()).isFalse();
        }
    }

    @Nested
    @DisplayName("isNew() - 신규 여부 확인")
    class IsNewTest {

        @Test
        @DisplayName("forNew()로 생성한 이미지는 isNew가 true이다")
        void forNewImageIsNew() {
            // when
            var image = ProductDescriptionFixtures.descriptionImage();

            // then
            assertThat(image.isNew()).isTrue();
        }

        @Test
        @DisplayName("reconstitute()로 복원한 이미지는 isNew가 false이다")
        void reconstitutedImageIsNotNew() {
            // when
            var image =
                    DescriptionImage.reconstitute(
                            DescriptionImageId.of(1L), "https://cdn.example.com/image.jpg", 0);

            // then
            assertThat(image.isNew()).isFalse();
        }
    }

    @Nested
    @DisplayName("Accessor 메서드 테스트")
    class AccessorTest {

        @Test
        @DisplayName("id()는 DescriptionImageId를 반환한다")
        void returnsId() {
            // given
            var image =
                    DescriptionImage.reconstitute(
                            DescriptionImageId.of(5L), "https://cdn.example.com/image.jpg", 2);

            // then
            assertThat(image.id()).isNotNull();
            assertThat(image.idValue()).isEqualTo(5L);
        }

        @Test
        @DisplayName("imageUrl()은 이미지 URL을 반환한다")
        void returnsImageUrl() {
            // given
            var image = ProductDescriptionFixtures.descriptionImage();

            // then
            assertThat(image.imageUrl()).isEqualTo(ProductDescriptionFixtures.DEFAULT_IMAGE_URL);
        }

        @Test
        @DisplayName("sortOrder()는 정렬 순서를 반환한다")
        void returnsSortOrder() {
            // given
            var image = ProductDescriptionFixtures.descriptionImage(7);

            // then
            assertThat(image.sortOrder()).isEqualTo(7);
        }
    }
}
