# ContentPage API Endpoints

## 요약

| 분류 | 개수 |
|------|------|
| Query (조회) | 3개 |
| Command (명령) | 0개 |
| **합계** | **3개** |

---

## Query Endpoints

| # | Method | Path | Controller | Method | UseCase |
|---|--------|------|------------|--------|---------|
| 1 | GET | /api/v1/content/on-display | ContentQueryV1Controller | fetchOnDisplayContents | GetOnDisplayContentPageIdsUseCase |
| 2 | GET | /api/v1/content/meta/{contentId} | ContentQueryV1Controller | fetchContentMetaData | GetContentPageMetaUseCase |
| 3 | GET | /api/v1/content/{contentId} | ContentQueryV1Controller | fetchContent | GetContentPageDetailUseCase |

---

### Q1. fetchOnDisplayContents - 전시 중인 콘텐츠 ID 목록 조회

- **Path**: `GET /api/v1/content/on-display`
- **Controller**: `ContentQueryV1Controller`
- **Request**: 없음 (파라미터 없음)
- **Response**: `V1ApiResponse<OnDisplayContentV1ApiResponse>`
- **UseCase**: `GetOnDisplayContentPageIdsUseCase`

#### Request Parameters

없음. 파라미터 없이 현재 전시 중인 콘텐츠 페이지 ID 목록 전체를 반환한다.

#### Response Fields

**OnDisplayContentV1ApiResponse**

| 필드 | 타입 | 설명 |
|------|------|------|
| contentIds | Set\<Long\> | 전시 중인 콘텐츠 페이지 ID 목록 |

#### Response Structure

```json
{
  "data": {
    "contentIds": [1, 2, 3, 5, 10]
  },
  "success": true,
  "message": null
}
```

#### 처리 흐름

```
1. ContentQueryV1Controller.fetchOnDisplayContents()
   ↓
2. GetOnDisplayContentPageIdsUseCase.execute()
   - 현재 전시 중인 콘텐츠 페이지 ID Set 조회
   ↓
3. Set<Long> contentIds 반환
   ↓
4. ContentV1ApiMapper.toOnDisplayResponse()
   - Set<Long> → OnDisplayContentV1ApiResponse 변환
   ↓
5. ResponseEntity<V1ApiResponse<OnDisplayContentV1ApiResponse>>
```

#### 특이사항

- **경량 조회**: ID만 반환하여 클라이언트가 필요한 콘텐츠만 선택적으로 상세 조회하는 패턴 지원
- **Set 타입 응답**: 중복 없는 ID 집합으로 반환

---

### Q2. fetchContentMetaData - 콘텐츠 메타데이터 조회

- **Path**: `GET /api/v1/content/meta/{contentId}`
- **Controller**: `ContentQueryV1Controller`
- **Request**: `contentId` (@PathVariable)
- **Response**: `V1ApiResponse<ContentMetaV1ApiResponse>`
- **UseCase**: `GetContentPageMetaUseCase`

#### Request Parameters

| 파라미터 | 위치 | 타입 | 필수 | 설명 | 예시 |
|---------|------|------|------|------|------|
| contentId | Path | long | ✅ | 콘텐츠 페이지 ID | `/api/v1/content/meta/1` |

#### Response Fields

**ContentMetaV1ApiResponse**

| 필드 | 타입 | 설명 |
|------|------|------|
| contentId | long | 콘텐츠 ID |
| displayPeriod | DisplayPeriodResponse | 전시 기간 |
| title | String | 콘텐츠 제목 |
| memo | String | 메모 |
| imageUrl | String | 이미지 URL |
| componentDetails | List\<Object\> | 컴포넌트 목록 (메타 조회 시 항상 빈 리스트) |

**DisplayPeriodResponse (중첩)**

| 필드 | 타입 | 설명 |
|------|------|------|
| displayStartDate | String | 전시 시작일 (yyyy-MM-dd HH:mm:ss) |
| displayEndDate | String | 전시 종료일 (yyyy-MM-dd HH:mm:ss) |

