package com.ryuqq.setof.storage.legacy.review.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * LegacyReviewEntity - 레거시 리뷰 엔티티.
 *
 * <p>레거시 DB의 review 테이블 매핑.
 *
 * <p>PER-ENT-001: 엔티티는 JPA 표준 어노테이션만 사용.
 *
 * <p>PER-ENT-003: Lombok 사용 금지 (Zero-Tolerance).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "review")
public class LegacyReviewEntity {

    @Id
    @Column(name = "review_id")
    private Long id;

    @Column(name = "product_group_id")
    private long productGroupId;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "order_id")
    private long orderId;

    @Column(name = "rating")
    private double rating;

    @Column(name = "content")
    private String content;

    @Column(name = "delete_yn")
    @Enumerated(EnumType.STRING)
    private Yn deleteYn;

    @Column(name = "insert_date")
    private LocalDateTime insertDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    protected LegacyReviewEntity() {}

    public Long getId() {
        return id;
    }

    public long getProductGroupId() {
        return productGroupId;
    }

    public long getUserId() {
        return userId;
    }

    public long getOrderId() {
        return orderId;
    }

    public double getRating() {
        return rating;
    }

    public String getContent() {
        return content;
    }

    public Yn getDeleteYn() {
        return deleteYn;
    }

    public LocalDateTime getInsertDate() {
        return insertDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    /** Yn - Y/N 구분 Enum. */
    public enum Yn {
        Y,
        N
    }
}
