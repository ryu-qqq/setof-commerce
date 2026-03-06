package com.ryuqq.setof.domain.contentpage.aggregate;

import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.common.vo.DisplayPeriod;
import com.ryuqq.setof.domain.contentpage.id.ContentPageId;
import java.time.Instant;

/**
 * ContentPage - 콘텐츠 페이지 Aggregate Root.
 *
 * <p>GNB 클릭 시 표시되는 전체 페이지(홈, 남성, 기획전 등)의 메타 정보를 관리합니다.
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
public class ContentPage {

    private final ContentPageId id;
    private String title;
    private String memo;
    private String coverImageUrl;
    private DisplayPeriod displayPeriod;
    private boolean active;
    private DeletionStatus deletionStatus;
    private final Instant createdAt;
    private Instant updatedAt;

    private ContentPage(
            ContentPageId id,
            String title,
            String memo,
            String coverImageUrl,
            DisplayPeriod displayPeriod,
            boolean active,
            DeletionStatus deletionStatus,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.title = title;
        this.memo = memo;
        this.coverImageUrl = coverImageUrl;
        this.displayPeriod = displayPeriod;
        this.active = active;
        this.deletionStatus = deletionStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * 새 콘텐츠 페이지 생성.
     *
     * @param title 페이지 제목
     * @param memo 메모
     * @param coverImageUrl 커버 이미지 URL
     * @param displayPeriod 노출 기간
     * @param active 노출 여부
     * @param now 현재 시각 (외부 주입)
     * @return ContentPage
     */
    public static ContentPage forNew(
            String title,
            String memo,
            String coverImageUrl,
            DisplayPeriod displayPeriod,
            boolean active,
            Instant now) {
        return new ContentPage(
                ContentPageId.forNew(),
                title,
                memo,
                coverImageUrl,
                displayPeriod,
                active,
                DeletionStatus.active(),
                now,
                now);
    }

    /**
     * 영속 데이터로부터 복원.
     *
     * @return ContentPage
     */
    public static ContentPage reconstitute(
            ContentPageId id,
            String title,
            String memo,
            String coverImageUrl,
            DisplayPeriod displayPeriod,
            boolean active,
            DeletionStatus deletionStatus,
            Instant createdAt,
            Instant updatedAt) {
        return new ContentPage(
                id,
                title,
                memo,
                coverImageUrl,
                displayPeriod,
                active,
                deletionStatus,
                createdAt,
                updatedAt);
    }

    /**
     * 콘텐츠 페이지 정보 수정.
     *
     * @param updateData 수정 데이터
     */
    public void update(ContentPageUpdateData updateData) {
        this.title = updateData.title();
        this.memo = updateData.memo();
        this.coverImageUrl = updateData.coverImageUrl();
        this.displayPeriod = updateData.displayPeriod();
        this.active = updateData.active();
        this.updatedAt = updateData.updatedAt();
    }

    /**
     * 노출 상태 변경.
     *
     * @param active 노출 여부
     * @param now 현재 시각
     */
    public void changeDisplayStatus(boolean active, Instant now) {
        this.active = active;
        this.updatedAt = now;
    }

    /**
     * 콘텐츠 페이지 삭제 (소프트).
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

    public ContentPageId id() {
        return id;
    }

    public Long idValue() {
        return id.value();
    }

    public String title() {
        return title;
    }

    public String memo() {
        return memo;
    }

    public String coverImageUrl() {
        return coverImageUrl;
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
