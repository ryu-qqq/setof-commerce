# APPLICATION Layer 코딩 규칙 (103개)

## 개요

- **총 규칙 수**: 103개
- **Zero-Tolerance**: 48개
- **일반 규칙**: 55개

## 요약 테이블

| Code | Name | Severity | Category | Zero-Tolerance |
|------|------|----------|----------|----------------|
| C-001 | Lombok 사용 금지 | BLOCKER | ANNOTATION | 🚨 |
| C-002 | 변환기에서 null 체크 금지 | BLOCKER | BEHAVIOR | 🚨 |
| C-003 | 변환기에서 기본값 할당 금지 | BLOCKER | BEHAVIOR | 🚨 |
| C-004 | @Transactional은 Manager/Facade에서만 메서드 단위로 | BLOCKER | ANNOTATION | 🚨 |
| C-005 | Port(Out) 직접 주입 금지 | BLOCKER | DEPENDENCY | 🚨 |
| C-006 | 시간/ID 생성은 Factory에서만 | BLOCKER | BEHAVIOR | 🚨 |
| C-007 | 이벤트 객체 생성은 Domain에서 | BLOCKER | BEHAVIOR | 🚨 |
| C-009 | ApplicationEventPublisher 직접 사용 금지 | BLOCKER | DEPENDENCY | 🚨 |
| C-010 | 시간 조회는 TimeProvider 사용 | BLOCKER | BEHAVIOR | 🚨 |
| C-011 | ID 생성은 IdGeneratorPort 사용 | BLOCKER | BEHAVIOR | 🚨 |
| CDTO-001 | Command DTO는 Record로 정의 | BLOCKER | STRUCTURE | 🚨 |
| CDTO-006 | Command DTO에 Validation 어노테이션 금지 | BLOCKER | ANNOTATION | 🚨 |
| CDTO-007 | Command DTO는 Domain 타입 의존 금지 | BLOCKER | DEPENDENCY | 🚨 |
| CLPRT-001 | ClientPort는 반드시 interface로 정의 | BLOCKER | STRUCTURE | 🚨 |
| COMP-004 | Component는 Manager 의존 가능, Port 직접 의존 금지 | BLOCKER | DEPENDENCY | 🚨 |
| CPRT-001 | CommandPort는 반드시 interface로 정의 | BLOCKER | STRUCTURE | 🚨 |
| CPRT-002 | CommandPort는 persist(Domain) 메서드만 제공 | BLOCKER | STRUCTURE | 🚨 |
| CPRT-003 | CommandPort는 Domain 객체를 파라미터로 받음 | BLOCKER | BEHAVIOR | 🚨 |
| EVT-001 | EventListener는 외부 시스템 호출 전용 | BLOCKER | BEHAVIOR | 🚨 |
| EVT-002 | EventListener 처리 전 Outbox 저장 필수 | BLOCKER | BEHAVIOR | 🚨 |
| EVT-003 | EventListener 외부 호출 시 멱등키(outboxId) 필수 전달 | BLOCKER | BEHAVIOR | 🚨 |
| EVT-005 | EventListener는 @EventListener 사용 (@TransactionalEv... | BLOCKER | ANNOTATION | 🚨 |
| FAC-003 | Facade는 2개 이상 Manager 조합할 때만 사용 | BLOCKER | STRUCTURE | 🚨 |
| FAC-004 | Facade는 RDB Persistence Manager만 의존 | BLOCKER | DEPENDENCY | 🚨 |
| FAC-006 | Facade에 비즈니스 로직 금지 | BLOCKER | BEHAVIOR | 🚨 |
| QDTO-001 | Query DTO는 Record로 정의 | BLOCKER | STRUCTURE | 🚨 |
| QDTO-005 | Query DTO는 Domain 타입 의존 금지 | BLOCKER | DEPENDENCY | 🚨 |
| QPRT-001 | QueryPort는 반드시 interface로 정의 | BLOCKER | STRUCTURE | 🚨 |
| QPRT-004 | QueryPort는 Domain 객체 반환 (Entity/DTO 금지) | BLOCKER | BEHAVIOR | 🚨 |
| QPRT-005 | QueryPort의 복합 조건 조회는 Criteria 사용 | BLOCKER | STRUCTURE | 🚨 |
| RDTO-001 | Response DTO는 Record로 정의 | BLOCKER | STRUCTURE | 🚨 |
| RDTO-008 | Response DTO는 Domain 타입 의존 금지 | BLOCKER | DEPENDENCY | 🚨 |
| SCHS-003 | Application Layer에 @Scheduled 어노테이션 금지 | BLOCKER | ANNOTATION | 🚨 |
| SCHS-004 | Scheduler Service는 분산락(LockManager) 사용 필수 | BLOCKER | BEHAVIOR | 🚨 |
| SCHS-005 | Scheduler Service는 Manager를 통해 Port 사용 (Port 직접 주입... | BLOCKER | DEPENDENCY | 🚨 |
| SCHS-007 | Scheduler Service 외부 호출 시 멱등키(outboxId) 필수 전달 | BLOCKER | BEHAVIOR | 🚨 |
| SCHS-012 | Scheduler Service는 finally 블록에서 분산락 해제 | BLOCKER | BEHAVIOR | 🚨 |
| SVC-002 | UseCase(Port-In) 인터페이스 구현 필수 | BLOCKER | STRUCTURE | 🚨 |
| SVC-003 | Domain 객체 직접 생성 금지 | BLOCKER | BEHAVIOR | 🚨 |
| SVC-005 | Domain 객체 직접 반환 금지 | BLOCKER | BEHAVIOR | 🚨 |
| SVC-006 | Service에서 @Transactional 절대 금지 | BLOCKER | ANNOTATION | 🚨 |
| SVC-007 | Service에 비즈니스 로직 금지 | BLOCKER | BEHAVIOR | 🚨 |
| SVC-008 | Service에서 Port(Out) 직접 주입 금지 | BLOCKER | DEPENDENCY | 🚨 |
| SVC-009 | Service에서 시간/ID 생성 금지 | BLOCKER | BEHAVIOR | 🚨 |
| SVC-010 | Service에서 이벤트 객체 생성 금지 | BLOCKER | BEHAVIOR | 🚨 |
| UC-001 | UseCase는 반드시 interface로 정의 | BLOCKER | STRUCTURE | 🚨 |
| UC-002 | UseCase는 execute() 단일 메서드 제공 | BLOCKER | STRUCTURE | 🚨 |
| VAL-003 | Validator는 ReadManager만 의존 | BLOCKER | DEPENDENCY | 🚨 |
| C-008 | Tell, Don't Ask 원칙 준수 | CRITICAL | BEHAVIOR |  |
| SVC-004 | UseCase 간 직접 호출 금지 | CRITICAL | DEPENDENCY |  |
| BDL-001 | PersistBundle은 Class로 정의 (mutable) | MAJOR | STRUCTURE |  |
| BDL-002 | QueryBundle은 Record로 정의 (immutable) | MAJOR | STRUCTURE |  |
| BDL-003 | PersistBundle은 {Domain}PersistBundle 네이밍 | MAJOR | NAMING |  |
| BDL-004 | QueryBundle은 {Domain}QueryBundle 네이밍 | MAJOR | NAMING |  |
| BDL-005 | PersistBundle은 with{Id}() 패턴으로 ID 세팅 | MAJOR | BEHAVIOR |  |
| BDL-006 | Bundle은 ID Getter를 위임 메서드로 제공 (Law of Demeter) | MAJOR | BEHAVIOR |  |
| BDL-007 | Bundle은 Domain 객체를 직접 포함 | MAJOR | STRUCTURE |  |
| CDTO-002 | Command DTO 생성용은 Create{Domain}Command 네이밍 | MAJOR | NAMING |  |
| CDTO-003 | Command DTO 수정용은 Update{Domain}Command 네이밍 | MAJOR | NAMING |  |
| CDTO-004 | Update Command는 UpdateData 생성에 필요한 전체 필드 포함 | MAJOR | STRUCTURE |  |
| CDTO-005 | 상태 변경 전용 Command는 Change{Domain}StatusCommand로 분리 | MAJOR | STRUCTURE |  |
| CLPRT-002 | ClientPort는 외부 시스템 연동 전용 | MAJOR | BEHAVIOR |  |
| CLPRT-003 | ClientPort는 도메인 언어로 메서드 정의 | MAJOR | NAMING |  |
| CLPRT-004 | ClientPort는 실패 처리를 반환 타입으로 명시 | MAJOR | BEHAVIOR |  |
| COMP-001 | Component 패키지 클래스는 @Component 어노테이션 사용 | MAJOR | ANNOTATION |  |
| COMP-002 | Component는 {Domain}{Type} 네이밍 규칙 사용 | MAJOR | NAMING |  |
| COMP-003 | Component는 단일 책임 원칙 준수 | MAJOR | STRUCTURE |  |
| CPRT-004 | CommandPort의 persist는 ID를 반환 | MAJOR | BEHAVIOR |  |
| EVT-004 | EventListener는 @Async 어노테이션 사용 | MAJOR | ANNOTATION |  |
| EVT-006 | EventListener는 실패 허용, 예외 발생 시 로깅만 | MAJOR | BEHAVIOR |  |
| EVT-007 | EventListener는 ClientPort를 통해 외부 호출 | MAJOR | DEPENDENCY |  |
| EVT-008 | EventListener는 OutboxManager를 의존하여 상태 업데이트 | MAJOR | DEPENDENCY |  |
| FAC-001 | Facade는 @Component 어노테이션 사용 | MAJOR | ANNOTATION |  |
| FAC-002 | CommandFacade와 QueryFacade 분리 | MAJOR | STRUCTURE |  |
| FAC-005 | Facade는 @Transactional 메서드 단위 허용 | MAJOR | ANNOTATION |  |
| QDTO-002 | 단건 조회 Query는 Get{Domain}Query 네이밍 | MAJOR | NAMING |  |
| QDTO-003 | 목록 조회 Query는 Search{Domain}sQuery 네이밍 (복수형) | MAJOR | NAMING |  |
| QDTO-004 | 목록 조회 Query는 CommonSearchParams 포함 필수 | MAJOR | STRUCTURE |  |
| QPRT-002 | QueryPort는 표준 4개 메서드 필수 | MAJOR | STRUCTURE |  |
| RDTO-002 | 단건 상세 응답은 {Domain}Response 네이밍 | MAJOR | NAMING |  |
| RDTO-003 | 목록용 요약 응답은 {Domain}SummaryResponse 네이밍 | MAJOR | NAMING |  |
| RDTO-004 | 페이지 응답은 {Domain}PageResponse 네이밍 (PageMeta 포함) | MAJOR | NAMING |  |
| RDTO-005 | 슬라이스 응답은 {Domain}SliceResponse 네이밍 (SliceMeta 포함) | MAJOR | NAMING |  |
| RDTO-006 | 생성 결과 응답은 {Domain}IdResponse 네이밍 | MAJOR | NAMING |  |
| RDTO-007 | Response DTO는 createdAt, updatedAt 시간 필드 필수 포함 | MAJOR | STRUCTURE |  |
| SCHS-001 | Scheduler UseCase는 port/in/scheduler 패키지에 정의 | MAJOR | STRUCTURE |  |
| SCHS-002 | Scheduler Service는 service/scheduler 패키지에 구현 | MAJOR | STRUCTURE |  |
| SCHS-006 | Scheduler Service는 ReadManager로 PENDING 상태 Outbox ... | MAJOR | BEHAVIOR |  |
| SCHS-008 | Scheduler Service는 처리 결과에 따라 Outbox 상태 업데이트 | MAJOR | BEHAVIOR |  |
| SCHS-009 | Scheduler Service는 maxRetryCount 초과 시 FAILED 처리 | MAJOR | BEHAVIOR |  |
| SCHS-010 | Scheduler Service는 한 번에 batchSize 건씩 배치 처리 | MAJOR | BEHAVIOR |  |
| SCHS-011 | Scheduler Service는 건별 독립 트랜잭션으로 처리 | MAJOR | BEHAVIOR |  |
| SVC-001 | @Service 어노테이션 필수 | MAJOR | ANNOTATION |  |
| UC-003 | UseCase interface 내 inner class/interface 금지 | MAJOR | STRUCTURE |  |
| UC-004 | Command UseCase와 Query UseCase 분리 | MAJOR | STRUCTURE |  |
| UC-005 | UseCase는 하나의 비즈니스 액션만 담당 | MAJOR | BEHAVIOR |  |
| UC-006 | Command UseCase는 동사 접두어 + UseCase 네이밍 | MAJOR | NAMING |  |
| UC-007 | Query UseCase는 조회 접두어 + UseCase 네이밍 | MAJOR | NAMING |  |
| VAL-001 | Validator는 @Component 어노테이션 사용 | MAJOR | ANNOTATION |  |
| VAL-002 | Validator는 {Domain}Validator 네이밍 사용 | MAJOR | NAMING |  |
| VAL-004 | Validator는 void 반환, 실패 시 DomainException | MAJOR | BEHAVIOR |  |
| VAL-005 | Validator 메서드는 validateXxx() 또는 checkXxx() 사용 | MAJOR | NAMING |  |
| QPRT-003 | QueryPort는 Unique 필드 조회 조건부 허용 | MINOR | STRUCTURE |  |

---

## 상세 규칙


### BLOCKER 규칙

#### C-001: Lombok 사용 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: ANNOTATION
- **Description**: @Data, @Builder, @Getter, @Setter, @RequiredArgsConstructor 등 모든 Lombok 어노테이션 사용이 금지됩니다. 생성자 주입은 명시적으로 작성하고, Record는 Lombok 없이 사용합니다.
- **Rationale**: Pure Java 원칙. 컴파일 타임에 코드가 명확히 보이고, IDE 지원이 확실하며, 바이트코드 조작으로 인한 예측 불가능한 동작을 방지합니다.

#### C-002: 변환기에서 null 체크 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: BEHAVIOR
- **Description**: Factory, Assembler 등 변환기 클래스에서 입력값 null 체크를 하면 안 됩니다. null 검증은 REST-API Layer의 Mapper에서 이미 처리되었으므로, Application Layer에서 중복 검증은 불필요합니다.
- **Rationale**: Fail-fast 원칙. REST-API Mapper에서 null이 유효한 값인지 검증 후 전달합니다. null이 넘어오면 시스템 오류이므로 NullPointerException이 발생해야 합니다.

#### C-003: 변환기에서 기본값 할당 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: BEHAVIOR
- **Description**: Factory, Assembler 등 변환기에서 null을 기본값으로 대체하면 안 됩니다. (예: name != null ? name : "Unknown" 금지) null은 null 그대로 전달하고, 기본값 결정은 Domain 영역의 책임입니다.
- **Rationale**: 단일 책임 원칙. 변환기는 "형태 변환"만 담당하고, 비즈니스 규칙(기본값 정책)은 Domain에서 처리해야 합니다.

#### C-004: @Transactional은 Manager/Facade에서만 메서드 단위로 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: ANNOTATION
- **Description**: @Transactional 어노테이션은 오직 Manager(PersistenceManager, ReadManager)와 Facade(RDB Manager만 의존 시)에서만 사용합니다. Service, Factory, Assembler 등에서는 절대 금지입니다. 클래스 단위가 아닌 메서드 단위로만 사용합니다.
- **Rationale**: 트랜잭션 경계 명확화. 트랜잭션을 짧게 유지하여 외부 호출이 트랜잭션에 묶이는 것을 방지합니다. Spring 프록시 제약(private, final, 내부 호출 불가)도 고려합니다.

#### C-005: Port(Out) 직접 주입 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: DEPENDENCY
- **Description**: Service, Factory, Assembler 등에서 CommandPort, QueryPort를 직접 주입받으면 안 됩니다. 반드시 Manager(PersistenceManager, ReadManager)를 통해 래핑하여 사용해야 합니다.
- **Rationale**: 트랜잭션 경계 일관성. Port를 직접 사용하면 트랜잭션 없이 DB 접근이 발생할 수 있습니다. Manager로 래핑하여 모든 영속성 접근에 트랜잭션을 보장합니다.

#### C-006: 시간/ID 생성은 Factory에서만 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: BEHAVIOR
- **Description**: Instant.now(), LocalDateTime.now(), UUID.randomUUID() 등 시간이나 ID 생성 로직은 Factory에서만 허용됩니다. Service, Assembler 등 다른 컴포넌트에서는 금지됩니다.
- **Rationale**: 단일 책임 원칙. 시간/ID 생성을 Factory에 집중하여 테스트 용이성을 확보하고, TimeProvider/IdGeneratorPort를 통한 추상화가 가능합니다.

#### C-007: 이벤트 객체 생성은 Domain에서 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: BEHAVIOR
- **Description**: DomainEvent 객체를 new OrderCreatedEvent(...) 형태로 Service, Factory에서 직접 생성하면 안 됩니다. Domain Aggregate 내부에서 이벤트 객체를 생성하고, Application Layer는 이를 발행만 담당합니다.
- **Rationale**: DDD 원칙. 도메인 이벤트는 도메인 상태 변경의 결과물이므로, Aggregate가 자신의 상태 변경 시 이벤트를 생성하는 것이 자연스럽습니다.

#### C-009: ApplicationEventPublisher 직접 사용 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: DEPENDENCY
- **Description**: Spring의 ApplicationEventPublisher를 직접 주입받아 사용하면 안 됩니다. 반드시 TransactionEventRegistry를 통해 이벤트를 발행해야 합니다.
- **Rationale**: 트랜잭션 안전성. TransactionEventRegistry는 트랜잭션 유무에 따라 발행 시점을 자동 결정합니다. 트랜잭션이 있으면 커밋 후 발행, 없으면 즉시 발행합니다.

#### C-010: 시간 조회는 TimeProvider 사용 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: BEHAVIOR
- **Description**: Instant.now(), LocalDateTime.now(), System.currentTimeMillis() 등 시간 조회를 직접 호출하면 안 됩니다. TimeProvider 인터페이스를 주입받아 timeProvider.now()를 사용해야 합니다.
- **Rationale**: 테스트 용이성. TimeProvider를 Mock으로 교체하여 시간 의존 로직을 테스트할 수 있습니다. 또한 시스템 시간 정책(UTC vs KST)을 중앙에서 관리할 수 있습니다.

#### C-011: ID 생성은 IdGeneratorPort 사용 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: BEHAVIOR
- **Description**: UUID.randomUUID(), ULID, Snowflake 등 ID 생성 로직을 직접 호출하면 안 됩니다. IdGeneratorPort 인터페이스를 주입받아 idGenerator.generate()를 사용해야 합니다.
- **Rationale**: 추상화 및 테스트 용이성. ID 생성 전략(UUID v4, v7, ULID, Snowflake 등)을 런타임에 교체할 수 있고, 테스트에서 예측 가능한 ID를 생성할 수 있습니다.

#### CDTO-001: Command DTO는 Record로 정의 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: STRUCTURE
- **Description**: Command DTO는 반드시 Java Record로 정의합니다. class 사용을 금지합니다. Record는 불변성을 보장하고 equals/hashCode/toString을 자동 생성합니다.
- **Rationale**: 불변성 보장. Command는 생성 후 변경되지 않아야 하며, Record가 이를 자연스럽게 강제합니다.

#### CDTO-006: Command DTO에 Validation 어노테이션 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: ANNOTATION
- **Description**: Command DTO에 @Valid, @NotNull, @Size 등 Bean Validation 어노테이션을 사용하지 않습니다. 입력 검증은 REST-API Layer의 ApiRequest DTO에서 처리되어야 합니다.
- **Rationale**: 레이어 분리. 입력 검증은 Adapter-In 책임이며, Application Layer Command는 이미 검증된 데이터를 받습니다.

#### CDTO-007: Command DTO는 Domain 타입 의존 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: DEPENDENCY
- **Description**: Command DTO의 필드는 primitive 타입, String, 표준 Java 타입만 사용합니다. Domain의 VO나 ID 타입을 직접 사용하면 안 됩니다. (예: Long memberId ✅, MemberId memberId ❌)
- **Rationale**: 레이어 분리. Command는 외부 입력을 표현하며, Domain 타입 변환은 Factory 책임입니다.

#### CLPRT-001: ClientPort는 반드시 interface로 정의 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: STRUCTURE
- **Description**: ClientPort(Port-Out)는 반드시 interface로 정의합니다. class나 abstract class는 금지됩니다. External Adapter가 이 인터페이스를 구현합니다.
- **Rationale**: 헥사고날 아키텍처 원칙. Port-Out을 통해 외부 시스템 의존성을 추상화하고, Application Layer는 구체 구현에 의존하지 않습니다.

#### COMP-004: Component는 Manager 의존 가능, Port 직접 의존 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: DEPENDENCY
- **Description**: component에서 DB 조회가 필요하면 ReadManager를 의존합니다. CommandPort, QueryPort를 직접 의존하면 안 됩니다. Manager로 래핑하여 트랜잭션 일관성을 보장합니다.
- **Rationale**: 트랜잭션 일관성. Port를 직접 사용하면 트랜잭션 없이 DB 접근이 발생할 수 있습니다.

#### CPRT-001: CommandPort는 반드시 interface로 정의 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: STRUCTURE
- **Description**: CommandPort(Port-Out)는 반드시 interface로 정의합니다. class나 abstract class는 금지됩니다. Persistence Layer의 CommandAdapter가 이 인터페이스를 구현합니다.
- **Rationale**: 헥사고날 아키텍처 원칙. Port-Out을 통해 외부 의존성을 추상화하고, Application Layer는 구체 구현에 의존하지 않습니다.

#### CPRT-002: CommandPort는 persist(Domain) 메서드만 제공 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: STRUCTURE
- **Description**: CommandPort는 persist(Domain domain): {Id} 단일 메서드만 정의합니다. save, insert, create, update, delete 등 다른 네이밍은 금지됩니다. 삭제는 Domain의 soft-delete 상태 변경 후 persist로 처리합니다.
- **Rationale**: 단순화 및 일관성. persist는 JPA의 persist와 의미가 일치하며, INSERT/UPDATE를 추상화합니다.

#### CPRT-003: CommandPort는 Domain 객체를 파라미터로 받음 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: BEHAVIOR
- **Description**: persist() 메서드는 Domain 객체(Aggregate)를 파라미터로 받습니다. JPA Entity나 DTO를 직접 전달하면 안 됩니다. Entity 변환은 Adapter 내부(EntityMapper)에서 처리합니다.
- **Rationale**: 계층 분리. Application Layer는 Domain만 다루고, Entity는 Persistence Layer 내부에 캡슐화됩니다.

#### EVT-001: EventListener는 외부 시스템 호출 전용 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: BEHAVIOR
- **Description**: EventListener는 외부 시스템(결제 API, 알림 서비스 등) 호출만 담당합니다. 내부 도메인 간 통신에 이벤트를 사용하면 안 됩니다. 내부 도메인 연동은 Facade/Manager 조합으로 직접 처리합니다.
- **Rationale**: 명확한 경계. 이벤트는 시스템 외부 연동에만 사용하여 내부 복잡도를 낮추고 트레이싱을 용이하게 합니다.

#### EVT-002: EventListener 처리 전 Outbox 저장 필수 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: BEHAVIOR
- **Description**: 이벤트 발행 전에 반드시 Outbox에 저장되어야 합니다. Service에서 Facade를 통해 Domain + Outbox를 함께 저장한 후 이벤트를 발행합니다. Outbox는 EventListener 실패 시 Scheduler의 재처리를 보장합니다.
- **Rationale**: 신뢰성 보장. Outbox Pattern으로 이벤트 처리 실패 시에도 재처리가 가능합니다.

#### EVT-003: EventListener 외부 호출 시 멱등키(outboxId) 필수 전달 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: BEHAVIOR
- **Description**: 외부 API 호출 시 outboxId를 멱등키로 반드시 전달합니다. 동일한 outboxId로 재호출되어도 외부 시스템이 중복 처리하지 않도록 합니다. EventListener 실패 후 Scheduler 재시도 시에도 안전합니다.
- **Rationale**: 중복 방지. 네트워크 오류나 재시도 상황에서도 외부 시스템의 중복 처리를 방지합니다.

#### EVT-005: EventListener는 @EventListener 사용 (@TransactionalEventListener 금지) 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: ANNOTATION
- **Description**: @EventListener를 사용합니다. @TransactionalEventListener는 사용하지 않습니다. 현재 아키텍처에서 이벤트 발행 시점에 이미 트랜잭션이 종료되어 있으므로 TransactionalEventListener가 동작하지 않습니다.
- **Rationale**: 아키텍처 정합성. Service에서 Facade 호출 후 이벤트를 발행하므로 트랜잭션이 이미 커밋된 상태입니다.

#### FAC-003: Facade는 2개 이상 Manager 조합할 때만 사용 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: STRUCTURE
- **Description**: Facade는 2개 이상의 Manager를 조합할 때만 생성합니다. 단일 Manager 호출만 필요하면 Service에서 Manager를 직접 사용해야 합니다. 불필요한 Facade는 복잡도만 증가시킵니다.
- **Rationale**: 적절한 추상화 수준. Facade의 존재 이유는 여러 Manager의 조합이므로, 단일 Manager 호출은 Facade 없이 처리합니다.

#### FAC-004: Facade는 RDB Persistence Manager만 의존 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: DEPENDENCY
- **Description**: CommandFacade는 RDB PersistenceManager만 의존해야 합니다. 외부 API Manager(ClientPort 래퍼), Cache Manager, Messaging Manager 등은 Facade에서 호출하면 안 됩니다. 외부 호출이 필요하면 Service 레벨에서 처리합니다.
- **Rationale**: 트랜잭션 경계 명확화. RDB Manager만 의존하면 하나의 DB 트랜잭션으로 묶을 수 있습니다. 외부 호출이 트랜잭션에 묶이면 롤백 불가, 타임아웃 증가 등 문제가 발생합니다.

#### FAC-006: Facade에 비즈니스 로직 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: BEHAVIOR
- **Description**: Facade에서 if/switch를 사용한 비즈니스 규칙, 계산 로직, 상태 판단 로직을 포함하면 안 됩니다. Facade는 여러 Manager 호출을 조합하는 역할만 수행하고, 비즈니스 로직은 Domain에 위임합니다.
- **Rationale**: 단일 책임 원칙. Facade는 "조합"만 담당하고 비즈니스 규칙은 Domain에서 처리하여 관심사를 분리합니다.

#### QDTO-001: Query DTO는 Record로 정의 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: STRUCTURE
- **Description**: Query DTO는 반드시 Java Record로 정의합니다. class 사용을 금지합니다. Query는 조회 조건을 담는 불변 객체입니다.
- **Rationale**: 불변성 보장. Query는 조회 요청을 표현하며, 생성 후 변경되지 않아야 합니다.

#### QDTO-005: Query DTO는 Domain 타입 의존 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: DEPENDENCY
- **Description**: Query DTO의 필드는 primitive 타입, String, 표준 Java 타입만 사용합니다. Domain의 VO나 ID 타입을 직접 사용하면 안 됩니다. Domain 타입 변환은 QueryFactory에서 Criteria 생성 시 처리합니다.
- **Rationale**: 레이어 분리. Query는 외부 입력을 표현하며, Domain 타입 변환은 Factory 책임입니다.

#### QPRT-001: QueryPort는 반드시 interface로 정의 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: STRUCTURE
- **Description**: QueryPort(Port-Out)는 반드시 interface로 정의합니다. class나 abstract class는 금지됩니다. Persistence Layer의 QueryAdapter가 이 인터페이스를 구현합니다.
- **Rationale**: 헥사고날 아키텍처 원칙. Port-Out을 통해 외부 의존성을 추상화하고, Application Layer는 구체 구현에 의존하지 않습니다.

#### QPRT-004: QueryPort는 Domain 객체 반환 (Entity/DTO 금지) 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: BEHAVIOR
- **Description**: QueryPort의 조회 메서드는 Domain 객체(Aggregate, Entity(Domain), VO)를 반환해야 합니다. JPA Entity나 DTO를 직접 반환하면 안 됩니다. Entity→Domain 변환은 Adapter 내부에서 처리합니다.
- **Rationale**: 계층 분리. Application Layer는 Domain만 다루고, JPA Entity는 Persistence Layer 내부에 캡슐화됩니다.

#### QPRT-005: QueryPort의 복합 조건 조회는 Criteria 사용 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: STRUCTURE
- **Description**: findByXxxAndYyy(), findByStatusAndDate() 형태의 복합 조건 메서드는 금지됩니다. 복합 조건은 Criteria VO를 사용하여 findByCriteria(Criteria)로 처리합니다. Criteria는 Domain Layer에 정의됩니다.
- **Rationale**: 인터페이스 폭발 방지. 조건 조합마다 메서드를 추가하면 메서드 수가 폭발합니다. Criteria로 동적 조회를 지원합니다.

#### RDTO-001: Response DTO는 Record로 정의 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: STRUCTURE
- **Description**: Response DTO는 반드시 Java Record로 정의합니다. class 사용을 금지합니다. Response는 조회 결과를 담는 불변 객체입니다.
- **Rationale**: 불변성 보장. Response는 외부로 전달되는 데이터이며, 생성 후 변경되지 않아야 합니다.

#### RDTO-008: Response DTO는 Domain 타입 의존 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: DEPENDENCY
- **Description**: Response DTO의 필드는 primitive 타입, String, 표준 Java 타입만 사용합니다. Domain의 VO나 ID 타입을 직접 노출하면 안 됩니다. Domain → Response 변환은 Assembler에서 값 추출하여 처리합니다.
- **Rationale**: 레이어 분리. Response는 외부 API 계약이며, Domain 내부 타입이 노출되면 결합도가 높아집니다.

#### SCHS-003: Application Layer에 @Scheduled 어노테이션 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: ANNOTATION
- **Description**: Application Layer의 Scheduler Service에는 @Scheduled 어노테이션을 사용하지 않습니다. @Scheduled는 adapter-in/scheduler에서만 사용합니다. Application Layer는 순수 비즈니스 로직만 포함합니다.
- **Rationale**: Thin Scheduler 패턴. 스케줄링 트리거는 adapter-in에서, 비즈니스 로직은 Application Layer에서 분리합니다.

#### SCHS-004: Scheduler Service는 분산락(LockManager) 사용 필수 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: BEHAVIOR
- **Description**: Scheduler Service 실행 시작 시 LockManager를 통해 분산락을 획득해야 합니다. 다중 인스턴스 환경에서 동일 작업이 중복 실행되는 것을 방지합니다. 락 획득 실패 시 즉시 리턴합니다.
- **Rationale**: 중복 방지. 여러 서버에서 동일 Scheduler가 동시에 실행되면 데이터 정합성 문제가 발생합니다.

#### SCHS-005: Scheduler Service는 Manager를 통해 Port 사용 (Port 직접 주입 금지) 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: DEPENDENCY
- **Description**: Scheduler Service는 Port(ClientPort 등)를 직접 주입받지 않습니다. 반드시 Manager(ClientManager)를 통해 외부 호출을 수행합니다. 예: PaymentClientPort 대신 PaymentClientManager를 주입받습니다.
- **Rationale**: C-005 규칙 준수. Port 직접 주입은 금지되며, Manager가 Port를 캡슐화하여 일관된 호출 정책을 적용합니다.

#### SCHS-007: Scheduler Service 외부 호출 시 멱등키(outboxId) 필수 전달 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: BEHAVIOR
- **Description**: Scheduler Service가 ClientManager를 통해 외부 API를 호출할 때 outboxId를 멱등키로 전달합니다. ClientManager가 실제 HTTP 헤더나 요청 파라미터로 멱등키를 설정합니다. EventListener에서 이미 성공했더라도 재호출이 안전합니다.
- **Rationale**: 중복 안전. 멱등키 덕분에 EventListener와 Scheduler가 동일 항목을 처리해도 문제없습니다.

#### SCHS-012: Scheduler Service는 finally 블록에서 분산락 해제 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: BEHAVIOR
- **Description**: 분산락 획득 후에는 반드시 try-finally 패턴을 사용하여 finally에서 락을 해제합니다. 예외 발생 시에도 락이 해제되어야 다음 실행 주기에 정상 동작합니다.
- **Rationale**: 데드락 방지. 예외로 인해 락이 해제되지 않으면 모든 인스턴스가 대기 상태가 됩니다.

#### SVC-002: UseCase(Port-In) 인터페이스 구현 필수 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: STRUCTURE
- **Description**: Service 클래스는 반드시 해당 UseCase 인터페이스를 구현해야 합니다. (예: CreateOrderService implements CreateOrderUseCase) UseCase 인터페이스 없이 Service만 존재하면 안 됩니다.
- **Rationale**: 헥사고날 아키텍처 원칙. Port-In을 통해 Application Layer 진입점을 추상화하고, Controller는 구체 클래스가 아닌 인터페이스에 의존합니다.

#### SVC-003: Domain 객체 직접 생성 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: BEHAVIOR
- **Description**: Service에서 new Order(), Order.forNew() 등 Domain 객체를 직접 생성하면 안 됩니다. 모든 Domain/UpdateData/Bundle 생성은 Factory에 위임해야 합니다.
- **Rationale**: 단일 책임 원칙. Domain 생성 로직(시간, ID 주입 등)을 Factory에 집중하여 Service는 흐름 제어만 담당합니다.

#### SVC-005: Domain 객체 직접 반환 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: BEHAVIOR
- **Description**: Service의 execute() 메서드에서 Domain 객체(Aggregate, Entity, VO)를 직접 반환하면 안 됩니다. 반드시 Assembler를 통해 Response DTO로 변환하여 반환해야 합니다.
- **Rationale**: 계층 분리. Domain 객체가 Controller까지 노출되면 캡슐화가 깨지고, JPA Entity인 경우 LazyLoading 문제도 발생합니다.

#### SVC-006: Service에서 @Transactional 절대 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: ANNOTATION
- **Description**: Service 클래스와 메서드에 @Transactional을 사용하면 안 됩니다. 트랜잭션 관리는 Manager/Facade의 책임입니다. Service는 비즈니스 흐름 조율만 담당합니다.
- **Rationale**: 트랜잭션 경계 명확화. 트랜잭션을 짧게 유지하여 외부 호출이 트랜잭션에 묶이는 것을 방지합니다.

#### SVC-007: Service에 비즈니스 로직 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: BEHAVIOR
- **Description**: Service에서 if/switch를 사용한 비즈니스 규칙, 계산 로직, 상태 판단 로직을 포함하면 안 됩니다. (예: if(order.isPaymentCompleted()) 금지) 모든 비즈니스 로직은 Domain에 위임합니다.
- **Rationale**: Tell, Don't Ask 원칙. Domain의 캡슐화를 유지하고, 비즈니스 규칙이 Domain에 집중되어 재사용성과 테스트 용이성이 향상됩니다.

#### SVC-008: Service에서 Port(Out) 직접 주입 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: DEPENDENCY
- **Description**: Service에서 CommandPort, QueryPort를 직접 주입받으면 안 됩니다. 반드시 PersistenceManager, ReadManager를 통해 접근해야 합니다.
- **Rationale**: 트랜잭션 일관성. Port를 직접 사용하면 트랜잭션 없이 DB 접근이 발생할 수 있습니다. Manager로 래핑하여 트랜잭션을 보장합니다.

#### SVC-009: Service에서 시간/ID 생성 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: BEHAVIOR
- **Description**: Service에서 Instant.now(), UUID.randomUUID() 등 시간이나 ID 생성 로직을 직접 호출하면 안 됩니다. 시간/ID 생성은 Factory의 책임입니다.
- **Rationale**: 단일 책임 원칙. 시간/ID 생성을 Factory에 집중하여 TimeProvider/IdGeneratorPort를 통한 테스트 용이성을 확보합니다.

#### SVC-010: Service에서 이벤트 객체 생성 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: BEHAVIOR
- **Description**: Service에서 new OrderCreatedEvent(...) 형태로 이벤트 객체를 직접 생성하면 안 됩니다. 이벤트는 Domain 내부에서 생성되고, Service는 TransactionEventRegistry로 발행만 담당합니다.
- **Rationale**: DDD 원칙. 도메인 이벤트는 Aggregate 상태 변경의 결과물이므로 Domain에서 생성해야 합니다.

#### UC-001: UseCase는 반드시 interface로 정의 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: STRUCTURE
- **Description**: UseCase(Port-In)는 반드시 interface로 정의합니다. class나 abstract class는 금지됩니다. Service가 이 인터페이스를 구현합니다.
- **Rationale**: 헥사고날 아키텍처 원칙. Port-In을 통해 Application Layer 진입점을 추상화하고, Controller는 구체 클래스가 아닌 인터페이스에 의존합니다.

#### UC-002: UseCase는 execute() 단일 메서드 제공 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: STRUCTURE
- **Description**: UseCase 인터페이스는 execute(Command) 또는 execute(Query) 단일 메서드만 정의합니다. 여러 메서드를 정의하면 안 됩니다. (예: execute(CreateOrderCommand): OrderIdResponse)
- **Rationale**: 단일 책임 원칙. 하나의 UseCase = 하나의 비즈니스 액션. 메서드가 여러 개 필요하면 별도 UseCase로 분리합니다.

#### VAL-003: Validator는 ReadManager만 의존 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: DEPENDENCY
- **Description**: Validator는 검증을 위해 ReadManager(조회용)만 의존합니다. PersistenceManager(쓰기용)나 CommandPort/QueryPort를 직접 의존하면 안 됩니다.
- **Rationale**: 읽기 전용 검증. Validator는 상태 조회만 필요하고, 데이터 변경은 Service→Manager에서 처리합니다.


### CRITICAL 규칙

#### C-008: Tell, Don't Ask 원칙 준수
- **Severity**: CRITICAL
- **Category**: BEHAVIOR
- **Description**: Service에서 Domain의 상태를 조회(get)하여 판단 후 행동하는 패턴을 금지합니다. (예: if(order.getStatus() == PAID) { order.ship(); } 금지) 대신 order.processShipment() 형태로 Domain에 행동을 위임합니다.
- **Rationale**: 캡슐화 강화. 상태 판단 로직을 Domain 내부에 두어 비즈니스 규칙이 Domain에 집중되고, Service는 흐름 제어만 담당합니다.

#### SVC-004: UseCase 간 직접 호출 금지
- **Severity**: CRITICAL
- **Category**: DEPENDENCY
- **Description**: Service에서 다른 UseCase(Service)를 직접 주입받아 호출하면 안 됩니다. 순환 의존성이 발생할 수 있고, 트랜잭션 경계가 모호해집니다. Admin 조회 등 복합 조회가 필요한 경우 별도 Facade나 ReadManager 조합을 사용합니다.
- **Rationale**: 순환 의존성 방지 및 트랜잭션 경계 명확화. UseCase는 독립적인 비즈니스 액션을 나타내므로 서로 호출하지 않아야 합니다.


### MAJOR 규칙

#### BDL-001: PersistBundle은 Class로 정의 (mutable)
- **Severity**: MAJOR
- **Category**: STRUCTURE
- **Description**: PersistBundle은 ID Enrichment를 위해 Class로 정의합니다. Record가 아닌 Class를 사용하여 저장 후 ID를 세팅할 수 있어야 합니다. Domain 저장 → ID 획득 → Outbox에 ID 세팅 패턴에 필요합니다.
- **Rationale**: ID Enrichment 패턴. Facade에서 persist 후 획득한 ID를 Bundle 내 다른 객체에 전달해야 합니다.

#### BDL-002: QueryBundle은 Record로 정의 (immutable)
- **Severity**: MAJOR
- **Category**: STRUCTURE
- **Description**: QueryBundle은 조회 결과의 단순 묶음이므로 Record로 정의합니다. 조회 결과는 변경될 필요가 없으므로 불변 객체가 적합합니다.
- **Rationale**: 불변성 보장. 조회 결과 Bundle은 생성 후 변경되지 않아야 합니다.

#### BDL-003: PersistBundle은 {Domain}PersistBundle 네이밍
- **Severity**: MAJOR
- **Category**: NAMING
- **Description**: 저장용 Bundle은 {Domain}PersistBundle로 네이밍합니다. 예: OrderPersistBundle, PaymentPersistBundle. Domain + 관련 객체(Outbox 등)를 함께 묶어 저장합니다.
- **Rationale**: 네이밍 일관성. PersistBundle 접미어로 저장용 복합 객체임을 명확히 합니다.

#### BDL-004: QueryBundle은 {Domain}QueryBundle 네이밍
- **Severity**: MAJOR
- **Category**: NAMING
- **Description**: 조회 결과 Bundle은 {Domain}QueryBundle로 네이밍합니다. 예: OrderQueryBundle, MemberQueryBundle. 여러 Domain의 조회 결과를 함께 묶어 전달합니다.
- **Rationale**: 네이밍 일관성. QueryBundle 접미어로 조회 결과 복합 객체임을 명확히 합니다.

#### BDL-005: PersistBundle은 with{Id}() 패턴으로 ID 세팅
- **Severity**: MAJOR
- **Category**: BEHAVIOR
- **Description**: PersistBundle은 ID 세팅을 위해 with{Domain}Id() 메서드를 제공합니다. 예: withOrderId(orderId). 내부적으로 Domain의 withId() 메서드를 호출하여 ID를 세팅합니다.
- **Rationale**: ID Enrichment 캡슐화. Bundle이 내부 객체의 ID 세팅 로직을 캡슐화합니다.

#### BDL-006: Bundle은 ID Getter를 위임 메서드로 제공 (Law of Demeter)
- **Severity**: MAJOR
- **Category**: BEHAVIOR
- **Description**: Bundle은 orderId(), outboxId() 같은 위임 메서드를 제공합니다. bundle.order().id() 대신 bundle.orderId()를 사용합니다. Law of Demeter를 준수하여 내부 구조를 캡슐화합니다.
- **Rationale**: Law of Demeter. 메서드 체이닝을 방지하고 Bundle의 내부 구조 변경에 대한 영향을 최소화합니다.

#### BDL-007: Bundle은 Domain 객체를 직접 포함
- **Severity**: MAJOR
- **Category**: STRUCTURE
- **Description**: Bundle은 Domain 객체(Aggregate, Entity, Outbox 등)를 필드로 직접 포함합니다. DTO나 primitive 타입으로 분해하지 않습니다. order(), outbox() 같은 getter로 Domain 객체에 접근합니다.
- **Rationale**: Domain 중심 설계. Bundle은 Application Layer 내부에서만 사용되며, Domain 객체를 그대로 전달합니다.

#### CDTO-002: Command DTO 생성용은 Create{Domain}Command 네이밍
- **Severity**: MAJOR
- **Category**: NAMING
- **Description**: 생성 Command는 Create{Domain}Command 형태로 네이밍합니다. 예: CreateOrderCommand, CreateMemberCommand. Register, Add 등 다른 동사는 사용하지 않습니다.
- **Rationale**: 네이밍 일관성. 생성 작업임을 명확히 표현하고 CRUD의 C를 직관적으로 나타냅니다.

#### CDTO-003: Command DTO 수정용은 Update{Domain}Command 네이밍
- **Severity**: MAJOR
- **Category**: NAMING
- **Description**: 수정 Command는 Update{Domain}Command 형태로 네이밍합니다. 예: UpdateOrderCommand, UpdateMemberCommand. Modify, Edit 등 다른 동사는 사용하지 않습니다.
- **Rationale**: 네이밍 일관성. 수정 작업임을 명확히 표현하고 CRUD의 U를 직관적으로 나타냅니다.

#### CDTO-004: Update Command는 UpdateData 생성에 필요한 전체 필드 포함
- **Severity**: MAJOR
- **Category**: STRUCTURE
- **Description**: Update Command는 UpdateData VO 생성에 필요한 모든 필드를 포함해야 합니다. 부분 업데이트가 필요하면 null 허용 필드로 정의합니다. Factory에서 UpdateData로 변환됩니다.
- **Rationale**: 명시적 수정 범위. 어떤 필드가 수정 가능한지 Command 레벨에서 명확히 정의합니다.

#### CDTO-005: 상태 변경 전용 Command는 Change{Domain}StatusCommand로 분리
- **Severity**: MAJOR
- **Category**: STRUCTURE
- **Description**: 상태만 변경하는 경우 Update Command와 분리하여 Change{Domain}StatusCommand를 사용합니다. 예: ChangeOrderStatusCommand, ChangeMemberStatusCommand. 상태 변경은 별도 비즈니스 의미를 가집니다.
- **Rationale**: 단일 책임. 전체 수정과 상태 변경은 다른 비즈니스 의미를 가지므로 분리하여 관리합니다.

#### CLPRT-002: ClientPort는 외부 시스템 연동 전용
- **Severity**: MAJOR
- **Category**: BEHAVIOR
- **Description**: ClientPort는 HTTP API, gRPC, 메시징 시스템 등 외부 시스템과의 연동을 위해 사용합니다. 내부 DB 접근은 CommandPort/QueryPort를 사용합니다. 예: PaymentClientPort, NotificationClientPort.
- **Rationale**: 역할 분리. DB 접근과 외부 API 호출을 명확히 구분하여 장애 격리와 모니터링을 용이하게 합니다.

#### CLPRT-003: ClientPort는 도메인 언어로 메서드 정의
- **Severity**: MAJOR
- **Category**: NAMING
- **Description**: ClientPort 메서드는 외부 시스템 용어가 아닌 도메인 언어를 사용합니다. (예: requestPayment() ✅, callPgApi() ❌) 외부 시스템 세부사항(URL, 프로토콜)은 Adapter에 캡슐화합니다.
- **Rationale**: 도메인 중심 설계. Application Layer는 외부 시스템이 무엇인지 알 필요 없이 도메인 관점의 메서드만 호출합니다.

#### CLPRT-004: ClientPort는 실패 처리를 반환 타입으로 명시
- **Severity**: MAJOR
- **Category**: BEHAVIOR
- **Description**: 외부 호출 실패 가능성을 반환 타입으로 명시합니다. 필수 성공: throws DomainException, 실패 허용: Optional<Result> 또는 Result with isSuccess(). 암묵적 예외 발생은 지양합니다.
- **Rationale**: 명시적 실패 처리. 호출자가 실패 가능성을 인지하고 적절히 처리할 수 있도록 API 시그니처에 표현합니다.

#### COMP-001: Component 패키지 클래스는 @Component 어노테이션 사용
- **Severity**: MAJOR
- **Category**: ANNOTATION
- **Description**: component 패키지 하위의 모든 클래스(Validator, Processor, Calculator, Resolver 등)는 @Component 어노테이션을 사용합니다. @Service는 UseCase 구현체에만 사용합니다.
- **Rationale**: 역할 명확화. @Service는 비즈니스 로직 진입점, @Component는 내부 유틸리티성 컴포넌트를 의미합니다.

#### COMP-002: Component는 {Domain}{Type} 네이밍 규칙 사용
- **Severity**: MAJOR
- **Category**: NAMING
- **Description**: component 클래스는 {Domain}{Type} 형태로 네이밍합니다. 예: OrderValidator, PaymentProcessor, PriceCalculator, ShippingResolver. 타입명이 역할을 명확히 표현해야 합니다.
- **Rationale**: 일관된 네이밍. 클래스명만 보고도 역할과 담당 도메인을 파악할 수 있습니다.

#### COMP-003: Component는 단일 책임 원칙 준수
- **Severity**: MAJOR
- **Category**: STRUCTURE
- **Description**: component 클래스는 하나의 역할만 수행합니다. Validator는 검증만, Calculator는 계산만, Processor는 처리만 담당합니다. 여러 역할이 필요하면 별도 component로 분리합니다.
- **Rationale**: 단일 책임 원칙(SRP). 역할이 명확하면 테스트가 쉽고 재사용성이 높아집니다.

#### CPRT-004: CommandPort의 persist는 ID를 반환
- **Severity**: MAJOR
- **Category**: BEHAVIOR
- **Description**: persist(Domain domain) 메서드는 저장 후 생성된 ID를 반환해야 합니다. (예: OrderId persist(Order order)) void 반환은 금지됩니다. ID Enrichment 패턴에서 필요합니다.
- **Rationale**: 후속 작업 지원. Facade에서 ID Enrichment 패턴을 사용하여 Outbox에 Domain ID를 세팅하려면 persist 결과로 ID가 필요합니다.

#### EVT-004: EventListener는 @Async 어노테이션 사용
- **Severity**: MAJOR
- **Category**: ANNOTATION
- **Description**: EventListener 메서드는 @Async 어노테이션을 사용하여 별도 스레드에서 처리합니다. 외부 호출 지연이 메인 트랜잭션에 영향을 주지 않도록 비동기로 처리합니다.
- **Rationale**: 성능 격리. 외부 API 응답 지연이 사용자 요청 처리 시간에 영향을 주지 않습니다.

#### EVT-006: EventListener는 실패 허용, 예외 발생 시 로깅만
- **Severity**: MAJOR
- **Category**: BEHAVIOR
- **Description**: EventListener에서 외부 호출 실패 시 예외를 던지지 않고 로깅만 합니다. Outbox는 PENDING 상태로 유지되며, Scheduler가 주기적으로 재시도합니다. 실패해도 시스템 전체에 영향을 주지 않습니다.
- **Rationale**: 복원력. EventListener 실패가 메인 비즈니스에 영향을 주지 않고, Scheduler가 자동 복구합니다.

#### EVT-007: EventListener는 ClientPort를 통해 외부 호출
- **Severity**: MAJOR
- **Category**: DEPENDENCY
- **Description**: EventListener의 외부 시스템 호출은 ClientPort 인터페이스를 통해 수행합니다. HTTP 클라이언트나 외부 SDK를 직접 사용하지 않습니다. 예: PaymentClientPort, NotificationClientPort.
- **Rationale**: 의존성 역전. 외부 시스템 구현 세부사항을 Adapter에 캡슐화하고, 테스트 시 Mock 교체가 용이합니다.

#### EVT-008: EventListener는 OutboxManager를 의존하여 상태 업데이트
- **Severity**: MAJOR
- **Category**: DEPENDENCY
- **Description**: EventListener는 외부 호출 성공 시 OutboxPersistenceManager를 통해 Outbox 상태를 SUCCESS로 업데이트합니다. 상태 업데이트 실패 시에도 예외를 던지지 않습니다 (Scheduler가 처리).
- **Rationale**: Outbox 관리. 외부 호출 성공 시 Outbox 상태를 갱신하여 Scheduler의 불필요한 재시도를 방지합니다.

#### FAC-001: Facade는 @Component 어노테이션 사용
- **Severity**: MAJOR
- **Category**: ANNOTATION
- **Description**: Facade 클래스는 @Component 어노테이션을 사용합니다. @Service가 아닙니다. Facade는 UseCase 구현체가 아닌 Manager 조합을 위한 내부 컴포넌트입니다.
- **Rationale**: @Service는 UseCase 구현체에만 사용합니다. Facade는 여러 Manager를 조합하는 내부 컴포넌트이므로 @Component가 적합합니다.

#### FAC-002: CommandFacade와 QueryFacade 분리
- **Severity**: MAJOR
- **Category**: STRUCTURE
- **Description**: Facade는 Command용(쓰기)과 Query용(읽기)으로 분리해야 합니다. OrderCommandFacade, OrderQueryFacade 형태로 네이밍합니다. 하나의 Facade에 읽기/쓰기 메서드를 혼합하면 안 됩니다.
- **Rationale**: CQRS 원칙. Command와 Query를 분리하여 관심사를 명확히 하고, 각 Facade의 책임을 단순화합니다.

#### FAC-005: Facade는 @Transactional 메서드 단위 허용
- **Severity**: MAJOR
- **Category**: ANNOTATION
- **Description**: Facade는 RDB Manager만 의존하므로 @Transactional을 메서드 단위로 사용할 수 있습니다. 클래스 단위 @Transactional은 금지됩니다. CommandFacade는 @Transactional, QueryFacade는 @Transactional(readOnly=true)를 사용합니다.
- **Rationale**: RDB 전용 Facade이므로 트랜잭션 허용. 여러 Manager 호출이 하나의 트랜잭션으로 묶여 일부만 커밋되는 문제를 방지합니다.

#### QDTO-002: 단건 조회 Query는 Get{Domain}Query 네이밍
- **Severity**: MAJOR
- **Category**: NAMING
- **Description**: 단건 조회 Query는 Get{Domain}Query 형태로 네이밍합니다. 예: GetOrderQuery, GetMemberQuery. ID로 단건 조회하는 경우 사용합니다.
- **Rationale**: 네이밍 일관성. Get 접두어로 단건 조회임을 명확히 표현합니다.

#### QDTO-003: 목록 조회 Query는 Search{Domain}sQuery 네이밍 (복수형)
- **Severity**: MAJOR
- **Category**: NAMING
- **Description**: 목록 조회 Query는 Search{Domain}sQuery 형태로 네이밍합니다. 예: SearchOrdersQuery, SearchMembersQuery. 복수형 도메인명을 사용하여 목록 조회임을 명확히 합니다.
- **Rationale**: 네이밍 일관성. Search 접두어와 복수형으로 목록 조회임을 직관적으로 표현합니다.

#### QDTO-004: 목록 조회 Query는 CommonSearchParams 포함 필수
- **Severity**: MAJOR
- **Category**: STRUCTURE
- **Description**: 목록 조회 Query(Search*Query)는 CommonSearchParams를 필드로 포함해야 합니다. CommonSearchParams는 정렬(sortKey, sortDirection), 페이징(page, size), 삭제 포함 여부(includeDeleted) 정보를 담습니다.
- **Rationale**: 조회 표준화. 정렬과 페이징 정보를 일관된 방식으로 전달하여 QueryFactory에서 QueryContext로 변환합니다.

#### QPRT-002: QueryPort는 표준 4개 메서드 필수
- **Severity**: MAJOR
- **Category**: STRUCTURE
- **Description**: QueryPort는 다음 4개 표준 메서드를 필수로 정의합니다: findById(Id): Optional<Domain>, existsById(Id): boolean, findByCriteria(Criteria): List<Domain>, countByCriteria(Criteria): long. 이 4개 메서드로 대부분의 조회 요구사항을 충족합니다.
- **Rationale**: 인터페이스 일관성. 모든 QueryPort가 동일한 표준 메서드를 제공하여 예측 가능한 API를 보장합니다.

#### RDTO-002: 단건 상세 응답은 {Domain}Response 네이밍
- **Severity**: MAJOR
- **Category**: NAMING
- **Description**: 단건 상세 조회 응답은 {Domain}Response 형태로 네이밍합니다. 예: OrderResponse, MemberResponse. 해당 Domain의 모든 상세 정보를 포함합니다.
- **Rationale**: 네이밍 일관성. Response 접미어로 응답 객체임을 명확히 표현합니다.

#### RDTO-003: 목록용 요약 응답은 {Domain}SummaryResponse 네이밍
- **Severity**: MAJOR
- **Category**: NAMING
- **Description**: 목록 조회 시 각 항목의 요약 정보는 {Domain}SummaryResponse로 네이밍합니다. 예: OrderSummaryResponse, MemberSummaryResponse. 상세 정보보다 적은 필드만 포함합니다.
- **Rationale**: 성능 최적화. 목록 조회 시 필요한 최소 정보만 전달하여 네트워크와 처리 비용을 절감합니다.

#### RDTO-004: 페이지 응답은 {Domain}PageResponse 네이밍 (PageMeta 포함)
- **Severity**: MAJOR
- **Category**: NAMING
- **Description**: 페이지 기반 목록 응답은 {Domain}PageResponse로 네이밍합니다. content(목록)와 PageMeta(page, size, totalElements, totalPages)를 포함합니다. 관리자 화면 등 전체 개수가 필요한 경우 사용합니다.
- **Rationale**: 페이지네이션 표준화. PageMeta를 통해 일관된 페이지 정보를 제공합니다.

#### RDTO-005: 슬라이스 응답은 {Domain}SliceResponse 네이밍 (SliceMeta 포함)
- **Severity**: MAJOR
- **Category**: NAMING
- **Description**: 슬라이스 기반 목록 응답은 {Domain}SliceResponse로 네이밍합니다. content(목록)와 SliceMeta(size, hasNext, cursor, count)를 포함합니다. 무한 스크롤, 더보기 UI에 적합하며 COUNT 쿼리가 불필요합니다.
- **Rationale**: 성능 최적화. COUNT 쿼리 없이 다음 페이지 존재 여부만 확인하여 대용량 데이터에 효율적입니다.

#### RDTO-006: 생성 결과 응답은 {Domain}IdResponse 네이밍
- **Severity**: MAJOR
- **Category**: NAMING
- **Description**: 생성(Create) 작업의 응답은 {Domain}IdResponse로 네이밍합니다. 예: OrderIdResponse, MemberIdResponse. 생성된 엔티티의 ID를 반환합니다.
- **Rationale**: 명확한 응답. 생성 작업의 결과로 ID만 필요한 경우 경량 응답을 제공합니다.

#### RDTO-007: Response DTO는 createdAt, updatedAt 시간 필드 필수 포함
- **Severity**: MAJOR
- **Category**: STRUCTURE
- **Description**: Response DTO({Domain}Response, {Domain}SummaryResponse)는 createdAt과 updatedAt 필드를 모두 포함해야 합니다. 단, IdResponse는 제외합니다.
- **Rationale**: 감사 추적. 데이터의 생성 시점과 최종 수정 시점은 필수 정보입니다.

#### SCHS-001: Scheduler UseCase는 port/in/scheduler 패키지에 정의
- **Severity**: MAJOR
- **Category**: STRUCTURE
- **Description**: Scheduler UseCase 인터페이스는 port/in/scheduler 패키지에 정의합니다. port/in/command, port/in/query와 분리하여 스케줄러 전용 UseCase임을 명확히 합니다. 예: ProcessOrderOutboxUseCase, RetryFailedPaymentUseCase.
- **Rationale**: 관심사 분리. Command/Query와 별도로 Scheduler 전용 UseCase를 관리하여 역할을 명확히 합니다.

#### SCHS-002: Scheduler Service는 service/scheduler 패키지에 구현
- **Severity**: MAJOR
- **Category**: STRUCTURE
- **Description**: Scheduler UseCase의 구현체는 service/scheduler 패키지에 위치합니다. @Component 어노테이션을 사용하며, UseCase 인터페이스를 구현합니다. 예: ProcessOrderOutboxService.
- **Rationale**: 일관된 구조. service/command, service/query와 동일한 패턴으로 service/scheduler를 구성합니다.

#### SCHS-006: Scheduler Service는 ReadManager로 PENDING 상태 Outbox 조회
- **Severity**: MAJOR
- **Category**: BEHAVIOR
- **Description**: Scheduler Service는 OutboxReadManager를 통해 PENDING 상태의 Outbox 목록을 조회합니다. EventListener 실패로 처리되지 않은 항목들을 배치로 처리합니다. 조회 시 batchSize를 제한합니다.
- **Rationale**: Fallback 역할. EventListener 실패 시 Scheduler가 재처리하여 최종 일관성을 보장합니다.

#### SCHS-008: Scheduler Service는 처리 결과에 따라 Outbox 상태 업데이트
- **Severity**: MAJOR
- **Category**: BEHAVIOR
- **Description**: Scheduler Service는 외부 호출 성공 시 OutboxPersistenceManager를 통해 Outbox를 SUCCESS로, 실패 시 retryCount를 증가시킵니다. markSuccess(), incrementRetryCount() 메서드를 사용합니다.
- **Rationale**: 상태 추적. Outbox 상태를 통해 처리 현황을 모니터링하고 문제를 추적할 수 있습니다.

#### SCHS-009: Scheduler Service는 maxRetryCount 초과 시 FAILED 처리
- **Severity**: MAJOR
- **Category**: BEHAVIOR
- **Description**: Outbox의 retryCount가 maxRetryCount를 초과하면 FAILED 상태로 변경합니다. FAILED는 Dead Letter로 취급하여 수동 처리 또는 운영 알림이 필요합니다.
- **Rationale**: 무한 재시도 방지. 복구 불가능한 오류에 대해 무한 재시도하지 않고 운영팀에 알립니다.

#### SCHS-010: Scheduler Service는 한 번에 batchSize 건씩 배치 처리
- **Severity**: MAJOR
- **Category**: BEHAVIOR
- **Description**: Scheduler Service는 PENDING Outbox를 한 번에 전체 처리하지 않고 batchSize만큼씩 조회하여 처리합니다. 대량 미처리 건이 있어도 메모리와 시간을 제한하여 시스템 안정성을 유지합니다.
- **Rationale**: 리소스 관리. 대량 데이터 처리 시 메모리 부족이나 타임아웃을 방지합니다.

#### SCHS-011: Scheduler Service는 건별 독립 트랜잭션으로 처리
- **Severity**: MAJOR
- **Category**: BEHAVIOR
- **Description**: Scheduler Service는 각 Outbox 항목을 독립적인 트랜잭션으로 처리합니다. 하나의 항목 처리 실패가 다른 항목에 영향을 주지 않습니다. for 루프 내에서 try-catch로 각 건을 격리합니다.
- **Rationale**: 장애 격리. 한 건의 실패가 전체 배치를 롤백시키지 않아 나머지 항목이 정상 처리됩니다.

#### SVC-001: @Service 어노테이션 필수
- **Severity**: MAJOR
- **Category**: ANNOTATION
- **Description**: Service 클래스는 @Service 어노테이션을 사용해야 합니다. @Component는 사용하지 않습니다. Service는 UseCase(Port-In 인터페이스) 구현체임을 명시합니다.
- **Rationale**: Spring 컴포넌트 스캔 및 역할 명확화. @Service는 비즈니스 로직 계층임을 의미론적으로 표현합니다.

#### UC-003: UseCase interface 내 inner class/interface 금지
- **Severity**: MAJOR
- **Category**: STRUCTURE
- **Description**: UseCase 인터페이스 내부에 nested class나 nested interface를 정의하면 안 됩니다. Command/Query DTO는 dto 패키지에 별도 파일로 정의합니다.
- **Rationale**: 코드 조직화. DTO와 인터페이스를 분리하여 각각의 역할을 명확히 합니다.

#### UC-004: Command UseCase와 Query UseCase 분리
- **Severity**: MAJOR
- **Category**: STRUCTURE
- **Description**: Command UseCase(생성/수정/삭제)와 Query UseCase(조회)는 별도 인터페이스로 분리합니다. port.in.command와 port.in.query 패키지로 구분합니다.
- **Rationale**: CQRS 원칙. 쓰기와 읽기를 분리하여 각각의 최적화와 확장이 가능합니다.

#### UC-005: UseCase는 하나의 비즈니스 액션만 담당
- **Severity**: MAJOR
- **Category**: BEHAVIOR
- **Description**: 하나의 UseCase = 하나의 비즈니스 액션입니다. (예: CreateOrderUseCase, UpdateOrderUseCase, GetOrderUseCase) 여러 액션을 하나의 UseCase에 묶으면 안 됩니다.
- **Rationale**: 단일 책임 원칙. 역할이 명확하면 테스트가 쉽고, 변경 시 영향 범위가 최소화됩니다.

#### UC-006: Command UseCase는 동사 접두어 + UseCase 네이밍
- **Severity**: MAJOR
- **Category**: NAMING
- **Description**: Command UseCase는 Create/Update/Delete/Change/Register/Modify 접두어를 사용합니다. 예: CreateOrderUseCase, UpdateOrderUseCase, ChangeOrderStatusUseCase.
- **Rationale**: 네이밍 일관성. 접두어로 액션 유형을 명확히 표현합니다.

#### UC-007: Query UseCase는 조회 접두어 + UseCase 네이밍
- **Severity**: MAJOR
- **Category**: NAMING
- **Description**: Query UseCase는 Get/Search/Find/List/Count 접두어를 사용합니다. 예: GetOrderUseCase(단건), SearchOrdersUseCase(목록), CountOrdersUseCase(카운트).
- **Rationale**: 네이밍 일관성. 접두어로 조회 유형을 명확히 표현합니다.

#### VAL-001: Validator는 @Component 어노테이션 사용
- **Severity**: MAJOR
- **Category**: ANNOTATION
- **Description**: Validator 클래스는 @Component 어노테이션을 사용합니다. Service에서 주입받아 비즈니스 검증을 수행합니다.
- **Rationale**: 의존성 주입 활용. Bean으로 등록하여 테스트에서 Mock 교체가 용이합니다.

#### VAL-002: Validator는 {Domain}Validator 네이밍 사용
- **Severity**: MAJOR
- **Category**: NAMING
- **Description**: Validator 클래스는 {Domain}Validator 형태로 네이밍합니다. 예: OrderValidator, MemberValidator. 도메인별로 검증 로직을 분리합니다.
- **Rationale**: 일관된 네이밍. 클래스명만 보고도 담당 도메인을 파악할 수 있습니다.

#### VAL-004: Validator는 void 반환, 실패 시 DomainException
- **Severity**: MAJOR
- **Category**: BEHAVIOR
- **Description**: Validator 메서드는 void를 반환하고, 검증 실패 시 DomainException(DuplicateException, NotFoundException, InvalidStateException 등)을 발생시킵니다. boolean 반환은 금지됩니다.
- **Rationale**: Fail-fast 원칙. 검증 실패 시 즉시 예외를 발생시켜 후속 로직 실행을 방지합니다.

#### VAL-005: Validator 메서드는 validateXxx() 또는 checkXxx() 사용
- **Severity**: MAJOR
- **Category**: NAMING
- **Description**: Validator의 검증 메서드는 validateXxx() 또는 checkXxx() 형태를 사용합니다. 예: validateEmailDuplication(), checkOrderExists(). verify, assert 등 다른 네이밍은 사용하지 않습니다.
- **Rationale**: 네이밍 일관성. validate/check 접두어로 검증 메서드임을 명확히 합니다.


### MINOR 규칙

#### QPRT-003: QueryPort는 Unique 필드 조회 조건부 허용
- **Severity**: MINOR
- **Category**: STRUCTURE
- **Description**: DB에 Unique 제약조건이 있는 필드에 대해 findBy{UniqueField}(): Optional<Domain>, existsBy{UniqueField}(): boolean 메서드를 추가할 수 있습니다. 예: findByEmail(Email), existsBySlug(Slug). 일반 필드 조회는 Criteria를 사용합니다.
- **Rationale**: 성능 최적화. Unique 인덱스를 직접 조회하면 Criteria보다 효율적입니다. 중복 검증 등에 유용합니다.

