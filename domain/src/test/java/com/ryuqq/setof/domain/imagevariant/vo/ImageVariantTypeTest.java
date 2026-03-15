package com.ryuqq.setof.domain.imagevariant.vo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ImageVariantType Enum 테스트")
class ImageVariantTypeTest {

    @Nested
    @DisplayName("Enum 값 테스트")
    class EnumValuesTest {

        @Test
        @DisplayName("SMALL_WEBP는 300x300 RESIZE 변환 유형이다")
        void smallWebpHasCorrectProperties() {
            assertThat(ImageVariantType.SMALL_WEBP.width()).isEqualTo(300);
            assertThat(ImageVariantType.SMALL_WEBP.height()).isEqualTo(300);
            assertThat(ImageVariantType.SMALL_WEBP.targetFormat()).isEqualTo("webp");
            assertThat(ImageVariantType.SMALL_WEBP.transformType()).isEqualTo("RESIZE");
            assertThat(ImageVariantType.SMALL_WEBP.quality()).isEqualTo(85);
        }

        @Test
        @DisplayName("MEDIUM_WEBP는 600x600 RESIZE 변환 유형이다")
        void mediumWebpHasCorrectProperties() {
            assertThat(ImageVariantType.MEDIUM_WEBP.width()).isEqualTo(600);
            assertThat(ImageVariantType.MEDIUM_WEBP.height()).isEqualTo(600);
            assertThat(ImageVariantType.MEDIUM_WEBP.transformType()).isEqualTo("RESIZE");
        }

        @Test
        @DisplayName("LARGE_WEBP는 1200x1200 RESIZE 변환 유형이다")
        void largeWebpHasCorrectProperties() {
            assertThat(ImageVariantType.LARGE_WEBP.width()).isEqualTo(1200);
            assertThat(ImageVariantType.LARGE_WEBP.height()).isEqualTo(1200);
            assertThat(ImageVariantType.LARGE_WEBP.transformType()).isEqualTo("RESIZE");
        }

        @Test
        @DisplayName("ORIGINAL_WEBP는 null 크기의 CONVERT 변환 유형이다")
        void originalWebpHasCorrectProperties() {
            assertThat(ImageVariantType.ORIGINAL_WEBP.width()).isNull();
            assertThat(ImageVariantType.ORIGINAL_WEBP.height()).isNull();
            assertThat(ImageVariantType.ORIGINAL_WEBP.targetFormat()).isEqualTo("webp");
            assertThat(ImageVariantType.ORIGINAL_WEBP.transformType()).isEqualTo("CONVERT");
            assertThat(ImageVariantType.ORIGINAL_WEBP.quality()).isEqualTo(90);
        }
    }

    @Nested
    @DisplayName("isResize() 테스트")
    class IsResizeTest {

        @Test
        @DisplayName("SMALL_WEBP는 isResize()가 true이다")
        void smallWebpIsResize() {
            assertThat(ImageVariantType.SMALL_WEBP.isResize()).isTrue();
        }

        @Test
        @DisplayName("MEDIUM_WEBP는 isResize()가 true이다")
        void mediumWebpIsResize() {
            assertThat(ImageVariantType.MEDIUM_WEBP.isResize()).isTrue();
        }

        @Test
        @DisplayName("LARGE_WEBP는 isResize()가 true이다")
        void largeWebpIsResize() {
            assertThat(ImageVariantType.LARGE_WEBP.isResize()).isTrue();
        }

        @Test
        @DisplayName("ORIGINAL_WEBP는 isResize()가 false이다")
        void originalWebpIsNotResize() {
            assertThat(ImageVariantType.ORIGINAL_WEBP.isResize()).isFalse();
        }
    }

    @Nested
    @DisplayName("isOriginalConversion() 테스트")
    class IsOriginalConversionTest {

        @Test
        @DisplayName("ORIGINAL_WEBP는 isOriginalConversion()이 true이다")
        void originalWebpIsOriginalConversion() {
            assertThat(ImageVariantType.ORIGINAL_WEBP.isOriginalConversion()).isTrue();
        }

        @Test
        @DisplayName("SMALL_WEBP는 isOriginalConversion()이 false이다")
        void smallWebpIsNotOriginalConversion() {
            assertThat(ImageVariantType.SMALL_WEBP.isOriginalConversion()).isFalse();
        }
    }

    @Nested
    @DisplayName("requiresDimensions() 테스트")
    class RequiresDimensionsTest {

        @Test
        @DisplayName("SMALL_WEBP는 requiresDimensions()가 true이다")
        void smallWebpRequiresDimensions() {
            assertThat(ImageVariantType.SMALL_WEBP.requiresDimensions()).isTrue();
        }

        @Test
        @DisplayName("ORIGINAL_WEBP는 requiresDimensions()가 false이다")
        void originalWebpDoesNotRequireDimensions() {
            assertThat(ImageVariantType.ORIGINAL_WEBP.requiresDimensions()).isFalse();
        }
    }

    @Nested
    @DisplayName("toFileSuffix() 테스트")
    class ToFileSuffixTest {

        @Test
        @DisplayName("SMALL_WEBP의 파일 접미사는 '300x300.webp'이다")
        void smallWebpFileSuffix() {
            assertThat(ImageVariantType.SMALL_WEBP.toFileSuffix()).isEqualTo("300x300.webp");
        }

        @Test
        @DisplayName("MEDIUM_WEBP의 파일 접미사는 '600x600.webp'이다")
        void mediumWebpFileSuffix() {
            assertThat(ImageVariantType.MEDIUM_WEBP.toFileSuffix()).isEqualTo("600x600.webp");
        }

        @Test
        @DisplayName("LARGE_WEBP의 파일 접미사는 '1200x1200.webp'이다")
        void largeWebpFileSuffix() {
            assertThat(ImageVariantType.LARGE_WEBP.toFileSuffix()).isEqualTo("1200x1200.webp");
        }

        @Test
        @DisplayName("ORIGINAL_WEBP의 파일 접미사는 'original.webp'이다")
        void originalWebpFileSuffix() {
            assertThat(ImageVariantType.ORIGINAL_WEBP.toFileSuffix()).isEqualTo("original.webp");
        }
    }
}
