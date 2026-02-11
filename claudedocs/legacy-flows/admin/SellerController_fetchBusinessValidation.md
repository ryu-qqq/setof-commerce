# API Flow Documentation: SellerController.fetchBusinessValidation

## 📌 기본 정보

| 항목 | 값 |
|------|-----|
| HTTP Method | GET |
| Path | `/api/v1/sellers/business-validation` |
| Controller | `SellerController` |
| Service | `SellerFetchService` → `SellerFetchServiceImpl` |
| Repository | `SellerFetchRepository` → `SellerFetchRepositoryImpl` |
| Authorization | 없음 (공개 API) |

---

## 📥 Request

### Request Parameters

| 파라미터 | 타입 | 필수 | 설명 | Validation |
|----------|------|------|------|------------|
| registrationNumber | String | ✅ | 사업자등록번호 | - |

### Request DTO 구조

```java
public class BusinessValidation {
    private String registrationNumber;
}
```

### Request 예시

```
GET /api/v1/sellers/business-validation?registrationNumber=123-45-67890
```

---

## 📤 Response

### Response DTO 구조

```java
// Boolean 반환
// true: 중복 사업자등록번호 존재 (이미 등록됨)
// false: 중복 없음 (사용 가능)
```

### Response JSON 예시

```json
{
  "data": true,
  "response": {
    "status": 200,
    "message": "success"
  }
}
```

---

## 🔄 호출 흐름

```
┌──────────────────────────────────────────────┐
│   Controller                                  │  SellerController.fetchBusinessValidation(businessValidation)
│   (REST API)                                  │  @GetMapping("/sellers/business-validation")
│                                               │  ⚠️ 인증 없음 (공개 API)
└────────────────────┬─────────────────────────┘
                     │
                     ▼
┌──────────────────────────────────────────────┐
│   Service                                     │  SellerFetchServiceImpl.fetchBusinessValidation(businessValidation)
│   (Business Logic)                            │  @Transactional(readOnly = true)
│                                               │
│   → sellerFetchRepository.fetchBusinessValidation()
└────────────────────┬─────────────────────────┘
                     │
                     ▼
┌──────────────────────────────────────────────┐
│   Repository                                  │  SellerFetchRepositoryImpl.fetchBusinessValidation()
│   (Data Access)                               │  QueryDSL
└────────────────────┬─────────────────────────┘
                     │
                     ▼
┌──────────────────────────────────────────────┐
│   Database                                    │  Tables: seller, seller_business_info
└──────────────────────────────────────────────┘
```

---

## 🗄️ Database Query 분석

### 조회 테이블

| 테이블 | Alias | 조인 유형 | 조인 조건 |
|--------|-------|----------|----------|
| seller | seller | FROM | - |
| seller_business_info | sellerBusinessInfo | INNER JOIN | sellerBusinessInfo.id = seller.id |

### QueryDSL 코드

```java
List<Long> fetch = getQueryFactory()
    .select(
        sellerBusinessInfo.id
    )
    .from(seller)
    .innerJoin(sellerBusinessInfo)
        .on(sellerBusinessInfo.id.eq(seller.id))
    .where(
        deleteYn(),
        sellerBusinessInfo.registrationNumber.eq(businessValidation.getRegistrationNumber())
    )
    .fetch();

return !fetch.isEmpty();
```

### WHERE 조건

| 조건 | 필드 | 설명 |
|------|------|------|
| deleteYn | seller.deleteYn | 삭제되지 않은 데이터만 (delete_yn = 'N') |
| registrationNumberEq | sellerBusinessInfo.registrationNumber | 사업자등록번호 일치 |

---

## 📋 관련 Entity

### Seller (seller 테이블)
| 컬럼 | 타입 | 설명 |
|------|------|------|
| seller_id | long | PK |
| delete_yn | Yn | 삭제 여부 |

### SellerBusinessInfo (seller_business_info 테이블)
| 컬럼 | 타입 | 설명 |
|------|------|------|
| seller_id | long | PK (seller FK) |
| registration_number | String | 사업자등록번호 |

---

## ⚠️ 특이사항

1. **공개 API**: 인증 없이 접근 가능 (회원가입 전 중복 체크 용도)
2. **중복 검사 목적**: 이미 등록된 사업자등록번호인지 확인
3. **반환값 의미**:
   - `true`: 해당 사업자등록번호가 이미 존재함 (중복)
   - `false`: 해당 사업자등록번호 사용 가능

---

## 🔧 사용 시나리오

```
1. 셀러 회원가입 화면
2. 사업자등록번호 입력
3. "중복 확인" 버튼 클릭
4. 이 API 호출
5. true 반환 시 → "이미 등록된 사업자등록번호입니다" 메시지
6. false 반환 시 → "사용 가능한 사업자등록번호입니다" 메시지
```
