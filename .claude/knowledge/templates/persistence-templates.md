# PERSISTENCE Layer 클래스 템플릿 (3개)

## 개요

이 문서는 PERSISTENCE Layer에서 사용하는 클래스 템플릿을 정의합니다.

## 템플릿 목록

| Class Type | Naming Pattern | Description |
|------------|----------------|-------------|
| ABSTRACT_CLASS | `BaseAuditEntity` | 모든 Entity의 공통 상위 클래스. JPA Auditing으로 createdAt/upd... |
| CLASS | `{Entity}JpaEntity` | BC별 JPA Entity. BaseAuditEntity 상속, Long FK 전략, JP... |
| INTERFACE | `{Entity}JpaRepository` | Command 전용 JPA Repository. save/delete만 사용, Query ... |

---

## 상세 템플릿

### ABSTRACT_CLASS

**설명**: 모든 Entity의 공통 상위 클래스. JPA Auditing으로 createdAt/updatedAt 자동 관리. Lombok 금지.

**네이밍 패턴**: `BaseAuditEntity`

**템플릿 코드**:
```java
package com.ryuqq.adapter.out.persistence.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

/**
 * BaseAuditEntity - 모든 Entity의 공통 상위 클래스
 *
 * <p>JPA Auditing을 통해 생성/수정 시간을 자동으로 관리합니다.
 *
 * <p><strong>설계 원칙:</strong>
 * <ul>
 *   <li>Lombok 금지 - Plain Java로 구현</li>
 *   <li>@MappedSuperclass - 테이블 생성 없이 상속</li>
 *   <li>Instant 타입 - 타임존 무관 UTC 시간</li>
 * </ul>
 *
 * @author ryu-qqq
 * @since {date}
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseAuditEntity {

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected BaseAuditEntity() {
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
```

---

### CLASS

**설명**: BC별 JPA Entity. BaseAuditEntity 상속, Long FK 전략, JPA 관계 어노테이션 금지, of() 팩토리.

**네이밍 패턴**: `{Entity}JpaEntity`

**템플릿 코드**:
```java
package com.ryuqq.adapter.out.persistence.{bc}.entity;

import com.ryuqq.adapter.out.persistence.common.entity.BaseAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * {Entity}JpaEntity - {Entity} JPA Entity
 *
 * <p>{Entity} Domain을 영속화하는 JPA Entity입니다.
 *
 * <p><strong>설계 원칙:</strong>
 * <ul>
 *   <li>BaseAuditEntity 상속 - createdAt/updatedAt 자동 관리</li>
 *   <li>Long FK 전략 - JPA 관계 어노테이션(@ManyToOne 등) 금지</li>
 *   <li>Lombok 금지 - Plain Java로 구현</li>
 *   <li>Setter 금지 - 불변 객체 설계</li>
 *   <li>of() 정적 팩토리 - 생성자 외부 노출 금지</li>
 * </ul>
 *
 * @author ryu-qqq
 * @since {date}
 */
@Entity
@Table(name = "{table_name}")
public class {Entity}JpaEntity extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "{entity}_id")
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private {Entity}Status status;

    // Long FK - JPA 관계 어노테이션 대신 Long 타입 사용
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * JPA 전용 기본 생성자
     */
    protected {Entity}JpaEntity() {
        super();
    }

    /**
     * 내부 생성자 - of() 팩토리에서만 호출
     */
    private {Entity}JpaEntity(
            Long id,
            String name,
            String description,
            {Entity}Status status,
            Long parentId) {
        super();
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.parentId = parentId;
    }

    /**
     * 정적 팩토리 메서드 - Entity 생성
     *
     * @param id Entity ID (신규 생성 시 null)
     * @param name 이름
     * @param description 설명
     * @param status 상태
     * @param parentId 부모 ID (없으면 null)
     * @return {Entity}JpaEntity 인스턴스
     */
    public static {Entity}JpaEntity of(
            Long id,
            String name,
            String description,
            {Entity}Status status,
            Long parentId) {
        return new {Entity}JpaEntity(id, name, description, status, parentId);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public {Entity}Status getStatus() {
        return status;
    }

    public Long getParentId() {
        return parentId;
    }
}
```

---

### INTERFACE

**설명**: Command 전용 JPA Repository. save/delete만 사용, Query Method/@Query 금지, CQRS 분리.

**네이밍 패턴**: `{Entity}JpaRepository`

**템플릿 코드**:
```java
package com.ryuqq.adapter.out.persistence.{bc}.repository.jpa;

import com.ryuqq.adapter.out.persistence.{bc}.entity.{Entity}JpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * {Entity}JpaRepository - {Entity} Command Repository
 *
 * <p>CQRS Command 전용 Repository입니다. 저장/삭제만 수행합니다.
 *
 * <p><strong>CQRS 분리 원칙:</strong>
 * <ul>
 *   <li>Command 전용: save(), delete() 만 사용</li>
 *   <li>Query Method 금지: findByXxx() 정의 금지</li>
 *   <li>@Query 금지: JPQL/Native Query 금지</li>
 *   <li>조회는 QueryDslRepository에서 수행</li>
 * </ul>
 *
 * <p><strong>금지 패턴:</strong>
 * <ul>
 *   <li>findByName(String name) - Query Method</li>
 *   <li>@Query("SELECT e FROM ...") - JPQL</li>
 *   <li>QuerydslPredicateExecutor 상속</li>
 * </ul>
 *
 * @author ryu-qqq
 * @since {date}
 */
public interface {Entity}JpaRepository extends JpaRepository<{Entity}JpaEntity, Long> {
    // Command 전용: 상속받은 save(), delete() 만 사용
    // 조회 메서드 정의 금지 - QueryDslRepository 사용
}
```

---

