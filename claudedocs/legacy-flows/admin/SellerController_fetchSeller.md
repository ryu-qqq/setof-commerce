# API Flow Documentation: SellerController.fetchSeller

## 📌 기본 정보

| 항목 | 값 |
|------|-----|
| HTTP Method | GET |
| Path | `/api/v1/seller` |
| Controller | `SellerController` |
| Service | `SellerFetchService` → `SellerFetchServiceImpl` |
| Repository | N/A (인증 정보에서 직접 조회) |
| Authorization | `HAS_ANY_AUTHORITY_MASTER_SELLER` |

---

## 📥 Request

### Request Parameters

| 파라미터 | 타입 | 필수 | 설명 | Validation |
|----------|------|------|------|------------|
| - | - | - | 파라미터 없음 (인증 토큰에서 조회) | - |

> 🔐 **인증 기반 조회**: SecurityContext에서 현재 로그인한 사용자 정보를 조회

---

## 📤 Response

### Response DTO 구조

```java
// SellerContext (Interface)
public interface SellerContext {
    Long getSellerId();
    String getEmail();
    String getPasswordHash();
    RoleType getRoleType();
    ApprovalStatus getApprovalStatus();
}

// BaseSellerContext (Implementation)
public class BaseSellerContext implements SellerContext {
    protected Long sellerId;
    protected String email;
    private String passwordHash;
    private RoleType roleType;          // MASTER, SELLER
    private ApprovalStatus approvalStatus;  // PENDING, APPROVED, REJECTED
}
```

### Response JSON 예시

```json
{
  "data": {
    "sellerId": 1,
    "email": "seller@example.com",
    "passwordHash": "hashed_password",
    "roleType": "SELLER",
    "approvalStatus": "APPROVED"
  },
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
│   Controller                                  │  SellerController.fetchSeller()
│   (REST API)                                  │  @GetMapping("/seller")
│                                               │  @PreAuthorize(HAS_ANY_AUTHORITY_MASTER_SELLER)
└────────────────────┬─────────────────────────┘
                     │
                     ▼
┌──────────────────────────────────────────────┐
│   Service                                     │  SellerFetchServiceImpl.fetchAuthorizedSeller()
│   (Business Logic)                            │  @Transactional(readOnly = true)
└────────────────────┬─────────────────────────┘
                     │
                     ▼
┌──────────────────────────────────────────────┐
│   SecurityUtils                               │  SecurityUtils.getAuthentication()
│   (Security Context)                          │  → UserPrincipal 조회
└────────────────────┬─────────────────────────┘
                     │
                     ▼
┌──────────────────────────────────────────────┐
│   BaseSellerContext 생성                      │  new BaseSellerContext(
│   (DTO Assembly)                              │      principal.sellerId(),
│                                               │      principal.email(),
│                                               │      principal.getPassword(),
│                                               │      SecurityUtils.getAuthorization(),
│                                               │      ApprovalStatus.APPROVED
│                                               │  )
└──────────────────────────────────────────────┘
```

---

## 🗄️ Database Query 분석

### 조회 테이블

> ⚠️ **DB 조회 없음**: 이 API는 SecurityContext에서 인증 정보를 직접 조회하므로 DB 쿼리가 발생하지 않음

### 데이터 흐름

1. JWT 토큰에서 `UserPrincipal` 추출
2. `UserPrincipal`에서 sellerId, email, password 획득
3. `SecurityUtils.getAuthorization()`으로 RoleType 획득
4. `BaseSellerContext` 객체 생성 후 반환

---

## 📋 관련 Enum

### ApprovalStatus
```java
public enum ApprovalStatus {
    PENDING,    // 승인 대기
    APPROVED,   // 승인됨
    REJECTED    // 거부됨
}
```

### RoleType
- `MASTER`: 관리자
- `SELLER`: 판매자
