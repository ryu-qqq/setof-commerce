# API Flow: ContentController.enrollBanner

## 1. 기본 정보

| 항목 | 값 |
|------|-----|
| HTTP Method | POST |
| Path | `/api/v1/content/banner` |
| Controller | `ContentController` |
| Service | `BannerQueryService` → `BannerQueryServiceImpl` |
| Repository | `BannerRepository` (JPA) |
| 권한 | `@PreAuthorize(HAS_AUTHORITY_MASTER)` |

## 2. Request

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

```json
{
  "title": "메인 배너",
  "bannerType": "MAIN",
  "displayPeriod": {
    "displayStartDate": "2024-01-01 00:00:00",
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
    "title": "메인 배너",
    "bannerType": "MAIN",
    "displayPeriod": {
      "displayStartDate": "2024-01-01 00:00:00",
      "displayEndDate": "2024-12-31 23:59:59"
    },
    "displayYn": "Y",
    "insertOperator": "admin",
    "updateOperator": null,
    "insertDate": "2024-01-01 10:00:00",
    "updateDate": null
  }
}
```

## 4. 호출 흐름

```
ContentController.enrollBanner(@RequestBody @Validated CreateBanner)
    └── BannerQueryService.enrollBanner(createBanner)
            └── BannerQueryServiceImpl.enrollBanner(createBanner)
                    ├── BannerMapper.toEntity(createBanner)
                    ├── BannerRepository.save(banner)  [JPA]
                    └── BannerMapper.toResponse(banner)
```

## 5. Database Query

### Tables

| 테이블 | 조작 | 설명 |
|--------|------|------|
| banner | INSERT | 배너 신규 등록 |

### Repository 패턴

```java
// JPA save
Banner banner = bannerMapper.toEntity(createBanner);
bannerRepository.save(banner);
return bannerMapper.toResponse(banner);
```

### 비고

- 배너 아이템은 별도 API(`POST /api/v1/content/banner/items`)로 등록
- 배너 등록 시 Redis 캐시 삭제 없음 (enrollBanner 단계는 캐시 미관여)
