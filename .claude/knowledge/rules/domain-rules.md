# DOMAIN Layer 코딩 규칙 (214개)

## 개요

- **총 규칙 수**: 214개
- **Zero-Tolerance**: 128개
- **일반 규칙**: 86개

## 요약 테이블

| Code | Name | Severity | Category | Zero-Tolerance |
|------|------|----------|----------|----------------|
| AGG-001 | Aggregate Lombok 어노테이션 금지 | BLOCKER | ANNOTATION | 🚨 |
| AGG-002 | Aggregate JPA 어노테이션 금지 | BLOCKER | ANNOTATION | 🚨 |
| AGG-003 | Aggregate Spring 어노테이션 금지 | BLOCKER | ANNOTATION | 🚨 |
| AGG-004 | forNew() 팩토리 메서드 필수 | BLOCKER | STRUCTURE | 🚨 |
| AGG-005 | reconstitute() 팩토리 메서드 필수 | BLOCKER | STRUCTURE | 🚨 |
| AGG-007 | Aggregate ID는 ID VO 사용 | BLOCKER | STRUCTURE | 🚨 |
| AGG-008 | isNew() 메서드 필수 | BLOCKER | STRUCTURE | 🚨 |
| AGG-009 | Aggregate 시간 필드는 Instant 타입 | BLOCKER | STRUCTURE | 🚨 |
| AGG-010 | Instant 파라미터 주입 (Instant.now() 금지) | BLOCKER | BEHAVIOR | 🚨 |
| AGG-012 | Aggregate Setter 메서드 금지 | BLOCKER | STRUCTURE | 🚨 |
| AGG-014 | Law of Demeter (Getter 체이닝 금지) | BLOCKER | BEHAVIOR | 🚨 |
| AGG-023 | Aggregate 외부 레이어 의존 금지 | BLOCKER | DEPENDENCY | 🚨 |
| AGG-024 | Aggregate에서 Repository 참조 금지 | BLOCKER | DEPENDENCY | 🚨 |
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
| CADP-001 | persist() 메서드만 제공 | BLOCKER | BEHAVIOR | 🚨 |
| CADP-002 | CommandAdapter에서 @Transactional 금지 | BLOCKER | ANNOTATION | 🚨 |
| CADP-007 | CommandAdapter에 비즈니스 로직 금지 | BLOCKER | BEHAVIOR | 🚨 |
| CDTO-001 | Command DTO는 Record로 정의 | BLOCKER | STRUCTURE | 🚨 |
| CDTO-006 | Command DTO에 Validation 어노테이션 금지 | BLOCKER | ANNOTATION | 🚨 |
| CDTO-007 | Command DTO는 Domain 타입 의존 금지 | BLOCKER | DEPENDENCY | 🚨 |
| CLPRT-001 | ClientPort는 반드시 interface로 정의 | BLOCKER | STRUCTURE | 🚨 |
| COMP-004 | Component는 Manager 의존 가능, Port 직접 의존 금지 | BLOCKER | DEPENDENCY | 🚨 |
| CPRT-001 | CommandPort는 반드시 interface로 정의 | BLOCKER | STRUCTURE | 🚨 |
| CPRT-002 | CommandPort는 persist(Domain) 메서드만 제공 | BLOCKER | STRUCTURE | 🚨 |
| CPRT-003 | CommandPort는 Domain 객체를 파라미터로 받음 | BLOCKER | BEHAVIOR | 🚨 |
| CRI-001 | domain.[bc].query.criteria 패키지 | BLOCKER | LOCATION | 🚨 |
| CRI-002 | *Criteria 네이밍 규칙 | BLOCKER | NAMING | 🚨 |
| CRI-003 | Criteria public 접근 제어자 | BLOCKER | STRUCTURE | 🚨 |
| CRI-004 | Record 타입 필수 | BLOCKER | STRUCTURE | 🚨 |
| CRI-005 | of() 정적 팩토리 메서드 필수 | BLOCKER | STRUCTURE | 🚨 |
| CRI-006 | Criteria Lombok 금지 | BLOCKER | ANNOTATION | 🚨 |
| CRI-007 | Criteria JPA 금지 | BLOCKER | ANNOTATION | 🚨 |
| CRI-008 | Criteria Spring 금지 | BLOCKER | ANNOTATION | 🚨 |
| CRI-009 | Criteria 외부 레이어 의존 금지 | BLOCKER | DEPENDENCY | 🚨 |
| ENT-001 | BaseAuditEntity 상속 필수 | BLOCKER | STRUCTURE | 🚨 |
| ENT-002 | Long FK 전략 (JPA 관계 어노테이션 금지) | BLOCKER | STRUCTURE | 🚨 |
| ENT-003 | Entity에서 Lombok 금지 | BLOCKER | ANNOTATION | 🚨 |
| ENT-005 | Setter 메서드 금지 | BLOCKER | BEHAVIOR | 🚨 |
| EVT-001 | DomainEvent 인터페이스 구현 필수 | BLOCKER | STRUCTURE | 🚨 |
| EVT-002 | Record 타입 필수 | BLOCKER | STRUCTURE | 🚨 |
| EVT-003 | occurredAt (Instant) 필드 필수 | BLOCKER | STRUCTURE | 🚨 |
| EVT-004 | from(Aggregate, Instant) 정적 팩토리 메서드 필수 | BLOCKER | STRUCTURE | 🚨 |
| EVT-005 | 과거형 네이밍 필수 (*Event) | BLOCKER | NAMING | 🚨 |
| EVT-006 | domain.[bc].event 패키지 위치 | BLOCKER | LOCATION | 🚨 |
| EVT-007 | Event Lombok 금지 | BLOCKER | ANNOTATION | 🚨 |
| EVT-008 | Event JPA 금지 | BLOCKER | ANNOTATION | 🚨 |
| EVT-009 | Event Spring 금지 | BLOCKER | ANNOTATION | 🚨 |
| EVT-010 | Event org.springframework.* 의존 금지 | BLOCKER | DEPENDENCY | 🚨 |
| EVT-011 | Event 외부 레이어 의존 금지 | BLOCKER | DEPENDENCY | 🚨 |
| EXC-001 | ErrorCode 인터페이스 구현 필수 | BLOCKER | STRUCTURE | 🚨 |
| EXC-002 | ErrorCode domain.[bc].exception 패키지 | BLOCKER | LOCATION | 🚨 |
| EXC-003 | ErrorCode Lombok 금지 | BLOCKER | ANNOTATION | 🚨 |
| EXC-004 | ErrorCode public 접근 제어자 | BLOCKER | STRUCTURE | 🚨 |
| EXC-005 | getCode() 메서드 필수 | BLOCKER | STRUCTURE | 🚨 |
| EXC-006 | getHttpStatus() int 타입 반환 | BLOCKER | STRUCTURE | 🚨 |
| EXC-007 | getMessage() 메서드 필수 | BLOCKER | STRUCTURE | 🚨 |
| EXC-008 | Spring HttpStatus 사용 금지 | BLOCKER | DEPENDENCY | 🚨 |
| EXC-009 | DomainException 상속 필수 | BLOCKER | STRUCTURE | 🚨 |
| EXC-010 | Exception domain.[bc].exception 패키지 | BLOCKER | LOCATION | 🚨 |
| EXC-011 | Exception Lombok 금지 | BLOCKER | ANNOTATION | 🚨 |
| EXC-012 | Exception JPA 금지 | BLOCKER | ANNOTATION | 🚨 |
| EXC-013 | Exception Spring 금지 | BLOCKER | ANNOTATION | 🚨 |
| EXC-014 | Exception public 클래스 | BLOCKER | STRUCTURE | 🚨 |
| EXC-015 | RuntimeException 계층 (Unchecked) | BLOCKER | STRUCTURE | 🚨 |
| EXC-016 | Exception 외부 레이어 의존 금지 | BLOCKER | DEPENDENCY | 🚨 |
| EXC-017 | Exception Spring Framework 의존 금지 | BLOCKER | DEPENDENCY | 🚨 |
| EXC-019 | DomainException common 패키지 | BLOCKER | LOCATION | 🚨 |
| EXC-020 | ErrorCode 인터페이스 common 패키지 | BLOCKER | LOCATION | 🚨 |
| FAC-003 | Facade는 2개 이상 Manager 조합할 때만 사용 | BLOCKER | STRUCTURE | 🚨 |
| FAC-004 | Facade는 RDB Persistence Manager만 의존 | BLOCKER | DEPENDENCY | 🚨 |
| FAC-006 | Facade에 비즈니스 로직 금지 | BLOCKER | BEHAVIOR | 🚨 |
| ID-001 | *Id 네이밍 필수 | BLOCKER | NAMING | 🚨 |
| ID-002 | Record 타입 필수 | BLOCKER | STRUCTURE | 🚨 |
| ID-003 | of() 정적 팩토리 메서드 필수 | BLOCKER | STRUCTURE | 🚨 |
| ID-004 | Long ID forNew() 필수 | BLOCKER | STRUCTURE | 🚨 |
| ID-005 | Long ID forNew()는 null 반환 | BLOCKER | BEHAVIOR | 🚨 |
| ID-006 | Long ID isNew() 필수 | BLOCKER | STRUCTURE | 🚨 |
| ID-007 | String ID isNew() 금지 | BLOCKER | BEHAVIOR | 🚨 |
| ID-008 | String ID는 외부에서 주입 | BLOCKER | BEHAVIOR | 🚨 |
| ID-009 | ID VO Lombok 금지 | BLOCKER | ANNOTATION | 🚨 |
| ID-010 | ID VO JPA/Spring 금지 | BLOCKER | ANNOTATION | 🚨 |
| QDR-001 | Query 전용 (조회만) | BLOCKER | BEHAVIOR | 🚨 |
| QDR-002 | Join 절대 금지 | BLOCKER | BEHAVIOR | 🚨 |
| QDR-003 | findAll 금지 (OOM 방지) | BLOCKER | BEHAVIOR | 🚨 |
| QDR-008 | QueryDslRepository에서 @Transactional 금지 | BLOCKER | ANNOTATION | 🚨 |
| QDTO-001 | Query DTO는 Record로 정의 | BLOCKER | STRUCTURE | 🚨 |
| QDTO-005 | Query DTO는 Domain 타입 의존 금지 | BLOCKER | DEPENDENCY | 🚨 |
| QPRT-001 | QueryPort는 반드시 interface로 정의 | BLOCKER | STRUCTURE | 🚨 |
| QPRT-004 | QueryPort는 Domain 객체 반환 (Entity/DTO 금지) | BLOCKER | BEHAVIOR | 🚨 |
| QPRT-005 | QueryPort의 복합 조건 조회는 Criteria 사용 | BLOCKER | STRUCTURE | 🚨 |
| RDTO-001 | Response DTO는 Record로 정의 | BLOCKER | STRUCTURE | 🚨 |
| RDTO-008 | Response DTO는 Domain 타입 의존 금지 | BLOCKER | DEPENDENCY | 🚨 |
| REPO-001 | Command 전용 (save/delete만) | BLOCKER | BEHAVIOR | 🚨 |
| REPO-002 | Query Method 금지 | BLOCKER | BEHAVIOR | 🚨 |
| REPO-003 | @Query 어노테이션 금지 | BLOCKER | ANNOTATION | 🚨 |
| REPO-004 | QuerydslPredicateExecutor 금지 | BLOCKER | STRUCTURE | 🚨 |
| REPO-005 | Repository에서 @Transactional 금지 | BLOCKER | ANNOTATION | 🚨 |
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
| VO-001 | Record 타입 필수 | BLOCKER | STRUCTURE | 🚨 |
| VO-002 | of() 정적 팩토리 메서드 필수 | BLOCKER | STRUCTURE | 🚨 |
| VO-003 | Compact Constructor 검증 필수 | BLOCKER | BEHAVIOR | 🚨 |
| VO-007 | VO Lombok 금지 | BLOCKER | ANNOTATION | 🚨 |
| VO-008 | VO JPA 금지 | BLOCKER | ANNOTATION | 🚨 |
| VO-009 | VO Spring 금지 | BLOCKER | ANNOTATION | 🚨 |
| AGG-006 | protected 기본 생성자 | CRITICAL | STRUCTURE |  |
| AGG-011 | 상태 변경 시 updatedAt 갱신 | CRITICAL | BEHAVIOR |  |
| AGG-015 | Tell, Don't Ask 원칙 | CRITICAL | BEHAVIOR |  |
| AGG-016 | 복잡한 비즈니스 규칙은 VO로 위임 | CRITICAL | BEHAVIOR |  |
| AGG-017 | 상태 변경 시 도메인 이벤트 발행 | CRITICAL | BEHAVIOR |  |
| AGG-018 | registerEvent() protected 메서드 | CRITICAL | STRUCTURE |  |
| AGG-019 | pollEvents() 메서드 (이벤트 수확) | CRITICAL | STRUCTURE |  |
| AGG-025 | 불변식(Invariant) 검증 | CRITICAL | BEHAVIOR |  |
| C-008 | Tell, Don't Ask 원칙 준수 | CRITICAL | BEHAVIOR |  |
| CADP-006 | persist 후 ID 반환 | CRITICAL | BEHAVIOR |  |
| ENT-004 | of() 정적 팩토리 메서드 필수 | CRITICAL | STRUCTURE |  |
| EXC-018 | 명확한 비즈니스 네이밍 | CRITICAL | NAMING |  |
| QDR-009 | Entity 반환 (DTO 반환 금지) | CRITICAL | STRUCTURE |  |
| SVC-004 | UseCase 간 직접 호출 금지 | CRITICAL | DEPENDENCY |  |
| VO-004 | Enum VO displayName() 필수 | CRITICAL | STRUCTURE |  |
| VO-005 | LockKey 인터페이스 구현 (선택적) | CRITICAL | STRUCTURE |  |
| VO-006 | CacheKey 인터페이스 구현 (선택적) | CRITICAL | STRUCTURE |  |
| AGG-013 | Aggregate Getter 최소화 | MAJOR | STRUCTURE |  |
| AGG-020 | Command 메서드는 동사로 시작 | MAJOR | NAMING |  |
| AGG-021 | Query 메서드는 get/is/has/can으로 시작 | MAJOR | NAMING |  |
| AGG-022 | 판단 메서드는 boolean 반환 | MAJOR | BEHAVIOR |  |
| AGG-026 | equals/hashCode는 ID 기반 | MAJOR | STRUCTURE |  |
| BDL-001 | PersistBundle은 Class로 정의 (mutable) | MAJOR | STRUCTURE |  |
| BDL-002 | QueryBundle은 Record로 정의 (immutable) | MAJOR | STRUCTURE |  |
| BDL-003 | PersistBundle은 {Domain}PersistBundle 네이밍 | MAJOR | NAMING |  |
| BDL-004 | QueryBundle은 {Domain}QueryBundle 네이밍 | MAJOR | NAMING |  |
| BDL-005 | PersistBundle은 with{Id}() 패턴으로 ID 세팅 | MAJOR | BEHAVIOR |  |
| BDL-006 | Bundle은 ID Getter를 위임 메서드로 제공 (Law of Demeter) | MAJOR | BEHAVIOR |  |
| BDL-007 | Bundle은 Domain 객체를 직접 포함 | MAJOR | STRUCTURE |  |
| CADP-003 | @Component 어노테이션 사용 | MAJOR | ANNOTATION |  |
| CADP-004 | Command Port 구현 | MAJOR | STRUCTURE |  |
| CADP-005 | Mapper + Repository 의존 | MAJOR | DEPENDENCY |  |
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
| ENT-006 | protected/private 생성자 사용 | MAJOR | STRUCTURE |  |
| ENT-007 | SoftDeletableEntity 적용 (논리삭제) | MAJOR | STRUCTURE |  |
| ENT-008 | ID Auto Increment 전략 | MAJOR | STRUCTURE |  |
| FAC-001 | Facade는 @Component 어노테이션 사용 | MAJOR | ANNOTATION |  |
| FAC-002 | CommandFacade와 QueryFacade 분리 | MAJOR | STRUCTURE |  |
| FAC-005 | Facade는 @Transactional 메서드 단위 허용 | MAJOR | ANNOTATION |  |
| QDR-004 | findById 필수 메서드 | MAJOR | STRUCTURE |  |
| QDR-005 | existsById 필수 메서드 | MAJOR | STRUCTURE |  |
| QDR-006 | @Repository 클래스 어노테이션 | MAJOR | ANNOTATION |  |
| QDR-007 | JPAQueryFactory 생성자 주입 | MAJOR | DEPENDENCY |  |
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
| AGG-027 | 불변 필드는 final 선언 | MINOR | STRUCTURE |  |
| CRI-010 | 공통 VO 사용 권장 | MINOR | BEHAVIOR |  |
| ENT-009 | @Column nullable, length 명시 | MINOR | ANNOTATION |  |
| ENT-010 | @Table 테이블명 명시 | MINOR | ANNOTATION |  |
| QDR-010 | 메서드 네이밍 패턴 | MINOR | NAMING |  |
| QPRT-003 | QueryPort는 Unique 필드 조회 조건부 허용 | MINOR | STRUCTURE |  |

