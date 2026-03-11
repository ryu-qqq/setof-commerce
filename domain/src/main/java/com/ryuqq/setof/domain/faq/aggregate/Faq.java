package com.ryuqq.setof.domain.faq.aggregate;

import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.faq.id.FaqId;
import com.ryuqq.setof.domain.faq.vo.FaqContents;
import com.ryuqq.setof.domain.faq.vo.FaqDisplayOrder;
import com.ryuqq.setof.domain.faq.vo.FaqTitle;
import com.ryuqq.setof.domain.faq.vo.FaqType;
import java.time.Instant;

/**
 * Faq - FAQ Aggregate.
 *
 * <p>FAQ 정보를 표현합니다.
 *
 * <p>현재 조회 전용 도메인으로, reconstitute 팩토리 메서드만 제공합니다. 향후 CUD 추가 시 forNew(), 비즈니스 메서드를 확장하세요.
 *
 * <p>주요 도메인 규칙:
 *
 * <ul>
 *   <li>TOP 유형 FAQ는 topDisplayOrder로 정렬
 *   <li>일반 유형 FAQ는 displayOrder로 정렬
 * </ul>
 *
 * <p>DOM-AGG-001: static 팩토리 메서드 사용.
 *
 * <p>DOM-AGG-002: ID는 전용 ID VO(FaqId) 사용.
 *
 * <p>DOM-AGG-003: 시간 필드 Instant 타입.
 *
 * <p>DOM-AGG-004: Setter 금지.
 *
 * <p>DOM-AGG-008: 필수 도메인 값은 전용 VO 사용.
 *
 * <p>DOM-CMN-001: 순수 자바 객체 (POJO).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class Faq {

    private final FaqId id;
    private final FaqType faqType;
    private final FaqTitle title;
    private final FaqContents contents;
    private final FaqDisplayOrder displayOrder;
    private final Integer topDisplayOrder;
    private final DeletionStatus deletionStatus;
    private final Instant createdAt;
    private final Instant updatedAt;

    private Faq(
            FaqId id,
            FaqType faqType,
            FaqTitle title,
            FaqContents contents,
            FaqDisplayOrder displayOrder,
            Integer topDisplayOrder,
            DeletionStatus deletionStatus,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.faqType = faqType;
        this.title = title;
        this.contents = contents;
        this.displayOrder = displayOrder;
        this.topDisplayOrder = topDisplayOrder;
        this.deletionStatus = deletionStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // ========== Factory Methods ==========

    /**
     * 영속성 계층에서 엔티티 복원.
     *
     * @param id 식별자
     * @param faqType FAQ 유형
     * @param title FAQ 제목
     * @param contents FAQ 내용
     * @param displayOrder 표시 순서
     * @param topDisplayOrder 상단 고정 순서 (null이면 일반 FAQ)
     * @param deletionStatus 삭제 상태
     * @param createdAt 생성일시
     * @param updatedAt 수정일시
     * @return 복원된 Faq 인스턴스
     */
    public static Faq reconstitute(
            FaqId id,
            FaqType faqType,
            FaqTitle title,
            FaqContents contents,
            FaqDisplayOrder displayOrder,
            Integer topDisplayOrder,
            DeletionStatus deletionStatus,
            Instant createdAt,
            Instant updatedAt) {
        return new Faq(
                id,
                faqType,
                title,
                contents,
                displayOrder,
                topDisplayOrder,
                deletionStatus,
                createdAt,
                updatedAt);
    }

    // ========== Query Methods ==========

    /** 신규 생성 여부 확인. */
    public boolean isNew() {
        return id.isNew();
    }

    /** TOP FAQ 여부 확인. */
    public boolean isTop() {
        return faqType.isTop();
    }

    /** 상단 고정 순서가 있는지 확인. */
    public boolean hasTopDisplayOrder() {
        return topDisplayOrder != null;
    }

    // ========== Accessor Methods ==========

    public FaqId id() {
        return id;
    }

    public Long idValue() {
        return id.value();
    }

    public FaqType faqType() {
        return faqType;
    }

    public FaqTitle title() {
        return title;
    }

    public String titleValue() {
        return title.value();
    }

    public FaqContents contents() {
        return contents;
    }

    public String contentsValue() {
        return contents.value();
    }

    public FaqDisplayOrder displayOrder() {
        return displayOrder;
    }

    public int displayOrderValue() {
        return displayOrder.value();
    }

    public Integer topDisplayOrder() {
        return topDisplayOrder;
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
