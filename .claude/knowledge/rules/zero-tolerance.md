# Zero-Tolerance 규칙 (절대 위반 금지)

이 문서는 프로젝트 전체에서 절대 위반해서는 안 되는 핵심 규칙들을 정리합니다.

## 요약 테이블

| Code | Layer | Name | Category |
|------|-------|------|----------|
| C-001 | APPLICATION | Lombok 사용 금지 | ANNOTATION |
| C-002 | APPLICATION | 변환기에서 null 체크 금지 | BEHAVIOR |
| C-003 | APPLICATION | 변환기에서 기본값 할당 금지 | BEHAVIOR |
| C-004 | APPLICATION | @Transactional은 Manager/Facade에서만 메서드 단위로 | ANNOTATION |
| C-005 | APPLICATION | Port(Out) 직접 주입 금지 | DEPENDENCY |
| C-006 | APPLICATION | 시간/ID 생성은 Factory에서만 | BEHAVIOR |
| C-007 | APPLICATION | 이벤트 객체 생성은 Domain에서 | BEHAVIOR |
| C-009 | APPLICATION | ApplicationEventPublisher 직접 사용 금지 | DEPENDENCY |
| C-010 | APPLICATION | 시간 조회는 TimeProvider 사용 | BEHAVIOR |
| C-011 | APPLICATION | ID 생성은 IdGeneratorPort 사용 | BEHAVIOR |
| CDTO-001 | APPLICATION | Command DTO는 Record로 정의 | STRUCTURE |
| CDTO-006 | APPLICATION | Command DTO에 Validation 어노테이션 금지 | ANNOTATION |
| CDTO-007 | APPLICATION | Command DTO는 Domain 타입 의존 금지 | DEPENDENCY |
| CLPRT-001 | APPLICATION | ClientPort는 반드시 interface로 정의 | STRUCTURE |
| COMP-004 | APPLICATION | Component는 Manager 의존 가능, Port 직접 의존 금지 | DEPENDENCY |
| CPRT-001 | APPLICATION | CommandPort는 반드시 interface로 정의 | STRUCTURE |
| CPRT-002 | APPLICATION | CommandPort는 persist(Domain) 메서드만 제공 | STRUCTURE |
| CPRT-003 | APPLICATION | CommandPort는 Domain 객체를 파라미터로 받음 | BEHAVIOR |
| EVT-001 | APPLICATION | EventListener는 외부 시스템 호출 전용 | BEHAVIOR |
| EVT-002 | APPLICATION | EventListener 처리 전 Outbox 저장 필수 | BEHAVIOR |
| EVT-003 | APPLICATION | EventListener 외부 호출 시 멱등키(outboxId) 필수 전달 | BEHAVIOR |
| EVT-005 | APPLICATION | EventListener는 @EventListener 사용 (@TransactionalEventListener 금지) | ANNOTATION |
| FAC-003 | APPLICATION | Facade는 2개 이상 Manager 조합할 때만 사용 | STRUCTURE |
| FAC-004 | APPLICATION | Facade는 RDB Persistence Manager만 의존 | DEPENDENCY |
| FAC-006 | APPLICATION | Facade에 비즈니스 로직 금지 | BEHAVIOR |
| QDTO-001 | APPLICATION | Query DTO는 Record로 정의 | STRUCTURE |
| QDTO-005 | APPLICATION | Query DTO는 Domain 타입 의존 금지 | DEPENDENCY |
| QPRT-001 | APPLICATION | QueryPort는 반드시 interface로 정의 | STRUCTURE |
| QPRT-004 | APPLICATION | QueryPort는 Domain 객체 반환 (Entity/DTO 금지) | BEHAVIOR |
| QPRT-005 | APPLICATION | QueryPort의 복합 조건 조회는 Criteria 사용 | STRUCTURE |
| RDTO-001 | APPLICATION | Response DTO는 Record로 정의 | STRUCTURE |
| RDTO-008 | APPLICATION | Response DTO는 Domain 타입 의존 금지 | DEPENDENCY |
| SCHS-003 | APPLICATION | Application Layer에 @Scheduled 어노테이션 금지 | ANNOTATION |
| SCHS-004 | APPLICATION | Scheduler Service는 분산락(LockManager) 사용 필수 | BEHAVIOR |
| SCHS-005 | APPLICATION | Scheduler Service는 Manager를 통해 Port 사용 (Port 직접 주입 금지) | DEPENDENCY |
| SCHS-007 | APPLICATION | Scheduler Service 외부 호출 시 멱등키(outboxId) 필수 전달 | BEHAVIOR |
| SCHS-012 | APPLICATION | Scheduler Service는 finally 블록에서 분산락 해제 | BEHAVIOR |
| SVC-002 | APPLICATION | UseCase(Port-In) 인터페이스 구현 필수 | STRUCTURE |
| SVC-003 | APPLICATION | Domain 객체 직접 생성 금지 | BEHAVIOR |
| SVC-005 | APPLICATION | Domain 객체 직접 반환 금지 | BEHAVIOR |
| SVC-006 | APPLICATION | Service에서 @Transactional 절대 금지 | ANNOTATION |
| SVC-007 | APPLICATION | Service에 비즈니스 로직 금지 | BEHAVIOR |
| SVC-008 | APPLICATION | Service에서 Port(Out) 직접 주입 금지 | DEPENDENCY |
| SVC-009 | APPLICATION | Service에서 시간/ID 생성 금지 | BEHAVIOR |
| SVC-010 | APPLICATION | Service에서 이벤트 객체 생성 금지 | BEHAVIOR |
| UC-001 | APPLICATION | UseCase는 반드시 interface로 정의 | STRUCTURE |
| UC-002 | APPLICATION | UseCase는 execute() 단일 메서드 제공 | STRUCTURE |
| VAL-003 | APPLICATION | Validator는 ReadManager만 의존 | DEPENDENCY |
| AGG-001 | DOMAIN | Aggregate Lombok 어노테이션 금지 | ANNOTATION |
| AGG-002 | DOMAIN | Aggregate JPA 어노테이션 금지 | ANNOTATION |
| AGG-003 | DOMAIN | Aggregate Spring 어노테이션 금지 | ANNOTATION |
| AGG-004 | DOMAIN | forNew() 팩토리 메서드 필수 | STRUCTURE |
| AGG-005 | DOMAIN | reconstitute() 팩토리 메서드 필수 | STRUCTURE |
| AGG-007 | DOMAIN | Aggregate ID는 ID VO 사용 | STRUCTURE |
| AGG-008 | DOMAIN | isNew() 메서드 필수 | STRUCTURE |
| AGG-009 | DOMAIN | Aggregate 시간 필드는 Instant 타입 | STRUCTURE |
| AGG-010 | DOMAIN | Instant 파라미터 주입 (Instant.now() 금지) | BEHAVIOR |
| AGG-012 | DOMAIN | Aggregate Setter 메서드 금지 | STRUCTURE |
| AGG-014 | DOMAIN | Law of Demeter (Getter 체이닝 금지) | BEHAVIOR |
| AGG-023 | DOMAIN | Aggregate 외부 레이어 의존 금지 | DEPENDENCY |
| AGG-024 | DOMAIN | Aggregate에서 Repository 참조 금지 | DEPENDENCY |
| C-001 | DOMAIN | Lombok 사용 금지 | ANNOTATION |
| C-002 | DOMAIN | 변환기에서 null 체크 금지 | BEHAVIOR |
| C-003 | DOMAIN | 변환기에서 기본값 할당 금지 | BEHAVIOR |
| C-004 | DOMAIN | @Transactional은 Manager/Facade에서만 메서드 단위로 | ANNOTATION |
| C-005 | DOMAIN | Port(Out) 직접 주입 금지 | DEPENDENCY |
| C-006 | DOMAIN | 시간/ID 생성은 Factory에서만 | BEHAVIOR |
| C-007 | DOMAIN | 이벤트 객체 생성은 Domain에서 | BEHAVIOR |
| C-009 | DOMAIN | ApplicationEventPublisher 직접 사용 금지 | DEPENDENCY |
| C-010 | DOMAIN | 시간 조회는 TimeProvider 사용 | BEHAVIOR |
| C-011 | DOMAIN | ID 생성은 IdGeneratorPort 사용 | BEHAVIOR |
| CADP-001 | DOMAIN | persist() 메서드만 제공 | BEHAVIOR |
| CADP-002 | DOMAIN | CommandAdapter에서 @Transactional 금지 | ANNOTATION |
| CADP-007 | DOMAIN | CommandAdapter에 비즈니스 로직 금지 | BEHAVIOR |
| CDTO-001 | DOMAIN | Command DTO는 Record로 정의 | STRUCTURE |
| CDTO-006 | DOMAIN | Command DTO에 Validation 어노테이션 금지 | ANNOTATION |
| CDTO-007 | DOMAIN | Command DTO는 Domain 타입 의존 금지 | DEPENDENCY |
| CLPRT-001 | DOMAIN | ClientPort는 반드시 interface로 정의 | STRUCTURE |
| COMP-004 | DOMAIN | Component는 Manager 의존 가능, Port 직접 의존 금지 | DEPENDENCY |
| CPRT-001 | DOMAIN | CommandPort는 반드시 interface로 정의 | STRUCTURE |
| CPRT-002 | DOMAIN | CommandPort는 persist(Domain) 메서드만 제공 | STRUCTURE |
| CPRT-003 | DOMAIN | CommandPort는 Domain 객체를 파라미터로 받음 | BEHAVIOR |
| CRI-001 | DOMAIN | domain.[bc].query.criteria 패키지 | LOCATION |
| CRI-002 | DOMAIN | *Criteria 네이밍 규칙 | NAMING |
| CRI-003 | DOMAIN | Criteria public 접근 제어자 | STRUCTURE |
| CRI-004 | DOMAIN | Record 타입 필수 | STRUCTURE |
| CRI-005 | DOMAIN | of() 정적 팩토리 메서드 필수 | STRUCTURE |
| CRI-006 | DOMAIN | Criteria Lombok 금지 | ANNOTATION |
| CRI-007 | DOMAIN | Criteria JPA 금지 | ANNOTATION |
| CRI-008 | DOMAIN | Criteria Spring 금지 | ANNOTATION |
| CRI-009 | DOMAIN | Criteria 외부 레이어 의존 금지 | DEPENDENCY |
| ENT-001 | DOMAIN | BaseAuditEntity 상속 필수 | STRUCTURE |
| ENT-002 | DOMAIN | Long FK 전략 (JPA 관계 어노테이션 금지) | STRUCTURE |
| ENT-003 | DOMAIN | Entity에서 Lombok 금지 | ANNOTATION |
| ENT-005 | DOMAIN | Setter 메서드 금지 | BEHAVIOR |
| EVT-001 | DOMAIN | DomainEvent 인터페이스 구현 필수 | STRUCTURE |
| EVT-002 | DOMAIN | Record 타입 필수 | STRUCTURE |
| EVT-003 | DOMAIN | occurredAt (Instant) 필드 필수 | STRUCTURE |
| EVT-004 | DOMAIN | from(Aggregate, Instant) 정적 팩토리 메서드 필수 | STRUCTURE |
| EVT-005 | DOMAIN | 과거형 네이밍 필수 (*Event) | NAMING |
| EVT-006 | DOMAIN | domain.[bc].event 패키지 위치 | LOCATION |
| EVT-007 | DOMAIN | Event Lombok 금지 | ANNOTATION |
| EVT-008 | DOMAIN | Event JPA 금지 | ANNOTATION |
| EVT-009 | DOMAIN | Event Spring 금지 | ANNOTATION |
| EVT-010 | DOMAIN | Event org.springframework.* 의존 금지 | DEPENDENCY |
| EVT-011 | DOMAIN | Event 외부 레이어 의존 금지 | DEPENDENCY |
| EXC-001 | DOMAIN | ErrorCode 인터페이스 구현 필수 | STRUCTURE |
| EXC-002 | DOMAIN | ErrorCode domain.[bc].exception 패키지 | LOCATION |
| EXC-003 | DOMAIN | ErrorCode Lombok 금지 | ANNOTATION |
| EXC-004 | DOMAIN | ErrorCode public 접근 제어자 | STRUCTURE |
| EXC-005 | DOMAIN | getCode() 메서드 필수 | STRUCTURE |
| EXC-006 | DOMAIN | getHttpStatus() int 타입 반환 | STRUCTURE |
| EXC-007 | DOMAIN | getMessage() 메서드 필수 | STRUCTURE |
| EXC-008 | DOMAIN | Spring HttpStatus 사용 금지 | DEPENDENCY |
| EXC-009 | DOMAIN | DomainException 상속 필수 | STRUCTURE |
| EXC-010 | DOMAIN | Exception domain.[bc].exception 패키지 | LOCATION |
| EXC-011 | DOMAIN | Exception Lombok 금지 | ANNOTATION |
| EXC-012 | DOMAIN | Exception JPA 금지 | ANNOTATION |
| EXC-013 | DOMAIN | Exception Spring 금지 | ANNOTATION |
| EXC-014 | DOMAIN | Exception public 클래스 | STRUCTURE |
| EXC-015 | DOMAIN | RuntimeException 계층 (Unchecked) | STRUCTURE |
| EXC-016 | DOMAIN | Exception 외부 레이어 의존 금지 | DEPENDENCY |
| EXC-017 | DOMAIN | Exception Spring Framework 의존 금지 | DEPENDENCY |
| EXC-019 | DOMAIN | DomainException common 패키지 | LOCATION |
| EXC-020 | DOMAIN | ErrorCode 인터페이스 common 패키지 | LOCATION |
| FAC-003 | DOMAIN | Facade는 2개 이상 Manager 조합할 때만 사용 | STRUCTURE |
| FAC-004 | DOMAIN | Facade는 RDB Persistence Manager만 의존 | DEPENDENCY |
| FAC-006 | DOMAIN | Facade에 비즈니스 로직 금지 | BEHAVIOR |
| ID-001 | DOMAIN | *Id 네이밍 필수 | NAMING |
| ID-002 | DOMAIN | Record 타입 필수 | STRUCTURE |
| ID-003 | DOMAIN | of() 정적 팩토리 메서드 필수 | STRUCTURE |
| ID-004 | DOMAIN | Long ID forNew() 필수 | STRUCTURE |
| ID-005 | DOMAIN | Long ID forNew()는 null 반환 | BEHAVIOR |
| ID-006 | DOMAIN | Long ID isNew() 필수 | STRUCTURE |
| ID-007 | DOMAIN | String ID isNew() 금지 | BEHAVIOR |
| ID-008 | DOMAIN | String ID는 외부에서 주입 | BEHAVIOR |
| ID-009 | DOMAIN | ID VO Lombok 금지 | ANNOTATION |
| ID-010 | DOMAIN | ID VO JPA/Spring 금지 | ANNOTATION |
| QDR-001 | DOMAIN | Query 전용 (조회만) | BEHAVIOR |
| QDR-002 | DOMAIN | Join 절대 금지 | BEHAVIOR |
| QDR-003 | DOMAIN | findAll 금지 (OOM 방지) | BEHAVIOR |
| QDR-008 | DOMAIN | QueryDslRepository에서 @Transactional 금지 | ANNOTATION |
| QDTO-001 | DOMAIN | Query DTO는 Record로 정의 | STRUCTURE |
| QDTO-005 | DOMAIN | Query DTO는 Domain 타입 의존 금지 | DEPENDENCY |
| QPRT-001 | DOMAIN | QueryPort는 반드시 interface로 정의 | STRUCTURE |
| QPRT-004 | DOMAIN | QueryPort는 Domain 객체 반환 (Entity/DTO 금지) | BEHAVIOR |
| QPRT-005 | DOMAIN | QueryPort의 복합 조건 조회는 Criteria 사용 | STRUCTURE |
| RDTO-001 | DOMAIN | Response DTO는 Record로 정의 | STRUCTURE |
| RDTO-008 | DOMAIN | Response DTO는 Domain 타입 의존 금지 | DEPENDENCY |
| REPO-001 | DOMAIN | Command 전용 (save/delete만) | BEHAVIOR |
| REPO-002 | DOMAIN | Query Method 금지 | BEHAVIOR |
| REPO-003 | DOMAIN | @Query 어노테이션 금지 | ANNOTATION |
| REPO-004 | DOMAIN | QuerydslPredicateExecutor 금지 | STRUCTURE |
| REPO-005 | DOMAIN | Repository에서 @Transactional 금지 | ANNOTATION |
| SCHS-003 | DOMAIN | Application Layer에 @Scheduled 어노테이션 금지 | ANNOTATION |
| SCHS-004 | DOMAIN | Scheduler Service는 분산락(LockManager) 사용 필수 | BEHAVIOR |
| SCHS-005 | DOMAIN | Scheduler Service는 Manager를 통해 Port 사용 (Port 직접 주입 금지) | DEPENDENCY |
| SCHS-007 | DOMAIN | Scheduler Service 외부 호출 시 멱등키(outboxId) 필수 전달 | BEHAVIOR |
| SCHS-012 | DOMAIN | Scheduler Service는 finally 블록에서 분산락 해제 | BEHAVIOR |
| SVC-002 | DOMAIN | UseCase(Port-In) 인터페이스 구현 필수 | STRUCTURE |
| SVC-003 | DOMAIN | Domain 객체 직접 생성 금지 | BEHAVIOR |
| SVC-005 | DOMAIN | Domain 객체 직접 반환 금지 | BEHAVIOR |
| SVC-006 | DOMAIN | Service에서 @Transactional 절대 금지 | ANNOTATION |
| SVC-007 | DOMAIN | Service에 비즈니스 로직 금지 | BEHAVIOR |
| SVC-008 | DOMAIN | Service에서 Port(Out) 직접 주입 금지 | DEPENDENCY |
| SVC-009 | DOMAIN | Service에서 시간/ID 생성 금지 | BEHAVIOR |
| SVC-010 | DOMAIN | Service에서 이벤트 객체 생성 금지 | BEHAVIOR |
| UC-001 | DOMAIN | UseCase는 반드시 interface로 정의 | STRUCTURE |
| UC-002 | DOMAIN | UseCase는 execute() 단일 메서드 제공 | STRUCTURE |
| VAL-003 | DOMAIN | Validator는 ReadManager만 의존 | DEPENDENCY |
| VO-001 | DOMAIN | Record 타입 필수 | STRUCTURE |
| VO-002 | DOMAIN | of() 정적 팩토리 메서드 필수 | STRUCTURE |
| VO-003 | DOMAIN | Compact Constructor 검증 필수 | BEHAVIOR |
| VO-007 | DOMAIN | VO Lombok 금지 | ANNOTATION |
| VO-008 | DOMAIN | VO JPA 금지 | ANNOTATION |
| VO-009 | DOMAIN | VO Spring 금지 | ANNOTATION |
| EMAP-002 | PERSISTENCE | Pure Java (Lombok/MapStruct 금지) | ANNOTATION |
| EMAP-003 | PERSISTENCE | 시간 필드 생성 금지 (Instant.now() 금지) | BEHAVIOR |
| QADP-001 | PERSISTENCE | QueryDslRepository 위임만 | BEHAVIOR |
| QADP-002 | PERSISTENCE | QueryAdapter에서 @Transactional 금지 | ANNOTATION |
| QADP-008 | PERSISTENCE | QueryAdapter에 비즈니스 로직 금지 | BEHAVIOR |
| CFG-001 | REST_API | OpenApiConfig 중앙 설정 필수 | STRUCTURE |
| CFG-002 | REST_API | JacksonConfig 중앙 설정 필수 | STRUCTURE |
| CTR-001 | REST_API | Thin Controller 패턴 적용 | BEHAVIOR |
| CTR-002 | REST_API | ResponseEntity<ApiResponse<T>> 래핑 필수 | STRUCTURE |
| CTR-004 | REST_API | DELETE 메서드 금지 (소프트 삭제는 PATCH) | BEHAVIOR |
| CTR-005 | REST_API | Controller에서 @Transactional 사용 금지 | ANNOTATION |
| CTR-007 | REST_API | Controller에 비즈니스 로직 포함 금지 | BEHAVIOR |
| CTR-008 | REST_API | Controller에서 Domain 객체 직접 생성/조작 금지 | DEPENDENCY |
| CTR-009 | REST_API | Controller에서 Lombok 사용 금지 | ANNOTATION |
| DTO-001 | REST_API | Request/Response DTO는 Java 21 Record 사용 필수 | STRUCTURE |
| DTO-002 | REST_API | Request DTO @NotNull 필수 (Nullable 금지) | ANNOTATION |
| DTO-005 | REST_API | DTO에서 Lombok 사용 금지 | ANNOTATION |
| DTO-006 | REST_API | DTO에서 Jackson 어노테이션 금지 | ANNOTATION |
| DTO-007 | REST_API | DTO에 Domain 변환 메서드 금지 | BEHAVIOR |
| DTO-008 | REST_API | DTO에 비즈니스 로직 메서드 금지 | BEHAVIOR |
| DTO-011 | REST_API | DTO에 Setter 메서드 금지 | STRUCTURE |
| DTO-012 | REST_API | DTO에 Spring 어노테이션 금지 | ANNOTATION |
| DTO-014 | REST_API | ApiResponse 원시타입 래핑 금지 | STRUCTURE |
| MAP-001 | REST_API | Mapper는 @Component Bean 등록 필수 | ANNOTATION |
| MAP-002 | REST_API | Mapper에서 Static 메서드 금지 | STRUCTURE |
| MAP-004 | REST_API | Mapper는 필드 매핑만 수행 | BEHAVIOR |
| MAP-005 | REST_API | Mapper에 비즈니스 로직 금지 | BEHAVIOR |
| MAP-006 | REST_API | Mapper에서 Domain 객체 직접 사용 금지 | DEPENDENCY |
| MAP-007 | REST_API | Mapper에서 Lombok 사용 금지 | ANNOTATION |
| MAP-008 | REST_API | Mapper에 Repository/UseCase 주입 금지 | DEPENDENCY |
| TEST-001 | REST_API | @WebMvcTest + RestDocsTestSupport 상속 필수 | STRUCTURE |
| TEST-002 | REST_API | @MockitoBean UseCase/Mapper Mock 필수 | DEPENDENCY |
| TEST-003 | REST_API | REST Docs document() 문서화 필수 | BEHAVIOR |
| TSCH-002 | SCHEDULER | Thin Scheduler는 UseCase 인터페이스만 의존 | DEPENDENCY |
| TSCH-003 | SCHEDULER | Thin Scheduler의 @Scheduled 메서드는 UseCase.execute() 단일 호출 | BEHAVIOR |
| TSCH-004 | SCHEDULER | Thin Scheduler에 비즈니스 로직 금지 | BEHAVIOR |