#### Response Structure

```json
{
  "data": {
    "contentId": 1,
    "displayPeriod": {
      "displayStartDate": "2026-01-01 00:00:00",
      "displayEndDate": "2026-12-31 23:59:59"
    },
    "title": "메인 콘텐츠",
    "memo": "메인 페이지용 콘텐츠",
    "imageUrl": "https://cdn.example.com/content/1.jpg",
    "componentDetails": []
  },
  "success": true,
  "message": null
}
```

#### 처리 흐름

```
1. ContentQueryV1Controller.fetchContentMetaData(contentId)
   ↓
2. GetContentPageMetaUseCase.execute(contentId)
   - 컴포넌트 없이 콘텐츠 페이지 메타 정보만 조회
   ↓
3. ContentPage 반환
   ↓
4. ContentV1ApiMapper.toMetaResponse()
   - ContentPage → ContentMetaV1ApiResponse 변환
   - componentDetails는 빈 리스트로 고정
   ↓
5. ResponseEntity<V1ApiResponse<ContentMetaV1ApiResponse>>
```

#### 특이사항

- **경량 메타 조회**: 컴포넌트 데이터를 제외한 메타 정보만 조회 (성능 최적화)
- **레거시 호환**: `componentDetails` 필드는 레거시 응답 포맷 호환을 위해 빈 리스트로 포함
- **콘텐츠 존재 여부**: 미존재 contentId 요청 시 UseCase 레이어에서 예외 처리 예상

---

### Q3. fetchContent - 콘텐츠 상세 조회 (컴포넌트 포함)

- **Path**: `GET /api/v1/content/{contentId}`
- **Controller**: `ContentQueryV1Controller`
- **Request**: `contentId` (@PathVariable) + `bypass` (@RequestParam, optional)
- **Response**: `V1ApiResponse<ContentV1ApiResponse>`
- **UseCase**: `GetContentPageDetailUseCase`

#### Request Parameters

| 파라미터 | 위치 | 타입 | 필수 | 설명 | 예시 |
|---------|------|------|------|------|------|
| contentId | Path | long | ✅ | 콘텐츠 페이지 ID | `/api/v1/content/1` |
| bypass | Query | String | ❌ | 전시 기간 체크 우회 여부 (Y: 우회, N: 체크, 기본값: N) | `?bypass=Y` |

#### Response Fields

**ContentV1ApiResponse**

| 필드 | 타입 | 설명 |
|------|------|------|
| contentId | long | 콘텐츠 ID |
| displayPeriod | DisplayPeriodResponse | 전시 기간 |
| title | String | 콘텐츠 제목 |
| memo | String | 메모 |
| imageUrl | String | 이미지 URL |
| componentDetails | List\<ComponentDetailV1ApiResponse\> | 컴포넌트 상세 목록 |

**ComponentDetailV1ApiResponse (중첩)**

| 필드 | 타입 | 설명 |
|------|------|------|
| componentId | long | 컴포넌트 ID |
| componentName | String | 컴포넌트 이름 |
| componentType | String | 컴포넌트 타입 (TEXT, TITLE, IMAGE, BLANK, TAB, BRAND, CATEGORY, PRODUCT) |
| listType | String | 리스트 타입 |
| orderType | String | 정렬 타입 |
| badgeType | String | 뱃지 타입 |
| filterYn | String | 필터 여부 |
| displayYn | String | 전시 여부 |
| component | Object | 컴포넌트 데이터 (타입별 상이, @JsonInclude NON_NULL) |
| viewExtensionDetails | Object | 뷰 확장 상세 (타입별 상이, @JsonInclude NON_NULL) |

#### Response Structure

