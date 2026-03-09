# 엔드포인트 목록: Discount 모듈 (Admin)

> Source: bootstrap-legacy-web-api-admin
> Controller: `DiscountController`
> Base Path: `/api/v1`
> 권한: `@PreAuthorize(HAS_AUTHORITY_MASTER)`

---

## 요약

| 분류 | 개수 |
|------|------|
| Query (조회) | 5개 |
| Command (명령) | 6개 |
| **총계** | **11개** |

---

## Query (조회성) - 5개

| # | Controller | Method | HTTP | Path | Request Type |
|---|------------|--------|------|------|--------------|
| 1 | DiscountController | fetchDiscountPolicy | GET | `/api/v1/discount/{discountPolicyId}` | @PathVariable |
| 2 | DiscountController | getDiscountPolies | GET | `/api/v1/discounts` | @ModelAttribute + Pageable |
| 3 | DiscountController | getDiscountTargets | GET | `/api/v1/discount/{discountPolicyId}/targets` | @PathVariable + @RequestParam + Pageable |
| 4 | DiscountController | getDiscountHistories | GET | `/api/v1/discounts/history` | @ModelAttribute + Pageable |
| 5 | DiscountController | getDiscountUseHistories | GET | `/api/v1/discount/history/{discountPolicyId}/use` | @PathVariable + @ModelAttribute + Pageable |

### 상세 목록

#### 1. DiscountController.fetchDiscountPolicy
- **HTTP**: `GET /api/v1/discount/{discountPolicyId}`
- **Request**: `@PathVariable("discountPolicyId") long discountPolicyId`
- **Response**: `ApiResponse<DiscountPolicyResponseDto>`
- **Service**: `DiscountFetchService.fetchDiscountPolicy(discountPolicyId)`
- **설명**: 할인 정책 단건 상세 조회

#### 2. DiscountController.getDiscountPolies
- **HTTP**: `GET /api/v1/discounts`
- **Request**: `@ModelAttribute @Validated DiscountFilter` + `Pageable`
- **Response**: `ApiResponse<CustomPageable<DiscountPolicyResponseDto>>`
- **Service**: `DiscountFetchService.fetchDiscountPolicies(filter, pageable)`
- **설명**: 할인 정책 목록 조회 (필터링, 페이징)

#### 3. DiscountController.getDiscountTargets
- **HTTP**: `GET /api/v1/discount/{discountPolicyId}/targets`
- **Request**: `@PathVariable("discountPolicyId") long discountPolicyId` + `@RequestParam IssueType issueType` + `Pageable`
- **Response**: `ApiResponse<Page<? extends DiscountTargetResponseDto>>`
- **Service**: `DiscountTargetFetchService.fetchDiscountTargets(discountPolicyId, issueType, pageable)`
- **설명**: 할인 정책의 대상(상품/셀러) 목록 조회

#### 4. DiscountController.getDiscountHistories
- **HTTP**: `GET /api/v1/discounts/history`
- **Request**: `@ModelAttribute @Validated DiscountFilter` + `Pageable`
- **Response**: `ApiResponse<Page<DiscountPolicyResponseDto>>`
- **Service**: `DiscountHistoryFetchService.fetchDiscountPolicyHistories(discountFilter, pageable)`
- **설명**: 할인 정책 이력 목록 조회

#### 5. DiscountController.getDiscountUseHistories
- **HTTP**: `GET /api/v1/discount/history/{discountPolicyId}/use`
- **Request**: `@PathVariable("discountPolicyId") long discountPolicyId` + `@ModelAttribute DiscountFilter` + `Pageable`
- **Response**: `ApiResponse<Page<DiscountUseHistoryDto>>`
- **Service**: `DiscountHistoryFetchService.fetchDiscountUseHistories(discountPolicyId, discountFilterDto, pageable)`
- **설명**: 특정 할인 정책의 사용 이력 조회

---

## Command (커맨드성) - 6개

| # | Controller | Method | HTTP | Path | Request Type |
|---|------------|--------|------|------|--------------|
| 1 | DiscountController | createDiscountPolicy | POST | `/api/v1/discount` | @RequestBody |
| 2 | DiscountController | updateDiscountPolicy | PUT | `/api/v1/discount/{discountPolicyId}` | @PathVariable + @RequestBody |
| 3 | DiscountController | updateDiscountPolicyUsageStatus | PATCH | `/api/v1/discounts` | @RequestBody |
| 4 | DiscountController | createDiscountsFromExcelUpload | POST | `/api/v1/discounts/excel` | @RequestBody |
| 5 | DiscountController | createDiscountTargets | POST | `/api/v1/discount/{discountPolicyId}/targets` | @PathVariable + @RequestBody |
| 6 | DiscountController | updateDiscountTargets | PUT | `/api/v1/discount/{discountPolicyId}/targets` | @PathVariable + @RequestBody |

