package com.ryuqq.setof.adapter.out.persistence.seller.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.BaseAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * SellerCsInfoJpaEntity - Seller CS Info JPA Entity
 *
 * <p>Persistence Layer의 JPA Entity로서 seller_cs_infos 테이블과 매핑됩니다.
 *
 * <p><strong>BaseAuditEntity 상속:</strong>
 *
 * <ul>
 *   <li>공통 감사 필드 상속: createdAt, updatedAt
 *   <li>Soft Delete 미적용 (Seller 삭제 시 함께 삭제)
 * </ul>
 *
 * <p><strong>Long FK 전략:</strong>
 *
 * <ul>
 *   <li>sellerId: Long 타입으로 FK 관리
 *   <li>JPA 관계 어노테이션 사용 금지
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
@Table(name = "seller_cs_infos")
public class SellerCsInfoJpaEntity extends BaseAuditEntity {

    /** 기본 키 (Auto Increment) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /** 셀러 ID (Long FK) */
    @Column(name = "seller_id", nullable = false, unique = true)
    private Long sellerId;

    /** CS 이메일 */
    @Column(name = "email", length = 100)
    private String email;

    /** CS 휴대폰 */
    @Column(name = "mobile_phone", length = 20)
    private String mobilePhone;

    /** CS 유선전화 */
    @Column(name = "landline_phone", length = 20)
    private String landlinePhone;

    /**
     * JPA 기본 생성자 (protected)
     *
     * <p>JPA 스펙 요구사항으로 반드시 필요합니다.
     */
    protected SellerCsInfoJpaEntity() {
        // JPA 기본 생성자
    }

    /**
     * 전체 필드 생성자 (private)
     *
     * <p>직접 호출 금지, of() 스태틱 메서드로만 생성하세요.
     */
    private SellerCsInfoJpaEntity(
            Long id,
            Long sellerId,
            String email,
            String mobilePhone,
            String landlinePhone,
            Instant createdAt,
            Instant updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.sellerId = sellerId;
        this.email = email;
        this.mobilePhone = mobilePhone;
        this.landlinePhone = landlinePhone;
    }

    /**
     * of() 스태틱 팩토리 메서드 (Mapper 전용)
     *
     * <p>Entity 생성은 반드시 이 메서드를 통해서만 가능합니다.
     *
     * @param id CS Info ID (null이면 신규 생성)
     * @param sellerId 셀러 ID
     * @param email CS 이메일
     * @param mobilePhone CS 휴대폰
     * @param landlinePhone CS 유선전화
     * @param createdAt 생성 일시
     * @param updatedAt 수정 일시
     * @return SellerCsInfoJpaEntity 인스턴스
     */
    public static SellerCsInfoJpaEntity of(
            Long id,
            Long sellerId,
            String email,
            String mobilePhone,
            String landlinePhone,
            Instant createdAt,
            Instant updatedAt) {
        return new SellerCsInfoJpaEntity(
                id, sellerId, email, mobilePhone, landlinePhone, createdAt, updatedAt);
    }

    // ===== Getters (Setter 제공 금지) =====

    public Long getId() {
        return id;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public String getEmail() {
        return email;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public String getLandlinePhone() {
        return landlinePhone;
    }
}
