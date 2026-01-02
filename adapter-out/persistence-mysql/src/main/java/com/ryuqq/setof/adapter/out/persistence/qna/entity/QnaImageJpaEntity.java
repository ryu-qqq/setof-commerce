package com.ryuqq.setof.adapter.out.persistence.qna.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.BaseAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * QnaImageJpaEntity - QnA 이미지 JPA Entity
 *
 * <p>Persistence Layer의 JPA Entity로서 qna_images 테이블과 매핑됩니다.
 *
 * <p><strong>이미지 첨부 규칙:</strong>
 *
 * <ul>
 *   <li>ORDER 유형 QnA만 이미지 첨부 가능
 *   <li>최대 3장 제한
 *   <li>displayOrder로 순서 관리
 * </ul>
 *
 * <p><strong>Long FK 전략:</strong>
 *
 * <ul>
 *   <li>qnaId: QnA 외래키 (Long)
 *   <li>JPA 관계 어노테이션 금지
 * </ul>
 *
 * <p><strong>Lombok 금지:</strong>
 *
 * <ul>
 *   <li>Plain Java getter 사용
 *   <li>Setter 제공 금지
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Entity
@Table(name = "qna_images")
public class QnaImageJpaEntity extends BaseAuditEntity {

    /** 기본 키 (Auto Increment) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /** QnA ID (Long FK) */
    @Column(name = "qna_id", nullable = false)
    private Long qnaId;

    /** 이미지 URL */
    @Column(name = "image_url", nullable = false, length = 500)
    private String imageUrl;

    /** 표시 순서 (0부터 시작) */
    @Column(name = "display_order", nullable = false)
    private int displayOrder;

    /**
     * JPA 기본 생성자 (protected)
     *
     * <p>JPA 스펙 요구사항으로 반드시 필요합니다.
     */
    protected QnaImageJpaEntity() {
        // JPA 기본 생성자
    }

    /**
     * 전체 필드 생성자 (private)
     *
     * <p>직접 호출 금지, of() 스태틱 메서드로만 생성하세요.
     */
    private QnaImageJpaEntity(
            Long id,
            Long qnaId,
            String imageUrl,
            int displayOrder,
            Instant createdAt,
            Instant updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.qnaId = qnaId;
        this.imageUrl = imageUrl;
        this.displayOrder = displayOrder;
    }

    /**
     * of() 스태틱 팩토리 메서드 (Mapper 전용)
     *
     * <p>Entity 생성은 반드시 이 메서드를 통해서만 가능합니다.
     *
     * @param id 이미지 ID (null이면 신규 생성)
     * @param qnaId QnA ID
     * @param imageUrl 이미지 URL
     * @param displayOrder 표시 순서
     * @param createdAt 생성 일시
     * @param updatedAt 수정 일시
     * @return QnaImageJpaEntity 인스턴스
     */
    public static QnaImageJpaEntity of(
            Long id,
            Long qnaId,
            String imageUrl,
            int displayOrder,
            Instant createdAt,
            Instant updatedAt) {
        return new QnaImageJpaEntity(id, qnaId, imageUrl, displayOrder, createdAt, updatedAt);
    }

    // ===== Getters (Setter 제공 금지) =====

    public Long getId() {
        return id;
    }

    public Long getQnaId() {
        return qnaId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }
}