---

## 상세 규칙


### APPLICATION Layer

#### C-001: Lombok 사용 금지
- **Category**: ANNOTATION
- **Description**: @Data, @Builder, @Getter, @Setter, @RequiredArgsConstructor 등 모든 Lombok 어노테이션 사용이 금지됩니다. 생성자 주입은 명시적으로 작성하고, Record는 Lombok 없이 사용합니다.

#### C-002: 변환기에서 null 체크 금지
- **Category**: BEHAVIOR
- **Description**: Factory, Assembler 등 변환기 클래스에서 입력값 null 체크를 하면 안 됩니다. null 검증은 REST-API Layer의 Mapper에서 이미 처리되었으므로, Application Layer에서 중복 검증은 불필요합니다.

#### C-003: 변환기에서 기본값 할당 금지
- **Category**: BEHAVIOR
- **Description**: Factory, Assembler 등 변환기에서 null을 기본값으로 대체하면 안 됩니다. (예: name != null ? name : "Unknown" 금지) null은 null 그대로 전달하고, 기본값 결정은 Domain 영역의 책임입니다.

#### C-004: @Transactional은 Manager/Facade에서만 메서드 단위로
- **Category**: ANNOTATION
- **Description**: @Transactional 어노테이션은 오직 Manager(PersistenceManager, ReadManager)와 Facade(RDB Manager만 의존 시)에서만 사용합니다. Service, Factory, Assembler 등에서는 절대 금지입니다. 클래스 단위가 아닌 메서드 단위로만 사용합니다.

