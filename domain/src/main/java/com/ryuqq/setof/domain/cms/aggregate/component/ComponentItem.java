package com.ryuqq.setof.domain.cms.aggregate.component;

import com.ryuqq.setof.domain.cms.vo.ComponentId;
import com.ryuqq.setof.domain.cms.vo.ComponentItemId;
import com.ryuqq.setof.domain.cms.vo.ComponentItemType;
import com.ryuqq.setof.domain.cms.vo.ComponentStatus;
import com.ryuqq.setof.domain.cms.vo.ContentTitle;
import com.ryuqq.setof.domain.cms.vo.DisplayOrder;
import com.ryuqq.setof.domain.cms.vo.ImageUrl;
import com.ryuqq.setof.domain.cms.vo.SortType;
import java.time.Clock;
import java.time.Instant;

/**
 * ComponentItem 엔티티
 *
 * <p>Component에 포함되는 개별 아이템입니다. (상품, 브랜드, 카테고리 등)
 *
 * <p><strong>용도</strong>:
 *
 * <ul>
 *   <li>PRODUCT 타입 컴포넌트의 개별 상품 목록
 *   <li>BRAND 타입 컴포넌트의 개별 브랜드 목록
 *   <li>CATEGORY 타입 컴포넌트의 개별 카테고리 참조
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public class ComponentItem {

    // ==================== 필드 ====================

    private final ComponentItemId id;
    private final ComponentId componentId;
    private final ComponentItemType itemType;
    private Long referenceId;
    private ContentTitle title;
    private ImageUrl imageUrl;
    private ImageUrl linkUrl;
    private DisplayOrder displayOrder;
    private ComponentStatus status;
    private SortType sortType;
    private String extraData;
    private final Instant createdAt;
    private Instant deletedAt;
    private final Clock clock;

    // ==================== 생성자 (private) ====================

    private ComponentItem(
            ComponentItemId id,
            ComponentId componentId,
            ComponentItemType itemType,
            Long referenceId,
            ContentTitle title,
            ImageUrl imageUrl,
            ImageUrl linkUrl,
            DisplayOrder displayOrder,
            ComponentStatus status,
            SortType sortType,
            String extraData,
            Instant createdAt,
            Instant deletedAt,
            Clock clock) {
        this.id = id;
        this.componentId = componentId;
        this.itemType = itemType;
        this.referenceId = referenceId;
        this.title = title;
        this.imageUrl = imageUrl;
        this.linkUrl = linkUrl;
        this.displayOrder = displayOrder;
        this.status = status;
        this.sortType = sortType;
        this.extraData = extraData;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
        this.clock = clock;
    }

    // ==================== 정적 팩토리 메서드 ====================

    /**
     * 신규 ComponentItem 생성
     *
     * @param componentId 소속 Component ID
     * @param itemType 아이템 타입
     * @param referenceId 참조 ID (상품ID, 브랜드ID 등)
     * @param title 표시 제목 (nullable)
     * @param imageUrl 표시 이미지 URL (nullable)
     * @param linkUrl 링크 URL (nullable)
     * @param displayOrder 노출 순서
     * @param sortType 정렬 타입 (nullable)
     * @param clock 시간 제공자
     * @return ComponentItem 인스턴스
     */
    public static ComponentItem forNew(
            ComponentId componentId,
            ComponentItemType itemType,
            Long referenceId,
            ContentTitle title,
            ImageUrl imageUrl,
            ImageUrl linkUrl,
            DisplayOrder displayOrder,
            SortType sortType,
            Clock clock) {
        validateCreate(componentId, itemType);
        Instant now = clock.instant();
        return new ComponentItem(
                ComponentItemId.forNew(),
                componentId,
                itemType,
                referenceId,
                title,
                imageUrl != null ? imageUrl : ImageUrl.empty(),
                linkUrl != null ? linkUrl : ImageUrl.empty(),
                displayOrder != null ? displayOrder : DisplayOrder.DEFAULT,
                ComponentStatus.ACTIVE,
                sortType,
                null,
                now,
                null,
                clock);
    }

    /**
     * 상품 아이템 생성 헬퍼
     *
     * @param componentId 소속 Component ID
     * @param productGroupId 상품 그룹 ID
     * @param title 표시 제목
     * @param imageUrl 표시 이미지 URL
     * @param displayOrder 노출 순서
     * @param clock 시간 제공자
     * @return ComponentItem 인스턴스
     */
    public static ComponentItem forProduct(
            ComponentId componentId,
            Long productGroupId,
            ContentTitle title,
            ImageUrl imageUrl,
            DisplayOrder displayOrder,
            Clock clock) {
        return forNew(
                componentId,
                ComponentItemType.PRODUCT,
                productGroupId,
                title,
                imageUrl,
                null,
                displayOrder,
                null,
                clock);
    }

    /**
     * 브랜드 아이템 생성 헬퍼
     *
     * @param componentId 소속 Component ID
     * @param brandId 브랜드 ID
     * @param title 표시 제목
     * @param imageUrl 표시 이미지 URL
     * @param displayOrder 노출 순서
     * @param clock 시간 제공자
     * @return ComponentItem 인스턴스
     */
    public static ComponentItem forBrand(
            ComponentId componentId,
            Long brandId,
            ContentTitle title,
            ImageUrl imageUrl,
            DisplayOrder displayOrder,
            Clock clock) {
        return forNew(
                componentId,
                ComponentItemType.BRAND,
                brandId,
                title,
                imageUrl,
                null,
                displayOrder,
                null,
                clock);
    }

    /**
     * 기존 ComponentItem 복원 (Persistence 전용)
     *
     * @param id ComponentItem ID
     * @param componentId Component ID
     * @param itemType 아이템 타입
     * @param referenceId 참조 ID
     * @param title 제목
     * @param imageUrl 이미지 URL
     * @param linkUrl 링크 URL
     * @param displayOrder 노출 순서
     * @param status 상태
     * @param sortType 정렬 타입
     * @param extraData 추가 데이터
     * @param createdAt 생성일시
     * @param deletedAt 삭제일시
     * @param clock 시간 제공자
     * @return ComponentItem 인스턴스
     */
    public static ComponentItem reconstitute(
            ComponentItemId id,
            ComponentId componentId,
            ComponentItemType itemType,
            Long referenceId,
            ContentTitle title,
            ImageUrl imageUrl,
            ImageUrl linkUrl,
            DisplayOrder displayOrder,
            ComponentStatus status,
            SortType sortType,
            String extraData,
            Instant createdAt,
            Instant deletedAt,
            Clock clock) {
        return new ComponentItem(
                id,
                componentId,
                itemType,
                referenceId,
                title,
                imageUrl,
                linkUrl,
                displayOrder,
                status,
                sortType,
                extraData,
                createdAt,
                deletedAt,
                clock);
    }

    // ==================== 비즈니스 메서드 ====================

    /** 컴포넌트 아이템 소프트 삭제 */
    public void delete() {
        this.status = ComponentStatus.DELETED;
        this.deletedAt = clock.instant();
    }

    /** 컴포넌트 아이템 활성화 */
    public void activate() {
        this.status = ComponentStatus.ACTIVE;
    }

    /** 컴포넌트 아이템 비활성화 */
    public void deactivate() {
        this.status = ComponentStatus.INACTIVE;
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
    }

    /**
     * 표시 정보 업데이트
     *
     * @param title 새 제목
     * @param imageUrl 새 이미지 URL
     */
    public void updateDisplayInfo(ContentTitle title, ImageUrl imageUrl) {
        this.title = title;
        this.imageUrl = imageUrl != null ? imageUrl : ImageUrl.empty();
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
     * 상품 아이템인지 확인
     *
     * @return 상품 아이템이면 true
     */
    public boolean isProductItem() {
        return itemType == ComponentItemType.PRODUCT;
    }

    /**
     * 브랜드 아이템인지 확인
     *
     * @return 브랜드 아이템이면 true
     */
    public boolean isBrandItem() {
        return itemType == ComponentItemType.BRAND;
    }

    // ==================== 검증 메서드 ====================

    private static void validateCreate(ComponentId componentId, ComponentItemType itemType) {
        if (componentId == null) {
            throw new IllegalArgumentException("Component ID는 필수입니다");
        }
        if (itemType == null) {
            throw new IllegalArgumentException("아이템 타입은 필수입니다");
        }
    }

    // ==================== Getter ====================

    public ComponentItemId id() {
        return id;
    }

    public ComponentId componentId() {
        return componentId;
    }

    public ComponentItemType itemType() {
        return itemType;
    }

    public Long referenceId() {
        return referenceId;
    }

    public ContentTitle title() {
        return title;
    }

    public ImageUrl imageUrl() {
        return imageUrl;
    }

    public ImageUrl linkUrl() {
        return linkUrl;
    }

    public DisplayOrder displayOrder() {
        return displayOrder;
    }

    public ComponentStatus status() {
        return status;
    }

    public SortType sortType() {
        return sortType;
    }

    public String extraData() {
        return extraData;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant deletedAt() {
        return deletedAt;
    }
}
