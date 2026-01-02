# ProductStock 모듈 분석

> 작성일: 2025-12-29
> 상태: 🟢 분석완료

---

## 1. 모듈 개요

| 항목 | 내용 |
|------|------|
| 도메인 | 상품 재고 관리 |
| 주요 기능 | 재고 초기화, 설정, 차감, 복원 |
| 복잡도 | 낮음 |
| 어드민 조회 | 불필요 (상품별 단건 조회) |

---

## 2. 컴포넌트 구조

### 2.1 파일 목록

```
productstock/
├── assembler/
│   └── ProductStockAssembler.java
├── dto/
│   ├── command/
│   │   ├── InitializeStockCommand.java
│   │   ├── SetStockCommand.java
│   │   ├── DeductStockCommand.java
│   │   └── RestoreStockCommand.java
│   └── response/
│       └── ProductStockResponse.java
├── factory/
│   └── command/
│       └── ProductStockCommandFactory.java
├── manager/
│   ├── command/
│   │   └── ProductStockPersistenceManager.java
│   └── query/
│       └── ProductStockReadManager.java
├── port/
│   ├── in/
│   │   ├── command/
│   │   │   ├── InitializeStockUseCase.java
│   │   │   ├── SetStockUseCase.java
│   │   │   ├── DeductStockUseCase.java
│   │   │   └── RestoreStockUseCase.java
│   │   └── query/
│   │       └── GetProductStockUseCase.java
│   └── out/
│       ├── command/
│       │   └── ProductStockPersistencePort.java
│       └── query/
│           └── ProductStockQueryPort.java
└── service/
    ├── command/
    │   ├── InitializeStockService.java
    │   ├── SetStockService.java
    │   ├── DeductStockService.java
    │   └── RestoreStockService.java
    └── query/
        └── ProductStockQueryService.java
```

### 2.2 컴포넌트 분석

#### Port-Out (QueryPort)
```java
public interface ProductStockQueryPort {
    Optional<ProductStock> findByProductId(ProductId productId);
    List<ProductStock> findByProductIds(List<Long> productIds);
    Optional<ProductStock> findById(ProductStockId productStockId);
}
```
✅ **컨벤션 준수**: 단순 조회 메서드만 제공

#### ReadManager
```java
@Component
public class ProductStockReadManager {
    @Transactional(readOnly = true)
    public ProductStock findByProductId(ProductId productId) { ... }

    @Transactional(readOnly = true)
    public List<ProductStock> findByProductIds(List<Long> productIds) { ... }

    @Transactional(readOnly = true)
    public ProductStock findById(ProductStockId productStockId) { ... }
}
```
✅ **컨벤션 준수**: 메서드 레벨 @Transactional(readOnly=true)

#### Service
```java
@Service
public class ProductStockQueryService implements GetProductStockUseCase {
    // @Transactional 없음 ✅
    // Manager만 호출 ✅
}
```
✅ **컨벤션 준수**

---

## 3. 컨벤션 준수 현황

### 3.1 체크리스트

| 항목 | 상태 | 비고 |
|------|------|------|
| **Port-In** | ✅ | UseCase 인터페이스 분리 |
| **Port-Out** | ✅ | QueryPort/PersistencePort 분리 |
| **Service @Transactional 금지** | ✅ | |
| **Manager @Transactional** | ✅ | readOnly=true |
| **Port 직접 호출 금지** | ✅ | Service → Manager |
| **Lombok 금지** | ✅ | |
| **Assembler toDomain 금지** | ✅ | toResponse만 |

### 3.2 어드민 조회 조건

| 조건 | 필요 여부 | 현재 상태 |
|------|----------|----------|
| 복합 필터 | ❌ 불필요 | - |
| 정렬 | ❌ 불필요 | - |
| 기간 조회 | ❌ 불필요 | - |
| 키워드 검색 | ❌ 불필요 | - |
| 페이지네이션 | ❌ 불필요 | - |

> **분석**: 재고는 상품(Product)별로 1:1 매핑되어 있어 상품그룹 조회 시 함께 조회됨. 별도의 어드민 검색 화면이 필요 없음.

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

**productstock 모듈은 현재 컨벤션을 잘 준수하고 있어 리팩토링이 필요하지 않습니다.**

재고 데이터는 상품별로 단건 조회되며, 어드민에서 재고 목록을 별도로 검색하는 기능은 상품 목록 검색에 포함되어 처리됩니다.
