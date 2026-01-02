package com.ryuqq.setof.domain.cms.aggregate.banner;

import com.ryuqq.setof.domain.cms.vo.BannerId;
import com.ryuqq.setof.domain.cms.vo.BannerStatus;
import com.ryuqq.setof.domain.cms.vo.BannerType;
import com.ryuqq.setof.domain.cms.vo.ContentTitle;
import com.ryuqq.setof.domain.cms.vo.DisplayPeriod;
import com.ryuqq.setof.domain.common.event.DomainEvent;
import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Banner Aggregate Root
 *
 * <p>배너 정보를 관리하는 도메인 객체입니다.
 *
 * <p><strong>불변식(Invariant)</strong>:
 *
 * <ul>
 *   <li>제목(title)은 필수
 *   <li>배너 타입(bannerType)은 필수
 *   <li>노출 기간(displayPeriod)은 필수
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public class Banner {

    // ==================== 필드 ====================

    private final BannerId id;
    private ContentTitle title;
    private final BannerType bannerType;
    private BannerStatus status;
    private DisplayPeriod displayPeriod;
    private final Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
    private final Clock clock;
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    // ==================== 생성자 (private) ====================

    private Banner(
            BannerId id,
            ContentTitle title,
            BannerType bannerType,
            BannerStatus status,
            DisplayPeriod displayPeriod,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt,
            Clock clock) {
        this.id = id;
        this.title = title;
        this.bannerType = bannerType;
        this.status = status;
        this.displayPeriod = displayPeriod;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.clock = clock;
    }

    // ==================== 정적 팩토리 메서드 ====================

    /**
     * 신규 Banner 생성
     *
     * @param title 제목
     * @param bannerType 배너 타입
     * @param displayPeriod 노출 기간
     * @param clock 시간 제공자
     * @return Banner 인스턴스
     */
    public static Banner forNew(
            ContentTitle title, BannerType bannerType, DisplayPeriod displayPeriod, Clock clock) {
        validateCreate(title, bannerType, displayPeriod);
        Instant now = clock.instant();
        return new Banner(
                BannerId.forNew(),
                title,
                bannerType,
                BannerStatus.ACTIVE,
                displayPeriod,
                now,
                now,
                null,
                clock);
    }

    /**
     * 기존 Banner 복원 (Persistence 전용)
     *
     * @param id Banner ID
     * @param title 제목
     * @param bannerType 배너 타입
     * @param status 상태
     * @param displayPeriod 노출 기간
     * @param createdAt 생성일시
     * @param updatedAt 수정일시
     * @param deletedAt 삭제일시
     * @param clock 시간 제공자
     * @return Banner 인스턴스
     */
    public static Banner reconstitute(
            BannerId id,
            ContentTitle title,
            BannerType bannerType,
            BannerStatus status,
            DisplayPeriod displayPeriod,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt,
            Clock clock) {
        return new Banner(
                id,
                title,
                bannerType,
                status,
                displayPeriod,
                createdAt,
                updatedAt,
                deletedAt,
                clock);
    }

    // ==================== 비즈니스 메서드 ====================

    /** 배너 활성화 */
    public void activate() {
        this.status = BannerStatus.ACTIVE;
        this.updatedAt = clock.instant();
    }

    /** 배너 비활성화 */
    public void deactivate() {
        this.status = BannerStatus.INACTIVE;
        this.updatedAt = clock.instant();
    }

    /** 배너 소프트 삭제 */
    public void delete() {
        this.status = BannerStatus.DELETED;
        this.deletedAt = clock.instant();
        this.updatedAt = this.deletedAt;
    }

    /**
     * 배너 정보 수정
     *
     * @param title 새 제목
     * @param displayPeriod 새 노출 기간
     */
    public void update(ContentTitle title, DisplayPeriod displayPeriod) {
        if (title == null) {
            throw new IllegalArgumentException("제목은 필수입니다");
        }
        if (displayPeriod == null) {
            throw new IllegalArgumentException("노출 기간은 필수입니다");
        }
        this.title = title;
        this.displayPeriod = displayPeriod;
        this.updatedAt = clock.instant();
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
        return displayPeriod.isDisplayableAt(clock.instant());
    }

    /**
     * 활성 상태인지 확인
     *
     * @return 활성이면 true
     */
    public boolean isActive() {
        return status.isActive();
    }

    // ==================== 검증 메서드 ====================

    private static void validateCreate(
            ContentTitle title, BannerType bannerType, DisplayPeriod displayPeriod) {
        if (title == null) {
            throw new IllegalArgumentException("제목은 필수입니다");
        }
        if (bannerType == null) {
            throw new IllegalArgumentException("배너 타입은 필수입니다");
        }
        if (displayPeriod == null) {
            throw new IllegalArgumentException("노출 기간은 필수입니다");
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

    public BannerId id() {
        return id;
    }

    public ContentTitle title() {
        return title;
    }

    public BannerType bannerType() {
        return bannerType;
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

    public Instant updatedAt() {
        return updatedAt;
    }

    public Instant deletedAt() {
        return deletedAt;
    }
}
