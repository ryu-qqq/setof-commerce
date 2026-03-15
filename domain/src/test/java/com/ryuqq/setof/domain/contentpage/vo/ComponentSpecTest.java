package com.ryuqq.setof.domain.contentpage.vo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ComponentSpec sealed interface 단위 테스트")
class ComponentSpecTest {

    @Nested
    @DisplayName("TextSpec 생성 테스트")
    class TextSpecTest {

        @Test
        @DisplayName("TextSpec을 생성하고 필드에 접근한다")
        void createTextSpec() {
            // when
            ComponentSpec spec = new ComponentSpec.TextSpec(1L, "안내 문구입니다");

            // then
            assertThat(spec).isInstanceOf(ComponentSpec.TextSpec.class);
            ComponentSpec.TextSpec textSpec = (ComponentSpec.TextSpec) spec;
            assertThat(textSpec.textComponentId()).isEqualTo(1L);
            assertThat(textSpec.content()).isEqualTo("안내 문구입니다");
        }

        @Test
        @DisplayName("같은 값의 TextSpec은 동등하다")
        void sameTextSpecIsEqual() {
            // given
            ComponentSpec spec1 = new ComponentSpec.TextSpec(1L, "내용");
            ComponentSpec spec2 = new ComponentSpec.TextSpec(1L, "내용");

            // then
            assertThat(spec1).isEqualTo(spec2);
        }
    }

    @Nested
    @DisplayName("TitleSpec 생성 테스트")
    class TitleSpecTest {

        @Test
        @DisplayName("TitleSpec을 생성하고 필드에 접근한다")
        void createTitleSpec() {
            // when
            ComponentSpec spec =
                    new ComponentSpec.TitleSpec(2L, "메인 타이틀", "서브 타이틀", "소제목1", "소제목2");

            // then
            assertThat(spec).isInstanceOf(ComponentSpec.TitleSpec.class);
            ComponentSpec.TitleSpec titleSpec = (ComponentSpec.TitleSpec) spec;
            assertThat(titleSpec.titleComponentId()).isEqualTo(2L);
            assertThat(titleSpec.title1()).isEqualTo("메인 타이틀");
            assertThat(titleSpec.title2()).isEqualTo("서브 타이틀");
            assertThat(titleSpec.subTitle1()).isEqualTo("소제목1");
            assertThat(titleSpec.subTitle2()).isEqualTo("소제목2");
        }
    }

    @Nested
    @DisplayName("ImageSpec 생성 테스트")
    class ImageSpecTest {

        @Test
        @DisplayName("ImageSpec을 생성하고 필드에 접근한다")
        void createImageSpec() {
            // given
            List<ImageSlide> slides =
                    List.of(
                            new ImageSlide(
                                    1L,
                                    1,
                                    "https://example.com/img1.jpg",
                                    "https://example.com/link1"),
                            new ImageSlide(2L, 2, "https://example.com/img2.jpg", null));

            // when
            ComponentSpec spec = new ComponentSpec.ImageSpec(3L, ImageType.SLIDE, slides);

            // then
            assertThat(spec).isInstanceOf(ComponentSpec.ImageSpec.class);
            ComponentSpec.ImageSpec imageSpec = (ComponentSpec.ImageSpec) spec;
            assertThat(imageSpec.imageComponentId()).isEqualTo(3L);
            assertThat(imageSpec.imageType()).isEqualTo(ImageType.SLIDE);
            assertThat(imageSpec.slides()).hasSize(2);
        }
    }

    @Nested
    @DisplayName("BlankSpec 생성 테스트")
    class BlankSpecTest {

        @Test
        @DisplayName("BlankSpec을 생성하고 필드에 접근한다")
        void createBlankSpec() {
            // when
            ComponentSpec spec = new ComponentSpec.BlankSpec(4L, 16.0, true);

            // then
            assertThat(spec).isInstanceOf(ComponentSpec.BlankSpec.class);
            ComponentSpec.BlankSpec blankSpec = (ComponentSpec.BlankSpec) spec;
            assertThat(blankSpec.blankComponentId()).isEqualTo(4L);
            assertThat(blankSpec.height()).isEqualTo(16.0);
            assertThat(blankSpec.showLine()).isTrue();
        }

        @Test
        @DisplayName("showLine이 false인 BlankSpec을 생성한다")
        void createBlankSpecWithNoLine() {
            // when
            ComponentSpec spec = new ComponentSpec.BlankSpec(5L, 8.0, false);

            // then
            ComponentSpec.BlankSpec blankSpec = (ComponentSpec.BlankSpec) spec;
            assertThat(blankSpec.showLine()).isFalse();
        }
    }

    @Nested
    @DisplayName("ProductSpec 생성 테스트")
    class ProductSpecTest {

        @Test
        @DisplayName("ProductSpec을 생성하고 필드에 접근한다")
        void createProductSpec() {
            // given
            List<ProductSlot> fixed = List.of(new ProductSlot(100L, 1));
            List<ProductSlot> auto = List.of(new ProductSlot(200L, 1), new ProductSlot(300L, 2));

            // when
            ComponentSpec spec = new ComponentSpec.ProductSpec(5L, 10, fixed, auto);

            // then
            assertThat(spec).isInstanceOf(ComponentSpec.ProductSpec.class);
            ComponentSpec.ProductSpec productSpec = (ComponentSpec.ProductSpec) spec;
            assertThat(productSpec.productComponentId()).isEqualTo(5L);
            assertThat(productSpec.exposedProducts()).isEqualTo(10);
            assertThat(productSpec.fixedProducts()).hasSize(1);
            assertThat(productSpec.autoProducts()).hasSize(2);
        }
    }

    @Nested
    @DisplayName("BrandSpec 생성 테스트")
    class BrandSpecTest {