---

## 상세 규칙


### BLOCKER 규칙

#### AGG-001: Aggregate Lombok 어노테이션 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: ANNOTATION
- **Description**: Aggregate 클래스에서 @Getter, @Setter, @Data, @Builder, @NoArgsConstructor, @AllArgsConstructor 등 모든 Lombok 어노테이션 사용을 금지합니다. 순수 자바 코드로 작성해야 합니다.
- **Rationale**: Domain Layer의 순수성 유지. Lombok은 컴파일 타임 코드 생성으로 디버깅이 어렵고, Domain 로직의 명시성을 해칩니다. 모든 접근자와 생성자는 명시적으로 작성합니다.

#### AGG-002: Aggregate JPA 어노테이션 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: ANNOTATION
- **Description**: Aggregate 클래스에서 @Entity, @Table, @Column, @Id, @ManyToOne, @OneToMany 등 모든 JPA 어노테이션 사용을 금지합니다. 영속화 관심사는 Persistence Layer의 Entity에서 처리합니다.
- **Rationale**: Domain Layer는 인프라스트럭처에 의존하면 안 됩니다. JPA는 Persistence Layer의 관심사이며, Aggregate는 순수 비즈니스 로직만 담당합니다.

#### AGG-003: Aggregate Spring 어노테이션 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: ANNOTATION
- **Description**: Aggregate 클래스에서 @Component, @Service, @Repository, @Autowired, @Transactional 등 모든 Spring 어노테이션 사용을 금지합니다.
- **Rationale**: Domain Layer는 프레임워크에 의존하면 안 됩니다. Spring은 Application/Adapter Layer의 관심사이며, Aggregate는 POJO로 유지해야 합니다.

