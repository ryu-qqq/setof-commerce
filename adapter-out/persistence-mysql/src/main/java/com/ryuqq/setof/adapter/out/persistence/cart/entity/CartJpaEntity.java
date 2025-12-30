package com.ryuqq.setof.adapter.out.persistence.cart.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.BaseAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * CartJpaEntity - Cart JPA Entity
 *
 * <p>Persistence Layer의 JPA Entity로서 carts 테이블과 매핑됩니다.
 *
 * <p><strong>Long FK 전략:</strong>
 *
 * <ul>
 *   <li>JPA 관계 어노테이션 금지 (@OneToMany, @ManyToOne 등)
 *   <li>자식 엔티티(CartItem) 조회는 별도 Repository 사용
 * </ul>
 *
 * <p><strong>Lombok 금지:</strong>
 *
 * <ul>
 *   <li>Plain Java getter 사용
 *   <li>Setter 제공 금지
 *   <li>명시적 생성자 제공
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Entity
@Table(name = "carts")
public class CartJpaEntity extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "member_id", nullable = false, unique = true, length = 36)
    private String memberId;

    protected CartJpaEntity() {
        // JPA 기본 생성자
    }

    private CartJpaEntity(Long id, String memberId, Instant createdAt, Instant updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.memberId = memberId;
    }

    /**
     * 신규 Entity 생성 (ID 미할당)
     *
     * @param memberId 회원 ID (UUID String)
     * @param createdAt 생성 시각
     * @param updatedAt 수정 시각
     * @return CartJpaEntity
     */
    public static CartJpaEntity forNew(String memberId, Instant createdAt, Instant updatedAt) {
        return new CartJpaEntity(null, memberId, createdAt, updatedAt);
    }

    /**
     * 영속화된 데이터로부터 복원
     *
     * @param id 장바구니 ID
     * @param memberId 회원 ID (UUID String)
     * @param createdAt 생성 시각
     * @param updatedAt 수정 시각
     * @return CartJpaEntity
     */
    public static CartJpaEntity of(Long id, String memberId, Instant createdAt, Instant updatedAt) {
        return new CartJpaEntity(id, memberId, createdAt, updatedAt);
    }

    public Long getId() {
        return id;
    }

    public String getMemberId() {
        return memberId;
    }
}
