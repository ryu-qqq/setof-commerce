package com.ryuqq.setof.storage.legacy.wishlist.entity;

import com.ryuqq.setof.storage.legacy.common.entity.LegacyBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * LegacyWishlistItemEntity - 레거시 찜 항목 엔티티.
 *
 * <p>레거시 DB의 user_favorite 테이블 매핑.
 *
 * <p>PER-ENT-001: 엔티티는 JPA 표준 어노테이션만 사용.
 *
 * <p>PER-ENT-003: Lombok 사용 금지 (Zero-Tolerance).
 *
 * <p>소프트 삭제 방식 - delete_yn 컬럼으로 삭제 여부 관리.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "user_favorite")
public class LegacyWishlistItemEntity extends LegacyBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_id")
    private Long id;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "product_group_id")
    private long productGroupId;

    @Column(name = "delete_yn", nullable = false)
    private String deleteYn;

    @Column(name = "INSERT_OPERATOR", nullable = false)
    private String insertOperator;

    @Column(name = "UPDATE_OPERATOR", nullable = false)
    private String updateOperator;

    protected LegacyWishlistItemEntity() {}

    public static LegacyWishlistItemEntity create(long userId, long productGroupId) {
        LegacyWishlistItemEntity entity = new LegacyWishlistItemEntity();
        entity.userId = userId;
        entity.productGroupId = productGroupId;
        entity.deleteYn = "N";
        entity.insertOperator = "SYSTEM";
        entity.updateOperator = "SYSTEM";
        LocalDateTime now = LocalDateTime.now();
        return new LegacyWishlistItemEntity(now, now, entity);
    }

    private LegacyWishlistItemEntity(
            LocalDateTime insertDate, LocalDateTime updateDate, LegacyWishlistItemEntity source) {
        super(insertDate, updateDate);
        this.id = source.id;
        this.userId = source.userId;
        this.productGroupId = source.productGroupId;
        this.deleteYn = source.deleteYn;
        this.insertOperator = source.insertOperator;
        this.updateOperator = source.updateOperator;
    }

    public static LegacyWishlistItemEntity reconstitute(
            Long id,
            long userId,
            long productGroupId,
            String deleteYn,
            LocalDateTime insertDate,
            LocalDateTime updateDate) {
        LegacyWishlistItemEntity entity = new LegacyWishlistItemEntity();
        entity.id = id;
        entity.userId = userId;
        entity.productGroupId = productGroupId;
        entity.deleteYn = deleteYn;
        entity.insertOperator = "SYSTEM";
        entity.updateOperator = "SYSTEM";
        return new LegacyWishlistItemEntity(insertDate, updateDate, entity);
    }

    public String getDeleteYn() {
        return deleteYn;
    }

    public Long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public long getProductGroupId() {
        return productGroupId;
    }
}
