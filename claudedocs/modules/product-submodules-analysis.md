# Product 하위 모듈 분석

> 작성일: 2025-12-29
> 상태: 🟢 분석완료
> 포함 모듈: productdescription, productimage, productnotice

---

## 1. 모듈 개요

세 모듈은 상품 그룹(ProductGroup)의 하위 정보를 관리하며, 동일한 패턴을 따릅니다.

| 모듈 | 도메인 | 관계 | 주요 기능 |
|------|--------|------|----------|
| productdescription | 상품 설명 | 1:1 | 상세 설명, 이미지 URL |
| productimage | 상품 이미지 | 1:N | 대표 이미지, 상세 이미지 |
| productnotice | 상품 고시 | 1:1 | 법적 고시 정보 |

---

## 2. 공통 구조

### 2.1 컴포넌트 패턴

```
product{submodule}/
├── assembler/
│   └── Product{Submodule}Assembler.java
├── dto/
│   ├── command/
│   │   ├── RegisterProduct{Submodule}Command.java
│   │   └── UpdateProduct{Submodule}Command.java
│   └── response/
│       └── Product{Submodule}Response.java
├── factory/
│   └── command/
│       └── Product{Submodule}CommandFactory.java
├── manager/
│   ├── command/
│   │   └── Product{Submodule}PersistenceManager.java
│   └── query/
│       └── Product{Submodule}ReadManager.java
├── port/
│   ├── in/
│   │   ├── command/
│   │   │   └── UpdateProduct{Submodule}UseCase.java
│   │   └── query/
│   │       └── GetProduct{Submodule}UseCase.java
│   └── out/
│       ├── command/
│       │   └── Product{Submodule}PersistencePort.java
│       └── query/
│           └── Product{Submodule}QueryPort.java
└── service/
    ├── command/
    │   └── UpdateProduct{Submodule}Service.java
    └── query/
        └── Product{Submodule}QueryService.java
```

### 2.2 QueryPort 패턴

모든 하위 모듈은 동일한 QueryPort 패턴을 따릅니다:

```java
public interface Product{Submodule}QueryPort {
    Optional<Product{Submodule}> findById(Product{Submodule}Id id);
    Optional<Product{Submodule}> findByProductGroupId(ProductGroupId productGroupId);
    // productimage만 List 반환
    List<ProductImage> findByProductGroupId(ProductGroupId productGroupId);
}
```

---

## 3. 개별 모듈 분석

### 3.1 ProductDescription (상품 설명)

#### 파일 목록
```
productdescription/
├── dto/command/
│   ├── RegisterProductDescriptionCommand.java
│   ├── UpdateProductDescriptionCommand.java
│   └── DescriptionImageDto.java
├── dto/response/
│   ├── ProductDescriptionResponse.java
│   └── DescriptionImageResponse.java
└── ... (표준 구조)
```

#### QueryPort
```java
public interface ProductDescriptionQueryPort {
    Optional<ProductDescription> findById(ProductDescriptionId id);
    Optional<ProductDescription> findByProductGroupId(ProductGroupId productGroupId);
}
```
✅ **1:1 관계로 단건 조회만 필요**

### 3.2 ProductImage (상품 이미지)

#### 파일 목록
```
productimage/
├── dto/command/
│   ├── RegisterProductImageCommand.java
│   └── UpdateProductImageCommand.java
├── dto/response/
│   └── ProductImageResponse.java
├── service/command/
│   ├── UpdateProductImageService.java
│   └── DeleteProductImageService.java  // 삭제 기능 추가
└── ... (표준 구조)
```

#### QueryPort
```java
public interface ProductImageQueryPort {
    Optional<ProductImage> findById(ProductImageId id);
    List<ProductImage> findByProductGroupId(ProductGroupId productGroupId);
}
```
✅ **1:N 관계로 List 반환**

#### 특이사항
- `DeleteProductImageService` - 이미지 개별 삭제 기능 제공
- `ProductImageWriteManager` (PersistenceManager 대신)

### 3.3 ProductNotice (상품 고시)

#### 파일 목록
```
productnotice/
├── dto/command/
│   ├── RegisterProductNoticeCommand.java
│   ├── UpdateProductNoticeCommand.java
│   └── NoticeItemDto.java
├── dto/response/
│   ├── ProductNoticeResponse.java
│   └── NoticeItemResponse.java
└── ... (표준 구조)
```

#### QueryPort
```java
public interface ProductNoticeQueryPort {
    Optional<ProductNotice> findById(ProductNoticeId id);
    Optional<ProductNotice> findByProductGroupId(ProductGroupId productGroupId);
}
```
✅ **1:1 관계로 단건 조회만 필요**

---

## 4. 컨벤션 준수 현황

### 4.1 공통 체크리스트

| 항목 | description | image | notice |
|------|-------------|-------|--------|
| **Port-In** | ✅ | ✅ | ✅ |
| **Port-Out** | ✅ | ✅ | ✅ |
| **Service @Transactional 금지** | ✅ | ✅ | ✅ |
| **Manager @Transactional** | ✅ | ✅ | ✅ |
| **Lombok 금지** | ✅ | ✅ | ✅ |

### 4.2 어드민 조회 조건

| 조건 | 필요 여부 | 비고 |
|------|----------|------|
| 복합 필터 | ❌ 불필요 | ProductGroup 조회 시 함께 로드 |
| 정렬 | ❌ 불필요 | - |
| 기간 조회 | ❌ 불필요 | - |
| 키워드 검색 | ❌ 불필요 | - |
| 페이지네이션 | ❌ 불필요 | - |

> **분석**: 이 모듈들은 ProductGroup의 하위 정보로, 독립적인 검색이 필요 없습니다. 상품 그룹 조회 시 함께 로드되거나, 상품 그룹 ID로 조회합니다.

---

## 5. 리팩토링 필요 사항

### 5.1 필수 변경
없음 - 모든 모듈이 컨벤션 준수 상태

### 5.2 권장 변경
없음

---

## 6. 예상 작업량

| 모듈 | 변경 파일 수 | 난이도 |
|------|-------------|--------|
| productdescription | 0 | 🟢 없음 |
| productimage | 0 | 🟢 없음 |
| productnotice | 0 | 🟢 없음 |

---

## 7. 결론

**세 하위 모듈 모두 컨벤션을 잘 준수하고 있어 리팩토링이 필요하지 않습니다.**

### 7.1 설계 특징
1. **ProductGroup과의 관계**: 모두 ProductGroupId로 연결
2. **단순 CRUD**: 복잡한 비즈니스 로직 없음
3. **일관된 패턴**: 세 모듈이 동일한 구조 사용

### 7.2 조회 방식
- **개별 조회**: ProductGroupId로 직접 조회
- **일괄 조회**: ProductGroup 조회 시 Assembler에서 조합

```java
// ProductGroupAssembler에서 사용 예시
public ProductGroupResponse toResponse(ProductGroup group,
    ProductDescription description,
    List<ProductImage> images,
    ProductNotice notice) {
    // 조합하여 응답 생성
}
```
