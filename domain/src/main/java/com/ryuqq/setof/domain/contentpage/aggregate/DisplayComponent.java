package com.ryuqq.setof.domain.contentpage.aggregate;

import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.common.vo.DisplayPeriod;
import com.ryuqq.setof.domain.contentpage.id.DisplayComponentId;
import com.ryuqq.setof.domain.contentpage.vo.ComponentSpec;
import com.ryuqq.setof.domain.contentpage.vo.ComponentType;
import com.ryuqq.setof.domain.contentpage.vo.DisplayConfig;
import com.ryuqq.setof.domain.contentpage.vo.ViewExtension;
import java.time.Instant;

/**
 * DisplayComponent - 디스플레이 컴포넌트 Aggregate Root.
 *
 * <p>콘텐츠 페이지 내 개별 컴포넌트(텍스트, 이미지, 상품 등)를 관리합니다. componentType은 생성 후 변경 불가(불변)입니다.
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
    private final long contentPageId;
    private String name;
    private int displayOrder;
    private final ComponentType componentType;
    private DisplayConfig displayConfig;
    private DisplayPeriod displayPeriod;
    private boolean active;
    private ViewExtension viewExtension;
    private ComponentSpec componentSpec;
    private DeletionStatus deletionStatus;
    private final Instant createdAt;
    private Instant updatedAt;

    private DisplayComponent(
            DisplayComponentId id,
            long contentPageId,
            String name,
            int displayOrder,
            ComponentType componentType,
            DisplayConfig displayConfig,
            DisplayPeriod displayPeriod,
            boolean active,
            ViewExtension viewExtension,
            ComponentSpec componentSpec,
            DeletionStatus deletionStatus,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.contentPageId = contentPageId;
        this.name = name;
        this.displayOrder = displayOrder;
        this.componentType = componentType;
        this.displayConfig = displayConfig;
        this.displayPeriod = displayPeriod;
        this.active = active;
        this.viewExtension = viewExtension;
        this.componentSpec = componentSpec;
        this.deletionStatus = deletionStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * 새 디스플레이 컴포넌트 생성.
     *
     * @param contentPageId 소속 콘텐츠 페이지 ID
     * @param name 컴포넌트 이름
     * @param displayOrder 노출 순서
     * @param componentType 컴포넌트 타입
     * @param displayConfig 표시 설정
     * @param displayPeriod 노출 기간
     * @param active 노출 여부
     * @param viewExtension 뷰 확장 설정 (nullable)
     * @param componentSpec 컴포넌트 스펙 (nullable)
     * @param now 현재 시각 (외부 주입)
     * @return DisplayComponent
     */
    public static DisplayComponent forNew(
            long contentPageId,
            String name,
            int displayOrder,
            ComponentType componentType,
            DisplayConfig displayConfig,
            DisplayPeriod displayPeriod,
            boolean active,
            ViewExtension viewExtension,
            ComponentSpec componentSpec,
            Instant now) {
        return new DisplayComponent(
                DisplayComponentId.forNew(),
                contentPageId,
                name,
                displayOrder,
                componentType,
                displayConfig,
                displayPeriod,
                active,
                viewExtension,
                componentSpec,
                DeletionStatus.active(),
                now,
                now);
    }

    /**
     * 영속 데이터로부터 복원.
     *
     * @return DisplayComponent
     */
    public static DisplayComponent reconstitute(
            DisplayComponentId id,
            long contentPageId,
            String name,
            int displayOrder,
            ComponentType componentType,
            DisplayConfig displayConfig,
            DisplayPeriod displayPeriod,
            boolean active,
            ViewExtension viewExtension,
            ComponentSpec componentSpec,
            DeletionStatus deletionStatus,
            Instant createdAt,
            Instant updatedAt) {
        return new DisplayComponent(
                id,
                contentPageId,
                name,
                displayOrder,
                componentType,
                displayConfig,
                displayPeriod,
                active,
                viewExtension,
                componentSpec,
                deletionStatus,
                createdAt,
                updatedAt);
    }

    /**
     * 디스플레이 컴포넌트 정보 수정.
     *
     * @param updateData 수정 데이터
     */
    public void update(DisplayComponentUpdateData updateData) {
        this.name = updateData.name();
        this.displayOrder = updateData.displayOrder();
        this.displayConfig = updateData.displayConfig();
        this.viewExtension = updateData.viewExtension();
        this.componentSpec = updateData.componentSpec();
        this.displayPeriod = updateData.displayPeriod();
        this.active = updateData.active();
        this.updatedAt = updateData.updatedAt();
    }

    /**
     * 디스플레이 컴포넌트 삭제 (소프트).
     *
     * @param now 삭제 시각
     */
    public void remove(Instant now) {
        this.deletionStatus = DeletionStatus.deletedAt(now);
        this.active = false;
        this.updatedAt = now;
    }

    /**
     * 현재 노출 가능 여부 판단.
     *
     * @param now 현재 시각
     * @return 노출 가능 여부
     */
    public boolean isDisplayable(Instant now) {
        return active && !deletionStatus.isDeleted() && displayPeriod.isWithin(now);
    }

    public DisplayComponentId id() {
        return id;
    }

    public Long idValue() {
        return id.value();
    }

    public long contentPageId() {
        return contentPageId;
    }

    public String name() {
        return name;
    }

    public int displayOrder() {
        return displayOrder;
    }

    public ComponentType componentType() {
        return componentType;
    }

    public DisplayConfig displayConfig() {
        return displayConfig;
    }

    public DisplayPeriod displayPeriod() {
        return displayPeriod;
    }

    public boolean isActive() {
        return active;
    }

    public ViewExtension viewExtension() {
        return viewExtension;
    }

    public ComponentSpec componentSpec() {
        return componentSpec;
    }

    public boolean isDeleted() {
        return deletionStatus.isDeleted();
    }

    public DeletionStatus deletionStatus() {
        return deletionStatus;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}
