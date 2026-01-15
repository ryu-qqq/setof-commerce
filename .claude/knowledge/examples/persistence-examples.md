# PERSISTENCE Layer 예제 코드 (8개)

## 개요

이 문서는 PERSISTENCE Layer의 코딩 규칙에 대한 GOOD/BAD 예제를 제공합니다.

## 예제 목록

| Rule Code | Rule Name | Type |
|-----------|-----------|------|
| EMAP-002 | Pure Java (Lombok/MapStruct 금지) | ✅ GOOD |
| EMAP-003 | 시간 필드 생성 금지 (Instant.now() 금지) | ✅ GOOD |
| EMAP-004 | toEntity(Domain) 메서드 필수 | ✅ GOOD |
| EMAP-005 | toDomain(Entity) 메서드 필수 | ✅ GOOD |
| EMAP-008 | Null 안전 처리 | ✅ GOOD |
| QADP-001 | QueryDslRepository 위임만 | ✅ GOOD |
| QADP-002 | QueryAdapter에서 @Transactional 금지 | ✅ GOOD |
| QADP-008 | QueryAdapter에 비즈니스 로직 금지 | ✅ GOOD |

---

## 상세 예제

### EMAP-002: Pure Java (Lombok/MapStruct 금지)

#### ✅ GOOD Example

```java
@Component
// @Mapper(MapStruct) 사용 금지!
// Lombok @Data, @Builder 등 사용 금지!
public class OrderJpaEntityMapper {

    // Pure Java로 구현
    public OrderJpaEntity toEntity(Order order) {
        if (order.getId() != null && order.getId().value() != null) {
            // 기존 Entity 업데이트
            return OrderJpaEntity.withId(
                order.getId().value(),
                order.getOrderNumber(),
                order.getCustomerId().value(),
                order.getStatus()
            );
        }
        // 신규 Entity 생성
        return OrderJpaEntity.of(
            order.getOrderNumber(),
            order.getCustomerId().value(),
            order.getStatus()
        );
    }

    public Order toDomain(OrderJpaEntity entity) {
        return Order.reconstitute(
            new OrderId(entity.getId()),
            entity.getOrderNumber(),
            new CustomerId(entity.getCustomerId()),
            entity.getStatus(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
}
```

**설명**: @Mapper(MapStruct), @Data, @Builder 등 모든 코드 생성 라이브러리 사용이 금지됩니다. 순수 Java로 변환 로직을 명시적으로 작성합니다. 바이트코드 생성 도구의 예측 불가능한 동작을 방지하고, 디버깅과 유지보수가 용이합니다.

---

### EMAP-003: 시간 필드 생성 금지 (Instant.now() 금지)

#### ✅ GOOD Example

```java
@Component
public class ProductJpaEntityMapper {

    public ProductJpaEntity toEntity(Product product) {
        // Instant.now(), LocalDateTime.now() 호출 금지!
        // 시간 필드는 JPA Auditing이 자동 처리
        // createdAt, updatedAt은 BaseAuditEntity에서 @CreatedDate, @LastModifiedDate로 설정

        if (product.getId() != null && product.getId().value() != null) {
            return ProductJpaEntity.withId(
                product.getId().value(),
                product.getName(),
                product.getPrice().value(),
                product.getCategoryId().value()
                // createdAt, updatedAt 파라미터 없음!
            );
        }
        return ProductJpaEntity.of(
            product.getName(),
            product.getPrice().value(),
            product.getCategoryId().value()
            // Instant.now() 호출 안함!
        );
    }

    public Product toDomain(ProductJpaEntity entity) {
        return Product.reconstitute(
            new ProductId(entity.getId()),
            entity.getName(),
            new Money(entity.getPrice()),
            new CategoryId(entity.getCategoryId()),
            entity.getCreatedAt(),  // Entity에서 가져온 시간
            entity.getUpdatedAt()   // Entity에서 가져온 시간
        );
    }
}
```

**설명**: Mapper에서 Instant.now(), LocalDateTime.now(), System.currentTimeMillis() 등 시간 필드를 직접 생성하지 않습니다. 시간은 Domain에서 주입받거나 JPA Auditing(@CreatedDate, @LastModifiedDate)으로 처리합니다. 테스트 가능성과 결정론적 동작을 보장합니다.

---

