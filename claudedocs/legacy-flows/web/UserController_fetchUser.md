# API Flow: UserController.fetchUser

## 1. 기본 정보

| 항목 | 값 |
|------|-----|
| HTTP | GET /api/v1/user |
| 인증 | @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')") |
| Controller | UserController |
| Service Interface | UserFindService |
| Service Impl | UserFindServiceImpl |
| Repository Interface | UserFindRepository |
| Repository Impl | UserFindRepositoryImpl |

---

## 2. Request

### Parameters

파라미터 없음. 사용자 식별은 SecurityContext에서 자동 추출.

### 인증 방식

쿠키 기반 JWT 토큰 (`token` 쿠키명).

---

## 3. Response

### DTO 구조

```
ResponseEntity<ApiResponse<JoinedUser>>
└── ApiResponse<JoinedUser>
    ├── data: JoinedUser
    │   ├── isJoined: boolean          // 가입 여부
    │   └── joinedUser: JoinedDto
    │       ├── name: String           // 사용자 이름
    │       ├── userId: long           // 사용자 PK
    │       ├── socialLoginType: Enum  // kakao | naver | none
    │       ├── phoneNumber: String    // 전화번호
    │       ├── socialPkId: String     // 소셜 로그인 PK ID
    │       ├── currentMileage: double // 현재 마일리지
    │       ├── joinedDate: LocalDateTime // 가입일시
    │       └── deleteYn: Enum         // 탈퇴 여부 (Y/N)
    │           (* @JsonIgnore: isEmailUser(), isWithdrawalUser() 메서드는 직렬화 제외)
    └── response: Response
        ├── status: int    // 200
        └── message: String // "success"
```

### JSON 예시

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

**사용자가 존재하지 않을 경우 (isJoined: false)**

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

## 4. SecurityContext 사용자 ID 추출 흐름

요청이 컨트롤러에 도달하기 전, 다음 순서로 JWT 파싱 및 인증 설정이 완료된다.

```
[HTTP Request] (Cookie: token=<JWT>)
    │
    v
[TokenAuthenticationFilter.doFilterInternal()]
    │
    ├── CookieUtils.getCookie(request, "token")       // 쿠키에서 액세스 토큰 추출
    │
    ├── AuthTokenProvider.getUserIdByToken(tokenStr)  // JWT Claims에서 subject(userId) 파싱
    │       └── Jwts.parserBuilder().setSigningKey(key)
    │               .build().parseClaimsJws(token).getBody().getSubject()
    │
    ├── UserFindServiceImpl.fetchUser(userId)          // Redis or DB에서 UserDto 조회
    │
    ├── AuthTokenProvider.getAuthentication(userDto)   // UserPrincipal 생성
    │       └── UserPrincipal.create(user)
    │               → userId, name, phoneNumber, authorities([NORMAL_GRADE]) 세팅
    │
    └── SecurityContextHolder.getContext().setAuthentication(authentication)
            // UsernamePasswordAuthenticationToken으로 SecurityContext에 등록

    [토큰 만료 시]
    ├── ExpiredJwtException 발생
    ├── CookieUtils.getCookie(request, "refresh_token") // Refresh Token 쿠키 확인
    ├── RefreshTokenRedisService.findByUserId(userId)    // Redis에서 Refresh Token 조회
    ├── 일치하면 신규 AccessToken 발급 → CookieUtils.setTokenInCookie()
    │       response.setHeader("X-Refreshed-Access-Token", "true")
    └── setAuthentication(newAccessToken)               // 재인증 후 필터 체인 계속
```

**컨트롤러 진입 후 userId 추출**

```
UserFindServiceImpl.fetchJoinedUser()
    └── SecurityUtils.currentUserId()
            └── SecurityContextHolder.getContext().getAuthentication()
                    → (UserPrincipal) authentication.getPrincipal()
                    → userPrincipal.getUserId()   // long 타입 userId 반환
```

