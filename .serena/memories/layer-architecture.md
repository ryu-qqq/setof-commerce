# 헥사고날 아키텍처 레이어 구조

## 의존성 방향 (절대 규칙)
```
Domain ← Application ← Adapter ← Bootstrap
(안쪽)                              (바깥쪽)

- 안쪽 레이어는 바깥쪽을 절대 모름
- Port 인터페이스로 의존성 역전 (DIP)
```

## 레이어별 역할

### Domain Layer (순수 비즈니스)
- **기술 독립**: Spring, JPA 어노테이션 금지
- **Aggregate**: 비즈니스 불변식 보호, 트랜잭션 경계
- **VO**: 불변, equals/hashCode 필수, of() 팩토리
- **Exception**: BC별 전용 예외 (DomainException 상속)

### Application Layer (UseCase + Transaction)
- **CQRS 분리**: Command/Query 완전 분리
- **Port-In**: UseCase 인터페이스 (Controller가 호출)
- **Port-Out**: Persistence/External 인터페이스 (Adapter가 구현)
- **Service**: UseCase 구현, @Transactional 경계
- **Manager**: 단일 트랜잭션 처리 (Command: TransactionManager, Query: ReadManager)
- **Facade**: 여러 Manager 조합 (Command: CommandFacade, Query: QueryFacade)
- **Factory**: Domain 객체 생성 (Command: CommandFactory, Query: QueryFactory)
- **Listener**: Domain Event 처리 (@TransactionalEventListener)
- **Scheduler**: 배치 작업 (@Scheduled, 외부 호출 금지)

### Adapter-In Layer (REST API)
- **Thin Controller**: 비즈니스 로직 금지, UseCase 위임만
- **DTO Record**: Request/Response는 Java Record
- **Bean Validation**: @Valid, @NotNull 등 필수

### Adapter-Out Layer (Persistence)
- **Long FK 전략**: JPA 관계 어노테이션 금지
- **CQRS Repository**: JPA(Command) / QueryDSL(Query) 분리
- **DTO Projection**: Query는 Entity 아닌 DTO 반환

## CQRS 흐름

### Command (상태 변경)
```
Controller → CommandUseCase → Service → Manager → Port-Out → Adapter
                                 ↓
                              Domain (비즈니스 로직)
```

### Query (조회)
```
Controller → QueryUseCase → Service → Port-Out → QueryDslRepository
                                         ↓
                                    DTO Projection (Entity X)
```
