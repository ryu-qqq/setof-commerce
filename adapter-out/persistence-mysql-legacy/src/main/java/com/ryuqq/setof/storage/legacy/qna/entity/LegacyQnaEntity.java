package com.ryuqq.setof.storage.legacy.qna.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * LegacyQnaEntity - 레거시 Q&A 엔티티.
 *
 * <p>레거시 DB의 qna 테이블 매핑.
 *
 * <p>PER-ENT-001: 엔티티는 JPA 표준 어노테이션만 사용.
 *
 * <p>PER-ENT-003: Lombok 사용 금지 (Zero-Tolerance).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "qna")
public class LegacyQnaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qna_id")
    private Long id;

    @Embedded private LegacyQnaContents qnaContents;

    @Column(name = "private_yn")
    @Enumerated(EnumType.STRING)
    private Yn privateYn;

    @Column(name = "qna_status")
    private String qnaStatus;

    @Column(name = "qna_type")
    private String qnaType;

    @Column(name = "qna_detail_type")
    private String qnaDetailType;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "seller_id")
    private Long sellerId;

    @Column(name = "user_type")
    private String userType;

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

    protected LegacyQnaEntity() {}

    public static LegacyQnaEntity create(
            LegacyQnaContents qnaContents,
            Yn privateYn,
            String qnaStatus,
            String qnaType,
            String qnaDetailType,
            Long userId,
            Long sellerId,
            String userType,
            Yn deleteYn,
            String insertOperator,
            String updateOperator,
            LocalDateTime insertDate,
            LocalDateTime updateDate) {
        LegacyQnaEntity entity = new LegacyQnaEntity();
        entity.qnaContents = qnaContents;
        entity.privateYn = privateYn;
        entity.qnaStatus = qnaStatus;
        entity.qnaType = qnaType;
        entity.qnaDetailType = qnaDetailType;
        entity.userId = userId;
        entity.sellerId = sellerId;
        entity.userType = userType;
        entity.deleteYn = deleteYn;
        entity.insertOperator = insertOperator;
        entity.updateOperator = updateOperator;
        entity.insertDate = insertDate;
        entity.updateDate = updateDate;
        return entity;
    }

    public static LegacyQnaEntity reconstitute(
            Long id,
            LegacyQnaContents qnaContents,
            Yn privateYn,
            String qnaStatus,
            String qnaType,
            String qnaDetailType,
            Long userId,
            Long sellerId,
            String userType,
            Yn deleteYn,
            String insertOperator,
            String updateOperator,
            LocalDateTime insertDate,
            LocalDateTime updateDate) {
        LegacyQnaEntity entity =
                create(
                        qnaContents,
                        privateYn,
                        qnaStatus,
                        qnaType,
                        qnaDetailType,
                        userId,
                        sellerId,
                        userType,
                        deleteYn,
                        insertOperator,
                        updateOperator,
                        insertDate,
                        updateDate);
        entity.id = id;
        return entity;
    }

    public Long getId() {
        return id;
    }

    public LegacyQnaContents getQnaContents() {
        return qnaContents;
    }

    public Yn getPrivateYn() {
        return privateYn;
    }

    public String getQnaStatus() {
        return qnaStatus;
    }

    public String getQnaType() {
        return qnaType;
    }

    public String getQnaDetailType() {
        return qnaDetailType;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public String getUserType() {
        return userType;
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

    /** Yn - Y/N 구분 Enum. */
    public enum Yn {
        Y,
        N
    }
}