#### AGG-004: forNew() 팩토리 메서드 필수 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: STRUCTURE
- **Description**: Aggregate는 새로운 인스턴스 생성을 위한 static forNew(..., Instant now) 팩토리 메서드가 필수입니다. ID는 null(Long ID) 또는 외부 주입(String ID)하고, createdAt/updatedAt은 파라미터로 받은 Instant로 초기화합니다.
- **Rationale**: 생성자를 직접 노출하지 않고 팩토리 메서드를 통해 생성 의도를 명확히 합니다. forNew는 "새로 생성"임을 명시하고, 시간은 외부에서 주입받아 테스트 가능성을 확보합니다.

#### AGG-005: reconstitute() 팩토리 메서드 필수 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: STRUCTURE
- **Description**: Aggregate는 DB에서 조회한 데이터로 복원하기 위한 static reconstitute(...) 팩토리 메서드가 필수입니다. 모든 필드를 파라미터로 받아 복원합니다. 비즈니스 검증은 수행하지 않습니다.
- **Rationale**: forNew()와 reconstitute()를 분리하여 생성과 복원의 의도를 명확히 합니다. DB 데이터는 이미 검증된 것으로 간주하여 복원 시에는 검증을 생략합니다.

#### AGG-007: Aggregate ID는 ID VO 사용 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: STRUCTURE
- **Description**: Aggregate의 ID 필드는 원시 타입(Long) 대신 전용 ID VO(예: OrderId)를 사용해야 합니다. ID VO는 domain/{bc}/id 패키지에 위치합니다.
- **Rationale**: Primitive Obsession 안티패턴 방지. ID VO를 사용하면 타입 안전성이 보장되고, 실수로 다른 ID를 전달하는 것을 컴파일 타임에 방지합니다.

#### AGG-008: isNew() 메서드 필수 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: STRUCTURE
- **Description**: Aggregate는 새로 생성된 객체인지 판단하는 isNew() 메서드가 필수입니다. Long ID의 경우 id.value() == null 또는 id.isNew()로 판단합니다.
- **Rationale**: JPA의 merge/persist 결정, Outbox 이벤트 생성 여부 등에서 신규 생성 여부 판단이 필요합니다. ID가 null이면 아직 영속화되지 않은 새 객체입니다.

#### AGG-009: Aggregate 시간 필드는 Instant 타입 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: STRUCTURE
- **Description**: Aggregate의 모든 시간 필드(createdAt, updatedAt, expiredAt 등)는 java.time.Instant 타입을 사용합니다. LocalDateTime, LocalDate, Date 등은 금지됩니다.
- **Rationale**: Instant는 타임존 독립적인 시점을 나타냅니다. LocalDateTime은 타임존 정보가 없어 글로벌 서비스에서 혼란을 야기합니다. UTC 기준 저장 후 표현 계층에서 타임존 변환합니다.

#### AGG-010: Instant 파라미터 주입 (Instant.now() 금지) 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: BEHAVIOR
- **Description**: Aggregate 내부에서 Instant.now() 또는 System.currentTimeMillis() 직접 호출을 금지합니다. 시간이 필요한 메서드(forNew, 상태 변경 등)는 Instant 파라미터로 받습니다. 예: forNew(..., Instant now), cancel(Instant now).
- **Rationale**: 테스트 가능성 확보. Instant.now()는 테스트에서 시간을 고정할 수 없습니다. Instant 파라미터로 받으면 테스트에서 특정 시점을 주입할 수 있습니다. Application Layer에서 Instant.now()를 호출하여 전달합니다.

#### AGG-012: Aggregate Setter 메서드 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: STRUCTURE
- **Description**: Aggregate에서 setXxx() 형태의 Setter 메서드를 정의할 수 없습니다. 상태 변경은 비즈니스 의도를 드러내는 명시적 메서드(cancel, confirm, updatePrice 등)로 수행합니다.
- **Rationale**: Setter는 의미 없는 상태 변경을 허용합니다. 비즈니스 메서드는 변경의 의도를 명확히 하고, 불변식을 보장하며, 도메인 이벤트를 발행할 수 있습니다.

#### AGG-014: Law of Demeter (Getter 체이닝 금지) 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: BEHAVIOR
- **Description**: order.getCustomer().getAddress().getCity()와 같은 Getter 체이닝을 금지합니다. 필요한 정보는 Aggregate 메서드를 통해 직접 반환하거나, 위임 메서드를 제공합니다.
- **Rationale**: Law of Demeter(디미터 법칙). 객체 간 결합도를 낮추고 캡슐화를 강화합니다. 체이닝은 내부 구조 변경 시 호출부 전체 수정이 필요합니다.

#### AGG-023: Aggregate 외부 레이어 의존 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: DEPENDENCY
- **Description**: Aggregate는 application, adapter 패키지에 의존할 수 없습니다. domain 패키지와 java 표준 라이브러리만 import 가능합니다.
- **Rationale**: 헥사고날 아키텍처 원칙. Domain Layer는 가장 안쪽 계층으로 외부에 의존하면 안 됩니다. 의존성 역전을 통해 순수성을 유지합니다.

#### AGG-024: Aggregate에서 Repository 참조 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: DEPENDENCY
- **Description**: Aggregate 내부에서 Repository를 호출하여 다른 Aggregate를 조회할 수 없습니다. 필요한 경우 Application Layer에서 조회하여 파라미터로 전달합니다.
- **Rationale**: Aggregate는 영속화 관심사를 모르는 순수 도메인 객체입니다. 다른 Aggregate 참조가 필요하면 ID로만 참조하거나, Application Layer에서 조합합니다.

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

#### CADP-001: persist() 메서드만 제공 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: BEHAVIOR
- **Description**: CommandAdapter는 persist(Aggregate aggregate) 메서드만 제공합니다. save(), update(), delete() 등 별도 메서드를 정의하면 안 됩니다. Merge 방식으로 INSERT/UPDATE를 JPA가 판단합니다.
- **Rationale**: persist() Port 통합 원칙. Domain에서 isNew()로 신규 여부를 판단하고, Mapper가 Entity로 변환 후 JPA save()를 호출하면 자동으로 INSERT/UPDATE가 결정됩니다.

#### CADP-002: CommandAdapter에서 @Transactional 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: ANNOTATION
- **Description**: CommandAdapter 클래스 및 메서드에서 @Transactional 어노테이션을 사용하면 안 됩니다. 트랜잭션 경계는 Application Layer(UseCase/Service)에서 관리합니다.
- **Rationale**: 트랜잭션 범위를 비즈니스 로직 계층에서 제어. 여러 Aggregate를 하나의 트랜잭션으로 묶거나, 트랜잭션 전파 정책을 Application에서 결정합니다.

#### CADP-007: CommandAdapter에 비즈니스 로직 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: BEHAVIOR
- **Description**: CommandAdapter에 if/switch 조건 분기, 계산 로직, 상태 검증 등 비즈니스 로직을 포함하면 안 됩니다. 단순히 Domain→Entity 변환 후 저장만 수행합니다.
- **Rationale**: Adapter는 기술적 변환만 담당. 비즈니스 로직은 Domain과 Application Layer에서 처리하고, Adapter는 영속화 어댑터 역할에 집중합니다.

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

#### CRI-001: domain.[bc].query.criteria 패키지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: LOCATION
- **Description**: Criteria는 domain.[bc].query.criteria 패키지에 위치해야 합니다. 예: domain.order.query.criteria.OrderSearchCriteria.
- **Rationale**: Query 관련 객체를 query 하위 패키지로 분리하여 CQRS 패턴을 명확히 합니다.

#### CRI-002: *Criteria 네이밍 규칙 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: NAMING
- **Description**: Criteria는 *Criteria 또는 *SearchCriteria 네이밍 규칙이 필수입니다. 예: OrderCriteria, OrderSearchCriteria.
- **Rationale**: 네이밍만으로 조회 조건 객체임을 명확히 알 수 있어야 합니다.

#### CRI-003: Criteria public 접근 제어자 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: STRUCTURE
- **Description**: Criteria는 public 클래스가 필수입니다. 예: public record OrderSearchCriteria(...) {}
- **Rationale**: Application Layer, Adapter Layer에서 Criteria를 사용하기 위해 public이어야 합니다.

#### CRI-004: Record 타입 필수 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: STRUCTURE
- **Description**: Criteria는 Java Record 타입으로 정의해야 합니다. 불변성을 보장합니다. 예: public record OrderSearchCriteria(OrderStatus status, DateRange dateRange, PageRequest page) {}
- **Rationale**: Record는 불변성과 값 비교를 자동으로 보장합니다. Criteria는 조회 조건을 담는 값 객체이므로 불변이어야 합니다.

#### CRI-005: of() 정적 팩토리 메서드 필수 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: STRUCTURE
- **Description**: Criteria는 public static XxxCriteria of(...) 정적 팩토리 메서드가 필수입니다. 기본값 적용 및 검증 로직을 포함합니다.
- **Rationale**: of() 메서드에서 기본값(default page size 등)을 적용하고, 필수 조건 검증을 수행합니다.

