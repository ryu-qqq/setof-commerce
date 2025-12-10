# Component Dependency Graph (전체 레이어)

> 기능 구현 시 필요한 컴포넌트와 의존성을 정의합니다.
> /design 커맨드에서 자동으로 참조됩니다.

---

## 레이어 개요

```
┌─────────────────────────────────────────────────────────────┐
│  REST API Layer (adapter-in)                                │
│    Controller → Request/Response DTO → Mapper               │
└─────────────────────────────────────────────────────────────┘
                            ↓ UseCase 호출
┌─────────────────────────────────────────────────────────────┐
│  Application Layer                                          │
│    Service → Factory → Manager/Facade → Port-Out            │
└─────────────────────────────────────────────────────────────┘
                            ↓ Port 구현
┌─────────────────────────────────────────────────────────────┐
│  Persistence Layer (adapter-out)                            │
│    Adapter → Repository → Mapper → Entity                   │
└─────────────────────────────────────────────────────────────┘
                            ↓ 매핑
┌─────────────────────────────────────────────────────────────┐
│  Domain Layer                                               │
│    Aggregate → VO → Exception → Event                       │
└─────────────────────────────────────────────────────────────┘
```

---

## 1. Domain Layer 컴포넌트

### 1.1 Aggregate Root (필수)

```yaml
component: Aggregate
location: "domain/{bc}/aggregate"
naming: "{Bc}.java"
requires: []
generates:
  - type: "VO (ID)"
    naming: "{Bc}Id"
    location: "domain/{bc}/vo"
  - type: "VO (상태)"
    naming: "{Bc}Status"
    location: "domain/{bc}/vo"
    condition: "상태 머신이 있는 경우"
rules_memory: "domain-rules-01-aggregate"
checklist:
  - "[ ] Lombok 금지"
  - "[ ] private 생성자 + static factory (forNew, reconstitute)"
  - "[ ] Long FK 전략 (관계 어노테이션 X)"
  - "[ ] Instant 사용 (LocalDateTime X)"
  - "[ ] 비즈니스 메서드로 상태 변경 (Tell, Don't Ask)"
  - "[ ] Domain Event 발행 (pullDomainEvents)"
```

### 1.2 Value Object

```yaml
component: ValueObject
location: "domain/{bc}/vo"
naming: "{Name}.java"
types:
  - id: "{Bc}Id"
  - status: "{Bc}Status"
  - money: "Money, Price, Amount"
  - contact: "Email, Phone, Address"
  - period: "DateRange, Period"
rules_memory: "domain-rules-02-value-object"
checklist:
  - "[ ] final class"
  - "[ ] private final 필드"
  - "[ ] private 생성자 + of/from factory"
  - "[ ] 생성 시 validation"
  - "[ ] equals/hashCode 구현"
  - "[ ] Lombok 금지"
```

### 1.3 Domain Exception

```yaml
component: DomainException
location: "domain/{bc}/exception"
naming: "{Bc}{Reason}Exception.java"
requires:
  - type: "ErrorCode"
    naming: "{Bc}ErrorCode"
    location: "domain/{bc}/exception"
rules_memory: "domain-rules-03-exception"
checklist:
  - "[ ] DomainException 상속"
  - "[ ] BC별 ErrorCode enum 사용"
  - "[ ] 메시지에 컨텍스트 정보 포함"
```

### 1.4 Domain Event

```yaml
component: DomainEvent
location: "domain/{bc}/event"
naming: "{Bc}{Action}Event.java"
rules_memory: "domain-rules-04-event"
checklist:
  - "[ ] DomainEvent 상속"
  - "[ ] 불변 (final 필드)"
  - "[ ] occurredAt 포함"
  - "[ ] Aggregate ID 포함"
```

### 1.5 Criteria (Query 조건)

