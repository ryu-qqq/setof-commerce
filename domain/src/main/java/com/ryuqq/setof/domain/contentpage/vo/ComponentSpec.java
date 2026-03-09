package com.ryuqq.setof.domain.contentpage.vo;

import java.util.List;

/**
 * ComponentSpec - 컴포넌트 타입별 상세 스펙 sealed interface.
 *
 * <p>8가지 ComponentType에 대응하는 record를 sealed permits로 제한합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public sealed interface ComponentSpec
        permits ComponentSpec.TextSpec,
                ComponentSpec.TitleSpec,
                ComponentSpec.ImageSpec,
                ComponentSpec.BlankSpec,
                ComponentSpec.ProductSpec,
                ComponentSpec.BrandSpec,
                ComponentSpec.CategorySpec,
                ComponentSpec.TabSpec {

    /** 텍스트 컴포넌트 스펙. */
    record TextSpec(long textComponentId, String content) implements ComponentSpec {}

    /** 타이틀 컴포넌트 스펙. */
    record TitleSpec(
            long titleComponentId, String title1, String title2, String subTitle1, String subTitle2)
            implements ComponentSpec {}

    /** 이미지 컴포넌트 스펙. */
    record ImageSpec(long imageComponentId, ImageType imageType, List<ImageSlide> slides)
            implements ComponentSpec {}

    /** 여백 컴포넌트 스펙. */
    record BlankSpec(long blankComponentId, double height, boolean showLine)
            implements ComponentSpec {}

    /** 상품 컴포넌트 스펙. */
    record ProductSpec(
            long productComponentId,
            int exposedProducts,
            List<ProductSlot> fixedProducts,
            List<ProductSlot> autoProducts)
            implements ComponentSpec {}

    /** 브랜드 컴포넌트 스펙. */
    record BrandSpec(
            long brandComponentId,
            long categoryId,
            int exposedProducts,
            List<BrandFilter> brandFilters,
            List<ProductSlot> fixedProducts,
            List<ProductSlot> autoProducts)
            implements ComponentSpec {}

    /** 카테고리 컴포넌트 스펙. */
    record CategorySpec(
            long categoryComponentId,
            long categoryId,
            int exposedProducts,
            List<ProductSlot> fixedProducts,
            List<ProductSlot> autoProducts)
            implements ComponentSpec {}

    /** 탭 컴포넌트 스펙. */
    record TabSpec(
            long tabComponentId,
            int exposedProducts,
            boolean sticky,
            TabMovingType movingType,
            List<DisplayTab> tabs)
            implements ComponentSpec {}
}
