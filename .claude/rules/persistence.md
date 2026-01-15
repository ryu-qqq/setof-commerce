---
paths:
  - "adapter-out/persistence-*/**"
  - "**/persistence/**"
  - "**/*Repository*"
  - "**/*JpaEntity*"
  - "**/*Adapter*"
---

# Persistence Layer 규칙 (자동 주입)

이 규칙은 Persistence 관련 파일을 작업할 때 **자동으로 적용**됩니다.

## Zero-Tolerance 규칙 (절대 위반 금지)

### 구조 규칙
- **ENT-001**: Entity는 `BaseAuditEntity` 상속 필수
- **ENT-002**: Long FK 전략 - JPA 관계 어노테이션 (`@ManyToOne`, `@OneToMany`, `@OneToOne`) 절대 금지
- **CPRT-001**: CommandPort는 반드시 interface로 정의
- **CPRT-002**: CommandPort는 `persist(Domain)` 메서드만 제공
- **QPRT-001**: QueryPort는 반드시 interface로 정의

### 어노테이션 규칙
- **ENT-003**: Entity에서 Lombok 금지
- **CADP-002**: CommandAdapter에서 `@Transactional` 금지
- **QDR-008**: QueryDslRepository에서 `@Transactional` 금지

### 행위 규칙
- **ENT-005**: Entity Setter 메서드 금지
- **CADP-001**: CommandAdapter는 `persist()` 메서드만 제공
- **CADP-007**: CommandAdapter에 비즈니스 로직 금지
- **CPRT-003**: CommandPort는 Domain 객체를 파라미터로 받음
- **QPRT-004**: QueryPort는 Domain 객체 반환 (Entity/DTO 직접 반환 금지)
- **QDR-001**: QueryDslRepository는 Query 전용 (조회만)
- **QDR-002**: QueryDslRepository Join 절대 금지
- **QDR-003**: `findAll()` 금지 (OOM 방지)

## JPA Entity 패턴

```java
@Entity
@Table(name = "orders")
public class OrderJpaEntity extends BaseAuditEntity {
    @Id
    private Long id;

    // Long FK 전략 - @ManyToOne 금지!
    @Column(nullable = false)
    private Long customerId;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    // JPA용 protected 기본 생성자
    protected OrderJpaEntity() {}

    // Domain → Entity 변환
    public static OrderJpaEntity from(Order domain) {
        OrderJpaEntity entity = new OrderJpaEntity();
        entity.id = domain.getId().value();
        entity.customerId = domain.getCustomerId().value();
        entity.status = domain.getStatus();
        return entity;
    }

    // Entity → Domain 변환
    public Order toDomain() {
        return Order.reconstitute(
            OrderId.of(this.id),
            CustomerId.of(this.customerId),
            this.status
        );
    }
}
```

## Port & Adapter 패턴

```java
// Port-Out (Interface) - Application Layer
public interface OrderCommandPort {
    void persist(Order order);
}

public interface OrderQueryPort {
    Order getById(Long id);
    Optional<Order> findById(Long id);
}

// Adapter (Implementation) - Persistence Layer
@Component
public class OrderCommandAdapter implements OrderCommandPort {
    private final OrderJpaRepository jpaRepository;

    // @Transactional 금지! Manager에서 처리
    @Override
    public void persist(Order order) {
        OrderJpaEntity entity = OrderJpaEntity.from(order);
        jpaRepository.save(entity);
    }
}

@Component
public class OrderQueryAdapter implements OrderQueryPort {
    private final OrderQueryDslRepository queryDslRepository;

    @Override
    public Order getById(Long id) {
        return queryDslRepository.findById(id)
            .map(OrderJpaEntity::toDomain)
            .orElseThrow(() -> new DomainException(OrderErrorCode.NOT_FOUND, id));
    }
}
```

## QueryDSL Repository 패턴

```java
@Repository
// @Transactional 금지!
public class OrderQueryDslRepository {
    private final JPAQueryFactory queryFactory;

    // findAll 금지! Criteria 사용
    public List<OrderJpaEntity> search(OrderCriteria criteria) {
        return queryFactory
            .selectFrom(order)
            .where(buildConditions(criteria))
            .fetch();
    }
}
```

## 상세 규칙 참조

더 자세한 규칙은 다음 파일을 참조하세요:
- `.claude/knowledge/rules/persistence-rules.md` (18개 규칙)
- `.claude/knowledge/templates/persistence-templates.md`
- `.claude/knowledge/examples/persistence-examples.md`
