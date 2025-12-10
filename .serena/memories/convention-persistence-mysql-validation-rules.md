# Persistence MySQL Layer Validation Rules Index

> 필요한 카테고리만 선택적으로 읽어서 토큰을 절약하세요.

## 개요

- **Layer**: Persistence (MySQL)
- **총 카테고리**: 10개
- **총 룰**: 214개
- **버전**: 2.0.0
- **문서 출처**: `docs/coding_convention/04-persistence-layer/mysql/`

---

## 카테고리 인덱스

### 🗄️ Entity
| 파일 | 카테고리 | 룰 수 | 용도 |
|-----|---------|------|------|
| `persistence-rules-01-entity.md` | ENTITY | 27 | JPA Entity 구조, Lombok/JPA관계 금지 |

### 📚 Repository
| 파일 | 카테고리 | 룰 수 | 용도 |
|-----|---------|------|------|
| `persistence-rules-02-jpa-repository.md` | JPA_REPOSITORY | 16 | JpaRepository 인터페이스 규칙 |
| `persistence-rules-03-querydsl-repository.md` | QUERYDSL_REPOSITORY | 24 | 일반 조회용 QueryDSL (4개 메서드, Join 금지) |
| `persistence-rules-04-admin-querydsl-repository.md` | ADMIN_QUERYDSL_REPOSITORY | 15 | 관리자 조회용 (Join 허용, DTO Projection) |
| `persistence-rules-05-lock-repository.md` | LOCK_REPOSITORY | 19 | Lock 조회용 (FOR UPDATE/SHARE) |

### 🔄 Mapper
| 파일 | 카테고리 | 룰 수 | 용도 |
|-----|---------|------|------|
| `persistence-rules-06-mapper.md` | MAPPER | 24 | Entity ↔ Domain 변환 |

### 🔌 Adapter
| 파일 | 카테고리 | 룰 수 | 용도 |
|-----|---------|------|------|
| `persistence-rules-07-command-adapter.md` | COMMAND_ADAPTER | 22 | CUD 전용 (persist만) |
| `persistence-rules-08-query-adapter.md` | QUERY_ADAPTER | 26 | 일반 조회 (4개 메서드) |
| `persistence-rules-09-admin-query-adapter.md` | ADMIN_QUERY_ADAPTER | 17 | 관리자 조회 (DTO 반환) |
| `persistence-rules-10-lock-query-adapter.md` | LOCK_QUERY_ADAPTER | 24 | Lock 조회 (6개 메서드) |

---

## 🏗️ 아키텍처 개요

