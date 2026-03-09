# API Flow: UserController.isExistUser

## 1. 기본 정보

| 항목 | 내용 |
|------|------|
| HTTP | GET /api/v1/user/exists |
| Controller | `com.setof.connectly.module.user.controller.UserController` |
| Service Interface | `UserFindService` |
| Service Impl | `UserFindServiceImpl` |
| Repository Interface | `UserFindRepository` |
| Repository Impl | `UserFindRepositoryImpl` |
| 인증 | 없음 (공개 API, `@PreAuthorize` 미적용) |
| 트랜잭션 | `@Transactional(readOnly = true)` (ServiceImpl) |

---

## 2. Request

### Parameters

`@ModelAttribute @Validated IsJoinedUser` — Query String 바인딩

| 이름 | 타입 | 필수 | Validation | 설명 |
|------|------|------|------------|------|
| `phoneNumber` | `String` | 필수 | `@Pattern(regexp = "010[0-9]{8}")` | 010으로 시작하는 11자리 전화번호 |

Validation 실패 메시지: `"유효하지 않은 전화번호 형식입니다."`

### Request 예시

```
GET /api/v1/user/exists?phoneNumber=01012345678
```

---

## 3. Response

### DTO 구조

**최상위: `ApiResponse<JoinedUser>`**

```
ApiResponse<JoinedUser>
├── data: JoinedUser
│   ├── isJoined: boolean          // 사용자 존재 여부
│   └── joinedUser: JoinedDto      // isJoined=false 이면 null
│       ├── name: String
│       ├── userId: long
│       ├── socialLoginType: SocialLoginType   // kakao | naver | none
│       ├── phoneNumber: String
│       ├── socialPkId: String
│       ├── currentMileage: double
│       ├── joinedDate: LocalDateTime
│       └── deleteYn: Yn           // @JsonIgnore 메서드만 있고 필드 자체는 직렬화됨
└── response: Response
    ├── status: int
    └── message: String
```

**@JsonIgnore 적용 항목:**

| 메서드 | 설명 |
|--------|------|
| `JoinedDto.isEmailUser()` | 소셜 로그인 여부 판별용, JSON 직렬화 제외 |
| `JoinedDto.isWithdrawalUser()` | 탈퇴 여부 판별용, JSON 직렬화 제외 |

### Response 예시 — 사용자 존재하는 경우

```json
{
  "data": {
    "isJoined": true,
    "joinedUser": {
      "name": "홍길동",
      "userId": 123,
      "socialLoginType": "none",
      "phoneNumber": "01012345678",
      "socialPkId": "",
      "currentMileage": 1500.0,
      "joinedDate": "2024-01-01T00:00:00",
      "deleteYn": "N"
    }
  },
  "response": {
    "status": 200,
    "message": "success"
  }
}
```

### Response 예시 — 사용자 미존재하는 경우

```json
{
  "data": {
    "isJoined": false,
    "joinedUser": null
  },
  "response": {
    "status": 200,
    "message": "success"
  }
}
```

---

## 4. 호출 흐름

```
[Client]
    |
    | GET /api/v1/user/exists?phoneNumber=01012345678
    v
[UserController.isExistUser(@ModelAttribute @Validated IsJoinedUser)]
    |
    | userFindService.fetchJoinedUser(isJoinedUser)
    v
[UserFindService (Interface)]
    |
    v
[UserFindServiceImpl.fetchJoinedUser(IsJoinedUser)]
    |
    | isJoinedUser.getPhoneNumber() 로 전화번호 추출
    | → fetchJoinedUserInDb(phoneNumber) 호출
    |
    |   ┌─────────────────────────────────────────────────────────┐
    |   │ [Redis 캐시 확인: UserFindRedisService]                  │
    |   │   NOTE: fetchJoinedUser(IsJoinedUser) 경로는             │
    |   │   Redis 캐시를 먼저 조회하지 않고 DB를 직접 조회함.       │
    |   │   (isJoinedUser(phoneNumber) 경로와 다름)                │
    |   └─────────────────────────────────────────────────────────┘
    |
    | userFindRepository.isJoinedMember(phoneNumber)
    v
[UserFindRepository (Interface)]
    |
    v
[UserFindRepositoryImpl.isJoinedMember(phoneNumber)]
    |
    | QueryDSL → MySQL SELECT
    v
[DB: users INNER JOIN user_mileage]
    |
    | Optional<JoinedDto> 반환
    v
[UserFindServiceImpl — 결과 처리]
    |
    |── 결과 있음 → userRedisQueryService.saveUser(joinedDto) (Redis 캐시 저장)
    |              → new JoinedUser(true, dto) 반환
    |
    └── 결과 없음 → new JoinedUser(false) 반환 (joinedUser=null)
    |
    v
[UserController]
    |
    | ApiResponse.success(joinedUser) 래핑
    v
[Client: HTTP 200 응답]
```

### 주요 분기 처리

| 상황 | Redis | DB 조회 | 결과 |
|------|-------|---------|------|
| `isExistUser` 경로 | 조회 안 함 | 항상 조회 | JoinedUser(true/false) |
| DB 결과 있음 | 결과를 캐시에 저장 | - | JoinedUser(isJoined=true, joinedUser=dto) |
| DB 결과 없음 | - | - | JoinedUser(isJoined=false, joinedUser=null) |

---

## 5. Database Query

### 관련 테이블