### EMAP-004: toEntity(Domain) 메서드 필수

#### ✅ GOOD Example

```java
@Component
public class CustomerJpaEntityMapper {

    // toEntity(Domain) 메서드 필수
    public CustomerJpaEntity toEntity(Customer customer) {
        if (customer.getId() != null && customer.getId().value() != null) {
            // 기존 Entity 복원 (ID 포함)
            return CustomerJpaEntity.withId(
                customer.getId().value(),
                customer.getName(),
                customer.getEmail().value(),
                customer.getGrade()
            );
        }
        // 신규 Entity 생성 (ID 없음)
        return CustomerJpaEntity.of(
            customer.getName(),
            customer.getEmail().value(),
            customer.getGrade()
        );
    }

    public Customer toDomain(CustomerJpaEntity entity) {
        return Customer.reconstitute(
            new CustomerId(entity.getId()),
            entity.getName(),
            new Email(entity.getEmail()),
            entity.getGrade(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
}
```

**설명**: EntityMapper는 toEntity(Domain) 메서드를 필수로 구현합니다. Domain → Entity 변환 로직을 캡슐화합니다. Entity.of()를 호출하여 Entity를 생성하고, 기존 데이터 업데이트 시 withId()를 사용합니다.

---

### EMAP-005: toDomain(Entity) 메서드 필수

#### ✅ GOOD Example

```java
@Component
public class OrderJpaEntityMapper {

    public OrderJpaEntity toEntity(Order order) {
        if (order.getId() != null && order.getId().value() != null) {
            return OrderJpaEntity.withId(
                order.getId().value(),
                order.getOrderNumber(),
                order.getCustomerId().value(),
                order.getStatus()
            );
        }
        return OrderJpaEntity.of(
            order.getOrderNumber(),
            order.getCustomerId().value(),
            order.getStatus()
        );
    }

    // toDomain(Entity) 메서드 필수
    public Order toDomain(OrderJpaEntity entity) {
        // Domain.reconstitute()를 호출하여 Domain 복원
        // new Order() 직접 생성 금지!
        // Domain.forNew() 금지! (신규 생성 전용)
        return Order.reconstitute(
            new OrderId(entity.getId()),
            entity.getOrderNumber(),
            new CustomerId(entity.getCustomerId()),
            entity.getStatus(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
}
```

**설명**: EntityMapper는 toDomain(Entity) 메서드를 필수로 구현합니다. Entity → Domain 변환 로직을 캡슐화합니다. Domain.reconstitute()를 호출하여 Domain을 복원하며, new Domain()이나 forNew()는 신규 생성 전용이므로 사용하지 않습니다.

---

### EMAP-008: Null 안전 처리

#### ✅ GOOD Example

```java
@Component
public class ProductJpaEntityMapper {

    // null 입력에 대한 안전한 처리
    public ProductJpaEntity toEntity(Product product) {
        if (product == null) {
            return null;  // null 입력 시 null 반환
        }
        if (product.getId() != null && product.getId().value() != null) {
            return ProductJpaEntity.withId(
                product.getId().value(),
                product.getName(),
                product.getPrice().value(),
                product.getCategoryId() != null ? product.getCategoryId().value() : null
            );
        }
        return ProductJpaEntity.of(
            product.getName(),
            product.getPrice().value(),
            product.getCategoryId() != null ? product.getCategoryId().value() : null
        );
    }

    public Product toDomain(ProductJpaEntity entity) {
        if (entity == null) {
            return null;  // null 입력 시 null 반환
        }
        return Product.reconstitute(
            new ProductId(entity.getId()),
            entity.getName(),
            new Money(entity.getPrice()),
            entity.getCategoryId() != null ? new CategoryId(entity.getCategoryId()) : null,
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    // Optional 처리 예시
    public Optional<Product> toDomainOptional(Optional<ProductJpaEntity> entityOptional) {
        return entityOptional.map(this::toDomain);
    }
}
```

**설명**: Mapper 메서드에서 null 입력에 대한 안전한 처리를 구현합니다. null 입력 시 null 반환 또는 명시적 예외 발생. Optional 래핑된 입력도 처리합니다. NPE를 방지하고 Repository에서 Optional.empty()가 반환될 수 있음을 고려합니다.

---

### QADP-001: QueryDslRepository 위임만

#### ✅ GOOD Example

