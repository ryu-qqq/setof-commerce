package com.ryuqq.setof.domain.cms.aggregate.component.detail;

import com.ryuqq.setof.domain.cms.vo.ComponentType;

/**
 * ComponentDetail Sealed Interface
 *
 * <p>Java 21 Sealed Interface를 사용하여 컴포넌트 타입별 상세 정보를 정의합니다.
 *
 * <p><strong>구현체</strong>:
 *
 * <ul>
 *   <li>{@link BlankDetail} - 빈 공간
 *   <li>{@link TextDetail} - 텍스트
 *   <li>{@link TitleDetail} - 제목
 *   <li>{@link ImageDetail} - 이미지
 *   <li>{@link ProductDetail} - 상품 리스트
 *   <li>{@link CategoryDetail} - 카테고리 기반 상품
 *   <li>{@link BrandDetail} - 브랜드
 *   <li>{@link TabDetail} - 탭
 * </ul>
 *
 * <p>Pattern Matching 사용 예시:
 *
 * <pre>{@code
 * return switch (detail) {
 *     case BlankDetail blank -> processBlank(blank);
 *     case TextDetail text -> processText(text);
 *     case TitleDetail title -> processTitle(title);
 *     case ImageDetail image -> processImage(image);
 *     case ProductDetail product -> processProduct(product);
 *     case CategoryDetail category -> processCategory(category);
 *     case BrandDetail brand -> processBrand(brand);
 *     case TabDetail tab -> processTab(tab);
 * };
 * }</pre>
 *
 * @author development-team
 * @since 1.0.0
 */
public sealed interface ComponentDetail
        permits BlankDetail,
                TextDetail,
                TitleDetail,
                ImageDetail,
                ProductDetail,
                CategoryDetail,
                BrandDetail,
                TabDetail {

    /**
     * 컴포넌트 타입 반환
     *
     * @return 해당 detail의 ComponentType
     */
    ComponentType getType();
}