#### C-005: Port(Out) 직접 주입 금지
- **Category**: DEPENDENCY
- **Description**: Service, Factory, Assembler 등에서 CommandPort, QueryPort를 직접 주입받으면 안 됩니다. 반드시 Manager(PersistenceManager, ReadManager)를 통해 래핑하여 사용해야 합니다.

#### C-006: 시간/ID 생성은 Factory에서만
- **Category**: BEHAVIOR
- **Description**: Instant.now(), LocalDateTime.now(), UUID.randomUUID() 등 시간이나 ID 생성 로직은 Factory에서만 허용됩니다. Service, Assembler 등 다른 컴포넌트에서는 금지됩니다.

#### C-007: 이벤트 객체 생성은 Domain에서
- **Category**: BEHAVIOR
- **Description**: DomainEvent 객체를 new OrderCreatedEvent(...) 형태로 Service, Factory에서 직접 생성하면 안 됩니다. Domain Aggregate 내부에서 이벤트 객체를 생성하고, Application Layer는 이를 발행만 담당합니다.

#### C-009: ApplicationEventPublisher 직접 사용 금지
- **Category**: DEPENDENCY
- **Description**: Spring의 ApplicationEventPublisher를 직접 주입받아 사용하면 안 됩니다. 반드시 TransactionEventRegistry를 통해 이벤트를 발행해야 합니다.

#### C-010: 시간 조회는 TimeProvider 사용
- **Category**: BEHAVIOR
- **Description**: Instant.now(), LocalDateTime.now(), System.currentTimeMillis() 등 시간 조회를 직접 호출하면 안 됩니다. TimeProvider 인터페이스를 주입받아 timeProvider.now()를 사용해야 합니다.

#### C-011: ID 생성은 IdGeneratorPort 사용
- **Category**: BEHAVIOR
- **Description**: UUID.randomUUID(), ULID, Snowflake 등 ID 생성 로직을 직접 호출하면 안 됩니다. IdGeneratorPort 인터페이스를 주입받아 idGenerator.generate()를 사용해야 합니다.

#### CDTO-001: Command DTO는 Record로 정의
- **Category**: STRUCTURE
- **Description**: Command DTO는 반드시 Java Record로 정의합니다. class 사용을 금지합니다. Record는 불변성을 보장하고 equals/hashCode/toString을 자동 생성합니다.

#### CDTO-006: Command DTO에 Validation 어노테이션 금지
- **Category**: ANNOTATION
- **Description**: Command DTO에 @Valid, @NotNull, @Size 등 Bean Validation 어노테이션을 사용하지 않습니다. 입력 검증은 REST-API Layer의 ApiRequest DTO에서 처리되어야 합니다.

#### CDTO-007: Command DTO는 Domain 타입 의존 금지
- **Category**: DEPENDENCY
- **Description**: Command DTO의 필드는 primitive 타입, String, 표준 Java 타입만 사용합니다. Domain의 VO나 ID 타입을 직접 사용하면 안 됩니다. (예: Long memberId ✅, MemberId memberId ❌)

#### CLPRT-001: ClientPort는 반드시 interface로 정의
- **Category**: STRUCTURE
- **Description**: ClientPort(Port-Out)는 반드시 interface로 정의합니다. class나 abstract class는 금지됩니다. External Adapter가 이 인터페이스를 구현합니다.

#### COMP-004: Component는 Manager 의존 가능, Port 직접 의존 금지
- **Category**: DEPENDENCY
- **Description**: component에서 DB 조회가 필요하면 ReadManager를 의존합니다. CommandPort, QueryPort를 직접 의존하면 안 됩니다. Manager로 래핑하여 트랜잭션 일관성을 보장합니다.

#### CPRT-001: CommandPort는 반드시 interface로 정의
- **Category**: STRUCTURE
- **Description**: CommandPort(Port-Out)는 반드시 interface로 정의합니다. class나 abstract class는 금지됩니다. Persistence Layer의 CommandAdapter가 이 인터페이스를 구현합니다.

#### CPRT-002: CommandPort는 persist(Domain) 메서드만 제공
- **Category**: STRUCTURE
- **Description**: CommandPort는 persist(Domain domain): {Id} 단일 메서드만 정의합니다. save, insert, create, update, delete 등 다른 네이밍은 금지됩니다. 삭제는 Domain의 soft-delete 상태 변경 후 persist로 처리합니다.

#### CPRT-003: CommandPort는 Domain 객체를 파라미터로 받음
- **Category**: BEHAVIOR
- **Description**: persist() 메서드는 Domain 객체(Aggregate)를 파라미터로 받습니다. JPA Entity나 DTO를 직접 전달하면 안 됩니다. Entity 변환은 Adapter 내부(EntityMapper)에서 처리합니다.

#### EVT-001: EventListener는 외부 시스템 호출 전용
- **Category**: BEHAVIOR
- **Description**: EventListener는 외부 시스템(결제 API, 알림 서비스 등) 호출만 담당합니다. 내부 도메인 간 통신에 이벤트를 사용하면 안 됩니다. 내부 도메인 연동은 Facade/Manager 조합으로 직접 처리합니다.

#### EVT-002: EventListener 처리 전 Outbox 저장 필수
- **Category**: BEHAVIOR
- **Description**: 이벤트 발행 전에 반드시 Outbox에 저장되어야 합니다. Service에서 Facade를 통해 Domain + Outbox를 함께 저장한 후 이벤트를 발행합니다. Outbox는 EventListener 실패 시 Scheduler의 재처리를 보장합니다.

#### EVT-003: EventListener 외부 호출 시 멱등키(outboxId) 필수 전달
- **Category**: BEHAVIOR
- **Description**: 외부 API 호출 시 outboxId를 멱등키로 반드시 전달합니다. 동일한 outboxId로 재호출되어도 외부 시스템이 중복 처리하지 않도록 합니다. EventListener 실패 후 Scheduler 재시도 시에도 안전합니다.

#### EVT-005: EventListener는 @EventListener 사용 (@TransactionalEventListener 금지)
- **Category**: ANNOTATION
- **Description**: @EventListener를 사용합니다. @TransactionalEventListener는 사용하지 않습니다. 현재 아키텍처에서 이벤트 발행 시점에 이미 트랜잭션이 종료되어 있으므로 TransactionalEventListener가 동작하지 않습니다.

#### FAC-003: Facade는 2개 이상 Manager 조합할 때만 사용
- **Category**: STRUCTURE
- **Description**: Facade는 2개 이상의 Manager를 조합할 때만 생성합니다. 단일 Manager 호출만 필요하면 Service에서 Manager를 직접 사용해야 합니다. 불필요한 Facade는 복잡도만 증가시킵니다.

#### FAC-004: Facade는 RDB Persistence Manager만 의존
- **Category**: DEPENDENCY
- **Description**: CommandFacade는 RDB PersistenceManager만 의존해야 합니다. 외부 API Manager(ClientPort 래퍼), Cache Manager, Messaging Manager 등은 Facade에서 호출하면 안 됩니다. 외부 호출이 필요하면 Service 레벨에서 처리합니다.

#### FAC-006: Facade에 비즈니스 로직 금지
- **Category**: BEHAVIOR
- **Description**: Facade에서 if/switch를 사용한 비즈니스 규칙, 계산 로직, 상태 판단 로직을 포함하면 안 됩니다. Facade는 여러 Manager 호출을 조합하는 역할만 수행하고, 비즈니스 로직은 Domain에 위임합니다.

#### QDTO-001: Query DTO는 Record로 정의
- **Category**: STRUCTURE
- **Description**: Query DTO는 반드시 Java Record로 정의합니다. class 사용을 금지합니다. Query는 조회 조건을 담는 불변 객체입니다.

#### QDTO-005: Query DTO는 Domain 타입 의존 금지
- **Category**: DEPENDENCY
- **Description**: Query DTO의 필드는 primitive 타입, String, 표준 Java 타입만 사용합니다. Domain의 VO나 ID 타입을 직접 사용하면 안 됩니다. Domain 타입 변환은 QueryFactory에서 Criteria 생성 시 처리합니다.

#### QPRT-001: QueryPort는 반드시 interface로 정의
- **Category**: STRUCTURE
- **Description**: QueryPort(Port-Out)는 반드시 interface로 정의합니다. class나 abstract class는 금지됩니다. Persistence Layer의 QueryAdapter가 이 인터페이스를 구현합니다.

#### QPRT-004: QueryPort는 Domain 객체 반환 (Entity/DTO 금지)
- **Category**: BEHAVIOR
- **Description**: QueryPort의 조회 메서드는 Domain 객체(Aggregate, Entity(Domain), VO)를 반환해야 합니다. JPA Entity나 DTO를 직접 반환하면 안 됩니다. Entity→Domain 변환은 Adapter 내부에서 처리합니다.

#### QPRT-005: QueryPort의 복합 조건 조회는 Criteria 사용
- **Category**: STRUCTURE
- **Description**: findByXxxAndYyy(), findByStatusAndDate() 형태의 복합 조건 메서드는 금지됩니다. 복합 조건은 Criteria VO를 사용하여 findByCriteria(Criteria)로 처리합니다. Criteria는 Domain Layer에 정의됩니다.

#### RDTO-001: Response DTO는 Record로 정의
- **Category**: STRUCTURE
- **Description**: Response DTO는 반드시 Java Record로 정의합니다. class 사용을 금지합니다. Response는 조회 결과를 담는 불변 객체입니다.

#### RDTO-008: Response DTO는 Domain 타입 의존 금지
- **Category**: DEPENDENCY
- **Description**: Response DTO의 필드는 primitive 타입, String, 표준 Java 타입만 사용합니다. Domain의 VO나 ID 타입을 직접 노출하면 안 됩니다. Domain → Response 변환은 Assembler에서 값 추출하여 처리합니다.