```yaml
component: Criteria
location: "domain/{bc}/criteria"
naming: "{Bc}SearchCriteria.java, {Bc}DetailCriteria.java"
rules_memory: "domain-rules-05-criteria"
checklist:
  - "[ ] 불변 객체"
  - "[ ] 페이징 정보 포함 (page, size)"
  - "[ ] 정렬 정보 포함 (sortBy, direction)"
```

---

## 2. Application Layer 컴포넌트

### 2.1 Command Flow (상태 변경)

#### 2.1.1 Simple Command (Manager 1개)

```yaml
flow: simple_command
entry: "{Action}{Bc}Service"
components:
  - component: "CommandUseCase"
    location: "application/{bc}/port/in/command"
    naming: "{Action}{Bc}UseCase"
    type: "interface"
    rules_memory: "app-rules-03-port"
    
  - component: "CommandService"
    location: "application/{bc}/service/command"
    naming: "{Action}{Bc}Service"
    implements: "{Action}{Bc}UseCase"
    rules_memory: "app-rules-01-service"
    
  - component: "Command DTO"
    location: "application/{bc}/dto/command"
    naming: "{Action}{Bc}Command"
    type: "record"
    rules_memory: "app-rules-02-dto"
    
  - component: "Response DTO"
    location: "application/{bc}/dto/response"
    naming: "{Bc}Response"
    type: "record"
    rules_memory: "app-rules-02-dto"
    
  - component: "CommandFactory"
    location: "application/{bc}/factory/command"
    naming: "{Bc}CommandFactory"
    rules_memory: "app-rules-05-factory-assembler"
    
  - component: "TransactionManager"
    location: "application/{bc}/manager/command"
    naming: "{Bc}TransactionManager"
    rules_memory: "app-rules-04-manager-facade"
    
  - component: "Assembler"
    location: "application/{bc}/assembler"
    naming: "{Bc}Assembler"
    rules_memory: "app-rules-05-factory-assembler"

downstream:
  - component: "PersistencePort"
    location: "application/{bc}/port/out/command"
    naming: "{Bc}PersistencePort"
```

#### 2.1.2 Complex Command (Manager 2개+, Facade 필요)

```yaml
flow: complex_command
entry: "{Action}{Bc}Service"
components:
  # ... simple_command 모든 컴포넌트 포함 +
  
  - component: "CommandFacade"
    location: "application/{bc}/facade/command"
    naming: "{Action}{Bc}Facade"
    rules_memory: "app-rules-04-manager-facade"
    requires:
      - "TransactionManager x 2+"
      
  - component: "PersistBundle"
    location: "application/{bc}/dto/bundle"
    naming: "{Bc}PersistBundle"
    type: "record"
    condition: "여러 Aggregate 동시 저장 시"
```

### 2.2 Query Flow (조회)

#### 2.2.1 Simple Query (ReadManager 1개)

```yaml
flow: simple_query
entry: "Get{Bc}Service / Search{Bc}Service"
components:
  - component: "QueryUseCase"
    location: "application/{bc}/port/in/query"
    naming: "Get{Bc}UseCase / Search{Bc}UseCase"
    type: "interface"
    
  - component: "QueryService"
    location: "application/{bc}/service/query"
    naming: "Get{Bc}Service / Search{Bc}Service"
    
  - component: "Query DTO"
    location: "application/{bc}/dto/query"
    naming: "Get{Bc}Query / Search{Bc}Query"
    type: "record"
    
  - component: "Response DTO"
    location: "application/{bc}/dto/response"
    naming: "{Bc}Response / {Bc}DetailResponse / {Bc}SummaryResponse"
    type: "record"
    
  - component: "QueryFactory"
    location: "application/{bc}/factory/query"
    naming: "{Bc}QueryFactory"
    condition: "Query → Criteria 변환 필요 시"
    
  - component: "ReadManager"
    location: "application/{bc}/manager/query"
    naming: "{Bc}ReadManager"
    
  - component: "Assembler"
    location: "application/{bc}/assembler"
    naming: "{Bc}Assembler"

downstream:
  - component: "QueryPort"
    location: "application/{bc}/port/out/query"
    naming: "{Bc}QueryPort"
```

