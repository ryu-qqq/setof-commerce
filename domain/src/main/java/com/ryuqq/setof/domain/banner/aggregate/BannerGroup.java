package com.ryuqq.setof.domain.banner.aggregate;

import com.ryuqq.setof.domain.banner.entity.BannerSlide;
import com.ryuqq.setof.domain.banner.id.BannerGroupId;
import com.ryuqq.setof.domain.banner.vo.BannerSlideDiff;
import com.ryuqq.setof.domain.banner.vo.BannerType;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.common.vo.DisplayPeriod;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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
     * 배너 그룹 정보 수정 및 슬라이드 Diff 계산.
     *
     * <p>기존 슬라이드와 요청 엔트리를 ID 기반으로 비교하여 추가/삭제/유지를 분류합니다.
     *
     * @param updateData 수정 데이터
     * @return BannerSlideDiff 슬라이드 변경 비교 결과
     */
    public BannerSlideDiff update(BannerGroupUpdateData updateData) {
        this.title = updateData.title();
        this.bannerType = updateData.bannerType();
        this.displayPeriod = updateData.displayPeriod();
        this.active = updateData.active();
        this.updatedAt = updateData.updatedAt();
        return computeSlideDiff(updateData.slideEntries(), updateData.updatedAt());
    }

    private BannerSlideDiff computeSlideDiff(
            List<BannerGroupUpdateData.SlideEntry> entries, Instant now) {
        Map<Long, BannerSlide> existingById =
                slides.stream()
                        .filter(s -> s.idValue() != null)
                        .collect(Collectors.toMap(BannerSlide::idValue, Function.identity()));

        Set<Long> requestedIds = new HashSet<>();
        List<BannerSlide> added = new ArrayList<>();
        List<BannerSlide> retained = new ArrayList<>();

        for (BannerGroupUpdateData.SlideEntry entry : entries) {
            if (entry.slideId() == null) {
                added.add(
                        BannerSlide.forNew(
                                entry.title(),
                                entry.imageUrl(),
                                entry.linkUrl(),
                                entry.displayOrder(),
                                entry.displayPeriod(),
                                entry.active(),
                                now));
            } else {
                requestedIds.add(entry.slideId());
                BannerSlide existing = existingById.get(entry.slideId());
                if (existing != null) {
                    existing.update(
                            entry.title(),
                            entry.imageUrl(),
                            entry.linkUrl(),
                            entry.displayOrder(),
                            entry.displayPeriod(),
                            entry.active(),
                            now);
                    retained.add(existing);
                }
            }
        }

        List<BannerSlide> removed = new ArrayList<>();
        for (BannerSlide slide : slides) {
            if (slide.idValue() != null && !requestedIds.contains(slide.idValue())) {
                slide.remove(now);
                removed.add(slide);
            }
        }

        replaceSlides(new ArrayList<>());
        slides.addAll(retained);
        slides.addAll(added);

        return BannerSlideDiff.of(added, removed, retained, now);
    }

    /**
     * 배너 그룹 정보만 수정합니다 (슬라이드는 변경하지 않음).
     *
     * @param title 배너 그룹명
     * @param bannerType 배너 타입
     * @param displayPeriod 노출 기간
     * @param active 노출 여부
     * @param now 현재 시각
     */
    public void updateGroupInfo(
            String title,
            BannerType bannerType,
            DisplayPeriod displayPeriod,
            boolean active,
            Instant now) {
        this.title = title;
        this.bannerType = bannerType;
        this.displayPeriod = displayPeriod;
        this.active = active;
        this.updatedAt = now;
    }

    /**
     * 슬라이드만 Diff 기반으로 수정합니다 (그룹 정보는 변경하지 않음).
     *
     * @param entries 슬라이드 수정 엔트리 목록
     * @param now 현재 시각
     * @return BannerSlideDiff 슬라이드 변경 비교 결과
     */
    public BannerSlideDiff updateSlides(
            List<BannerGroupUpdateData.SlideEntry> entries, Instant now) {
        this.updatedAt = now;
        return computeSlideDiff(entries, now);
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
