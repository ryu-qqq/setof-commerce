package com.ryuqq.setof.domain.cms.aggregate.banner;

import com.ryuqq.setof.domain.cms.vo.BannerId;
import com.ryuqq.setof.domain.cms.vo.BannerItemId;
import com.ryuqq.setof.domain.cms.vo.BannerStatus;
import com.ryuqq.setof.domain.cms.vo.ContentTitle;
import com.ryuqq.setof.domain.cms.vo.DisplayOrder;
import com.ryuqq.setof.domain.cms.vo.DisplayPeriod;
import com.ryuqq.setof.domain.cms.vo.ImageSize;
import com.ryuqq.setof.domain.cms.vo.ImageUrl;
import java.time.Clock;
import java.time.Instant;

/**
 * BannerItem 엔티티
 *
 * <p>Banner에 포함되는 개별 배너 아이템입니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public class BannerItem {

    // ==================== 필드 ====================

    private final BannerItemId id;
    private final BannerId bannerId;
    private ContentTitle title;
    private ImageUrl imageUrl;
    private ImageUrl linkUrl;
    private ImageSize imageSize;
    private DisplayOrder displayOrder;
    private BannerStatus status;
    private DisplayPeriod displayPeriod;
    private final Instant createdAt;
    private Instant deletedAt;
    private final Clock clock;

    // ==================== 생성자 (private) ====================

    private BannerItem(
            BannerItemId id,
            BannerId bannerId,
            ContentTitle title,
            ImageUrl imageUrl,
            ImageUrl linkUrl,
            ImageSize imageSize,
            DisplayOrder displayOrder,
            BannerStatus status,
            DisplayPeriod displayPeriod,
            Instant createdAt,
            Instant deletedAt,
            Clock clock) {
        this.id = id;
        this.bannerId = bannerId;
        this.title = title;
        this.imageUrl = imageUrl;
        this.linkUrl = linkUrl;
        this.imageSize = imageSize;
        this.displayOrder = displayOrder;
        this.status = status;
        this.displayPeriod = displayPeriod;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
        this.clock = clock;
    }

    // ==================== 정적 팩토리 메서드 ====================

    /**
     * 신규 BannerItem 생성
     *
     * @param bannerId 소속 Banner ID
     * @param title 제목 (nullable)
     * @param imageUrl 이미지 URL
     * @param linkUrl 링크 URL (nullable)
     * @param imageSize 이미지 크기 (nullable)
     * @param displayOrder 노출 순서
     * @param displayPeriod 노출 기간 (nullable)
     * @param clock 시간 제공자
     * @return BannerItem 인스턴스
     */
    public static BannerItem forNew(
            BannerId bannerId,
            ContentTitle title,
            ImageUrl imageUrl,
            ImageUrl linkUrl,
            ImageSize imageSize,
            DisplayOrder displayOrder,
            DisplayPeriod displayPeriod,
            Clock clock) {
        validateCreate(bannerId, imageUrl);
        Instant now = clock.instant();
        return new BannerItem(
                BannerItemId.forNew(),
                bannerId,
                title,
                imageUrl,
                linkUrl != null ? linkUrl : ImageUrl.empty(),
                imageSize != null ? imageSize : ImageSize.empty(),
                displayOrder != null ? displayOrder : DisplayOrder.DEFAULT,
                BannerStatus.ACTIVE,
                displayPeriod,
                now,
                null,
                clock);
    }

    /**
     * 기존 BannerItem 복원 (Persistence 전용)
     *
     * @param id BannerItem ID
     * @param bannerId Banner ID
     * @param title 제목
     * @param imageUrl 이미지 URL
     * @param linkUrl 링크 URL
     * @param imageSize 이미지 크기
     * @param displayOrder 노출 순서
     * @param status 상태
     * @param displayPeriod 노출 기간
     * @param createdAt 생성일시
     * @param deletedAt 삭제일시
     * @param clock 시간 제공자
     * @return BannerItem 인스턴스
     */
    public static BannerItem reconstitute(
            BannerItemId id,
            BannerId bannerId,
            ContentTitle title,
            ImageUrl imageUrl,
            ImageUrl linkUrl,
            ImageSize imageSize,
            DisplayOrder displayOrder,
            BannerStatus status,
            DisplayPeriod displayPeriod,
            Instant createdAt,
            Instant deletedAt,
            Clock clock) {
        return new BannerItem(
                id,
                bannerId,
                title,
                imageUrl,
                linkUrl,
                imageSize,
                displayOrder,
                status,
                displayPeriod,
                createdAt,
                deletedAt,
                clock);
    }

    // ==================== 비즈니스 메서드 ====================

    /** 배너 아이템 소프트 삭제 */
    public void delete() {
        this.status = BannerStatus.DELETED;
        this.deletedAt = clock.instant();
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

    // ==================== 판단 메서드 ====================

    /**
     * 현재 노출 가능 여부 확인
     *
     * @return 노출 가능하면 true
     */
    public boolean isDisplayable() {
        if (!status.isActive()) {
            return false;
        }
        if (displayPeriod == null) {
            return true;
        }
        return displayPeriod.isDisplayableAt(clock.instant());
    }

    // ==================== 검증 메서드 ====================

    private static void validateCreate(BannerId bannerId, ImageUrl imageUrl) {
        if (bannerId == null) {
            throw new IllegalArgumentException("Banner ID는 필수입니다");
        }
        if (imageUrl == null || !imageUrl.hasValue()) {
            throw new IllegalArgumentException("이미지 URL은 필수입니다");
        }
    }

    // ==================== Getter ====================

    public BannerItemId id() {
        return id;
    }

    public BannerId bannerId() {
        return bannerId;
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

    public ImageSize imageSize() {
        return imageSize;
    }

    public DisplayOrder displayOrder() {
        return displayOrder;
    }

    public BannerStatus status() {
        return status;
    }

    public DisplayPeriod displayPeriod() {
        return displayPeriod;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant deletedAt() {
        return deletedAt;
    }
}
