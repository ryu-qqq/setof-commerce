# API Flow Documentation: SellerController.fetchSeller(sellerId)

## 📌 기본 정보

| 항목 | 값 |
|------|-----|
| HTTP Method | GET |
| Path | `/api/v1/seller/{sellerId}` |
| Controller | `SellerController` |
| Service | `SellerFetchService` → `SellerFetchServiceImpl` |
| Repository | `SellerFetchRepository` → `SellerFetchRepositoryImpl` |
| Authorization | `HAS_AUTHORITY_MASTER` |

---

## 📥 Request

### Request Parameters

| 파라미터 | 타입 | 필수 | 설명 | Validation |
|----------|------|------|------|------------|
| sellerId | long | ✅ | 판매자 ID | @PathVariable |

### Request 예시

```
GET /api/v1/seller/123
```

---

## 📤 Response

### Response DTO 구조

```java
public class SellerDetailResponse {
    private long sellerId;
    private String sellerName;
    private String logoUrl;
    private Double commissionRate;
    private ApprovalStatus approvalStatus;      // PENDING, APPROVED, REJECTED
    private String sellerDescription;

    // 사업자 주소
    private String businessAddressLine1;
    private String businessAddressLine2;
    private String businessAddressZipCode;

    // 반품 주소
    private String returnAddressLine1;
    private String returnAddressLine2;
    private String returnAddressZipCode;

    // 고객센터 정보
    private String csPhoneNumber;
    private String csNumber;
    private String csEmail;

    // 사업자 정보
    private String registrationNumber;          // 사업자등록번호
    private String saleReportNumber;            // 통신판매업신고번호
    private String representative;              // 대표자명

    // 정산 계좌 정보
    private String bankName;
    private String accountNumber;
    private String accountHolderName;

    // 입점 사이트 정보
    private List<SiteResponse> sites;
}

public class SiteResponse {
    private long siteId;
    private String siteName;
}
```

### Response JSON 예시

```json
{
  "data": {
    "sellerId": 123,
    "sellerName": "판매자 A",
    "logoUrl": "https://example.com/logo.png",
    "commissionRate": 10.5,
    "approvalStatus": "APPROVED",
    "sellerDescription": "판매자 설명",
    "businessAddressLine1": "서울시 강남구",
    "businessAddressLine2": "테헤란로 123",
    "businessAddressZipCode": "06234",
    "returnAddressLine1": "서울시 강남구",
    "returnAddressLine2": "반품주소 456",
    "returnAddressZipCode": "06235",
    "csPhoneNumber": "02-1234-5678",
    "csNumber": "1588-1234",
    "csEmail": "cs@example.com",
    "registrationNumber": "123-45-67890",
    "saleReportNumber": "2024-서울강남-12345",
    "representative": "홍길동",
    "bankName": "신한은행",
    "accountNumber": "110-123-456789",
    "accountHolderName": "홍길동",
    "sites": [
      {
        "siteId": 1,
        "siteName": "메인몰"
      },
      {
        "siteId": 2,
        "siteName": "서브몰"
      }
    ]
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
│   Controller                                  │  SellerController.fetchSeller(sellerId)
│   (REST API)                                  │  @GetMapping("/seller/{sellerId}")
│                                               │  @PreAuthorize(HAS_AUTHORITY_MASTER)
└────────────────────┬─────────────────────────┘
                     │
                     ▼
┌──────────────────────────────────────────────┐
│   Service                                     │  SellerFetchServiceImpl.fetchSellerDetail(sellerId)
│   (Business Logic)                            │  @Transactional(readOnly = true)
│                                               │
│   - sellerFetchRepository.fetchSellerDetail() │
│   - sellerDetailResponse.setFilteredSites()   │  (siteId != 0 필터링)
└────────────────────┬─────────────────────────┘
                     │
                     ▼
┌──────────────────────────────────────────────┐
│   Repository                                  │  SellerFetchRepositoryImpl.fetchSellerDetail(sellerId)
│   (Data Access)                               │  QueryDSL + GroupBy Transform
└────────────────────┬─────────────────────────┘
                     │
                     ▼
┌──────────────────────────────────────────────┐
│   Database                                    │  Tables: seller, seller_business_info,
│                                               │          seller_shipping_info, seller_site_relation, site
└──────────────────────────────────────────────┘
```

---

## 🗄️ Database Query 분석

### 조회 테이블

