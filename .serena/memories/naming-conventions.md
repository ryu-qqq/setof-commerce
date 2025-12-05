# 네이밍 컨벤션

## 클래스 네이밍

### Domain Layer
| 유형 | 패턴 | 예시 |
|------|------|------|
| Aggregate | `{Name}` | `Order`, `Customer`, `Product` |
| VO | `{Name}` | `Email`, `Money`, `OrderStatus` |
| Domain Event | `{Name}Event` | `OrderPlacedEvent` |
| Exception | `{Name}Exception` | `OrderNotFoundException` |
| ErrorCode | `{Bc}ErrorCode` | `OrderErrorCode` |

### Application Layer
| 유형 | 패턴 | 예시 |
|------|------|------|
| Command UseCase | `{Action}{Bc}UseCase` | `CreateOrderUseCase` |
| Query UseCase | `Find{Bc}QueryUseCase` | `FindOrderQueryUseCase` |
| Command Service | `{Bc}CommandService` | `OrderCommandService` |
| Query Service | `{Bc}QueryService` | `OrderQueryService` |
| Manager | `{Bc}TransactionManager` | `OrderTransactionManager` |
| Read Manager | `{Bc}ReadManager` | `OrderReadManager` |
| Facade | `{Bc}CommandFacade` | `OrderCommandFacade` |
| Query Facade | `{Bc}QueryFacade` | `OrderQueryFacade` |
| Command Factory | `{Bc}CommandFactory` | `OrderCommandFactory` |
| Query Factory | `{Bc}QueryFactory` | `OrderQueryFactory` |
| Assembler | `{Bc}Assembler` | `OrderAssembler` |
| Event Listener | `{Bc}EventListener` | `OrderEventListener` |
| Scheduler | `{Bc}Scheduler` | `OrderSyncScheduler` |
| Command DTO | `{Action}{Bc}Command` | `CreateOrderCommand` |
| Query DTO | `{Bc}SearchQuery` | `OrderSearchQuery` |
| Response DTO | `{Bc}Response` | `OrderResponse` |
| Persist Bundle | `{Bc}PersistBundle` | `OrderPersistBundle` |
| Query Bundle | `{Bc}QueryBundle` | `OrderQueryBundle` |
| Port-Out Command | `{Bc}PersistencePort` | `OrderPersistencePort` |
| Port-Out Query | `{Bc}QueryPort` | `OrderQueryPort` |
| Cache Port | `{Bc}CacheQueryPort` | `OrderCacheQueryPort` |
| Lock Port | `DistributedLockPort` | `DistributedLockPort` |

### Persistence Layer
| 유형 | 패턴 | 예시 |
|------|------|------|
| Entity | `{Bc}JpaEntity` | `OrderJpaEntity` |
| JPA Repository | `{Bc}JpaRepository` | `OrderJpaRepository` |
| QueryDSL Repository | `{Bc}QueryDslRepository` | `OrderQueryDslRepository` |
| Command Adapter | `{Bc}PersistenceAdapter` | `OrderPersistenceAdapter` |
| Query Adapter | `{Bc}QueryAdapter` | `OrderQueryAdapter` |
| Entity Mapper | `{Bc}EntityMapper` | `OrderEntityMapper` |

### REST API Layer
| 유형 | 패턴 | 예시 |
|------|------|------|
| Command Controller | `{Bc}CommandController` | `OrderCommandController` |
| Query Controller | `{Bc}QueryController` | `OrderQueryController` |
| Request DTO | `{Action}{Bc}Request` | `CreateOrderRequest` |
| Response DTO | `{Bc}Response` | `OrderResponse` |
| API Mapper | `{Bc}ApiMapper` | `OrderApiMapper` |

## 메서드 네이밍

### CQRS 패턴
```java
// Command (상태 변경)
create*(), update*(), delete*(), place*(), cancel*()

// Query (조회)
find*(), get*(), search*(), exists*()
```

### Static Factory
```java
// 신규 생성
forNew(), create(), of()

// DB 복원
reconstitute(), from()
```

### Manager/Adapter
```java
// 영속화
persist()  // 저장 (create + update)

// 조회
fetch*(), find*()
```

## 패키지 구조
```
com.{company}.{project}/
├── domain/{bc}/
│   ├── aggregate/
│   ├── vo/
│   ├── event/
│   └── exception/
├── application/{bc}/
│   ├── port/in/command/
│   ├── port/in/query/
│   ├── port/out/command/
│   ├── port/out/query/
│   ├── service/command/
│   ├── service/query/
│   ├── manager/
│   ├── facade/
│   ├── factory/
│   ├── assembler/
│   └── dto/
├── adapter/in/rest/{bc}/
│   ├── controller/
│   ├── dto/
│   └── mapper/
└── adapter/out/persistence/{bc}/
    ├── adapter/
    ├── entity/
    ├── repository/
    └── mapper/
```
