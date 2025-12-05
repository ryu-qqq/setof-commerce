# Zero-Tolerance 규칙 (절대 위반 금지)

## 1. Lombok 전면 금지
```java
// ❌ 금지: @Data, @Builder, @Getter, @Setter, @AllArgsConstructor 등 모든 Lombok
@Data
public class Order { }

// ✅ 권장: 명시적 구현
public class Order {
    private final Long id;
    
    public Order(Long id) { this.id = id; }
    public Long id() { return id; }  // Record 스타일 또는 getId()
}
```

## 2. Law of Demeter (Getter 체이닝 금지)
```java
// ❌ 금지: 2개 이상 점(.) 체이닝
String zip = order.getCustomer().getAddress().getZipCode();

// ✅ 권장: Tell, Don't Ask - 하나의 점만
String zip = order.customerZipCode();
```

## 3. Long FK 전략 (JPA 관계 어노테이션 금지)
```java
// ❌ 금지: @OneToMany, @ManyToOne, @OneToOne, @ManyToMany
@ManyToOne
private CustomerJpaEntity customer;

// ✅ 권장: Long FK
@Column(name = "customer_id", nullable = false)
private Long customerId;
```

## 4. Transaction 경계 (외부 API 호출 금지)
```java
// ❌ 금지: @Transactional 내 외부 호출
@Transactional
public void process(Order order) {
    repository.save(order);
    paymentClient.call(order);  // 외부 API ❌
    messageQueue.send(order);   // 메시지 큐 ❌
}

// ✅ 권장: Transaction 밖에서 외부 호출
@Transactional
public Order save(Order order) {
    return repository.save(order);
}

public void processExternal(Order order) {  // Non-transactional
    paymentClient.call(order);
}
```

## 5. Spring Proxy 제약
```java
// ❌ 금지: @Transactional 무효화되는 케이스
@Transactional
private void save() { }           // private 메서드

public final class Service { }     // final 클래스

public void outer() {
    this.transactionalMethod();    // 내부 호출 (self-invocation)
}

// ✅ 권장: public + non-final + 외부 호출
@Transactional
public void save() { }

// 내부 호출 필요시 → 별도 Bean 분리
```

## 6. Javadoc 필수
- public 클래스: 클래스 설명
- public 메서드: @param, @return, @throws

## 7. Scope 준수
- 요청된 기능만 구현
- 추가 기능, 리팩토링 임의 수행 금지