#### SCHS-003: Application Layer에 @Scheduled 어노테이션 금지
- **Category**: ANNOTATION
- **Description**: Application Layer의 Scheduler Service에는 @Scheduled 어노테이션을 사용하지 않습니다. @Scheduled는 adapter-in/scheduler에서만 사용합니다. Application Layer는 순수 비즈니스 로직만 포함합니다.

#### SCHS-004: Scheduler Service는 분산락(LockManager) 사용 필수
- **Category**: BEHAVIOR
- **Description**: Scheduler Service 실행 시작 시 LockManager를 통해 분산락을 획득해야 합니다. 다중 인스턴스 환경에서 동일 작업이 중복 실행되는 것을 방지합니다. 락 획득 실패 시 즉시 리턴합니다.

#### SCHS-005: Scheduler Service는 Manager를 통해 Port 사용 (Port 직접 주입 금지)
- **Category**: DEPENDENCY
- **Description**: Scheduler Service는 Port(ClientPort 등)를 직접 주입받지 않습니다. 반드시 Manager(ClientManager)를 통해 외부 호출을 수행합니다. 예: PaymentClientPort 대신 PaymentClientManager를 주입받습니다.

#### SCHS-007: Scheduler Service 외부 호출 시 멱등키(outboxId) 필수 전달
- **Category**: BEHAVIOR
- **Description**: Scheduler Service가 ClientManager를 통해 외부 API를 호출할 때 outboxId를 멱등키로 전달합니다. ClientManager가 실제 HTTP 헤더나 요청 파라미터로 멱등키를 설정합니다. EventListener에서 이미 성공했더라도 재호출이 안전합니다.

#### SCHS-012: Scheduler Service는 finally 블록에서 분산락 해제
- **Category**: BEHAVIOR
- **Description**: 분산락 획득 후에는 반드시 try-finally 패턴을 사용하여 finally에서 락을 해제합니다. 예외 발생 시에도 락이 해제되어야 다음 실행 주기에 정상 동작합니다.

#### SVC-002: UseCase(Port-In) 인터페이스 구현 필수
- **Category**: STRUCTURE
- **Description**: Service 클래스는 반드시 해당 UseCase 인터페이스를 구현해야 합니다. (예: CreateOrderService implements CreateOrderUseCase) UseCase 인터페이스 없이 Service만 존재하면 안 됩니다.

#### SVC-003: Domain 객체 직접 생성 금지
- **Category**: BEHAVIOR
- **Description**: Service에서 new Order(), Order.forNew() 등 Domain 객체를 직접 생성하면 안 됩니다. 모든 Domain/UpdateData/Bundle 생성은 Factory에 위임해야 합니다.

#### SVC-005: Domain 객체 직접 반환 금지
- **Category**: BEHAVIOR
- **Description**: Service의 execute() 메서드에서 Domain 객체(Aggregate, Entity, VO)를 직접 반환하면 안 됩니다. 반드시 Assembler를 통해 Response DTO로 변환하여 반환해야 합니다.

#### SVC-006: Service에서 @Transactional 절대 금지
- **Category**: ANNOTATION
- **Description**: Service 클래스와 메서드에 @Transactional을 사용하면 안 됩니다. 트랜잭션 관리는 Manager/Facade의 책임입니다. Service는 비즈니스 흐름 조율만 담당합니다.

#### SVC-007: Service에 비즈니스 로직 금지
- **Category**: BEHAVIOR
- **Description**: Service에서 if/switch를 사용한 비즈니스 규칙, 계산 로직, 상태 판단 로직을 포함하면 안 됩니다. (예: if(order.isPaymentCompleted()) 금지) 모든 비즈니스 로직은 Domain에 위임합니다.

#### SVC-008: Service에서 Port(Out) 직접 주입 금지
- **Category**: DEPENDENCY
- **Description**: Service에서 CommandPort, QueryPort를 직접 주입받으면 안 됩니다. 반드시 PersistenceManager, ReadManager를 통해 접근해야 합니다.

#### SVC-009: Service에서 시간/ID 생성 금지
- **Category**: BEHAVIOR
- **Description**: Service에서 Instant.now(), UUID.randomUUID() 등 시간이나 ID 생성 로직을 직접 호출하면 안 됩니다. 시간/ID 생성은 Factory의 책임입니다.

#### SVC-010: Service에서 이벤트 객체 생성 금지
- **Category**: BEHAVIOR
- **Description**: Service에서 new OrderCreatedEvent(...) 형태로 이벤트 객체를 직접 생성하면 안 됩니다. 이벤트는 Domain 내부에서 생성되고, Service는 TransactionEventRegistry로 발행만 담당합니다.

#### UC-001: UseCase는 반드시 interface로 정의
- **Category**: STRUCTURE
- **Description**: UseCase(Port-In)는 반드시 interface로 정의합니다. class나 abstract class는 금지됩니다. Service가 이 인터페이스를 구현합니다.

#### UC-002: UseCase는 execute() 단일 메서드 제공
- **Category**: STRUCTURE
- **Description**: UseCase 인터페이스는 execute(Command) 또는 execute(Query) 단일 메서드만 정의합니다. 여러 메서드를 정의하면 안 됩니다. (예: execute(CreateOrderCommand): OrderIdResponse)

#### VAL-003: Validator는 ReadManager만 의존
- **Category**: DEPENDENCY
- **Description**: Validator는 검증을 위해 ReadManager(조회용)만 의존합니다. PersistenceManager(쓰기용)나 CommandPort/QueryPort를 직접 의존하면 안 됩니다.


### DOMAIN Layer

#### AGG-001: Aggregate Lombok 어노테이션 금지
- **Category**: ANNOTATION
- **Description**: Aggregate 클래스에서 @Getter, @Setter, @Data, @Builder, @NoArgsConstructor, @AllArgsConstructor 등 모든 Lombok 어노테이션 사용을 금지합니다. 순수 자바 코드로 작성해야 합니다.

#### AGG-002: Aggregate JPA 어노테이션 금지
- **Category**: ANNOTATION
- **Description**: Aggregate 클래스에서 @Entity, @Table, @Column, @Id, @ManyToOne, @OneToMany 등 모든 JPA 어노테이션 사용을 금지합니다. 영속화 관심사는 Persistence Layer의 Entity에서 처리합니다.

#### AGG-003: Aggregate Spring 어노테이션 금지
- **Category**: ANNOTATION
- **Description**: Aggregate 클래스에서 @Component, @Service, @Repository, @Autowired, @Transactional 등 모든 Spring 어노테이션 사용을 금지합니다.

#### AGG-004: forNew() 팩토리 메서드 필수
- **Category**: STRUCTURE
- **Description**: Aggregate는 새로운 인스턴스 생성을 위한 static forNew(..., Instant now) 팩토리 메서드가 필수입니다. ID는 null(Long ID) 또는 외부 주입(String ID)하고, createdAt/updatedAt은 파라미터로 받은 Instant로 초기화합니다.

#### AGG-005: reconstitute() 팩토리 메서드 필수
- **Category**: STRUCTURE
- **Description**: Aggregate는 DB에서 조회한 데이터로 복원하기 위한 static reconstitute(...) 팩토리 메서드가 필수입니다. 모든 필드를 파라미터로 받아 복원합니다. 비즈니스 검증은 수행하지 않습니다.

#### AGG-007: Aggregate ID는 ID VO 사용
- **Category**: STRUCTURE
- **Description**: Aggregate의 ID 필드는 원시 타입(Long) 대신 전용 ID VO(예: OrderId)를 사용해야 합니다. ID VO는 domain/{bc}/id 패키지에 위치합니다.

#### AGG-008: isNew() 메서드 필수
- **Category**: STRUCTURE
- **Description**: Aggregate는 새로 생성된 객체인지 판단하는 isNew() 메서드가 필수입니다. Long ID의 경우 id.value() == null 또는 id.isNew()로 판단합니다.

#### AGG-009: Aggregate 시간 필드는 Instant 타입
- **Category**: STRUCTURE
- **Description**: Aggregate의 모든 시간 필드(createdAt, updatedAt, expiredAt 등)는 java.time.Instant 타입을 사용합니다. LocalDateTime, LocalDate, Date 등은 금지됩니다.

#### AGG-010: Instant 파라미터 주입 (Instant.now() 금지)
- **Category**: BEHAVIOR
- **Description**: Aggregate 내부에서 Instant.now() 또는 System.currentTimeMillis() 직접 호출을 금지합니다. 시간이 필요한 메서드(forNew, 상태 변경 등)는 Instant 파라미터로 받습니다. 예: forNew(..., Instant now), cancel(Instant now).

#### AGG-012: Aggregate Setter 메서드 금지
- **Category**: STRUCTURE
- **Description**: Aggregate에서 setXxx() 형태의 Setter 메서드를 정의할 수 없습니다. 상태 변경은 비즈니스 의도를 드러내는 명시적 메서드(cancel, confirm, updatePrice 등)로 수행합니다.

#### AGG-014: Law of Demeter (Getter 체이닝 금지)
- **Category**: BEHAVIOR
- **Description**: order.getCustomer().getAddress().getCity()와 같은 Getter 체이닝을 금지합니다. 필요한 정보는 Aggregate 메서드를 통해 직접 반환하거나, 위임 메서드를 제공합니다.

#### AGG-023: Aggregate 외부 레이어 의존 금지
- **Category**: DEPENDENCY
- **Description**: Aggregate는 application, adapter 패키지에 의존할 수 없습니다. domain 패키지와 java 표준 라이브러리만 import 가능합니다.

#### AGG-024: Aggregate에서 Repository 참조 금지
- **Category**: DEPENDENCY
- **Description**: Aggregate 내부에서 Repository를 호출하여 다른 Aggregate를 조회할 수 없습니다. 필요한 경우 Application Layer에서 조회하여 파라미터로 전달합니다.

#### C-001: Lombok 사용 금지
- **Category**: ANNOTATION
- **Description**: @Data, @Builder, @Getter, @Setter, @RequiredArgsConstructor 등 모든 Lombok 어노테이션 사용이 금지됩니다. 생성자 주입은 명시적으로 작성하고, Record는 Lombok 없이 사용합니다.

#### C-002: 변환기에서 null 체크 금지
- **Category**: BEHAVIOR
- **Description**: Factory, Assembler 등 변환기 클래스에서 입력값 null 체크를 하면 안 됩니다. null 검증은 REST-API Layer의 Mapper에서 이미 처리되었으므로, Application Layer에서 중복 검증은 불필요합니다.

#### C-003: 변환기에서 기본값 할당 금지
- **Category**: BEHAVIOR
- **Description**: Factory, Assembler 등 변환기에서 null을 기본값으로 대체하면 안 됩니다. (예: name != null ? name : "Unknown" 금지) null은 null 그대로 전달하고, 기본값 결정은 Domain 영역의 책임입니다.

#### C-004: @Transactional은 Manager/Facade에서만 메서드 단위로
- **Category**: ANNOTATION
- **Description**: @Transactional 어노테이션은 오직 Manager(PersistenceManager, ReadManager)와 Facade(RDB Manager만 의존 시)에서만 사용합니다. Service, Factory, Assembler 등에서는 절대 금지입니다. 클래스 단위가 아닌 메서드 단위로만 사용합니다.

