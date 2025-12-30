package com.ryuqq.setof.domain.cms.aggregate.component;

import com.ryuqq.setof.domain.cms.aggregate.component.detail.ComponentDetail;
import com.ryuqq.setof.domain.cms.vo.ComponentId;
import com.ryuqq.setof.domain.cms.vo.ComponentName;
import com.ryuqq.setof.domain.cms.vo.ComponentStatus;
import com.ryuqq.setof.domain.cms.vo.ComponentType;
import com.ryuqq.setof.domain.cms.vo.ContentId;
import com.ryuqq.setof.domain.cms.vo.DisplayOrder;
import com.ryuqq.setof.domain.cms.vo.DisplayPeriod;
import com.ryuqq.setof.domain.common.event.DomainEvent;
import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Component Aggregate Root
 *
 * <p>CMS 컴포넌트 도메인 객체입니다. ComponentDetail은 Sealed Interface로 타입 안전성을 보장합니다.
 *
 * <p><strong>불변식(Invariant)</strong>:
 *
 * <ul>
 *   <li>contentId는 필수
 *   <li>componentType과 detail의 타입이 일치해야 함
 *   <li>detail은 필수
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public class Component {

    // ==================== 필드 ====================

    private final ComponentId id;
    private final ContentId contentId;
    private final ComponentType componentType;
    private ComponentName componentName;
    private DisplayOrder displayOrder;
    private ComponentStatus status;
    private int exposedProducts;
    private DisplayPeriod displayPeriod;
    private ComponentDetail detail;
    private final Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
    private final Clock clock;
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    // ==================== 생성자 (private) ====================

    private Component(
            ComponentId id,
            ContentId contentId,
            ComponentType componentType,
            ComponentName componentName,
            DisplayOrder displayOrder,
            ComponentStatus status,
            int exposedProducts,
            DisplayPeriod displayPeriod,
            ComponentDetail detail,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt,
            Clock clock) {
        this.id = id;
        this.contentId = contentId;
        this.componentType = componentType;
        this.componentName = componentName;
        this.displayOrder = displayOrder;
        this.status = status;
        this.exposedProducts = exposedProducts;
        this.displayPeriod = displayPeriod;
        this.detail = detail;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.clock = clock;
    }

    // ==================== 정적 팩토리 메서드 ====================

    /**
     * 신규 Component 생성
     *
     * @param contentId 소속 Content ID
     * @param componentName 컴포넌트 이름 (nullable)
     * @param displayOrder 노출 순서
     * @param displayPeriod 노출 기간 (nullable)
     * @param detail 타입별 상세 정보
     * @param clock 시간 제공자
     * @return Component 인스턴스
     */
    public static Component forNew(
            ContentId contentId,
            ComponentName componentName,
            DisplayOrder displayOrder,
            DisplayPeriod displayPeriod,
            ComponentDetail detail,
            Clock clock) {
        validateCreate(contentId, detail);
        Instant now = clock.instant();
        return new Component(
                ComponentId.forNew(),
                contentId,
                detail.getType(),
                componentName != null ? componentName : ComponentName.empty(),
                displayOrder != null ? displayOrder : DisplayOrder.DEFAULT,
                ComponentStatus.ACTIVE,
                0,
                displayPeriod,
                detail,
                now,
                now,
                null,
                clock);
    }

    /**
     * 기존 Component 복원 (Persistence 전용)
     *
     * @param id Component ID
     * @param contentId Content ID
     * @param componentType 컴포넌트 타입
     * @param componentName 이름
     * @param displayOrder 노출 순서
     * @param status 상태
     * @param exposedProducts 노출 상품 수
     * @param displayPeriod 노출 기간
     * @param detail 상세 정보
     * @param createdAt 생성일시
     * @param updatedAt 수정일시
     * @param deletedAt 삭제일시
     * @param clock 시간 제공자
     * @return Component 인스턴스
     */
    public static Component reconstitute(
            ComponentId id,
            ContentId contentId,
            ComponentType componentType,
            ComponentName componentName,
            DisplayOrder displayOrder,
            ComponentStatus status,
            int exposedProducts,
            DisplayPeriod displayPeriod,
            ComponentDetail detail,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt,
            Clock clock) {
        return new Component(
                id,
                contentId,
                componentType,
                componentName,
                displayOrder,
                status,
                exposedProducts,
                displayPeriod,
                detail,
                createdAt,
                updatedAt,
                deletedAt,
                clock);
    }

    // ==================== 비즈니스 메서드 ====================

    /** Component 비활성화 */
    public void deactivate() {
        this.status = ComponentStatus.INACTIVE;
        this.updatedAt = clock.instant();
    }

    /** Component 활성화 */
    public void activate() {
        this.status = ComponentStatus.ACTIVE;
        this.updatedAt = clock.instant();
    }

    /** Component 소프트 삭제 */
    public void delete() {
        this.status = ComponentStatus.DELETED;
        this.deletedAt = clock.instant();
        this.updatedAt = this.deletedAt;
    }

    /**
     * 노출 순서 변경
     *
     * @param newOrder 새 노출 순서
     */
    public void changeDisplayOrder(DisplayOrder newOrder) {
        if (newOrder == null) {
            throw new IllegalArgumentException("노출 순서는 null일 수 없습니다");
        }
        this.displayOrder = newOrder;
        this.updatedAt = clock.instant();
    }

    /**
     * 상세 정보 업데이트
     *
     * @param newDetail 새 상세 정보
     */
    public void updateDetail(ComponentDetail newDetail) {
        if (newDetail == null) {
            throw new IllegalArgumentException("상세 정보는 null일 수 없습니다");
        }
        if (newDetail.getType() != this.componentType) {
            throw new IllegalArgumentException(
                    "타입이 일치하지 않습니다. 현재: " + this.componentType + ", 입력: " + newDetail.getType());
        }
        this.detail = newDetail;
        this.updatedAt = clock.instant();
    }

    /**
     * 노출 상품 수 업데이트
     *
     * @param count 상품 수
     */
    public void updateExposedProducts(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("노출 상품 수는 0 이상이어야 합니다");
        }
        this.exposedProducts = count;
        this.updatedAt = clock.instant();
    }

    // ==================== 판단 메서드 ====================

    /**
     * 활성 상태인지 확인
     *
     * @return 활성이면 true
     */
    public boolean isActive() {
        return status.isActive();
    }

    /**
     * 삭제된 상태인지 확인
     *
     * @return 삭제되었으면 true
     */
    public boolean isDeleted() {
        return status == ComponentStatus.DELETED;
    }

    /**
     * 현재 노출 가능 여부 확인
     *
     * @return 노출 가능하면 true
     */
    public boolean isDisplayable() {
        if (!isActive()) {
            return false;
        }
        if (displayPeriod == null) {
            return true;
        }
        return displayPeriod.isDisplayableAt(clock.instant());
    }

    // ==================== 검증 메서드 ====================

    private static void validateCreate(ContentId contentId, ComponentDetail detail) {
        if (contentId == null) {
            throw new IllegalArgumentException("Content ID는 필수입니다");
        }
        if (detail == null) {
            throw new IllegalArgumentException("상세 정보는 필수입니다");
        }
    }

    // ==================== Event 관리 ====================

    protected void registerEvent(DomainEvent event) {
        this.domainEvents.add(event);
    }

    public List<DomainEvent> pullDomainEvents() {
        List<DomainEvent> events = List.copyOf(this.domainEvents);
        this.domainEvents.clear();
        return events;
    }

    // ==================== Getter ====================

    public ComponentId id() {
        return id;
    }

    public ContentId contentId() {
        return contentId;
    }

    public ComponentType componentType() {
        return componentType;
    }

    public ComponentName componentName() {
        return componentName;
    }

    public DisplayOrder displayOrder() {
        return displayOrder;
    }

    public ComponentStatus status() {
        return status;
    }

    public int exposedProducts() {
        return exposedProducts;
    }

    public DisplayPeriod displayPeriod() {
        return displayPeriod;
    }

    public ComponentDetail detail() {
        return detail;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    public Instant deletedAt() {
        return deletedAt;
    }
}
