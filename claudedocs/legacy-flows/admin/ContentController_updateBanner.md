# API Flow: ContentController.updateBanner

## 1. 기본 정보

| 항목 | 값 |
|------|-----|
| HTTP Method | PUT |
| Path | `/api/v1/content/banner/{bannerId}` |
| Controller | `ContentController` |
| Service | `BannerQueryService` → `BannerQueryServiceImpl` |
| Repository | `BannerFindRepository` (조회), `BannerRepository` (JPA - Dirty Checking) |
| 캐시 | Redis 캐시 삭제 (`BannerRedisQueryService`) |
| 권한 | `@PreAuthorize(HAS_AUTHORITY_MASTER)` |

## 2. Request

### Parameters

| 이름 | 타입 | 위치 | 필수 | 설명 |
|------|------|------|------|------|
| bannerId | long | PathVariable | 필수 | 수정할 배너 ID |

### Request Body (CreateBanner)

| 필드명 | 타입 | 필수 | Validation | 설명 |
|--------|------|------|------------|------|
| title | String | 필수 | @NotBlank, @Length(max=50) | 배너 타이틀 (최대 50자) |
| bannerType | BannerType | 필수 | @NotNull | 배너 타입 |
| displayPeriod | DisplayPeriod | 필수 | @NotNull | 전시 기간 |
| displayPeriod.displayStartDate | LocalDateTime | 필수 | @NotNull | 전시 시작일시 |
| displayPeriod.displayEndDate | LocalDateTime | 필수 | @NotNull | 전시 종료일시 |
| displayYn | Yn | 필수 | @NotNull | 전시 여부 (Y/N) |

### JSON Example

```
PUT /api/v1/content/banner/1
```

```json
{
  "title": "메인 배너 수정",
  "bannerType": "MAIN",
  "displayPeriod": {
    "displayStartDate": "2024-02-01 00:00:00",
    "displayEndDate": "2024-12-31 23:59:59"
  },
  "displayYn": "Y"
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
| displayYn | Yn | 전시 여부 (Y/N) |
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
    "title": "메인 배너 수정",
    "bannerType": "MAIN",
    "displayPeriod": {
      "displayStartDate": "2024-02-01 00:00:00",
      "displayEndDate": "2024-12-31 23:59:59"
    },
    "displayYn": "Y",
    "insertOperator": "admin",
    "updateOperator": "admin",
    "insertDate": "2024-01-01 10:00:00",
    "updateDate": "2024-02-01 09:00:00"
  }
}
```

## 4. 호출 흐름

```
ContentController.updateBanner(@PathVariable long bannerId, @RequestBody @Validated CreateBanner)
    └── BannerQueryService.updateBanner(bannerId, createBanner)
            └── BannerQueryServiceImpl.updateBanner(bannerId, createBanner)
                    ├── BannerFetchService.fetchBannerEntity(bannerId)
                    │       └── BannerFindRepositoryImpl
                    │           SELECT * FROM banner WHERE id = bannerId
                    ├── banner.update(createBanner)  [JPA Dirty Checking]
                    ├── deleteBannerInRedis(banner.getBannerType())
                    │       └── BannerRedisQueryService.deleteBannerItemsInRedis(bannerType)
                    └── BannerMapper.toResponse(banner)
```

## 5. Database Query

### Tables

| 테이블 | 조작 | 설명 |
|--------|------|------|
| banner | SELECT | 배너 조회 (존재 확인) |
| banner | UPDATE | JPA Dirty Checking으로 자동 UPDATE |

### QueryDSL (BannerFindRepositoryImpl.fetchBannerEntity)

```java
getQueryFactory().selectFrom(banner)
    .where(banner.id.eq(bannerId))
    .fetchOne()
```

### 캐시 삭제 흐름

```java
// bannerType에 해당하는 Redis 키 삭제
bannerRedisQueryService.deleteBannerItemsInRedis(banner.getBannerType());
```

### 비고

- Entity 조회 후 `banner.update(createBanner)` 호출 → JPA Dirty Checking으로 UPDATE 자동 실행
- 수정 후 해당 BannerType의 Redis 캐시를 삭제하여 캐시 정합성 유지