---

## 5. 호출 흐름

```
UserController.fetchUser()
    │  @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    │  GET /api/v1/user
    │
    └── UserFindService.fetchJoinedUser()
            │  [인터페이스]
            │
            └── UserFindServiceImpl.fetchJoinedUser()
                    │  @Transactional(readOnly = true)
                    │
                    ├── SecurityUtils.currentUserId()
                    │       └── SecurityContextHolder → UserPrincipal.getUserId()
                    │
                    └── UserFindRepository.fetchUser(userId)
                            │  [인터페이스]
                            │
                            └── UserFindRepositoryImpl.fetchUser(userId)
                                    │  QueryDSL
                                    │
                                    └── JPAQueryFactory
                                            .select(new QUserDto(...))
                                            .from(users)
                                            .join(userGrade).on(...)
                                            .join(userMileage).on(...)
                                            .where(users.id.eq(userId))
                                            .fetchOne()

        결과 처리:
        ├── UserDto 존재 → userDto.joinedDto() 변환
        │       → new JoinedUser(true, joinedDto)
        └── UserDto 없음 → new JoinedUser(false)
```

**참고: UserFindServiceImpl.fetchJoinedUser()는 Redis 캐시 레이어를 거치지 않는다.**
(Redis를 거치는 경로는 `fetchUser(userId)` 메서드이며, `fetchJoinedUser()`는 DB를 직접 조회한다.)

---

## 6. Database Query

### 관련 테이블

| 테이블 | 역할 | JOIN 조건 |
|--------|------|-----------|
| users | 사용자 기본 정보 (FROM) | - |
| user_grade | 사용자 등급 정보 | user_grade.user_grade_id = users.user_grade_id |
| user_mileage | 사용자 마일리지 | user_mileage.user_id = users.user_id |

### QueryDSL

```java
// UserFindRepositoryImpl.fetchUser(long userId)
queryFactory
    .select(
        new QUserDto(
            users.id,               // userId
            users.socialLoginType,  // socialLoginType
            users.phoneNumber,      // phoneNumber
            users.name,             // name
            userGrade.gradeName,    // userGrade (UserGradeEnum)
            userMileage.currentMileage, // currentMileage
            users.insertDate        // joinedDate
        ))
    .from(users)
    .join(userGrade)
        .on(userGrade.id.eq(users.userGradeId))
    .join(userMileage)
        .on(userMileage.id.eq(users.id))
    .where(users.id.eq(userId))
    .fetchOne()
```

### 동등 SQL

```sql
SELECT
    u.user_id,
    u.social_login_type,
    u.phone_number,
    u.name,
    ug.grade_name,
    um.current_mileage,
    u.insert_date
FROM users u
JOIN user_grade ug ON ug.user_grade_id = u.user_grade_id
JOIN user_mileage um ON um.user_id = u.user_id
WHERE u.user_id = :userId
LIMIT 1
```

---

## 7. Entity - 테이블 매핑

| Entity | 테이블 | PK 컬럼 |
|--------|--------|---------|
| Users | users | user_id |
| UserGrade | user_grade | user_grade_id |
| UserMileage | user_mileage | user_id (FK = users.user_id, @MapsId) |

**UserMileage는 @MapsId로 Users와 동일한 PK를 공유하는 1:1 관계.**

---

## 8. 예외 처리

| 상황 | 예외 | 처리 |
|------|------|------|
| 인증 토큰 없음 | - | @PreAuthorize 에 의해 403 Forbidden |
| 권한 부족 (GUEST 등) | AuthForbiddenException | 403 반환 |
| 토큰 만료 | ExpiredJwtException | Refresh Token으로 재발급 시도 후 필터 체인 진행 |
| 토큰 무효 | (null 반환) | SecurityContext 미설정 → @PreAuthorize 403 |
| DB에 사용자 없음 | - | JoinedUser(false) 반환 (예외 아님) |