#### CRI-006: Criteria Lombok 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: ANNOTATION
- **Description**: Criteria에서 @Getter, @Setter, @Data, @Builder, @Value, @RequiredArgsConstructor, @AllArgsConstructor 등 모든 Lombok 어노테이션 사용을 금지합니다.
- **Rationale**: Domain Layer 순수 자바 원칙. Record 타입은 이미 접근자와 equals/hashCode를 제공하므로 Lombok이 불필요합니다.

#### CRI-007: Criteria JPA 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: ANNOTATION
- **Description**: Criteria에서 @Entity, @Table, @Id, @Column, @Embeddable, @Embedded 등 JPA 어노테이션 사용을 금지합니다.
- **Rationale**: Domain Layer는 인프라스트럭처에 의존하면 안 됩니다.

#### CRI-008: Criteria Spring 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: ANNOTATION
- **Description**: Criteria에서 @Component, @Service, @Repository, @Transactional, @Autowired 등 Spring 어노테이션 사용을 금지합니다.
- **Rationale**: Domain Layer는 Spring Framework에 의존하면 안 됩니다.

#### CRI-009: Criteria 외부 레이어 의존 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: DEPENDENCY
- **Description**: Criteria에서 Application Layer, Adapter Layer, Bootstrap Layer 의존을 금지합니다.
- **Rationale**: 헥사고날 아키텍처의 의존성 역전 원칙. Domain Layer는 외부 레이어에 의존하면 안 됩니다.

#### ENT-001: BaseAuditEntity 상속 필수 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: STRUCTURE
- **Description**: 모든 JPA Entity는 BaseAuditEntity를 상속해야 합니다. createdAt, updatedAt 필드가 자동으로 관리됩니다. 논리삭제가 필요한 Entity는 SoftDeletableEntity를 상속합니다.
- **Rationale**: 감사 추적(Audit Trail) 일관성 보장. 모든 Entity에 생성/수정 시간이 자동 기록되어 데이터 변경 이력을 추적할 수 있습니다.

#### ENT-002: Long FK 전략 (JPA 관계 어노테이션 금지) 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: STRUCTURE
- **Description**: @ManyToOne, @OneToMany, @OneToOne, @ManyToMany 등 JPA 관계 어노테이션 사용을 금지합니다. 외래키는 Long userId 형태로 직접 관리합니다.
- **Rationale**: Lazy Loading 문제, N+1 쿼리 문제, 복잡한 영속성 컨텍스트 관리 문제를 원천 차단합니다. Application Layer에서 필요 시 별도 조회로 해결합니다.

#### ENT-003: Entity에서 Lombok 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: ANNOTATION
- **Description**: @Data, @Builder, @Getter, @Setter, @NoArgsConstructor, @AllArgsConstructor 등 모든 Lombok 어노테이션 사용을 금지합니다. JPA proxy와의 충돌 및 바이트코드 조작 문제를 방지합니다.
- **Rationale**: JPA Entity의 특성상 프록시 객체가 생성되며, Lombok의 equals/hashCode가 예상치 못한 문제를 일으킬 수 있습니다. 명시적 코드로 동작을 명확히 합니다.

#### ENT-005: Setter 메서드 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: BEHAVIOR
- **Description**: Entity에 Setter 메서드(set*)를 정의하면 안 됩니다. 상태 변경은 의미 있는 비즈니스 메서드(예: cancel(), complete())를 통해 수행합니다. 단, update 용도의 of() 재호출은 허용됩니다.
- **Rationale**: Entity의 일관성과 불변식 보장. 무분별한 상태 변경을 방지하고, 비즈니스 의도가 담긴 메서드로 상태 전이를 명확히 합니다.

#### EVT-001: DomainEvent 인터페이스 구현 필수 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: STRUCTURE
- **Description**: Domain Event는 DomainEvent 인터페이스를 구현해야 합니다. 예: public record OrderCreatedEvent(...) implements DomainEvent {}
- **Rationale**: DomainEvent 인터페이스를 통해 이벤트 타입을 식별하고, 공통 메서드(occurredAt 등)를 강제합니다.

#### EVT-002: Record 타입 필수 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: STRUCTURE
- **Description**: Domain Event는 Java Record 타입으로 정의해야 합니다. 불변성을 보장합니다. 예: public record OrderCreatedEvent(OrderId orderId, Instant occurredAt) implements DomainEvent {}
- **Rationale**: Event는 발생한 사실을 나타내므로 불변이어야 합니다. Record는 불변성과 값 비교를 자동으로 보장합니다.

#### EVT-003: occurredAt (Instant) 필드 필수 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: STRUCTURE
- **Description**: Domain Event는 Instant occurredAt 필드가 필수입니다. 이벤트 발생 시점을 기록합니다.
- **Rationale**: 이벤트 발생 시점은 이벤트 소싱, 감사 로그, 순서 정렬 등에 필수입니다. Instant 타입으로 타임존 독립적 시간을 저장합니다.

#### EVT-004: from(Aggregate, Instant) 정적 팩토리 메서드 필수 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: STRUCTURE
- **Description**: Domain Event는 public static XxxEvent from(Aggregate, Instant now) 정적 팩토리 메서드가 필수입니다. Aggregate에서 필요한 정보를 추출하여 Event를 생성합니다.
- **Rationale**: Aggregate의 내부 상태를 캡슐화하면서 필요한 정보만 Event로 전달합니다. Instant 파라미터로 테스트 가능성을 확보합니다.

#### EVT-005: 과거형 네이밍 필수 (*Event) 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: NAMING
- **Description**: Domain Event는 과거형 네이밍이 필수입니다. OrderCreatedEvent(✅), OrderCancelledEvent(✅), OrderPaidEvent(✅). 현재/미래형 금지: OrderCreateEvent(❌), OrderCancelEvent(❌).
- **Rationale**: Event는 "이미 발생한 사실"을 나타내므로 과거형이어야 합니다. 현재형/명령형은 Command와 혼동될 수 있습니다.

#### EVT-006: domain.[bc].event 패키지 위치 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: LOCATION
- **Description**: Domain Event는 domain.[bc].event 패키지에 위치해야 합니다. 예: domain.order.event.OrderCreatedEvent.
- **Rationale**: 도메인 이벤트는 도메인 레이어에 속하며, Bounded Context별로 이벤트를 그룹화하여 관리합니다.

#### EVT-007: Event Lombok 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: ANNOTATION
- **Description**: Domain Event에서 @Getter, @Setter, @Data, @Builder, @Value, @RequiredArgsConstructor, @AllArgsConstructor 등 모든 Lombok 어노테이션 사용을 금지합니다.
- **Rationale**: Domain Layer 순수 자바 원칙. Record 타입은 이미 접근자와 equals/hashCode를 제공하므로 Lombok이 불필요합니다.

#### EVT-008: Event JPA 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: ANNOTATION
- **Description**: Domain Event에서 @Entity, @Table, @Id, @Column, @Embeddable, @Embedded 등 JPA 어노테이션 사용을 금지합니다.
- **Rationale**: Domain Layer는 인프라스트럭처에 의존하면 안 됩니다. Event 영속화는 Outbox 패턴 등 별도 메커니즘으로 처리합니다.

#### EVT-009: Event Spring 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: ANNOTATION
- **Description**: Domain Event에서 @Component, @Service, @Repository, @Transactional, @Autowired, @EventListener 등 Spring 어노테이션 사용을 금지합니다.
- **Rationale**: Domain Layer는 Spring Framework에 의존하면 안 됩니다. Event Listener는 Application/Adapter Layer에서 구현합니다.

#### EVT-010: Event org.springframework.* 의존 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: DEPENDENCY
- **Description**: Domain Event에서 org.springframework.* 패키지 import를 금지합니다.
- **Rationale**: Domain Layer는 프레임워크에 독립적이어야 합니다. 순수 자바로 유지하여 테스트 용이성과 이식성을 확보합니다.

#### EVT-011: Event 외부 레이어 의존 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: DEPENDENCY
- **Description**: Domain Event에서 Application Layer, Adapter Layer, Bootstrap Layer 의존을 금지합니다. import *.application.*, *.adapter.*, *.bootstrap.* 금지.
- **Rationale**: 헥사고날 아키텍처의 의존성 역전 원칙. Domain Layer는 가장 안쪽에 위치하며 외부 레이어에 의존하면 안 됩니다.

#### EXC-001: ErrorCode 인터페이스 구현 필수 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: STRUCTURE
- **Description**: ErrorCode Enum은 ErrorCode 인터페이스를 구현해야 합니다. 예: public enum OrderErrorCode implements ErrorCode { ... }
- **Rationale**: ErrorCode 인터페이스를 통해 공통 메서드(getCode, getHttpStatus, getMessage)를 강제하고 일관성을 보장합니다.

#### EXC-002: ErrorCode domain.[bc].exception 패키지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: LOCATION
- **Description**: ErrorCode Enum은 domain.[bc].exception 패키지에 위치해야 합니다. 예: domain.order.exception.OrderErrorCode.
- **Rationale**: Bounded Context별로 ErrorCode를 그룹화하여 관리합니다.

#### EXC-003: ErrorCode Lombok 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: ANNOTATION
- **Description**: ErrorCode Enum에서 @Getter, @Setter, @Data, @RequiredArgsConstructor 등 Lombok 어노테이션 사용을 금지합니다.
- **Rationale**: Domain Layer 순수 자바 원칙. Enum은 간단하므로 Lombok이 불필요합니다.

