package com.ryuqq.setof.application.component.assembler;

import com.ryuqq.setof.application.component.dto.response.ComponentDetailResponse;
import com.ryuqq.setof.application.component.dto.response.ComponentResponse;
import com.ryuqq.setof.domain.cms.aggregate.component.Component;
import com.ryuqq.setof.domain.cms.aggregate.component.detail.BlankDetail;
import com.ryuqq.setof.domain.cms.aggregate.component.detail.BrandDetail;
import com.ryuqq.setof.domain.cms.aggregate.component.detail.CategoryDetail;
import com.ryuqq.setof.domain.cms.aggregate.component.detail.ComponentDetail;
import com.ryuqq.setof.domain.cms.aggregate.component.detail.ImageDetail;
import com.ryuqq.setof.domain.cms.aggregate.component.detail.ProductDetail;
import com.ryuqq.setof.domain.cms.aggregate.component.detail.TabDetail;
import com.ryuqq.setof.domain.cms.aggregate.component.detail.TextDetail;
import com.ryuqq.setof.domain.cms.aggregate.component.detail.TitleDetail;
import com.ryuqq.setof.domain.cms.vo.DisplayPeriod;
import java.time.Instant;
import java.util.List;

/**
 * Component Assembler
 *
 * <p>Domain → Response DTO 변환을 담당합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@org.springframework.stereotype.Component
public class ComponentAssembler {

    /**
     * Component 도메인 → ComponentResponse 변환
     *
     * @param component 컴포넌트 도메인
     * @return ComponentResponse DTO
     */
    public ComponentResponse toResponse(Component component) {
        DisplayPeriod displayPeriod = component.displayPeriod();
        Instant displayStartDate = displayPeriod != null ? displayPeriod.startDate() : null;
        Instant displayEndDate = displayPeriod != null ? displayPeriod.endDate() : null;

        return ComponentResponse.of(
                component.id() != null ? component.id().value() : null,
                component.contentId() != null ? component.contentId().value() : null,
                component.componentType() != null ? component.componentType().name() : null,
                component.componentName() != null ? component.componentName().value() : null,
                component.displayOrder() != null ? component.displayOrder().value() : 0,
                component.status() != null ? component.status().name() : null,
                component.exposedProducts(),
                displayStartDate,
                displayEndDate,
                toDetailResponse(component.detail()),
                component.createdAt(),
                component.updatedAt());
    }

    /**
     * Component 목록 → ComponentResponse 목록 변환
     *
     * @param components 컴포넌트 목록
     * @return ComponentResponse 목록
     */
    public List<ComponentResponse> toResponses(List<Component> components) {
        return components.stream().map(this::toResponse).toList();
    }

    /**
     * ComponentDetail → ComponentDetailResponse 변환
     *
     * <p>Java 21 Pattern Matching for Switch를 사용하여 Sealed Class 타입별 변환
     *
     * @param detail ComponentDetail sealed interface
     * @return ComponentDetailResponse DTO
     */
    private ComponentDetailResponse toDetailResponse(ComponentDetail detail) {
        if (detail == null) {
            return null;
        }

        return switch (detail) {
            case BlankDetail blank ->
                    ComponentDetailResponse.forBlank(blank.height(), blank.showLine());

            case TextDetail text -> ComponentDetailResponse.forText(text.content());

            case TitleDetail title ->
                    ComponentDetailResponse.forTitle(
                            title.title1(), title.title2(), title.subTitle1(), title.subTitle2());

            case ImageDetail image ->
                    ComponentDetailResponse.forImage(
                            image.imageType() != null ? image.imageType().name() : null);

            case ProductDetail product ->
                    ComponentDetailResponse.forProduct(
                            product.listType() != null ? product.listType().name() : null,
                            product.orderType() != null ? product.orderType().name() : null,
                            product.badgeType() != null ? product.badgeType().name() : null,
                            product.showFilter());

            case CategoryDetail category ->
                    ComponentDetailResponse.forCategory(
                            category.categoryId() != null ? category.categoryId().value() : null,
                            category.listType() != null ? category.listType().name() : null,
                            category.orderType() != null ? category.orderType().name() : null,
                            category.badgeType() != null ? category.badgeType().name() : null,
                            category.showFilter());

            case BrandDetail brand -> ComponentDetailResponse.forBrand();

            case TabDetail tab ->
                    ComponentDetailResponse.forTab(
                            tab.stickyYn(),
                            tab.tabMovingType() != null ? tab.tabMovingType().name() : null);
        };
    }
}
