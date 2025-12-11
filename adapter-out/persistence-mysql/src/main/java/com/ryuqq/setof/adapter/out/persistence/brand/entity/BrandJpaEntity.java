package com.ryuqq.setof.adapter.out.persistence.brand.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.BaseAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * BrandJpaEntity - Brand JPA Entity
 *
 * <p>Persistence Layer의 JPA Entity로서 brand 테이블과 매핑됩니다.
 *
 * <p><strong>BaseAuditEntity 상속:</strong>
 *
 * <ul>
 *   <li>공통 감사 필드 상속: createdAt, updatedAt
 *   <li>Soft Delete 미적용 (읽기 전용 데이터)
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
@Table(name = "brand")
public class BrandJpaEntity extends BaseAuditEntity {

    /** 기본 키 (Auto Increment) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /** 브랜드 코드 (MarketPlace와 동일 값 사용) */
    @Column(name = "code", nullable = false, length = 100, unique = true)
    private String code;

    /** 한글 브랜드명 */
    @Column(name = "name_ko", nullable = false, length = 255)
    private String nameKo;

    /** 영문 브랜드명 (선택) */
    @Column(name = "name_en", length = 255)
    private String nameEn;

    /** 로고 이미지 URL */
    @Column(name = "logo_url", length = 500)
    private String logoUrl;

    /** 상태 (ACTIVE, INACTIVE) */
    @Column(name = "status", nullable = false, length = 20)
    private String status;

    /**
     * JPA 기본 생성자 (protected)
     *
     * <p>JPA 스펙 요구사항으로 반드시 필요합니다.
     */
    protected BrandJpaEntity() {
        // JPA 기본 생성자
    }

    /**
     * 전체 필드 생성자 (private)
     *
     * <p>직접 호출 금지, of() 스태틱 메서드로만 생성하세요.
     */
    private BrandJpaEntity(
            Long id,
            String code,
            String nameKo,
            String nameEn,
            String logoUrl,
            String status,
            Instant createdAt,
            Instant updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.code = code;
        this.nameKo = nameKo;
        this.nameEn = nameEn;
        this.logoUrl = logoUrl;
        this.status = status;
    }

    /**
     * of() 스태틱 팩토리 메서드 (Mapper 전용)
     *
     * <p>Entity 생성은 반드시 이 메서드를 통해서만 가능합니다.
     *
     * @param id Brand ID (null이면 신규 생성)
     * @param code 브랜드 코드
     * @param nameKo 한글 브랜드명
     * @param nameEn 영문 브랜드명
     * @param logoUrl 로고 이미지 URL
     * @param status 상태
     * @param createdAt 생성 일시 (UTC 기준 Instant)
     * @param updatedAt 수정 일시 (UTC 기준 Instant)
     * @return BrandJpaEntity 인스턴스
     */
    public static BrandJpaEntity of(
            Long id,
            String code,
            String nameKo,
            String nameEn,
            String logoUrl,
            String status,
            Instant createdAt,
            Instant updatedAt) {
        return new BrandJpaEntity(id, code, nameKo, nameEn, logoUrl, status, createdAt, updatedAt);
    }

    // ===== Getters (Setter 제공 금지) =====

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getNameKo() {
        return nameKo;
    }

    public String getNameEn() {
        return nameEn;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public String getStatus() {
        return status;
    }
}