| 테이블 | Alias | 조인 유형 | 조인 조건 |
|--------|-------|----------|----------|
| seller | seller | FROM | - |
| seller_business_info | sellerBusinessInfo | INNER JOIN | sellerBusinessInfo.id = seller.id |
| seller_shipping_info | sellerShippingInfo | INNER JOIN | sellerShippingInfo.id = seller.id |
| seller_site_relation | sellerSiteRelation | LEFT JOIN | sellerSiteRelation.sellerId = seller.id AND activeYn = 'Y' |
| site | site | LEFT JOIN | site.id = sellerSiteRelation.siteId |

### QueryDSL 코드

```java
getQueryFactory()
    .from(seller)
    .innerJoin(sellerBusinessInfo)
        .on(sellerBusinessInfo.id.eq(seller.id))
    .innerJoin(sellerShippingInfo)
        .on(sellerShippingInfo.id.eq(seller.id))
    .leftJoin(sellerSiteRelation)
        .on(sellerSiteRelation.sellerId.eq(seller.id), relationActiveYn(Yn.Y))
    .leftJoin(site)
        .on(site.id.eq(sellerSiteRelation.siteId))
    .where(seller.id.eq(sellerId))
    .transform(
        GroupBy.groupBy(seller.id).as(
            new QSellerDetailResponse(
                seller.id,
                seller.sellerName,
                seller.sellerLogoUrl,
                seller.commissionRate,
                seller.approvalStatus,
                seller.sellerDescription,
                sellerBusinessInfo.businessAddressLine1,
                sellerBusinessInfo.businessAddressLine2,
                sellerBusinessInfo.businessAddressZipCode,
                sellerShippingInfo.returnAddressLine1,
                sellerShippingInfo.returnAddressLine2,
                sellerShippingInfo.returnAddressZipCode,
                sellerBusinessInfo.csPhoneNumber,
                sellerBusinessInfo.csNumber,
                sellerBusinessInfo.csEmail,
                sellerBusinessInfo.registrationNumber,
                sellerBusinessInfo.saleReportNumber,
                sellerBusinessInfo.representative,
                sellerBusinessInfo.bankName,
                sellerBusinessInfo.accountNumber,
                sellerBusinessInfo.accountHolderName,
                GroupBy.list(
                    new QSiteResponse(
                        site.id,
                        site.siteName
                    )
                )
            )
        )
    )
    .get(sellerId)
```

### WHERE 조건

| 조건 | 필드 | 설명 |
|------|------|------|
| sellerIdEq | seller.id | 판매자 ID 일치 |
| relationActiveYn | sellerSiteRelation.activeYn | 활성화된 사이트 관계만 조회 |

---

## 📋 관련 Entity

### Seller (seller 테이블)
| 컬럼 | 타입 | 설명 |
|------|------|------|
| seller_id | long | PK |
| seller_name | String | 판매자명 |
| seller_logo_url | String | 로고 URL |
| seller_description | String | 판매자 설명 |
| commission_rate | double | 수수료율 |
| approval_status | ApprovalStatus | 승인 상태 |
| delete_yn | Yn | 삭제 여부 |

### SellerBusinessInfo (seller_business_info 테이블)
| 컬럼 | 타입 | 설명 |
|------|------|------|
| seller_id | long | PK (seller FK) |
| registration_number | String | 사업자등록번호 |
| company_name | String | 회사명 |
| business_address_line1 | String | 사업장 주소1 |
| business_address_line2 | String | 사업장 주소2 |
| business_address_zip_code | String | 사업장 우편번호 |
| bank_name | String | 은행명 |
| account_number | String | 계좌번호 |
| account_holder_name | String | 예금주 |
| cs_number | String | 고객센터 대표번호 |
| cs_phone_number | String | 고객센터 전화번호 |
| cs_email | String | 고객센터 이메일 |
| sale_report_number | String | 통신판매업신고번호 |
| representative | String | 대표자명 |

### SellerShippingInfo (seller_shipping_info 테이블)
| 컬럼 | 타입 | 설명 |
|------|------|------|
| seller_id | long | PK (seller FK) |
| return_address_line1 | String | 반품 주소1 |
| return_address_line2 | String | 반품 주소2 |
| return_address_zip_code | String | 반품 우편번호 |

### SellerSiteRelation (seller_site_relation 테이블)
| 컬럼 | 타입 | 설명 |
|------|------|------|
| seller_site_relation_id | long | PK |
| seller_id | long | 판매자 ID |
| site_id | long | 사이트 ID |
| site_type | SiteType | 사이트 유형 |
| active_yn | Yn | 활성화 여부 |

### Site (site 테이블)
| 컬럼 | 타입 | 설명 |
|------|------|------|
| site_id | long | PK |
| site_name | String | 사이트명 |
| description | String | 설명 |
| base_url | String | 기본 URL |
