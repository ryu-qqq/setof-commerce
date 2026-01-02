# Cart 모듈 분석

> 작성일: 2025-12-29
> 상태: 🟢 분석완료

---

## 1. 모듈 개요

| 항목 | 내용 |
|------|------|
| 도메인 | 장바구니 관리 |
| 주요 기능 | 상품 추가, 수량 변경, 선택 변경, 삭제, 비우기 |
| 복잡도 | 중간 |
| 어드민 조회 | 불필요 (회원별 1개 장바구니) |

---

## 2. 컴포넌트 구조

### 2.1 파일 목록

```
cart/
├── assembler/
│   └── CartAssembler.java
├── dto/
│   ├── command/
│   │   ├── AddCartItemCommand.java
│   │   ├── UpdateCartItemQuantityCommand.java
│   │   ├── UpdateCartItemSelectedCommand.java
│   │   ├── RemoveCartItemCommand.java
│   │   ├── ClearCartCommand.java
│   │   ├── SoftDeleteCartItemsCommand.java
│   │   └── RestoreCartItemsCommand.java
│   └── response/
│       ├── CartResponse.java
│       ├── CartItemResponse.java
│       ├── EnrichedCartResponse.java
│       └── EnrichedCartItemResponse.java
├── factory/
│   └── command/
│       └── CartCommandFactory.java
├── manager/
│   ├── command/
│   │   └── CartPersistenceManager.java
│   └── query/
│       └── CartReadManager.java
├── port/
│   ├── in/
│   │   ├── command/
│   │   │   ├── AddCartItemUseCase.java
│   │   │   ├── UpdateCartItemQuantityUseCase.java
│   │   │   ├── UpdateCartItemSelectedUseCase.java
│   │   │   ├── RemoveCartItemUseCase.java
│   │   │   ├── ClearCartUseCase.java
│   │   │   ├── SoftDeleteCartItemsUseCase.java
│   │   │   └── RestoreCartItemsUseCase.java
│   │   └── query/
│   │       ├── GetCartUseCase.java
│   │       └── GetEnrichedCartUseCase.java
│   └── out/
│       ├── command/
│       │   └── CartPersistencePort.java
│       └── query/
│           └── CartQueryPort.java
└── service/
    ├── command/
    │   ├── AddCartItemService.java
    │   ├── UpdateCartItemQuantityService.java
    │   ├── UpdateCartItemSelectedService.java
    │   ├── RemoveCartItemService.java
    │   ├── ClearCartService.java
    │   ├── SoftDeleteCartItemsService.java
    │   └── RestoreCartItemsService.java
    └── query/
        ├── GetCartService.java
        └── GetEnrichedCartService.java
```

### 2.2 컴포넌트 분석

#### Port-Out (QueryPort)
```java
public interface CartQueryPort {
    Optional<Cart> findById(CartId cartId);
    Cart getById(CartId cartId);
    Optional<Cart> findByMemberId(UUID memberId);
    Cart getOrCreateByMemberId(UUID memberId);
}
```
✅ **컨벤션 준수**: 회원별 장바구니 조회 (1:1 관계)

#### UseCase 분리
- Command: 7개 (Add, Update 2개, Remove, Clear, SoftDelete, Restore)
- Query: 2개 (GetCart, GetEnrichedCart)

✅ **CQRS 분리 완료**

---

## 3. 컨벤션 준수 현황

### 3.1 체크리스트

| 항목 | 상태 | 비고 |
|------|------|------|
| **Port-In** | ✅ | UseCase 인터페이스 분리 (9개) |
| **Port-Out** | ✅ | QueryPort/PersistencePort 분리 |
| **Service @Transactional 금지** | ✅ | |
| **Manager @Transactional** | ✅ | |
| **Port 직접 호출 금지** | ✅ | Service → Manager |
| **Lombok 금지** | ✅ | |
| **Assembler toDomain 금지** | ✅ | |

### 3.2 어드민 조회 조건

| 조건 | 필요 여부 | 현재 상태 |
|------|----------|----------|
| 복합 필터 | ❌ 불필요 | - |
| 정렬 | ❌ 불필요 | - |
| 기간 조회 | ❌ 불필요 | - |
| 키워드 검색 | ❌ 불필요 | - |
| 페이지네이션 | ❌ 불필요 | - |

> **분석**: 장바구니는 회원당 1개만 존재하므로 어드민 검색이 불필요. 회원 조회 시 장바구니 정보가 함께 표시될 수 있음.

---

## 4. 리팩토링 필요 사항

### 4.1 필수 변경
없음 - 현재 컨벤션 준수 상태

### 4.2 권장 변경
없음

---

## 5. 예상 작업량

| 항목 | 예상 |
|------|------|
| 변경 파일 수 | 0 |
| 리팩토링 난이도 | 🟢 없음 |
| 테스트 영향 | 없음 |

---

## 6. 결론

**cart 모듈은 현재 컨벤션을 잘 준수하고 있어 리팩토링이 필요하지 않습니다.**

장바구니는 회원별 1:1 구조로 복잡한 검색 조건이 필요 없으며, EnrichedCart를 통해 상품 정보까지 포함된 응답을 제공합니다.
