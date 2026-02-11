# API Flow Documentation: SellerController.fetchSellers

## 📌 기본 정보

| 항목 | 값 |
|------|-----|
| HTTP Method | GET |
| Path | `/api/v1/sellers` |
| Controller | `SellerController` |
| Service | `SellerFetchService` → `SellerFetchServiceImpl` |
| Repository | `SellerFetchRepository` → `SellerFetchRepositoryImpl` |
| Authorization | `HAS_AUTHORITY_MASTER` |

---

## 📥 Request

### Request Parameters

| 파라미터 | 타입 | 필수 | 설명 | Validation |
|----------|------|------|------|------------|
| searchKeyword | SearchKeyword | ❌ | 검색 키워드 타입 | - |
| searchWord | String | ❌ | 검색어 | - |
| siteIds | List\<Long\> | ❌ | 사이트 ID 목록 (필터) | - |
| status | ApprovalStatus | ❌ | 승인 상태 필터 | - |
| page | int | ❌ | 페이지 번호 (0-based) | - |
| size | int | ❌ | 페이지 크기 | - |
| sort | String | ❌ | 정렬 조건 | - |

### Request DTO 구조

```java
// SellerFilter (extends AbstractSearchFilter)
public class SellerFilter extends AbstractSearchFilter {
    private List<Long> siteIds;
    private ApprovalStatus status;      // PENDING, APPROVED, REJECTED
}

// AbstractSearchFilter
public abstract class AbstractSearchFilter implements SearchFilter {
    protected SearchKeyword searchKeyword;  // SELLER_ID, SELLER_NAME 등
    protected String searchWord;
}
```

### SearchKeyword Enum (Seller 관련)

```java
public enum SearchKeyword {
    SELLER_ID("sellerId"),
    SELLER_NAME("sellerName"),
    // ... 기타 키워드
}
```

### Request 예시

```
GET /api/v1/sellers?searchKeyword=SELLER_NAME&searchWord=홍길동&status=APPROVED&siteIds=1,2&page=0&size=20
```

---

## 📤 Response

### Response DTO 구조

```java
// CustomPageable<SellerResponse>
public class CustomPageable<T> implements Page<T> {
    private final List<T> content;
    private final Pageable pageable;
    private final long totalElements;
    private final Long lastDomainId;
}

// SellerResponse
public class SellerResponse {
    private long sellerId;
    private String sellerName;
    private Double commissionRate;
    private ApprovalStatus approvalStatus;  // PENDING, APPROVED, REJECTED
    private String csPhoneNumber;
    private String csEmail;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime insertDate;
}
```

### Response JSON 예시

```json
{
  "data": {
    "content": [
      {
        "sellerId": 1,
        "sellerName": "판매자 A",
        "commissionRate": 10.5,
        "approvalStatus": "APPROVED",
        "csPhoneNumber": "02-1234-5678",
        "csEmail": "seller-a@example.com",
        "insertDate": "2024-01-15 10:30:00"
      },
      {
        "sellerId": 2,
        "sellerName": "판매자 B",
        "commissionRate": 12.0,
        "approvalStatus": "PENDING",
        "csPhoneNumber": "02-9876-5432",
        "csEmail": "seller-b@example.com",
        "insertDate": "2024-01-16 14:20:00"
      }
    ],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 20,
      "sort": { "sorted": false }
    },
    "totalElements": 150,
    "totalPages": 8,
    "first": true,
    "last": false,
    "numberOfElements": 20,
    "lastDomainId": null
  },
  "response": {
    "status": 200,
    "message": "success"
  }
}
```

---

## 🔄 호출 흐름

```
┌──────────────────────────────────────────────┐
│   Controller                                  │  SellerController.fetchSellers(filter, pageable)
│   (REST API)                                  │  @GetMapping("/sellers")
│                                               │  @PreAuthorize(HAS_AUTHORITY_MASTER)
└────────────────────┬─────────────────────────┘
                     │
                     ▼
┌──────────────────────────────────────────────┐
│   Service                                     │  SellerFetchServiceImpl.fetchSellers(filter, pageable)
│   (Business Logic)                            │  @Transactional(readOnly = true)
│                                               │
│   1. sellerFetchRepository.fetchSellers()     │  → 목록 조회
│   2. fetchSellerCountQuery()                  │  → 전체 개수 조회
│   3. sellerPageableMapper.toSellerContext()   │  → CustomPageable 변환
└────────────────────┬─────────────────────────┘
                     │
                     ▼
┌──────────────────────────────────────────────┐
│   Repository                                  │  SellerFetchRepositoryImpl
│   (Data Access)                               │
│                                               │
│   - fetchSellers(): 목록 조회 (페이징)         │
│   - fetchSellerCountQuery(): 전체 개수 조회    │
└────────────────────┬─────────────────────────┘
                     │
                     ▼
┌──────────────────────────────────────────────┐
│   Database                                    │  Tables: seller, seller_business_info,
│                                               │          seller_site_relation
└──────────────────────────────────────────────┘
```

