# PERSISTENCE Layer 코딩 규칙 (18개)

## 개요

- **총 규칙 수**: 18개
- **Zero-Tolerance**: 5개
- **일반 규칙**: 13개

## 요약 테이블

| Code | Name | Severity | Category | Zero-Tolerance |
|------|------|----------|----------|----------------|
| EMAP-002 | Pure Java (Lombok/MapStruct 금지) | BLOCKER | ANNOTATION | 🚨 |
| EMAP-003 | 시간 필드 생성 금지 (Instant.now() 금지) | BLOCKER | BEHAVIOR | 🚨 |
| QADP-001 | QueryDslRepository 위임만 | BLOCKER | BEHAVIOR | 🚨 |
| QADP-002 | QueryAdapter에서 @Transactional 금지 | BLOCKER | ANNOTATION | 🚨 |
| QADP-008 | QueryAdapter에 비즈니스 로직 금지 | BLOCKER | BEHAVIOR | 🚨 |
| EMAP-004 | toEntity(Domain) 메서드 필수 | CRITICAL | STRUCTURE |  |
| EMAP-005 | toDomain(Entity) 메서드 필수 | CRITICAL | STRUCTURE |  |
| EMAP-009 | 단방향 변환 (순환 변환 금지) | CRITICAL | BEHAVIOR |  |
| QADP-006 | Domain 반환 (DTO 반환 금지) | CRITICAL | STRUCTURE |  |
| QADP-007 | Entity → Domain 변환 (Mapper 사용) | CRITICAL | BEHAVIOR |  |
| EMAP-001 | @Component 어노테이션 사용 | MAJOR | ANNOTATION |  |
| EMAP-006 | Entity.of() 호출 | MAJOR | BEHAVIOR |  |
| EMAP-007 | Domain.reconstitute() 호출 | MAJOR | BEHAVIOR |  |
| EMAP-008 | Null 안전 처리 | MAJOR | BEHAVIOR |  |
| QADP-003 | @Component 어노테이션 사용 | MAJOR | ANNOTATION |  |
| QADP-004 | Query Port 구현 | MAJOR | STRUCTURE |  |
| QADP-005 | Mapper + QueryDslRepository 의존 | MAJOR | DEPENDENCY |  |
| QADP-009 | 4개 표준 메서드 패턴 | MAJOR | STRUCTURE |  |

---

## 상세 규칙


### BLOCKER 규칙

#### EMAP-002: Pure Java (Lombok/MapStruct 금지) 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: ANNOTATION
- **Description**: EntityMapper는 순수 Java로 구현합니다. @Mapper(MapStruct), @Data, @Builder 등 모든 코드 생성 라이브러리 사용을 금지합니다.
- **Rationale**: 변환 로직의 명시적 제어. 바이트코드 생성 도구의 예측 불가능한 동작을 방지하고, 디버깅과 유지보수가 용이합니다.

#### EMAP-003: 시간 필드 생성 금지 (Instant.now() 금지) 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: BEHAVIOR
- **Description**: Mapper에서 Instant.now(), LocalDateTime.now(), System.currentTimeMillis() 등 시간 필드를 직접 생성하면 안 됩니다. 시간은 Domain에서 주입받거나 JPA Auditing(@CreatedDate, @LastModifiedDate)으로 처리합니다.
- **Rationale**: 테스트 가능성과 결정론적 동작 보장. 시간 의존성을 외부에서 주입받아 테스트 시 고정 시간 사용이 가능합니다.

#### QADP-001: QueryDslRepository 위임만 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: BEHAVIOR
- **Description**: QueryAdapter는 QueryDslRepository로 조회를 위임하고 Mapper로 변환하는 역할만 수행합니다. 직접 JPAQueryFactory를 사용하거나 쿼리 로직을 포함하면 안 됩니다.
- **Rationale**: 책임 분리. Repository는 쿼리 실행, Mapper는 변환, Adapter는 조합만 담당합니다. 각 컴포넌트의 단일 책임을 유지합니다.