#### 2.2.2 Complex Query (ReadManager 2개+)

```yaml
flow: complex_query
entry: "Get{Bc}DetailService"
components:
  # ... simple_query 모든 컴포넌트 포함 +
  
  - component: "QueryFacade"
    location: "application/{bc}/facade/query"
    naming: "{Bc}QueryFacade"
    requires:
      - "ReadManager x 2+"
      
  - component: "QueryBundle"
    location: "application/{bc}/dto/bundle"
    naming: "{Bc}QueryBundle / {Bc}DetailQueryBundle"
    type: "record"
```

---

## 3. Persistence Layer 컴포넌트

### 3.1 Command Side (JPA)

```yaml
flow: persistence_command
components:
  - component: "JpaEntity"
    location: "adapter-out/persistence/{bc}/entity"
    naming: "{Bc}JpaEntity"
    rules_memory: "persistence-rules-01-entity"
    checklist:
      - "[ ] Long FK 전략 (관계 어노테이션 X)"
      - "[ ] protected 기본 생성자"
      - "[ ] Setter 금지"
      - "[ ] Lombok 금지"
    
  - component: "JpaRepository"
    location: "adapter-out/persistence/{bc}/repository"
    naming: "{Bc}JpaRepository"
    extends: "JpaRepository<{Bc}JpaEntity, Long>"
    rules_memory: "persistence-rules-02-jpa-repository"
    
  - component: "EntityMapper"
    location: "adapter-out/persistence/{bc}/mapper"
    naming: "{Bc}EntityMapper"
    rules_memory: "persistence-rules-06-mapper"
    checklist:
      - "[ ] toEntity(Domain) 메서드"
      - "[ ] toDomain(Entity) 메서드"
      - "[ ] @Component 어노테이션"
    
  - component: "PersistenceAdapter"
    location: "adapter-out/persistence/{bc}/adapter/command"
    naming: "{Bc}PersistenceAdapter"
    implements: "{Bc}PersistencePort"
    rules_memory: "persistence-rules-07-command-adapter"
```

### 3.2 Query Side (QueryDSL)

```yaml
flow: persistence_query
components:
  - component: "QueryDslRepository"
    location: "adapter-out/persistence/{bc}/repository"
    naming: "{Bc}QueryDslRepository"
    rules_memory: "persistence-rules-03-querydsl-repository"
    checklist:
      - "[ ] DTO Projection (Entity 반환 X)"
      - "[ ] Q-Class 사용"
      - "[ ] @Repository 어노테이션"
    
  - component: "QueryAdapter"
    location: "adapter-out/persistence/{bc}/adapter/query"
    naming: "{Bc}QueryAdapter"
    implements: "{Bc}QueryPort"
    rules_memory: "persistence-rules-08-query-adapter"
```

---

## 4. REST API Layer 컴포넌트

### 4.1 Command Controller

```yaml
flow: rest_command
components:
  - component: "CommandController"
    location: "adapter-in/rest-api/{bc}/controller"
    naming: "{Bc}CommandController"
    rules_memory: "rest-api-rules-01-controller"
    checklist:
      - "[ ] @RestController"
      - "[ ] @RequestMapping(\"/api/v1/{bcs}\")"
      - "[ ] UseCase 위임만 (비즈니스 로직 X)"
      - "[ ] @Valid 필수"
    
  - component: "Request DTO"
    location: "adapter-in/rest-api/{bc}/dto/request"
    naming: "{Action}{Bc}Request"
    type: "record"
    rules_memory: "rest-api-rules-02-command-dto"
    checklist:
      - "[ ] Bean Validation 어노테이션"
      - "[ ] @NotNull, @NotEmpty 등"
    
  - component: "Response DTO"
    location: "adapter-in/rest-api/{bc}/dto/response"
    naming: "{Bc}ApiResponse"
    type: "record"
    rules_memory: "rest-api-rules-04-response-dto"
    
  - component: "RequestMapper"
    location: "adapter-in/rest-api/{bc}/mapper"
    naming: "{Bc}RequestMapper"
    rules_memory: "rest-api-rules-05-mapper"
    checklist:
      - "[ ] toCommand(Request) 메서드"
      - "[ ] @Component 어노테이션"
```

