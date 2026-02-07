# API Flow Documentation: AuthController.getAdminValidation

## 📌 기본 정보

| 항목 | 값 |
|------|-----|
| HTTP Method | GET |
| Path | `/api/v1/auth/admin-validation` |
| Controller | `AuthController` |
| Service | `AdminQueryService` |
| Repository | `AdministratorsQueryRepository` |

---

## 📥 Request

### Request Parameters

| 파라미터 | 타입 | 필수 | 설명 | Validation |
|----------|------|------|------|------------|
| email | String | ✅ | 검증할 이메일 | - |

### Request DTO 구조

```java
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AdminValidation {
    private String email;
}
```

### Request 예시

```
GET /api/v1/auth/admin-validation?email=admin@example.com
```

---

## 📤 Response

### Response 구조

```java
ApiResponse<Boolean>
```

### Response JSON 예시

```json
// 이메일이 존재하는 경우 (중복)
{
  "success": true,
  "data": true
}

// 이메일이 존재하지 않는 경우 (사용 가능)
{
  "success": true,
  "data": false
}
```

---

## 🔄 호출 흐름

```
┌──────────────────────────────────────────────────────────────┐
│   AuthController                                              │
│   @GetMapping("/auth/admin-validation")                       │
│   getAdminValidation(@ModelAttribute AdminValidation)        │
└────────────────────────┬─────────────────────────────────────┘
                         │
                         ▼
┌──────────────────────────────────────────────────────────────┐
│   AdminQueryService                                           │
│   fetchAdminValidation(AdminValidation adminValidation)      │
│                                                               │
│   return administratorsQueryRepository                        │
│       .fetchAdminValidation(adminValidation).isPresent();    │
└────────────────────────┬─────────────────────────────────────┘
                         │
                         ▼
┌──────────────────────────────────────────────────────────────┐
│   AdministratorsQueryRepository                               │
│   fetchAdminValidation(AdminValidation)                       │
│   → Optional<AdministratorResponse>                          │
└────────────────────────┬─────────────────────────────────────┘
                         │
                         ▼
┌──────────────────────────────────────────────────────────────┐
│   Database                                                    │
│   Tables: administrators, seller                              │
│   WHERE: administrators.email = ?                             │
└──────────────────────────────────────────────────────────────┘
```

---

## 🗄️ Database Query 분석

### 조회 테이블

| 테이블 | Alias | 조인 유형 | 조인 조건 |
|--------|-------|----------|----------|
| administrators | administrators | FROM | - |
| seller | seller | INNER JOIN | `seller.id = administrators.sellerId` |

### WHERE 조건

| 조건 | 필드 | 설명 |
|------|------|------|
| emailEq | administrators.email | 이메일 일치 여부 |

### QueryDSL 코드

```java
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
    .where(
        administrators.email.eq(adminValidation.getEmail())
    )
    .fetchOne();
```

---

## 📝 비즈니스 로직

1. 입력받은 이메일로 관리자 조회
2. 조회 결과가 있으면 `true` (이메일 중복 - 사용 불가)
3. 조회 결과가 없으면 `false` (이메일 사용 가능)

> ⚠️ **주의**: `@Transactional(readOnly = true)` 없이 호출됨

---

## 📂 관련 파일

| 구분 | 파일 경로 |
|------|----------|
| Controller | `bootstrap-legacy-web-api-admin/.../auth/controller/AuthController.java` |
| Service | `bootstrap-legacy-web-api-admin/.../auth/service/AdminQueryService.java` |
| Repository | `bootstrap-legacy-web-api-admin/.../auth/repository/AdministratorsQueryRepository.java` |
| Request DTO | `bootstrap-legacy-web-api-admin/.../auth/dto/AdminValidation.java` |

---

## 🔗 다음 단계

DTO 변환:
```bash
/legacy-convert admin:AuthController.getAdminValidation
```

Persistence Layer 생성:
```bash
/legacy-query admin:AuthController.getAdminValidation
```