#### EXC-004: ErrorCode public 접근 제어자 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: STRUCTURE
- **Description**: ErrorCode Enum은 public 접근 제어자가 필수입니다. 예: public enum OrderErrorCode implements ErrorCode { ... }
- **Rationale**: Application Layer, Adapter Layer에서 ErrorCode를 사용하기 위해 public이어야 합니다.

#### EXC-005: getCode() 메서드 필수 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: STRUCTURE
- **Description**: ErrorCode Enum은 String getCode() 메서드가 필수입니다. API 응답에서 사용되는 고유 에러 코드를 반환합니다.
- **Rationale**: 에러 코드는 클라이언트가 에러를 식별하고 처리하는 데 사용됩니다. 예: "ORDER_NOT_FOUND", "PAYMENT_FAILED".

#### EXC-006: getHttpStatus() int 타입 반환 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: STRUCTURE
- **Description**: ErrorCode Enum은 int getHttpStatus() 메서드가 필수입니다. Spring HttpStatus가 아닌 int 타입으로 반환합니다. 예: return 404;
- **Rationale**: Domain Layer가 Spring에 의존하지 않도록 int 타입을 사용합니다. Adapter Layer에서 HttpStatus로 변환합니다.

#### EXC-007: getMessage() 메서드 필수 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: STRUCTURE
- **Description**: ErrorCode Enum은 String getMessage() 메서드가 필수입니다. 사용자에게 표시되는 에러 메시지를 반환합니다.
- **Rationale**: 에러 메시지는 사용자가 에러 원인을 이해하는 데 도움을 줍니다.

#### EXC-008: Spring HttpStatus 사용 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: DEPENDENCY
- **Description**: ErrorCode Enum에서 org.springframework.http.HttpStatus 사용을 금지합니다. int 타입으로 HTTP 상태 코드를 저장합니다.
- **Rationale**: Domain Layer는 Spring Framework에 의존하면 안 됩니다. int 타입(400, 404, 500 등)으로 표현합니다.

#### EXC-009: DomainException 상속 필수 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: STRUCTURE
- **Description**: Concrete Exception은 DomainException을 상속해야 합니다. 예: public class OrderNotFoundException extends DomainException { ... }
- **Rationale**: DomainException 공통 기능(ErrorCode 연동 등)을 상속받아 일관된 예외 처리를 보장합니다.

#### EXC-010: Exception domain.[bc].exception 패키지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: LOCATION
- **Description**: Concrete Exception은 domain.[bc].exception 패키지에 위치해야 합니다. 예: domain.order.exception.OrderNotFoundException.
- **Rationale**: Bounded Context별로 Exception을 그룹화하여 관리합니다.

#### EXC-011: Exception Lombok 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: ANNOTATION
- **Description**: Exception에서 @Getter, @Setter, @Data, @Builder, @RequiredArgsConstructor 등 Lombok 어노테이션 사용을 금지합니다.
- **Rationale**: Domain Layer 순수 자바 원칙. Exception은 간단하므로 Lombok이 불필요합니다.

#### EXC-012: Exception JPA 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: ANNOTATION
- **Description**: Exception에서 @Entity, @Table, @Id, @Column 등 JPA 어노테이션 사용을 금지합니다.
- **Rationale**: Domain Layer는 인프라스트럭처에 의존하면 안 됩니다.

#### EXC-013: Exception Spring 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: ANNOTATION
- **Description**: Exception에서 @Component, @Service, @Repository, @Transactional 등 Spring 어노테이션 사용을 금지합니다.
- **Rationale**: Domain Layer는 Spring Framework에 의존하면 안 됩니다.

#### EXC-014: Exception public 클래스 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: STRUCTURE
- **Description**: Exception은 public 클래스가 필수입니다. 예: public class OrderNotFoundException extends DomainException { ... }
- **Rationale**: Application Layer, Adapter Layer에서 Exception을 catch하기 위해 public이어야 합니다.

#### EXC-015: RuntimeException 계층 (Unchecked) 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: STRUCTURE
- **Description**: Exception은 RuntimeException 계층이어야 합니다 (Unchecked Exception). DomainException은 RuntimeException을 상속합니다.
- **Rationale**: Unchecked Exception으로 메서드 시그니처를 깨끗하게 유지하고, 글로벌 예외 처리기에서 일괄 처리합니다.

#### EXC-016: Exception 외부 레이어 의존 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: DEPENDENCY
- **Description**: Exception에서 Application Layer, Adapter Layer, Bootstrap Layer 의존을 금지합니다.
- **Rationale**: 헥사고날 아키텍처의 의존성 역전 원칙. Domain Layer는 외부 레이어에 의존하면 안 됩니다.

#### EXC-017: Exception Spring Framework 의존 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: DEPENDENCY
- **Description**: Exception에서 org.springframework.* 패키지 import를 금지합니다.
- **Rationale**: Domain Layer는 프레임워크에 독립적이어야 합니다.

#### EXC-019: DomainException common 패키지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: LOCATION
- **Description**: DomainException 기본 클래스는 domain.common.exception 패키지에 위치해야 합니다.
- **Rationale**: 공통 Exception은 common 패키지에서 관리하여 모든 Bounded Context에서 재사용합니다.

#### EXC-020: ErrorCode 인터페이스 common 패키지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: LOCATION
- **Description**: ErrorCode 인터페이스는 domain.common.exception 패키지에 위치해야 합니다.
- **Rationale**: 공통 인터페이스는 common 패키지에서 관리하여 모든 Bounded Context에서 구현합니다.

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

#### ID-001: *Id 네이밍 필수 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: NAMING
- **Description**: ID Value Object는 *Id 네이밍 규칙 필수 (예: OrderId, UserId, ProductId). 식별자임을 명확히 표현합니다.
- **Rationale**: Primitive Obsession 방지. 타입 이름만으로 ID VO임을 명확히 알 수 있어 실수로 다른 타입의 ID를 전달하는 것을 컴파일 타임에 방지합니다.

#### ID-002: Record 타입 필수 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: STRUCTURE
- **Description**: ID Value Object는 Java Record 타입으로 정의해야 합니다. 예: public record OrderId(Long value) {}
- **Rationale**: Record는 equals/hashCode/toString을 자동 생성하고 불변성을 보장합니다. ID는 값 객체이므로 Record가 가장 적합합니다.

#### ID-003: of() 정적 팩토리 메서드 필수 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: STRUCTURE
- **Description**: ID Value Object는 public static XxxId of(Long/String value) 정적 팩토리 메서드가 필수입니다. 기존 ID 값을 복원할 때 사용합니다.
- **Rationale**: 생성 의도를 명확히 하고, 검증 로직을 포함할 수 있습니다. of()는 기존에 영속화된 ID를 복원하는 용도로 사용됩니다.

#### ID-004: Long ID forNew() 필수 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: STRUCTURE
- **Description**: Long 기반 ID VO는 public static XxxId forNew() 메서드가 필수입니다. 신규 Aggregate 생성 시 사용하며, Auto Increment를 위해 null을 반환합니다.
- **Rationale**: forNew()는 "새로 생성"의 의도를 명확히 합니다. DB Auto Increment 전략에서는 ID가 null인 상태로 persist해야 합니다.

#### ID-005: Long ID forNew()는 null 반환 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: BEHAVIOR
- **Description**: Long ID의 forNew()는 new XxxId(null)을 반환해야 합니다. JPA Auto Increment가 persist 시점에 실제 ID를 할당합니다.
- **Rationale**: JPA의 Auto Increment 전략 호환. ID가 null이면 insert, 값이 있으면 update로 판단합니다.

#### ID-006: Long ID isNew() 필수 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: STRUCTURE
- **Description**: Long 기반 ID VO는 public boolean isNew() 메서드가 필수입니다. value == null 또는 value.equals(null)을 체크하여 신규 생성 여부를 반환합니다.
- **Rationale**: Aggregate.isNew() 구현에 활용됩니다. JPA merge/persist 결정, Outbox 이벤트 생성 여부 판단 등에 사용됩니다.

#### ID-007: String ID isNew() 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: BEHAVIOR
- **Description**: String 기반 ID VO는 isNew() 메서드를 가지면 안 됩니다. UUID/UUIDv7/Snowflake 등 String ID는 생성 시점에 이미 값이 존재하므로 신규 여부를 ID로 판단할 수 없습니다.
- **Rationale**: String ID는 생성 시점에 값이 할당되므로 null 체크로 신규 여부를 판단할 수 없습니다. 필요 시 Aggregate에 별도 플래그를 사용하세요.

#### ID-008: String ID는 외부에서 주입 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: BEHAVIOR
- **Description**: String 기반 ID VO는 내부에서 UUID를 생성하지 않습니다. forNew(String value) 형태로 외부에서 String 값을 주입받습니다. Application Layer의 ID Generator가 UUIDv7, Snowflake 등을 생성하여 전달합니다.
- **Rationale**: ID 생성 전략(UUID, UUIDv7, Snowflake 등)을 Domain Layer에서 결정하면 유연성이 떨어집니다. Application/Infrastructure Layer에서 ID를 생성하여 Domain에 주입하면 전략 변경이 용이합니다.

#### ID-009: ID VO Lombok 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: ANNOTATION
- **Description**: ID Value Object에서 @Getter, @Setter, @Data, @Builder, @Value, @RequiredArgsConstructor 등 모든 Lombok 어노테이션 사용을 금지합니다.
- **Rationale**: Domain Layer 순수 자바 원칙. Record 타입은 이미 접근자와 equals/hashCode를 제공하므로 Lombok이 불필요합니다.