```
┌─────────────────────────────────────────────────────────────────┐
│                    Persistence MySQL Layer                       │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  ┌──────────────┐     ┌──────────────┐     ┌──────────────┐    │
│  │   Adapter    │     │  Repository  │     │    Entity    │    │
│  ├──────────────┤     ├──────────────┤     ├──────────────┤    │
│  │ Command      │────▶│ JpaRepository│────▶│ *JpaEntity   │    │
│  │ Adapter      │     │              │     │              │    │
│  └──────────────┘     └──────────────┘     │ Lombok 금지   │    │
│                                            │ Long FK 전략  │    │
│  ┌──────────────┐     ┌──────────────┐     └──────────────┘    │
│  │   Query      │────▶│ QueryDsl     │                         │
│  │  Adapter     │     │ Repository   │     ┌──────────────┐    │
│  │ (4개 메서드) │     │ (Join 금지)  │     │   Mapper     │    │
│  └──────────────┘     └──────────────┘     ├──────────────┤    │
│                                            │ toEntity()   │    │
│  ┌──────────────┐     ┌──────────────┐     │ toDomain()   │    │
│  │ AdminQuery   │────▶│ AdminQueryDsl│     │ Entity.of()  │    │
│  │  Adapter     │     │ Repository   │     └──────────────┘    │
│  │ (DTO 반환)   │     │ (Join 허용)  │                         │
│  └──────────────┘     └──────────────┘                         │
│                                                                  │
│  ┌──────────────┐     ┌──────────────┐                         │
│  │  LockQuery   │────▶│    Lock      │                         │
│  │  Adapter     │     │ Repository   │                         │
│  │ (6개 메서드) │     │ (FOR UPDATE) │                         │
│  └──────────────┘     └──────────────┘                         │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## Zero-Tolerance 규칙 요약

### 1. Lombok 완전 금지 (9개 어노테이션)
- `@Data`, `@Getter`, `@Setter`, `@Builder`, `@Value`
- `@AllArgsConstructor`, `@NoArgsConstructor`, `@RequiredArgsConstructor`, `@UtilityClass`
- **적용 대상**: Entity, Mapper

### 2. JPA 관계 어노테이션 금지 (Long FK 전략)
- `@ManyToOne`, `@OneToMany`, `@OneToOne`, `@ManyToMany`
- **대안**: `private Long customerId;` (Long FK)

### 3. @Transactional 금지
- Repository, Adapter 모두에서 금지
- **트랜잭션 관리**: Application Layer (UseCase)에서만

### 4. 1:1 매핑 원칙
- 각 Adapter는 **하나의 Repository에만** 의존
- 필드 **2개만** 허용 (Repository + Mapper)

### 5. 메서드 제한
- **JPA Repository**: 빈 인터페이스 (기본 메서드만)
- **QueryDSL Repository**: 4개 고정 (findById, existsById, findByCriteria, countByCriteria)
- **Query Adapter**: 4개 고정
- **Lock Query Adapter**: 6개 고정

### 6. Join 금지 (일반 QueryDSL)
- Join은 **AdminQueryDslRepository에서만** 허용
- N+1 해결은 Application Layer에서

---

## 컴포넌트별 빠른 참조

### Entity
- `@Entity`, `@Table`, `*JpaEntity` 네이밍
- `protected` 기본 생성자, `private` all-args 생성자
- `public static of()` 팩토리 메서드
- Getter만, Setter 금지

### Repository
| 타입 | 상속/어노테이션 | 메서드 | Join |
|------|----------------|--------|------|
| JpaRepository | `extends JpaRepository<E, Long>` | 기본만 | N/A |
| QueryDslRepository | `@Repository` class | 4개 | ❌ |
| AdminQueryDslRepository | `@Repository` class | 자유 | ✅ |
| LockRepository | `@Repository` class | Lock만 | N/A |

### Adapter
| 타입 | 필드 | 메서드 | 반환 |
|------|------|--------|------|
| CommandAdapter | 2개 (JpaRepo + Mapper) | 1개 (persist) | *Id |
| QueryAdapter | 2개 (QueryDsl + Mapper) | 4개 | Domain |
| AdminQueryAdapter | 1-2개 (AdminQueryDsl) | 자유 | DTO |
| LockQueryAdapter | 2개 (LockRepo + Mapper) | 6개 | Domain |

---

## 사용법

```
# 인덱스 확인
read_memory("convention-persistence-mysql-validation-rules.md")

# 필요한 룰만 선택적으로 읽기
read_memory("persistence-rules-01-entity.md")              # Entity 룰
read_memory("persistence-rules-02-jpa-repository.md")      # JPA Repository 룰
read_memory("persistence-rules-03-querydsl-repository.md") # QueryDSL Repository 룰
read_memory("persistence-rules-04-admin-querydsl-repository.md") # Admin QueryDSL 룰
read_memory("persistence-rules-05-lock-repository.md")     # Lock Repository 룰
read_memory("persistence-rules-06-mapper.md")              # Mapper 룰
read_memory("persistence-rules-07-command-adapter.md")     # Command Adapter 룰
read_memory("persistence-rules-08-query-adapter.md")       # Query Adapter 룰
read_memory("persistence-rules-09-admin-query-adapter.md") # Admin Query Adapter 룰
read_memory("persistence-rules-10-lock-query-adapter.md")  # Lock Query Adapter 룰
read_memory("persistence-rules-11-testing.md.md")  # MYSQL Testing 룰

```

---

## 관련 문서

- **Redis 규칙**: `convention-persistence-redis-validation-rules.md`
- **REST API 규칙**: `convention-rest-api-layer-validation-rules.md`
- **Application 규칙**: `convention-application-layer-validation-rules.md`
- **Domain 규칙**: `convention-domain-layer-validation-rules.md`

---

**총 규칙 수**: 214개
**작성일**: 2025-12-08
**버전**: 2.0.0
