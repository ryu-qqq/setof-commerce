package com.ryuqq.setof.domain.qna.aggregate;

import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.qna.id.QnaImageId;
import com.ryuqq.setof.domain.qna.vo.QnaImageInfo;
import java.time.Instant;

/**
 * QnaImage - Q&A 질문 이미지 엔티티.
 *
 * <p>Qna Aggregate의 하위 엔티티로, 질문에 첨부된 이미지를 나타냅니다.
 *
 * <p>DOM-CMN-001: 순수 자바 객체 (POJO).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class QnaImage {

    private final QnaImageId id;
    private final QnaImageInfo imageInfo;
    private final int displayOrder;
    private DeletionStatus deletionStatus;
    private final Instant createdAt;

    private QnaImage(
            QnaImageId id,
            QnaImageInfo imageInfo,
            int displayOrder,
            DeletionStatus deletionStatus,
            Instant createdAt) {
        this.id = id;
        this.imageInfo = imageInfo;
        this.displayOrder = displayOrder;
        this.deletionStatus = deletionStatus != null ? deletionStatus : DeletionStatus.active();
        this.createdAt = createdAt;
    }

    /** 신규 Q&A 이미지 생성. */
    public static QnaImage create(QnaImageInfo imageInfo, int displayOrder, Instant occurredAt) {
        return new QnaImage(
                QnaImageId.forNew(), imageInfo, displayOrder, DeletionStatus.active(), occurredAt);
    }

    /** 영속성 레이어에서 복원. */
    public static QnaImage reconstitute(
            QnaImageId id,
            QnaImageInfo imageInfo,
            int displayOrder,
            DeletionStatus deletionStatus,
            Instant createdAt) {
        return new QnaImage(id, imageInfo, displayOrder, deletionStatus, createdAt);
    }

    /** 소프트 삭제. */
    public void delete(Instant occurredAt) {
        this.deletionStatus = DeletionStatus.deletedAt(occurredAt);
    }

    public boolean isDeleted() {
        return deletionStatus.isDeleted();
    }

    public QnaImageId id() {
        return id;
    }

    public Long idValue() {
        return id.value();
    }

    public QnaImageInfo imageInfo() {
        return imageInfo;
    }

    public String imageUrl() {
        return imageInfo.imageUrl();
    }

    public int displayOrder() {
        return displayOrder;
    }

    public DeletionStatus deletionStatus() {
        return deletionStatus;
    }

    public Instant createdAt() {
        return createdAt;
    }
}
