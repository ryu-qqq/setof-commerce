package com.ryuqq.setof.domain.cms.aggregate.content;

import com.ryuqq.setof.domain.cms.exception.ContentInvalidStateException;
import com.ryuqq.setof.domain.cms.vo.ContentId;
import com.ryuqq.setof.domain.cms.vo.ContentMemo;
import com.ryuqq.setof.domain.cms.vo.ContentStatus;
import com.ryuqq.setof.domain.cms.vo.ContentTitle;
import com.ryuqq.setof.domain.cms.vo.DisplayPeriod;
import com.ryuqq.setof.domain.cms.vo.ImageUrl;
import com.ryuqq.setof.domain.common.event.DomainEvent;
import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Content Aggregate Root
 *
 * <p>CMS 컨텐츠의 핵심 도메인 객체입니다.
 *
 * <p><strong>불변식(Invariant)</strong>:
 *
 * <ul>
 *   <li>제목(title)은 필수
 *   <li>노출 기간(displayPeriod)은 필수
 *   <li>상태 전환은 비즈니스 규칙에 따라 제한
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public class Content {

    // ==================== 필드 ====================

    private final ContentId id;
    private ContentTitle title;
    private ContentMemo memo;
    private ImageUrl imageUrl;
    private ContentStatus status;
    private DisplayPeriod displayPeriod;
    private final Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
    private final Clock clock;
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    // ==================== 생성자 (private) ====================

    private Content(
            ContentId id,
            ContentTitle title,
            ContentMemo memo,
            ImageUrl imageUrl,
            ContentStatus status,
            DisplayPeriod displayPeriod,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt,
            Clock clock) {
        this.id = id;
        this.title = title;
        this.memo = memo;
        this.imageUrl = imageUrl;
        this.status = status;
        this.displayPeriod = displayPeriod;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.clock = clock;
    }

    // ==================== 정적 팩토리 메서드 ====================

    /**
     * 신규 Content 생성
     *
     * <p>상태는 DRAFT로 시작합니다.
     *
     * @param title 제목
     * @param memo 메모 (nullable)
     * @param imageUrl 이미지 URL (nullable)
     * @param displayPeriod 노출 기간
     * @param clock 시간 제공자
     * @return Content 인스턴스
     */
    public static Content forNew(
            ContentTitle title,
            ContentMemo memo,
            ImageUrl imageUrl,
            DisplayPeriod displayPeriod,
            Clock clock) {
        validateCreate(title, displayPeriod);
        Instant now = clock.instant();
        return new Content(
                ContentId.forNew(),
                title,
                memo != null ? memo : ContentMemo.empty(),
                imageUrl != null ? imageUrl : ImageUrl.empty(),
                ContentStatus.DRAFT,
                displayPeriod,
                now,
                now,
                null,
                clock);
    }

    /**
     * 기존 Content 복원 (Persistence 전용)
     *
     * <p>검증 없이 모든 필드를 그대로 복원합니다.
     *
     * @param id Content ID
     * @param title 제목
     * @param memo 메모
     * @param imageUrl 이미지 URL
     * @param status 상태
     * @param displayPeriod 노출 기간
     * @param createdAt 생성일시
     * @param updatedAt 수정일시
     * @param deletedAt 삭제일시
     * @param clock 시간 제공자
     * @return Content 인스턴스
     */
    public static Content reconstitute(
            ContentId id,
            ContentTitle title,
            ContentMemo memo,
            ImageUrl imageUrl,
            ContentStatus status,
            DisplayPeriod displayPeriod,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt,
            Clock clock) {
        return new Content(
                id,
                title,
                memo,
                imageUrl,
                status,
                displayPeriod,
                createdAt,
                updatedAt,
                deletedAt,
                clock);
    }

    // ==================== 비즈니스 메서드 ====================

    /**
     * Content 활성화
     *
     * <p>DRAFT 또는 INACTIVE 상태에서만 활성화 가능합니다.
     *
     * @throws ContentInvalidStateException 활성화 불가능한 상태인 경우
     */
    public void activate() {
        if (!status.canActivate()) {
            throw ContentInvalidStateException.cannotActivate(
                    id != null ? id.value() : null, status.name());
        }
        this.status = ContentStatus.ACTIVE;
        this.updatedAt = clock.instant();
    }

    /**
     * Content 비활성화
     *
     * <p>ACTIVE 상태에서만 비활성화 가능합니다.
     *
     * @throws ContentInvalidStateException 비활성화 불가능한 상태인 경우
     */
    public void deactivate() {
        if (!status.canDeactivate()) {
            throw ContentInvalidStateException.cannotDeactivate(
                    id != null ? id.value() : null, status.name());
        }
        this.status = ContentStatus.INACTIVE;
        this.updatedAt = clock.instant();
    }

    /**
     * Content 소프트 삭제
     *
     * <p>DELETED 상태가 아니면 삭제 가능합니다.
     *
     * @throws ContentInvalidStateException 삭제 불가능한 상태인 경우
     */
    public void delete() {
        if (!status.canDelete()) {
            throw ContentInvalidStateException.cannotDelete(
                    id != null ? id.value() : null, status.name());
        }
        this.status = ContentStatus.DELETED;
        this.deletedAt = clock.instant();
        this.updatedAt = this.deletedAt;
    }

    /**
     * Content 정보 수정
     *
     * @param title 새 제목
     * @param memo 새 메모
     * @param imageUrl 새 이미지 URL
     * @param displayPeriod 새 노출 기간
     */
    public void update(
            ContentTitle title, ContentMemo memo, ImageUrl imageUrl, DisplayPeriod displayPeriod) {
        validateCreate(title, displayPeriod);
        this.title = title;
        this.memo = memo != null ? memo : ContentMemo.empty();
        this.imageUrl = imageUrl != null ? imageUrl : ImageUrl.empty();
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
        if (status != ContentStatus.ACTIVE) {
            return false;
        }
        return displayPeriod.isDisplayableAt(clock.instant());
    }

    /**
     * 활성 상태인지 확인
     *
     * @return 활성 상태면 true
     */
    public boolean isActive() {
        return status == ContentStatus.ACTIVE;
    }

    /**
     * 삭제된 상태인지 확인
     *
     * @return 삭제되었으면 true
     */
    public boolean isDeleted() {
        return status == ContentStatus.DELETED;
    }

    // ==================== 검증 메서드 ====================

    private static void validateCreate(ContentTitle title, DisplayPeriod displayPeriod) {
        if (title == null) {
            throw new IllegalArgumentException("제목은 필수입니다");
        }
        if (displayPeriod == null) {
            throw new IllegalArgumentException("노출 기간은 필수입니다");
        }
    }

    // ==================== Event 관리 ====================

    /**
     * 도메인 이벤트 등록
     *
     * @param event 등록할 이벤트
     */
    protected void registerEvent(DomainEvent event) {
        this.domainEvents.add(event);
    }

    /**
     * 등록된 도메인 이벤트 조회 및 클리어
     *
     * @return 등록된 이벤트 목록
     */
    public List<DomainEvent> pullDomainEvents() {
        List<DomainEvent> events = List.copyOf(this.domainEvents);
        this.domainEvents.clear();
        return events;
    }

    // ==================== Getter (Setter 금지) ====================

    public ContentId id() {
        return id;
    }

    public ContentTitle title() {
        return title;
    }

    public ContentMemo memo() {
        return memo;
    }

    public ImageUrl imageUrl() {
        return imageUrl;
    }

    public ContentStatus status() {
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

    // ==================== 편의 메서드 (Assembler용) ====================

    /**
     * Content ID 값 조회
     *
     * @return Content ID (Long) - 신규면 null
     */
    public Long getIdValue() {
        return id != null ? id.value() : null;
    }

    /**
     * 제목 값 조회
     *
     * @return 제목 문자열
     */
    public String getTitleValue() {
        return title != null ? title.value() : null;
    }

    /**
     * 메모 값 조회
     *
     * @return 메모 문자열 (없으면 null)
     */
    public String getMemoValue() {
        return memo != null ? memo.value() : null;
    }

    /**
     * 이미지 URL 값 조회
     *
     * @return 이미지 URL 문자열 (없으면 null)
     */
    public String getImageUrlValue() {
        return imageUrl != null ? imageUrl.value() : null;
    }

    /**
     * 상태 값 조회
     *
     * @return 상태 문자열
     */
    public String getStatusValue() {
        return status != null ? status.name() : null;
    }

    /**
     * 노출 시작일 조회
     *
     * @return 노출 시작일시
     */
    public Instant getDisplayStartDate() {
        return displayPeriod != null ? displayPeriod.startDate() : null;
    }

    /**
     * 노출 종료일 조회
     *
     * @return 노출 종료일시
     */
    public Instant getDisplayEndDate() {
        return displayPeriod != null ? displayPeriod.endDate() : null;
    }
}
