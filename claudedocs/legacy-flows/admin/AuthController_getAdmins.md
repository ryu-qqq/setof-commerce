# API Flow Documentation: AuthController.getAdmins

## 📌 기본 정보

| 항목 | 값 |
|------|-----|
| HTTP Method | GET |
| Path | `/api/v1/auth` |
| Controller | `AuthController` |
| Service | `AdminQueryService` |
| Repository | `AdministratorsQueryRepository` |

---

## 📥 Request

### Request Parameters

| 파라미터 | 타입 | 필수 | 설명 | 기본값 |
|----------|------|------|------|--------|
| page | int | ❌ | 페이지 번호 | 0 |
| size | int | ❌ | 페이지 크기 | 20 |
| sort | String | ❌ | 정렬 기준 | - |

### Request 예시

```
GET /api/v1/auth?page=0&size=20
```

---

## 📤 Response

### Response DTO 구조

```java
// CustomPageable<AdministratorResponse>
public class CustomPageable<T> implements Page<T> {
    private final List<T> content;
    private final Pageable pageable;
    private final long totalElements;
    private final Long lastDomainId;
}

// AdministratorResponse
@Getter
public class AdministratorResponse {
    private final long id;              // 관리자 ID
    private final String email;         // 이메일
    private final String fullName;      // 이름
    private final String phoneNumber;   // 전화번호
    private final long sellerId;        // 소속 셀러 ID
    private final String sellerName;    // 소속 셀러명
    private final ApprovalStatus approvalStatus;  // 승인 상태
}

// ApprovalStatus
public enum ApprovalStatus {
    PENDING,   // 대기
    APPROVED,  // 승인
    REJECTED   // 거절
}
```

### Response JSON 예시

```json
{
  "success": true,
  "data": {
    "content": [
      {
        "id": 1,
        "email": "admin@example.com",
        "fullName": "홍길동",
        "phoneNumber": "010-1234-5678",
        "sellerId": 100,
        "sellerName": "셀러A",
        "approvalStatus": "APPROVED"
      }
    ],
    "totalElements": 50,
    "totalPages": 3,
    "number": 0,
    "size": 20,
    "lastDomainId": 1
  }
}
```

---

## 🔄 호출 흐름

```
┌──────────────────────────────────────────────────────────────┐
│   AuthController                                              │
│   @GetMapping("/auth")                                        │
│   getAdmins(Pageable pageable)                               │
└────────────────────────┬─────────────────────────────────────┘
                         │
                         ▼
┌──────────────────────────────────────────────────────────────┐
│   AdminQueryService                                           │
│   @Transactional(readOnly = true)                            │
│   fetchAdmins(Pageable pageable)                             │
│                                                               │
│   1. administratorsQueryRepository.fetchAdministrators()     │
│   2. administratorsQueryRepository.fetchAdminCount()         │
│   3. adminPageableMapper.toAdministratorResponse()           │
└────────────────────────┬─────────────────────────────────────┘
                         │
                         ▼
┌──────────────────────────────────────────────────────────────┐
│   AdministratorsQueryRepository                               │
│   fetchAdministrators(Pageable) → List<AdministratorResponse>│
│   fetchAdminCount() → long                                   │
└────────────────────────┬─────────────────────────────────────┘
                         │
                         ▼
┌──────────────────────────────────────────────────────────────┐
│   Database                                                    │
│   Tables: administrators, seller                              │
│   JOIN: INNER JOIN seller ON seller.id = administrators.sellerId │
└──────────────────────────────────────────────────────────────┘
```

---

## 🗄️ Database Query 분석

### 조회 테이블

| 테이블 | Alias | 조인 유형 | 조인 조건 |
|--------|-------|----------|----------|
| administrators | administrators | FROM | - |
| seller | seller | INNER JOIN | `seller.id = administrators.sellerId` |

### Entity 매핑

**administrators 테이블**
| 컬럼명 | 필드 | 타입 |
|--------|------|------|
| admin_id | id | long (PK) |
| EMAIL | email | String(100) |
| FULL_NAME | fullName | String(45) |
| PHONE_NUMBER | phoneNumber | String(15) |
| seller_id | sellerId | long |
| approval_status | approvalStatus | ApprovalStatus (Enum) |
| PASSWORD_HASH | passwordHash | String(60) |
| REFRESH_TOKEN | refreshToken | String(256) |

**seller 테이블**
| 컬럼명 | 필드 | 타입 |
|--------|------|------|
| seller_id | id | long (PK) |
| sellerName | sellerName | String |

### QueryDSL 코드

```java
// 목록 조회
queryFactory
    .select(
        new QAdministratorResponse(
            administrators.id,
            administrators.email,
            administrators.fullName,
            administrators.phoneNumber,
            administrators.sellerId,
            seller.sellerName,
            administrators.approvalStatus
        )
    )
    .from(administrators)
    .innerJoin(seller)
        .on(seller.id.eq(administrators.sellerId))
    .limit(pageable.getPageSize())
    .offset(pageable.getPageNumber())
    .fetch();

// 카운트 조회
queryFactory
    .select(administrators.count())
    .from(administrators)
    .fetchOne();
```

---

## 📂 관련 파일

| 구분 | 파일 경로 |
|------|----------|
| Controller | `bootstrap-legacy-web-api-admin/.../auth/controller/AuthController.java` |
| Service | `bootstrap-legacy-web-api-admin/.../auth/service/AdminQueryService.java` |
| Repository | `bootstrap-legacy-web-api-admin/.../auth/repository/AdministratorsQueryRepository.java` |
| DTO | `bootstrap-legacy-web-api-admin/.../auth/dto/AdministratorResponse.java` |
| Mapper | `bootstrap-legacy-web-api-admin/.../auth/mapper/AdminPageableMapper.java` |
| Entity | `bootstrap-legacy-web-api-admin/.../auth/entity/Administrators.java` |
| Entity | `bootstrap-legacy-web-api-admin/.../seller/entity/Seller.java` |

---

## 🔗 다음 단계

DTO 변환:
```bash
/legacy-convert admin:AuthController.getAdmins
```

Persistence Layer 생성:
```bash
/legacy-query admin:AuthController.getAdmins
```
