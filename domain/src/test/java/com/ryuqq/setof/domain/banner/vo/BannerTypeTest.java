package com.ryuqq.setof.domain.banner.vo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("BannerType Value Object 테스트")
class BannerTypeTest {

    @Nested
    @DisplayName("enum 값 존재 테스트")
    class EnumValuesTest {

        @Test
        @DisplayName("모든 BannerType 값이 존재한다")
        void allValuesExist() {
            // then
            assertThat(BannerType.values())
                    .containsExactlyInAnyOrder(
                            BannerType.CATEGORY,
                            BannerType.MY_PAGE,
                            BannerType.CART,
                            BannerType.PRODUCT_DETAIL_DESCRIPTION,
                            BannerType.RECOMMEND,
                            BannerType.LOGIN);
        }

        @Test
        @DisplayName("BannerType의 개수가 6개이다")
        void bannerTypeCount() {
            // then
            assertThat(BannerType.values()).hasSize(6);
        }
    }

    @Nested
    @DisplayName("BannerType 개별 값 테스트")
    class IndividualValueTest {

        @Test
        @DisplayName("CATEGORY 타입의 이름이 올바르다")
        void categoryName() {
            assertThat(BannerType.CATEGORY.name()).isEqualTo("CATEGORY");
        }

        @Test
        @DisplayName("MY_PAGE 타입의 이름이 올바르다")
        void myPageName() {
            assertThat(BannerType.MY_PAGE.name()).isEqualTo("MY_PAGE");
        }

        @Test
        @DisplayName("CART 타입의 이름이 올바르다")
        void cartName() {
            assertThat(BannerType.CART.name()).isEqualTo("CART");
        }

        @Test
        @DisplayName("PRODUCT_DETAIL_DESCRIPTION 타입의 이름이 올바르다")
        void productDetailDescriptionName() {
            assertThat(BannerType.PRODUCT_DETAIL_DESCRIPTION.name())
                    .isEqualTo("PRODUCT_DETAIL_DESCRIPTION");
        }

        @Test
        @DisplayName("RECOMMEND 타입의 이름이 올바르다")
        void recommendName() {
            assertThat(BannerType.RECOMMEND.name()).isEqualTo("RECOMMEND");
        }

        @Test
        @DisplayName("LOGIN 타입의 이름이 올바르다")
        void loginName() {
            assertThat(BannerType.LOGIN.name()).isEqualTo("LOGIN");
        }
    }

    @Nested
    @DisplayName("valueOf 테스트")
    class ValueOfTest {

        @Test
        @DisplayName("문자열로 BannerType을 가져올 수 있다")
        void getByName() {
            assertThat(BannerType.valueOf("CATEGORY")).isEqualTo(BannerType.CATEGORY);
            assertThat(BannerType.valueOf("MY_PAGE")).isEqualTo(BannerType.MY_PAGE);
            assertThat(BannerType.valueOf("CART")).isEqualTo(BannerType.CART);
            assertThat(BannerType.valueOf("PRODUCT_DETAIL_DESCRIPTION"))
                    .isEqualTo(BannerType.PRODUCT_DETAIL_DESCRIPTION);
            assertThat(BannerType.valueOf("RECOMMEND")).isEqualTo(BannerType.RECOMMEND);
            assertThat(BannerType.valueOf("LOGIN")).isEqualTo(BannerType.LOGIN);
        }
    }
}
