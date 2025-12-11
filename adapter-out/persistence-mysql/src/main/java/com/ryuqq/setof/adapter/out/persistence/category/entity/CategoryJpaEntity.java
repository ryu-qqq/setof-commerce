package com.ryuqq.setof.adapter.out.persistence.category.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.BaseAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * CategoryJpaEntity - Category JPA Entity
 *
 * <p>Persistence Layer의 JPA Entity로서 category 테이블과 매핑됩니다.
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
@Table(name = "category")
public class CategoryJpaEntity extends BaseAuditEntity {

    /** 기본 키 (Auto Increment) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /** 카테고리 코드 */
    @Column(name = "code", nullable = false, length = 100, unique = true)
    private String code;

    /** 한글 카테고리명 */
    @Column(name = "name_ko", nullable = false, length = 255)
    private String nameKo;

    /** 부모 카테고리 ID (null이면 최상위) */
    @Column(name = "parent_id")
    private Long parentId;

    /** 깊이 (0=최상위, 1=중분류, 2=소분류...) */
    @Column(name = "depth", nullable = false)
    private Integer depth;

    /** Path Enumeration (예: "/1/5/23/") */
    @Column(name = "path", nullable = false, length = 500)
    private String path;

    /** 정렬 순서 (낮을수록 상위) */
    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    /** 리프 노드 여부 (하위 카테고리 없음) */
    @Column(name = "is_leaf", nullable = false)
    private boolean isLeaf;

    /** 상태 (ACTIVE, INACTIVE) */
    @Column(name = "status", nullable = false, length = 20)
    private String status;

    /**
     * JPA 기본 생성자 (protected)
     *
     * <p>JPA 스펙 요구사항으로 반드시 필요합니다.
     */
    protected CategoryJpaEntity() {
        // JPA 기본 생성자
    }

    /**
     * 전체 필드 생성자 (private)
     *
     * <p>직접 호출 금지, of() 스태틱 메서드로만 생성하세요.
     */
    private CategoryJpaEntity(
            Long id,
            String code,
            String nameKo,
            Long parentId,
            Integer depth,
            String path,
            int sortOrder,
            boolean isLeaf,
            String status,
            Instant createdAt,
            Instant updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.code = code;
        this.nameKo = nameKo;
        this.parentId = parentId;
        this.depth = depth;
        this.path = path;
        this.sortOrder = sortOrder;
        this.isLeaf = isLeaf;
        this.status = status;
    }

    /**
     * of() 스태틱 팩토리 메서드 (Mapper 전용)
     *
     * <p>Entity 생성은 반드시 이 메서드를 통해서만 가능합니다.
     *
     * @param id Category ID (null이면 신규 생성)
     * @param code 카테고리 코드
     * @param nameKo 한글 카테고리명
     * @param parentId 부모 카테고리 ID
     * @param depth 깊이
     * @param path 경로
     * @param sortOrder 정렬 순서
     * @param isLeaf 리프 노드 여부
     * @param status 상태
     * @param createdAt 생성 일시 (UTC 기준 Instant)
     * @param updatedAt 수정 일시 (UTC 기준 Instant)
     * @return CategoryJpaEntity 인스턴스
     */
    public static CategoryJpaEntity of(
            Long id,
            String code,
            String nameKo,
            Long parentId,
            Integer depth,
            String path,
            int sortOrder,
            boolean isLeaf,
            String status,
            Instant createdAt,
            Instant updatedAt) {
        return new CategoryJpaEntity(
                id, code, nameKo, parentId, depth, path, sortOrder, isLeaf, status, createdAt,
                updatedAt);
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

    public Long getParentId() {
        return parentId;
    }

    public Integer getDepth() {
        return depth;
    }

    public String getPath() {
        return path;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public String getStatus() {
        return status;
    }
}
