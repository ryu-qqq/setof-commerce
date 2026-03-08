package com.ryuqq.setof.storage.legacy.qna.entity;

import com.ryuqq.setof.storage.legacy.common.Yn;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * LegacyQnaOrderEntity - 레거시 Q&A-주문 매핑 엔티티.
 *
 * <p>레거시 DB의 qna_order 테이블 매핑.
 *
 * <p>PER-ENT-001: 엔티티는 JPA 표준 어노테이션만 사용.
 *
 * <p>PER-ENT-003: Lombok 사용 금지 (Zero-Tolerance).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "qna_order")
public class LegacyQnaOrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qna_order_id")
    private Long id;

    @Column(name = "qna_id")
    private Long qnaId;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "delete_yn")
    @Enumerated(EnumType.STRING)
    private Yn deleteYn;

    @Column(name = "insert_operator")
    private String insertOperator;

    @Column(name = "update_operator")
    private String updateOperator;

    @Column(name = "insert_date")
    private LocalDateTime insertDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    protected LegacyQnaOrderEntity() {}

    public static LegacyQnaOrderEntity create(
            Long qnaId,
            Long orderId,
            Yn deleteYn,
            String insertOperator,
            String updateOperator,
            LocalDateTime insertDate,
            LocalDateTime updateDate) {
        LegacyQnaOrderEntity entity = new LegacyQnaOrderEntity();
        entity.qnaId = qnaId;
        entity.orderId = orderId;
        entity.deleteYn = deleteYn;
        entity.insertOperator = insertOperator;
        entity.updateOperator = updateOperator;
        entity.insertDate = insertDate;
        entity.updateDate = updateDate;
        return entity;
    }

    public Long getId() {
        return id;
    }

    public Long getQnaId() {
        return qnaId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Yn getDeleteYn() {
        return deleteYn;
    }

    public String getInsertOperator() {
        return insertOperator;
    }

    public String getUpdateOperator() {
        return updateOperator;
    }

    public LocalDateTime getInsertDate() {
        return insertDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }
}