#### ID-010: ID VO JPA/Spring 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: ANNOTATION
- **Description**: ID Value Object에서 @Entity, @Table, @Id, @Column, @Embeddable, @Component, @Service, @Repository 등 JPA/Spring 어노테이션 사용을 금지합니다.
- **Rationale**: Domain Layer는 인프라스트럭처/프레임워크에 의존하면 안 됩니다. JPA 매핑은 Persistence Layer Entity에서 처리합니다.

#### QDR-001: Query 전용 (조회만) 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: BEHAVIOR
- **Description**: QueryDslRepository는 Query(조회) 전용입니다. save(), delete(), update() 등 상태 변경 메서드를 포함하면 안 됩니다. 상태 변경은 JpaRepository를 통해 수행합니다.
- **Rationale**: CQRS 패턴 적용. Query와 Command의 책임을 분리하여 조회 최적화와 변경 트랜잭션을 독립적으로 관리합니다.

#### QDR-002: Join 절대 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: BEHAVIOR
- **Description**: QueryDslRepository에서 .join(), .leftJoin(), .rightJoin(), .innerJoin() 등 모든 Join 구문을 사용하면 안 됩니다. 연관 데이터는 Application Layer에서 별도 조회 후 조합합니다.
- **Rationale**: Join으로 인한 N+1 문제, 복잡한 영속성 관리, 성능 예측 어려움을 원천 차단. Long FK 전략과 일관성 유지.

#### QDR-003: findAll 금지 (OOM 방지) 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: BEHAVIOR
- **Description**: findAll() 메서드 정의를 금지합니다. 조건 없이 전체 데이터를 조회하면 OutOfMemory 위험이 있습니다. 반드시 Criteria 기반 조회(findByCriteria)와 페이징을 사용합니다.
- **Rationale**: 대량 데이터 로드로 인한 OOM 방지. 항상 조건과 페이징을 강제하여 안정적인 조회를 보장합니다.

#### QDR-008: QueryDslRepository에서 @Transactional 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: ANNOTATION
- **Description**: QueryDslRepository 클래스 및 메서드에서 @Transactional 어노테이션을 사용하면 안 됩니다. 트랜잭션 경계는 Application Layer에서 관리합니다.
- **Rationale**: 조회 전용 Repository도 트랜잭션 경계를 비즈니스 로직 계층에서 제어. @Transactional(readOnly = true)도 UseCase에서 선언합니다.

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

#### REPO-001: Command 전용 (save/delete만) 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: BEHAVIOR
- **Description**: JpaRepository는 Command(저장/삭제) 전용입니다. JpaRepository<Entity, Long>을 상속하면 save(), delete()만 사용합니다. 조회는 QueryDslRepository로 분리합니다.
- **Rationale**: CQRS 패턴 적용. Command와 Query의 책임을 분리하여 각 Repository의 역할을 명확히 하고, 성능 최적화 포인트를 분리합니다.

#### REPO-002: Query Method 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: BEHAVIOR
- **Description**: JpaRepository에 findByName(), findByEmailAndStatus() 등 Query Method를 정의하면 안 됩니다. 모든 조회 메서드는 QueryDslRepository에서 JPAQueryFactory로 구현합니다.
- **Rationale**: Query Method의 메서드명 길이 증가, 복잡한 조건 표현 한계, 타입 안전성 부족 문제를 QueryDSL로 해결합니다.

#### REPO-003: @Query 어노테이션 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: ANNOTATION
- **Description**: @Query 어노테이션을 사용한 JPQL/Native Query 정의를 금지합니다. 문자열 기반 쿼리는 컴파일 타임 오류 감지 불가하며, QueryDSL의 타입 안전 쿼리로 대체합니다.
- **Rationale**: JPQL 문자열은 런타임에만 오류 발견 가능. QueryDSL은 Java 코드로 쿼리를 작성하여 컴파일 타임에 오류를 감지합니다.

#### REPO-004: QuerydslPredicateExecutor 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: STRUCTURE
- **Description**: JpaRepository에서 QuerydslPredicateExecutor<Entity>를 상속하면 안 됩니다. JpaRepository는 순수 Command 전용으로 유지하고, 조회는 별도 QueryDslRepository 클래스에서 처리합니다.
- **Rationale**: CQRS 분리 원칙 준수. Repository 인터페이스를 단순하게 유지하고, 복잡한 쿼리 로직을 별도 클래스로 캡슐화합니다.

#### REPO-005: Repository에서 @Transactional 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: ANNOTATION
- **Description**: JpaRepository 인터페이스나 구현체에서 @Transactional 어노테이션을 사용하면 안 됩니다. 트랜잭션 경계는 Application Layer(UseCase/Service)에서 관리합니다.
- **Rationale**: 트랜잭션 경계를 비즈니스 로직 계층에서 제어하여 일관된 트랜잭션 관리. Repository는 단순 데이터 접근 역할만 수행합니다.

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

#### VO-001: Record 타입 필수 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: STRUCTURE
- **Description**: Value Object는 Java Record 타입으로 정의해야 합니다 (Enum/Interface 제외). 예: public record Email(String value) {}
- **Rationale**: Record는 equals/hashCode/toString을 자동 생성하고 불변성을 보장합니다. VO는 값으로 비교되므로 Record가 가장 적합합니다.

#### VO-002: of() 정적 팩토리 메서드 필수 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: STRUCTURE
- **Description**: Value Object는 public static XxxVo of(...) 정적 팩토리 메서드가 필수입니다. 생성 시 검증 로직을 포함합니다.
- **Rationale**: 생성 의도를 명확히 하고, 검증 로직을 포함하여 유효한 객체만 생성되도록 보장합니다.

#### VO-003: Compact Constructor 검증 필수 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: BEHAVIOR
- **Description**: Record의 Compact Constructor에서 필드 검증이 필수입니다 (null 체크, 빈값 체크, 형식 검증 등). 예: public Email { if (value == null || value.isBlank()) throw ... }
- **Rationale**: VO는 항상 유효한 상태로만 존재해야 합니다. 생성자에서 검증하면 불변식(Invariant)을 보장할 수 있습니다.

#### VO-007: VO Lombok 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: ANNOTATION
- **Description**: Value Object에서 @Getter, @Setter, @Data, @Builder, @Value, @RequiredArgsConstructor, @AllArgsConstructor 등 모든 Lombok 어노테이션 사용을 금지합니다.
- **Rationale**: Domain Layer 순수 자바 원칙. Record 타입은 이미 접근자와 equals/hashCode를 제공하므로 Lombok이 불필요합니다.

#### VO-008: VO JPA 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: ANNOTATION
- **Description**: Value Object에서 @Entity, @Table, @Id, @Column, @Embeddable, @Embedded 등 JPA 어노테이션 사용을 금지합니다.
- **Rationale**: Domain Layer는 인프라스트럭처에 의존하면 안 됩니다. JPA 매핑은 Persistence Layer Entity에서 처리합니다.

#### VO-009: VO Spring 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: ANNOTATION
- **Description**: Value Object에서 @Component, @Service, @Repository, @Transactional, @Autowired 등 Spring 어노테이션 사용을 금지합니다.
- **Rationale**: Domain Layer는 Spring Framework에 의존하면 안 됩니다. VO는 순수 자바 객체로 유지해야 합니다.


### CRITICAL 규칙

#### AGG-006: protected 기본 생성자
- **Severity**: CRITICAL
- **Category**: STRUCTURE
- **Description**: Aggregate의 기본 생성자(no-args constructor)는 protected 접근 제어자를 사용합니다. public 또는 private 기본 생성자는 금지됩니다.
- **Rationale**: 외부에서 빈 생성자를 통한 잘못된 객체 생성을 방지합니다. protected는 같은 패키지와 하위 클래스에서만 접근 가능하며, 팩토리 메서드 사용을 강제합니다.

#### AGG-011: 상태 변경 시 updatedAt 갱신
- **Severity**: CRITICAL
- **Category**: BEHAVIOR
- **Description**: Aggregate의 상태를 변경하는 메서드(command method)는 반드시 updatedAt 필드를 갱신해야 합니다. Instant 파라미터를 통해 시간을 받습니다. 예: public void cancel(Instant now) { this.updatedAt = now; ... }
- **Rationale**: 변경 이력 추적 및 낙관적 잠금에 활용됩니다. updatedAt은 마지막 수정 시점을 나타내며, 감사(Audit) 목적으로도 사용됩니다.

#### AGG-015: Tell, Don't Ask 원칙
- **Severity**: CRITICAL
- **Category**: BEHAVIOR
- **Description**: 상태를 조회(Getter)해서 외부에서 판단하고 다시 설정하는 대신, Aggregate에게 직접 행동을 요청합니다. if (order.getStatus() == PENDING) order.confirm() ❌ → order.confirmIfPending() ✅
- **Rationale**: 비즈니스 로직이 Aggregate 외부로 유출되는 것을 방지합니다. 조건부 로직도 Aggregate 내부에서 처리하여 응집도를 높입니다.

#### AGG-016: 복잡한 비즈니스 규칙은 VO로 위임
- **Severity**: CRITICAL
- **Category**: BEHAVIOR
- **Description**: 복잡한 비즈니스 규칙(할인 계산, 유효성 검증 로직 등)은 Aggregate에 직접 구현하지 않고 전용 VO(Value Object)로 추출합니다. VO가 자체적으로 판단 로직을 갖습니다. 예: Money.add(), DiscountPolicy.calculate(), Email.isValid().
- **Rationale**: 단일 책임 원칙(SRP). Aggregate는 상태 관리와 불변식 보장에 집중하고, 비즈니스 규칙은 VO에 캡슐화합니다. VO는 불변이므로 테스트하기 쉽고 재사용 가능합니다.

