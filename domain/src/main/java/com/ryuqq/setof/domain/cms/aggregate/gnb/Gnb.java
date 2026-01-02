package com.ryuqq.setof.domain.cms.aggregate.gnb;

import com.ryuqq.setof.domain.cms.vo.ContentTitle;
import com.ryuqq.setof.domain.cms.vo.DisplayOrder;
import com.ryuqq.setof.domain.cms.vo.DisplayPeriod;
import com.ryuqq.setof.domain.cms.vo.GnbId;
import com.ryuqq.setof.domain.cms.vo.GnbStatus;
import com.ryuqq.setof.domain.cms.vo.ImageUrl;
import com.ryuqq.setof.domain.common.event.DomainEvent;
import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * GNB(Global Navigation Bar) Aggregate Root
 *
 * <p>글로벌 네비게이션 바 항목을 관리하는 도메인 객체입니다.
 *
 * <p><strong>불변식(Invariant)</strong>:
 *
 * <ul>
 *   <li>제목(title)은 필수
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public class Gnb {

    // ==================== 필드 ====================

    private final GnbId id;
    private ContentTitle title;
    private ImageUrl linkUrl;
    private DisplayOrder displayOrder;
    private GnbStatus status;
    private DisplayPeriod displayPeriod;
    private final Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
    private final Clock clock;
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    // ==================== 생성자 (private) ====================

    private Gnb(
            GnbId id,
            ContentTitle title,
            ImageUrl linkUrl,
            DisplayOrder displayOrder,
            GnbStatus status,
            DisplayPeriod displayPeriod,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt,
            Clock clock) {
        this.id = id;
        this.title = title;
        this.linkUrl = linkUrl;
        this.displayOrder = displayOrder;
        this.status = status;
        this.displayPeriod = displayPeriod;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.clock = clock;
    }

    // ==================== 정적 팩토리 메서드 ====================

    /**
     * 신규 GNB 생성
     *
     * @param title 제목
     * @param linkUrl 링크 URL (nullable)
     * @param displayOrder 노출 순서
     * @param displayPeriod 노출 기간 (nullable)
     * @param clock 시간 제공자
     * @return Gnb 인스턴스
     */
    public static Gnb forNew(
            ContentTitle title,
            ImageUrl linkUrl,
            DisplayOrder displayOrder,
            DisplayPeriod displayPeriod,
            Clock clock) {
        validateCreate(title);
        Instant now = clock.instant();
        return new Gnb(
                GnbId.forNew(),
                title,
                linkUrl != null ? linkUrl : ImageUrl.empty(),
                displayOrder != null ? displayOrder : DisplayOrder.DEFAULT,
                GnbStatus.ACTIVE,
                displayPeriod,
                now,
                now,
                null,
                clock);
    }

    /**
     * 기존 GNB 복원 (Persistence 전용)
     *
     * @param id GNB ID
     * @param title 제목
     * @param linkUrl 링크 URL
     * @param displayOrder 노출 순서
     * @param status 상태
     * @param displayPeriod 노출 기간
     * @param createdAt 생성일시
     * @param updatedAt 수정일시
     * @param deletedAt 삭제일시
     * @param clock 시간 제공자
     * @return Gnb 인스턴스
     */
    public static Gnb reconstitute(
            GnbId id,
            ContentTitle title,
            ImageUrl linkUrl,
            DisplayOrder displayOrder,
            GnbStatus status,
            DisplayPeriod displayPeriod,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt,
            Clock clock) {
        return new Gnb(
                id,
                title,
                linkUrl,
                displayOrder,
                status,
                displayPeriod,
                createdAt,
                updatedAt,
                deletedAt,
                clock);
    }

    // ==================== 비즈니스 메서드 ====================

    /** GNB 활성화 */
    public void activate() {
        this.status = GnbStatus.ACTIVE;
        this.updatedAt = clock.instant();
    }

    /** GNB 비활성화 */
    public void deactivate() {
        this.status = GnbStatus.INACTIVE;
        this.updatedAt = clock.instant();
    }

    /** GNB 소프트 삭제 */
    public void delete() {
        this.status = GnbStatus.DELETED;
        this.deletedAt = clock.instant();
        this.updatedAt = this.deletedAt;
    }

    /**
     * GNB 정보 수정
     *
     * @param title 새 제목
     * @param linkUrl 새 링크 URL
     * @param displayOrder 새 노출 순서
     * @param displayPeriod 새 노출 기간
     */
    public void update(
            ContentTitle title,
            ImageUrl linkUrl,
            DisplayOrder displayOrder,
            DisplayPeriod displayPeriod) {
        validateCreate(title);
        this.title = title;
        this.linkUrl = linkUrl != null ? linkUrl : ImageUrl.empty();
        this.displayOrder = displayOrder != null ? displayOrder : DisplayOrder.DEFAULT;
        this.displayPeriod = displayPeriod;
        this.updatedAt = clock.instant();
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

    /**
     * 활성 상태인지 확인
     *
     * @return 활성이면 true
     */
    public boolean isActive() {
        return status.isActive();
    }

    // ==================== 검증 메서드 ====================

    private static void validateCreate(ContentTitle title) {
        if (title == null) {
            throw new IllegalArgumentException("제목은 필수입니다");
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

    public GnbId id() {
        return id;
    }

    public ContentTitle title() {
        return title;
    }

    public ImageUrl linkUrl() {
        return linkUrl;
    }

    public DisplayOrder displayOrder() {
        return displayOrder;
    }

    public GnbStatus status() {
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
