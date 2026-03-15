# API Flow: ContentController.enrollBannerItems

## 1. 기본 정보

| 항목 | 값 |
|------|-----|
| HTTP Method | POST |
| Path | `/api/v1/content/banner/items` |
| Controller | `ContentController` |
| Service | `BannerQueryService` → `BannerQueryServiceImpl` |
| Repository | `BannerItemJdbcRepository` (JDBC 배치), `BannerItemFindRepository` (조회) |
| 캐시 | Redis 캐시 삭제 (`BannerRedisQueryService`) |
| 권한 | `@PreAuthorize(HAS_AUTHORITY_MASTER)` |

## 2. Request

### Request Body (List\<CreateBannerItem\>)

| 필드명 | 타입 | 필수 | Validation | 설명 |
|--------|------|------|------------|------|
| bannerId | long | 필수 | @NotNull | 배너 ID |
| bannerItemId | Long | 선택 | @JsonInclude(NON_NULL) | null이면 신규, 값 있으면 수정 |
| title | String | 필수 | @NotBlank, @Length(max=50) | 배너 아이템 타이틀 (최대 50자) |
| imageUrl | String | 필수 | @NotBlank | 이미지 URL |
| linkUrl | String | 선택 | - | 링크 URL |
| displayPeriod | DisplayPeriod | 필수 | @NotNull | 전시 기간 |
| displayPeriod.displayStartDate | LocalDateTime | 필수 | @NotNull | 전시 시작일시 |
| displayPeriod.displayEndDate | LocalDateTime | 필수 | @NotNull | 전시 종료일시 |
| displayOrder | int | 필수 | @Max(10), @Min(1) | 전시 순서 (1~10) |
| displayYn | Yn | 필수 | @NotNull | 전시 여부 (Y/N) |
| width | Double | 선택 | - | 이미지 너비 (null이면 0.0) |
| height | Double | 선택 | - | 이미지 높이 (null이면 0.0) |

### JSON Example

```json
[
  {
    "bannerId": 1,
    "title": "배너 아이템 1",
    "imageUrl": "https://cdn.example.com/banner1.jpg",
    "linkUrl": "https://example.com/event/1",
    "displayPeriod": {
      "displayStartDate": "2024-01-01 00:00:00",
      "displayEndDate": "2024-12-31 23:59:59"
    },
    "displayOrder": 1,
    "displayYn": "Y",
    "width": 1920.0,
    "height": 600.0
  },
  {
    "bannerId": 1,
    "bannerItemId": 5,
    "title": "배너 아이템 2 수정",
    "imageUrl": "https://cdn.example.com/banner2.jpg",
    "displayPeriod": {
      "displayStartDate": "2024-01-01 00:00:00",
      "displayEndDate": "2024-12-31 23:59:59"
    },
    "displayOrder": 2,
    "displayYn": "Y"
  }
]
```

## 3. Response

### DTO Structure (List\<CreateBannerItem\>)

요청으로 받은 `List<CreateBannerItem>`을 그대로 반환합니다.

| 필드명 | 타입 | 설명 |
|--------|------|------|
| bannerId | long | 배너 ID |
| bannerItemId | Long | 배너 아이템 ID (신규 시 null) |
| title | String | 타이틀 |
| imageUrl | String | 이미지 URL |
| linkUrl | String | 링크 URL |
| displayPeriod | DisplayPeriod | 전시 기간 |
| displayOrder | int | 전시 순서 |
| displayYn | Yn | 전시 여부 |
| width | Double | 이미지 너비 |
| height | Double | 이미지 높이 |

### JSON Example

```json
{
  "success": true,
  "data": [
    {
      "bannerId": 1,
      "title": "배너 아이템 1",
      "imageUrl": "https://cdn.example.com/banner1.jpg",
      "linkUrl": "https://example.com/event/1",
      "displayPeriod": {
        "displayStartDate": "2024-01-01 00:00:00",
        "displayEndDate": "2024-12-31 23:59:59"
      },
      "displayOrder": 1,
      "displayYn": "Y",
      "width": 1920.0,
      "height": 600.0
    }
  ]
}
```

## 4. 호출 흐름

```
ContentController.enrollBannerItems(@RequestBody @Validated List<CreateBannerItem>)
    └── BannerQueryService.enrollBannerItems(createBannerItems)
            └── BannerQueryServiceImpl.enrollBannerItems(createBannerItems)
                    ├── [수정 처리] updateBannerItems(createBannerItems)
                    │       ├── bannerId = createBannerItems.get(0).getBannerId()
                    │       ├── BannerFetchService.fetchBannerItemEntitiesByBannerId(bannerId)
                    │       │       └── BannerItemFindRepository.fetchBannerItemEntitiesByBannerId(bannerId)
                    │       │           SELECT * FROM banner_item WHERE bannerId = :bannerId AND deleteYn = N
                    │       ├── 기존 아이템과 요청 아이템 매핑 후 update()/delete() 호출 [JPA Dirty Checking]
                    │       └── deleteBannerItemsInRedis(bannerId)
                    │               ├── BannerFetchService.fetchBannerEntity(bannerId)
                    │               └── BannerRedisQueryService.deleteBannerItemsInRedis(bannerType)
                    ├── [신규 처리] bannerItemId == null인 항목 필터링
                    ├── BannerMapper.toBannerItems(newBanners)
                    └── BannerItemJdbcRepository.saveAll(bannerItems)  [JDBC 배치 INSERT]
```

## 5. Database Query

### Tables

| 테이블 | 조작 | 설명 |
|--------|------|------|
| banner_item | SELECT | 기존 아이템 조회 (bannerId 기준) |
| banner | SELECT | 배너 조회 (Redis 캐시 삭제용) |
| banner_item | UPDATE | JPA Dirty Checking으로 기존 아이템 수정 |
| banner_item | DELETE (soft) | deleteYn 플래그 처리 |
| banner_item | INSERT (batch) | JDBC 배치로 신규 아이템 삽입 |

### QueryDSL (BannerItemFindRepositoryImpl.fetchBannerItemEntitiesByBannerId)

```java
queryFactory.selectFrom(bannerItem)
    .where(
        bannerItem.bannerId.eq(bannerId),
        bannerItem.deleteYn.eq(Yn.N)
    )
    .fetch()
```

### 아이템 처리 로직

```java
// bannerItemId > 0인 것만 수정 맵에 포함
Map<Long, BannerItem> bannerItemIdMap = bannerItems.stream()
    .filter(bannerItem -> bannerItem.getId() > 0)
    .collect(Collectors.toMap(BannerItem::getId, Function.identity()));

// 기존 아이템 순회 - 수정 또는 소프트 삭제
findBannerItems.forEach(bannerItem -> {
    BannerItem updateBannerItem = bannerItemIdMap.getOrDefault(bannerItem.getId(), null);
    if (updateBannerItem != null) {
        bannerItem.update(updateBannerItem);     // Dirty Checking
    } else {
        bannerItem.delete();                      // 소프트 삭제
    }
});

// 신규 아이템 JDBC 배치 INSERT
bannerItemJdbcRepository.saveAll(bannerItems);
```

### 비고

- 요청 리스트의 첫 번째 원소에서 bannerId를 추출하여 기준으로 사용
- 기존 아이템 중 요청에 없는 아이템은 소프트 삭제 처리
- 신규 아이템은 JDBC 배치로 효율적 INSERT
- 처리 후 BannerType 기준으로 Redis 캐시 삭제