```java
@Component
public class ProductQueryAdapter implements ProductQueryPort {

    private final ProductQueryDslRepository repository;
    private final ProductJpaEntityMapper mapper;

    public ProductQueryAdapter(ProductQueryDslRepository repository,
            ProductJpaEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    // Repository 위임 + Mapper 변환만 수행
    @Override
    public Optional<Product> findById(ProductId id) {
        return repository.findById(id.value())
            .map(mapper::toDomain);
    }

    @Override
    public List<Product> findByCriteria(ProductCriteria criteria) {
        return repository.findByCriteria(criteria).stream()
            .map(mapper::toDomain)
            .toList();
    }

    // JPAQueryFactory 직접 사용 금지!
    // private final JPAQueryFactory queryFactory;  // 금지!
    // queryFactory.selectFrom(product)...          // 금지!
    // → 쿼리 로직은 QueryDslRepository에서 처리
}
```

**설명**: QueryAdapter는 QueryDslRepository로 조회를 위임하고 Mapper로 변환하는 역할만 수행합니다. JPAQueryFactory를 직접 사용하거나 쿼리 로직을 포함하지 않습니다. Repository는 쿼리 실행, Mapper는 변환, Adapter는 조합만 담당합니다.

---

### QADP-002: QueryAdapter에서 @Transactional 금지

#### ✅ GOOD Example

```java
@Component
// @Transactional 어노테이션 없음!
// @Transactional(readOnly = true) 도 금지 - Application Layer에서 설정
public class OrderQueryAdapter implements OrderQueryPort {

    private final OrderQueryDslRepository repository;
    private final OrderJpaEntityMapper mapper;

    public OrderQueryAdapter(OrderQueryDslRepository repository,
            OrderJpaEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    // @Transactional 없음
    @Override
    public Optional<Order> findById(OrderId id) {
        return repository.findById(id.value())
            .map(mapper::toDomain);
    }

    @Override
    public boolean existsById(OrderId id) {
        return repository.existsById(id.value());
    }
}

// 읽기 전용 트랜잭션은 Application Layer에서 설정
@Service
public class GetOrderService implements GetOrderUseCase {

    private final OrderQueryPort orderQueryPort;

    public GetOrderService(OrderQueryPort orderQueryPort) {
        this.orderQueryPort = orderQueryPort;
    }

    @Override
    @Transactional(readOnly = true)  // 읽기 전용 트랜잭션은 여기서 설정
    public OrderResponse execute(GetOrderQuery query) {
        Order order = orderQueryPort.findById(new OrderId(query.orderId()))
            .orElseThrow(() -> new OrderNotFoundException(query.orderId()));
        return OrderAssembler.toResponse(order);
    }
}
```

**설명**: @Transactional 어노테이션을 QueryAdapter 클래스나 메서드에 사용하지 않습니다. 읽기 전용 트랜잭션(@Transactional(readOnly = true))도 Application Layer에서 관리합니다.

---

### QADP-008: QueryAdapter에 비즈니스 로직 금지

#### ✅ GOOD Example

```java
@Component
public class CustomerQueryAdapter implements CustomerQueryPort {

    private final CustomerQueryDslRepository repository;
    private final CustomerJpaEntityMapper mapper;

    public CustomerQueryAdapter(CustomerQueryDslRepository repository,
            CustomerJpaEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Customer> findById(CustomerId id) {
        // 비즈니스 로직 없음!
        // if (customer.isVip()) { ... }        // 금지!
        // customer.calculateDiscount();        // 금지!
        // filterInactiveCustomers(customers);  // 금지!

        return repository.findById(id.value())
            .map(mapper::toDomain);
    }

    @Override
    public List<Customer> findByCriteria(CustomerCriteria criteria) {
        // 조건 필터링은 Repository에서 처리
        // 비즈니스 필터링은 Application Layer에서 처리
        return repository.findByCriteria(criteria).stream()
            .map(mapper::toDomain)
            .toList();
    }
}
```

**설명**: QueryAdapter에 if/switch 조건 분기, 필터링 로직, 정렬 로직 등 비즈니스 로직을 포함하지 않습니다. 단순히 Repository 호출 후 Mapper 변환만 수행합니다. 조회 조건과 정렬은 Repository에서, 비즈니스 필터링은 Application Layer에서 처리합니다.

---