#### QADP-002: QueryAdapter에서 @Transactional 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: ANNOTATION
- **Description**: QueryAdapter 클래스 및 메서드에서 @Transactional 어노테이션을 사용하면 안 됩니다. 읽기 전용 트랜잭션도 Application Layer에서 @Transactional(readOnly = true)로 관리합니다.
- **Rationale**: 트랜잭션 경계를 비즈니스 로직 계층에서 일관되게 제어. Adapter는 기술적 변환만 담당합니다.

#### QADP-008: QueryAdapter에 비즈니스 로직 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: BEHAVIOR
- **Description**: QueryAdapter에 if/switch 조건 분기, 필터링 로직, 정렬 로직 등 비즈니스 로직을 포함하면 안 됩니다. 단순히 Repository 호출 후 Mapper 변환만 수행합니다.
- **Rationale**: Adapter는 기술적 변환만 담당. 조회 조건과 정렬은 Repository에서, 비즈니스 필터링은 Application Layer에서 처리합니다.


### CRITICAL 규칙

#### EMAP-004: toEntity(Domain) 메서드 필수
- **Severity**: CRITICAL
- **Category**: STRUCTURE
- **Description**: EntityMapper는 Entity toEntity(Domain domain) 메서드를 필수로 구현합니다. Domain → Entity 변환 로직을 캡슐화합니다.
- **Rationale**: Domain을 Entity로 변환하여 JPA 영속화에 사용. Entity.of()를 호출하여 Entity를 생성합니다.

#### EMAP-005: toDomain(Entity) 메서드 필수
- **Severity**: CRITICAL
- **Category**: STRUCTURE
- **Description**: EntityMapper는 Domain toDomain(Entity entity) 메서드를 필수로 구현합니다. Entity → Domain 변환 로직을 캡슐화합니다.
- **Rationale**: Entity를 Domain으로 변환하여 Application Layer에 전달. Domain.reconstitute()를 호출하여 Domain을 복원합니다.

#### EMAP-009: 단방향 변환 (순환 변환 금지)
- **Severity**: CRITICAL
- **Category**: BEHAVIOR
- **Description**: toEntity()와 toDomain()은 각각 단방향 변환만 수행합니다. toDomain() 내부에서 toEntity()를 호출하거나 그 반대의 순환 호출을 금지합니다.
- **Rationale**: 무한 루프와 스택 오버플로우 방지. 각 변환 메서드는 독립적으로 동작하며, 서로를 호출하지 않습니다.

#### QADP-006: Domain 반환 (DTO 반환 금지)
- **Severity**: CRITICAL
- **Category**: STRUCTURE
- **Description**: QueryAdapter 메서드는 Domain 객체(Aggregate, Value Object)를 반환합니다. Entity나 DTO를 직접 반환하면 안 됩니다. Domain으로 변환하여 Application Layer에 전달합니다.
- **Rationale**: 계층 간 의존성 격리. Application Layer는 Domain만 사용하고, Entity는 Persistence Layer 내부에 캡슐화됩니다.

#### QADP-007: Entity → Domain 변환 (Mapper 사용)
- **Severity**: CRITICAL
- **Category**: BEHAVIOR
- **Description**: Entity를 Domain으로 변환할 때 반드시 EntityMapper.toDomain()을 사용합니다. Adapter 내부에서 직접 new Domain()이나 Domain.reconstitute()를 호출하면 안 됩니다.
- **Rationale**: 변환 로직 중앙화. Mapper에서 변환 규칙을 관리하여 일관성을 보장하고, 변환 로직 변경 시 영향 범위를 최소화합니다.


### MAJOR 규칙