---

## 🗄️ Database Query 분석

### 조회 테이블

| 테이블 | Alias | 조인 유형 | 조인 조건 |
|--------|-------|----------|----------|
| seller | seller | FROM | - |
| seller_business_info | sellerBusinessInfo | INNER JOIN | sellerBusinessInfo.id = seller.id |
| seller_site_relation | sellerSiteRelation | LEFT JOIN | sellerSiteRelation.sellerId = seller.id AND activeYn = 'Y' |

### QueryDSL 코드 - 목록 조회

```java
getQueryFactory().select(
    new QSellerResponse(
        seller.id,
        seller.sellerName,
        seller.commissionRate,
        seller.approvalStatus,
        sellerBusinessInfo.csPhoneNumber,
        sellerBusinessInfo.csEmail,
        seller.insertDate
    )
)
.from(seller)
.innerJoin(sellerBusinessInfo)
    .on(sellerBusinessInfo.id.eq(seller.id))
.leftJoin(sellerSiteRelation)
    .on(sellerSiteRelation.sellerId.eq(seller.id), sellerSiteRelation.activeYn.eq(Yn.Y))
.where(
    searchKeywordEq(filter.getSearchKeyword(), filter.getSearchWord()),
    deleteYn(),                          // delete_yn = 'N'
    approvalStatusEq(filter.getStatus()),
    siteIdIn(filter.getSiteIds())
)
.groupBy(seller.id)
.offset(pageable.getOffset())
.limit(pageable.getPageSize())
.fetch();
```

### QueryDSL 코드 - 전체 개수 조회

```java
getQueryFactory()
    .select(seller.id.countDistinct())
    .from(seller)
    .innerJoin(sellerBusinessInfo)
        .on(sellerBusinessInfo.id.eq(seller.id))
    .leftJoin(sellerSiteRelation)
        .on(sellerSiteRelation.sellerId.eq(seller.id), sellerSiteRelation.activeYn.eq(Yn.Y))
    .where(
        searchKeywordEq(filter.getSearchKeyword(), filter.getSearchWord()),
        deleteYn(),
        approvalStatusEq(filter.getStatus()),
        siteIdIn(filter.getSiteIds())
    );
```

### WHERE 조건

| 조건 | 필드 | 설명 | 적용 시점 |
|------|------|------|----------|
| searchKeywordEq | 동적 필드 | 검색 키워드 기반 검색 | searchKeyword != null |
| deleteYn | seller.deleteYn | 삭제되지 않은 데이터만 | 항상 |
| approvalStatusEq | seller.approvalStatus | 승인 상태 필터 | status != null |
| siteIdIn | sellerSiteRelation.siteId | 사이트 필터 | siteIds != null && !empty |

### BooleanExpression 메서드

```java
private BooleanExpression deleteYn() {
    return QueryDslUtil.notDeleted(seller.deleteYn, Yn.N);
}

private BooleanExpression approvalStatusEq(ApprovalStatus status) {
    return status != null ? seller.approvalStatus.eq(status) : null;
}

private BooleanExpression siteIdIn(List<Long> siteIds) {
    return (siteIds != null && !siteIds.isEmpty())
        ? sellerSiteRelation.siteId.in(siteIds)
        : null;
}
```

---

## 📋 관련 Entity

### Seller (seller 테이블)
| 컬럼 | 타입 | 설명 |
|------|------|------|
| seller_id | long | PK |
| seller_name | String | 판매자명 |
| commission_rate | double | 수수료율 |
| approval_status | ApprovalStatus | 승인 상태 |
| delete_yn | Yn | 삭제 여부 |
| insert_date | LocalDateTime | 등록일시 |

### SellerBusinessInfo (seller_business_info 테이블)
| 컬럼 | 타입 | 설명 |
|------|------|------|
| seller_id | long | PK (seller FK) |
| cs_phone_number | String | 고객센터 전화번호 |
| cs_email | String | 고객센터 이메일 |

### SellerSiteRelation (seller_site_relation 테이블)
| 컬럼 | 타입 | 설명 |
|------|------|------|
| seller_site_relation_id | long | PK |
| seller_id | long | 판매자 ID |
| site_id | long | 사이트 ID |
| active_yn | Yn | 활성화 여부 |

---

## 📋 관련 Enum

### ApprovalStatus
```java
public enum ApprovalStatus {
    PENDING,    // 승인 대기
    APPROVED,   // 승인됨
    REJECTED    // 거부됨
}
```

### SearchKeyword (Seller 관련)
```java
SELLER_ID("sellerId"),
SELLER_NAME("sellerName")
```

---

## ⚠️ 특이사항

1. **GROUP BY 사용**: `seller_site_relation`과 LEFT JOIN 후 `groupBy(seller.id)` 사용하여 중복 제거
2. **COUNT DISTINCT**: 전체 개수 조회 시 `countDistinct()` 사용
3. **동적 검색 조건**: `SearchConditionStrategy`를 통한 동적 검색 키워드 처리