#### C-005: Port(Out) 직접 주입 금지
- **Category**: DEPENDENCY
- **Description**: Service, Factory, Assembler 등에서 CommandPort, QueryPort를 직접 주입받으면 안 됩니다. 반드시 Manager(PersistenceManager, ReadManager)를 통해 래핑하여 사용해야 합니다.

#### C-006: 시간/ID 생성은 Factory에서만
- **Category**: BEHAVIOR
- **Description**: Instant.now(), LocalDateTime.now(), UUID.randomUUID() 등 시간이나 ID 생성 로직은 Factory에서만 허용됩니다. Service, Assembler 등 다른 컴포넌트에서는 금지됩니다.

#### C-007: 이벤트 객체 생성은 Domain에서
- **Category**: BEHAVIOR
- **Description**: DomainEvent 객체를 new OrderCreatedEvent(...) 형태로 Service, Factory에서 직접 생성하면 안 됩니다. Domain Aggregate 내부에서 이벤트 객체를 생성하고, Application Layer는 이를 발행만 담당합니다.

#### C-009: ApplicationEventPublisher 직접 사용 금지
- **Category**: DEPENDENCY
- **Description**: Spring의 ApplicationEventPublisher를 직접 주입받아 사용하면 안 됩니다. 반드시 TransactionEventRegistry를 통해 이벤트를 발행해야 합니다.

#### C-010: 시간 조회는 TimeProvider 사용
- **Category**: BEHAVIOR
- **Description**: Instant.now(), LocalDateTime.now(), System.currentTimeMillis() 등 시간 조회를 직접 호출하면 안 됩니다. TimeProvider 인터페이스를 주입받아 timeProvider.now()를 사용해야 합니다.

#### C-011: ID 생성은 IdGeneratorPort 사용
- **Category**: BEHAVIOR
- **Description**: UUID.randomUUID(), ULID, Snowflake 등 ID 생성 로직을 직접 호출하면 안 됩니다. IdGeneratorPort 인터페이스를 주입받아 idGenerator.generate()를 사용해야 합니다.

#### CADP-001: persist() 메서드만 제공
- **Category**: BEHAVIOR
- **Description**: CommandAdapter는 persist(Aggregate aggregate) 메서드만 제공합니다. save(), update(), delete() 등 별도 메서드를 정의하면 안 됩니다. Merge 방식으로 INSERT/UPDATE를 JPA가 판단합니다.

#### CADP-002: CommandAdapter에서 @Transactional 금지
- **Category**: ANNOTATION
- **Description**: CommandAdapter 클래스 및 메서드에서 @Transactional 어노테이션을 사용하면 안 됩니다. 트랜잭션 경계는 Application Layer(UseCase/Service)에서 관리합니다.

#### CADP-007: CommandAdapter에 비즈니스 로직 금지
- **Category**: BEHAVIOR
- **Description**: CommandAdapter에 if/switch 조건 분기, 계산 로직, 상태 검증 등 비즈니스 로직을 포함하면 안 됩니다. 단순히 Domain→Entity 변환 후 저장만 수행합니다.

#### CDTO-001: Command DTO는 Record로 정의
- **Category**: STRUCTURE
- **Description**: Command DTO는 반드시 Java Record로 정의합니다. class 사용을 금지합니다. Record는 불변성을 보장하고 equals/hashCode/toString을 자동 생성합니다.

#### CDTO-006: Command DTO에 Validation 어노테이션 금지
- **Category**: ANNOTATION
- **Description**: Command DTO에 @Valid, @NotNull, @Size 등 Bean Validation 어노테이션을 사용하지 않습니다. 입력 검증은 REST-API Layer의 ApiRequest DTO에서 처리되어야 합니다.

#### CDTO-007: Command DTO는 Domain 타입 의존 금지
- **Category**: DEPENDENCY
- **Description**: Command DTO의 필드는 primitive 타입, String, 표준 Java 타입만 사용합니다. Domain의 VO나 ID 타입을 직접 사용하면 안 됩니다. (예: Long memberId ✅, MemberId memberId ❌)

#### CLPRT-001: ClientPort는 반드시 interface로 정의
- **Category**: STRUCTURE
- **Description**: ClientPort(Port-Out)는 반드시 interface로 정의합니다. class나 abstract class는 금지됩니다. External Adapter가 이 인터페이스를 구현합니다.

#### COMP-004: Component는 Manager 의존 가능, Port 직접 의존 금지
- **Category**: DEPENDENCY
- **Description**: component에서 DB 조회가 필요하면 ReadManager를 의존합니다. CommandPort, QueryPort를 직접 의존하면 안 됩니다. Manager로 래핑하여 트랜잭션 일관성을 보장합니다.

#### CPRT-001: CommandPort는 반드시 interface로 정의
- **Category**: STRUCTURE
- **Description**: CommandPort(Port-Out)는 반드시 interface로 정의합니다. class나 abstract class는 금지됩니다. Persistence Layer의 CommandAdapter가 이 인터페이스를 구현합니다.

#### CPRT-002: CommandPort는 persist(Domain) 메서드만 제공
- **Category**: STRUCTURE
- **Description**: CommandPort는 persist(Domain domain): {Id} 단일 메서드만 정의합니다. save, insert, create, update, delete 등 다른 네이밍은 금지됩니다. 삭제는 Domain의 soft-delete 상태 변경 후 persist로 처리합니다.

#### CPRT-003: CommandPort는 Domain 객체를 파라미터로 받음
- **Category**: BEHAVIOR
- **Description**: persist() 메서드는 Domain 객체(Aggregate)를 파라미터로 받습니다. JPA Entity나 DTO를 직접 전달하면 안 됩니다. Entity 변환은 Adapter 내부(EntityMapper)에서 처리합니다.

#### CRI-001: domain.[bc].query.criteria 패키지
- **Category**: LOCATION
- **Description**: Criteria는 domain.[bc].query.criteria 패키지에 위치해야 합니다. 예: domain.order.query.criteria.OrderSearchCriteria.

#### CRI-002: *Criteria 네이밍 규칙
- **Category**: NAMING
- **Description**: Criteria는 *Criteria 또는 *SearchCriteria 네이밍 규칙이 필수입니다. 예: OrderCriteria, OrderSearchCriteria.

#### CRI-003: Criteria public 접근 제어자
- **Category**: STRUCTURE
- **Description**: Criteria는 public 클래스가 필수입니다. 예: public record OrderSearchCriteria(...) {}

#### CRI-004: Record 타입 필수
- **Category**: STRUCTURE
- **Description**: Criteria는 Java Record 타입으로 정의해야 합니다. 불변성을 보장합니다. 예: public record OrderSearchCriteria(OrderStatus status, DateRange dateRange, PageRequest page) {}

#### CRI-005: of() 정적 팩토리 메서드 필수
- **Category**: STRUCTURE
- **Description**: Criteria는 public static XxxCriteria of(...) 정적 팩토리 메서드가 필수입니다. 기본값 적용 및 검증 로직을 포함합니다.

#### CRI-006: Criteria Lombok 금지
- **Category**: ANNOTATION
- **Description**: Criteria에서 @Getter, @Setter, @Data, @Builder, @Value, @RequiredArgsConstructor, @AllArgsConstructor 등 모든 Lombok 어노테이션 사용을 금지합니다.

#### CRI-007: Criteria JPA 금지
- **Category**: ANNOTATION
- **Description**: Criteria에서 @Entity, @Table, @Id, @Column, @Embeddable, @Embedded 등 JPA 어노테이션 사용을 금지합니다.

#### CRI-008: Criteria Spring 금지
- **Category**: ANNOTATION
- **Description**: Criteria에서 @Component, @Service, @Repository, @Transactional, @Autowired 등 Spring 어노테이션 사용을 금지합니다.

#### CRI-009: Criteria 외부 레이어 의존 금지
- **Category**: DEPENDENCY
- **Description**: Criteria에서 Application Layer, Adapter Layer, Bootstrap Layer 의존을 금지합니다.

#### ENT-001: BaseAuditEntity 상속 필수
- **Category**: STRUCTURE
- **Description**: 모든 JPA Entity는 BaseAuditEntity를 상속해야 합니다. createdAt, updatedAt 필드가 자동으로 관리됩니다. 논리삭제가 필요한 Entity는 SoftDeletableEntity를 상속합니다.

#### ENT-002: Long FK 전략 (JPA 관계 어노테이션 금지)
- **Category**: STRUCTURE
- **Description**: @ManyToOne, @OneToMany, @OneToOne, @ManyToMany 등 JPA 관계 어노테이션 사용을 금지합니다. 외래키는 Long userId 형태로 직접 관리합니다.

#### ENT-003: Entity에서 Lombok 금지
- **Category**: ANNOTATION
- **Description**: @Data, @Builder, @Getter, @Setter, @NoArgsConstructor, @AllArgsConstructor 등 모든 Lombok 어노테이션 사용을 금지합니다. JPA proxy와의 충돌 및 바이트코드 조작 문제를 방지합니다.

#### ENT-005: Setter 메서드 금지
- **Category**: BEHAVIOR
- **Description**: Entity에 Setter 메서드(set*)를 정의하면 안 됩니다. 상태 변경은 의미 있는 비즈니스 메서드(예: cancel(), complete())를 통해 수행합니다. 단, update 용도의 of() 재호출은 허용됩니다.

#### EVT-001: DomainEvent 인터페이스 구현 필수
- **Category**: STRUCTURE
- **Description**: Domain Event는 DomainEvent 인터페이스를 구현해야 합니다. 예: public record OrderCreatedEvent(...) implements DomainEvent {}

#### EVT-002: Record 타입 필수
- **Category**: STRUCTURE
- **Description**: Domain Event는 Java Record 타입으로 정의해야 합니다. 불변성을 보장합니다. 예: public record OrderCreatedEvent(OrderId orderId, Instant occurredAt) implements DomainEvent {}

#### EVT-003: occurredAt (Instant) 필드 필수
- **Category**: STRUCTURE
- **Description**: Domain Event는 Instant occurredAt 필드가 필수입니다. 이벤트 발생 시점을 기록합니다.

#### EVT-004: from(Aggregate, Instant) 정적 팩토리 메서드 필수
- **Category**: STRUCTURE
- **Description**: Domain Event는 public static XxxEvent from(Aggregate, Instant now) 정적 팩토리 메서드가 필수입니다. Aggregate에서 필요한 정보를 추출하여 Event를 생성합니다.

#### EVT-005: 과거형 네이밍 필수 (*Event)
- **Category**: NAMING
- **Description**: Domain Event는 과거형 네이밍이 필수입니다. OrderCreatedEvent(✅), OrderCancelledEvent(✅), OrderPaidEvent(✅). 현재/미래형 금지: OrderCreateEvent(❌), OrderCancelEvent(❌).

#### EVT-006: domain.[bc].event 패키지 위치
- **Category**: LOCATION
- **Description**: Domain Event는 domain.[bc].event 패키지에 위치해야 합니다. 예: domain.order.event.OrderCreatedEvent.

#### EVT-007: Event Lombok 금지
- **Category**: ANNOTATION
- **Description**: Domain Event에서 @Getter, @Setter, @Data, @Builder, @Value, @RequiredArgsConstructor, @AllArgsConstructor 등 모든 Lombok 어노테이션 사용을 금지합니다.

