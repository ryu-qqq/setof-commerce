package com.ryuqq.setof.domain.navigation.aggregate;

import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.common.vo.DisplayPeriod;
import com.ryuqq.setof.domain.navigation.id.NavigationMenuId;
import java.time.Instant;

/**
 * NavigationMenu Aggregate Root.
 *
 * <p>상단 네비게이션 메뉴(GNB)를 나타냅니다. 홈, 남성, 기획전 등 메뉴 항목을 관리합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class NavigationMenu {

    private final NavigationMenuId id;
    private String title;
    private String linkUrl;
    private int displayOrder;
    private DisplayPeriod displayPeriod;
    private boolean active;
    private DeletionStatus deletionStatus;
    private final Instant createdAt;
    private Instant updatedAt;

    private NavigationMenu(
            NavigationMenuId id,
            String title,
            String linkUrl,
            int displayOrder,
            DisplayPeriod displayPeriod,
            boolean active,
            DeletionStatus deletionStatus,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.title = title;
        this.linkUrl = linkUrl;
        this.displayOrder = displayOrder;
        this.displayPeriod = displayPeriod;
        this.active = active;
        this.deletionStatus = deletionStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * 새 네비게이션 메뉴를 생성합니다.
     *
     * @param title 메뉴명
     * @param linkUrl 이동 URL
     * @param displayOrder 노출 순서
     * @param displayPeriod 노출 기간
     * @param active 노출 여부
     * @param now 생성 시각
     * @return NavigationMenu 인스턴스
     */
    public static NavigationMenu forNew(
            String title,
            String linkUrl,
            int displayOrder,
            DisplayPeriod displayPeriod,
            boolean active,
            Instant now) {
        return new NavigationMenu(
                NavigationMenuId.forNew(),
                title,
                linkUrl,
                displayOrder,
                displayPeriod,
                active,
                DeletionStatus.active(),
                now,
                now);
    }

    /** DB에서 복원합니다. */
    public static NavigationMenu reconstitute(
            NavigationMenuId id,
            String title,
            String linkUrl,
            int displayOrder,
            DisplayPeriod displayPeriod,
            boolean active,
            DeletionStatus deletionStatus,
            Instant createdAt,
            Instant updatedAt) {
        return new NavigationMenu(
                id,
                title,
                linkUrl,
                displayOrder,
                displayPeriod,
                active,
                deletionStatus,
                createdAt,
                updatedAt);
    }

    /**
     * 메뉴 정보를 수정합니다.
     *
     * @param updateData 수정 데이터
     */
    public void update(NavigationMenuUpdateData updateData) {
        this.title = updateData.title();
        this.linkUrl = updateData.linkUrl();
        this.displayOrder = updateData.displayOrder();
        this.displayPeriod = updateData.displayPeriod();
        this.active = updateData.active();
        this.updatedAt = updateData.updatedAt();
    }

    /**
     * 메뉴를 논리적으로 삭제합니다.
     *
     * @param now 삭제 시각
     */
    public void remove(Instant now) {
        this.active = false;
        this.deletionStatus = DeletionStatus.deletedAt(now);
        this.updatedAt = now;
    }

    /**
     * 현재 시각에 노출 가능한지 확인합니다.
     *
     * @param now 현재 시각
     * @return 노출 가능하면 true
     */
    public boolean isDisplayable(Instant now) {
        return active && !deletionStatus.isDeleted() && displayPeriod.isWithin(now);
    }

    public NavigationMenuId id() {
        return id;
    }

    public Long idValue() {
        return id.value();
    }

    public String title() {
        return title;
    }

    public String linkUrl() {
        return linkUrl;
    }

    public int displayOrder() {
        return displayOrder;
    }

    public DisplayPeriod displayPeriod() {
        return displayPeriod;
    }

    public boolean isActive() {
        return active;
    }

    public DeletionStatus deletionStatus() {
        return deletionStatus;
    }

    public boolean isDeleted() {
        return deletionStatus.isDeleted();
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}