### 상세 목록

#### 1. DiscountController.createDiscountPolicy
- **HTTP**: `POST /api/v1/discount`
- **Request**: `@Valid @RequestBody CreateDiscount`
- **Response**: `ApiResponse<DiscountPolicyResponseDto>`
- **Service**: `DiscountQueryService.createDiscount(createDiscount)`
- **설명**: 할인 정책 생성

#### 2. DiscountController.updateDiscountPolicy
- **HTTP**: `PUT /api/v1/discount/{discountPolicyId}`
- **Request**: `@PathVariable("discountPolicyId") long discountPolicyId` + `@Valid @RequestBody UpdateDiscount`
- **Response**: `ApiResponse<DiscountPolicyResponseDto>`
- **Service**: `DiscountQueryService.updateDiscount(discountPolicyId, updateDiscount)`
- **설명**: 할인 정책 수정

#### 3. DiscountController.updateDiscountPolicyUsageStatus
- **HTTP**: `PATCH /api/v1/discounts`
- **Request**: `@RequestBody UpdateUseDiscount`
- **Response**: `ApiResponse<List<DiscountPolicyResponseDto>>`
- **Service**: `DiscountQueryService.updateDiscountUseYn(updateUseDiscount)`
- **설명**: 할인 정책 사용 상태 일괄 변경 (활성화/비활성화)

#### 4. DiscountController.createDiscountsFromExcelUpload
- **HTTP**: `POST /api/v1/discounts/excel`
- **Request**: `@Valid @RequestBody List<CreateDiscountFromExcel>`
- **Response**: `ApiResponse<List<DiscountPolicyResponseDto>>`
- **Service**: `DiscountQueryService.createDiscountFromExcel(createDiscountFromExcels)`
- **설명**: 엑셀 업로드를 통한 할인 정책 일괄 생성

#### 5. DiscountController.createDiscountTargets
- **HTTP**: `POST /api/v1/discount/{discountPolicyId}/targets`
- **Request**: `@PathVariable("discountPolicyId") long discountPolicyId` + `@Valid @RequestBody CreateDiscountTarget`
- **Response**: `ApiResponse<List<DiscountTarget>>`
- **Service**: `DiscountTargetQueryService.createDiscountTargets(discountPolicyId, createDiscountTarget)`
- **설명**: 할인 정책에 대상(상품/셀러) 추가

#### 6. DiscountController.updateDiscountTargets
- **HTTP**: `PUT /api/v1/discount/{discountPolicyId}/targets`
- **Request**: `@PathVariable("discountPolicyId") long discountPolicyId` + `@Valid @RequestBody CreateDiscountTarget`
- **Response**: `ApiResponse<List<DiscountTarget>>`
- **Service**: `DiscountTargetQueryService.updateDiscountTargets(discountPolicyId, createDiscountTarget)`
- **설명**: 할인 정책의 대상(상품/셀러) 수정 (교체)

---

## 보안 설정

```java
@PreAuthorize(HAS_AUTHORITY_MASTER)  // MASTER 권한 필요
@RestController
@RequestMapping(BASE_END_POINT_V1)   // /api/v1
```

---

## 관련 Service 클래스

| Service | 역할 |
|---------|------|
| `DiscountQueryService` | 할인 정책 생성/수정/상태변경 (Command) |
| `DiscountTargetQueryService` | 할인 대상 생성/수정 (Command) |
| `DiscountFetchService` | 할인 정책 조회 (Query) |
| `DiscountTargetFetchService` | 할인 대상 조회 (Query) |
| `DiscountHistoryFetchService` | 할인 이력/사용이력 조회 (Query) |

---

## 다음 단계

각 엔드포인트 상세 분석:
```bash
/legacy-flow admin:DiscountController.fetchDiscountPolicy
/legacy-flow admin:DiscountController.getDiscountPolies
/legacy-flow admin:DiscountController.getDiscountTargets
/legacy-flow admin:DiscountController.getDiscountHistories
/legacy-flow admin:DiscountController.getDiscountUseHistories
/legacy-flow admin:DiscountController.createDiscountPolicy
/legacy-flow admin:DiscountController.updateDiscountPolicy
/legacy-flow admin:DiscountController.updateDiscountPolicyUsageStatus
```