#### EMAP-001: @Component 어노테이션 사용
- **Severity**: MAJOR
- **Category**: ANNOTATION
- **Description**: EntityMapper는 @Component 어노테이션으로 Bean 등록합니다. Adapter에서 생성자 주입으로 의존성을 주입받아 사용합니다.
- **Rationale**: DI를 통한 Mapper 사용. Mock 주입으로 단위 테스트가 용이하며, Mapper 교체가 유연합니다.

#### EMAP-006: Entity.of() 호출
- **Severity**: MAJOR
- **Category**: BEHAVIOR
- **Description**: toEntity() 메서드에서 Entity 생성 시 Entity.of(...) 정적 팩토리 메서드를 호출합니다. new Entity() 직접 생성을 금지합니다.
- **Rationale**: Entity 생성 로직 중앙화. Entity의 불변식을 of() 메서드에서 보장하고, 생성자를 외부에 노출하지 않습니다.

#### EMAP-007: Domain.reconstitute() 호출
- **Severity**: MAJOR
- **Category**: BEHAVIOR
- **Description**: toDomain() 메서드에서 Domain 복원 시 Domain.reconstitute(...) 정적 팩토리 메서드를 호출합니다. new Domain()이나 forNew()는 신규 생성 전용입니다.
- **Rationale**: Domain의 신규 생성(forNew)과 복원(reconstitute)을 명확히 구분. 영속화된 데이터로부터 Domain을 복원할 때는 reconstitute()를 사용합니다.

#### EMAP-008: Null 안전 처리
- **Severity**: MAJOR
- **Category**: BEHAVIOR
- **Description**: Mapper 메서드에서 null 입력에 대한 안전한 처리를 구현합니다. null 입력 시 null 반환 또는 명시적 예외 발생. Optional 래핑된 입력도 처리합니다.
- **Rationale**: NPE 방지. Repository에서 Optional.empty()가 반환될 수 있으므로, Mapper도 null/empty 케이스를 안전하게 처리합니다.

#### QADP-003: @Component 어노테이션 사용
- **Severity**: MAJOR
- **Category**: ANNOTATION
- **Description**: QueryAdapter는 @Component 어노테이션으로 Bean 등록합니다. @Repository가 아닌 @Component를 사용하여 Adapter 역할을 명확히 합니다.
- **Rationale**: Hexagonal Architecture에서 Adapter는 @Component로 표시. 기술적 역할(영속성 어댑터)을 명확히 구분합니다.

#### QADP-004: Query Port 구현
- **Severity**: MAJOR
- **Category**: STRUCTURE
- **Description**: QueryAdapter는 Application Layer에서 정의한 Query Port(예: OrderQueryPort) 인터페이스를 구현합니다. Port 인터페이스와 1:1 매핑됩니다.
- **Rationale**: Ports & Adapters 패턴. Application은 Port 인터페이스에만 의존하고, Adapter가 구체적인 조회 로직을 구현합니다.

#### QADP-005: Mapper + QueryDslRepository 의존
- **Severity**: MAJOR
- **Category**: DEPENDENCY
- **Description**: QueryAdapter는 EntityMapper와 QueryDslRepository를 생성자 주입으로 의존합니다. Repository로 Entity 조회 후 Mapper로 Domain 변환합니다.
- **Rationale**: 조회(Repository)와 변환(Mapper)의 책임 분리. 각 컴포넌트는 단일 책임을 가지며, 테스트와 유지보수가 용이합니다.

#### QADP-009: 4개 표준 메서드 패턴
- **Severity**: MAJOR
- **Category**: STRUCTURE
- **Description**: QueryAdapter는 findById(Id), existsById(Id), findByCriteria(Criteria), countByCriteria(Criteria) 4개 메서드를 기본 패턴으로 제공합니다. 필요 시 추가 메서드를 정의할 수 있습니다.
- **Rationale**: 일관된 조회 패턴 제공. 단건 조회, 존재 여부, 목록 조회, 개수 조회가 기본 Query 패턴입니다.