### 4.2 Query Controller

```yaml
flow: rest_query
components:
  - component: "QueryController"
    location: "adapter-in/rest-api/{bc}/controller"
    naming: "{Bc}QueryController"
    
  - component: "Query Request DTO"
    location: "adapter-in/rest-api/{bc}/dto/request"
    naming: "Search{Bc}Request / Get{Bc}Request"
    
  - component: "ResponseMapper"
    location: "adapter-in/rest-api/{bc}/mapper"
    naming: "{Bc}ResponseMapper"
    checklist:
      - "[ ] toApiResponse(Response) 메서드"
```

---

## 5. 전체 Flow 예시

### 5.1 Simple Command 전체 경로

```
POST /api/v1/orders
        ↓
┌─ REST API Layer ─────────────────────────────────────────────┐
│  OrderCommandController                                       │
│    → CreateOrderRequest (record + @Valid)                    │
│    → OrderRequestMapper.toCommand()                          │
└──────────────────────────────────────────────────────────────┘
        ↓ CreateOrderUseCase.execute()
┌─ Application Layer ──────────────────────────────────────────┐
│  CreateOrderService                                          │
│    → OrderCommandFactory.create(command)                     │
│    → OrderTransactionManager.persist(order)                  │
│    → OrderAssembler.toResponse(order)                        │
└──────────────────────────────────────────────────────────────┘
        ↓ OrderPersistencePort.persist()
┌─ Persistence Layer ──────────────────────────────────────────┐
│  OrderPersistenceAdapter                                     │
│    → OrderEntityMapper.toEntity(order)                       │
│    → OrderJpaRepository.save(entity)                         │
│    → OrderEntityMapper.toDomain(saved)                       │
└──────────────────────────────────────────────────────────────┘
        ↓ 매핑
┌─ Domain Layer ───────────────────────────────────────────────┐
│  Order (Aggregate)                                           │
│    → OrderId (VO)                                            │
│    → OrderStatus (VO)                                        │
│    → OrderLine (내부 Entity)                                 │
│    → OrderPlacedEvent (Domain Event)                         │
└──────────────────────────────────────────────────────────────┘
```

---

## 6. 네이밍 규칙 요약

| Layer | Component | Pattern | Example |
|-------|-----------|---------|---------|
| Domain | Aggregate | `{Bc}` | `Order` |
| Domain | VO | `{Bc}Id`, `{Name}` | `OrderId`, `Money` |
| Domain | Exception | `{Bc}{Reason}Exception` | `OrderNotFoundException` |
| Domain | Event | `{Bc}{Action}Event` | `OrderPlacedEvent` |
| Application | UseCase | `{Action}{Bc}UseCase` | `PlaceOrderUseCase` |
| Application | Service | `{Action}{Bc}Service` | `PlaceOrderService` |
| Application | Manager | `{Bc}TransactionManager` | `OrderTransactionManager` |
| Application | ReadManager | `{Bc}ReadManager` | `OrderReadManager` |
| Application | Facade | `{Action}{Bc}Facade` | `PlaceOrderFacade` |
| Application | Factory | `{Bc}CommandFactory` | `OrderCommandFactory` |
| Application | Assembler | `{Bc}Assembler` | `OrderAssembler` |
| Persistence | Entity | `{Bc}JpaEntity` | `OrderJpaEntity` |
| Persistence | Repository | `{Bc}JpaRepository` | `OrderJpaRepository` |
| Persistence | Adapter | `{Bc}PersistenceAdapter` | `OrderPersistenceAdapter` |
| REST API | Controller | `{Bc}CommandController` | `OrderCommandController` |
| REST API | Request | `{Action}{Bc}Request` | `PlaceOrderRequest` |