        @Test
        @DisplayName("BrandSpec을 생성하고 필드에 접근한다")
        void createBrandSpec() {
            // given
            List<BrandFilter> brandFilters =
                    List.of(new BrandFilter(10L, "브랜드A"), new BrandFilter(20L, "브랜드B"));
            List<ProductSlot> fixed = List.of();
            List<ProductSlot> auto = List.of(new ProductSlot(100L, 1));

            // when
            ComponentSpec spec =
                    new ComponentSpec.BrandSpec(6L, 50L, 20, brandFilters, fixed, auto);

            // then
            assertThat(spec).isInstanceOf(ComponentSpec.BrandSpec.class);
            ComponentSpec.BrandSpec brandSpec = (ComponentSpec.BrandSpec) spec;
            assertThat(brandSpec.brandComponentId()).isEqualTo(6L);
            assertThat(brandSpec.categoryId()).isEqualTo(50L);
            assertThat(brandSpec.exposedProducts()).isEqualTo(20);
            assertThat(brandSpec.brandFilters()).hasSize(2);
            assertThat(brandSpec.autoProducts()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("CategorySpec 생성 테스트")
    class CategorySpecTest {

        @Test
        @DisplayName("CategorySpec을 생성하고 필드에 접근한다")
        void createCategorySpec() {
            // given
            List<ProductSlot> fixed = List.of(new ProductSlot(100L, 1));
            List<ProductSlot> auto = List.of();

            // when
            ComponentSpec spec = new ComponentSpec.CategorySpec(7L, 99L, 15, fixed, auto);

            // then
            assertThat(spec).isInstanceOf(ComponentSpec.CategorySpec.class);
            ComponentSpec.CategorySpec categorySpec = (ComponentSpec.CategorySpec) spec;
            assertThat(categorySpec.categoryComponentId()).isEqualTo(7L);
            assertThat(categorySpec.categoryId()).isEqualTo(99L);
            assertThat(categorySpec.exposedProducts()).isEqualTo(15);
            assertThat(categorySpec.fixedProducts()).hasSize(1);
            assertThat(categorySpec.autoProducts()).isEmpty();
        }
    }

    @Nested
    @DisplayName("TabSpec 생성 테스트")
    class TabSpecTest {

        @Test
        @DisplayName("TabSpec을 생성하고 필드에 접근한다")
        void createTabSpec() {
            // given
            List<DisplayTab> tabs =
                    List.of(
                            new DisplayTab(1L, "탭A", 1, List.of(), List.of()),
                            new DisplayTab(2L, "탭B", 2, List.of(), List.of()));

            // when
            ComponentSpec spec =
                    new ComponentSpec.TabSpec(8L, 20, true, TabMovingType.SCROLL, tabs);

            // then
            assertThat(spec).isInstanceOf(ComponentSpec.TabSpec.class);
            ComponentSpec.TabSpec tabSpec = (ComponentSpec.TabSpec) spec;
            assertThat(tabSpec.tabComponentId()).isEqualTo(8L);
            assertThat(tabSpec.exposedProducts()).isEqualTo(20);
            assertThat(tabSpec.sticky()).isTrue();
            assertThat(tabSpec.movingType()).isEqualTo(TabMovingType.SCROLL);
            assertThat(tabSpec.tabs()).hasSize(2);
        }

        @Test
        @DisplayName("sticky가 false인 TabSpec을 생성한다")
        void createNonStickyTabSpec() {
            // when
            ComponentSpec spec =
                    new ComponentSpec.TabSpec(9L, 10, false, TabMovingType.SCROLL, List.of());

            // then
            ComponentSpec.TabSpec tabSpec = (ComponentSpec.TabSpec) spec;
            assertThat(tabSpec.sticky()).isFalse();
        }
    }

    @Nested
    @DisplayName("sealed 타입 패턴 매칭 테스트")
    class SealedPatternMatchingTest {

        @Test
        @DisplayName("switch 표현식으로 모든 sealed 타입을 구분할 수 있다")
        void switchExpressionCoversAllTypes() {
            // given
            List<ComponentSpec> specs =
                    List.of(
                            new ComponentSpec.TextSpec(1L, "텍스트"),
                            new ComponentSpec.TitleSpec(2L, "타이틀", null, null, null),
                            new ComponentSpec.ImageSpec(3L, ImageType.SLIDE, List.of()),
                            new ComponentSpec.BlankSpec(4L, 8.0, false),
                            new ComponentSpec.ProductSpec(5L, 10, List.of(), List.of()),
                            new ComponentSpec.BrandSpec(
                                    6L, 0L, 10, List.of(), List.of(), List.of()),
                            new ComponentSpec.CategorySpec(7L, 0L, 10, List.of(), List.of()),
                            new ComponentSpec.TabSpec(
                                    8L, 20, false, TabMovingType.SCROLL, List.of()));

            // when & then: 각 타입이 올바르게 instanceof로 식별된다
            assertThat(specs.get(0)).isInstanceOf(ComponentSpec.TextSpec.class);
            assertThat(specs.get(1)).isInstanceOf(ComponentSpec.TitleSpec.class);
            assertThat(specs.get(2)).isInstanceOf(ComponentSpec.ImageSpec.class);
            assertThat(specs.get(3)).isInstanceOf(ComponentSpec.BlankSpec.class);
            assertThat(specs.get(4)).isInstanceOf(ComponentSpec.ProductSpec.class);
            assertThat(specs.get(5)).isInstanceOf(ComponentSpec.BrandSpec.class);
            assertThat(specs.get(6)).isInstanceOf(ComponentSpec.CategorySpec.class);
            assertThat(specs.get(7)).isInstanceOf(ComponentSpec.TabSpec.class);
        }
    }
}
