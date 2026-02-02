package com.ryuqq.setof.domain.brand.aggregate;

import com.ryuqq.setof.domain.brand.id.BrandId;
import com.ryuqq.setof.domain.brand.vo.BrandIconImageUrl;
import com.ryuqq.setof.domain.brand.vo.BrandName;
import com.ryuqq.setof.domain.brand.vo.DisplayName;
import com.ryuqq.setof.domain.brand.vo.DisplayOrder;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import java.time.Instant;

/**
 * Brand - 브랜드 Aggregate.
 *
 * <p>상품 브랜드 정보를 표현합니다.
 *
 * <p>주요 불변식:
 *
 * <ul>
 *   <li>brandName, brandIconImageUrl, displayName은 필수
 *   <li>displayOrder는 0 이상이어야 함
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public class Brand {

    private final BrandId id;
    private final BrandName brandName;
    private final BrandIconImageUrl brandIconImageUrl;
    private final DisplayName displayName;
    private DisplayOrder displayOrder;
    private boolean displayed;
    private DeletionStatus deletionStatus;
    private final Instant createdAt;
    private Instant updatedAt;

    private Brand(
            BrandId id,
            BrandName brandName,
            BrandIconImageUrl brandIconImageUrl,
            DisplayName displayName,
            DisplayOrder displayOrder,
            boolean displayed,
            DeletionStatus deletionStatus,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.brandName = brandName;
        this.brandIconImageUrl = brandIconImageUrl;
        this.displayName = displayName;
        this.displayOrder = displayOrder;
        this.displayed = displayed;
        this.deletionStatus = deletionStatus != null ? deletionStatus : DeletionStatus.active();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // ========== Factory Methods ==========

    /**
     * 새 브랜드 생성.
     *
     * @param brandName 브랜드명 (필수)
     * @param brandIconImageUrl 브랜드 아이콘 이미지 URL (필수)
     * @param displayName 표시명 (필수)
     * @param displayOrder 표시 순서 (0 이상)
     * @param now 생성 시각
     * @return 새 Brand 인스턴스 (displayed=true)
     */
    public static Brand forNew(
            BrandName brandName,
            BrandIconImageUrl brandIconImageUrl,
            DisplayName displayName,
            DisplayOrder displayOrder,
            Instant now) {
        return new Brand(
                BrandId.forNew(),
                brandName,
                brandIconImageUrl,
                displayName,
                displayOrder,
                true,
                DeletionStatus.active(),
                now,
                now);
    }

    /**
     * 영속성 계층에서 엔티티 복원.
     *
     * @param id 식별자
     * @param brandName 브랜드명
     * @param brandIconImageUrl 브랜드 아이콘 이미지 URL
     * @param displayName 표시명
     * @param displayOrder 표시 순서
     * @param displayed 표시 여부
     * @param deletedAt 삭제 시각 (null이면 활성 상태)
     * @param createdAt 생성일시
     * @param updatedAt 수정일시
     * @return 복원된 Brand 인스턴스
     */
    public static Brand reconstitute(
            BrandId id,
            BrandName brandName,
            BrandIconImageUrl brandIconImageUrl,
            DisplayName displayName,
            DisplayOrder displayOrder,
            boolean displayed,
            Instant deletedAt,
            Instant createdAt,
            Instant updatedAt) {
        DeletionStatus status =
                deletedAt != null ? DeletionStatus.deletedAt(deletedAt) : DeletionStatus.active();
        return new Brand(
                id,
                brandName,
                brandIconImageUrl,
                displayName,
                displayOrder,
                displayed,
                status,
                createdAt,
                updatedAt);
    }

    // ========== Business Methods ==========

    /** 신규 생성 여부 확인 */
    public boolean isNew() {
        return id.isNew();
    }

    /**
     * 브랜드 정보 수정.
     *
     * @param data 수정할 데이터 번들
     * @param now 수정 시각
     */
    public void update(BrandUpdateData data, Instant now) {
        this.displayOrder = data.displayOrder();
        this.displayed = data.displayed();
        this.updatedAt = now;
    }

    /** 표시 활성화 */
    public void show(Instant now) {
        this.displayed = true;
        this.updatedAt = now;
    }

    /** 표시 비활성화 */
    public void hide(Instant now) {
        this.displayed = false;
        this.updatedAt = now;
    }

    /**
     * 브랜드 삭제 (Soft Delete).
     *
     * @param now 삭제 발생 시각
     */
    public void delete(Instant now) {
        this.deletionStatus = DeletionStatus.deletedAt(now);
        this.updatedAt = now;
    }

    /**
     * 삭제된 브랜드 복원.
     *
     * @param now 복원 시각
     */
    public void restore(Instant now) {
        this.deletionStatus = DeletionStatus.active();
        this.updatedAt = now;
    }

    // ========== Accessor Methods ==========

    public BrandId id() {
        return id;
    }

    public Long idValue() {
        return id.value();
    }

    public BrandName brandName() {
        return brandName;
    }

    public String brandNameValue() {
        return brandName.value();
    }

    public BrandIconImageUrl brandIconImageUrl() {
        return brandIconImageUrl;
    }

    public String brandIconImageUrlValue() {
        return brandIconImageUrl.value();
    }

    public DisplayName displayName() {
        return displayName;
    }

    public String displayNameValue() {
        return displayName.value();
    }

    public DisplayOrder displayOrder() {
        return displayOrder;
    }

    public int displayOrderValue() {
        return displayOrder.value();
    }

    public boolean isDisplayed() {
        return displayed;
    }

    public DeletionStatus deletionStatus() {
        return deletionStatus;
    }

    public boolean isDeleted() {
        return deletionStatus.isDeleted();
    }

    public Instant deletedAt() {
        return deletionStatus.deletedAt();
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}
