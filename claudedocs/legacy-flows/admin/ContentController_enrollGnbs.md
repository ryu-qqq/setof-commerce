# API Flow: ContentController.enrollGnbs

## 1. 기본 정보

| 항목 | 값 |
|------|-----|
| HTTP Method | POST |
| Path | `/api/v1/content/gnbs` |
| Controller | `ContentController` |
| Service | `GnbQueryService` → `GnbQueryServiceImpl` |
| Sub-Service | `GnbFetchService` → `GnbFetchServiceImpl` (내부 의존) |
| Repository | `GnbRepository` (JPA), `GnbFetchRepository` (조회) |
| 캐시 | Redis 캐시 삭제 (`GnbRedisQueryService`) |
| 권한 | `@PreAuthorize(HAS_AUTHORITY_MASTER)` |

## 2. Request

### Request Body (UpdateGnb)

| 필드명 | 타입 | 필수 | 설명 |
|--------|------|------|------|
| toUpdateGnbs | List\<CreateGnb\> | 선택 | 등록/수정할 GNB 목록 (gnbId=null이면 신규, 값 있으면 수정) |
| deleteGnbIds | List\<Long\> | 선택 | 삭제할 GNB ID 목록 |

**CreateGnb 구조:**

| 필드명 | 타입 | 필수 | 설명 |
|--------|------|------|------|
| gnbId | Long | 선택 | @JsonInclude(NON_NULL) - null이면 신규, 값 있으면 수정 |
| gnbDetails | GnbDetails | 필수 | GNB 상세 정보 |
| gnbDetails.title | String | 필수 | GNB 타이틀 |
| gnbDetails.linkUrl | String | 필수 | 링크 URL |
| gnbDetails.displayOrder | int | 필수 | 전시 순서 |
| gnbDetails.displayPeriod | DisplayPeriod | 필수 | 전시 기간 |
| gnbDetails.displayYn | Yn | 필수 | 전시 여부 (Y/N) |

### JSON Example

```json
{
  "toUpdateGnbs": [
    {
      "gnbDetails": {
        "title": "홈",
        "linkUrl": "/",
        "displayOrder": 1,
        "displayPeriod": {
          "displayStartDate": "2024-01-01 00:00:00",
          "displayEndDate": "2099-12-31 23:59:59"
        },
        "displayYn": "Y"
      }
    },
    {
      "gnbId": 2,
      "gnbDetails": {
        "title": "카테고리 수정",
        "linkUrl": "/category",
        "displayOrder": 2,
        "displayPeriod": {
          "displayStartDate": "2024-01-01 00:00:00",
          "displayEndDate": "2099-12-31 23:59:59"
        },
        "displayYn": "Y"
      }
    }
  ],
  "deleteGnbIds": [3, 4]
}
```

## 3. Response

### DTO Structure (List\<GnbResponse\>)

| 필드명 | 타입 | 설명 |
|--------|------|------|
| gnbId | long | GNB ID |
| gnbDetails | GnbDetails | GNB 상세 정보 |
| gnbDetails.title | String | GNB 타이틀 |
| gnbDetails.linkUrl | String | 링크 URL |
| gnbDetails.displayOrder | int | 전시 순서 |
| gnbDetails.displayPeriod | DisplayPeriod | 전시 기간 |
| gnbDetails.displayYn | Yn | 전시 여부 |

### JSON Example

```json
{
  "success": true,
  "data": [
    {
      "gnbId": 10,
      "gnbDetails": {
        "title": "홈",
        "linkUrl": "/",
        "displayOrder": 1,
        "displayPeriod": {
          "displayStartDate": "2024-01-01 00:00:00",
          "displayEndDate": "2099-12-31 23:59:59"
        },
        "displayYn": "Y"
      }
    },
    {
      "gnbId": 2,
      "gnbDetails": {
        "title": "카테고리 수정",
        "linkUrl": "/category",
        "displayOrder": 2,
        "displayPeriod": {
          "displayStartDate": "2024-01-01 00:00:00",
          "displayEndDate": "2099-12-31 23:59:59"
        },
        "displayYn": "Y"
      }
    }
  ]
}
```

## 4. 호출 흐름

```
ContentController.enrollGnbs(@RequestBody UpdateGnb)
    └── GnbQueryService.createGnbs(updateGnb)
            └── GnbQueryServiceImpl.createGnbs(updateGnb)
                    ├── [신규 등록] gnbId == null인 CreateGnb 필터링
                    │       ├── GnbMapper.toEntity(createGnb)
                    │       └── GnbRepository.saveAll(gnbs)  [JPA 배치 INSERT]
                    │
                    ├── [수정] gnbId != null인 CreateGnb 필터링 → updateGnbs(toUpdateGnbs)
                    │       ├── GnbFetchService.fetchGnbEntities(gnbIds)
                    │       │       └── GnbFetchRepositoryImpl
                    │       │           SELECT * FROM gnb WHERE id IN (:gnbIds)
                    │       └── gnb.updateGnbDetails(createGnb.getGnbDetails())  [JPA Dirty Checking]
                    │
                    ├── [삭제] deleteGnbIds 처리 → deleteGnbs(deleteGnbIds)
                    │       ├── GnbFetchService.fetchGnbEntities(deleteGnbIds)
                    │       └── gnb.delete()  [소프트 삭제, JPA Dirty Checking]
                    │
                    ├── [Redis 캐시 삭제] 수정 또는 삭제가 있을 경우
                    │       └── GnbRedisQueryService.deleteGnbInRedis()
                    │
                    └── 저장된 Gnb 목록 → GnbMapper.toResponse(gnb)
```

## 5. Database Query

### Tables

| 테이블 | 조작 | 설명 |
|--------|------|------|
| gnb | SELECT | 수정/삭제 대상 GNB 조회 |
| gnb | INSERT (batch) | JPA saveAll로 신규 GNB 등록 |
| gnb | UPDATE | JPA Dirty Checking으로 GNB 수정 |
| gnb | UPDATE | 소프트 삭제 (deleteYn=Y) |

### QueryDSL (GnbFetchRepositoryImpl.fetchGnbEntities)

```java
queryFactory.selectFrom(gnb)
    .where(gnb.id.in(gnbIds))
    .distinct()
    .fetch()
```

### Redis 캐시 삭제 조건

```java
// 수정 또는 삭제가 있는 경우에만 캐시 삭제
if (!toUpdateGnbs.isEmpty() || !updateGnb.getDeleteGnbIds().isEmpty()) {
    deleteGnbInRedis();
}
```

### 비고

- 단일 API로 신규 등록 / 수정 / 삭제를 한 번에 처리
- 신규 GNB는 `gnbId == null`로 구분, 수정 GNB는 `gnbId != null`로 구분
- 삭제는 소프트 삭제 (deleteYn 플래그)
- 수정 또는 삭제가 발생한 경우에만 Redis GNB 캐시 삭제
- 응답은 신규 저장된 GNB + 수정된 GNB 합산 목록 반환
