package com.ryuqq.setof.domain.imagevariant.vo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ImageSourceType Enum 테스트")
class ImageSourceTypeTest {

    @Nested
    @DisplayName("상태 확인 메서드 테스트")
    class StatusCheckTest {

        @Test
        @DisplayName("PRODUCT_GROUP_IMAGE는 isProductGroupImage()가 true이다")
        void productGroupImageIsProductGroupImage() {
            assertThat(ImageSourceType.PRODUCT_GROUP_IMAGE.isProductGroupImage()).isTrue();
            assertThat(ImageSourceType.PRODUCT_GROUP_IMAGE.isDescriptionImage()).isFalse();
        }

        @Test
        @DisplayName("DESCRIPTION_IMAGE는 isDescriptionImage()가 true이다")
        void descriptionImageIsDescriptionImage() {
            assertThat(ImageSourceType.DESCRIPTION_IMAGE.isDescriptionImage()).isTrue();
            assertThat(ImageSourceType.DESCRIPTION_IMAGE.isProductGroupImage()).isFalse();
        }
    }

    @Nested
    @DisplayName("description() 테스트")
    class DescriptionTest {

        @Test
        @DisplayName("각 소스 타입의 설명을 반환한다")
        void eachSourceTypeHasCorrectDescription() {
            assertThat(ImageSourceType.PRODUCT_GROUP_IMAGE.description()).isEqualTo("상품그룹 이미지");
            assertThat(ImageSourceType.DESCRIPTION_IMAGE.description()).isEqualTo("상세설명 이미지");
        }
    }

    @Nested
    @DisplayName("Enum 값 존재 테스트")
    class EnumValuesTest {

        @Test
        @DisplayName("모든 소스 타입 값이 존재한다")
        void allValuesExist() {
            assertThat(ImageSourceType.values())
                    .containsExactlyInAnyOrder(
                            ImageSourceType.PRODUCT_GROUP_IMAGE, ImageSourceType.DESCRIPTION_IMAGE);
        }
    }
}