#### EVT-008: Event JPA 금지
- **Category**: ANNOTATION
- **Description**: Domain Event에서 @Entity, @Table, @Id, @Column, @Embeddable, @Embedded 등 JPA 어노테이션 사용을 금지합니다.

#### EVT-009: Event Spring 금지
- **Category**: ANNOTATION
- **Description**: Domain Event에서 @Component, @Service, @Repository, @Transactional, @Autowired, @EventListener 등 Spring 어노테이션 사용을 금지합니다.

#### EVT-010: Event org.springframework.* 의존 금지
- **Category**: DEPENDENCY
- **Description**: Domain Event에서 org.springframework.* 패키지 import를 금지합니다.

#### EVT-011: Event 외부 레이어 의존 금지
- **Category**: DEPENDENCY
- **Description**: Domain Event에서 Application Layer, Adapter Layer, Bootstrap Layer 의존을 금지합니다. import *.application.*, *.adapter.*, *.bootstrap.* 금지.

#### EXC-001: ErrorCode 인터페이스 구현 필수
- **Category**: STRUCTURE
- **Description**: ErrorCode Enum은 ErrorCode 인터페이스를 구현해야 합니다. 예: public enum OrderErrorCode implements ErrorCode { ... }

#### EXC-002: ErrorCode domain.[bc].exception 패키지
- **Category**: LOCATION
- **Description**: ErrorCode Enum은 domain.[bc].exception 패키지에 위치해야 합니다. 예: domain.order.exception.OrderErrorCode.

#### EXC-003: ErrorCode Lombok 금지
- **Category**: ANNOTATION
- **Description**: ErrorCode Enum에서 @Getter, @Setter, @Data, @RequiredArgsConstructor 등 Lombok 어노테이션 사용을 금지합니다.

#### EXC-004: ErrorCode public 접근 제어자
- **Category**: STRUCTURE
- **Description**: ErrorCode Enum은 public 접근 제어자가 필수입니다. 예: public enum OrderErrorCode implements ErrorCode { ... }

#### EXC-005: getCode() 메서드 필수
- **Category**: STRUCTURE
- **Description**: ErrorCode Enum은 String getCode() 메서드가 필수입니다. API 응답에서 사용되는 고유 에러 코드를 반환합니다.

#### EXC-006: getHttpStatus() int 타입 반환
- **Category**: STRUCTURE
- **Description**: ErrorCode Enum은 int getHttpStatus() 메서드가 필수입니다. Spring HttpStatus가 아닌 int 타입으로 반환합니다. 예: return 404;

#### EXC-007: getMessage() 메서드 필수
- **Category**: STRUCTURE
- **Description**: ErrorCode Enum은 String getMessage() 메서드가 필수입니다. 사용자에게 표시되는 에러 메시지를 반환합니다.

#### EXC-008: Spring HttpStatus 사용 금지
- **Category**: DEPENDENCY
- **Description**: ErrorCode Enum에서 org.springframework.http.HttpStatus 사용을 금지합니다. int 타입으로 HTTP 상태 코드를 저장합니다.

#### EXC-009: DomainException 상속 필수
- **Category**: STRUCTURE
- **Description**: Concrete Exception은 DomainException을 상속해야 합니다. 예: public class OrderNotFoundException extends DomainException { ... }

#### EXC-010: Exception domain.[bc].exception 패키지
- **Category**: LOCATION
- **Description**: Concrete Exception은 domain.[bc].exception 패키지에 위치해야 합니다. 예: domain.order.exception.OrderNotFoundException.

#### EXC-011: Exception Lombok 금지
- **Category**: ANNOTATION
- **Description**: Exception에서 @Getter, @Setter, @Data, @Builder, @RequiredArgsConstructor 등 Lombok 어노테이션 사용을 금지합니다.

#### EXC-012: Exception JPA 금지
- **Category**: ANNOTATION
- **Description**: Exception에서 @Entity, @Table, @Id, @Column 등 JPA 어노테이션 사용을 금지합니다.

#### EXC-013: Exception Spring 금지
- **Category**: ANNOTATION
- **Description**: Exception에서 @Component, @Service, @Repository, @Transactional 등 Spring 어노테이션 사용을 금지합니다.

#### EXC-014: Exception public 클래스
- **Category**: STRUCTURE
- **Description**: Exception은 public 클래스가 필수입니다. 예: public class OrderNotFoundException extends DomainException { ... }

#### EXC-015: RuntimeException 계층 (Unchecked)
- **Category**: STRUCTURE
- **Description**: Exception은 RuntimeException 계층이어야 합니다 (Unchecked Exception). DomainException은 RuntimeException을 상속합니다.

#### EXC-016: Exception 외부 레이어 의존 금지
- **Category**: DEPENDENCY
- **Description**: Exception에서 Application Layer, Adapter Layer, Bootstrap Layer 의존을 금지합니다.

#### EXC-017: Exception Spring Framework 의존 금지
- **Category**: DEPENDENCY
- **Description**: Exception에서 org.springframework.* 패키지 import를 금지합니다.

#### EXC-019: DomainException common 패키지
- **Category**: LOCATION
- **Description**: DomainException 기본 클래스는 domain.common.exception 패키지에 위치해야 합니다.

#### EXC-020: ErrorCode 인터페이스 common 패키지
- **Category**: LOCATION
- **Description**: ErrorCode 인터페이스는 domain.common.exception 패키지에 위치해야 합니다.

#### FAC-003: Facade는 2개 이상 Manager 조합할 때만 사용
- **Category**: STRUCTURE
- **Description**: Facade는 2개 이상의 Manager를 조합할 때만 생성합니다. 단일 Manager 호출만 필요하면 Service에서 Manager를 직접 사용해야 합니다. 불필요한 Facade는 복잡도만 증가시킵니다.

#### FAC-004: Facade는 RDB Persistence Manager만 의존
- **Category**: DEPENDENCY
- **Description**: CommandFacade는 RDB PersistenceManager만 의존해야 합니다. 외부 API Manager(ClientPort 래퍼), Cache Manager, Messaging Manager 등은 Facade에서 호출하면 안 됩니다. 외부 호출이 필요하면 Service 레벨에서 처리합니다.

#### FAC-006: Facade에 비즈니스 로직 금지
- **Category**: BEHAVIOR
- **Description**: Facade에서 if/switch를 사용한 비즈니스 규칙, 계산 로직, 상태 판단 로직을 포함하면 안 됩니다. Facade는 여러 Manager 호출을 조합하는 역할만 수행하고, 비즈니스 로직은 Domain에 위임합니다.

#### ID-001: *Id 네이밍 필수
- **Category**: NAMING
- **Description**: ID Value Object는 *Id 네이밍 규칙 필수 (예: OrderId, UserId, ProductId). 식별자임을 명확히 표현합니다.

#### ID-002: Record 타입 필수
- **Category**: STRUCTURE
- **Description**: ID Value Object는 Java Record 타입으로 정의해야 합니다. 예: public record OrderId(Long value) {}

#### ID-003: of() 정적 팩토리 메서드 필수
- **Category**: STRUCTURE
- **Description**: ID Value Object는 public static XxxId of(Long/String value) 정적 팩토리 메서드가 필수입니다. 기존 ID 값을 복원할 때 사용합니다.

#### ID-004: Long ID forNew() 필수
- **Category**: STRUCTURE
- **Description**: Long 기반 ID VO는 public static XxxId forNew() 메서드가 필수입니다. 신규 Aggregate 생성 시 사용하며, Auto Increment를 위해 null을 반환합니다.

#### ID-005: Long ID forNew()는 null 반환
- **Category**: BEHAVIOR
- **Description**: Long ID의 forNew()는 new XxxId(null)을 반환해야 합니다. JPA Auto Increment가 persist 시점에 실제 ID를 할당합니다.

#### ID-006: Long ID isNew() 필수
- **Category**: STRUCTURE
- **Description**: Long 기반 ID VO는 public boolean isNew() 메서드가 필수입니다. value == null 또는 value.equals(null)을 체크하여 신규 생성 여부를 반환합니다.

#### ID-007: String ID isNew() 금지
- **Category**: BEHAVIOR
- **Description**: String 기반 ID VO는 isNew() 메서드를 가지면 안 됩니다. UUID/UUIDv7/Snowflake 등 String ID는 생성 시점에 이미 값이 존재하므로 신규 여부를 ID로 판단할 수 없습니다.

#### ID-008: String ID는 외부에서 주입
- **Category**: BEHAVIOR
- **Description**: String 기반 ID VO는 내부에서 UUID를 생성하지 않습니다. forNew(String value) 형태로 외부에서 String 값을 주입받습니다. Application Layer의 ID Generator가 UUIDv7, Snowflake 등을 생성하여 전달합니다.

#### ID-009: ID VO Lombok 금지
- **Category**: ANNOTATION
- **Description**: ID Value Object에서 @Getter, @Setter, @Data, @Builder, @Value, @RequiredArgsConstructor 등 모든 Lombok 어노테이션 사용을 금지합니다.

#### ID-010: ID VO JPA/Spring 금지
- **Category**: ANNOTATION
- **Description**: ID Value Object에서 @Entity, @Table, @Id, @Column, @Embeddable, @Component, @Service, @Repository 등 JPA/Spring 어노테이션 사용을 금지합니다.

#### QDR-001: Query 전용 (조회만)
- **Category**: BEHAVIOR
- **Description**: QueryDslRepository는 Query(조회) 전용입니다. save(), delete(), update() 등 상태 변경 메서드를 포함하면 안 됩니다. 상태 변경은 JpaRepository를 통해 수행합니다.

#### QDR-002: Join 절대 금지
- **Category**: BEHAVIOR
- **Description**: QueryDslRepository에서 .join(), .leftJoin(), .rightJoin(), .innerJoin() 등 모든 Join 구문을 사용하면 안 됩니다. 연관 데이터는 Application Layer에서 별도 조회 후 조합합니다.

#### QDR-003: findAll 금지 (OOM 방지)
- **Category**: BEHAVIOR
- **Description**: findAll() 메서드 정의를 금지합니다. 조건 없이 전체 데이터를 조회하면 OutOfMemory 위험이 있습니다. 반드시 Criteria 기반 조회(findByCriteria)와 페이징을 사용합니다.

#### QDR-008: QueryDslRepository에서 @Transactional 금지
- **Category**: ANNOTATION
- **Description**: QueryDslRepository 클래스 및 메서드에서 @Transactional 어노테이션을 사용하면 안 됩니다. 트랜잭션 경계는 Application Layer에서 관리합니다.

#### QDTO-001: Query DTO는 Record로 정의
- **Category**: STRUCTURE
- **Description**: Query DTO는 반드시 Java Record로 정의합니다. class 사용을 금지합니다. Query는 조회 조건을 담는 불변 객체입니다.

#### QDTO-005: Query DTO는 Domain 타입 의존 금지
- **Category**: DEPENDENCY
- **Description**: Query DTO의 필드는 primitive 타입, String, 표준 Java 타입만 사용합니다. Domain의 VO나 ID 타입을 직접 사용하면 안 됩니다. Domain 타입 변환은 QueryFactory에서 Criteria 생성 시 처리합니다.