```json
{
  "data": {
    "contentId": 1,
    "displayPeriod": {
      "displayStartDate": "2026-01-01 00:00:00",
      "displayEndDate": "2026-12-31 23:59:59"
    },
    "title": "메인 콘텐츠",
    "memo": "메인 페이지용 콘텐츠",
    "imageUrl": "https://cdn.example.com/content/1.jpg",
    "componentDetails": [
      {
        "componentId": 10,
        "componentName": "추천 상품",
        "componentType": "PRODUCT",
        "listType": "GRID",
        "orderType": "SCORE",
        "badgeType": "NEW",
        "filterYn": "N",
        "displayYn": "Y",
        "component": { ... },
        "viewExtensionDetails": { ... }
      }
    ]
  },
  "success": true,
  "message": null
}
```

#### 처리 흐름

```
1. ContentQueryV1Controller.fetchContent(contentId, bypass)
   ↓
2. bypass 파라미터 변환
   - "Y".equalsIgnoreCase(bypass) → bypassFlag (boolean)
   ↓
3. ContentPageSearchCriteria 생성
   - new ContentPageSearchCriteria(contentId, bypassFlag)
   ↓
4. GetContentPageDetailUseCase.execute(ContentPageSearchCriteria)
   - 콘텐츠 메타 + 컴포넌트 상세 조회
   - bypassFlag=true 시 전시 기간 체크 우회
   ↓
5. ContentPageDetailResult 반환
   - contentPage, displayComponents, productBundle 포함
   ↓
6. ContentV1ApiMapper.toContentResponse()
   - ContentPage + displayComponents + productBundle
     → ContentV1ApiResponse 변환
   ↓
7. ResponseEntity<V1ApiResponse<ContentV1ApiResponse>>
```

#### 특이사항

- **bypass 플래그**: 관리자 미리보기 등 전시 기간 외 조회가 필요한 경우 `bypass=Y` 사용
- **컴포넌트 타입별 응답**: `component`, `viewExtensionDetails` 필드는 컴포넌트 타입에 따라 구조가 달라짐 (@JsonInclude NON_NULL로 null 필드 제외)
- **ProductBundle 포함**: 상품형 컴포넌트(PRODUCT, BRAND, CATEGORY)의 경우 `productBundle`에서 상품 데이터를 조합
- **Q2와의 차이**: Q2(meta)는 컴포넌트 제외 경량 조회, Q3(detail)은 컴포넌트 포함 전체 조회

---

## Command Endpoints

현재 ContentPage 도메인에는 Command 엔드포인트가 구현되어 있지 않습니다.

---

## 아키텍처 매핑

### Hexagonal Architecture Layer 흐름

```
[Adapter-In] ContentQueryV1Controller
    ↓
[Adapter-In] ContentV1ApiMapper (Domain → Response DTO 변환)
    ↓
[Application] GetOnDisplayContentPageIdsUseCase (Port-In)  ← Q1
[Application] GetContentPageMetaUseCase        (Port-In)  ← Q2
[Application] GetContentPageDetailUseCase      (Port-In)  ← Q3
    ↓
[Domain] ContentPage / ContentPageDetailResult (Domain 객체)
```

### UseCase 별 입출력 타입

| UseCase | 입력 | 출력 |
|---------|------|------|
| `GetOnDisplayContentPageIdsUseCase` | 없음 | `Set<Long>` |
| `GetContentPageMetaUseCase` | `long contentPageId` | `ContentPage` |
| `GetContentPageDetailUseCase` | `ContentPageSearchCriteria` | `ContentPageDetailResult` |

### CQRS 패턴 적용

- **Query Side**: `ContentQueryV1Controller` - 조회 전용, 3개의 UseCase를 목적별로 분리하여 의존
- **Command Side**: 미구현
- **레거시 호환**: `/api/v1/content` 경로는 기존 ContentController 경로와 동일하게 유지

---

## 문서 생성 정보

- **분석 일시**: 2026-03-14
- **대상 모듈**: `adapter-in/rest-api` (v1)
- **대상 패키지**: `com.ryuqq.setof.adapter.in.rest.v1.content`
- **컨트롤러 파일**: `ContentQueryV1Controller.java`
- **엔드포인트 Base**: `/api/v1/content`