| 테이블 | 별칭 (QClass) | JOIN 방식 | 조건 |
|--------|---------------|-----------|------|
| `users` | `QUsers` | FROM (기준) | `phone_number = ?` |
| `user_mileage` | `QUserMileage` | INNER JOIN | `users.user_id = user_mileage.user_id` |

### QueryDSL 코드 (UserFindRepositoryImpl.isJoinedMember)

```java
queryFactory
    .select(
        new QJoinedDto(
            users.name,
            users.id,
            users.socialLoginType,
            users.phoneNumber,
            users.socialPkId.coalesce(""),   // NULL이면 빈 문자열로 대체
            userMileage.currentMileage,
            users.insertDate,
            users.deleteYn))
    .from(users)
    .innerJoin(userMileage)
    .on(users.id.eq(userMileage.id))
    .where(users.phoneNumber.eq(phoneNumber))
    .fetchFirst();
```

### 동등 SQL

```sql
SELECT
    u.name,
    u.user_id,
    u.social_login_type,
    u.phone_number,
    COALESCE(u.social_pk_id, '') AS social_pk_id,
    um.current_mileage,
    u.insert_date,
    u.delete_yn
FROM users u
INNER JOIN user_mileage um
    ON u.user_id = um.user_id
WHERE u.phone_number = '01012345678'
LIMIT 1;
```

### 인덱스 고려 사항

- WHERE 조건: `users.phone_number` — 단일 컬럼 동등 비교
- `fetchFirst()` 사용으로 LIMIT 1 적용, 중복 전화번호가 없다면 성능 영향 없음

---

## 6. Redis 캐시 전략

### 읽기 경로 (이 엔드포인트)

```
isExistUser 호출
    → Redis 캐시 조회 없이 DB 직접 조회
    → 조회 결과 있으면 Redis에 Write-Through 저장
```

**참고:** `isJoinedUser(String phoneNumber)` 메서드(소셜 로그인 등 내부 경로)는
Redis를 먼저 읽고, miss 발생 시 DB를 조회하는 Cache-Aside 패턴을 사용함.
`isExistUser` 엔드포인트의 `fetchJoinedUser(IsJoinedUser)` 경로는 이 캐시 조회를 거치지 않는다.

### Redis 키 구조

| Redis Key | 형식 | TTL |
|-----------|------|-----|
| `joinedUsers::{phoneNumber}` | JSON 직렬화된 `JoinedDto` | 1시간 |

### Redis 저장 코드 (UserRedisQueryServiceImpl.saveUser)

```java
String key = generateKey(RedisKey.JOINED_USERS, joinedDto.getPhoneNumber());
// → "joinedUsers::01012345678"
String value = JsonUtils.toJson(joinedDto);
save(key, value, RedisKey.JOINED_USERS.getHourDuration());
// → TTL: 1시간
```

---

## 7. 예외 처리

| 상황 | 처리 방식 |
|------|----------|
| `phoneNumber` 형식 위반 (`@Pattern`) | Spring Validation 예외 → 컨트롤러 레벨 ExceptionHandler 처리 |
| 사용자 미존재 | 예외 미발생, `JoinedUser(false)` 반환 (정상 응답) |
| DB 연결 오류 | 미처리 (런타임 예외 전파) |

---

## 8. 파일 경로 참조

| 역할 | 파일 경로 |
|------|----------|
| Controller | `bootstrap/bootstrap-legacy-web-api/src/main/java/com/setof/connectly/module/user/controller/UserController.java` |
| Request DTO | `bootstrap/bootstrap-legacy-web-api/src/main/java/com/setof/connectly/module/user/dto/join/IsJoinedUser.java` |
| Response DTO | `bootstrap/bootstrap-legacy-web-api/src/main/java/com/setof/connectly/module/user/dto/join/JoinedUser.java` |
| Inner DTO | `bootstrap/bootstrap-legacy-web-api/src/main/java/com/setof/connectly/module/user/dto/JoinedDto.java` |
| Service Interface | `bootstrap/bootstrap-legacy-web-api/src/main/java/com/setof/connectly/module/user/service/fetch/UserFindService.java` |
| Service Impl | `bootstrap/bootstrap-legacy-web-api/src/main/java/com/setof/connectly/module/user/service/fetch/UserFindServiceImpl.java` |
| Redis Find Service | `bootstrap/bootstrap-legacy-web-api/src/main/java/com/setof/connectly/module/user/service/fetch/UserFindRedisServiceImpl.java` |
| Redis Write Service | `bootstrap/bootstrap-legacy-web-api/src/main/java/com/setof/connectly/module/user/service/query/UserRedisQueryServiceImpl.java` |
| Repository Interface | `bootstrap/bootstrap-legacy-web-api/src/main/java/com/setof/connectly/module/user/repository/fetch/UserFindRepository.java` |
| Repository Impl | `bootstrap/bootstrap-legacy-web-api/src/main/java/com/setof/connectly/module/user/repository/fetch/UserFindRepositoryImpl.java` |
| Users Entity | `bootstrap/bootstrap-legacy-web-api/src/main/java/com/setof/connectly/module/user/entity/Users.java` |
| UserMileage Entity | `bootstrap/bootstrap-legacy-web-api/src/main/java/com/setof/connectly/module/user/entity/UserMileage.java` |
| Redis Key Enum | `bootstrap/bootstrap-legacy-web-api/src/main/java/com/setof/connectly/module/common/enums/RedisKey.java` |