#### AGG-017: 상태 변경 시 도메인 이벤트 발행
- **Severity**: CRITICAL
- **Category**: BEHAVIOR
- **Description**: 중요한 상태 변경(생성, 취소, 완료 등)이 발생하면 해당하는 도메인 이벤트를 registerEvent()로 등록합니다. 이벤트는 Aggregate 내부 리스트에 저장됩니다.
- **Rationale**: 이벤트 기반 아키텍처 지원. 도메인 이벤트를 통해 다른 바운디드 컨텍스트에 변경 사항을 전파하고, 감사 로그 생성, Outbox 패턴 구현 등에 활용합니다.

#### AGG-018: registerEvent() protected 메서드
- **Severity**: CRITICAL
- **Category**: STRUCTURE
- **Description**: Aggregate는 protected void registerEvent(DomainEvent event) 메서드를 가지며, 내부에서 이벤트 리스트에 추가합니다. 외부에서 직접 이벤트를 등록할 수 없습니다.
- **Rationale**: 이벤트 등록은 Aggregate의 상태 변경과 함께 이루어져야 합니다. 외부에서 임의로 이벤트를 등록하면 상태와 이벤트 불일치가 발생할 수 있습니다.

#### AGG-019: pollEvents() 메서드 (이벤트 수확)
- **Severity**: CRITICAL
- **Category**: STRUCTURE
- **Description**: Aggregate는 public List<DomainEvent> pollEvents() 메서드를 제공하여 등록된 이벤트를 반환하고 내부 리스트를 비웁니다. 호출할 때마다 새 리스트를 반환합니다.
- **Rationale**: 이벤트 중복 발행 방지. poll 방식으로 이벤트를 가져가면 내부 리스트가 비워져 같은 이벤트가 두 번 처리되지 않습니다.

#### AGG-025: 불변식(Invariant) 검증
- **Severity**: CRITICAL
- **Category**: BEHAVIOR
- **Description**: Aggregate의 상태 변경 메서드는 불변식을 위반하는 경우 DomainException을 발생시킵니다. 잘못된 상태 전이를 방지합니다. 예: 취소된 주문은 다시 취소할 수 없음.
- **Rationale**: Aggregate는 항상 유효한 상태를 유지해야 합니다. 불변식 위반 시 명확한 도메인 예외를 발생시켜 호출자에게 알립니다.

#### C-008: Tell, Don't Ask 원칙 준수
- **Severity**: CRITICAL
- **Category**: BEHAVIOR
- **Description**: Service에서 Domain의 상태를 조회(get)하여 판단 후 행동하는 패턴을 금지합니다. (예: if(order.getStatus() == PAID) { order.ship(); } 금지) 대신 order.processShipment() 형태로 Domain에 행동을 위임합니다.
- **Rationale**: 캡슐화 강화. 상태 판단 로직을 Domain 내부에 두어 비즈니스 규칙이 Domain에 집중되고, Service는 흐름 제어만 담당합니다.

#### CADP-006: persist 후 ID 반환
- **Severity**: CRITICAL
- **Category**: BEHAVIOR
- **Description**: persist() 메서드는 저장 후 생성된 ID(예: OrderId)를 반환합니다. Auto Increment로 생성된 ID를 Domain Value Object로 래핑하여 반환합니다.
- **Rationale**: Application Layer에서 생성된 ID로 후속 처리(이벤트 발행, 응답 생성)가 가능합니다. repository.save(entity).getId()로 즉시 ID를 얻습니다.

#### ENT-004: of() 정적 팩토리 메서드 필수
- **Severity**: CRITICAL
- **Category**: STRUCTURE
- **Description**: Entity 생성은 public static of(...) 팩토리 메서드를 통해서만 가능해야 합니다. 생성자는 protected 또는 private으로 선언합니다.
- **Rationale**: Entity 생성 시점의 불변식(invariant)을 보장하고, 명확한 생성 의도를 표현합니다. Mapper에서 Entity.of(...)로 호출합니다.

#### EXC-018: 명확한 비즈니스 네이밍
- **Severity**: CRITICAL
- **Category**: NAMING
- **Description**: Exception은 명확한 비즈니스 의미를 담은 네이밍을 사용합니다. 권장 패턴: NotFound, Invalid, AlreadyExists, Cannot, Failed, Duplicate, Conflict, Forbidden, Unauthorized, Expired 등.
- **Rationale**: Exception 이름만으로 에러 원인을 파악할 수 있어야 합니다. 예: OrderNotFoundException, PaymentFailedException.

#### QDR-009: Entity 반환 (DTO 반환 금지)
- **Severity**: CRITICAL
- **Category**: STRUCTURE
- **Description**: QueryDslRepository 메서드는 Entity를 반환해야 합니다. DTO Projection은 AdminQueryDslRepository(관리자 전용)에서만 허용됩니다. Adapter에서 Mapper를 통해 Domain으로 변환합니다.
- **Rationale**: Repository 계층의 책임 분리. DTO 변환은 Adapter 또는 Application Layer에서 수행하고, Repository는 Entity 영속화에 집중합니다.

#### SVC-004: UseCase 간 직접 호출 금지
- **Severity**: CRITICAL
- **Category**: DEPENDENCY
- **Description**: Service에서 다른 UseCase(Service)를 직접 주입받아 호출하면 안 됩니다. 순환 의존성이 발생할 수 있고, 트랜잭션 경계가 모호해집니다. Admin 조회 등 복합 조회가 필요한 경우 별도 Facade나 ReadManager 조합을 사용합니다.
- **Rationale**: 순환 의존성 방지 및 트랜잭션 경계 명확화. UseCase는 독립적인 비즈니스 액션을 나타내므로 서로 호출하지 않아야 합니다.

#### VO-004: Enum VO displayName() 필수
- **Severity**: CRITICAL
- **Category**: STRUCTURE
- **Description**: Enum Value Object는 public String displayName() 메서드가 필수입니다. 사용자에게 표시되는 한글/영문 이름을 반환합니다.
- **Rationale**: Enum의 name()은 코드용이고 displayName()은 사용자 표시용입니다. UI에서 직접 Enum을 사용할 때 필요합니다.

#### VO-005: LockKey 인터페이스 구현 (선택적)
- **Severity**: CRITICAL
- **Category**: STRUCTURE
- **Description**: [Lock 기능 사용 시 적용] *LockKey VO는 LockKey 인터페이스를 구현하고 value() 메서드가 필수입니다. 분산 락 키 생성에 사용됩니다. 예: public record OrderLockKey(Long orderId) implements LockKey { @Override public String value() { return "order:" + orderId; } }
- **Rationale**: LockKey 인터페이스를 통해 락 키 생성 방식을 표준화합니다. Domain Layer에서 락 키의 형식과 네이밍을 제어할 수 있습니다.

#### VO-006: CacheKey 인터페이스 구현 (선택적)
- **Severity**: CRITICAL
- **Category**: STRUCTURE
- **Description**: [Cache 기능 사용 시 적용] *CacheKey VO는 CacheKey 인터페이스를 구현하고 value() 메서드가 필수입니다. 캐시 키 생성에 사용됩니다. 예: public record OrderCacheKey(Long orderId) implements CacheKey { @Override public String value() { return "order:cache:" + orderId; } }
- **Rationale**: CacheKey 인터페이스를 통해 캐시 키 생성 방식을 표준화합니다. 키 형식의 일관성과 네이밍 충돌 방지를 보장합니다.


### MAJOR 규칙

#### AGG-013: Aggregate Getter 최소화
- **Severity**: MAJOR
- **Category**: STRUCTURE
- **Description**: Aggregate의 Getter는 외부에서 정말 필요한 경우에만 제공합니다. 내부 컬렉션은 불변 래퍼(List.copyOf, Collections.unmodifiableList)로 반환합니다.
- **Rationale**: Tell, Don't Ask 원칙. Getter를 통해 상태를 조회하고 외부에서 판단하는 대신, Aggregate에게 행동을 요청합니다. 내부 상태 노출을 최소화합니다.

#### AGG-020: Command 메서드는 동사로 시작
- **Severity**: MAJOR
- **Category**: NAMING
- **Description**: 상태를 변경하는 Command 메서드는 동사로 시작합니다. 예: cancel(), confirm(), updatePrice(), addItem(). void 반환 또는 this 반환(fluent)이 가능합니다.
- **Rationale**: 메서드 이름만으로 부수효과가 있는 Command인지 명확히 알 수 있습니다. 도메인 전문가도 이해할 수 있는 유비쿼터스 언어를 사용합니다.

#### AGG-021: Query 메서드는 get/is/has/can으로 시작
- **Severity**: MAJOR
- **Category**: NAMING
- **Description**: 상태를 조회하는 Query 메서드는 getXxx(), isXxx(), hasXxx(), canXxx() 형태로 네이밍합니다. 이 메서드들은 상태를 변경하면 안 됩니다 (부수효과 없음).
- **Rationale**: CQS(Command Query Separation) 원칙. Query 메서드는 상태를 변경하지 않으므로 여러 번 호출해도 동일한 결과를 반환합니다.

