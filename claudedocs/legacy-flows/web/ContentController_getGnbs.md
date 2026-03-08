# API Flow: ContentController.getGnbs

## 1. 기본 정보

| 항목 | 내용 |
|------|------|
| HTTP Method | GET |
| API Path | /api/v1/content/gnbs |
| Controller | ContentController |
| Service Interface | GnbFindService |
| Service Impl | GnbFindServiceImpl |
| Repository Interface | GnbFindRepository |
| Repository Impl | GnbFindRepositoryImpl |
| 인증 | 불필요 (public) |
| 트랜잭션 | @Transactional(readOnly = true) |

---

## 2. Request

파라미터 없음 (No request body, No query parameters, No path variables)

---

## 3. Response

### DTO 구조

#### GnbResponse

| 필드명 | 타입 | JSON 직렬화 | 설명 |
|--------|------|------------|------|
| gnbId | long | O | GNB 식별자 |
| title | String | O | GNB 메뉴 타이틀 |
| linkUrl | String | O | 이동 링크 URL |
| displayOrder | int | X (`@JsonIgnore`) | 노출 순서 (정렬용, 응답 제외) |
| displayPeriod | DisplayPeriod | X (`@JsonIgnore`) | 노출 기간 (Redis TTL 계산용, 응답 제외) |

#### DisplayPeriod (GnbResponse 내부 - JSON 직렬화 제외)

| 필드명 | 타입 | 포맷 | 설명 |
|--------|------|------|------|
| displayStartDate | LocalDateTime | yyyy-MM-dd HH:mm:ss | 노출 시작일시 |
| displayEndDate | LocalDateTime | yyyy-MM-dd HH:mm:ss | 노출 종료일시 |

### JSON 응답 예시

```json
{
  "status": 200,
  "data": [
    {
      "gnbId": 1,
      "title": "신상품",
      "linkUrl": "/new-arrivals"
    },
    {
      "gnbId": 2,
      "title": "베스트",
      "linkUrl": "/best"
    },
    {
      "gnbId": 3,
      "title": "세일",
      "linkUrl": "/sale"
    }
  ]
}
```

---

## 4. 호출 흐름

```
[Client] GET /api/v1/content/gnbs
    │
    ▼
ContentController.getGnbs()
    │
    ├── GnbFindService.fetchGnbs()
    │       └── GnbFindServiceImpl.fetchGnbs()
    │               │
    │               ├── [1] GnbFindRepository.fetchGnbs()
    │               │       └── GnbFindRepositoryImpl.fetchGnbs()
    │               │               └── QueryDSL: SELECT FROM gnb
    │               │                   WHERE displayStartDate <= now
    │               │                     AND displayEndDate >= now
    │               │                     AND deleteYn = 'N'
    │               │                   ORDER BY display_order ASC
    │               │
    │               └── [2] GnbRedisQueryService.saveGnbsInRedis(gnbResponses)
    │                       └── GnbRedisQueryServiceImpl.saveGnbsInRedis()
    │                               ├── 가장 이른 displayEndDate 를 가진 GNB 탐색
    │                               ├── TTL = displayEndDate - now
    │                               └── Redis SET gnbs {json} EX {ttl}
    │
    └── ApiResponse.success(List<GnbResponse>)
```

### 흐름 설명

1. DB에서 현재 노출 기간 내의 GNB 목록을 조회 (항상 DB 조회 수행)
2. 조회 결과를 Redis에 Write-through 방식으로 저장
   - TTL은 가장 이른 `displayEndDate`를 기준으로 동적 산출
   - GNB가 없는 경우 Redis 저장 미수행
3. DB 조회 결과를 바로 응답으로 반환

> 주의: `GnbRedisFindService`(Redis 우선 조회 인터페이스)가 별도로 존재하나, `GnbFindServiceImpl`에서는 사용하지 않음. 매 요청마다 DB를 조회한 뒤 Redis에 갱신하는 구조임.

---

## 5. Database Query

### 대상 테이블

| 테이블 | 별칭 | 역할 |
|--------|------|------|
| gnb | gnb | GNB 메뉴 정보 |

### 조인 없음 (단일 테이블 조회)

### WHERE 조건