#### QPRT-001: QueryPort는 반드시 interface로 정의
- **Category**: STRUCTURE
- **Description**: QueryPort(Port-Out)는 반드시 interface로 정의합니다. class나 abstract class는 금지됩니다. Persistence Layer의 QueryAdapter가 이 인터페이스를 구현합니다.

#### QPRT-004: QueryPort는 Domain 객체 반환 (Entity/DTO 금지)
- **Category**: BEHAVIOR
- **Description**: QueryPort의 조회 메서드는 Domain 객체(Aggregate, Entity(Domain), VO)를 반환해야 합니다. JPA Entity나 DTO를 직접 반환하면 안 됩니다. Entity→Domain 변환은 Adapter 내부에서 처리합니다.

#### QPRT-005: QueryPort의 복합 조건 조회는 Criteria 사용
- **Category**: STRUCTURE
- **Description**: findByXxxAndYyy(), findByStatusAndDate() 형태의 복합 조건 메서드는 금지됩니다. 복합 조건은 Criteria VO를 사용하여 findByCriteria(Criteria)로 처리합니다. Criteria는 Domain Layer에 정의됩니다.

#### RDTO-001: Response DTO는 Record로 정의
- **Category**: STRUCTURE
- **Description**: Response DTO는 반드시 Java Record로 정의합니다. class 사용을 금지합니다. Response는 조회 결과를 담는 불변 객체입니다.

#### RDTO-008: Response DTO는 Domain 타입 의존 금지
- **Category**: DEPENDENCY
- **Description**: Response DTO의 필드는 primitive 타입, String, 표준 Java 타입만 사용합니다. Domain의 VO나 ID 타입을 직접 노출하면 안 됩니다. Domain → Response 변환은 Assembler에서 값 추출하여 처리합니다.

#### REPO-001: Command 전용 (save/delete만)
- **Category**: BEHAVIOR
- **Description**: JpaRepository는 Command(저장/삭제) 전용입니다. JpaRepository<Entity, Long>을 상속하면 save(), delete()만 사용합니다. 조회는 QueryDslRepository로 분리합니다.

#### REPO-002: Query Method 금지
- **Category**: BEHAVIOR
- **Description**: JpaRepository에 findByName(), findByEmailAndStatus() 등 Query Method를 정의하면 안 됩니다. 모든 조회 메서드는 QueryDslRepository에서 JPAQueryFactory로 구현합니다.

#### REPO-003: @Query 어노테이션 금지
- **Category**: ANNOTATION
- **Description**: @Query 어노테이션을 사용한 JPQL/Native Query 정의를 금지합니다. 문자열 기반 쿼리는 컴파일 타임 오류 감지 불가하며, QueryDSL의 타입 안전 쿼리로 대체합니다.

#### REPO-004: QuerydslPredicateExecutor 금지
- **Category**: STRUCTURE
- **Description**: JpaRepository에서 QuerydslPredicateExecutor<Entity>를 상속하면 안 됩니다. JpaRepository는 순수 Command 전용으로 유지하고, 조회는 별도 QueryDslRepository 클래스에서 처리합니다.

#### REPO-005: Repository에서 @Transactional 금지
- **Category**: ANNOTATION
- **Description**: JpaRepository 인터페이스나 구현체에서 @Transactional 어노테이션을 사용하면 안 됩니다. 트랜잭션 경계는 Application Layer(UseCase/Service)에서 관리합니다.

#### SCHS-003: Application Layer에 @Scheduled 어노테이션 금지
- **Category**: ANNOTATION
- **Description**: Application Layer의 Scheduler Service에는 @Scheduled 어노테이션을 사용하지 않습니다. @Scheduled는 adapter-in/scheduler에서만 사용합니다. Application Layer는 순수 비즈니스 로직만 포함합니다.

#### SCHS-004: Scheduler Service는 분산락(LockManager) 사용 필수
- **Category**: BEHAVIOR
- **Description**: Scheduler Service 실행 시작 시 LockManager를 통해 분산락을 획득해야 합니다. 다중 인스턴스 환경에서 동일 작업이 중복 실행되는 것을 방지합니다. 락 획득 실패 시 즉시 리턴합니다.

#### SCHS-005: Scheduler Service는 Manager를 통해 Port 사용 (Port 직접 주입 금지)
- **Category**: DEPENDENCY
- **Description**: Scheduler Service는 Port(ClientPort 등)를 직접 주입받지 않습니다. 반드시 Manager(ClientManager)를 통해 외부 호출을 수행합니다. 예: PaymentClientPort 대신 PaymentClientManager를 주입받습니다.

#### SCHS-007: Scheduler Service 외부 호출 시 멱등키(outboxId) 필수 전달
- **Category**: BEHAVIOR
- **Description**: Scheduler Service가 ClientManager를 통해 외부 API를 호출할 때 outboxId를 멱등키로 전달합니다. ClientManager가 실제 HTTP 헤더나 요청 파라미터로 멱등키를 설정합니다. EventListener에서 이미 성공했더라도 재호출이 안전합니다.

#### SCHS-012: Scheduler Service는 finally 블록에서 분산락 해제
- **Category**: BEHAVIOR
- **Description**: 분산락 획득 후에는 반드시 try-finally 패턴을 사용하여 finally에서 락을 해제합니다. 예외 발생 시에도 락이 해제되어야 다음 실행 주기에 정상 동작합니다.

#### SVC-002: UseCase(Port-In) 인터페이스 구현 필수
- **Category**: STRUCTURE
- **Description**: Service 클래스는 반드시 해당 UseCase 인터페이스를 구현해야 합니다. (예: CreateOrderService implements CreateOrderUseCase) UseCase 인터페이스 없이 Service만 존재하면 안 됩니다.

#### SVC-003: Domain 객체 직접 생성 금지
- **Category**: BEHAVIOR
- **Description**: Service에서 new Order(), Order.forNew() 등 Domain 객체를 직접 생성하면 안 됩니다. 모든 Domain/UpdateData/Bundle 생성은 Factory에 위임해야 합니다.

#### SVC-005: Domain 객체 직접 반환 금지
- **Category**: BEHAVIOR
- **Description**: Service의 execute() 메서드에서 Domain 객체(Aggregate, Entity, VO)를 직접 반환하면 안 됩니다. 반드시 Assembler를 통해 Response DTO로 변환하여 반환해야 합니다.

#### SVC-006: Service에서 @Transactional 절대 금지
- **Category**: ANNOTATION
- **Description**: Service 클래스와 메서드에 @Transactional을 사용하면 안 됩니다. 트랜잭션 관리는 Manager/Facade의 책임입니다. Service는 비즈니스 흐름 조율만 담당합니다.

#### SVC-007: Service에 비즈니스 로직 금지
- **Category**: BEHAVIOR
- **Description**: Service에서 if/switch를 사용한 비즈니스 규칙, 계산 로직, 상태 판단 로직을 포함하면 안 됩니다. (예: if(order.isPaymentCompleted()) 금지) 모든 비즈니스 로직은 Domain에 위임합니다.

#### SVC-008: Service에서 Port(Out) 직접 주입 금지
- **Category**: DEPENDENCY
- **Description**: Service에서 CommandPort, QueryPort를 직접 주입받으면 안 됩니다. 반드시 PersistenceManager, ReadManager를 통해 접근해야 합니다.

#### SVC-009: Service에서 시간/ID 생성 금지
- **Category**: BEHAVIOR
- **Description**: Service에서 Instant.now(), UUID.randomUUID() 등 시간이나 ID 생성 로직을 직접 호출하면 안 됩니다. 시간/ID 생성은 Factory의 책임입니다.

#### SVC-010: Service에서 이벤트 객체 생성 금지
- **Category**: BEHAVIOR
- **Description**: Service에서 new OrderCreatedEvent(...) 형태로 이벤트 객체를 직접 생성하면 안 됩니다. 이벤트는 Domain 내부에서 생성되고, Service는 TransactionEventRegistry로 발행만 담당합니다.

#### UC-001: UseCase는 반드시 interface로 정의
- **Category**: STRUCTURE
- **Description**: UseCase(Port-In)는 반드시 interface로 정의합니다. class나 abstract class는 금지됩니다. Service가 이 인터페이스를 구현합니다.

#### UC-002: UseCase는 execute() 단일 메서드 제공
- **Category**: STRUCTURE
- **Description**: UseCase 인터페이스는 execute(Command) 또는 execute(Query) 단일 메서드만 정의합니다. 여러 메서드를 정의하면 안 됩니다. (예: execute(CreateOrderCommand): OrderIdResponse)

#### VAL-003: Validator는 ReadManager만 의존
- **Category**: DEPENDENCY
- **Description**: Validator는 검증을 위해 ReadManager(조회용)만 의존합니다. PersistenceManager(쓰기용)나 CommandPort/QueryPort를 직접 의존하면 안 됩니다.

#### VO-001: Record 타입 필수
- **Category**: STRUCTURE
- **Description**: Value Object는 Java Record 타입으로 정의해야 합니다 (Enum/Interface 제외). 예: public record Email(String value) {}

#### VO-002: of() 정적 팩토리 메서드 필수
- **Category**: STRUCTURE
- **Description**: Value Object는 public static XxxVo of(...) 정적 팩토리 메서드가 필수입니다. 생성 시 검증 로직을 포함합니다.

#### VO-003: Compact Constructor 검증 필수
- **Category**: BEHAVIOR
- **Description**: Record의 Compact Constructor에서 필드 검증이 필수입니다 (null 체크, 빈값 체크, 형식 검증 등). 예: public Email { if (value == null || value.isBlank()) throw ... }

#### VO-007: VO Lombok 금지
- **Category**: ANNOTATION
- **Description**: Value Object에서 @Getter, @Setter, @Data, @Builder, @Value, @RequiredArgsConstructor, @AllArgsConstructor 등 모든 Lombok 어노테이션 사용을 금지합니다.

#### VO-008: VO JPA 금지
- **Category**: ANNOTATION
- **Description**: Value Object에서 @Entity, @Table, @Id, @Column, @Embeddable, @Embedded 등 JPA 어노테이션 사용을 금지합니다.

#### VO-009: VO Spring 금지
- **Category**: ANNOTATION
- **Description**: Value Object에서 @Component, @Service, @Repository, @Transactional, @Autowired 등 Spring 어노테이션 사용을 금지합니다.


### PERSISTENCE Layer

#### EMAP-002: Pure Java (Lombok/MapStruct 금지)
- **Category**: ANNOTATION
- **Description**: EntityMapper는 순수 Java로 구현합니다. @Mapper(MapStruct), @Data, @Builder 등 모든 코드 생성 라이브러리 사용을 금지합니다.

#### EMAP-003: 시간 필드 생성 금지 (Instant.now() 금지)
- **Category**: BEHAVIOR
- **Description**: Mapper에서 Instant.now(), LocalDateTime.now(), System.currentTimeMillis() 등 시간 필드를 직접 생성하면 안 됩니다. 시간은 Domain에서 주입받거나 JPA Auditing(@CreatedDate, @LastModifiedDate)으로 처리합니다.

#### QADP-001: QueryDslRepository 위임만
- **Category**: BEHAVIOR
- **Description**: QueryAdapter는 QueryDslRepository로 조회를 위임하고 Mapper로 변환하는 역할만 수행합니다. 직접 JPAQueryFactory를 사용하거나 쿼리 로직을 포함하면 안 됩니다.

