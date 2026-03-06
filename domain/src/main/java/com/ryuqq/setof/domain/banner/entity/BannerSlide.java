package com.ryuqq.setof.domain.banner.entity;

import com.ryuqq.setof.domain.banner.id.BannerSlideId;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.common.vo.DisplayPeriod;
import java.time.Instant;

/**
 * BannerSlide - 배너 슬라이드 Entity (BannerGroup 내).
 *
 * <p>DOM-AGG-003: 시간은 Instant, domain 내에서 Instant.now() 호출 금지.
 *
 * <p>DOM-AGG-004: setter 금지.
 *
 * <p>DOM-CMN-001: POJO, 프레임워크 어노테이션 금지.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class BannerSlide {

    private final BannerSlideId id;
    private String title;
    private String imageUrl;
    private String linkUrl;
    private int displayOrder;
    private DisplayPeriod displayPeriod;
    private boolean active;
    private DeletionStatus deletionStatus;
    private final Instant createdAt;
    private Instant updatedAt;

    private BannerSlide(
            BannerSlideId id,
            String title,
            String imageUrl,
            String linkUrl,
            int displayOrder,
            DisplayPeriod displayPeriod,
            boolean active,
            DeletionStatus deletionStatus,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.linkUrl = linkUrl;
        this.displayOrder = displayOrder;
        this.displayPeriod = displayPeriod;
        this.active = active;
        this.deletionStatus = deletionStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * 새 배너 슬라이드 생성.
     *
     * @param title 슬라이드 제목
     * @param imageUrl 이미지 URL
     * @param linkUrl 링크 URL
     * @param displayOrder 노출 순서
     * @param displayPeriod 노출 기간
     * @param active 노출 여부
     * @param now 현재 시각 (외부 주입)
     * @return BannerSlide
     */
    public static BannerSlide forNew(
            String title,
            String imageUrl,
            String linkUrl,
            int displayOrder,
            DisplayPeriod displayPeriod,
            boolean active,
            Instant now) {
        return new BannerSlide(
                BannerSlideId.forNew(),
                title,
                imageUrl,
                linkUrl,
                displayOrder,
                displayPeriod,
                active,
                DeletionStatus.active(),
                now,
                now);
    }

    /**
     * 영속 데이터로부터 복원.
     *
     * @return BannerSlide
     */
    public static BannerSlide reconstitute(
            BannerSlideId id,
            String title,
            String imageUrl,
            String linkUrl,
            int displayOrder,
            DisplayPeriod displayPeriod,
            boolean active,
            DeletionStatus deletionStatus,
            Instant createdAt,
            Instant updatedAt) {
        return new BannerSlide(
                id,
                title,
                imageUrl,
                linkUrl,
                displayOrder,
                displayPeriod,
                active,
                deletionStatus,
                createdAt,
                updatedAt);
    }

    /**
     * 슬라이드 정보 수정.
     *
     * @param title 슬라이드 제목
     * @param imageUrl 이미지 URL
     * @param linkUrl 링크 URL
     * @param displayOrder 노출 순서
     * @param displayPeriod 노출 기간
     * @param active 노출 여부
     * @param now 현재 시각
     */
    public void update(
            String title,
            String imageUrl,
            String linkUrl,
            int displayOrder,
            DisplayPeriod displayPeriod,
            boolean active,
            Instant now) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.linkUrl = linkUrl;
        this.displayOrder = displayOrder;
        this.displayPeriod = displayPeriod;
        this.active = active;
        this.updatedAt = now;
    }

    /**
     * 슬라이드 삭제 (소프트).
     *
     * @param now 삭제 시각
     */
    public void remove(Instant now) {
        this.deletionStatus = DeletionStatus.deletedAt(now);
        this.active = false;
        this.updatedAt = now;
    }

    public BannerSlideId id() {
        return id;
    }

    public Long idValue() {
        return id.value();
    }

    public String title() {
        return title;
    }

    public String imageUrl() {
        return imageUrl;
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
