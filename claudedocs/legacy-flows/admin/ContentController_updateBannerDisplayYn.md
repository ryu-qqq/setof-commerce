# API Flow: ContentController.updateBannerDisplayYn

## 1. 기본 정보

| 항목 | 값 |
|------|-----|
| HTTP Method | PATCH |
| Path | `/api/v1/content/banner/{bannerId}/display-status` |
| Controller | `ContentController` |
| Service | `BannerQueryService` → `BannerQueryServiceImpl` |
| Repository | `BannerFindRepository` (조회), JPA Dirty Checking (수정) |
| 캐시 | Redis 캐시 삭제 (`BannerRedisQueryService`) |
| 권한 | `@PreAuthorize(HAS_AUTHORITY_MASTER)` |

## 2. Request

### Parameters

| 이름 | 타입 | 위치 | 필수 | 설명 |
|------|------|------|------|------|
| bannerId | long | PathVariable | 필수 | 수정할 배너 ID |

### Request Body (UpdateBannerDisplayYn)

| 필드명 | 타입 | 필수 | Validation | 설명 |
|--------|------|------|------------|------|
| displayYn | Yn | 필수 | - | 변경할 전시 여부 (Y/N) |

### JSON Example

```
PATCH /api/v1/content/banner/1/display-status
```

```json
{
  "displayYn": "N"
}
```

## 3. Response

### DTO Structure (BannerResponse)

| 필드명 | 타입 | 설명 |
|--------|------|------|
| bannerId | long | 배너 ID |
| title | String | 배너 타이틀 |
| bannerType | BannerType | 배너 타입 |
| displayPeriod | DisplayPeriod | 전시 기간 |
| displayYn | Yn | 변경된 전시 여부 (Y/N) |
| insertOperator | String | 등록자 |
| updateOperator | String | 수정자 |
| insertDate | LocalDateTime | 등록일시 (yyyy-MM-dd HH:mm:ss) |
| updateDate | LocalDateTime | 수정일시 (yyyy-MM-dd HH:mm:ss) |

### JSON Example

```json
{
  "success": true,
  "data": {
    "bannerId": 1,
    "title": "메인 배너",
    "bannerType": "MAIN",
    "displayPeriod": {
      "displayStartDate": "2024-01-01 00:00:00",
      "displayEndDate": "2024-12-31 23:59:59"
    },
    "displayYn": "N",
    "insertOperator": "admin",
    "updateOperator": "admin",
    "insertDate": "2024-01-01 10:00:00",
    "updateDate": "2024-06-01 09:00:00"
  }
}
```

## 4. 호출 흐름

```
ContentController.updateBannerDisplayYn(@PathVariable long bannerId, @RequestBody UpdateBannerDisplayYn)
    └── BannerQueryService.updateDisplayYn(bannerId, updateBannerDisplayYn)
            └── BannerQueryServiceImpl.updateDisplayYn(bannerId, updateBannerDisplayYn)
                    ├── BannerFetchService.fetchBannerEntity(bannerId)
                    │       └── BannerFindRepositoryImpl
                    │           SELECT * FROM banner WHERE id = bannerId
                    ├── banner.updateDisplayYn(updateBannerDisplayYn.getDisplayYn())  [JPA Dirty Checking]
                    ├── deleteBannerInRedis(banner.getBannerType())
                    │       └── BannerRedisQueryService.deleteBannerItemsInRedis(bannerType)
                    └── BannerMapper.toResponse(banner)
```

## 5. Database Query

### Tables

| 테이블 | 조작 | 설명 |
|--------|------|------|
| banner | SELECT | 배너 조회 |
| banner | UPDATE | displayYn 변경 (JPA Dirty Checking) |

### QueryDSL (BannerFindRepositoryImpl.fetchBannerEntity)

```java
getQueryFactory().selectFrom(banner)
    .where(banner.id.eq(bannerId))
    .fetchOne()
```

### 비고

- `banner.updateDisplayYn(Yn)` 호출 → JPA Dirty Checking으로 UPDATE 자동 실행
- 변경 후 해당 BannerType의 Redis 캐시 삭제로 캐시 정합성 유지
- displayYn 단건 변경 전용 경량 PATCH API
