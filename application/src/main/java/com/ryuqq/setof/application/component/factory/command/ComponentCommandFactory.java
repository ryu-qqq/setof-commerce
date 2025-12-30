package com.ryuqq.setof.application.component.factory.command;

import com.ryuqq.setof.application.component.dto.command.ComponentDetailCommand;
import com.ryuqq.setof.application.component.dto.command.CreateComponentCommand;
import com.ryuqq.setof.application.component.dto.command.UpdateComponentCommand;
import com.ryuqq.setof.domain.category.vo.CategoryId;
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
import com.ryuqq.setof.domain.cms.vo.BadgeType;
import com.ryuqq.setof.domain.cms.vo.ComponentName;
import com.ryuqq.setof.domain.cms.vo.ComponentType;
import com.ryuqq.setof.domain.cms.vo.ContentId;
import com.ryuqq.setof.domain.cms.vo.DisplayOrder;
import com.ryuqq.setof.domain.cms.vo.DisplayPeriod;
import com.ryuqq.setof.domain.cms.vo.ImageType;
import com.ryuqq.setof.domain.cms.vo.ListType;
import com.ryuqq.setof.domain.cms.vo.OrderType;
import com.ryuqq.setof.domain.cms.vo.TabMovingType;
import com.ryuqq.setof.domain.common.util.ClockHolder;

/**
 * Component Command Factory
 *
 * <p>Command DTO를 Domain 객체로 변환합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@org.springframework.stereotype.Component
public class ComponentCommandFactory {

    private final ClockHolder clockHolder;

    public ComponentCommandFactory(ClockHolder clockHolder) {
        this.clockHolder = clockHolder;
    }

    /**
     * CreateComponentCommand → Component 도메인 변환
     *
     * @param command 생성 커맨드
     * @return Component 도메인 객체
     */
    public Component createComponent(CreateComponentCommand command) {
        ContentId contentId = command.contentId();
        ComponentName componentName =
                command.componentName() != null
                        ? ComponentName.of(command.componentName())
                        : ComponentName.empty();
        DisplayOrder displayOrder = DisplayOrder.of(command.displayOrder());
        DisplayPeriod displayPeriod = createDisplayPeriod(command);
        ComponentDetail detail = createComponentDetail(command.componentType(), command.detail());

        return Component.forNew(
                contentId,
                componentName,
                displayOrder,
                displayPeriod,
                detail,
                clockHolder.getClock());
    }

    /**
     * UpdateComponentCommand로 기존 Component 업데이트
     *
     * @param existing 기존 컴포넌트
     * @param command 수정 커맨드
     */
    public void applyUpdateComponent(Component existing, UpdateComponentCommand command) {
        if (command.displayOrder() != null) {
            existing.changeDisplayOrder(DisplayOrder.of(command.displayOrder()));
        }

        if (command.detail() != null) {
            ComponentDetail newDetail =
                    createComponentDetail(existing.componentType().name(), command.detail());
            existing.updateDetail(newDetail);
        }

        if (command.exposedProducts() != null) {
            existing.updateExposedProducts(command.exposedProducts());
        }
    }

    // ==================== Private Methods ====================

    private DisplayPeriod createDisplayPeriod(CreateComponentCommand command) {
        if (!command.hasDisplayPeriod()) {
            return null;
        }
        return DisplayPeriod.of(command.displayStartDate(), command.displayEndDate());
    }

    /**
     * ComponentType과 ComponentDetailCommand를 이용해 ComponentDetail 생성
     *
     * <p>Java 21 Pattern Matching 사용
     *
     * @param componentType 컴포넌트 타입 문자열
     * @param detail 상세 커맨드
     * @return ComponentDetail sealed interface 구현체
     */
    private ComponentDetail createComponentDetail(
            String componentType, ComponentDetailCommand detail) {
        ComponentType type = ComponentType.valueOf(componentType);

        return switch (type) {
            case BLANK -> createBlankDetail(detail);
            case TEXT -> createTextDetail(detail);
            case TITLE -> createTitleDetail(detail);
            case IMAGE -> createImageDetail(detail);
            case PRODUCT -> createProductDetail(detail);
            case CATEGORY -> createCategoryDetail(detail);
            case BRAND -> createBrandDetail();
            case TAB -> createTabDetail(detail);
        };
    }

    private BlankDetail createBlankDetail(ComponentDetailCommand detail) {
        double height = detail.height() != null ? detail.height() : 0.0;
        boolean showLine = detail.showLine() != null && detail.showLine();
        return BlankDetail.of(height, showLine);
    }

    private TextDetail createTextDetail(ComponentDetailCommand detail) {
        return TextDetail.of(detail.content());
    }

    private TitleDetail createTitleDetail(ComponentDetailCommand detail) {
        return TitleDetail.of(
                detail.title1(), detail.title2(), detail.subTitle1(), detail.subTitle2());
    }

    private ImageDetail createImageDetail(ComponentDetailCommand detail) {
        ImageType imageType =
                detail.imageType() != null
                        ? ImageType.valueOf(detail.imageType())
                        : ImageType.SINGLE;
        return ImageDetail.of(imageType);
    }

    private ProductDetail createProductDetail(ComponentDetailCommand detail) {
        ListType listType =
                detail.listType() != null ? ListType.valueOf(detail.listType()) : ListType.GRID;
        OrderType orderType =
                detail.orderType() != null
                        ? OrderType.valueOf(detail.orderType())
                        : OrderType.LATEST;
        BadgeType badgeType =
                detail.badgeType() != null ? BadgeType.valueOf(detail.badgeType()) : BadgeType.NONE;
        boolean showFilter = detail.showFilter() != null && detail.showFilter();

        return ProductDetail.of(listType, orderType, badgeType, showFilter);
    }

    private CategoryDetail createCategoryDetail(ComponentDetailCommand detail) {
        if (detail.categoryId() == null) {
            throw new IllegalArgumentException("카테고리 ID는 필수입니다");
        }

        CategoryId categoryId = CategoryId.of(detail.categoryId());
        ListType listType =
                detail.listType() != null ? ListType.valueOf(detail.listType()) : ListType.GRID;
        OrderType orderType =
                detail.orderType() != null
                        ? OrderType.valueOf(detail.orderType())
                        : OrderType.LATEST;
        BadgeType badgeType =
                detail.badgeType() != null ? BadgeType.valueOf(detail.badgeType()) : BadgeType.NONE;
        boolean showFilter = detail.showFilter() != null && detail.showFilter();

        return CategoryDetail.of(categoryId, listType, orderType, badgeType, showFilter);
    }

    private BrandDetail createBrandDetail() {
        return BrandDetail.create();
    }

    private TabDetail createTabDetail(ComponentDetailCommand detail) {
        boolean stickyYn = detail.stickyYn() != null && detail.stickyYn();
        TabMovingType tabMovingType =
                detail.tabMovingType() != null
                        ? TabMovingType.valueOf(detail.tabMovingType())
                        : TabMovingType.SCROLL;
        return TabDetail.of(stickyYn, tabMovingType);
    }
}
