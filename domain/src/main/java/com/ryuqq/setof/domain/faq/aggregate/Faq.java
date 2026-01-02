package com.ryuqq.setof.domain.faq.aggregate;

import com.ryuqq.setof.domain.faq.exception.InvalidFaqStatusException;
import com.ryuqq.setof.domain.faq.vo.FaqCategoryCode;
import com.ryuqq.setof.domain.faq.vo.FaqContent;
import com.ryuqq.setof.domain.faq.vo.FaqId;
import com.ryuqq.setof.domain.faq.vo.FaqStatus;
import com.ryuqq.setof.domain.faq.vo.TopSetting;
import java.time.Instant;

/**
 * FAQ Aggregate Root
 *
 * <p>FAQ 도메인의 핵심 Aggregate Root입니다. 모든 FAQ 관련 비즈니스 로직은 이 클래스를 통해 처리됩니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java 사용
 *   <li>Private 생성자 + Static Factory
 *   <li>Law of Demeter - Getter 체이닝 금지 (Helper 메서드 제공)
 *   <li>Tell, Don't Ask - 상태 변경 메서드 제공
 * </ul>
 */
public class Faq {

    private final FaqId id;
    private final FaqCategoryCode categoryCode;
    private FaqContent content;
    private TopSetting topSetting;
    private int displayOrder;
    private FaqStatus status;
    private int viewCount;
    private final Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    /** Private 생성자 - Static Factory 통해서만 생성 */
    private Faq(
            FaqId id,
            FaqCategoryCode categoryCode,
            FaqContent content,
            TopSetting topSetting,
            int displayOrder,
            FaqStatus status,
            int viewCount,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        this.id = id;
        this.categoryCode = categoryCode;
        this.content = content;
        this.topSetting = topSetting;
        this.displayOrder = displayOrder;
        this.status = status;
        this.viewCount = viewCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    // ===== Static Factory Methods =====

    /**
     * 신규 FAQ 생성 (DRAFT 상태로 시작)
     *
     * @param categoryCode 카테고리 코드
     * @param content FAQ 내용
     * @param displayOrder 표시 순서
     * @param now 현재 시간
     * @return 새로운 Faq 인스턴스
     */
    public static Faq forNew(
            FaqCategoryCode categoryCode, FaqContent content, int displayOrder, Instant now) {
        return new Faq(
                FaqId.forNew(),
                categoryCode,
                content,
                TopSetting.notTop(),
                displayOrder,
                FaqStatus.DRAFT,
                0,
                now,
                now,
                null);
    }

    /**
     * DB에서 조회한 데이터로 FAQ 복원
     *
     * @param id FAQ ID
     * @param categoryCode 카테고리 코드
     * @param content FAQ 내용
     * @param topSetting 상단 노출 설정
     * @param displayOrder 표시 순서
     * @param status 상태
     * @param viewCount 조회수
     * @param createdAt 생성일시
     * @param updatedAt 수정일시
     * @param deletedAt 삭제일시
     * @return 복원된 Faq 인스턴스
     */
    public static Faq reconstitute(
            FaqId id,
            FaqCategoryCode categoryCode,
            FaqContent content,
            TopSetting topSetting,
            int displayOrder,
            FaqStatus status,
            int viewCount,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        return new Faq(
                id,
                categoryCode,
                content,
                topSetting,
                displayOrder,
                status,
                viewCount,
                createdAt,
                updatedAt,
                deletedAt);
    }

    // ===== Business Methods (Tell, Don't Ask) =====

    /**
     * FAQ 게시
     *
     * @param now 현재 시간
     * @throws InvalidFaqStatusException 게시할 수 없는 상태인 경우
     */
    public void publish(Instant now) {
        validateNotDeleted();
        if (!status.canPublish()) {
            throw InvalidFaqStatusException.cannotPublish(status);
        }
        if (status == FaqStatus.PUBLISHED) {
            throw InvalidFaqStatusException.alreadyPublished();
        }
        this.status = FaqStatus.PUBLISHED;
        this.updatedAt = now;
    }

    /**
     * FAQ 숨김 처리
     *
     * @param now 현재 시간
     * @throws InvalidFaqStatusException 숨김 처리할 수 없는 상태인 경우
     */
    public void hide(Instant now) {
        validateNotDeleted();
        if (!status.canHide()) {
            throw InvalidFaqStatusException.cannotHide(status);
        }
        if (status == FaqStatus.HIDDEN) {
            throw InvalidFaqStatusException.alreadyHidden();
        }
        this.status = FaqStatus.HIDDEN;
        this.updatedAt = now;
    }

    /**
     * 상단 노출 설정
     *
     * @param order 상단 순서
     * @param now 현재 시간
     */
    public void setTop(int order, Instant now) {
        validateNotDeleted();
        this.topSetting = topSetting.setTop(order);
        this.updatedAt = now;
    }

    /**
     * 상단 노출 해제
     *
     * @param now 현재 시간
     */
    public void unsetTop(Instant now) {
        validateNotDeleted();
        this.topSetting = topSetting.unsetTop();
        this.updatedAt = now;
    }

    /**
     * FAQ 내용 수정
     *
     * @param content 새로운 내용
     * @param now 현재 시간
     */
    public void updateContent(FaqContent content, Instant now) {
        validateNotDeleted();
        this.content = content;
        this.updatedAt = now;
    }

    /**
     * 표시 순서 변경
     *
     * @param displayOrder 새로운 표시 순서
     * @param now 현재 시간
     */
    public void updateDisplayOrder(int displayOrder, Instant now) {
        validateNotDeleted();
        if (displayOrder < 0) {
            throw new IllegalArgumentException("표시 순서는 0 이상이어야 합니다: " + displayOrder);
        }
        this.displayOrder = displayOrder;
        this.updatedAt = now;
    }

    /**
     * 카테고리 변경
     *
     * @param newCategoryCode 새로운 카테고리 코드
     * @param now 현재 시간
     * @return 카테고리가 변경된 새로운 Faq 인스턴스
     */
    public Faq changeCategory(FaqCategoryCode newCategoryCode, Instant now) {
        validateNotDeleted();
        return new Faq(
                this.id,
                newCategoryCode,
                this.content,
                this.topSetting,
                this.displayOrder,
                this.status,
                this.viewCount,
                this.createdAt,
                now,
                this.deletedAt);
    }

    /** 조회수 증가 */
    public void incrementViewCount() {
        validateNotDeleted();
        this.viewCount++;
    }

    /**
     * Soft Delete 처리
     *
     * @param now 현재 시간
     * @throws InvalidFaqStatusException 이미 삭제된 경우
     */
    public void softDelete(Instant now) {
        if (isDeleted()) {
            throw InvalidFaqStatusException.alreadyDeleted();
        }
        this.deletedAt = now;
        this.updatedAt = now;
    }

    // ===== Query Methods =====

    /**
     * 삭제 여부 확인
     *
     * @return 삭제되었으면 true
     */
    public boolean isDeleted() {
        return deletedAt != null;
    }

    /**
     * 표시 가능 여부 확인 (게시 상태이고 삭제되지 않음)
     *
     * @return 표시 가능하면 true
     */
    public boolean isDisplayable() {
        return !isDeleted() && status.isDisplayable();
    }

    /**
     * 상단 노출 여부 확인
     *
     * @return 상단 노출이면 true
     */
    public boolean isTop() {
        return topSetting.isTop();
    }

    // ===== Validation Helper =====

    private void validateNotDeleted() {
        if (isDeleted()) {
            throw InvalidFaqStatusException.alreadyDeleted();
        }
    }

    // ===== Law of Demeter Helper Methods =====

    /**
     * ID 값 반환 (Law of Demeter)
     *
     * @return FAQ ID 값 (Long)
     */
    public Long getIdValue() {
        return id.value();
    }

    /**
     * 카테고리 코드 값 반환 (Law of Demeter)
     *
     * @return 카테고리 코드 값 (String)
     */
    public String getCategoryCodeValue() {
        return categoryCode.value();
    }

    /**
     * 질문 반환 (Law of Demeter)
     *
     * @return 질문 문자열
     */
    public String getQuestion() {
        return content.question();
    }

    /**
     * 답변 반환 (Law of Demeter)
     *
     * @return 답변 문자열
     */
    public String getAnswer() {
        return content.answer();
    }

    /**
     * 상단 여부 반환 (Law of Demeter)
     *
     * @return 상단 노출 여부
     */
    public boolean getIsTop() {
        return topSetting.isTop();
    }

    /**
     * 상단 순서 반환 (Law of Demeter)
     *
     * @return 상단 순서
     */
    public int getTopOrder() {
        return topSetting.topOrder();
    }

    // ===== Standard Getters =====

    public FaqId getId() {
        return id;
    }

    public FaqCategoryCode getCategoryCode() {
        return categoryCode;
    }

    public FaqContent getContent() {
        return content;
    }

    public TopSetting getTopSetting() {
        return topSetting;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public FaqStatus getStatus() {
        return status;
    }

    public int getViewCount() {
        return viewCount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }
}