| 조건 | 내용 |
|------|------|
| 노출 기간 | `display_start_date <= now AND display_end_date >= now` |
| 삭제 여부 | `delete_yn = 'N'` |

### ORDER BY

| 컬럼 | 방향 |
|------|------|
| display_order | ASC |

### Projection (QueryDSL GroupBy.transform)

```
GroupBy.groupBy(gnb.id)
    .list(
        new QGnbResponse(
            gnb.id,
            gnb.gnbDetails.title,
            gnb.gnbDetails.linkUrl,
            gnb.gnbDetails.displayOrder,
            gnb.gnbDetails.displayPeriod
        )
    )
```

### QueryDSL 코드

```java
queryFactory
    .selectFrom(gnb)
    .where(betweenTime(), deleteYnNo())
    .distinct()
    .orderBy(gnb.gnbDetails.displayOrder.asc())
    .transform(
        GroupBy.groupBy(gnb.id)
            .list(
                new QGnbResponse(
                    gnb.id,
                    gnb.gnbDetails.title,
                    gnb.gnbDetails.linkUrl,
                    gnb.gnbDetails.displayOrder,
                    gnb.gnbDetails.displayPeriod)));

// betweenTime(): displayStartDate <= now AND displayEndDate >= now
// deleteYnNo(): deleteYn = 'N'
```

---

## 6. Entity 구조

### Gnb (테이블: gnb)

| 컬럼 | Java 타입 | 설명 |
|------|-----------|------|
| gnb_id (PK) | Long | 식별자 (AUTO_INCREMENT) |
| title | String | 메뉴 타이틀 |
| link_url | String | 링크 URL |
| display_order | int | 노출 순서 |
| display_start_date | LocalDateTime | 노출 시작일시 |
| display_end_date | LocalDateTime | 노출 종료일시 |
| display_yn | Yn (enum) | 노출 여부 |
| delete_yn | Yn (enum) | BaseEntity 상속 - 삭제 여부 |

> `GnbDetails`, `DisplayPeriod` 는 `@Embedded` 구조로 gnb 단일 테이블에 매핑됨.

---

## 7. Redis 캐시 구조

| 항목 | 값 |
|------|----|
| Key | `gnbs` |
| Value | `List<GnbResponse>` JSON 직렬화 문자열 |
| TTL | 가장 이른 `displayEndDate` - 현재 시각 (동적 산출) |
| 저장 시점 | DB 조회 직후 Write-through |

### Redis Key 생성 규칙

```
RedisKey.GNBS.generateKey("") → "gnbs"
```

> TTL이 동적으로 결정되므로 GNB 노출 종료 시각과 캐시 만료 시각이 일치함.
> GNB 목록이 비어있는 경우 Redis 저장을 수행하지 않음.

---

## 8. 관련 파일 경로

| 파일 | 경로 |
|------|------|
| ContentController | `module/display/controller/ContentController.java` |
| GnbFindService | `module/display/service/component/fetch/gnb/fetch/GnbFindService.java` |
| GnbFindServiceImpl | `module/display/service/component/fetch/gnb/fetch/GnbFindServiceImpl.java` |
| GnbRedisFindService | `module/display/service/component/fetch/gnb/fetch/GnbRedisFindService.java` |
| GnbRedisFindServiceImpl | `module/display/service/component/fetch/gnb/fetch/GnbRedisFindServiceImpl.java` |
| GnbRedisQueryService | `module/display/service/component/fetch/gnb/GnbRedisQueryService.java` |
| GnbRedisQueryServiceImpl | `module/display/service/component/fetch/gnb/GnbRedisQueryServiceImpl.java` |
| GnbFindRepository | `module/display/repository/gnb/GnbFindRepository.java` |
| GnbFindRepositoryImpl | `module/display/repository/gnb/GnbFindRepositoryImpl.java` |
| GnbResponse | `module/display/dto/gnb/GnbResponse.java` |
| Gnb (Entity) | `module/display/entity/gnb/Gnb.java` |
| GnbDetails (Embedded) | `module/display/entity/embedded/GnbDetails.java` |
| DisplayPeriod (Embedded) | `module/display/entity/embedded/DisplayPeriod.java` |
| AbstractRedisService | `module/common/service/AbstractRedisService.java` |
| RedisKey | `module/common/enums/RedisKey.java` |
