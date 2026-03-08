package com.ryuqq.setof.domain.displaycomponent.aggregate;

import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.common.vo.DisplayPeriod;
import com.ryuqq.setof.domain.contentpage.id.ContentPageId;
import com.ryuqq.setof.domain.displaycomponent.entity.ProductCuration;
import com.ryuqq.setof.domain.displaycomponent.entity.ViewExtension;
import com.ryuqq.setof.domain.displaycomponent.id.DisplayComponentId;
import com.ryuqq.setof.domain.displaycomponent.vo.ComponentLayout;
import com.ryuqq.setof.domain.displaycomponent.vo.body.ComponentBody;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * DisplayComponent - 전시 컴포넌트 Aggregate Root.
 *
 * <p>개별 컴포넌트의 레이아웃, 타입별 본문(ComponentBody), 뷰 확장(ViewExtension), 상품 큐레이션(ProductCuration)을 관리한다.
 *
 * <p>DOM-AGG-001: forNew(), reconstitute() 팩터리 메서드.
 *
 * <p>DOM-AGG-003: Instant 타입, domain 내 Instant.now() 금지.
 *
 * <p>DOM-AGG-004: setter 금지.
 *
 * <p>DOM-CMN-001: POJO, 프레임워크 어노테이션 금지.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class DisplayComponent {

    private final DisplayComponentId id;
    private final ContentPageId contentPageId;
    private String name;
    private ComponentLayout componentLayout;
    private DisplayPeriod displayPeriod;
    private int displayOrder;
    private int exposedProductCount;
    private boolean active;
    private DeletionStatus deletionStatus;
    private ViewExtension viewExtension;
    private ComponentBody componentBody;
    private final List<ProductCuration> productCurations;
    private final Instant createdAt;
    private Instant updatedAt;

    private DisplayComponent(
            DisplayComponentId id,
            ContentPageId contentPageId,
            String name,
            ComponentLayout componentLayout,
            DisplayPeriod displayPeriod,
            int displayOrder,
            int exposedProductCount,
            boolean active,
            DeletionStatus deletionStatus,
            ViewExtension viewExtension,
            ComponentBody componentBody,
            List<ProductCuration> productCurations,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.contentPageId = contentPageId;
        this.name = name;
        this.componentLayout = componentLayout;
        this.displayPeriod = displayPeriod;
        this.displayOrder = displayOrder;
        this.exposedProductCount = exposedProductCount;
        this.active = active;
        this.deletionStatus = deletionStatus;
        this.viewExtension = viewExtension;
        this.componentBody = componentBody;
        this.productCurations = new ArrayList<>(productCurations);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /** 새 전시 컴포넌트 생성. */
    public static DisplayComponent forNew(
            ContentPageId contentPageId,
            String name,
            ComponentLayout componentLayout,
            DisplayPeriod displayPeriod,
            int displayOrder,
            int exposedProductCount,
            boolean active,
            ViewExtension viewExtension,
            ComponentBody componentBody,
            List<ProductCuration> productCurations,
            Instant now) {
        return new DisplayComponent(
                DisplayComponentId.forNew(),
                contentPageId,
                name,
                componentLayout,
                displayPeriod,
                displayOrder,
                exposedProductCount,
                active,
                DeletionStatus.active(),
                viewExtension,
                componentBody,
                productCurations,
                now,
                now);
    }

    /** 영속 데이터로부터 복원. */
    public static DisplayComponent reconstitute(
            DisplayComponentId id,
            ContentPageId contentPageId,
            String name,
            ComponentLayout componentLayout,
            DisplayPeriod displayPeriod,
            int displayOrder,
            int exposedProductCount,
            boolean active,
            DeletionStatus deletionStatus,
            ViewExtension viewExtension,
            ComponentBody componentBody,
            List<ProductCuration> productCurations,
            Instant createdAt,
            Instant updatedAt) {
        return new DisplayComponent(
                id,
                contentPageId,
                name,
                componentLayout,
                displayPeriod,
                displayOrder,
                exposedProductCount,
                active,
                deletionStatus,
                viewExtension,
                componentBody,
                productCurations,
                createdAt,
                updatedAt);
    }

    /** 컴포넌트 정보 수정. */
    public void update(DisplayComponentUpdateData updateData) {
        this.name = updateData.name();
        this.componentLayout = updateData.componentLayout();
        this.displayPeriod = updateData.displayPeriod();
        this.displayOrder = updateData.displayOrder();
        this.exposedProductCount = updateData.exposedProductCount();
        this.active = updateData.active();
        this.componentBody = updateData.componentBody();
        this.updatedAt = updateData.updatedAt();
    }

    /** 컴포넌트 본문 교체. */
    public void replaceBody(ComponentBody componentBody, Instant now) {
        this.componentBody = componentBody;
        this.updatedAt = now;
    }

    /** 상품 큐레이션 교체. */
    public void replaceCurations(List<ProductCuration> newCurations, Instant now) {
        this.productCurations.clear();
        this.productCurations.addAll(newCurations);
        this.updatedAt = now;
    }

    /** 뷰 확장 설정/교체. */
    public void setViewExtension(ViewExtension viewExtension, Instant now) {
        this.viewExtension = viewExtension;
        this.updatedAt = now;
    }

    /** 노출 상태 변경. */
    public void changeDisplayStatus(boolean active, Instant now) {
        this.active = active;
        this.updatedAt = now;
    }

    /** 컴포넌트 삭제 (소프트). */
    public void remove(Instant now) {
        this.deletionStatus = DeletionStatus.deletedAt(now);
        this.active = false;
        this.updatedAt = now;
    }

    /** 현재 노출 가능 여부 판단. */
    public boolean isDisplayable(Instant now) {
        return active && !deletionStatus.isDeleted() && displayPeriod.isWithin(now);
    }

    /** 상품 관련 컴포넌트 여부. */
    public boolean isProductRelated() {
        return componentLayout.isProductRelated();
    }

    // Getters
    public DisplayComponentId id() {
        return id;
    }

    public Long idValue() {
        return id.value();
    }

    public ContentPageId contentPageId() {
        return contentPageId;
    }

    public Long contentPageIdValue() {
        return contentPageId.value();
    }

    public String name() {
        return name;
    }

    public ComponentLayout componentLayout() {
        return componentLayout;
    }

    public DisplayPeriod displayPeriod() {
        return displayPeriod;
    }

    public int displayOrder() {
        return displayOrder;
    }

    public int exposedProductCount() {
        return exposedProductCount;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isDeleted() {
        return deletionStatus.isDeleted();
    }

    public DeletionStatus deletionStatus() {
        return deletionStatus;
    }

    public ViewExtension viewExtension() {
        return viewExtension;
    }

    public ComponentBody componentBody() {
        return componentBody;
    }

    public List<ProductCuration> productCurations() {
        return Collections.unmodifiableList(productCurations);
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}
