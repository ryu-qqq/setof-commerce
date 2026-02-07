# API Flow Documentation: AuthController.getAdminsBySellerId

## 📌 기본 정보

| 항목 | 값 |
|------|-----|
| HTTP Method | GET |
| Path | `/api/v1/auth/{sellerId}` |
| Controller | `AuthController` |
| Service | `AdminQueryService` |
| Repository | `AdministratorsQueryRepository` |

---

## 📥 Request

### Request Parameters

| 파라미터 | 타입 | 필수 | 설명 | 위치 |
|----------|------|------|------|------|
| sellerId | long | ✅ | 셀러 ID | @PathVariable |
| page | int | ❌ | 페이지 번호 | Query Param |
| size | int | ❌ | 페이지 크기 | Query Param |
| sort | String | ❌ | 정렬 기준 | Query Param |

### Request 예시

```
GET /api/v1/auth/100?page=0&size=20
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
```

### Response JSON 예시

```json
{
  "success": true,
  "data": {
    "content": [
      {
        "id": 5,
        "email": "seller-admin@example.com",
        "fullName": "김셀러",
        "phoneNumber": "010-9876-5432",
        "sellerId": 100,
        "sellerName": "셀러A",
        "approvalStatus": "APPROVED"
      },
      {
        "id": 6,
        "email": "seller-staff@example.com",
        "fullName": "이직원",
        "phoneNumber": "010-1111-2222",
        "sellerId": 100,
        "sellerName": "셀러A",
        "approvalStatus": "PENDING"
      }
    ],
    "totalElements": 2,
    "totalPages": 1,
    "number": 0,
    "size": 20,
    "lastDomainId": 6
  }
}
```

---

## 🔄 호출 흐름

```
┌──────────────────────────────────────────────────────────────┐
│   AuthController                                              │
│   @GetMapping("/auth/{sellerId}")                             │
│   getAdminsBySellerId(@PathVariable long sellerId, Pageable) │
└────────────────────────┬─────────────────────────────────────┘
                         │
                         ▼
┌──────────────────────────────────────────────────────────────┐
│   AdminQueryService                                           │
│   @Transactional(readOnly = true)                            │
│   fetchAdminsBySellerId(long sellerId, Pageable pageable)    │
│                                                               │
│   1. administratorsQueryRepository                            │
│         .fetchAdministratorBySellerId(sellerId, pageable)    │
│   2. administratorsQueryRepository                            │
│         .fetchAdminBySellerIdCount(sellerId)                 │
│   3. adminPageableMapper.toAdministratorResponse()           │
└────────────────────────┬─────────────────────────────────────┘
                         │
                         ▼
┌──────────────────────────────────────────────────────────────┐
│   AdministratorsQueryRepository                               │
│   fetchAdministratorBySellerId(sellerId, Pageable)           │
│   → List<AdministratorResponse>                              │
│                                                               │
│   fetchAdminBySellerIdCount(sellerId) → long                 │
└────────────────────────┬─────────────────────────────────────┘
                         │
                         ▼
┌──────────────────────────────────────────────────────────────┐
│   Database                                                    │
│   Tables: administrators, seller                              │
│   JOIN: INNER JOIN seller ON                                  │
│         seller.id = administrators.sellerId                   │
│         AND seller.id = ?                                     │
└──────────────────────────────────────────────────────────────┘
```

---

## 🗄️ Database Query 분석

### 조회 테이블

| 테이블 | Alias | 조인 유형 | 조인 조건 |
|--------|-------|----------|----------|
| administrators | administrators | FROM | - |
| seller | seller | INNER JOIN | `seller.id = administrators.sellerId AND seller.id = sellerId` |

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
        .on(seller.id.eq(administrators.sellerId), seller.id.eq(sellerId))
    .limit(pageable.getPageSize())
    .offset(pageable.getPageNumber())
    .fetch();

// 카운트 조회
queryFactory
    .select(administrators.count())
    .from(administrators)
    .innerJoin(seller)
        .on(seller.id.eq(administrators.sellerId), seller.id.eq(sellerId))
    .fetchOne();
```

---

## 📝 비즈니스 로직

1. `sellerId`로 해당 셀러 소속 관리자 목록 조회
2. 페이징 처리 (limit, offset)
3. 총 카운트 별도 쿼리
4. `CustomPageable`로 래핑하여 반환

---

## 📂 관련 파일

| 구분 | 파일 경로 |
|------|----------|
| Controller | `bootstrap-legacy-web-api-admin/.../auth/controller/AuthController.java` |
| Service | `bootstrap-legacy-web-api-admin/.../auth/service/AdminQueryService.java` |
| Repository | `bootstrap-legacy-web-api-admin/.../auth/repository/AdministratorsQueryRepository.java` |
| DTO | `bootstrap-legacy-web-api-admin/.../auth/dto/AdministratorResponse.java` |
| Mapper | `bootstrap-legacy-web-api-admin/.../auth/mapper/AdminPageableMapper.java` |

---

## 🔗 다음 단계

DTO 변환:
```bash
/legacy-convert admin:AuthController.getAdminsBySellerId
```

Persistence Layer 생성:
```bash
/legacy-query admin:AuthController.getAdminsBySellerId
```