#### AGG-022: 판단 메서드는 boolean 반환
- **Severity**: MAJOR
- **Category**: BEHAVIOR
- **Description**: is*, can*, has* 형태의 판단 메서드는 boolean을 반환합니다. 복잡한 조건도 Aggregate 내부에서 판단하여 외부에 단순한 boolean으로 제공합니다.
- **Rationale**: 비즈니스 로직 캡슐화. 외부에서 여러 상태를 조합하여 판단하는 대신, Aggregate가 캡슐화된 판단을 제공합니다.

#### AGG-026: equals/hashCode는 ID 기반
- **Severity**: MAJOR
- **Category**: STRUCTURE
- **Description**: Aggregate의 equals()와 hashCode()를 정의할 경우 ID 필드만 사용합니다. 다른 필드를 포함하면 상태 변경 시 해시값이 변경되어 컬렉션 사용에 문제가 발생합니다.
- **Rationale**: Aggregate는 ID로 식별됩니다. 같은 ID를 가진 두 객체는 상태가 달라도 동일한 엔티티입니다. 새 객체(ID가 null)는 equals 비교에 주의가 필요합니다.

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

#### CADP-003: @Component 어노테이션 사용
- **Severity**: MAJOR
- **Category**: ANNOTATION
- **Description**: CommandAdapter는 @Component 어노테이션으로 Bean 등록합니다. @Repository나 @Service가 아닌 @Component를 사용하여 Adapter 역할을 명확히 합니다.
- **Rationale**: Hexagonal Architecture에서 Adapter는 @Component로 표시. @Repository는 Spring Data JPA 전용, @Service는 비즈니스 로직 계층 전용입니다.

#### CADP-004: Command Port 구현
- **Severity**: MAJOR
- **Category**: STRUCTURE
- **Description**: CommandAdapter는 Application Layer에서 정의한 Command Port(예: OrderCommandPort) 인터페이스를 구현합니다. Port 인터페이스와 1:1 매핑됩니다.
- **Rationale**: Hexagonal Architecture의 Ports & Adapters 패턴. Application은 Port 인터페이스에만 의존하고, Adapter가 구체적인 영속화 로직을 구현합니다.

#### CADP-005: Mapper + Repository 의존
- **Severity**: MAJOR
- **Category**: DEPENDENCY
- **Description**: CommandAdapter는 EntityMapper와 JpaRepository를 생성자 주입으로 의존합니다. Mapper로 Domain→Entity 변환 후 Repository로 저장합니다.
- **Rationale**: 변환(Mapper)과 저장(Repository)의 책임 분리. 각 컴포넌트는 단일 책임을 가지며, 테스트와 유지보수가 용이합니다.

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

#### ENT-006: protected/private 생성자 사용
- **Severity**: MAJOR
- **Category**: STRUCTURE
- **Description**: Entity의 생성자는 protected 또는 private으로 선언합니다. JPA 프록시를 위해 기본 생성자(protected)가 필수이며, 외부에서 new 키워드로 직접 생성을 방지합니다.
- **Rationale**: Entity 생성을 of() 팩토리로 강제하여 불변식 보장 및 생성 로직 중앙화. JPA 명세상 기본 생성자 필수 요구사항 충족.

#### ENT-007: SoftDeletableEntity 적용 (논리삭제)
- **Severity**: MAJOR
- **Category**: STRUCTURE
- **Description**: 논리삭제가 필요한 Entity는 SoftDeletableEntity를 상속합니다. deletedAt 필드가 추가되며, @SQLRestriction("deleted_at IS NULL")로 기본 조회에서 삭제된 데이터를 제외합니다.
- **Rationale**: 물리삭제 대신 논리삭제로 데이터 복구 가능성 확보 및 감사 추적 유지. 삭제된 데이터는 조회되지 않지만 DB에 보존됩니다.

#### ENT-008: ID Auto Increment 전략
- **Severity**: MAJOR
- **Category**: STRUCTURE
- **Description**: Entity ID는 @GeneratedValue(strategy = GenerationType.IDENTITY)를 사용합니다. INSERT 시 DB가 ID를 생성하며, persist() 후 생성된 ID를 반환합니다.
- **Rationale**: MySQL Auto Increment와 일치. save() 후 즉시 ID 접근 가능하며, 분산 환경에서는 UUIDv7/Snowflake 등 별도 전략 적용.

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

#### QDR-004: findById 필수 메서드
- **Severity**: MAJOR
- **Category**: STRUCTURE
- **Description**: QueryDslRepository는 Optional<Entity> findById(Long id) 메서드를 필수로 구현해야 합니다. 단건 조회의 기본 메서드입니다.
- **Rationale**: 단건 조회는 가장 기본적인 Query 패턴. selectFrom(entity).where(entity.id.eq(id)).fetchOne()으로 구현합니다.

#### QDR-005: existsById 필수 메서드
- **Severity**: MAJOR
- **Category**: STRUCTURE
- **Description**: QueryDslRepository는 boolean existsById(Long id) 메서드를 필수로 구현해야 합니다. 존재 여부만 확인할 때 전체 Entity를 로드하지 않습니다.
- **Rationale**: exists 쿼리는 COUNT(*)보다 효율적. 데이터 존재 여부만 확인하는 용도로 최적화된 쿼리를 제공합니다.

#### QDR-006: @Repository 클래스 어노테이션
- **Severity**: MAJOR
- **Category**: ANNOTATION
- **Description**: QueryDslRepository는 @Repository 어노테이션을 사용한 구체 클래스로 정의합니다. 인터페이스가 아닌 클래스로 JPAQueryFactory를 직접 사용합니다.
- **Rationale**: Spring Data JPA와 달리 QueryDSL은 구체 클래스에서 JPAQueryFactory를 주입받아 사용. @Repository로 Bean 등록 및 예외 변환을 적용합니다.

#### QDR-007: JPAQueryFactory 생성자 주입
- **Severity**: MAJOR
- **Category**: DEPENDENCY
- **Description**: QueryDslRepository는 생성자를 통해 JPAQueryFactory를 주입받습니다. 필드 주입(@Autowired) 대신 생성자 주입을 사용하여 불변성과 테스트 용이성을 확보합니다.
- **Rationale**: 생성자 주입은 필수 의존성을 명확히 하고, 테스트 시 Mock 객체 주입이 용이합니다.

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

#### AGG-027: 불변 필드는 final 선언
- **Severity**: MINOR
- **Category**: STRUCTURE
- **Description**: 생성 후 변경되지 않는 필드(ID, createdAt 등)는 final로 선언합니다. 단, JPA 매핑을 위해 Persistence Layer Entity에서는 final을 사용하지 않을 수 있습니다.
- **Rationale**: 불변성을 컴파일러 수준에서 보장합니다. final 필드는 생성자에서만 할당 가능하므로 실수로 변경하는 것을 방지합니다.

#### CRI-010: 공통 VO 사용 권장
- **Severity**: MINOR
- **Category**: BEHAVIOR
- **Description**: Criteria는 DateRange, SortDirection, PageRequest, CursorPageRequest, SortKey, QueryContext 등 공통 VO 사용을 권장합니다.
- **Rationale**: 공통 VO를 사용하면 조회 조건의 일관성을 유지하고, 반복적인 코드를 줄일 수 있습니다.

#### ENT-009: @Column nullable, length 명시
- **Severity**: MINOR
- **Category**: ANNOTATION
- **Description**: @Column 어노테이션에 nullable, length 속성을 명시적으로 선언합니다. 특히 String 타입은 @Column(nullable = false, length = 100) 형태로 제약조건을 명확히 합니다.
- **Rationale**: DDL 자동 생성 시 명확한 스키마 정의. 코드 리뷰 시 필드 제약조건을 즉시 파악 가능하며, 문서화 효과가 있습니다.

#### ENT-010: @Table 테이블명 명시
- **Severity**: MINOR
- **Category**: ANNOTATION
- **Description**: @Entity 클래스에 @Table(name = "order") 형태로 테이블명을 명시합니다. 클래스명과 테이블명이 다를 수 있으며, 네이밍 규칙 일관성을 보장합니다.
- **Rationale**: 명시적 테이블명으로 DB 스키마와 Entity 매핑을 명확히 합니다. 복수형/단수형 혼란을 방지합니다.

#### QDR-010: 메서드 네이밍 패턴
- **Severity**: MINOR
- **Category**: NAMING
- **Description**: QueryDslRepository 메서드는 findBy*, existsBy*, search*, count* 패턴을 따릅니다. 예: findById, findByCriteria, existsByEmail, searchWithPaging, countByCriteria.
- **Rationale**: 일관된 네이밍으로 메서드 역할을 즉시 파악 가능. Spring Data JPA의 네이밍 컨벤션과 유사성을 유지합니다.

#### QPRT-003: QueryPort는 Unique 필드 조회 조건부 허용
- **Severity**: MINOR
- **Category**: STRUCTURE
- **Description**: DB에 Unique 제약조건이 있는 필드에 대해 findBy{UniqueField}(): Optional<Domain>, existsBy{UniqueField}(): boolean 메서드를 추가할 수 있습니다. 예: findByEmail(Email), existsBySlug(Slug). 일반 필드 조회는 Criteria를 사용합니다.
- **Rationale**: 성능 최적화. Unique 인덱스를 직접 조회하면 Criteria보다 효율적입니다. 중복 검증 등에 유용합니다.