#### QADP-002: QueryAdapter에서 @Transactional 금지
- **Category**: ANNOTATION
- **Description**: QueryAdapter 클래스 및 메서드에서 @Transactional 어노테이션을 사용하면 안 됩니다. 읽기 전용 트랜잭션도 Application Layer에서 @Transactional(readOnly = true)로 관리합니다.

#### QADP-008: QueryAdapter에 비즈니스 로직 금지
- **Category**: BEHAVIOR
- **Description**: QueryAdapter에 if/switch 조건 분기, 필터링 로직, 정렬 로직 등 비즈니스 로직을 포함하면 안 됩니다. 단순히 Repository 호출 후 Mapper 변환만 수행합니다.


### REST_API Layer

#### CFG-001: OpenApiConfig 중앙 설정 필수
- **Category**: STRUCTURE
- **Description**: OpenAPI 설정은 config/OpenApiConfig.java에서 중앙 관리합니다. API 정보(title, version, description), Security Scheme(Bearer Token), 공통 스키마(ApiResponse, ProblemDetail)를 정의합니다.

#### CFG-002: JacksonConfig 중앙 설정 필수
- **Category**: STRUCTURE
- **Description**: Jackson ObjectMapper 설정은 config/JacksonConfig.java에서 중앙 관리합니다. PropertyNamingStrategy, JavaTimeModule, Date 형식, Null 처리 등을 전역 설정합니다.

#### CTR-001: Thin Controller 패턴 적용
- **Category**: BEHAVIOR
- **Description**: Controller는 HTTP 요청을 UseCase로 전달하고, 결과를 HTTP 응답으로 반환하는 역할만 수행합니다. 비즈니스 로직, Domain 객체 직접 생성/조작, 예외 처리(try-catch) 등을 Controller에 포함하면 안 됩니다.

#### CTR-002: ResponseEntity<ApiResponse<T>> 래핑 필수
- **Category**: STRUCTURE
- **Description**: 모든 Controller 응답은 ResponseEntity<ApiResponse<T>> 형식으로 반환해야 합니다. ApiResponse만 반환하면 HTTP 상태 코드 제어가 불가하고, ResponseEntity만 반환하면 표준 응답 형식을 준수하지 못합니다.

#### CTR-004: DELETE 메서드 금지 (소프트 삭제는 PATCH)
- **Category**: BEHAVIOR
- **Description**: DELETE 엔드포인트는 지원하지 않습니다. 소프트 삭제는 PATCH /{id}/delete 형태로 상태를 DELETED로 변경합니다. @DeleteMapping 어노테이션 사용이 금지됩니다.

#### CTR-005: Controller에서 @Transactional 사용 금지
- **Category**: ANNOTATION
- **Description**: Controller에서 @Transactional 어노테이션을 사용하면 안 됩니다. 트랜잭션 관리는 UseCase(Service) 계층의 책임입니다.

#### CTR-007: Controller에 비즈니스 로직 포함 금지
- **Category**: BEHAVIOR
- **Description**: Controller에 if/switch를 사용한 비즈니스 규칙, 계산 로직, 상태 변경 로직 등을 포함하면 안 됩니다. Controller는 Mapper 변환과 UseCase 호출만 수행합니다.

#### CTR-008: Controller에서 Domain 객체 직접 생성/조작 금지
- **Category**: DEPENDENCY
- **Description**: Controller에서 Domain Entity, Aggregate, Value Object를 직접 생성하거나 조작하면 안 됩니다. Domain 변환은 Application Layer의 Assembler 책임입니다.

#### CTR-009: Controller에서 Lombok 사용 금지
- **Category**: ANNOTATION
- **Description**: @Data, @Builder, @Getter, @Setter, @RequiredArgsConstructor 등 모든 Lombok 어노테이션 사용이 금지됩니다. 생성자 주입은 명시적으로 작성합니다.

#### DTO-001: Request/Response DTO는 Java 21 Record 사용 필수
- **Category**: STRUCTURE
- **Description**: 모든 Request DTO와 Response DTO는 public record 키워드로 정의합니다. Class로 정의하면 안 됩니다. Nested DTO도 record로 정의합니다.

#### DTO-002: Request DTO @NotNull 필수 (Nullable 금지)
- **Category**: ANNOTATION
- **Description**: Command/Query Request DTO의 모든 필드에 @NotNull (또는 @NotBlank, @NotEmpty)이 필수입니다. Nullable 필드는 허용하지 않습니다. 변경 여부는 서버에서 기존 값과 비교하여 판단합니다. Optional 필드가 필요한 경우에도 클라이언트가 현재 값을 그대로 보내도록 합니다.

#### DTO-005: DTO에서 Lombok 사용 금지
- **Category**: ANNOTATION
- **Description**: @Data, @Builder, @Getter, @Setter, @NoArgsConstructor, @AllArgsConstructor 등 모든 Lombok 어노테이션 사용이 금지됩니다. Java 21 Record 기능으로 대체합니다.

#### DTO-006: DTO에서 Jackson 어노테이션 금지
- **Category**: ANNOTATION
- **Description**: @JsonFormat, @JsonProperty, @JsonIgnore, @JsonInclude 등 Jackson 어노테이션 사용이 금지됩니다. 필드명은 camelCase를 사용하고, 전역 ObjectMapper 설정으로 처리합니다.

#### DTO-007: DTO에 Domain 변환 메서드 금지
- **Category**: BEHAVIOR
- **Description**: DTO에 toDomain(), toEntity(), toAggregate() 등 Domain 객체 변환 메서드를 포함하면 안 됩니다. API DTO → Application DTO 변환은 Mapper, Application DTO → Domain 변환은 Assembler 책임입니다.

#### DTO-008: DTO에 비즈니스 로직 메서드 금지
- **Category**: BEHAVIOR
- **Description**: DTO에 isVip(), calculateTotal(), validate() 등 비즈니스 로직 메서드를 포함하면 안 됩니다. DTO는 데이터 전송 객체로서 데이터만 담습니다.

#### DTO-011: DTO에 Setter 메서드 금지
- **Category**: STRUCTURE
- **Description**: DTO는 불변 객체여야 합니다. Setter 메서드를 정의하면 안 됩니다. Record는 기본적으로 불변이므로 Setter가 없습니다.

#### DTO-012: DTO에 Spring 어노테이션 금지
- **Category**: ANNOTATION
- **Description**: @Component, @Service, @Repository, @Controller 등 Spring 어노테이션을 DTO에 사용하면 안 됩니다. DTO는 순수 데이터 객체입니다.

#### DTO-014: ApiResponse 원시타입 래핑 금지
- **Category**: STRUCTURE
- **Description**: ApiResponse<T>의 제네릭 타입으로 원시타입(String, Long, Boolean 등)을 직접 사용할 수 없습니다. 단순한 응답이라도 반드시 *ApiResponse 형태의 Response DTO를 생성하여 래핑합니다. 예: ApiResponse<String> ❌ → ApiResponse<OrderIdApiResponse> ✅

#### MAP-001: Mapper는 @Component Bean 등록 필수
- **Category**: ANNOTATION
- **Description**: Mapper 클래스는 @Component 어노테이션으로 Spring Bean 등록합니다. Static 메서드로 구현하면 안 됩니다. 생성자 주입으로 Controller에 DI합니다.

#### MAP-002: Mapper에서 Static 메서드 금지
- **Category**: STRUCTURE
- **Description**: Mapper의 변환 메서드를 static으로 정의하면 안 됩니다. 인스턴스 메서드로 구현하여 DI가 가능하게 합니다. MapperUtil 등 Static 클래스 금지.

#### MAP-004: Mapper는 필드 매핑만 수행
- **Category**: BEHAVIOR
- **Description**: Mapper는 API DTO ↔ Application DTO 간 필드 매핑만 수행합니다. 비즈니스 로직, 검증 로직, 기본값 설정, 계산 로직 등을 포함하면 안 됩니다.

#### MAP-005: Mapper에 비즈니스 로직 금지
- **Category**: BEHAVIOR
- **Description**: if/switch를 사용한 조건부 로직, 계산(tax, discount), 상태 변환(enum → label), 기본값 설정 등을 Mapper에 포함하면 안 됩니다. 단순 필드 복사만 수행합니다.

#### MAP-006: Mapper에서 Domain 객체 직접 사용 금지
- **Category**: DEPENDENCY
- **Description**: Mapper에서 Domain Entity, Aggregate, Value Object를 직접 import하거나 사용하면 안 됩니다. API DTO ↔ Application DTO 변환만 담당합니다.

#### MAP-007: Mapper에서 Lombok 사용 금지
- **Category**: ANNOTATION
- **Description**: @Data, @Builder, @RequiredArgsConstructor 등 모든 Lombok 어노테이션 사용이 금지됩니다. 생성자와 의존성 필드는 명시적으로 작성합니다.

#### MAP-008: Mapper에 Repository/UseCase 주입 금지
- **Category**: DEPENDENCY
- **Description**: Mapper에 Repository, UseCase, Service 등 다른 계층의 컴포넌트를 주입하면 안 됩니다. 허용되는 의존성: MessageSource, ObjectMapper 등 인프라 유틸리티만 가능합니다.

#### TEST-001: @WebMvcTest + RestDocsTestSupport 상속 필수
- **Category**: STRUCTURE
- **Description**: REST Docs 테스트는 @WebMvcTest(TargetController.class)와 RestDocsTestSupport 상속이 필수입니다. @SpringBootTest는 사용하지 않습니다. RestDocsTestSupport는 MockMvc, ObjectMapper, REST Docs 설정을 제공합니다.

#### TEST-002: @MockitoBean UseCase/Mapper Mock 필수
- **Category**: DEPENDENCY
- **Description**: Controller가 의존하는 UseCase, Mapper, 필터 의존성은 @MockitoBean으로 Mock 처리합니다. 실제 구현체 대신 Mock을 사용하여 Controller 레이어만 격리 테스트합니다.

#### TEST-003: REST Docs document() 문서화 필수
- **Category**: BEHAVIOR
- **Description**: 모든 REST Docs 테스트는 .andDo(document("identifier", ...))로 문서를 생성해야 합니다. identifier는 kebab-case로 작성합니다 (예: order-create, order-update).


### SCHEDULER Layer

#### TSCH-002: Thin Scheduler는 UseCase 인터페이스만 의존
- **Category**: DEPENDENCY
- **Description**: Thin Scheduler는 Application Layer의 Scheduler UseCase 인터페이스만 주입받습니다. Manager, Port, Repository 등 다른 컴포넌트를 직접 주입받으면 안 됩니다. 모든 비즈니스 로직은 UseCase 구현체에 위임합니다.

#### TSCH-003: Thin Scheduler의 @Scheduled 메서드는 UseCase.execute() 단일 호출
- **Category**: BEHAVIOR
- **Description**: @Scheduled 메서드는 useCase.execute() 한 줄만 포함합니다. 조건 분기, 예외 처리, 로깅 외 로직을 추가하면 안 됩니다. 분산락 획득, 배치 처리, 상태 업데이트는 모두 UseCase 구현체에서 처리합니다.

#### TSCH-004: Thin Scheduler에 비즈니스 로직 금지
- **Category**: BEHAVIOR
- **Description**: Thin Scheduler에 분산락 획득/해제, Outbox 조회, 상태 업데이트, 외부 API 호출 로직을 구현하지 않습니다. 이 모든 로직은 Application Layer의 Scheduler Service에서 처리합니다.

