package com.ryuqq.setof.domain.banner.aggregate;

import com.ryuqq.setof.domain.banner.entity.BannerSlide;
import com.ryuqq.setof.domain.banner.id.BannerGroupId;
import com.ryuqq.setof.domain.banner.vo.BannerType;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.common.vo.DisplayPeriod;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * BannerGroup - 배너 그룹 Aggregate Root.
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
public class BannerGroup {

    private final BannerGroupId id;
    private String title;
    private BannerType bannerType;
    private DisplayPeriod displayPeriod;
    private boolean active;
    private DeletionStatus deletionStatus;
    private final List<BannerSlide> slides;
    private final Instant createdAt;
    private Instant updatedAt;

    private BannerGroup(
            BannerGroupId id,
            String title,
            BannerType bannerType,
            DisplayPeriod displayPeriod,
            boolean active,
            DeletionStatus deletionStatus,
            List<BannerSlide> slides,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.title = title;
        this.bannerType = bannerType;
        this.displayPeriod = displayPeriod;
        this.active = active;
        this.deletionStatus = deletionStatus;
        this.slides = new ArrayList<>(slides);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * 새 배너 그룹 생성.
     *
     * @param title 배너 그룹명
     * @param bannerType 배너 타입
     * @param displayPeriod 노출 기간
     * @param active 노출 여부
     * @param slides 배너 슬라이드 목록
     * @param now 현재 시각 (외부 주입)
     * @return BannerGroup
     */
    public static BannerGroup forNew(
            String title,
            BannerType bannerType,
            DisplayPeriod displayPeriod,
            boolean active,
            List<BannerSlide> slides,
            Instant now) {
        return new BannerGroup(
                BannerGroupId.forNew(),
                title,
                bannerType,
                displayPeriod,
                active,
                DeletionStatus.active(),
                slides,
                now,
                now);
    }

    /**
     * 영속 데이터로부터 복원.
     *
     * @return BannerGroup
     */
    public static BannerGroup reconstitute(
            BannerGroupId id,
            String title,
            BannerType bannerType,
            DisplayPeriod displayPeriod,
            boolean active,
            DeletionStatus deletionStatus,
            List<BannerSlide> slides,
            Instant createdAt,
            Instant updatedAt) {
        return new BannerGroup(
                id,
                title,
                bannerType,
                displayPeriod,
                active,
                deletionStatus,
                slides,
                createdAt,
                updatedAt);
    }

    /**
     * 배너 그룹 정보 수정.
     *
     * @param updateData 수정 데이터
     */
    public void update(BannerGroupUpdateData updateData) {
        this.title = updateData.title();
        this.bannerType = updateData.bannerType();
        this.displayPeriod = updateData.displayPeriod();
        this.active = updateData.active();
        this.updatedAt = updateData.updatedAt();
        replaceSlides(updateData.slides());
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
     * 배너 그룹 삭제 (소프트).
     *
     * @param now 삭제 시각
     */
    public void remove(Instant now) {
        this.deletionStatus = DeletionStatus.deletedAt(now);
        this.active = false;
        this.updatedAt = now;
    }

    /**
     * 슬라이드 추가.
     *
     * @param slide 추가할 슬라이드
     */
    public void addSlide(BannerSlide slide) {
        this.slides.add(slide);
    }

    /**
     * 슬라이드 교체 (전체).
     *
     * @param newSlides 새 슬라이드 목록
     */
    public void replaceSlides(List<BannerSlide> newSlides) {
        this.slides.clear();
        this.slides.addAll(newSlides);
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

    public BannerGroupId id() {
        return id;
    }

    public Long idValue() {
        return id.value();
    }

    public String title() {
        return title;
    }

    public BannerType bannerType() {
        return bannerType;
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

    public List<BannerSlide> slides() {
        return Collections.unmodifiableList(slides);
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}
